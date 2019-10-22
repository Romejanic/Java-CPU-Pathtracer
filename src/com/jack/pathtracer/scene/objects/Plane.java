package com.jack.pathtracer.scene.objects;

import com.jack.pathtracer.math.Ray;
import com.jack.pathtracer.math.Vec3;
import com.jack.pathtracer.scene.Material;
import com.jack.pathtracer.scene.Scene;
import com.jack.pathtracer.scene.SceneObject;

public class Plane implements SceneObject {
	
	private Vec3 normal;
	private Vec3 offset;
	private Material material;
	
	public Plane(Vec3 normal, Vec3 offset, Material material) {
		this.setNormal(normal);
		this.offset = offset;
		this.material = material;
	}
	
	public void setNormal(Vec3 normal) {
		this.normal = normal;
		// if not normalized, normalize it
		if(this.normal.lengthSqr() != 1f) {
			this.normal = Vec3.normalize(this.normal);
		}
	}
	
	public Vec3 getNormal() {
		return this.normal;
	}
	
	public void setOffset(Vec3 offset) {
		this.offset = offset;
	}
	
	public Vec3 getOffset() {
		return this.offset;
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
		float num = -Vec3.dot(this.normal, Vec3.sub(ray.origin, this.offset));
		float den = Vec3.dot(this.normal, ray.direction);
		float t = num / den;
		return t >= 0f ? t : -1f;
	}

	@Override
	public Vec3 getNormal(Vec3 p, float t, Scene scene) {
		return this.normal;
	}

	@Override
	public Material getMaterial(Vec3 p, Vec3 n, float t, Scene scene) {
		return this.material;
	}

}
