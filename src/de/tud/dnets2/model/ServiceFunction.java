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

import java.util.ArrayList;
import java.util.List;

/**
 * A Service Function is a model specifying a
 * type of network-based service.
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class ServiceFunction extends Entity {
	private static final long serialVersionUID = -8385704558270146972L;

	/** Determines the rewrite mode of the destination MAC for incoming packets */
	private String ingressPacketL2Destination;
	
	/** Determines the rewrite mode of the destination IP for incoming packets */
	private String ingressPacketL3Destination;
	
	/** Determines the rewrite mode of the source MAC for outgoing packets */
	private String egressPacketL2Source;
	
	/** Determines the rewrite mode of the source IP for outgoing packets */
	private String egressPacketL3Source;
	
	/** Captions of all parameters which can be set for the Service Instance */ 
	private List<String> parameterCaptions;
	
	/** Default values of all parameters which can be set for the Service Instance */
	private List<String> defaultParameterValues;
	
	public String getIngressPacketL2Destination() {
		return ingressPacketL2Destination;
	}
	public void setIngressPacketL2Destination(
			String ingressPacketL2Destination) {
		this.ingressPacketL2Destination = ingressPacketL2Destination;
	}
	public String getIngressPacketL3Destination() {
		return ingressPacketL3Destination;
	}
	public void setIngressPacketL3Destination(
			String ingressPacketL3Destination) {
		this.ingressPacketL3Destination = ingressPacketL3Destination;
	}
	public String getEgressPacketL2Source() {
		return egressPacketL2Source;
	}
	public void setEgressPacketL2Source(String egressPacketL2Source) {
		this.egressPacketL2Source = egressPacketL2Source;
	}
	public String getEgressPacketL3Source() {
		return egressPacketL3Source;
	}
	public void setEgressPacketL3Source(String egressPacketL3Source) {
		this.egressPacketL3Source = egressPacketL3Source;
	}
	public List<String> getParameterCaptions() {
		return parameterCaptions;
	}
	public void setParameterCaptions(List<String> parameterCaptions) {
		this.parameterCaptions = new ArrayList<String>(parameterCaptions);
	}
	public List<String> getDefaultParameterValues() {
		return defaultParameterValues;
	}
	public void setDefaultParameterValues(List<String> defaultParameterValues) {
		this.defaultParameterValues = new ArrayList<String>(defaultParameterValues);
	}
}
