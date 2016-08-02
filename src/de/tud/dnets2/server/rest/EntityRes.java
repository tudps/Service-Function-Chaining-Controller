/**
 * This file is part of the program/application/library Service Function Chaining Controller (SFCC), a component of the Software-Defined Network Service Chaining Demonstrator.
 *
 * © 2015 Negoescu, Victor and Blendin, Jeremias
 *
 * This program is licensed under the Apache-2.0 License (http://opensource.org/licenses/Apache-2.0). See also file LICENSING
 *
 * Development sponsored by Deutsche Telekom AG [ opensource@telekom.de<mailto:opensource@telekom.de> ]
 * 
 * Modificationsections:
 * 1. 2014, Negoescu, Victor-Phillipp: Initial Release.
 */
package de.tud.dnets2.server.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import de.tud.dnets2.global.Constants;
import de.tud.dnets2.global.JSON;
import de.tud.dnets2.model.Client;
import de.tud.dnets2.model.ContentProvider;
import de.tud.dnets2.model.Entity;
import de.tud.dnets2.model.EntityId;
import de.tud.dnets2.model.ServiceInstance;
import de.tud.dnets2.model.ServiceNode;
import de.tud.dnets2.server.ChainRouter;
import de.tud.dnets2.server.ServiceFunctionChainingController;

/**
 * Generic REST server resource for managing <code>Entity</code>s.
 * 
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public abstract class EntityRes<T extends Entity> extends ServerResource {

	/** Last occurred error */
	protected String lastError = "";
	
	/** Last info */
	protected String lastInfo = "";
	
	/**
	 * Defines the controlled entity class used in the SFCC.
	 * @return Class of the controlled entity
	 */
	public abstract Class<T> getEntityClass();
	
	/**
	 * Specifies the JSON object key where the requested resource
	 * should be attached to.
	 * @return JSON single entity response key
	 */
	protected abstract String getEntitySingleResponseKey();
	
	/**
	 * Specifies the JSON object key where the requested list
	 * of resources should be attached to.
	 * @return JSON entity list response key
	 */
	protected abstract String getEntityListResponseKey();
	
	/**
	 * Internal handler for a REST PUT command where a resource
	 * should be inserted.
	 * @return Entity to be inserted, <i>null</i> if an error occurred.
	 * In this case, a call to <code>getLastError()</code> and optionally to
	 * <code>getLastInfo()</code> should give details of the occurred error.
	 */
	protected abstract Entity internalHandlePut(JSONObject query) throws Exception;
	
	/**
	 * Internal handler for a REST PUT command where a resource
	 * should be updated.
	 * @param e the entity to be updated
	 * @return <i>true</i> if the update was successful, <i>false</i> otherwise
	 */
	protected abstract boolean internalHandlePut(T e, JSONObject query) throws Exception;
	
	/**
	 * Internal handler for a REST POST command where a custom operation
	 * should be executed on the resource.
	 * @param e the entity which the operation should be executed on
	 * @return <i>true</i> if the operation was successful, <i>false</i> otherwise
	 */
	protected abstract boolean internalHandlePost(String operation, T e, JSONObject query) throws Exception;
	
	/**
	 * Internal handler for a REST DELETE command where a resource
	 * should be removed.
	 * @param id id of the entity to be deleted
	 * @return <i>true</i> if the entity can be deleted, <i>false</i> otherwise.
	 * In this case, a call to <code>getLastError()</code> and optionally to
	 * <code>getLastInfo()</code> should give details of the occurred error.
	 */
	protected abstract boolean internalHandleDelete(EntityId id) throws Exception;
	
	/**
	 * Internal event which is fired after a parameterless REST GET command
	 * has been executed. 
	 * @throws Exception
	 */
	protected void onAfterGet() throws Exception {
		return;
	}
	
	/**
	 * REST GET request. Returns the resource with a given id or
	 * lists all controlled resources.
	 * @return
	 * @throws Exception
	 */
	@Get("json")
	public String handleGet() {
		try {
			synchronized (ServiceFunctionChainingController.restLock) { // Synchronize for consistent storage
				JSONObject sysInitErr = checkSystemInit();
				if (sysInitErr != null)
					return sysInitErr.toString();
				
				if (getRequestAttributes().containsKey("id")) {
					// GET single entity
					EntityId id = new EntityId((String) getRequestAttributes().get("id"));
					try {
						return jsonGetById(getEntitySingleResponseKey(), id).toString();
					} catch (Exception e) {
						JSONObject ret = new JSONObject();
						ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
						ret.put(Constants.REST_KEY_ERROR, Constants.REST_ERROR_INTERNAL_ERROR);
						ret.put(Constants.REST_KEY_INFO, e.getMessage());
						return ret.toString();
					}
				} else {
					// LIST all entities
					String ret = jsonList(getEntityListResponseKey()).toString();
					onAfterGet();
					return ret;
				}
			}
		} catch (Exception e) {
			// should really never happen
			e.printStackTrace();
			return buildExceptionJSON(e);
		}
	}

	/**
	 * REST PUT request. Registers a new resource or update an existing one.
	 * @return
	 */
	@Put("json")
	public String handlePut(String q) {
		try {
			synchronized (ServiceFunctionChainingController.restLock) { // Synchronize for consistent storage
				JSONObject sysInitErr = checkSystemInit();
				if (sysInitErr != null && !(this instanceof ServiceNodeRes) && !(this instanceof ServiceInstanceRes))
					return sysInitErr.toString();
				
				JSONObject ret = new JSONObject();
				
				JSONObject query;
				try {
					query = new JSONObject(q);
				} catch (Exception e) {
					ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
					ret.put(Constants.REST_KEY_ERROR, Constants.REST_ERROR_INVALID_QUERY);
					ret.put(Constants.REST_KEY_INFO, buildExceptionJSON(e));
					return ret.toString();
				}
				
				if (getRequestAttributes().containsKey("id")) {
					// UPDATE entity
					EntityId id = new EntityId((String) getRequestAttributes().get("id"));
					T e = ServiceFunctionChainingController.getEntityStorage().getById(getEntityClass(), id);
					if (e == null) {
						// The entity has not been found
						ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
						ret.put(Constants.REST_KEY_ERROR, Constants.REST_ERROR_INVALID_ENTITY);
					} else {
						// The entity can be updated
						if (internalHandlePut(e, query)) {
							// The update was successful
							if (query.has(Constants.REST_QUERY_TAG))
								e.setTag(query.getString(Constants.REST_QUERY_TAG));
							ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_OK);
						} else {
							// The update went wrong
							ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
							ret.put(Constants.REST_KEY_ERROR, Constants.REST_ERROR_INVALID_ENTITY);
						}
					}
				} else {
					// INSERT entity
					Entity e = internalHandlePut(query);
					if (e == null) {
						// An error has happened, return it
						ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
						ret.put(Constants.REST_KEY_ERROR, getLastError());
						ret.put(Constants.REST_KEY_INFO, getLastInfo());
					} else {
						// All went fine, insert the entity
						if (query.has(Constants.REST_QUERY_TAG))
							e.setTag(query.getString(Constants.REST_QUERY_TAG));
						if (ServiceFunctionChainingController.getEntityStorage().insert(e)) {
							ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_OK);
							ret.put(Constants.REST_KEY_ID, e.getId().toString());
						} else {
							// Entity is already registered
							ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
							ret.put(Constants.REST_KEY_ERROR, Constants.REST_ERROR_ALREADY_REGISTERED);
						}
					}
				}
				return ret.toString();
			}
		} catch (Exception e) {
			// can happen if e.g. insufficient query parameters were supplied
			e.printStackTrace();
			return buildExceptionJSON(e);
		}	
	}
	
	/**
	 * REST DELETE request. Deletes a resource.
	 * @return
	 */
	@Delete("json")
	public String handleDelete() {
		try {
			synchronized (ServiceFunctionChainingController.restLock) { // Synchronize for consistent storage
				JSONObject sysInitErr = checkSystemInit();
				if (sysInitErr != null)
					return sysInitErr.toString();
				
				JSONObject ret = new JSONObject();
				if (getRequestAttributes().containsKey("id")) {
					EntityId id = new EntityId((String) getRequestAttributes().get("id"));
					if (internalHandleDelete(id)) {
						if (! ServiceFunctionChainingController.getEntityStorage().deleteById(getEntityClass(), id)) {
							// not found
							ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
							ret.put(Constants.REST_KEY_ERROR, Constants.REST_ERROR_INVALID_ENTITY);
						} else {
							ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_OK);
						}
					} else {
						// Entity may not be deleted
						ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
						ret.put(Constants.REST_KEY_ERROR, getLastError());
						ret.put(Constants.REST_KEY_INFO, getLastInfo());
					}
				} else {
					// No id specified
					ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
					ret.put(Constants.REST_KEY_ERROR, Constants.REST_ERROR_INVALID_ENTITY);
				}
				return ret.toString();
			}
		} catch (Exception e) {
			// should really never happen
			e.printStackTrace();
			return buildExceptionJSON(e);
		}	
	}
	
	/**
	 * REST POST request. Allows custom operations on a given entity.
	 * @return
	 */
	@Post("json")
	public String handlePost(String q) {
		try {
			synchronized (ServiceFunctionChainingController.restLock) { // Synchronize for consistent storage
				JSONObject sysInitErr = checkSystemInit();
				if (sysInitErr != null)
					return sysInitErr.toString();
				
				JSONObject ret = new JSONObject();
				
				JSONObject query;
				try {
					query = new JSONObject(q);
				} catch (Exception e) {
					ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
					ret.put(Constants.REST_KEY_ERROR, Constants.REST_ERROR_INVALID_QUERY);
					return ret.toString();
				}
				
				if (getRequestAttributes().containsKey("id")) {
					if (! getRequestAttributes().containsKey("operation")) {
						// No operation given
						ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
						ret.put(Constants.REST_KEY_ERROR, Constants.REST_ERROR_INVALID_OPERATION);
					} else {
						EntityId id = new EntityId((String) getRequestAttributes().get("id"));
						String operation = (String) getRequestAttributes().get("operation");
						T e = ServiceFunctionChainingController.getEntityStorage().getById(getEntityClass(), id);
						if (e == null) {
							// The entity has not been found
							ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
							ret.put(Constants.REST_KEY_ERROR, Constants.REST_ERROR_INVALID_ENTITY);
						} else {
							// The operation can be executed
							if (internalHandlePost(operation, e, query)) {
								// Operation successful
								ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_OK);
								ret.put(Constants.REST_KEY_INFO, getLastInfo());
							} else {
								// Operation not successful
								ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
								ret.put(Constants.REST_KEY_ERROR, getLastError());
								ret.put(Constants.REST_KEY_INFO, getLastInfo());
							}
						}
					}
				} else {
					// No id specified
					ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
					ret.put(Constants.REST_KEY_ERROR, Constants.REST_ERROR_INVALID_ENTITY);
				}
				return ret.toString();
			}
		} catch (Exception e) {
			// should really never happen
			e.printStackTrace();
			return buildExceptionJSON(e);
		}	
	}
	
	/**
	 * Builds a JSON response containing a list of all controlled entities.
	 * @param listKey JSON object key where the entities should be put
	 * @return JSON response containing a list of all controlled entities
	 */
	protected JSONObject jsonList(String listKey) throws Exception {
		JSONObject ret = new JSONObject();
		ret.put(listKey, JSON.buildJSON(ServiceFunctionChainingController.getEntityStorage().list(getEntityClass())));
		ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_OK);
		return ret;
	}
	
	/**
	 * Builds a JSON response containing the requested entity.
	 * @param entityKey JSON object key where the requested entity should be attached to 
	 * @param id Id of the requested entity
	 * @return JSON response containing the requested entity
	 * @throws Exception
	 */
	protected JSONObject jsonGetById(String entityKey, EntityId id) throws Exception {
		JSONObject ret = new JSONObject();
		Entity e = ServiceFunctionChainingController.getEntityStorage().getById(getEntityClass(), id);
		if (e == null) {
			// Entity not found
			ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
			ret.put(Constants.REST_KEY_ERROR, Constants.REST_ERROR_INVALID_ENTITY);
		} else {
			// Entity found
			ret.put(entityKey, JSON.buildJSON(e));
			ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_OK);	
		}
		return ret;
	}
	
	/**
	 * Builds a JSON response for a given Exception
	 * @param e The exception
	 * @return JSON-formatted exception
	 */
	protected String buildExceptionJSON(Exception e) {
		String exceptionMessage = e.toString();
		for (StackTraceElement ste : e.getStackTrace())
			exceptionMessage += " -- " + ste.toString();
		
		exceptionMessage = exceptionMessage.replaceAll("\"", "\\\\\""); // Escape quotes
		
		return "{\"" + Constants.REST_KEY_STATUS + "\" : \"" + Constants.REST_STATUS_FAIL + "\", \"" + Constants.REST_KEY_ERROR + "\" : \"" + Constants.REST_ERROR_INTERNAL_ERROR + "\", \"" + Constants.REST_KEY_INFO + "\" : \"" + exceptionMessage + "\"}";
	}
	
	/**
	 * Checks if a given MAC is already in use in the system
	 * @param mac MAC to check
	 * @param cls entity class of the MAC to be checked
	 * @param id entity id of the MAC to be checked
	 * @param checkSNs whether to check Service Nodes
	 * @param checkSIs whether to check Service Instances
	 * @param checkCs whether to check Clients
	 * @param checkCPs whether to check Content Providers
	 * @return <i>true</i> if the MAC is unique, <i>false</i> otherwise
	 * @throws Exception 
	 */
	protected boolean checkUniqueMAC(String mac, Class<T> cls, EntityId id, boolean checkSNs, boolean checkSIs, boolean checkCs, boolean checkCPs) throws Exception {
		ServiceNode sn = null;
		ServiceInstance si = null;
		Client c = null;
		ContentProvider cp = null;
		
		if (checkSNs) {
			// Check Service Nodes
			List<ServiceNode> sns = ServiceFunctionChainingController.getEntityStorage().list(ServiceNode.class);
			for (ServiceNode osn : sns) {
				if (osn.getSwitchMAC().equals(mac)) {
					if (cls.equals(ServiceNode.class) && id != null && osn.getId().equals(id))
						continue; // Ignore MAC of same entity
					sn = osn;
					break;
				}
			}
		}
		
		if (checkSIs && sn == null) {
			// Check Service Instances
			List<ServiceInstance> sis = ServiceFunctionChainingController.getEntityStorage().list(ServiceInstance.class);
			for (ServiceInstance osi : sis) {
				if (osi.getEgressMAC().equals(mac) || osi.getIngressMAC().equals(mac)) {
					if (cls.equals(ServiceInstance.class) && id != null && osi.getId().equals(id))
						continue; // Ignore MAC of same entity
					si = osi;
					break;
				}
			}
		}
		
		if (checkCs && sn == null && si == null) {
			// Check Clients
			List<Client> cs = ServiceFunctionChainingController.getEntityStorage().list(Client.class);
			for (Client oc : cs) {
				if (oc.getMAC().equals(mac)) {
					if (cls.equals(Client.class) && id != null && oc.getId().equals(id))
						continue; // Ignore MAC of same entity
					c = oc;
					break;
				}
			}
		}
		
		if (checkCPs && sn == null && si == null && c == null) {
			// Check Content Providers
			List<ContentProvider> cps = ServiceFunctionChainingController.getEntityStorage().list(ContentProvider.class);
			for (ContentProvider ocp : cps) {
				if (ocp.getMAC().equals(mac)) {
					if (cls.equals(ContentProvider.class) && id != null && ocp.getId().equals(id))
						continue; // Ignore MAC of same entity
					cp = ocp;
					break;
				}
			}
		}
		
		if (sn != null || si != null || c != null || cp != null) {
			setLastError(Constants.REST_ERROR_DUPLICATE_MAC);
			Map<String, String> info = new HashMap<String, String>();
			info.put("mac", mac);
			if (sn != null) {
				info.put("entityType", "ServiceNode");
				info.put("entityId", sn.getId().toString());
			} else if (si != null) {
				info.put("entityType", "ServiceInstance");
				info.put("entityId", si.getId().toString());
			} else if (c != null) {
				info.put("entityType", "Client");
				info.put("entityId", c.getId().toString());
			} else {
				info.put("entityType", "ContentProvider");
				info.put("entityId", cp.getId().toString());
			}
			setLastInfo(JSON.buildJSON(info).toString().replaceAll("\"", "\\\""));
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the system is ready.
	 * 
	 * @return null, if the system is ready.
	 * @throws JSONException
	 */
	protected JSONObject checkSystemInit() throws JSONException {
		// check if system is ready
		boolean ready = true;
		if (ServiceFunctionChainingController.getEntityStorage().list(ServiceNode.class).size() < 2) {
			ready = false;
		}
		for (ServiceNode sn : ServiceFunctionChainingController.getEntityStorage().list(ServiceNode.class)) {
			if(! sn.isReady()) {
				ready = false;
				break;
			}
		}
		
		if (! ready) {
			JSONObject ret = new JSONObject();
			ret.put(Constants.REST_KEY_STATUS, Constants.REST_STATUS_FAIL);
			ret.put(Constants.REST_KEY_ERROR, Constants.REST_ERROR_SYSTEM_INIT);
			return ret;
		} else
			return null;
	}

	public String getLastError() {
		String ret = lastError;
		lastError = ""; // Reset
		return ret;
	}

	public void setLastError(String lastError) {
		this.lastError = lastError;
	}

	public String getLastInfo() {
		String ret = lastInfo;
		lastInfo = ""; // Reset
		return ret;
	}

	public void setLastInfo(String lastInfo) {
		this.lastInfo = lastInfo;
	}
	
	protected ChainRouter getChainRouter() {
		return ((ServiceFunctionChainingController)getApplication()).getChainRouter();
	}
}
