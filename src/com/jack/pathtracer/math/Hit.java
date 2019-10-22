package com.jack.pathtracer.math;

import com.jack.pathtracer.scene.SceneObject;

public class Hit {

	public SceneObject object;
	public float distance;
	
	public Hit(SceneObject object, float distance) {
		this.object = object;
		this.distance = distance;
	}
	
}