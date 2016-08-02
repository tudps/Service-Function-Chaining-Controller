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

import org.json.JSONException;
import org.json.JSONObject;

import de.tud.dnets2.global.Constants;
import de.tud.dnets2.model.Entity;
import de.tud.dnets2.model.EntityId;
import de.tud.dnets2.model.ServiceNode;

/**
 * REST server resource for managing <code>ServiceNode</code>s.
 * 
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class ServiceNodeRes extends EntityRes<ServiceNode> {
	@Override
	protected String getEntitySingleResponseKey() {
		return Constants.REST_KEY_SERVICENODE;
	}

	@Override
	protected String getEntityListResponseKey() {
		return Constants.REST_KEY_SERVICENODES;
	}
	
	@Override
	public Class<ServiceNode> getEntityClass() {
		return ServiceNode.class;
	}

	@Override
	protected Entity internalHandlePut(JSONObject query) throws Exception {
		String switchMAC = query.getString(Constants.REST_QUERY_SWITCH_MAC);
		String managementIP = query.getString(Constants.REST_QUERY_MANAGEMENT_IP);
		
		// Check switch MAC
		if (! checkUniqueMAC(switchMAC, getEntityClass(), null, true, true, true, true))
			return null;
				
		ServiceNode sn = new ServiceNode();
		sn.setSwitchMAC(switchMAC);
		sn.setManagementIP(managementIP);
		sn.setReady(false);
		return sn;
	}

	@Override
	protected boolean internalHandleDelete(EntityId id) {
		// TODO improve
		return true;
	}

	@Override
	protected boolean internalHandlePut(ServiceNode e, JSONObject query) throws Exception  {
		// Not supported
		boolean ready = query.getBoolean(Constants.REST_QUERY_READY);
		e.setReady(ready);
		return true;
	}
	
	@Override
	protected boolean internalHandlePost(String operation,
			ServiceNode e, JSONObject query) {
		// No custom operations supported
		return false;
	}
	
	@Override
	protected JSONObject checkSystemInit() throws JSONException {
		return null; // System status irrelevant for Service Nodes
	}
	
}
