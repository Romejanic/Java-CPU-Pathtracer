package com.jack.pathtracer.scene;

import com.jack.pathtracer.math.Ray;
import com.jack.pathtracer.math.Vec3;

public class Camera {

	public Vec3 position;
	
	public Camera(Vec3 position) {
		this.position = position;
	}
	
	public Ray pixelToRay(int x, int y, int w, int h) {
		return new Ray(
			this.position,
			new Vec3((x-w*0.5f)/(float)h,-(y-h*0.5f)/(float)h,1f)
		);
	}
	
}