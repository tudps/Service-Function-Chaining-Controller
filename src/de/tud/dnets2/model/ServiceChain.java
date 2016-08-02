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

import java.util.List;

/**
 * Service Chain incorporating Service Functions. A Service Chain serves
 * as a template for a concrete Service Chain Instance.
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class ServiceChain extends Entity {
	private static final long serialVersionUID = 705007053063334011L;
	
	/** Desired Service Functions in this Service Chain */
	private List<ServiceFunction> serviceFunctions;
	
	public List<ServiceFunction> getServiceFunctions() {
		return serviceFunctions;
	}

	public void setServiceFunctions(List<ServiceFunction> serviceFunctions) {
		this.serviceFunctions = serviceFunctions;
	}
}
