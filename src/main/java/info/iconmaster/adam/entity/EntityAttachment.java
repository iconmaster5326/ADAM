package info.iconmaster.adam.entity;

public class EntityAttachment {
	public static enum Type {
		INSIDE,
		OUTSIDE,
		FRONT,
		BACK,
		TOP,
		BOTTOM,
		LEFT,
		RIGHT;
	}
	
	public Entity attachedFrom;
	public Type type;
	public Entity attachedTo;
}
