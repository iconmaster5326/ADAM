package info.iconmaster.adam.entity;

import info.iconmaster.adam.util.Vector3;

/**
 * The base class for an interactable entity in ADAM.
 * @author iconmaster
 *
 */
public interface Entity {
	public Vector3 getSize();
	public double getMass();
	public default double getVolume() {
		Vector3 size = getSize();
		return size.x*size.y*size.z;
	}
	public default double getDensity() {
		return getMass()/getVolume();
	}
	
	/**
	 * Returns the entity's primary name.
	 * @return
	 */
	public String getName();
	/**
	 * Sets the entity's primary name.
	 * @return
	 */
	public void setName(String name);
	
	/**
	 * A "parent" object is for bookkeeping information. For example, entities are part of assemblies or worlds or etc.
	 * @return
	 */
	public Object getParent();
	public void setParent(Object entity);
	
	public default <E> E getParentWithClass(Class<E> clazz) {
		Object parent = getParent();
		while (parent != null) {
			if (parent.getClass().equals(clazz)) {
				return (E) parent;
			} else if (parent instanceof Chunk) {
				parent = ((Chunk)parent).world;
			} else if (parent instanceof Entity) {
				parent = ((Entity)parent).getParent();
			} else {
				return null;
			}
		}
		return null;
	}
}
