package com.jack.pathtracer.scene.objects;

import com.jack.pathtracer.math.Ray;
import com.jack.pathtracer.math.Vec3;
import com.jack.pathtracer.scene.Material;
import com.jack.pathtracer.scene.Scene;
import com.jack.pathtracer.scene.SceneObject;

public class Sphere implements SceneObject {
	
	private Vec3 position;
	private float radius;
	private Material material;
	
	public Sphere(Vec3 position, float radius, Material material) {
		this.position = position;
		this.radius = radius;
		this.material = material;
	}
	
	public void setPosition(Vec3 position) {
		this.position = position;
	}
	
	public Vec3 getPosition() {
		return this.position;
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public float getRadius() {
		return this.radius;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public Material getMaterial() {
		return this.material;
	}
	
	//----------------------------------------------------------//

	@Override
	public float intersect(Ray ray, Scene scene) {
		Vec3 oc = Vec3.sub(ray.origin, this.position);
		float a = Vec3.dot(ray.direction, ray.direction);
		float b = 2f * Vec3.dot(ray.direction, oc);
		float c = Vec3.dot(oc, oc) - this.radius*this.radius;
		float d = b*b - 4f*a*c;
		return d < 0f ? -1f : (-b - (float)Math.sqrt((double)d)) / (2f * a);
	}

	@Override
	public Vec3 getNormal(Vec3 p, float t, Scene scene) {
		return Vec3.normalize(Vec3.sub(p, this.position));
	}

	@Override
	public Material getMaterial(Vec3 p, Vec3 n, float t, Scene scene) {
		return this.material;
	}

}
