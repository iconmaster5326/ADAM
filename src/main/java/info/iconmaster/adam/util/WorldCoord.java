package info.iconmaster.adam.util;

public class WorldCoord {
	public final double x,y,z;

	public WorldCoord(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public WorldCoord add(double x, double y, double z) {
		return new WorldCoord(this.x+x, this.y+y, this.z+z);
	}
	
	public WorldCoord add(WorldCoord other) {
		return new WorldCoord(this.x+other.x, this.y+other.y, this.z+other.z);
	}
	
	public WorldCoord sub(double x, double y, double z) {
		return new WorldCoord(this.x-x, this.y-y, this.z-z);
	}
	
	public WorldCoord sub(WorldCoord other) {
		return new WorldCoord(this.x-other.x, this.y-other.y, this.z-other.z);
	}
	
	public WorldCoord mul(double n) {
		return new WorldCoord(this.x*n, this.y*n, this.z*n);
	}
	
	public WorldCoord mul(double x, double y, double z) {
		return new WorldCoord(this.x*x, this.y*y, this.z*z);
	}
	
	public WorldCoord mul(WorldCoord other) {
		return new WorldCoord(this.x*other.x, this.y*other.y, this.z*other.z);
	}
	
	public WorldCoord mod(double n) {
		return new WorldCoord(this.x%n, this.y%n, this.z%n);
	}

	@Override
	public String toString() {
		return "Vector3 [x=" + x + ", y=" + y + ", z=" + z + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		WorldCoord other = (WorldCoord) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}
}
