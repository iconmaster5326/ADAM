package info.iconmaster.adam.util;

public class Vector3 {
	public final double x,y,z;

	public Vector3(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3 add(double x, double y, double z) {
		return new Vector3(this.x+x, this.y+y, this.z+z);
	}
	
	public Vector3 add(Vector3 other) {
		return new Vector3(this.x+other.x, this.y+other.y, this.z+other.z);
	}
	
	public Vector3 sub(double x, double y, double z) {
		return new Vector3(this.x-x, this.y-y, this.z-z);
	}
	
	public Vector3 sub(Vector3 other) {
		return new Vector3(this.x-other.x, this.y-other.y, this.z-other.z);
	}
	
	public Vector3 mul(double n) {
		return new Vector3(this.x*n, this.y*n, this.z*n);
	}
	
	public Vector3 mul(double x, double y, double z) {
		return new Vector3(this.x*x, this.y*y, this.z*z);
	}
	
	public Vector3 mul(Vector3 other) {
		return new Vector3(this.x*other.x, this.y*other.y, this.z*other.z);
	}
	
	public Vector3 mod(double n) {
		return new Vector3(this.x%n, this.y%n, this.z%n);
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
		Vector3 other = (Vector3) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}
}
