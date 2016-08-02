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
import de.tud.dnets2.model.FlowLogEntry;
import de.tud.dnets2.server.ServiceFunctionChainingController;

/**
 * REST server resource for managing <code>FlowLogEntry</code>s.
 * 
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class FlowLogEntryRes extends EntityRes<FlowLogEntry> {

	@Override
	public Class<FlowLogEntry> getEntityClass() {
		return FlowLogEntry.class;
	}

	@Override
	protected String getEntitySingleResponseKey() {
		return Constants.REST_KEY_FLOWLOGENTRY;
	}

	@Override
	protected String getEntityListResponseKey() {
		return Constants.REST_KEY_FLOWLOGENTRIES;
	}

	@Override
	protected Entity internalHandlePut(JSONObject query) throws Exception {
		// Not supported
		return null;
	}
	
	@Override
	protected boolean internalHandlePut(FlowLogEntry e, JSONObject query) {
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
			FlowLogEntry e, JSONObject query) {
		// No custom operations supported
		return false;
	}
	
	@Override
	protected void onAfterGet() {
		// Delete all log messages
		ServiceFunctionChainingController.getEntityStorage().deleteAll(FlowLogEntry.class);
	}
}
