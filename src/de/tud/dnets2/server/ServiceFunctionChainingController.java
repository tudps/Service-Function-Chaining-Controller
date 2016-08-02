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

import java.util.ArrayList;
import java.util.List;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;

import de.tud.dnets2.global.Constants;
import de.tud.dnets2.global.Settings;
import de.tud.dnets2.model.Client;
import de.tud.dnets2.model.ContentProvider;
import de.tud.dnets2.model.EntityId;
import de.tud.dnets2.model.ServiceChain;
import de.tud.dnets2.model.ServiceFunction;
import de.tud.dnets2.server.rest.ClientRes;
import de.tud.dnets2.server.rest.ContentProviderRes;
import de.tud.dnets2.server.rest.ServiceChainInstanceRes;
import de.tud.dnets2.server.rest.ServiceChainRes;
import de.tud.dnets2.server.rest.ServiceFunctionRes;
import de.tud.dnets2.server.rest.ServiceInstanceRes;
import de.tud.dnets2.server.rest.ServiceNodeRes;
import de.tud.dnets2.storage.EntityStorage;
import de.tud.dnets2.util.ShellExecuter;

/**
 * Runnable core class of the Service Function Chaining Controller (SFCC). Starts a REST server
 * on port <code>Settings.REST_SERVER_PORT</code> over which the accessing clients can
 * control the information stored in the controller.
 * Serves as controlling unit for the Service Function Chaining Router (SFCR).
 * 
 * @version 1.0
 * @author Victor-Philipp Negoescu
 *
 */
public class ServiceFunctionChainingController extends Application implements Runnable {
	private Component component = new Component();
	private Server server = new Server(Protocol.HTTP, Settings.SFCC_REST_SERVER_PORT);
	
	/** Service node dpid of the open vswitch */
	private short dpid = 1;
	
	/** Storage for all controlled entities */
	private static EntityStorage entityStorage = new EntityStorage();
	
	/** Lock for REST requests in order to ensure consistent storage */
	public final static Object restLock = new Object();
	
	
	/** (OpenFlow) routing engine */
	private ChainRouter chainRouter;
	private Client testClient;
//	private Client testClient3;
//	private Client testClient2;
	
	/**
	 * @param IP_snc IP address of the service node
	 * @param fw number of firewall instances
	 * @param ts number of traffic shaping instances
	 * @param redirect redirect instances 
	 * @param ps proxy server instances 
	 * @param lqp low quality proxy instances - video 
	 * @param hqp high quailty proxy instances - video
	 */
	private void startNodes(String IP_snc, int fw, int ts, int redirect, int ps, int lqp, int hqp){
		ShellExecuter sh = new ShellExecuter();
		System.err.println("Starting node script '" + Settings.START_NODE_SCRIPT + "' with parameters '" + Settings.MININET_PDW + "', '" + IP_snc + "', '" + Settings.IP_CONTROLLER + "', '" + dpid + "', '" + fw + "', '" + ts + "', '" + redirect + "', '" + ps + "', '" + lqp + "', '" + hqp + "'");
		sh.execute(Settings.START_NODE_SCRIPT, Settings.MININET_PDW, IP_snc, Settings.IP_CONTROLLER,
				dpid++,fw,ts,redirect,ps,lqp,hqp);
	}
	
	public ServiceFunctionChainingController(ChainRouter chainRouter) {
		this.chainRouter = chainRouter;
		
		// Add Service Functions
		ServiceFunction redirect = new ServiceFunction();
		redirect.setId(new EntityId("1"));
		redirect.setTag("Redirect");
		redirect.setParameterCaptions(new ArrayList<String>());
		redirect.setDefaultParameterValues(new ArrayList<String>());
		redirect.setIngressPacketL2Destination(Constants.ADDRESS_REWRITE_MODE_INSTANCE);
		redirect.setIngressPacketL3Destination(Constants.ADDRESS_REWRITE_MODE_REAL);
		redirect.setEgressPacketL2Source(Constants.ADDRESS_REWRITE_MODE_INSTANCE);
		redirect.setEgressPacketL3Source(Constants.ADDRESS_REWRITE_MODE_REAL);
		ServiceFunctionChainingController.getEntityStorage().insert(redirect);
		
		ServiceFunction firewall = new ServiceFunction();
		firewall.setId(new EntityId("2"));
		firewall.setTag("Firewall");
		List<String> fwCaptions = new ArrayList<String>();
		fwCaptions.add("Block web");
		fwCaptions.add("Block video");
		List<String> fwDefaultParams = new ArrayList<String>();
		fwDefaultParams.add("0");
		fwDefaultParams.add("1");
		firewall.setDefaultParameterValues(fwDefaultParams);
		firewall.setParameterCaptions(fwCaptions);
		firewall.setIngressPacketL2Destination(Constants.ADDRESS_REWRITE_MODE_REAL);
		firewall.setIngressPacketL3Destination(Constants.ADDRESS_REWRITE_MODE_REAL);
		firewall.setEgressPacketL2Source(Constants.ADDRESS_REWRITE_MODE_REAL);
		firewall.setEgressPacketL3Source(Constants.ADDRESS_REWRITE_MODE_REAL);
		ServiceFunctionChainingController.getEntityStorage().insert(firewall);
		
		ServiceFunction trafficShaper = new ServiceFunction();
		trafficShaper.setId(new EntityId("3"));
		trafficShaper.setTag("Traffic Shaper");
		List<String> tsCaptions = new ArrayList<String>();
		tsCaptions.add("bandwidth");
		trafficShaper.setParameterCaptions(tsCaptions);
		List<String> tsDefaultParams = new ArrayList<String>();
		tsDefaultParams.add("-1");
		trafficShaper.setDefaultParameterValues(tsDefaultParams);
		trafficShaper.setIngressPacketL2Destination(Constants.ADDRESS_REWRITE_MODE_REAL);
		trafficShaper.setIngressPacketL3Destination(Constants.ADDRESS_REWRITE_MODE_REAL);
		trafficShaper.setEgressPacketL2Source(Constants.ADDRESS_REWRITE_MODE_REAL);
		trafficShaper.setEgressPacketL3Source(Constants.ADDRESS_REWRITE_MODE_REAL);
		ServiceFunctionChainingController.getEntityStorage().insert(trafficShaper);
		
		ServiceFunction proxy = new ServiceFunction();
		proxy.setId(new EntityId("4"));
		proxy.setTag("HTTP Proxy");
		List<String> pxCaptions = new ArrayList<String>();
		proxy.setParameterCaptions(pxCaptions);
		List<String> pxDefaultParams = new ArrayList<String>();
		proxy.setDefaultParameterValues(pxDefaultParams);
		proxy.setIngressPacketL2Destination(Constants.ADDRESS_REWRITE_MODE_INSTANCE);
		proxy.setIngressPacketL3Destination(Constants.ADDRESS_REWRITE_MODE_REAL);
		proxy.setEgressPacketL2Source(Constants.ADDRESS_REWRITE_MODE_INSTANCE);
		proxy.setEgressPacketL3Source(Constants.ADDRESS_REWRITE_MODE_INSTANCE);
		ServiceFunctionChainingController.getEntityStorage().insert(proxy);
		
		ServiceFunction lqVideoProxy = new ServiceFunction();
		lqVideoProxy.setId(new EntityId("5"));
		lqVideoProxy.setTag("Low Quality Video Proxy");
		pxCaptions = new ArrayList<String>();
		lqVideoProxy.setParameterCaptions(pxCaptions);
		pxDefaultParams = new ArrayList<String>();
		lqVideoProxy.setDefaultParameterValues(pxDefaultParams);
		lqVideoProxy.setIngressPacketL2Destination(Constants.ADDRESS_REWRITE_MODE_INSTANCE);
		lqVideoProxy.setIngressPacketL3Destination(Constants.ADDRESS_REWRITE_MODE_REAL);
		lqVideoProxy.setEgressPacketL2Source(Constants.ADDRESS_REWRITE_MODE_INSTANCE);
		lqVideoProxy.setEgressPacketL3Source(Constants.ADDRESS_REWRITE_MODE_INSTANCE);
		ServiceFunctionChainingController.getEntityStorage().insert(lqVideoProxy);
		
		ServiceFunction hqVideoProxy = new ServiceFunction();
		hqVideoProxy.setId(new EntityId("6"));
		hqVideoProxy.setTag("High Quality Video Proxy");
		pxCaptions = new ArrayList<String>();
		hqVideoProxy.setParameterCaptions(pxCaptions);
		pxDefaultParams = new ArrayList<String>();
		hqVideoProxy.setDefaultParameterValues(pxDefaultParams);
		hqVideoProxy.setIngressPacketL2Destination(Constants.ADDRESS_REWRITE_MODE_INSTANCE);
		hqVideoProxy.setIngressPacketL3Destination(Constants.ADDRESS_REWRITE_MODE_REAL);
		hqVideoProxy.setEgressPacketL2Source(Constants.ADDRESS_REWRITE_MODE_INSTANCE);
		hqVideoProxy.setEgressPacketL3Source(Constants.ADDRESS_REWRITE_MODE_INSTANCE);
		ServiceFunctionChainingController.getEntityStorage().insert(hqVideoProxy);
		
		List<ServiceFunction> sfs = new ArrayList<ServiceFunction>();
		sfs.add(redirect);
		ServiceChain sc = new ServiceChain();
		sc.setServiceFunctions(sfs);
		sc.setTag("Blocked Access");
		entityStorage.insert(sc);
		
		sfs = new ArrayList<ServiceFunction>();
		sfs.add(firewall);
		sc = new ServiceChain();
		sc.setServiceFunctions(sfs);
		sc.setTag("Limited Access");
		entityStorage.insert(sc);
		
		sfs = new ArrayList<ServiceFunction>();
		sfs.add(trafficShaper);
		sc = new ServiceChain();
		sc.setServiceFunctions(sfs);
		sc.setTag("Default Access");
		entityStorage.insert(sc);
		
		sfs = new ArrayList<ServiceFunction>();
		sfs.add(proxy);
		sc = new ServiceChain();
		sc.setServiceFunctions(sfs);
		sc.setTag("Web Proxy");
		entityStorage.insert(sc);
		
		sfs = new ArrayList<ServiceFunction>();
		sfs.add(lqVideoProxy);
		sc = new ServiceChain();
		sc.setServiceFunctions(sfs);
		sc.setTag("Static SD Video");
		entityStorage.insert(sc);
		
		sfs = new ArrayList<ServiceFunction>();
		sfs.add(hqVideoProxy);
		sc = new ServiceChain();
		sc.setServiceFunctions(sfs);
		sc.setTag("Static HD Video");
		entityStorage.insert(sc);
		
		// Content Provider (== WWW Edge)
		ContentProvider cp = new ContentProvider(new EntityId(Settings.CONTENT_PROVIDER_MAC));
		cp.setHostname("CP");
		cp.setIP(Settings.CONTENT_PROVIDER_IP);
		cp.setSwitchInterface(Settings.EGRESS_SWITCH_INTERFACE);
		// old settings
//		cp.setSwitchMAC(Settings.CONTENT_PROVIDER_MAC_LONG);
		// new settings
		cp.setSwitchMAC(String.valueOf(Long.parseLong(Settings.EGRESS_SWITCH_MAC,16)));
		entityStorage.insert(cp);
		
		component.getServers().add(server);
		component.getClients().add(Protocol.HTTP);
		
		server.getContext().getParameters().add("maxThreads", "512");
		server.getContext().getParameters().add("maxTotalConnections", "512");
		
		component.getDefaultHost().attach("/sfcc", this);
		
		// --- DEBUG ---
		
		// Static service nodes
		/*ServiceNode[] serviceNodes = new ServiceNode[2];
		ServiceNode sn1 = new ServiceNode();
		sn1.setSwitchMAC("1");
		sn1.setTag("cprovider2");
		sn1.setManagementIP("10.0.9.19");
		ServiceNode sn2 = new ServiceNode();
		sn2.setSwitchMAC("2");
		sn2.setTag("cache1");
		sn2.setManagementIP("10.0.9.29");
		
		serviceNodes[0] = sn1;
		serviceNodes[1] = sn2;
		
		entityStorage.insert(sn1);
		entityStorage.insert(sn2);
		
		List<String> fwValues = new ArrayList<String>();
		fwValues.add("0");
		fwValues.add("0");
		List<String> tsValues1 = new ArrayList<String>();
		tsValues1.add("10");
		
		// Service instances
		ServiceInstance[][] sisBySns = new ServiceInstance[2][];
		for (int m = 1; m <= 2; m++) {
			sisBySns[m-1] = new ServiceInstance[20];
			for (int n = 1; n <= 20; n++) {
				ServiceInstance si = new ServiceInstance();
				si.setIngressMAC("00:00:00:0" + m + ":0" + n + ":00");
				si.setIngressSwitchInterface("s1-eth" + Integer.valueOf((n+1)/2).toString());
				si.setEgressMAC("00:00:00:0" + m + ":0" + n + ":01");
				si.setEgressSwitchInterface("s1-eth" + Integer.valueOf((n+1)/2+1).toString());
				si.setFree(true);
				if (n % 4 == 0)
					si.setServiceFunction(proxy);
				else if (n % 3 == 0)
					si.setServiceFunction(trafficShaper);
				else if (n % 2 == 0)
					si.setServiceFunction(firewall);
				else
					si.setServiceFunction(redirect);
				si.setServiceNode(serviceNodes[m-1]);
				//si.setParameterValues(fwValues);
				sisBySns[m-1][n-1] = si;
				entityStorage.insert(si);
			}
		}

		// Service Chain		
		sfs = new ArrayList<ServiceFunction>();
		sfs.add(firewall);
		sc = new ServiceChain();
		sc.setServiceFunctions(sfs);
		sc.setTag("Firewall Service Chain");
		entityStorage.insert(sc);
		
		sfs = new ArrayList<ServiceFunction>();
		sfs.add(firewall);
		sfs.add(proxy);
		sc = new ServiceChain();
		sc.setServiceFunctions(sfs);
		sc.setTag("Premium Video Service Chain");
		entityStorage.insert(sc);
		
		sfs = new ArrayList<ServiceFunction>();
		sfs.add(firewall);
		sfs.add(proxy);
		sfs.add(trafficShaper);
		sc = new ServiceChain();
		sc.setServiceFunctions(sfs);
		sc.setTag("Limited Video Service Chain");
		entityStorage.insert(sc);
		
		// Client
		testClient = new Client(new EntityId("08:60:6e:9d:b8:01"));
		testClient.setSwitchInterface("25");
		testClient.setSwitchMAC("281635445233219");
		testClient.setIP("10.0.9.251");
		testClient.setHostname("Tab");
		testClient.setServiceChainInstance(null);
		entityStorage.insert(testClient);
		
		testClient2 = new Client(new EntityId("09:60:6e:9d:b8:01"));
		testClient2.setSwitchInterface("25");
		testClient2.setSwitchMAC("281635445233219");
		testClient2.setIP("10.0.9.252");
		testClient2.setHostname("Phone");
		testClient2.setServiceChainInstance(null);
		entityStorage.insert(testClient2);
		
		testClient3 = new Client(new EntityId("0a:60:6e:9d:b8:01"));
		testClient3.setSwitchInterface("25");
		testClient3.setSwitchMAC("281635445233219");
		testClient3.setIP("10.0.9.253");
		testClient3.setHostname("PC");
		testClient3.setServiceChainInstance(null);
		entityStorage.insert(testClient3);
		
		JSONObject sciQuery = new JSONObject();
		try {
			sciQuery.put("serviceChainId", "1");
			sciQuery.put("contentProviderMAC", cp.getMAC());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		}
		
		ClientRes cRes = new ClientRes();
		cRes.setApplication(this);
		try {
			cRes.assignToServiceChain(testClient, sciQuery);
			sciQuery.put("serviceChainId", "3");
			cRes.assignToServiceChain(testClient2, sciQuery);
			sciQuery.put("serviceChainId", "4");
			cRes.assignToServiceChain(testClient3, sciQuery);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		}*/
		
		
		//start nodes
		// node 1 with proxies
		startNodes(Settings.IP_SNC1, 0, 0, 0, 10, 10, 10);
		try {
			Thread.sleep(5000); // just to be "sure" that SN 1 registers before SN 2, ugly hack...
		} catch (InterruptedException e1) {}
		// node 2 with firewalls, traffic shaper, redirect
		startNodes(Settings.IP_SNC2, 10, 10, 10, 0, 0, 0);

		try {
			component.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	@Override
	public synchronized Restlet createInboundRoot() {
		Router router = new Router(getContext());
		
		// ----- GLOBAL -----
		//router.attach("/global/reset", GlobalResetRes.class);
		//router.attach("/global/info", GlobalInfoRes.class);
		
		// ----- SERVICE NODE -----
		router.attach("/serviceNode", ServiceNodeRes.class);
		router.attach("/serviceNode/{id}", ServiceNodeRes.class);
		router.attach("/serviceNode/{id}/{operation}", ServiceNodeRes.class);
		
		// ----- SERVICE FUNCTION -----
		router.attach("/serviceFunction", ServiceFunctionRes.class);
		router.attach("/serviceFunction/{id}", ServiceFunctionRes.class);
		router.attach("/serviceFunction/{id}/{operation}", ServiceFunctionRes.class);
		
		// ----- SERVICE INSTANCE -----
		router.attach("/serviceInstance", ServiceInstanceRes.class);
		router.attach("/serviceInstance/{id}", ServiceInstanceRes.class);
		router.attach("/serviceInstance/{id}/{operation}", ServiceInstanceRes.class);
		
		// ----- SERVICE CHAIN -----
		router.attach("/serviceChain", ServiceChainRes.class);
		router.attach("/serviceChain/{id}", ServiceChainRes.class);
		router.attach("/serviceChain/{id}/{operation}", ServiceChainRes.class);
		
		// ----- SERVICE CHAIN INSTANCE -----
		router.attach("/serviceChainInstance", ServiceChainInstanceRes.class);
		router.attach("/serviceChainInstance/{id}", ServiceChainInstanceRes.class);
		router.attach("/serviceChainInstance/{id}/{operation}", ServiceChainInstanceRes.class);
		
		// ----- CLIENT -----
		router.attach("/client", ClientRes.class);
		router.attach("/client/{id}", ClientRes.class);
		router.attach("/client/{id}/{operation}", ClientRes.class);
		
		// ----- CONTENT PROVIDER -----
		router.attach("/contentProvider", ContentProviderRes.class);
		router.attach("/contentProvider/{id}", ContentProviderRes.class);
		router.attach("/contentProvider/{id}/{operation}", ContentProviderRes.class);
		
		return router;
	}

	public static EntityStorage getEntityStorage() {
		return entityStorage;
	}

	public ChainRouter getChainRouter() {
		return chainRouter;
	}
	
	public static void main(String[] args) {
		new ServiceFunctionChainingController(null);
	}

	@Override
	public void run() {
		if (chainRouter != null && testClient != null)
			while (true) {
				if (chainRouter.initChain(testClient))
					break;
				
				try {
					Thread.sleep(2 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
}
