package info.iconmaster.adam.entity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * The base class for an interactable entity in ADAM.
 * @author iconmaster
 *
 */
public interface Entity {
	public double getSize();
	public double getMass();
	public default double getDensity() {
		return getMass()/getSize();
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
	 * Returns the color for drawing, or null if invisible.
	 * @return
	 */
	public Color getColor();
	
	/**
	 * Returns a list of all attachments. Any modification to this list should be reflected in the Entity.
	 * @return
	 */
	public List<EntityAttachment> getAttachments();
	
	/**
	 * Returns all the parts of an entity of a certain attachment type.
	 * @param type
	 * @return
	 */
	public default List<EntityAttachment> getAttachments(EntityAttachment.Type type) {
		ArrayList<EntityAttachment> a = new ArrayList<>();
		for (EntityAttachment att : getAttachments()) {
			if (att.type == type) a.add(att);
		}
		return a;
	}
}
