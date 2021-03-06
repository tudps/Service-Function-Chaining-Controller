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
package de.tud.dnets2.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tud.dnets2.model.Entity;
import de.tud.dnets2.model.EntityId;

/**
 * Can store multiple groups of entities distinguished by their class. 
 * 
 * @version 1.0
 * @author Victor-Philipp Negoescu
 */
@SuppressWarnings("rawtypes")
public class EntityStorage {

	/** The stored entities.
	 * First key: fully-qualified class name of the stored entities,
	 * second key: entity id
	 */
	Map<Class, Map<EntityId, Entity>> entities;
	
	/** Last inserted ids */
	Map<Class, String> lastInsertedIds; 
	
	public EntityStorage() {
		entities = new HashMap<Class, Map<EntityId, Entity>>();
		lastInsertedIds = new HashMap<Class, String>();
	}
	
	/**
	 * Inserts the given entity into the storage.<br/>
	 * <b>Hint:</b> Any subsequent changes to the entity will affect the stored entity since
	 * no copy is made when inserting.
	 * @param entity The entity to insert
	 * @return <i>true</i>, if the operation succeeded, <i>false</i> otherwise.
	 */
	public boolean insert(Entity entity) {
		// initialization
		if (! entities.containsKey(entity.getClass()))
			entities.put(entity.getClass(), new HashMap<EntityId, Entity>());
		
		if (entity.getId() == null) {
			// assign next free id
			if (! lastInsertedIds.containsKey(entity.getClass())) {
				lastInsertedIds.put(entity.getClass(), "1");
				entity.setId(new EntityId("1"));
			} else {
				Integer nextId = Integer.parseInt(lastInsertedIds.get(entity.getClass())) + 1;
				lastInsertedIds.put(entity.getClass(), nextId.toString());
				entity.setId(new EntityId(nextId.toString()));
			}
		}

		if (entities.get(entity.getClass()).containsKey(entity.getId()))
			return false;
		else {
			entities.get(entity.getClass()).put(entity.getId(), entity);
			return true;
		}
	}
	
	/**
	 * Deletes the entity with the given id from the storage. If the
	 * storage does not contain the entity, nothing will happen.
	 * @param cls Class of the entity to be deleted
	 * @param id Id of the entity to be deleted
	 * @return <i>true</i> if the entity was deleted, <i>false</i> otherwise.
	 */
	public boolean deleteById(Class cls, EntityId id) {
		if (! entities.containsKey(cls))
			return false; // Class name unknown
		
		if (! entities.get(cls).containsKey(id))
			return false; // Entity id unknown
		else {
			entities.get(cls).remove(id);
			
			// Clean up empty class keys
			if (entities.get(cls).size() <= 0)
				entities.remove(cls);
			
			return true; // Entity deleted
		}
	}
	
	/**
	 * Generates a list of all entities with the given class.
	 * The resulting list is sorted ascending by entity id.
	 * @param cls Class of which entities have to be an instance of
	 * @return Sorted list of entities with given class.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity> List<T> list(Class<T> cls) {
		if (! entities.containsKey(cls) || entities.get(cls) == null)
			return new ArrayList<T>();
		else {
			List<T> ret = new ArrayList<T>((Collection<T>) entities.get(cls).values());
			Collections.sort(ret);
			return ret;
		}
	}

	/**
	 * Gets the entity with the given class and id.
	 * @param cls Class of the queried entity
	 * @param id Id of the queried entity
	 * @return Found entity, <i>null</i> if the entity was not found.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity> T getById(Class<T> cls, EntityId id) {
		if (! entities.containsKey(cls))
			return null; // Class name unknown
		
		if (! entities.get(cls).containsKey(id))
			return null; // Entity id unknown
		else
			return (T) entities.get(cls).get(id); // Entity found
	}

	/**
	 * Removes all entities of the given class.
	 * @param cls Class of the entity
	 */
	public <T extends Entity> void deleteAll(Class<T> cls) {
		if (! entities.containsKey(cls) || entities.get(cls) == null)
			return; // No elements stored
		
		entities.get(cls).clear();
	}
}
