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
import de.tud.dnets2.model.ContentProvider;
import de.tud.dnets2.model.Entity;
import de.tud.dnets2.model.EntityId;

/**
 * REST server resource for managing <code>ContentProvider</code>s.
 * 
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class ContentProviderRes extends EntityRes<ContentProvider> {

	@Override
	protected String getEntitySingleResponseKey() {
		return Constants.REST_KEY_CLIENT;
	}

	@Override
	public Class<ContentProvider> getEntityClass() {
		return ContentProvider.class;
	}

	@Override
	protected String getEntityListResponseKey() {
		return Constants.REST_KEY_CONTENTPROVIDERS;
	}

	@Override
	protected Entity internalHandlePut(JSONObject query) throws Exception {
		String clientMAC = query.getString(Constants.REST_QUERY_MAC);
		
		// Check content provider MAC (do not check CPs since this is done later)
		if (! checkUniqueMAC(clientMAC, getEntityClass(), null, true, true, true, false))
			return null;
		
		ContentProvider cp = new ContentProvider(new EntityId(query.getString(Constants.REST_QUERY_MAC)));
		cp.setIP(query.getString(Constants.REST_QUERY_IP));
		cp.setSwitchMAC(query.getString(Constants.REST_QUERY_SWITCH_MAC));
		cp.setSwitchInterface(query.getString(Constants.REST_QUERY_SWITCH_INTERFACE));
		if (query.has(Constants.REST_QUERY_HOSTNAME))
			cp.setHostname(query.getString(Constants.REST_QUERY_HOSTNAME));
		
		return cp;
	}
	
	@Override
	protected boolean internalHandlePut(ContentProvider e, JSONObject query) throws Exception {
		// Not supported
		return false;
	}

	@Override
	protected boolean internalHandleDelete(EntityId id) {
		// Not supported
		return false;
	}

	@Override
	protected boolean internalHandlePost(String operation, ContentProvider e, JSONObject query) throws Exception {
		// Not supported
		return false;
	}
	
	@Override
	protected JSONObject checkSystemInit() throws JSONException {
		return null; // System status irrelevant for Content Providers
	}
}
