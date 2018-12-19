package de.uniluebeck.imis.triage.controller;

import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.dm.zbar.android.scanner.CameraPreview;

import java.util.Locale;

import de.uniluebeck.imis.triage.R;
import de.uniluebeck.imis.triage.activities.ScanActivity;
import de.uniluebeck.imis.triage.model.ScanModel;
import de.uniluebeck.imis.triage.view.ScanView;
import de.uniluebeck.imis.triage.view.TriageView;

public class ScanController {

    private ScanActivity scanActivity;
    private ScanModel scanModel;
    private ScanView scanView;

    public ScanController(ScanActivity scanActivity) {
        this.scanActivity = scanActivity;
        this.scanModel = new ScanModel(scanActivity);
        this.scanView = new ScanView(scanActivity);

    }

    /**
     * Method that gets the selected item from voice or tap menu. According to
     * the item it has actions for stopping the application and for changing the
     * language
     *
     * @param item
     * @return
     */
    public boolean selectItem(MenuItem item) {
        switch (item.getItemId()) {
            // close this menu without a selection
            case R.id.close_menu:
                scanActivity.closeOptionsMenu();
                return true;
            // switch language from english to german and vice versa
            case R.id.switchLanguage_menu_item:
                scanModel.switchLanguage();
                return true;
            // stops the application
            case R.id.stop_menu_item:
                scanActivity.finish();
                return true;
            case R.id.overview_menu_item:
                //TODO: KONZEPT OVERVIEW VIEW
                return true;
            default:
                return scanActivity.onMenuItemSelected(item.getItemId(), item);
        }
    }
    // Methods for Tap-Menu - end
    // Methods for voice and tap menu - END

    public ScanView getScanView() {
        return scanView;
    }

    public ScanModel getScanModel() {
        return scanModel;
    }
}
