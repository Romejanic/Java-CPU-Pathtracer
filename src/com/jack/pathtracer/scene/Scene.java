package com.jack.pathtracer.scene;

import java.util.ArrayList;
import java.util.List;

import com.jack.pathtracer.math.Hit;
import com.jack.pathtracer.math.Ray;
import com.jack.pathtracer.math.Vec3;

public class Scene {

	private final List<SceneObject> objects = new ArrayList<SceneObject>();
	
	public Scene(SceneObject... objs) {
		for(SceneObject obj : objs) {
			// use method so null objects are ignored
			addObject(obj);
		}
	}
	
	public void addObject(SceneObject object) {
		if(object == null) return;
		this.objects.add(object);
	}
	
	public SceneObject removeObject(SceneObject object) {
		if(objects.contains(object)) {
			objects.remove(object);
			return object;
		} else {
			return null;
		}
	}
	
	/**
	 * Finds the closest object which intersects the given ray.
	 * @param ray The ray to test for intersections.
	 * @return The closest SceneObject which intersects the ray, null if
	 * no object was found.
	 */
	public Hit intersect(Ray ray) {
		float distance = -1f;
		SceneObject closest = null;
		for(SceneObject obj : this.objects) {
			if(obj == null) continue;
			float dst = obj.intersect(ray, this);
			if(dst >= 0f && (closest == null || dst < distance)) {
				distance = dst;
				closest = obj;
			}
		}
		return new Hit(closest, distance);
	}
	
	/**
	 * Path traces the given ray and calculates the color for
	 * that ray.
	 * @param ray The ray to calculate the color of.
	 * @return The color of the given ray, as a HDR color vector.
	 */
	public Vec3 getRayColor(Ray ray, SceneSettings settings) {
		Vec3 col = new Vec3(0f);
		Vec3 mask = new Vec3(1f);
		
		for(int i = 0; i < settings.maxBounces; i++) {
			Hit t = this.intersect(ray);
			if(t.object == null) {
				// if we miss, add the sky color and bail out
				col.add(Vec3.mul(settings.skyColor, mask));
				break;
			}
			
			Vec3 p = ray.getPoint(t.distance);
			Vec3 n = t.object.getNormal(p, t.distance, this);
			Material m = t.object.getMaterial(p, n, t.distance, this);
			
			// add emissive color
			col.add(Vec3.mul(mask, m.getEmission()));
			
			// calculate the new ray
//			Vec3 d = Vec3.mix(Vec3.randomPointHemisphere(n), Vec3.reflect(ray.direction, n), m.getReflectivity());
			float shade = 0f;
			Vec3 d = new Vec3();
			do {
				d.x = 2f * (float)Math.random() - 1f;
				d.y = 2f * (float)Math.random() - 1f;
				d.z = 2f * (float)Math.random() - 1f;
			} while((shade = Vec3.dot(d, n)) <= 0f);
			
			ray = new Ray(
				Vec3.add(p, Vec3.mul(d, 0.01f)), // origin
				Vec3.normalize(d)                // direction
			);
			
			// adjust mask to add attenuation
			mask.mul(m.getAlbedo());
			//mask.mul(Vec3.dot(n, d));
			mask.mul(2f * shade);
		}
		
		return col;
	}
	
	public Vec3 trace(Ray ray, SceneSettings settings) {
		Vec3 col = new Vec3(0f);
		for(int i = 0; i < settings.numSamples; i++) {
			col.add(getRayColor(ray, settings));
		}
		return Vec3.mul(col, 1f / settings.numSamples);
	}
	
	public static class SceneSettings {
		public int maxBounces = 8;
		public int numSamples = 1;
		public Vec3 skyColor  = new Vec3(0.4f, 0.6f, 0.9f);
	}
	
}