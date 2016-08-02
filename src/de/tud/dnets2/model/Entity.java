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

import java.io.Serializable;

import org.json.JSONObject;

import de.tud.dnets2.global.JSONable;

/**
 * Generic entity
 * @author Victor-Philipp Negoescu
 * @version 1.0
 */
@SuppressWarnings("serial")
public abstract class Entity implements Serializable, Comparable<Entity>, JSONable {

	/** Unique identification. Must implement the methods
	 * <code>equals()</code> and <code>hashCode()</code> in order to
	 * make <code>EntityStorage</code>s work correctly. */
	private EntityId id;
	
	/** Custom information tag (e.g. to hold debug information) */
	private String tag;
	
	public Entity() {
		
	}
	
	public Entity(EntityId id) {
		this.id = id;
	}

	@Override
	public int compareTo(Entity o) {
		return getId().compareTo(o.getId());
	}
	
	@Override
	public JSONObject toJSON() {
		return new JSONObject(this);
	}
	
	public EntityId getId() {
		return id;
	}
	
	public void setId(EntityId id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null || !other.getClass().equals(this.getClass()))
			return false;
		Entity otherEntity = (Entity)other;
		return otherEntity.getId().equals(this.getId());
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
