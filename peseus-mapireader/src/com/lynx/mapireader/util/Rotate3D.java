package com.lynx.mapireader.util;

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
	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;

	private float fromDegree; // 旋转起始角度
	private float toDegree; // 旋转终止角度
	private float mCenterX; // 旋转中心x
	private float mCenterY; // 旋转中心y
	private int orientation = VERTICAL; // 旋转方向
	private Camera mCamera;

	public Rotate3D(float fromDegree, float toDegree, float centerX,
			float centerY, int orientation) {
		this.fromDegree = fromDegree;
		this.toDegree = toDegree;
		this.mCenterX = centerX;
		this.mCenterY = centerY;
		this.orientation = orientation;
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
	}

	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		if (orientation == VERTICAL) {
			vertailRotate(interpolatedTime, t);
		} else if (orientation == HORIZONTAL) {
			horizontalRotate(interpolatedTime, t);
		}
	}

	private void horizontalRotate(float interpolatedTime, Transformation t) {
		final float FromDegree = fromDegree;
		float degrees = FromDegree + (toDegree - fromDegree) * interpolatedTime; // 旋转角度（angle）
		final float centerX = mCenterX;
		final float centerY = mCenterY;
		final Matrix matrix = t.getMatrix();

		if (degrees <= -76.0f) {
			degrees = -90.0f;
			mCamera.save();
			mCamera.rotateY(degrees); // 旋转
			mCamera.getMatrix(matrix);
			mCamera.restore();
		} else if (degrees >= 76.0f) {
			degrees = 90.0f;
			mCamera.save();
			mCamera.rotateY(degrees);
			mCamera.getMatrix(matrix);
			mCamera.restore();
		} else {
			mCamera.save();
			mCamera.translate(0, 0, centerX); // 位移x
			mCamera.rotateY(degrees);
			mCamera.translate(0, 0, -centerX);
			mCamera.getMatrix(matrix);
			mCamera.restore();
		}

		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}

	private void vertailRotate(float interpolatedTime, Transformation t) {
		final float FromDegree = fromDegree;
		float degrees = FromDegree + (toDegree - fromDegree) * interpolatedTime; // 旋转角度（angle）
		final float centerX = mCenterX;
		final float centerY = mCenterY;
		final Matrix matrix = t.getMatrix();

		if (degrees <= -76.0f) {
			degrees = -90.0f;
			mCamera.save();
			mCamera.rotateX(degrees); // 旋转
			mCamera.getMatrix(matrix);
			mCamera.restore();
		} else if (degrees >= 76.0f) {
			degrees = 90.0f;
			mCamera.save();
			mCamera.rotateX(degrees);
			mCamera.getMatrix(matrix);
			mCamera.restore();
		} else {
			mCamera.save();
			mCamera.translate(0, centerY, centerX); // 位移x
			mCamera.rotateX(degrees);
			mCamera.translate(0, -centerY, -centerX);
			mCamera.getMatrix(matrix);
			mCamera.restore();
		}

		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}
}