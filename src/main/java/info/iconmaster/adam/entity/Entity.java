package info.iconmaster.adam.entity;

import java.awt.Graphics;
import java.awt.Point;

import info.iconmaster.adam.game.AdamGame;
import info.iconmaster.adam.util.WorldCoord;

/**
 * The base class for an interactable entity in ADAM.
 * @author iconmaster
 *
 */
public interface Entity {
	public WorldCoord getSize();
	public double getMass();
	public default double getVolume() {
		WorldCoord size = getSize();
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
	
	public void draw(AdamGame game, Graphics g, int x, int y, double pixPerUnit, Assembly.JointType side);
	public Point drawSize(AdamGame game, double pixPerUnit, Assembly.JointType side);
}
