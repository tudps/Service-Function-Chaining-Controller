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
package de.tud.dnets2.server;

import de.tud.dnets2.model.Client;

/**
 * Interface for the (OpenFlow) routing engine. The Service Function Chaining Controller
 * calls these methods whenever a chain is assigned or deleted
 * 
 * @author Victor-Philipp Negoescu
 * @author Timm W�chter
 * @version 1.0
 */
public interface ChainRouter {
	/**
	 * Initialize a Service Chain for a single client 
	 * 
	 * @param client
	 */
	public boolean initChain(Client client);
	
	/**
	 * Remove an existing Service Chain for a single client 
	 * 
	 * @param client
	 */
	public boolean removeChain(Client client);
	
	/**
	 * Updates a Service Chain Instance. Is called whenever
	 * the Service Chain Instance changes (after adding/removing a Service Instance).
	 * 
	 * @param client The client
	 */
	public boolean updateChain(Client client);
}
