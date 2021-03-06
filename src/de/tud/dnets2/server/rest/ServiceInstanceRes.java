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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;

import de.tud.dnets2.global.Constants;
import de.tud.dnets2.global.JSON;
import de.tud.dnets2.global.Settings;
import de.tud.dnets2.model.Entity;
import de.tud.dnets2.model.EntityId;
import de.tud.dnets2.model.ServiceFunction;
import de.tud.dnets2.model.ServiceInstance;
import de.tud.dnets2.model.ServiceNode;
import de.tud.dnets2.server.ServiceFunctionChainingController;

/**
 * REST server resource for managing <code>ServiceInstance</code>s.
 * 
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class ServiceInstanceRes extends EntityRes<ServiceInstance> {

	@Override
	public Class<ServiceInstance> getEntityClass() {
		return ServiceInstance.class;
	}

	@Override
	protected String getEntitySingleResponseKey() {
		return Constants.REST_KEY_SERVICEINSTANCE;
	}

	@Override
	protected String getEntityListResponseKey() {
		return Constants.REST_KEY_SERVICEINSTANCES;
	}

	@Override
	protected Entity internalHandlePut(JSONObject query) throws Exception {
		// Check Service Function
		ServiceFunction sf = ServiceFunctionChainingController.getEntityStorage().getById(ServiceFunction.class, new EntityId(query.getString(Constants.REST_QUERY_SERVICEFUNCTION_ID)));		
		if (sf == null) {
			// Invalid Service Function
			setLastError(Constants.REST_ERROR_INVALID_SERVICEFUNCTION);
			return null;
		}
		
		// Check Service Node
		ServiceNode sn = ServiceFunctionChainingController.getEntityStorage().getById(ServiceNode.class, new EntityId(query.getString(Constants.REST_QUERY_SERVICENODE_ID)));
		if (sn == null) {
			// Invalid Service Node
			setLastError(Constants.REST_ERROR_INVALID_SERVICENODE);
			return null;
		}
		
		List<ServiceInstance> sis = ServiceFunctionChainingController.getEntityStorage().list(getEntityClass());
		for (ServiceInstance osi : sis) {
			if (osi.getServiceNode().getId().equals(sn.getId())) {
				// Same Service Node
				boolean sameEgressSwIf1 = osi.getEgressSwitchInterface().equals(query.getString(Constants.REST_QUERY_EGRESS_SWITCH_INTERFACE));
				boolean sameIngressSwIf1 = osi.getIngressSwitchInterface().equals(query.getString(Constants.REST_QUERY_INGRESS_SWITCH_INTERFACE));
				boolean sameEgressSwIf2 = osi.getEgressSwitchInterface().equals(query.getString(Constants.REST_QUERY_EGRESS_SWITCH_INTERFACE));
				boolean sameIngressSwIf2 = osi.getIngressSwitchInterface().equals(query.getString(Constants.REST_QUERY_INGRESS_SWITCH_INTERFACE));
				
				if (sameEgressSwIf1 || sameIngressSwIf1 || sameEgressSwIf2 || sameIngressSwIf2) {
					// Duplicate switch interface
					setLastError(Constants.REST_ERROR_DUPLICATE_SWITCH_INTERFACE);
					
					if (sameEgressSwIf1 || sameEgressSwIf2)
						setLastInfo(query.getString(Constants.REST_QUERY_EGRESS_SWITCH_INTERFACE));
					else
						setLastInfo(query.getString(Constants.REST_QUERY_INGRESS_SWITCH_INTERFACE));
					
					return null;
				}
			}
		}
		
		// check MACs
		String ingressMAC = query.getString(Constants.REST_QUERY_INGRESS_MAC);
		String egressMAC = query.getString(Constants.REST_QUERY_EGRESS_MAC);
		
		// Check ingress MAC
		if (! checkUniqueMAC(ingressMAC, getEntityClass(), null, true, true, true, true))
			return null;
		
		// Check egress MAC
		if (! checkUniqueMAC(egressMAC, getEntityClass(), null, true, true, true, true))
			return null;
		
		ServiceInstance si = new ServiceInstance();
		si.setServiceFunction(sf);
		si.setServiceNode(sn);
		si.setIngressMAC(query.getString(Constants.REST_QUERY_INGRESS_MAC));
		si.setIngressSwitchInterface(query.getString(Constants.REST_QUERY_INGRESS_SWITCH_INTERFACE));
		si.setEgressMAC(query.getString(Constants.REST_QUERY_EGRESS_MAC));
		si.setEgressSwitchInterface(query.getString(Constants.REST_QUERY_EGRESS_SWITCH_INTERFACE));
		si.setPid(query.getString(Constants.REST_QUERY_PID));
		si.setIngressIP("172.16.31.1"); // TODO
		si.setEgressIP("172.16.31.5"); // TODO
		
		// Parameter values (optional)
		List<String> parameterValues = new ArrayList<String>();
		if (query.has(Constants.REST_QUERY_PARAMETER_VALUES)) {
			JSONArray paramValues = query.getJSONArray(Constants.REST_QUERY_PARAMETER_VALUES);
			if (sf.getParameterCaptions().size() != paramValues.length()) {
				// Invalid number of parameters
				setLastError(Constants.REST_ERROR_INVALID_NUM_PARAMETERS);
				return null;
			}
			
			for (int i = 0; i < paramValues.length(); i++) {
				parameterValues.add((String) paramValues.get(i));
			}
			si.setParameterValues(parameterValues);
		} else {
			si.setParameterValues(si.getServiceFunction().getDefaultParameterValues());
		}
				
		return si;
	}
	
	@Override
	protected boolean internalHandlePut(ServiceInstance e, JSONObject query) {
		// Not supported
		return false;
	}

	@Override
	protected boolean internalHandleDelete(EntityId id) {
		// TODO improve
		return true;
	}
	
	@Override
	protected boolean internalHandlePost(String operation,
			ServiceInstance e, JSONObject query) throws Exception {
		if (operation.equals("setParameterValue")) {
			// Set new parameter value
			int index = query.getInt(Constants.REST_QUERY_INDEX);
			String value = query.getString(Constants.REST_QUERY_VALUE);
			
			if (index < 0 || index >= e.getParameterValues().size()) {
				setLastError(Constants.REST_ERROR_INDEX_OUT_OF_BOUNDS);
				return false;
			}
			
			e.getParameterValues().set(index, value);
			
			// REST request to Service Node
			WebClient client = new WebClient();
			WebRequest request = new WebRequest(new URL("http://" + e.getServiceNode().getManagementIP() + ":" + Settings.SERVICE_NODE_PORT + "/"), HttpMethod.POST);
			request.setAdditionalHeader("Content-Type", "text/plain");
			request.setRequestBody(JSON.buildJSON(e).toString());
			client.getPage(request);
			return true;
		} else {
			setLastError(Constants.REST_ERROR_INVALID_OPERATION);
			return false;
		}
	}
	
	@Override
	protected JSONObject checkSystemInit() throws JSONException {
		return null; // System status irrelevant for Service Instances
	}
}
