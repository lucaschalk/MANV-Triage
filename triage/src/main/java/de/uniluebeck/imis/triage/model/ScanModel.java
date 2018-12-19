package de.uniluebeck.imis.triage.model;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Handler;
import android.util.DisplayMetrics;

import net.sourceforge.zbar.ImageScanner;

import java.util.Locale;

import de.uniluebeck.imis.triage.R;
import de.uniluebeck.imis.triage.activities.ScanActivity;

public class ScanModel {

    private ScanActivity scanActivity;


    static {
        System.loadLibrary("iconv");
    }

    public ScanModel(ScanActivity scanActivity){

        this.scanActivity = scanActivity;
    }

    public void switchLanguage(){
        // set Locale to german
        Locale myLocale = new Locale("de");
        // get current Locale
        String currentLocale = scanActivity.getResources().getConfiguration().locale
                .toString();
        // set Locale to english if current Locale is german
        if (currentLocale.equals("de")) {
            myLocale = new Locale("en");
        }
        // sets the Locale in the configuration and updates the
        // configuration
        DisplayMetrics metrics = scanActivity.getResources().getDisplayMetrics();
        Configuration config = scanActivity.getResources().getConfiguration();
        config.locale = myLocale;
        scanActivity.getResources().updateConfiguration(config, metrics);
        // recreates the app in order to show the selected language
        scanActivity.recreate();
    }


    public ScanActivity getScanActivity() {
        return scanActivity;
    }

    public void setScanActivity(ScanActivity scanActivity) {
        this.scanActivity = scanActivity;
    }

}
