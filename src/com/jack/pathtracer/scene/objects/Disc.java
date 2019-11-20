package com.jack.pathtracer.scene.objects;

import com.jack.pathtracer.math.Ray;
import com.jack.pathtracer.math.Vec3;
import com.jack.pathtracer.scene.Material;
import com.jack.pathtracer.scene.Scene;

public class Disc extends Plane {
	
	private float radius;
	
	public Disc(Vec3 position, Vec3 normal, float radius, Material material) {
		super(normal, position, material);
		this.setRadius(radius);
	}

	public float getRadius() {
		return this.radius;
	}
	
	public void setRadius(float radius) {
		this.radius = Math.max(radius, 0f);
	}
	
	//-----------------------------------------//
	
	@Override
	public float intersect(Ray ray, Scene scene) {
		float t = super.intersect(ray, scene);
		if(t >= 0f) {
			// if there's an intersection with the base plane, then also check that
			// the point is within the radius of the point
			float d = Vec3.sub(ray.getPoint(t), this.getOffset()).length();
			return d <= this.radius ? t : -1f;
		}
		return t;
	}
	
}