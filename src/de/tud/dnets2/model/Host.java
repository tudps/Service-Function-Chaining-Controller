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
 * A generic network host.
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public abstract class Host extends Entity {
	private static final long serialVersionUID = 5305676701714044885L;

	/** IP address */
	private String ip;
	
	/** Hostname (optional) */
	private String hostname;
	
	/** Physical switch port where the host is connected to */
	private String switchInterface;
	
	/** MAC of the switch the host is connected to */
	private String switchMAC;
	
	public Host(EntityId id) {
		super(id);
	}
	
	/**
	 * Gets the MAC address of the host.
	 * @return MAC address
	 */
	public String getMAC() {
		return getId().toString();
	}

	public String getIP() {
		return ip;
	}

	public void setIP(String ip) {
		this.ip = ip;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getSwitchInterface() {
		return switchInterface;
	}

	public void setSwitchInterface(String switchInterface) {
		this.switchInterface = switchInterface;
	}

	public String getSwitchMAC() {
		return switchMAC;
	}

	public void setSwitchMAC(String switchMAC) {
		this.switchMAC = switchMAC;
	}
}
