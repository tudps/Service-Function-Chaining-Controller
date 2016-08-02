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

import java.util.Collection;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * JSON helper class
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class JSON {
	/**
	 * Build a JSON object from a JSON-able object
	 * @param obj The object to be JSON-ized
	 * @return JSON-ized object
	 */
	public static JSONObject buildJSON(JSONable obj) {
		return obj.toJSON();
	}
	
	/**
	 * Build a JSON array from a collection of JSON-able objects
	 * @param list The list to be JSON-ized
	 * @return JSON-ized list
	 * @throws Exception
	 */
	public static <T extends JSONable> JSONArray buildJSON(Collection<T> list) throws Exception {
		return new JSONArray(list);
	}

	/**
	 * Build a JSON object from a map of objects
	 * @param map The map to be JSON-ized
	 * @return JSON-ized map
	 * @throws Exception
	 */
	public static JSONObject buildJSON(@SuppressWarnings("rawtypes") Map map) throws Exception {
		return new JSONObject(map);
	}
}
