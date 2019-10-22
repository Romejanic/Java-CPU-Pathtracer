package com.jack.pathtracer.math;

public class Ray {

	public Vec3 origin;
	public Vec3 direction;
	
	public Ray(Vec3 origin, Vec3 direction) {
		this.origin = origin;
		this.direction = direction;
		// normalize direction if it isn't already
		if(this.direction.lengthSqr() != 1f) {
			this.direction = Vec3.normalize(this.direction);
		}
	}
	
	public Vec3 getPoint(float distance) {
		return Vec3.add(this.origin, Vec3.mul(this.direction, distance));
	}
	
}