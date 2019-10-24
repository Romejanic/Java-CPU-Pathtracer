package com.jack.pathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.jack.pathtracer.math.Ray;
import com.jack.pathtracer.math.Vec3;
import com.jack.pathtracer.scene.Camera;
import com.jack.pathtracer.scene.Material;
import com.jack.pathtracer.scene.Scene;
import com.jack.pathtracer.scene.Scene.SceneSettings;
import com.jack.pathtracer.scene.gen.RoomGenerator;
import com.jack.pathtracer.scene.objects.Sphere;

public class Pathtracer {

	public static void main(String[] args) throws Exception {
		Map<String,String> options = parseOptions(args);
		SceneSettings settings = new SceneSettings();
		
		int width = parseInt(options.get("width"), 1280);
		int height = parseInt(options.get("height"), 720);
		boolean hideBar = options.containsKey("hide-bar") ? options.get("hide-bar").equalsIgnoreCase("true") : false;
		settings.maxBounces = parseInt(options.get("bounces"), 8);
		settings.numSamples = parseInt(options.get("samples"), 8);
		settings.skyColor.mul(0f); // make sky black
		
		Scene scene = new Scene();
		Camera cam  = new Camera(new Vec3(0f, 0f, -3f));
		
		// generate room
		RoomGenerator.populate(scene, 4f, 2f);
		
		// add sphere
		Material mat = new Material(new Vec3(1f), new Vec3(0f), 0.1f);
		Sphere sph = new Sphere(new Vec3(0f), 1f, mat);
		scene.addObject(sph);
		
		// add lights
		Material mat2 = new Material(new Vec3(0f), new Vec3(20f), 0f);
		Sphere light = new Sphere(new Vec3(2f,1.2f,-0.5f), 0.4f, mat2);
		scene.addObject(light);
		
		// set up buffer
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		// draw scene
		// TODO: move this code to Scene class
		System.out.println("Starting render... ");
		ProgressBar progress = null;
		if(!hideBar) {
			progress = new ProgressBar("samples", width * height * settings.numSamples);
		}
		long start = System.currentTimeMillis();
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				Ray ray = cam.pixelToRay(x, y, width, height);
				Vec3 col = scene.trace(ray, settings, progress);
				img.setRGB(x, y, Vec3.toRGB(col));
			}
		}
		long time = System.currentTimeMillis() - start;
		if(!hideBar) progress.end();
		System.out.println("Done! Took: " + formatTime(time));
		
		// save to file
		ImageIO.write(img, "PNG", new File("render.png"));
	}
	
	private static String formatTime(long ms) {
		float timeTaken = (float)ms;
		String unit = "ms";
		if(timeTaken >= 1000) {
			unit = "s";
			timeTaken /= 1000;
			if(timeTaken >= 60) {
				unit = "m";
				timeTaken /= 60;
			}
		}
		return timeTaken + unit;
	}
	
	private static Map<String,String> parseOptions(String[] args) {
		Map<String,String> ops = new HashMap<String,String>();
		for(String arg : args) {
			int idx = arg.indexOf("=");
			if(!arg.startsWith("--") || idx < 0) continue;
			String key = arg.substring(2, idx);
			String val = arg.substring(idx+1);
			ops.put(key, val);
		}
		return ops;
	}
	
	private static int parseInt(String in, int def) {
		try {
			return Integer.parseInt(in);
		} catch(NumberFormatException e) {
			return def;
		}
	}
	
}