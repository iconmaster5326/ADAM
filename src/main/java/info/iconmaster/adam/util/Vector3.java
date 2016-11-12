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
}
