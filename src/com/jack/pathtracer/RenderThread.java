package com.jack.pathtracer;

import java.awt.image.BufferedImage;

import com.jack.pathtracer.math.Ray;
import com.jack.pathtracer.math.Vec3;
import com.jack.pathtracer.scene.Camera;
import com.jack.pathtracer.scene.Scene;
import com.jack.pathtracer.scene.Scene.SceneSettings;

public class RenderThread extends Thread {

	private final BufferedImage img;
	private final Camera cam;
	private final SceneSettings settings;
	private final Scene scene;
	private int xStart;
	private int xEnd;
	private int yStart;
	private int yEnd;
	
	public boolean isDone = false;
	
	public RenderThread(BufferedImage img, Camera cam, SceneSettings settings, Scene scene, int xStart, int xEnd, int yStart, int yEnd) {
		super("RenderThread: " + xStart + "-" + xEnd + ", " + yStart + "-" + yEnd);
		this.img = img;
		this.cam = cam;
		this.settings = settings;
		this.scene = scene;
		this.xStart = xStart;
		this.xEnd = xEnd;
		this.yStart = yStart;
		this.yEnd = yEnd;
	}
	
	@Override
	public void run() {
		int width = this.img.getWidth();
		int height = this.img.getHeight();
		for(int y = this.yStart; y < this.yEnd; y++) {
			for(int x = this.xStart; x < this.xEnd; x++) {
				Ray ray = this.cam.pixelToRay(x, y, width, height);
				Vec3 col = this.scene.trace(ray, this.settings);
				this.img.setRGB(x, y, Vec3.toRGB(col));
			}
		}
		this.isDone = true;
	}
	
}