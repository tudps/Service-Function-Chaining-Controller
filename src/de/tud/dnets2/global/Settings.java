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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Global settings
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class Settings {
	private static Properties PROPS = new Properties();
	
	static{
		try{
			PROPS.load(new FileInputStream("dnets2-ctrl.properties"));
		} catch(IOException e){
			
		}
	}
	
	public static final int SFCC_REST_SERVER_PORT	   = getValueAsInt("SFCC_REST_SERVER_PORT");
	public static final int SFCR_REST_SERVER_PORT	   = getValueAsInt("SFCR_REST_SERVER_PORT");
	public static final int SERVICE_NODE_PORT         = getValueAsInt("SERVICE_NODE_PORT");
	
	public static final String INGRESS_SWITCH_INTERFACE = PROPS.getProperty("INGRESS_SWITCH_INTERFACE");
	public static final String EGRESS_SWITCH_INTERFACE = PROPS.getProperty("EGRESS_SWITCH_INTERFACE");
	
	/***********************************************************/
	/*                SERVICE NODE SETTINGS                    */
	/***********************************************************/
	public static final String MININET_PDW = PROPS.getProperty("MININET_PDW");
	public static final String IP_CONTROLLER = PROPS.getProperty("IP_CONTROLLER");
	public static final String IP_SNC1 = PROPS.getProperty("IP_SNC1");
	public static final String IP_SNC2 = PROPS.getProperty("IP_SNC2");
	public static final String START_NODE_SCRIPT = PROPS.getProperty("START_NODE_SCRIPT");
	
	/***********************************************************/
	/*                CLIENT PROVIDER SETTINGS                */
	/***********************************************************/
	public static final String INGRESS_SWITCH_MAC = PROPS.getProperty("INGRESS_SWITCH_MAC");
	public static final String INGRESS_SWITCH_MAC_LONG = PROPS.getProperty("INGRESS_SWITCH_MAC_LONG");
	
	/***********************************************************/
	/*                CONTENT PROVIDER SETTINGS                */
	/***********************************************************/
	public static final String CONTENT_PROVIDER_IP = PROPS.getProperty("CONTENT_PROVIDER_IP");
	public static final String CONTENT_PROVIDER_MAC = PROPS.getProperty("CONTENT_PROVIDER_MAC");
	public static final String EGRESS_SWITCH_MAC_LONG = PROPS.getProperty("EGRESS_SWITCH_MAC_LONG");
	public static final String EGRESS_SWITCH_MAC = PROPS.getProperty("EGRESS_SWITCH_MAC");

	private static String getValue(String key){
		try{
			PROPS.load(new FileInputStream("dnets2-ctrl.properties"));
		} catch(IOException e){
		}
		return PROPS.getProperty(key);
	}
	
	private static int getValueAsInt(String key){
		try{
			return Integer.parseInt(getValue(key));	
		} catch(NumberFormatException e){
			e.toString();
			return -1;
		}
	}
}
