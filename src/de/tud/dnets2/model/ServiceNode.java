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
 * A Service Node is a computation unit where multiple
 * Service Instances are delivering transparent networking
 * services.
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class ServiceNode extends Entity {
	private static final long serialVersionUID = -7971784099159960975L;
	
	/** MAC of the Open vSwitch running on the machine */
	private String switchMAC;
	
	/** IP address of the Service Node within management network */
	private String managementIP;
	
	private boolean ready;

	public String getSwitchMAC() {
		return switchMAC;
	}

	public void setSwitchMAC(String switchMAC) {
		this.switchMAC = switchMAC;
	}

	public String getManagementIP() {
		return managementIP;
	}

	public void setManagementIP(String managementIP) {
		this.managementIP = managementIP;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}
}
