package de.uniluebeck.imis.triage.activities;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import net.sourceforge.zbar.Symbol;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.view.WindowUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import de.uniluebeck.imis.triage.R;
import de.uniluebeck.imis.triage.controller.ScanController;
import de.uniluebeck.imis.triage.controller.TriageController;
import de.uniluebeck.imis.triage.utilities.Constants;

/**
 *
 * An {@link Activity} showing the mstart algorithm.
 *
 * @author Henrik Berndt
 *
 */
public class MainActivity extends Activity {

	// Menus for voice and touchpad interaction
	private Menu voiceMenu;
	private Menu tapMenu;

	private TriageController triageController;

	// Objects for tap menu interaction and audio
	private GestureDetector mGestureDetector;
	private AudioManager maManager;



	/**
	 * Will be executed when the activity starts. Initializes the text fields
	 * and fills the with initial and standard values and the settings for
	 * speech recognition
	 */
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		// get a voice menu
		getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);

		// Set Layout
		setContentView(R.layout.triageactivity);

		// initialize the audio manager
		maManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// create gesture listener for touchpad menu interaction
		mGestureDetector = createGestureDetector(this);

		// Initialize controllers
		triageController = new TriageController(this);

		// fill in standard texts
		//readQRCode();
		openBT();
		//openUnpairBT();

	}




	// START: Methods for QR Code
	// Intialize and start ZBarScanner in order to read a QR code
	public void readQRCode() {
		// initialize QR code reader ZBarScanner
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), ScanActivity.class);
		// only QR Codes
		intent.putExtra(ZBarConstants.SCAN_MODES, new int[] { Symbol.QRCODE });
		// start QR code reader. result in onActivityResult()
		startActivityForResult(intent, Constants.ZBAR_SCANNER_REQUEST);
	}

	public void openBT() {
		// initialize QR code reader ZBarScanner
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), BluetoothClient.class);
		startActivity(intent);
	}

	public void openUnpairBT() {
		// initialize QR code reader ZBarScanner
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), BT_erase_Activity.class);
		startActivity(intent);
	}

	/**
	 * Gets the result of the QR code and saves it as a string in patientId.
	 *
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			// Scan result is available by making a call to
			// data.getStringExtra(ZBarConstants.SCAN_RESULT)
			triageController.getTriageModel().setCurrentPatientID(data.getStringExtra(ZBarConstants.SCAN_RESULT));
			// set the first state in the state controller
			triageController.getTriageModel().startTriage(triageController.getTriageView());
			// set the patient id
			triageController.getTriageView().fillLeftHeaderWithPatientID(triageController.getTriageModel().getCurrentPatientID());
			// play a sound in order to let the user know that the input was
			// read correctly
			maManager.playSoundEffect(Sounds.SUCCESS);
			triageController.getTriageView().drawTimeView();
		} else if (resultCode == RESULT_CANCELED) {
			// if the user cancels the input (touchdown) the whole app should be
			// closed
			finish();
		}
	}
	// END: Methods for QR Code



	// Standard methods START
	/**
	 * start listening to user voice if activity will be resumed
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * stop listening to user voice if activity will be paused
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}

	// Standard methods END

	// Methods for voice and tap menu - START
	/**
	 * Creates the voice menu
	 */
	@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
			voiceMenu = menu;
			// first menu
			getMenuInflater().inflate(R.menu.menu_yes_no_back_overview_stop, menu);
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
		tapMenu = menu;
		// first menu
		getMenuInflater().inflate(R.menu.menu_yes_no_back_overview_stop, menu);
		return true;
	}


	/**
	 * Method that detects when an element in the voice menu is chosen. The
	 * selected item is processed in the method selectItem(item)
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
			return triageController.selectItem(item);
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
		return triageController.selectItem(item);
	}

	/**
	 * Methods to send data via WLAN to a server
	 * TODO: IP und Port nicht fest konfigurieren
	 * TODO: Alle Infos aufnehmen
	 * TODO: Warten und späteres Schicken, falls kein Netz vorhanden. Benutzer dann informieren
	 * TODO: Funktion für manuelles Übertragen (USB?)
	 */
	public void sendData(int classification)  {
		Log.d("test1", "anfang");
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		try {
			Log.d("test1", "vor");
			Socket socket = new Socket("192.168.1.104", 1234);
			Log.d("test1", "1");
			PrintWriter out = new PrintWriter(socket.getOutputStream(),
					true);
			Log.d("test1", "2");
			out.println(classification);
			Log.d("test1", "3");
		} catch (IOException i) {
			Log.d("IOException", ""+i);
		}

	}

	/**
	 * get voice menu
	 *
	 * @return
	 */
	public Menu getVoiceMenu() {
		return voiceMenu;
	}

	/**
	 * get tap menu
	 *
	 * @return
	 */
	public Menu getTapMenu() {
		return tapMenu;
	}

	/**
	 * get TriageController
	 *
	 * @return
	 */
	public TriageController getMenuAndSpeechController() {
		return triageController;
	}

}