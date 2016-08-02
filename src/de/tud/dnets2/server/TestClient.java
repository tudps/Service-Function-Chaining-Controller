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
package de.tud.dnets2.server;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;

import de.tud.dnets2.global.Constants;
import de.tud.dnets2.global.JSON;
import de.tud.dnets2.model.EntityId;
import de.tud.dnets2.model.ServiceFunction;
import de.tud.dnets2.model.ServiceInstance;
import de.tud.dnets2.model.ServiceNode;

public class TestClient {
	public static void main(String[] args) throws FailingHttpStatusCodeException, IOException {
		ServiceFunction redirect = new ServiceFunction();
		redirect.setId(new EntityId("1"));
		redirect.setTag("Redirect");
		redirect.setParameterCaptions(new ArrayList<String>());
		redirect.setDefaultParameterValues(new ArrayList<String>());
		redirect.setIngressPacketL2Destination(Constants.ADDRESS_REWRITE_MODE_REAL);
		redirect.setIngressPacketL3Destination(Constants.ADDRESS_REWRITE_MODE_REAL);
		redirect.setEgressPacketL2Source(Constants.ADDRESS_REWRITE_MODE_REAL);
		redirect.setEgressPacketL3Source(Constants.ADDRESS_REWRITE_MODE_REAL);
		
		ServiceNode sn = new ServiceNode();
		sn.setId(new EntityId("sn1"));
		sn.setManagementIP("123");
		sn.setSwitchMAC("456");
		sn.setTag("SERVICE NODE");
		
		ServiceInstance si = new ServiceInstance();
		si.setId(new EntityId("si1"));
		si.setEgressIP("101");
		si.setEgressMAC("19991");
		si.setEgressSwitchInterface("superinterface");
		si.setFree(false);
		si.setServiceFunction(redirect);
		si.setParameterValues(new ArrayList<String>());
		
		WebClient client = new WebClient();
		WebRequest request = new WebRequest(new URL("http://127.0.0.1:8089/sfcc/serviceInstance"), HttpMethod.POST);
		request.setAdditionalHeader("Content-Type", "application/json");
		request.setRequestBody(JSON.buildJSON(si).toString());
		client.getPage(request);
	}
}
