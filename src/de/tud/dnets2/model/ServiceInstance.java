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
 * A Service Instance is a concrete realization of
 * an abstract Service Function.
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class ServiceInstance extends Entity {
	private static final long serialVersionUID = 4817961176875234795L;

	/** The abstract Service Function which is realized by this Service Instance */
	private ServiceFunction serviceFunction;
	
	/** The Service Node the Service Instance is running on */
	private ServiceNode serviceNode;
	
	/** MAC address where data is received by the Service Instance */
	private String ingressMAC;
	
	/** IP address where data is received by the Service Instance */
	private String ingressIP;
	
	/** Physical switch port where data is received by the Service Instance */
	private String ingressSwitchInterface;
	
	/** MAC address where data is sent by the Service Instance */
	private String egressMAC;
	
	/** IP address where data is sent by the Service Instance */
	private String egressIP;
	
	/** Physical switch port where data is sent by the Service Instance */
	private String egressSwitchInterface;
	
	/** Parameter values. Must be of same length as Service Function's field `parameterCaptions` */
	private List<String> parameterValues;
	
	/** Specifies if the Service Instance is currently in use within a Service Chain Instance */
	private boolean free = true;
	
	/** Process id on the virtual machine*/
	private String pid;
	
	public ServiceFunction getServiceFunction() {
		return serviceFunction;
	}
	public void setServiceFunction(ServiceFunction serviceFunction) {
		this.serviceFunction = serviceFunction;
	}
	public ServiceNode getServiceNode() {
		return serviceNode;
	}
	public void setServiceNode(ServiceNode serviceNode) {
		this.serviceNode = serviceNode;
	}
	public String getIngressMAC() {
		return ingressMAC;
	}
	public void setIngressMAC(String ingressMAC) {
		this.ingressMAC = ingressMAC;
	}
	public String getIngressIP() {
		return ingressIP;
	}
	public void setIngressIP(String ingressIP) {
		this.ingressIP = ingressIP;
	}
	public String getIngressSwitchInterface() {
		return ingressSwitchInterface;
	}
	public void setIngressSwitchInterface(String ingressSwitchInterface) {
		this.ingressSwitchInterface = ingressSwitchInterface;
	}
	public String getEgressMAC() {
		return egressMAC;
	}
	public void setEgressMAC(String egressMAC) {
		this.egressMAC = egressMAC;
	}
	public String getEgressIP() {
		return egressIP;
	}
	public void setEgressIP(String egressIP) {
		this.egressIP = egressIP;
	}
	public String getEgressSwitchInterface() {
		return egressSwitchInterface;
	}
	public void setEgressSwitchInterface(String egressSwitchInterface) {
		this.egressSwitchInterface = egressSwitchInterface;
	}
	public boolean isFree() {
		return free;
	}
	public void setFree(boolean free) {
		this.free = free;
	}
	public List<String> getParameterValues() {
		return parameterValues;
	}
	public void setParameterValues(List<String> parameterValues) {
		this.parameterValues = new ArrayList<String>(parameterValues);
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}

}
