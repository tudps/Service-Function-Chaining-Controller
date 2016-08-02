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
package de.tud.dnets2.model;

/**
 * A Content Provider serves a specific content to a Client.
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class ContentProvider extends Host {
	private static final long serialVersionUID = 1680538354972144024L;
	
	public ContentProvider(EntityId mac) {
		super(mac);
	}		
}
