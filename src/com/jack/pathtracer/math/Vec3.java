package com.jack.pathtracer.math;

public class Vec3 {

	public float x, y, z;
	
	public Vec3() {
		this(0f);
	}
	
	public Vec3(float x) {
		this(x,x,x);
	}
	
	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3(Vec3 v) {
		this(v.x, v.y, v.z);
	}
	
	public Vec3 clone() {
		return new Vec3(this);
	}
	
	public float lengthSqr() {
		return this.x*this.x + this.y*this.y + this.z*this.z;
	}
	
	public float length() {
		return (float)Math.sqrt((double)this.lengthSqr());
	}
	
	/**
	 * Adds the vector b to this vector **without** creating a new 
	 * Vec3 instance.
	 * @param other The vector to add to this vector.
	 * @return This vector.
	 */
	public Vec3 add(Vec3 other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		return this;
	}
	
	/**
	 * Multiplies this vector by b **without** creating a new 
	 * Vec3 instance.
	 * @param other The vector to multiply with this vector.
	 * @return This vector.
	 */
	public Vec3 mul(Vec3 other) {
		this.x *= other.x;
		this.y *= other.y;
		this.z *= other.z;
		return this;
	}
	
	/**
	 * Multiplies this vector by b **without** creating a new 
	 * Vec3 instance.
	 * @param other The vector to multiply with this vector.
	 * @return This vector.
	 */
	public Vec3 mul(float other) {
		this.x *= other;
		this.y *= other;
		this.z *= other;
		return this;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[x=" + this.x + ",y=" + this.y + ",z=" + this.z + "]";
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		if(!(other instanceof Vec3)) return false;
		Vec3 v = (Vec3)other;
		return v.x == this.x && v.y == this.y && v.z == this.z;
	}
	
	public static Vec3 add(Vec3 a, Vec3 b) {
		return a.clone().add(b);
	}
	
	public static Vec3 add(Vec3 a, float b) {
		return new Vec3(a.x + b, a.y + b, a.z + b);
	}
	
	public static Vec3 sub(Vec3 a, Vec3 b) {
		return new Vec3(a.x - b.x, a.y - b.y, a.z - b.z);
	}
	
	public static Vec3 sub(Vec3 a, float b) {
		return new Vec3(a.x - b, a.y - b, a.z - b);
	}
	
	public static Vec3 mul(Vec3 a, Vec3 b) {
		return new Vec3(a.x * b.x, a.y * b.y, a.z * b.z);
	}
	
	public static Vec3 mul(Vec3 a, float b) {
		return new Vec3(a.x * b, a.y * b, a.z * b);
	}
	
	public static Vec3 div(Vec3 a, Vec3 b) {
		return new Vec3(a.x / b.x, a.y / b.y, a.z / b.z);
	}
	
	public static Vec3 div(Vec3 a, float b) {
		return new Vec3(a.x / b, a.y / b, a.z / b);
	}
	
	public static float dot(Vec3 a, Vec3 b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}
	
	public static Vec3 normalize(Vec3 v) {
		// if it's already normalized don't do anything
		if(v.lengthSqr() == 1f) return v.clone();
		return mul(v, 1f / v.length());
	}
	
	public static Vec3 cross(Vec3 a, Vec3 b) {
		return new Vec3(
			a.y*b.z - a.z*b.y,
			a.z*b.x - a.x*b.z,
			a.x*b.y - a.y*b.x
		);
	}
	
	public static int toRGB(Vec3 col) {
		int r = (int)Math.floor(Math.max(0d, Math.min(1d, (double)col.x)) * 255d);
		int g = (int)Math.floor(Math.max(0d, Math.min(1d, (double)col.y)) * 255d);
		int b = (int)Math.floor(Math.max(0d, Math.min(1d, (double)col.z)) * 255d);
		return (r << 16) + (g << 8) + b;
	}
	
	/*
	// dirty dirty hack
	// but maybe it works?
	public static Vec3 randomPointHemisphere(Vec3 normal) {
		Vec3 v = new Vec3();
		do {
			v.x = (float)(2d * Math.random() - 1d);
			v.y = (float)(2d * Math.random() - 1d);
			v.z = (float)(2d * Math.random() - 1d);
			v   = normalize(v);
		} while(dot(v,normal) < 0f);
		return v;
	}
	*/
	
	/**
	 * Calculates a random direction vector inside the hemisphere
	 * oriented by the given normal. This method can be used to
	 * calculate a diffuse light vector.
	 * @param normal The normal to orient the hemisphere to.
	 * @return A random direction inside the hemisphere.
	 */
	public static Vec3 randomPointHemisphere(Vec3 normal) {
		float r1 = (float)(2d * Math.PI * Math.random());
		float r2 = (float)Math.random();
		float r2s = (float)Math.sqrt(r2);
			
		Vec3 w = normal.clone();
		Vec3 u = normalize(cross(Math.abs(w.x) > 0.1f ? new Vec3(0f, 1f, 0f) : new Vec3(1f, 0f, 0f), w));
		Vec3 v = cross(w, u);
		Vec3 d = add(add(mul(u,(float)Math.cos(r1)*r2s), mul(v,(float)Math.sin(r1)*r2s)), mul(w,(float)Math.sqrt(1f-r2)));
		
		return normalize(d);
	}
	
	/**
	 * Calculates the reflection vector of the given
	 * incident vector and the given normal vector. It is
	 * the product of I - 2.0 * dot(N, I) * N;
	 * @param i The incident vector.
	 * @param n The normal vector.
	 * @return The reflected vector of the given incident
	 * and normal vectors.
	 */
	public static Vec3 reflect(Vec3 i, Vec3 n) {
		return Vec3.sub(i, Vec3.mul(n, 2f * dot(n, i)));
	}
	
	public static Vec3 mix(Vec3 a, Vec3 b, float x) {
		x = Math.max(0f, Math.min(1f, x));
		float nx = 1f - x;
		return Vec3.add(Vec3.mul(a, nx), Vec3.mul(b, x));
	}
	
}