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

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import de.tud.dnets2.global.Constants;
import de.tud.dnets2.global.Settings;
import de.tud.dnets2.model.Client;
import de.tud.dnets2.model.ContentProvider;
import de.tud.dnets2.model.Entity;
import de.tud.dnets2.model.EntityId;
import de.tud.dnets2.model.ServiceChain;
import de.tud.dnets2.model.ServiceChainInstance;
import de.tud.dnets2.model.ServiceFunction;
import de.tud.dnets2.model.ServiceInstance;
import de.tud.dnets2.server.ServiceFunctionChainingController;

/**
 * REST server resource for managing <code>Client</code>s.
 * 
 * @author Victor-Philipp Negoescu
 * @version 2.0
 */
public class ClientRes extends EntityRes<Client> {

	private static int clientIndex = 0;
	
	@Override
	protected String getEntitySingleResponseKey() {
		return Constants.REST_KEY_CLIENT;
	}

	@Override
	public Class<Client> getEntityClass() {
		return Client.class;
	}

	@Override
	protected String getEntityListResponseKey() {
		return Constants.REST_KEY_CLIENTS;
	}

	@Override
	protected Entity internalHandlePut(JSONObject query) throws Exception {
		String clientMAC = query.getString(Constants.REST_QUERY_MAC);
		
		// Check client MAC (do not check Clients since this is done later)
		if (! checkUniqueMAC(clientMAC, getEntityClass(), null, true, true, false, true))
			return null;
		
		Client c = new Client(new EntityId(query.getString(Constants.REST_QUERY_MAC)));
		c.setIndex(clientIndex++);
		c.setIP(query.getString(Constants.REST_QUERY_IP));
		// old settings
//		c.setSwitchMAC(query.getString(Constants.REST_QUERY_SWITCH_MAC));
		// new settings
		c.setSwitchMAC(String.valueOf(Long.parseLong(Settings.INGRESS_SWITCH_MAC,16)));
		
		c.setSwitchInterface(Settings.INGRESS_SWITCH_INTERFACE);
		//c.setSwitchInterface(query.getString(Constants.REST_QUERY_SWITCH_INTERFACE));
		c.setEntryHopMAC(query.getString(Constants.REST_QUERY_ENTRY_HOP_MAC));
		if (query.has(Constants.REST_QUERY_HOSTNAME))
			c.setHostname(query.getString(Constants.REST_QUERY_HOSTNAME));
		
		return c;
	}
	
	@Override
	protected boolean internalHandlePut(Client e, JSONObject query) throws Exception {
		if (query.has(Constants.REST_QUERY_IP))
			e.setIP(query.getString(Constants.REST_QUERY_IP));
		if (query.has(Constants.REST_QUERY_HOSTNAME))
			e.setHostname(query.getString(Constants.REST_QUERY_HOSTNAME));
		if (query.has(Constants.REST_QUERY_ENTRY_HOP_MAC))
			e.setEntryHopMAC(query.getString(Constants.REST_QUERY_ENTRY_HOP_MAC));
		return true;
	}

	@Override
	protected boolean internalHandleDelete(EntityId id) {
		Client c = ServiceFunctionChainingController.getEntityStorage().getById(getEntityClass(), id);
		if (c == null) {
			// Client not found
			setLastError(Constants.REST_ERROR_INVALID_ENTITY);
			return false;
		}
		
		if (getChainRouter() != null && ! getChainRouter().removeChain(c)) {
			// Routing error
			setLastError(Constants.REST_ERROR_ROUTING_ERROR);
			return false;
		}
		
		if (c.getServiceChainInstance() != null) {		
			// remove assigned Service Chain Instance (SCI) if it is not assigned to other users
			List<Client> cs = ServiceFunctionChainingController.getEntityStorage().list(Client.class);
			boolean assigned = false; // SCI assigned to other client(s)?
			for (Client oc : cs) {
				if (oc.getId().equals(c.getId())) // Skip current client
					continue;
				
				if (oc.getServiceChainInstance() != null && oc.getServiceChainInstance().getId().equals(c.getServiceChainInstance().getId())) {
					assigned = true;
					break;
				}
			}
			
			if (! assigned) {
				// Free Service Instances
				for (ServiceInstance si : c.getServiceChainInstance().getServiceInstances()) {
					si.setFree(true);
				}
				
				// Remove SCI
				if (! ServiceFunctionChainingController.getEntityStorage().deleteById(ServiceChainInstance.class, c.getServiceChainInstance().getId())) {
					// this should never happen
					setLastError(Constants.REST_ERROR_INTERNAL_ERROR);
					setLastInfo("Could not delete ServiceChainInstance though it should exist.");
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	protected boolean internalHandlePost(String operation, Client e, JSONObject query) throws Exception {
		if (operation.equals("assignToServiceChain")) {
			return assignToServiceChain(e, query);
		} else {
			setLastError(Constants.REST_ERROR_INVALID_OPERATION);
			return false;
		}
	}

	// TODO protected
	public boolean assignToServiceChain(Client c, JSONObject query)
			throws Exception {
		// Fetch the Content Provider
		ContentProvider cp = ServiceFunctionChainingController.getEntityStorage().getById(ContentProvider.class, new EntityId(query.getString(Constants.REST_QUERY_CONTENTPROVIDER_MAC)));
		if (cp == null) {
			// Content Provider not found
			setLastError(Constants.REST_ERROR_INVALID_CONTENTPROVIDER);
			return false;
		}
		
		// Do the real work
		if (c.getServiceChainInstance() != null && getChainRouter() != null && ! getChainRouter().removeChain(c)) {
			// Routing error
			setLastError(Constants.REST_ERROR_ROUTING_ERROR);
			return false;
		}
		
		// Remember the client's current Service Chain Instances
		List<EntityId> clientServiceInstaneIds = new ArrayList<EntityId>();
		ServiceChainInstance csci = c.getServiceChainInstance(); // old Service Chain Instance
		
		// Release the client's current Service Chain Instance (if existent)
		if (c.getServiceChainInstance() != null) {
			// Free the Service Instances
			for (ServiceInstance si : c.getServiceChainInstance().getServiceInstances()) {
				clientServiceInstaneIds.add(si.getId());
				si.setFree(true);
			}
			
			// Delete the Service Chain Instance
			if (! ServiceFunctionChainingController.getEntityStorage().deleteById(ServiceChainInstance.class, c.getServiceChainInstance().getId())) {
				// This should never happen
				setLastError(Constants.REST_ERROR_INTERNAL_ERROR);
				setLastInfo("Could not delete Service Chain Instance.");
				
				// Revert
				for (ServiceInstance si : c.getServiceChainInstance().getServiceInstances()) {
					si.setFree(false);
				}
				
				return false;
			}
			
			c.setServiceChainInstance(null);
		}
		
		ServiceChain sc = ServiceFunctionChainingController.getEntityStorage().getById(ServiceChain.class, new EntityId(query.getString(Constants.REST_QUERY_SERVICECHAIN_ID)));
		if (sc == null) {
			// Requested Service Chain not found
			setLastError(Constants.REST_ERROR_INVALID_SERVICECHAIN);
			
			// Revert
			if (csci != null)
				revertServiceChain(c, clientServiceInstaneIds, csci);
			return false;
		}
		
		// Find free Service Instances which realize the desired Service Functions
		List<ServiceInstance> sis = ServiceFunctionChainingController.getEntityStorage().list(ServiceInstance.class);
		List<ServiceInstance> clientServiceInstances = new ArrayList<ServiceInstance>(); // Reserved Service Instances for this client
		for (ServiceFunction sf : sc.getServiceFunctions()) {
			ServiceInstance freeInstance = null; // Free Service Instance realizing this Service Function
			for (ServiceInstance si : sis) {
				if (si.getServiceFunction().equals(sf) && si.isFree()) {
					freeInstance = si;
					break;
				}
			}
			
			if (freeInstance == null) {
				// No free Service Instance for this Service Function
				setLastError(Constants.REST_ERROR_INSUFFICIENT_SERVICEINSTANCES);
				
				// Revert
				revertServiceChain(c, clientServiceInstaneIds, csci);
				return false;
			} else {
				// Reserve instance for this client
				freeInstance.setFree(false);
				clientServiceInstances.add(freeInstance);
			}
		}
		
		ServiceChainInstance sci = new ServiceChainInstance();
		sci.setServiceInstances(clientServiceInstances);
		sci.setServiceChain(sc);
		sci.setContentProvider(cp);
		
		// Insert the Service Chain Instance
		if (ServiceFunctionChainingController.getEntityStorage().insert(sci)) {
			// Update client
			c.setServiceChainInstance(sci);
			
			// Do the real work
			if (getChainRouter() != null && ! getChainRouter().initChain(c)) {
				// Routing error
				setLastError(Constants.REST_ERROR_ROUTING_ERROR);
				return false;
			}
			
			return true;
		} else {
			// should never happen since Service Chain Instance ids are automatically generated
			setLastError(Constants.REST_ERROR_INTERNAL_ERROR);
			setLastInfo("Could not insert Service Chain Instance.");
			return false;
		}
	}

	/**
	 * Reverts the Client's Service Chain to its original state
	 * 
	 * @param e Client
	 * @param clientServiceInstaneIds List of old Service Instance ids of Client
	 * @param csci Old Service Chain Instance of Client
	 * @return
	 */
	private void revertServiceChain(Client e,
			List<EntityId> clientServiceInstaneIds, ServiceChainInstance csci) {
		for (EntityId siId : clientServiceInstaneIds) {
			ServiceInstance si = ServiceFunctionChainingController.getEntityStorage().getById(ServiceInstance.class, siId);
			if (si == null) {
				setLastError(Constants.REST_ERROR_INTERNAL_ERROR);
				setLastInfo("Could not find current Service Instance.");
				return;
			} else {
				si.setFree(false);
			}
		}
		e.setServiceChainInstance(csci);
		
		// readd Service Chain Instance
		ServiceFunctionChainingController.getEntityStorage().insert(csci);
	}
	
}
