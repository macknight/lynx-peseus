package com.cop.carmonitor.util;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 
 * @author chris.liu
 *
 */
public class Rotate3D extends Animation {
	private float fromDegree;
	private float toDegree;
	private float centerX;
	private float centerY;
	private Camera camera;
	
	public Rotate3D(float fromDegree, float toDegree, float centerX, float centerY) {
		this.fromDegree = fromDegree;
		this.toDegree = toDegree;
		this.centerX = centerX;
		this.centerY = centerY;
	}
	
	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		camera = new Camera();
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final float FromDegree = fromDegree;
		float degrees = FromDegree + (toDegree - fromDegree) * interpolatedTime;
		
		final float x = centerX;
		final float y = centerY;
		final Matrix matrix = t.getMatrix();
		
		if (degrees <= -76.0f) {
			degrees = -90.0f;
			camera.save();
			camera.rotateY(degrees);
			camera.getMatrix(matrix);
			camera.restore();
		} else if (degrees >= 76.0f) {
			degrees = 90.0f;
			camera.save();
			camera.rotateY(degrees);
			camera.getMatrix(matrix);
			camera.restore();
		} else {
			camera.save();
			camera.translate(0, 0, x);
			camera.rotateY(degrees);
			camera.translate(0, 0, -x);
			camera.getMatrix(matrix);
			camera.restore();
		}
		
		matrix.preTranslate(-x, -y);
		matrix.postTranslate(x, y);
	}
}
