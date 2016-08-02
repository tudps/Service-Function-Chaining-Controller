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
 * A Service Chain Instance (SCI) realizes a Service Chain (SC).
 * For each Service Function (SF) of the SC a free Service
 * Instance implementing the SF is incorporated in the SCI. 
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class ServiceChainInstance extends Entity {
	private static final long serialVersionUID = 6035421612799664608L;

	/** Realized Service Chain */
	private ServiceChain serviceChain;
	
	/** last element of the chain */
	private ContentProvider contentProvider;
	
	/** Incorporated Service Instances */
	private List<ServiceInstance> serviceInstances;

	public ServiceChain getServiceChain() {
		return serviceChain;
	}

	public void setServiceChain(ServiceChain serviceChain) {
		this.serviceChain = serviceChain;
	}
	
	public ContentProvider getContentProvider() {
		return contentProvider;
	}

	public void setContentProvider(ContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	public List<ServiceInstance> getServiceInstances() {
		return serviceInstances;
	}

	public void setServiceInstances(List<ServiceInstance> serviceInstances) {
		this.serviceInstances = serviceInstances;
	}
}
