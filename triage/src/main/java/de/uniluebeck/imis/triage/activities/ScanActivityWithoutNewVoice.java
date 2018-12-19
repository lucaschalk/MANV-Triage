package de.uniluebeck.imis.triage.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.zbar.android.scanner.CameraPreview;
import com.dm.zbar.android.scanner.ZBarConstants;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.view.WindowUtils;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.util.Calendar;
import java.util.Locale;

import de.uniluebeck.imis.triage.R;

/**
 * This is a modified version of ZBarScannerActivity from Dushyanth Maguluru.
 * ZBarScanner is under MIT license:
 * http://opensource.org/licenses/mit-license.php
 *
 * @author Henrik Berndt / Dushyanth Maguluru (ZBarScanner)
 *
 */
public class ScanActivityWithoutNewVoice extends Activity implements Camera.PreviewCallback,
		ZBarConstants {

	// Objects for tap menu interaction and audio
	private GestureDetector mGestureDetector;
	private AudioManager maManager;

	private CameraPreview mPreview;
	private Camera mCamera;
	private ImageScanner mScanner;
	private Handler mAutoFocusHandler;
	private LinearLayout scanLayout;
	private TextView headLeft;
	private TextView instructionText;
	private TextView footerRightTime;
	private boolean mPreviewing = true;

	static {
		System.loadLibrary("iconv");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// from Dushyanth Maguluru (ZBarScanner)
		if (!isCameraAvailable()) {
			// Cancel request if there is no rear-facing camera.
			cancelRequest();
			return;
		}

		// from Dushyanth Maguluru (ZBarScanner)
		// Create and configure the ImageScanner;
		setupScanner();

		// get a voice menu
		getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);

		// Set the Layout
		setContentView(R.layout.scanactivity);

		// initialize the audio manager
		maManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// create gesture listener for touchpad menu interaction
		mGestureDetector = createGestureDetector(this);

		// Get and fill the text field for the instruction
		instructionText = (TextView) findViewById(R.id.mainHeadLine);
		instructionText.setText(R.string.scan_code);

		// Get Layout for the scan field
		scanLayout = (LinearLayout) findViewById(R.id.scan_layout);
		// Initialize the class that scans the qr codes
		mPreview = new CameraPreview(this, this, autoFocusCB);
		// Set the layout params for the scan
		LayoutParams l = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPreview.setLayoutParams(l);
		// Add the scan field to the scan layout
		scanLayout.addView(mPreview);

		// Set the head text
		headLeft = (TextView) findViewById(R.id.headline_left);
		headLeft.setText(getString(R.string.pretriage) + ": "
				+ getString(R.string.new_patient));
		// Set the clock to the current time
		Calendar calendar = Calendar.getInstance();
		footerRightTime = (TextView) findViewById(R.id.timestamp);
		int minute = calendar.get(Calendar.MINUTE);
		if (minute < 10) {
			footerRightTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":0" + minute);
			// If the minute is 10 to 59, nothing must be added before the
			// minute
		} else {
			footerRightTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + minute);
		}

	}

	/**
	 * from Dushyanth Maguluru (ZBarScanner)
	 */
	public void setupScanner() {
		mScanner = new ImageScanner();
		mScanner.setConfig(0, Config.X_DENSITY, 3);
		mScanner.setConfig(0, Config.Y_DENSITY, 3);

		int[] symbols = getIntent().getIntArrayExtra(SCAN_MODES);
		if (symbols != null) {
			mScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
			for (int symbol : symbols) {
				mScanner.setConfig(symbol, Config.ENABLE, 1);
			}
		}
	}

	/**
	 * from Dushyanth Maguluru (ZBarScanner)
	 */
	@Override
	protected void onResume() {
		super.onResume();

		// Open the default i.e. the first rear facing camera.
		mCamera = Camera.open();
		if (mCamera == null) {
			// Cancel request if mCamera is null.
			cancelRequest();
			return;
		}

		mPreview.setCamera(mCamera);
		mPreview.showSurfaceView();

		mPreviewing = true;

	}

	/**
	 * from Dushyanth Maguluru (ZBarScanner)
	 */
	@Override
	protected void onPause() {
		super.onPause();

		// Because the Camera object is a shared resource, it's very
		// important to release it when the activity is paused.
		if (mCamera != null) {
			mPreview.setCamera(null);
			mCamera.cancelAutoFocus();
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();

			// According to Jason Kuang on
			// http://stackoverflow.com/questions/6519120/how-to-recover-camera-preview-from-sleep,
			// there might be surface recreation problems when the device goes
			// to sleep. So lets just hide it and
			// recreate on resume
			mPreview.hideSurfaceView();

			mPreviewing = false;
			mCamera = null;
		}
	}

	/**
	 * from Dushyanth Maguluru (ZBarScanner)
	 */
	public boolean isCameraAvailable() {
		PackageManager pm = getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	/**
	 * from Dushyanth Maguluru (ZBarScanner)
	 */
	public void cancelRequest() {
		Intent dataIntent = new Intent();
		dataIntent.putExtra(ERROR_INFO, R.string.no_camera);
		setResult(Activity.RESULT_CANCELED, dataIntent);
		finish();
	}

	/**
	 * from Dushyanth Maguluru (ZBarScanner)
	 */
	public void onPreviewFrame(byte[] data, Camera camera) {
		Camera.Parameters parameters = camera.getParameters();
		Camera.Size size = parameters.getPreviewSize();

		Image barcode = new Image(size.width, size.height, "Y800");
		barcode.setData(data);

		int result = mScanner.scanImage(barcode);

		if (result != 0) {
			mCamera.cancelAutoFocus();
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mPreviewing = false;
			SymbolSet syms = mScanner.getResults();
			for (Symbol sym : syms) {
				String symData = sym.getData();
				if (!TextUtils.isEmpty(symData)) {
					Intent dataIntent = new Intent();
					dataIntent.putExtra(SCAN_RESULT, symData);
					dataIntent.putExtra(SCAN_RESULT_TYPE, sym.getType());
					setResult(Activity.RESULT_OK, dataIntent);
					finish();
					break;
				}
			}
		}
	}

	/**
	 * from Dushyanth Maguluru (ZBarScanner)
	 */
	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (mCamera != null && mPreviewing) {
				mCamera.autoFocus(autoFocusCB);
			}
		}
	};

	/**
	 * from Dushyanth Maguluru (ZBarScanner)
	 */
	// Mimic continuous auto-focusing
	Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			mAutoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	// Methods for voice and tap menu - START
	/**
	 * Creates the voice menu
	 */
	@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
			// get menu
			getMenuInflater().inflate(R.menu.menu_overview_language_close_stop, menu);
			return true;
		}
		// Pass through to super to setup touch menu.
		return super.onCreatePanelMenu(featureId, menu);
	}

	/**
	 * Creates the tap menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// get menu
		getMenuInflater().inflate(R.menu.menu_overview_language_close_stop, menu);
		return true;
	}

	/**
	 * Method that detects when an element in the voice menu is chosen. The
	 * selected item is processed in the method selectItem(item)
	 */
	@Override
		public boolean onMenuItemSelected(int featureId, MenuItem item) {

		if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
			//TODO:
			//return selectItem(item);
		}
		// Good practice to pass through to super if not handled
		return super.onMenuItemSelected(featureId, item);
	}

	// Methods for Tap-Menu - start
	/**
	 * Creates a Detector for tap gesture on touchpad
	 *
	 * @param context
	 * @return
	 */
	private GestureDetector createGestureDetector(Context context) {
		GestureDetector gdDetector = new GestureDetector(context);
		// Create a base listener for generic gestures
		gdDetector.setBaseListener(new GestureDetector.BaseListener() {
			@Override
			public boolean onGesture(Gesture gesture) {
				if (gesture == Gesture.TAP) {
					// play the tap sound
					maManager.playSoundEffect(Sounds.TAP);
					// open the menu
					openOptionsMenu();
					return true;
				}
				return false;
			}

		});
		return gdDetector;
	}

	/**
	 * processing of a MotionEvent
	 */
	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		if (mGestureDetector != null)
			return mGestureDetector.onMotionEvent(event);
		return false;
	}

	/**
	 * Method that detects when an element in the tap menu is chosen. The
	 * selected item is processed in the method selectItem(item)
	 *
	 * @param item
	 * @return
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection. Menu items typically start another
		// activity, start a service, or broadcast another intent.
		return selectItem(item);
	}

	/**
	 * Method that gets the selected item from voice or tap menu. According to
	 * the item it has actions for stopping the application and for changing the
	 * language
	 *
	 * @param item
	 * @return
	 */
	private boolean selectItem(MenuItem item) {
		switch (item.getItemId()) {
			// close this menu without a selection
			case R.id.close_menu:
				closeOptionsMenu();
				return true;
			// switch language from english to german and vice versa
			case R.id.switchLanguage_menu_item:
				// set Locale to german
				Locale myLocale = new Locale("de");
				// get current Locale
				String currentLocale = getResources().getConfiguration().locale
						.toString();
				// set Locale to english if current Locale is german
				if (currentLocale.equals("de")) {
					myLocale = new Locale("en");
				}
				// sets the Locale in the configuration and updates the
				// configuration
				DisplayMetrics metrics = getResources().getDisplayMetrics();
				Configuration config = getResources().getConfiguration();
				config.locale = myLocale;
				getResources().updateConfiguration(config, metrics);
				// recreates the app in order to show the selected language
				recreate();

				return true;
			// stops the application
			case R.id.stop_menu_item:
				finish();
				return true;
			// switches the language from english to german or vice versa
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	// Methods for Tap-Menu - end
	// Methods for voice and tap menu - END

}
