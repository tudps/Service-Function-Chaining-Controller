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
 * An OpenFlow logging entry
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class FlowLogEntry extends Entity {
	private static final long serialVersionUID = -1523867149575864126L;

	/** Timestamp of the message */
	private long timestamp;
	
	/** The client which the rule was created for */ 
	private Client client;
	
	/** MAC address of the switch */
	private String switchMAC;
	
	/** (<i>see OFFlowMod</i>), command which was executed on the switch */
	private int command;
	
	/** (<i>see OFFlowMatch</i>), matching part of the command */
	private String match;
	
	/** (<i>see OFAction</i>), actions part of the command */
	private List<String> actions;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getSwitchMAC() {
		return switchMAC;
	}

	public void setSwitchMAC(String switchMAC) {
		this.switchMAC = switchMAC;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public List<String> getActions() {
		return actions;
	}

	public void setActions(List<String> actions) {
		this.actions = actions;
	}
}
