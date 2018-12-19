package de.uniluebeck.imis.triage.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

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

import de.uniluebeck.imis.triage.R;
import de.uniluebeck.imis.triage.controller.ScanController;

/**
 * This is a modified version of ZBarScannerActivity from Dushyanth Maguluru.
 * ZBarScanner is under MIT license:
 * http://opensource.org/licenses/mit-license.php
 *
 * @author Henrik Berndt / Dushyanth Maguluru (ZBarScanner)
 */
public class ScanActivity extends Activity implements Camera.PreviewCallback,
        ZBarConstants {

    private ScanController scanController;

    // Objects for tap menu interaction and audio
    private GestureDetector mGestureDetector;
    private AudioManager maManager;

    private Camera mCamera;
    private ImageScanner mScanner;
    private Handler mAutoFocusHandler;
    private boolean mPreviewing = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get a voice menu
        getWindow().requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS);

        // Set the Layout
        setContentView(R.layout.scanactivity);

        // initialize the audio manager
        maManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // create gesture listener for touchpad menu interaction
        mGestureDetector = createGestureDetector(this);

        scanController = new ScanController(this);

        // from Dushyanth Maguluru (ZBarScanner)
        // Create and configure the ImageScanner;
        setupScanner();
    }

    /**
     * from Dushyanth Maguluru (ZBarScanner)
     */
    public void setupScanner() {

        // from Dushyanth Maguluru (ZBarScanner)
        if (!isCameraAvailable()) {
            // Cancel request if there is no rear-facing camera.
            cancelRequest();
            return;
        }
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

        scanController.getScanView().getmPreview().setCamera(mCamera);
        scanController.getScanView().getmPreview().showSurfaceView();

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
            scanController.getScanView().getmPreview().setCamera(null);
            mCamera.cancelAutoFocus();
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();

            // According to Jason Kuang on
            // http://stackoverflow.com/questions/6519120/how-to-recover-camera-preview-from-sleep,
            // there might be surface recreation problems when the device goes
            // to sleep. So lets just hide it and
            // recreate on resume
            scanController.getScanView().getmPreview().hideSurfaceView();

            mPreviewing = false;
            mCamera = null;
        }
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
            return scanController.selectItem(item);
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
        return scanController.selectItem(item);
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
    public Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            mAutoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };


}
