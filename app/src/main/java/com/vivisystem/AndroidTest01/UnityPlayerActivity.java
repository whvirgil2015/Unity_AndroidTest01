package com.vivisystem.AndroidTest01;

import com.unity3d.player.*;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

public class UnityPlayerActivity extends Activity
{
	protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code

	// Setup activity layout
//	@Override protected void onCreate (Bundle savedInstanceState)
//	{
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		super.onCreate(savedInstanceState);
//
//		getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy
//
//		mUnityPlayer = new UnityPlayer(this);
//		setContentView(mUnityPlayer);
//		mUnityPlayer.requestFocus();
//	}
	// Setup activity layout
	@Override protected void onCreate (Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unity);

		getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

		mUnityPlayer = new UnityPlayer(this);
		int glesMode = mUnityPlayer.getSettings().getInt("gles_mode", 1);
		mUnityPlayer.init(glesMode, false);

		FrameLayout layout = (FrameLayout) findViewById(R.id.unityView);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		layout.addView(mUnityPlayer, 0, lp);
//		mUnityPlayer.windowFocusChanged(true);
		mUnityPlayer.resume();

		mButtonZoomIn = (Button)findViewById(R.id.zoomInButton);
		mButtonZoomIn.setOnClickListener(mButtonZoomInOnClickListener);
		mButtonZoomOut = (Button)findViewById(R.id.zoomOutButton);
		mButtonZoomOut.setOnClickListener(mButtonZoomOutOnClickListener);

	}

	// Quit Unity
	@Override protected void onDestroy ()
	{
		mUnityPlayer.quit();
		super.onDestroy();
	}

	// Pause Unity
	@Override protected void onPause()
	{
		super.onPause();
		mUnityPlayer.pause();
	}

	// Resume Unity
	@Override protected void onResume()
	{
		super.onResume();
		mUnityPlayer.resume();
	}

	// This ensures the layout will be correct.
	@Override public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mUnityPlayer.configurationChanged(newConfig);
	}

	// Notify Unity of the focus change.
	@Override public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		mUnityPlayer.windowFocusChanged(hasFocus);
	}

	// For some reason the multiple keyevent type is not supported by the ndk.
	// Force event injection by overriding dispatchKeyEvent().
	@Override public boolean dispatchKeyEvent(KeyEvent event)
	{
		if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
			return mUnityPlayer.injectEvent(event);
		return super.dispatchKeyEvent(event);
	}

	// Pass any events not handled by (unfocused) views straight to UnityPlayer
	@Override public boolean onKeyUp(int keyCode, KeyEvent event){
		Log.d("Hughie", "onKeyUp keyCode=" + keyCode + " event=" + event);
		return mUnityPlayer.injectEvent(event);
	}
	@Override public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d("Hughie", "onKeyDown keyCode=" + keyCode + " event=" + event);
		return mUnityPlayer.injectEvent(event);
	}
	@Override public boolean onTouchEvent(MotionEvent event) {
		Log.d("Hughie", "onTouchEvent event=" + event);

		return mUnityPlayer.injectEvent(event);
	}
	/*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }


	private Button mButtonZoomIn;
	private View.OnClickListener mButtonZoomInOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("Hughie", "Zoom In Button onClick");
			mUnityPlayer.UnitySendMessage("Manager", "ZoomIn", "");
		}
	};
	private Button mButtonZoomOut;
	private View.OnClickListener mButtonZoomOutOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.d("Hughie", "Zoom Out Button onClick");
			mUnityPlayer.UnitySendMessage("Manager", "ZoomOut", "");
		}
	};
}
