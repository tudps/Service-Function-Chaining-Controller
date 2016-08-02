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
 * A network client accessing the Content Provider. Network traffic from/to clients
 * passes the assigned Service Chain Instance.
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class Client extends Host {
	private static final long serialVersionUID = 2365213413051486742L;
	
	/** Assigned service chain */
	private ServiceChainInstance serviceChainInstance;
	
	/** MAC address of the entry hop */
	private String entryHopMAC;
	
	/** Client index */
	private int index;
	
	public Client(EntityId mac) {
		super(mac);
	}
	
	public ServiceChainInstance getServiceChainInstance() {
		return serviceChainInstance;
	}

	public void setServiceChainInstance(ServiceChainInstance serviceChainInstance) {
		this.serviceChainInstance = serviceChainInstance;
	}

	public String getEntryHopMAC() {
		return entryHopMAC;
	}

	public void setEntryHopMAC(String entryHopMAC) {
		this.entryHopMAC = entryHopMAC;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
