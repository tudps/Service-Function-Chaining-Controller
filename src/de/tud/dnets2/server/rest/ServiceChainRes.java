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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import de.tud.dnets2.global.Constants;
import de.tud.dnets2.model.Client;
import de.tud.dnets2.model.Entity;
import de.tud.dnets2.model.EntityId;
import de.tud.dnets2.model.ServiceChain;
import de.tud.dnets2.model.ServiceChainInstance;
import de.tud.dnets2.model.ServiceFunction;
import de.tud.dnets2.model.ServiceInstance;
import de.tud.dnets2.server.ServiceFunctionChainingController;

/**
 * REST server resource for managing <code>ServiceChain</code>s.
 * 
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class ServiceChainRes extends EntityRes<ServiceChain> {
	
	@Override
	public Class<ServiceChain> getEntityClass() {
		return ServiceChain.class;
	}

	@Override
	protected String getEntitySingleResponseKey() {
		return Constants.REST_KEY_SERVICECHAIN;
	}

	@Override
	protected String getEntityListResponseKey() {
		return Constants.REST_KEY_SERVICECHAINS;
	}

	@Override
	protected Entity internalHandlePut(JSONObject query) throws Exception {
		JSONArray serviceFunctionIds = query.getJSONArray(Constants.REST_QUERY_SERVICEFUNCTION_IDS);
		
		ServiceChain sc = new ServiceChain();
		List<ServiceFunction> sfs = new ArrayList<ServiceFunction>();
		for (int i = 0; i < serviceFunctionIds.length(); i++) {
			String sfId = serviceFunctionIds.getString(i);
			ServiceFunction sf = ServiceFunctionChainingController.getEntityStorage().getById(ServiceFunction.class, new EntityId(sfId));
			if (sf == null) {
				// Invalid Service Function
				setLastError(Constants.REST_ERROR_INVALID_SERVICEFUNCTION);
				setLastInfo(sfId);
				return null;
			} else
				sfs.add(sf);
		}
		
		sc.setServiceFunctions(sfs);
		return sc;
	}

	@Override
	protected boolean internalHandleDelete(EntityId id) {
		// Check if there are active Service Chain Instances
		for (ServiceChainInstance sci : ServiceFunctionChainingController.getEntityStorage().list(ServiceChainInstance.class)) {
			if (sci.getServiceChain().getId().equals(id)) {
				setLastError(Constants.REST_ERROR_HAS_SERVICECHAININSTANCES);
				return false;
			}
		}
		
		return true;
	}

	@Override
	protected boolean internalHandlePut(ServiceChain e, JSONObject query) throws Exception {
		if (query.has(Constants.REST_QUERY_TAG))
			e.setTag(query.getString(Constants.REST_QUERY_TAG));
		return true;
	}
	
	@Override
	protected boolean internalHandlePost(String operation,
			ServiceChain e, JSONObject query) throws Exception {
		if (operation.equals("addServiceFunction")) {
			// add a Service Function to an existing Service Chain
			EntityId serviceFunctionId = new EntityId(query.getString(Constants.REST_QUERY_SERVICEFUNCTION_ID));
			ServiceFunction serviceFunction = ServiceFunctionChainingController.getEntityStorage().getById(ServiceFunction.class, serviceFunctionId);
			
			if (serviceFunction == null) {
				// Service Function not found
				setLastError(Constants.REST_ERROR_INVALID_SERVICEFUNCTION);
				return false;
			}
			
			int index = query.getInt(Constants.REST_QUERY_INDEX);
			// Boundary check
			if (index < 0 || index > e.getServiceFunctions().size()) {
				setLastError(Constants.REST_ERROR_INDEX_OUT_OF_BOUNDS);
				return false;
			}
			
			for (ServiceFunction sf : e.getServiceFunctions()) {
				if (sf.equals(serviceFunction)) {
					setLastError(Constants.REST_ERROR_SERVICEFUNCTION_ALREADY_CONTAINED);
					return false;
				}
			}
			
			return addServiceFunction(e, serviceFunction, index);
		} else if (operation.equals("removeServiceFunction")) {
			// Remove a Service Function from a Service Chain
			
			int index = query.getInt(Constants.REST_QUERY_INDEX);
			// Boundary check
			if (index < 0 || index >= e.getServiceFunctions().size()) {
				setLastError(Constants.REST_ERROR_INDEX_OUT_OF_BOUNDS);
				return false;
			}
			
			return removeServiceFunction(e, index);
		} else {
			setLastError(Constants.REST_ERROR_INVALID_OPERATION);
			return false;
		}
	}
	
	/**
	 * Removes a Service Function from a Service Chain all appropriate Service Instances
	 * from the Service Chain Instances. 
	 * 
	 * @param sc Service Chain to be modified
	 * @param index index at which the Service Function is to be deleted
	 * @return success of the operation
	 */
	private boolean removeServiceFunction(ServiceChain sc, int index) {
		// Remove the Service Function from the Service Chain
		sc.getServiceFunctions().remove(index);
		
		Map<ServiceChainInstance, Client> serviceChainInstanceToClient = getServiceChainInstanceToClient();
		
		// Find all Service Chain Instances that realize the given Service Chain and delete the appropriate Service Instances
		for (ServiceChainInstance sci : ServiceFunctionChainingController.getEntityStorage().list(ServiceChainInstance.class)) {
			if (sci.getServiceChain().equals(sc)) {
				ServiceInstance si = sci.getServiceInstances().get(index);
				si.setFree(true);
				sci.getServiceInstances().remove(index);
				
				// Do the real work
				if (getChainRouter() != null &&
						serviceChainInstanceToClient.containsKey(sci) &&
						! getChainRouter().updateChain(serviceChainInstanceToClient.get(sci))) {
					setLastError(Constants.REST_ERROR_ROUTING_ERROR);
					return false;
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Adds a Service Function to a Service Chain and the appropriate Service Instances to the Service Chain Instances 
	 * 
	 * @param sc Service Chain to be modified
	 * @param sf Service Function to be added
	 * @param index index at which the Service Function should be added
	 * @return success of the operation
	 */
	private boolean addServiceFunction(ServiceChain sc, ServiceFunction sf, int index) {
		// Find all Service Chain Instances that realize the given Service Chain and count them
		List<ServiceChainInstance> affectedServiceChainInstances = new ArrayList<ServiceChainInstance>();
		for (ServiceChainInstance sci : ServiceFunctionChainingController.getEntityStorage().list(ServiceChainInstance.class)) {
			if (sci.getServiceChain().equals(sc)) {
				affectedServiceChainInstances.add(sci);
			}
		}
		
		// Collect free Service Instances which realize the desired Service Function
		List<ServiceInstance> freeServiceInstances = new ArrayList<ServiceInstance>();
		for (ServiceInstance osi : ServiceFunctionChainingController.getEntityStorage().list(ServiceInstance.class)) {
			if (osi.getServiceFunction().equals(sf) && osi.isFree())
				freeServiceInstances.add(osi);
		}
		
		// Compare needed and free Service Instances
		if (freeServiceInstances.size() < affectedServiceChainInstances.size()) {
			setLastError(Constants.REST_ERROR_INSUFFICIENT_SERVICEINSTANCES);
			return false;
		}
		
		Map<ServiceChainInstance, Client> serviceChainInstanceToClient = getServiceChainInstanceToClient();
		
		// Pre-check completed, do the changes.
		sc.getServiceFunctions().add(index, sf);
		int i = 0;
		for (ServiceChainInstance affectedServiceChainInstance : affectedServiceChainInstances) {
			ServiceInstance si = freeServiceInstances.get(i);
			si.setFree(false); // mark as used
			si.setParameterValues(si.getServiceFunction().getDefaultParameterValues()); // set parameters to default
			
			affectedServiceChainInstance.getServiceInstances().add(index, si);
			
			// Do the real work
			if (getChainRouter() != null &&
					serviceChainInstanceToClient.containsKey(affectedServiceChainInstance) &&
					! getChainRouter().updateChain(serviceChainInstanceToClient.get(affectedServiceChainInstance))) {
				setLastError(Constants.REST_ERROR_ROUTING_ERROR);
				return false;
			}
			
			i++;
		}
		
		return true;
	}
	
	/**
	 * Creates a mapping of Service Chain Instances to Clients
	 * @return the mapping
	 */
	private Map<ServiceChainInstance, Client> getServiceChainInstanceToClient() {
		// Mapping of Service Chain Instance to Client
		Map<ServiceChainInstance, Client> serviceChainInstanceToClient = new HashMap<ServiceChainInstance, Client>();
		for (Client c : ServiceFunctionChainingController.getEntityStorage().list(Client.class)) {
			if (c.getServiceChainInstance() != null)
				serviceChainInstanceToClient.put(c.getServiceChainInstance(), c);
		}
		return serviceChainInstanceToClient;
	}
}
