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

import org.json.JSONString;

/**
 * Unique entity identification
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
public class EntityId implements Comparable<EntityId>, JSONString {
	/** Identification value */
	private String value;

	public EntityId(String value) {
		// Remove leading zeros
		/*int startI = 0;
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) == '0')
				startI++;
			else
				break;
		}
		
		this.value = value.substring(startI);*/
		this.value = value;
	}
	
	@Override
	public int compareTo(EntityId o) {
		return value.compareTo(o.value);
	}
	
	@Override
	public String toString() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityId other = (EntityId) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toJSONString() {
		return "\"" + value + "\"";
	}
	
	
}
