package org.anddev.andengine.extension.ui.livewallpaper;

import net.rbgrn.opengl.GLWallpaperService;

import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.view.RenderSurfaceView;
import org.anddev.andengine.opengl.view.GLSurfaceView.Renderer;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.sensor.orientation.IOrientationListener;
import org.anddev.andengine.ui.IGameInterface;

import android.app.WallpaperManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;

public abstract class BaseLiveWallpaperService extends GLWallpaperService implements IGameInterface {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private org.anddev.andengine.engine.Engine mEngine;

	// ===========================================================
	// Constructors
	// ===========================================================

	@Override
	public void onCreate() {
		super.onCreate();

		this.mEngine = this.onLoadEngine();
		this.applyEngineOptions(this.mEngine.getEngineOptions());

		this.onLoadResources();
		final Scene scene = this.onLoadScene();
		this.mEngine.onLoadComplete(scene);
		this.onLoadComplete();
		this.mEngine.start();
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	public org.anddev.andengine.engine.Engine getEngine() {
		return this.mEngine;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	protected void onPause(){
		this.mEngine.stop();
	}

	protected void onResume(){
		this.mEngine.start();
	}

	@Override
	public Engine onCreateEngine() {
		return new BaseWallpaperGLEngine();
	}

	@Override
	public void onConfigurationChanged (Configuration newConfig){
	}

	// ===========================================================
	// Methods
	// ===========================================================

	protected void onTap(final int pX, final int pY) {
		this.mEngine.onTouch(null, MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, pX, pY, 0));
	}

	protected void onDrop(final int pX, final int pY) {

	}

	protected void onOffsetsChanged(float xOffset, float yOffset,
			float xOffsetStep, float yOffsetStep, int xPixelOffset,
			int yPixelOffset) {           
	}

	protected void applyEngineOptions(final EngineOptions pEngineOptions) {

	}

	protected boolean enableVibrator() {
		return this.mEngine.enableVibrator(this);
	}

	protected boolean enableAccelerometerSensor(final IAccelerometerListener pAccelerometerListener) {
		return this.mEngine.enableAccelerometerSensor(this, pAccelerometerListener);
	}

	protected boolean enableOrientationSensor(final IOrientationListener pOrientationListener) {
		return this.mEngine.enableOrientationSensor(this, pOrientationListener);
	}



	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	protected class BaseWallpaperGLEngine extends GLEngine {
		// ===========================================================
		// Fields
		// ===========================================================

		private Renderer mRenderer;

		// ===========================================================
		// Constructors
		// ===========================================================

		public BaseWallpaperGLEngine() {
			this.setEGLConfigChooser(false);
			this.mRenderer = new RenderSurfaceView.Renderer(BaseLiveWallpaperService.this.mEngine);
			this.setRenderer(this.mRenderer);
			this.setRenderMode(RENDERMODE_CONTINUOUSLY);
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public Bundle onCommand(final String pAction, final int pX, final int pY, final int pZ, final Bundle pExtras, final boolean pResultRequested) {
			if(pAction.equals(WallpaperManager.COMMAND_TAP)) {
				BaseLiveWallpaperService.this.onTap(pX, pY);
			} else if (pAction.equals(WallpaperManager.COMMAND_DROP)) {
				BaseLiveWallpaperService.this.onDrop(pX, pY);
			}

			return super.onCommand(pAction, pX, pY, pZ, pExtras, pResultRequested);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset,
				float xOffsetStep, float yOffsetStep, int xPixelOffset,
				int yPixelOffset) {
			// TODO Auto-generated method stub
			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep,
					xPixelOffset, yPixelOffset);

			BaseLiveWallpaperService.this.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixelOffset, yPixelOffset);

		}

		@Override
		public void onResume() {
			super.onResume();
			BaseLiveWallpaperService.this.getEngine().onResume();
			BaseLiveWallpaperService.this.onResume();
		}

		@Override
		public void onPause() {
			super.onPause();
			BaseLiveWallpaperService.this.getEngine().onPause();
			BaseLiveWallpaperService.this.onPause();
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			if (this.mRenderer != null) {
				// mRenderer.release();
			}
			this.mRenderer = null;
		}
	}
}