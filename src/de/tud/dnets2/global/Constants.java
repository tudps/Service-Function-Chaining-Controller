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
package de.tud.dnets2.global;

/**
 * Global REST constants. See SFCC REST API for more information.
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class Constants {
	// ----- REST response status -----
	public static final String REST_STATUS_OK = "OK";
	public static final String REST_STATUS_FAIL = "FAIL";
	
	// ----- REST response error -----
	public static final String REST_ERROR_INTERNAL_ERROR = "INTERNAL_ERROR";
	public static final String REST_ERROR_ALREADY_REGISTERED = "ALREADY_REGISTERED";
	public static final String REST_ERROR_INVALID_QUERY = "INVALID_QUERY";
	public static final String REST_ERROR_INVALID_ENTITY = "INVALID_ENTITY";
	public static final String REST_ERROR_INVALID_OPERATION = "INVALID_OPERATION";
	public static final String REST_ERROR_INVALID_SERVICEFUNCTION = "INVALID_SERVICEFUNCTION";
	public static final String REST_ERROR_INVALID_SERVICENODE = "INVALID_SERVICENODE";
	public static final String REST_ERROR_INVALID_SERVICECHAIN = "INVALID_SERVICECHAIN";
	public static final String REST_ERROR_INVALID_CONTENTPROVIDER = "INVALID_CONTENTPROVIDER";
	public static final String REST_ERROR_DUPLICATE_SWITCH_INTERFACE = "DUPLICATE_SWITCH_INTERFACE";
	public static final String REST_ERROR_DUPLICATE_MAC = "DUPLICATE_MAC";
	public static final String REST_ERROR_INSUFFICIENT_SERVICEINSTANCES = "INSUFFICIENT_SERVICEINSTANCES";
	public static final String REST_ERROR_ROUTING_ERROR = "ROUTING_ERROR";
	public static final String REST_ERROR_INVALID_NUM_PARAMETERS = "INVALID_NUM_PARAMETERS";
	public static final String REST_ERROR_INDEX_OUT_OF_BOUNDS = "INDEX_OUT_OF_BOUNDS";
	public static final String REST_ERROR_SERVICEFUNCTION_ALREADY_CONTAINED = "SERVICEFUNCTION_ALREADY_CONTAINED";
	public static final String REST_ERROR_HAS_SERVICECHAININSTANCES = "HAS_SERVICECHAININSTANCES";
	public static final String REST_ERROR_SYSTEM_INIT = "SYSTEM_INIT";
	
	// ----- REST response key -----
	public static final String REST_KEY_STATUS = "status";
	public static final String REST_KEY_ERROR = "error";
	public static final String REST_KEY_INFO = "info";
	public static final String REST_KEY_ID = "id";
	
	public static final String REST_KEY_CLIENT = "client";
	public static final String REST_KEY_SERVICECHAIN = "serviceChain";
	public static final String REST_KEY_SERVICECHAININSTANCE = "serviceChainInstance";
	public static final String REST_KEY_SERVICEINSTANCE = "serviceInstance";
	public static final String REST_KEY_SERVICENODE = "serviceNode";
	public static final String REST_KEY_SERVICEFUNCTION = "serviceFunction";
	public static final String REST_KEY_CONTENTPROVIDER = "contentProvier";
	
	public static final String REST_KEY_CLIENTS = "clients";
	public static final String REST_KEY_SERVICECHAINS = "serviceChains";
	public static final String REST_KEY_SERVICECHAININSTANCES = "serviceChainInstances";
	public static final String REST_KEY_SERVICEINSTANCES = "serviceInstances";
	public static final String REST_KEY_SERVICENODES = "serviceNodes";
	public static final String REST_KEY_SERVICEFUNCTIONS = "serviceFunctions";
	public static final String REST_KEY_CONTENTPROVIDERS = "contentProviers";
	
	public static final String REST_KEY_FLOWLOGENTRY = "flowLogEntry";
	public static final String REST_KEY_FLOWLOGENTRIES = "flowLogEntries";
	
	// ----- REST query parameter -----
	// Host
	public static final String REST_QUERY_SWITCH_INTERFACE = "switchInterface";
	
	// Client
	public static final String REST_QUERY_ID = "id";
	public static final String REST_QUERY_MAC = "MAC";
	public static final String REST_QUERY_IP = "IP";
	public static final String REST_QUERY_HOSTNAME = "hostname";
	public static final String REST_QUERY_CONTENTPROVIDER_MAC = "contentProviderMAC";
	public static final String REST_QUERY_ENTRY_HOP_MAC = "entryHopMAC";
	
	// Service Chain
	public static final String REST_QUERY_SERVICEFUNCTION_IDS = "serviceFunctionIds";
	public static final String REST_QUERY_INDEX = "index";
	
	// Service Chain Instance
	public static final String REST_QUERY_SERVICECHAIN_ID = "serviceChainId";
	
	// Service Instance
	public static final String REST_QUERY_SERVICEFUNCTION_ID = "serviceFunctionId";
	public static final String REST_QUERY_SERVICENODE_ID = "serviceNodeId";
	public static final String REST_QUERY_INGRESS_MAC = "ingressMAC";
	public static final String REST_QUERY_INGRESS_SWITCH_INTERFACE = "ingressSwitchInterface";
	public static final String REST_QUERY_EGRESS_MAC = "egressMAC";
	public static final String REST_QUERY_EGRESS_SWITCH_INTERFACE = "egressSwitchInterface";
	public static final String REST_QUERY_PARAMETER_VALUES = "parameterValues";
	public static final String REST_QUERY_VALUE = "value";
	public static final String REST_QUERY_PID = "pid";
	
	// Service Node, Host
	public static final String REST_QUERY_SWITCH_MAC = "switchMAC";
	public static final String REST_QUERY_MANAGEMENT_IP = "managementIP";
	public static final String REST_QUERY_READY = "ready";
	
	// Global	
	public static final String REST_QUERY_TAG = "tag";
	
	public static final String ADDRESS_REWRITE_MODE_REAL = "REAL";
	public static final String ADDRESS_REWRITE_MODE_INSTANCE = "INSTANCE";
}
