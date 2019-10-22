package com.jack.pathtracer.scene;

import com.jack.pathtracer.math.Ray;
import com.jack.pathtracer.math.Vec3;

public interface SceneObject {

	/**
	 * Calculates the intersection distance between the ray's origin
	 * and the point of intersection on the object's surface.
	 * @param ray The ray to test for intersection.
	 * @param scene The scene the object belongs to.
	 * @return The distance along the ray of the intersection point,
	 * or -1 if the ray missed the object.
	 */
	float intersect(Ray ray, Scene scene);
	
	/**
	 * Calculates the normal vector at the given point on the surface
	 * of the object.
	 * @param p The point on the surface.
	 * @param t The intersection distance of that point.
	 * @param scene The scene the object belongs to.
	 * @return The normalized normal vector of the given point on the surface.
	 */
	Vec3 getNormal(Vec3 p, float t, Scene scene);
	
	/**
	 * Gets the material of the object at the given point on the surface
	 * of the object.
	 * @param p The point on the surface.
	 * @param n The normal at the point on the surface.
	 * @param t The intersection distance of that point.
	 * @param scene The scene the object belongs to.
	 * @return A Material object representing the material of that point
	 * on the surface.
	 */
	Material getMaterial(Vec3 p, Vec3 n, float t, Scene scene);
	
}