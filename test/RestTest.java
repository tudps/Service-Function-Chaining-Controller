/*
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

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;

import com.eclipsesource.restfuse.Destination;
import com.eclipsesource.restfuse.HttpJUnitRunner;
import com.eclipsesource.restfuse.MediaType;
import com.eclipsesource.restfuse.Method;
import com.eclipsesource.restfuse.Response;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.HttpTest;

import de.tud.dnets2.global.Constants;
import de.tud.dnets2.server.ServiceFunctionChainingController;
import static com.eclipsesource.restfuse.Assert.assertOk;

@RunWith( HttpJUnitRunner.class )
public class RestTest {
	//protected static ServiceFunctionChainingController sfcc;

	@Rule
	public Destination restfuse = new Destination( this, "http://127.0.0.1:8089/sfcc" );

	@Context
	private Response response;

	@BeforeClass
	public static void beforeClass() {
		new ServiceFunctionChainingController(null);
	}

	/***********************************************************************
	 *
	 * Service Node tests
	 *
	 ***********************************************************************/

	@HttpTest( method = Method.GET, path = "/serviceNode", order = 1)
	public void serviceNodeListTest1() {
		assertOk(response);
		// Initial list should be empty
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(0, o.getJSONArray(Constants.REST_KEY_SERVICENODES).length());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/serviceNode", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SWITCH_MAC + "\" : \"123\"}", order = 2)
	public void serviceNodeAddTest1() {
		assertOk(response);
		// Adding a Service Node works
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals("1", o.getString(Constants.REST_KEY_ID));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.GET, path = "/serviceNode", order = 3)
	public void serviceNodeListTest2() {
		// Adding a Service Nodes increases the number of Service Nodes
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(1, o.getJSONArray(Constants.REST_KEY_SERVICENODES).length());
			System.out.println(o.getJSONArray(Constants.REST_KEY_SERVICENODES).toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/serviceNode", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SWITCH_MAC + "\" : \"123\"}", order = 4)
	public void serviceNodeAddTest2() {
		assertOk(response);
		// Adding the same Service Node again gives an error
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_FAIL, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(Constants.REST_ERROR_DUPLICATE_MAC, o.getString(Constants.REST_KEY_ERROR));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/serviceNode", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SWITCH_MAC + "\" : \"456\"}", order = 5)
	public void serviceNodeAddTest3() {
		assertOk(response);
		// Adding a different Service Node works
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals("2", o.getString(Constants.REST_KEY_ID));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	// Now inserted: Service Nodes 1 (Switch MAC 123), 2 (Switch MAC 456)

	/***********************************************************************
	 *
	 * Service Instance tests
	 *
	 ***********************************************************************/
	@HttpTest( method = Method.GET, path = "/serviceInstance", order = 101)
	public void serviceInstanceListTest1() {
		assertOk(response);
		// Initial list should be empty
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(0, o.getJSONArray(Constants.REST_KEY_SERVICEINSTANCES).length());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/serviceInstance", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SERVICEFUNCTION_ID + "\" : \"1\", \"" + Constants.REST_QUERY_SERVICENODE_ID + "\" : \"1\", \"" + Constants.REST_QUERY_INGRESS_MAC + "\" : \"101\", \"" + Constants.REST_QUERY_EGRESS_MAC + "\" : \"102\", \"" + Constants.REST_QUERY_INGRESS_SWITCH_INTERFACE + "\" : \"1\", \"" + Constants.REST_QUERY_EGRESS_SWITCH_INTERFACE + "\" : \"2\"}", order = 102)
	public void serviceInstanceAddTest1() {
		assertOk(response);
		// Adding a Service Instance works
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals("1", o.getString(Constants.REST_KEY_ID));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.GET, path = "/serviceInstance", order = 103)
	public void serviceInstanceListTest2() {
		// Adding a Service Instances increases the number of Service Instances
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(1, o.getJSONArray(Constants.REST_KEY_SERVICEINSTANCES).length());
			System.out.println(o.getJSONArray(Constants.REST_KEY_SERVICEINSTANCES).toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/serviceInstance", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SERVICEFUNCTION_ID + "\" : \"1\", \"" + Constants.REST_QUERY_SERVICENODE_ID + "\" : \"1\", \"" + Constants.REST_QUERY_INGRESS_MAC + "\" : \"101\", \"" + Constants.REST_QUERY_EGRESS_MAC + "\" : \"104\", \"" + Constants.REST_QUERY_INGRESS_SWITCH_INTERFACE + "\" : \"3\", \"" + Constants.REST_QUERY_EGRESS_SWITCH_INTERFACE + "\" : \"4\"}", order = 104)
	public void serviceInstanceAddTest2() {
		assertOk(response);
		// Adding a Service Instance with an already used MAC leads to an error
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_FAIL, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(Constants.REST_ERROR_DUPLICATE_MAC, o.getString(Constants.REST_KEY_ERROR));
			JSONObject info = new JSONObject(o.getString(Constants.REST_KEY_INFO));
			Assert.assertEquals("101", info.getString("mac"));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/serviceInstance", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SERVICEFUNCTION_ID + "\" : \"1\", \"" + Constants.REST_QUERY_SERVICENODE_ID + "\" : \"1\", \"" + Constants.REST_QUERY_INGRESS_MAC + "\" : \"103\", \"" + Constants.REST_QUERY_EGRESS_MAC + "\" : \"104\", \"" + Constants.REST_QUERY_INGRESS_SWITCH_INTERFACE + "\" : \"3\", \"" + Constants.REST_QUERY_EGRESS_SWITCH_INTERFACE + "\" : \"2\"}", order = 105)
	public void serviceInstanceAddTest3() {
		assertOk(response);
		// Adding a Service Instance with an already used switch port leads to an error
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_FAIL, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(Constants.REST_ERROR_DUPLICATE_SWITCH_INTERFACE, o.getString(Constants.REST_KEY_ERROR));
			Assert.assertEquals("2", o.getString(Constants.REST_KEY_INFO));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/serviceInstance", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SERVICEFUNCTION_ID + "\" : \"1\", \"" + Constants.REST_QUERY_SERVICENODE_ID + "\" : \"3\", \"" + Constants.REST_QUERY_INGRESS_MAC + "\" : \"103\", \"" + Constants.REST_QUERY_EGRESS_MAC + "\" : \"104\", \"" + Constants.REST_QUERY_INGRESS_SWITCH_INTERFACE + "\" : \"3\", \"" + Constants.REST_QUERY_EGRESS_SWITCH_INTERFACE + "\" : \"4\"}", order = 106)
	public void serviceInstanceAddTest4() {
		assertOk(response);
		// Adding a Service Instance with an invalid Service Node id leads to an error
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_FAIL, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(Constants.REST_ERROR_INVALID_SERVICENODE, o.getString(Constants.REST_KEY_ERROR));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/serviceInstance", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SERVICEFUNCTION_ID + "\" : \"9\", \"" + Constants.REST_QUERY_SERVICENODE_ID + "\" : \"3\", \"" + Constants.REST_QUERY_INGRESS_MAC + "\" : \"103\", \"" + Constants.REST_QUERY_EGRESS_MAC + "\" : \"104\", \"" + Constants.REST_QUERY_INGRESS_SWITCH_INTERFACE + "\" : \"3\", \"" + Constants.REST_QUERY_EGRESS_SWITCH_INTERFACE + "\" : \"4\"}", order = 107)
	public void serviceInstanceAddTest5() {
		assertOk(response);
		// Adding a Service Instance with an invalid Service Function id leads to an error
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_FAIL, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(Constants.REST_ERROR_INVALID_SERVICEFUNCTION, o.getString(Constants.REST_KEY_ERROR));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/serviceInstance", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SERVICEFUNCTION_ID + "\" : \"1\", \"" + Constants.REST_QUERY_SERVICENODE_ID + "\" : \"2\", \"" + Constants.REST_QUERY_INGRESS_MAC + "\" : \"103\", \"" + Constants.REST_QUERY_EGRESS_MAC + "\" : \"104\", \"" + Constants.REST_QUERY_INGRESS_SWITCH_INTERFACE + "\" : \"1\", \"" + Constants.REST_QUERY_EGRESS_SWITCH_INTERFACE + "\" : \"2\"}", order = 108)
	public void serviceInstanceAddTest6() {
		assertOk(response);
		// Adding another Service Instance works
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals("2", o.getString(Constants.REST_KEY_ID));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/serviceInstance", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SERVICEFUNCTION_ID + "\" : \"2\", \"" + Constants.REST_QUERY_SERVICENODE_ID + "\" : \"2\", \"" + Constants.REST_QUERY_INGRESS_MAC + "\" : \"105\", \"" + Constants.REST_QUERY_EGRESS_MAC + "\" : \"106\", \"" + Constants.REST_QUERY_INGRESS_SWITCH_INTERFACE + "\" : \"3\", \"" + Constants.REST_QUERY_EGRESS_SWITCH_INTERFACE + "\" : \"4\"}", order = 109)
	public void serviceInstanceAddTest7() {
		assertOk(response);
		// Adding another Service Instance works
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals("3", o.getString(Constants.REST_KEY_ID));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	// Now inserted: Service Nodes 1 (Switch MAC 123), 2 (Switch MAC 456)
	//               Service Instances 1 (SN 1, SF 1), 2 (SN 2, SF 1), 3 (SN 2, SF 2)

	/***********************************************************************
	 *
	 * Service Chain tests
	 *
	 ***********************************************************************/

	@HttpTest( method = Method.GET, path = "/serviceChain", order = 201)
	public void serviceChainListTest1() {
		assertOk(response);
		// Initial list should be empty
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(0, o.getJSONArray(Constants.REST_KEY_SERVICECHAINS).length());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/serviceChain", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SERVICEFUNCTION_IDS + "\" : [\"2\", \"1\"]}", order = 202)
	public void serviceChainAddTest1() {
		assertOk(response);
		// Adding a Service Chain works
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals("1", o.getString(Constants.REST_KEY_ID));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.GET, path = "/serviceChain", order = 203)
	public void serviceChainListTest2() {
		// Adding a Service Chain increases the number of Service Chains
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(1, o.getJSONArray(Constants.REST_KEY_SERVICECHAINS).length());
			System.out.println(o.getJSONArray(Constants.REST_KEY_SERVICECHAINS).toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/serviceChain", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SERVICEFUNCTION_IDS + "\" : [\"1\", \"9\"]}", order = 204)
	public void serviceChainAddTest2() {
		assertOk(response);
		// Adding a Service Chain with an invalid Service Function id leads to an error
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_FAIL, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(Constants.REST_ERROR_INVALID_SERVICEFUNCTION, o.getString(Constants.REST_KEY_ERROR));
			Assert.assertEquals("9", o.getString(Constants.REST_KEY_INFO));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/serviceChain", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SERVICEFUNCTION_IDS + "\" : [\"1\"]}", order = 205)
	public void serviceChainAddTest3() {
		assertOk(response);
		// Adding another Service Chain works
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals("2", o.getString(Constants.REST_KEY_ID));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	// Now inserted: Service Nodes 1 (Switch MAC 123), 2 (Switch MAC 456)
	//               Service Instances 1 (SN 1, SF 1), 2 (SN 2, SF 1), 3 (SN 2, SF 2)
	//               Service Chain 1 (SFs 2, 1), 2 (SF 1)

	/***********************************************************************
	 *
	 * Client tests
	 *
	 ***********************************************************************/

	@HttpTest( method = Method.GET, path = "/client", order = 301)
	public void clientListTest1() {
		assertOk(response);
		// Initial list should be empty
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(0, o.getJSONArray(Constants.REST_KEY_CLIENTS).length());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/client", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_MAC + "\" : \"701\", \"" + Constants.REST_QUERY_IP + "\" : \"192.168.178.20\", \"hostname\" : \"WORKSTATION\", \"" + Constants.REST_QUERY_SWITCH_MAC + "\" : \"444\", \"" + Constants.REST_QUERY_SWITCH_INTERFACE + "\" : \"1\"}", order = 302)
	public void clientAddTest1() {
		assertOk(response);
		// Adding a Client works
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals("701", o.getString(Constants.REST_KEY_ID));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.GET, path = "/client", order = 303)
	public void clientListTest2() {
		// Adding a Client increases the number of Clients
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(1, o.getJSONArray(Constants.REST_KEY_CLIENTS).length());
			System.out.println(o.getJSONArray(Constants.REST_KEY_CLIENTS).toString());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/client", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_MAC + "\" : \"701\", \"" + Constants.REST_QUERY_IP + "\" : \"192.168.178.21\", \"hostname\" : \"NOTEBOOK\", \"" + Constants.REST_QUERY_SWITCH_MAC + "\" : \"444\", \"" + Constants.REST_QUERY_SWITCH_INTERFACE + "\" : \"1\"}", order = 304)
	public void clientAddTest2() {
		assertOk(response);
		// Adding a Client with an already used Client MAC leads to an error
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_FAIL, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(Constants.REST_ERROR_ALREADY_REGISTERED, o.getString(Constants.REST_KEY_ERROR));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/client", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_MAC + "\" : \"702\", \"" + Constants.REST_QUERY_IP + "\" : \"192.168.178.21\", \"" + Constants.REST_QUERY_SWITCH_MAC + "\" : \"444\", \"" + Constants.REST_QUERY_SWITCH_INTERFACE + "\" : \"1\"}", order = 305)
	public void clientAddTest3() {
		assertOk(response);
		// Adding another Client (without hostname) works
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals("702", o.getString(Constants.REST_KEY_ID));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/client/702", type = MediaType.APPLICATION_JSON, content = "{\"hostname\" : \"NOTEBOOK\"}", order = 306)
	public void clientModifyTest1() {
		assertOk(response);
		// Changing the hostname works
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/client/703", type = MediaType.APPLICATION_JSON, content = "{\"hostname\" : \"iPad\"}", order = 307)
	public void clientModifyTest2() {
		assertOk(response);
		// Changing the hostname of an invalid Client leads to an error
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_FAIL, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(Constants.REST_ERROR_INVALID_ENTITY, o.getString(Constants.REST_KEY_ERROR));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/client", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_MAC + "\" : \"703\", \"" + Constants.REST_QUERY_IP + "\" : \"192.168.178.22\", \"hostname\" : \"iPad\", \"" + Constants.REST_QUERY_SWITCH_MAC + "\" : \"444\", \"" + Constants.REST_QUERY_SWITCH_INTERFACE + "\" : \"1\"}", order = 308)
	public void clientAddTest4() {
		assertOk(response);
		// Adding another Client works
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals("703", o.getString(Constants.REST_KEY_ID));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.DELETE, path = "/client/704", type = MediaType.APPLICATION_JSON, order = 309)
	public void clientDeleteTest1() {
		assertOk(response);
		// Deleting an invalid Client leads to an error
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_FAIL, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(Constants.REST_ERROR_INVALID_ENTITY, o.getString(Constants.REST_KEY_ERROR));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.DELETE, path = "/client/703", type = MediaType.APPLICATION_JSON, order = 310)
	public void clientDeleteTest2() {
		assertOk(response);
		// Deleting a Client works
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.PUT, path = "/client", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_MAC + "\" : \"703\", \"" + Constants.REST_QUERY_IP + "\" : \"192.168.178.22\", \"hostname\" : \"iPad\", \"" + Constants.REST_QUERY_SWITCH_MAC + "\" : \"444\", \"" + Constants.REST_QUERY_SWITCH_INTERFACE + "\" : \"1\"}", order = 311)
	public void clientAddTest5() {
		assertOk(response);
		// Adding another Client works
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals("703", o.getString(Constants.REST_KEY_ID));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	// Now inserted: Service Nodes 1 (Switch MAC 123), 2 (Switch MAC 456)
	//               Service Instances 1 (SN 1, SF 1), 2 (SN 2, SF 1), 3 (SN 2, SF 2)
	//               Service Chain 1 (SFs 2, 1), 2 (SF 1)
	//               Clients 701 ("WORKSTATION"), 702 ("NOTEBOOK")

	/***********************************************************************
	 *
	 * Content Provider tests
	 *
	 ***********************************************************************/

	@HttpTest( method = Method.PUT, path = "/contentProvider", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_MAC + "\" : \"512\", \"" + Constants.REST_QUERY_IP + "\" : \"192.168.178.44\", \"hostname\" : \"CP\", \"" + Constants.REST_QUERY_SWITCH_MAC + "\" : \"555\", \"" + Constants.REST_QUERY_SWITCH_INTERFACE + "\" : \"8\"}", order = 401)
	public void contentProviderAddTest1() {
		assertOk(response);
		// Adding a Content Provider works
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals("512", o.getString(Constants.REST_KEY_ID));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	/***********************************************************************
	 *
	 * Assigning Clients to Service Chain Instances
	 *
	 ***********************************************************************/

	@HttpTest( method = Method.POST, path = "/client/701/assignToServiceChain", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SERVICECHAIN_ID + "\" : \"1\", \"" + Constants.REST_QUERY_CONTENTPROVIDER_MAC + "\" : \"512\"}", order = 501)
	public void clientAssignTest1() {
		assertOk(response);
		// Assigning the WORKSTATION to the first Service Chain works
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.POST, path = "/client/702/assignToServiceChain", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SERVICECHAIN_ID + "\" : \"2\", \"" + Constants.REST_QUERY_CONTENTPROVIDER_MAC + "\" : \"512\"}", order = 502)
	public void clientAssignTest2() {
		assertOk(response);
		// Assigning the NOTEBOOK to the second Service Chain works
		// (Now, all Service Instances should be used)
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.POST, path = "/client/703/assignToServiceChain", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SERVICECHAIN_ID + "\" : \"2\", \"" + Constants.REST_QUERY_CONTENTPROVIDER_MAC + "\" : \"512\"}", order = 503)
	public void clientAssignTest3() {
		assertOk(response);
		// Assigning the iPad to the second Service Chain leads to an error
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_FAIL, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(Constants.REST_ERROR_INSUFFICIENT_SERVICEINSTANCES, o.getString(Constants.REST_KEY_ERROR));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.DELETE, path = "/client/702", type = MediaType.APPLICATION_JSON, order = 504)
	public void clientDeleteTest3() {
		assertOk(response);
		// Deleting NOTEBOOK should release Service Chain 2
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.POST, path = "/client/703/assignToServiceChain", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SERVICECHAIN_ID + "\" : \"2\", \"" + Constants.REST_QUERY_CONTENTPROVIDER_MAC + "\" : \"000\"}", order = 505)
	public void clientAssignTest4() {
		assertOk(response);
		// Setting an invalid Content Provider MAC leads to an error
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_FAIL, o.getString(Constants.REST_KEY_STATUS));
			Assert.assertEquals(Constants.REST_ERROR_INVALID_CONTENTPROVIDER, o.getString(Constants.REST_KEY_ERROR));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@HttpTest( method = Method.POST, path = "/client/703/assignToServiceChain", type = MediaType.APPLICATION_JSON, content = "{\"" + Constants.REST_QUERY_SERVICECHAIN_ID + "\" : \"2\", \"" + Constants.REST_QUERY_CONTENTPROVIDER_MAC + "\" : \"512\"}", order = 506)
	public void clientAssignTest5() {
		assertOk(response);
		// Now, assigning the iPad to Service Chain 2 should work
		try {
			JSONObject o = new JSONObject(response.getBody());
			Assert.assertEquals(Constants.REST_STATUS_OK, o.getString(Constants.REST_KEY_STATUS));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}
}
