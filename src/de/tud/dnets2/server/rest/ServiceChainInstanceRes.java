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

import org.json.JSONObject;

import de.tud.dnets2.global.Constants;
import de.tud.dnets2.model.Entity;
import de.tud.dnets2.model.EntityId;
import de.tud.dnets2.model.ServiceChainInstance;

/**
 * REST server resource for managing <code>ServiceChainInstance</code>s.
 * 
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class ServiceChainInstanceRes extends EntityRes<ServiceChainInstance> {

	@Override
	public Class<ServiceChainInstance> getEntityClass() {
		return ServiceChainInstance.class;
	}

	@Override
	protected String getEntitySingleResponseKey() {
		return Constants.REST_KEY_SERVICECHAININSTANCE;
	}

	@Override
	protected String getEntityListResponseKey() {
		return Constants.REST_KEY_SERVICECHAININSTANCES;
	}

	@Override
	protected Entity internalHandlePut(JSONObject query) {
		// Not supported
		return null;
	}

	@Override
	protected boolean internalHandleDelete(EntityId id) {
		// Not supported
		return false;
	}

	@Override
	protected boolean internalHandlePut(ServiceChainInstance e, JSONObject query) {
		// Not supported
		return false;
	}

	@Override
	protected boolean internalHandlePost(String operation,
			ServiceChainInstance e, JSONObject query) {
		// No custom operations supported
		return false;
	}

}
