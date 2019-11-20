package com.jack.pathtracer.scene.gen;

import com.jack.pathtracer.math.Vec3;
import com.jack.pathtracer.scene.Material;
import com.jack.pathtracer.scene.Scene;
import com.jack.pathtracer.scene.objects.Disc;
import com.jack.pathtracer.scene.objects.Plane;

public class RoomGenerator {
	
	private static final float LIGHT_OFFSET = 0.01f;
	private static final float LIGHT_FACTOR = 0.5f;

	public static void populate(Scene scene, float wallSpacing, float roofSpacing, Material roofLight) {
		// materials
		Material white = new Material(new Vec3(1f, 1f, 1f), new Vec3(0f), 0f);
		Material red   = new Material(new Vec3(1f, 0f, 0f), new Vec3(0f), 0f);
		Material green = new Material(new Vec3(0f, 1f, 0f), new Vec3(0f), 0f);
		
		// planes
		scene.addObject(new Plane(new Vec3(0f, 1f, 0f), new Vec3(0f, -roofSpacing, 0f), white)); // floor
		scene.addObject(new Plane(new Vec3(0f, -1f, 0f), new Vec3(0f, roofSpacing, 0f), white)); // ceiling
		scene.addObject(new Plane(new Vec3(1f, 0f, 0f), new Vec3(-wallSpacing, 0f, 0f), red)); // left wall
		scene.addObject(new Plane(new Vec3(-1f, 0f, 0f), new Vec3(wallSpacing, 0f, 0f), green)); // right wall
		scene.addObject(new Plane(new Vec3(0f, 0f, 1f), new Vec3(0f, 0f, -wallSpacing), white)); // back wall
		scene.addObject(new Plane(new Vec3(0f, 0f, -1f), new Vec3(0f, 0f, wallSpacing), white)); // front wall
		
		// light
		if(roofLight != null) {
			scene.addObject(new Disc(new Vec3(0f, roofSpacing-LIGHT_OFFSET, 0f), new Vec3(0f, -1f, 0f), wallSpacing * LIGHT_FACTOR, roofLight));
		}
	}
	
}