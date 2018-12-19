package de.uniluebeck.imis.triage.view;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.zbar.android.scanner.CameraPreview;

import java.util.Calendar;

import de.uniluebeck.imis.triage.R;
import de.uniluebeck.imis.triage.activities.ScanActivity;

public class ScanView {

    private ScanActivity scanActivity;
    private LinearLayout scanLayout;
    private TextView headLeft;
    private TextView instructionText;
    private TextView footerRightTime;


    private CameraPreview mPreview;

    public ScanView(ScanActivity scanActivity){
        this.scanActivity = scanActivity;

        // Get and fill the text field for the instruction
        instructionText = (TextView) scanActivity.findViewById(R.id.mainHeadLine);
        // Get Layout for the scan field
        scanLayout = (LinearLayout) scanActivity.findViewById(R.id.scan_layout);
        // Set the head text
        this.headLeft = (TextView) scanActivity.findViewById(R.id.headline_left);
        initView();


    }
    public void initView(){

        instructionText.setText(R.string.scan_code);
        this.headLeft.setText(scanActivity.getString(R.string.pretriage) + ": "
                + scanActivity.getString(R.string.new_patient));
        drawTimeView();
        drawCameraPreview();
    }

    public void drawTimeView(){
        // Set the clock to the current time
        Calendar calendar = Calendar.getInstance();
        footerRightTime = (TextView) scanActivity.findViewById(R.id.timestamp);
        int minute = calendar.get(Calendar.MINUTE);
        if (minute < 10) {
            footerRightTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":0" + minute);
            // If the minute is 10 to 59, nothing must be added before the
            // minute
        } else {
            footerRightTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + minute);
        }

    }

    public void drawCameraPreview(){
        // Initialize the class that scans the qr codes
        mPreview = new CameraPreview(scanActivity, scanActivity, scanActivity.autoFocusCB);
        // Set the layout params for the scan
        ViewGroup.LayoutParams l = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mPreview.setLayoutParams(l);
        // Add the scan field to the scan layout

        scanLayout.addView(mPreview);


    }

    public void drawOverviewView(){

    }

    public CameraPreview getmPreview() {
        return mPreview;
    }




}
