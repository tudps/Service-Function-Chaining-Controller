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
import de.tud.dnets2.model.ServiceFunction;

/**
 * REST server resource for managing <code>ServiceFunction</code>s.
 * 
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class ServiceFunctionRes extends EntityRes<ServiceFunction> {
	@Override
	protected String getEntitySingleResponseKey() {
		return Constants.REST_KEY_SERVICEFUNCTION;
	}

	@Override
	protected String getEntityListResponseKey() {
		return Constants.REST_KEY_SERVICEFUNCTIONS;
	}
	
	@Override
	public Class<ServiceFunction> getEntityClass() {
		return ServiceFunction.class;
	}

	@Override
	protected Entity internalHandlePut(JSONObject query) throws Exception {
		// Not supported
		return null;
	}

	@Override
	protected boolean internalHandleDelete(EntityId id) {
		// TODO improve
		return true;
	}

	@Override
	protected boolean internalHandlePut(ServiceFunction e, JSONObject query) {
		// Not supported
		return false;
	}
	
	@Override
	protected boolean internalHandlePost(String operation,
			ServiceFunction e, JSONObject query) {
		// No custom operations supported
		return false;
	}
	
	@Override
	protected JSONObject checkSystemInit() throws JSONException {
		return null; // System status irrelevant for Service Functions
	}
	
}
