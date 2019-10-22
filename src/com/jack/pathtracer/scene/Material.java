package com.jack.pathtracer.scene;

import com.jack.pathtracer.math.Vec3;

public class Material {

	private Vec3 albedo;
	private Vec3 emission;
	private float reflectivity;
	
	public Material(Vec3 albedo, Vec3 emission, float reflectivity) {
		this.albedo = albedo;
		this.emission = emission;
		this.reflectivity = reflectivity;
	}
	
	public Vec3 getAlbedo() {
		return this.albedo;
	}
	
	public Vec3 getEmission() {
		return this.emission;
	}
	
	public float getReflectivity() {
		return this.reflectivity;
	}
	
	public boolean sampleReflective() {
		return this.getReflectivity() < Math.random();
	}
	
}