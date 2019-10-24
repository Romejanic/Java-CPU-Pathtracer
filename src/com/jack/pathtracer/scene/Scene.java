package com.jack.pathtracer.scene;

import java.util.ArrayList;
import java.util.List;

import com.jack.pathtracer.ProgressBar;
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
	 * @param settings The scene settings to use.
	 * @param depth The current bounce index we're up to.
	 * @return The color of the given ray, as a HDR color vector.
	 */
	public Vec3 getRayColor(Ray ray, SceneSettings settings, int depth) {
		// if we reach max recursion depth, bail out
		if(depth > settings.maxBounces) return new Vec3(0f);

		Hit t = this.intersect(ray);
		if(t.object == null) {
			// if we miss, add the sky color and bail out
			return settings.skyColor.clone();
		}

		Vec3 p = ray.getPoint(t.distance);
		Vec3 n = t.object.getNormal(p, t.distance, this);
		Material m = t.object.getMaterial(p, n, t.distance, this);

		// add emissive color
		Vec3 col = m.getEmission().clone();

		// calculate the new ray
		float shade = 0f;
		Vec3 d = new Vec3();
		do {
			d.x = 2f * (float)Math.random() - 1f;
			d.y = 2f * (float)Math.random() - 1f;
			d.z = 2f * (float)Math.random() - 1f;
			d = Vec3.normalize(d);
		} while((shade = Vec3.dot(d, n)) <= 0f);

		ray = new Ray(
			Vec3.add(p, Vec3.mul(d, 0.01f)), // origin
			d                                // direction
		);
		
		// adjust mask to add attenuation
		Vec3 mask = new Vec3(1f);
		mask.mul(m.getAlbedo());
		mask.mul(2f * shade);

		// if the mask is black, bail out early and don't waste
		// time on bounces (since it won't be visible anyway)
		if(mask.lengthSqr() == 0f) {
			return col;
		}
		
		// get secondary ray color and return weighed color
		Vec3 bounceCol = getRayColor(ray, settings, depth+1);
		return col.add(bounceCol.mul(mask));
	}

	public Vec3 trace(Ray ray, SceneSettings settings, ProgressBar bar) {
		Vec3 col = new Vec3(0f);
		for(int i = 0; i < settings.numSamples; i++) {
			col.add(getRayColor(ray, settings, 0));
			if(bar != null) bar.increment();
		}
		return Vec3.mul(col, 1f / settings.numSamples);
	}
	
	public Vec3 trace(Ray ray, SceneSettings settings) {
		return trace(ray, settings, null);
	}

	public static class SceneSettings {
		public int maxBounces = 8;
		public int numSamples = 1;
		public Vec3 skyColor  = new Vec3(0.4f, 0.6f, 0.9f);
	}

}
