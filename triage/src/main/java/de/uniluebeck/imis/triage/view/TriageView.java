package de.uniluebeck.imis.triage.view;

import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;

import de.uniluebeck.imis.triage.R;
import de.uniluebeck.imis.triage.activities.MainActivity;
import de.uniluebeck.imis.triage.utilities.Constants;

public class TriageView {


    private MainActivity mainActivity;


    // Text fields for the layout
    private TextView headLeft;
    private TextView headRight;
    private TextView footerRightTime;
    private TextView mainHeadLine;
    private TextView mainTextLine;

    public TriageView(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        // FROM HERE: INITIALIZE LAYOUT ELEMENTS
        // headline
        this.headLeft = (TextView) mainActivity.findViewById(R.id.headline_left);
        this.headRight = (TextView) mainActivity.findViewById(R.id.headline_right);

        // Text lines of the main panel
        this.mainHeadLine = (TextView) mainActivity.findViewById(R.id.mainHeadLine);
        this.mainTextLine = (TextView) mainActivity.findViewById(R.id.mainTextLine);

        // the footer
        this.footerRightTime = (TextView) mainActivity.findViewById(R.id.timestamp);
        // UNTIL HERE: INITIALIZE LAYOUT ELEMENTS

        initView();

    }


    // START: Methods, that are used to fill the text fields of the layout -
    /**
     * Fills the fields with standard texts and with the current time.
     */
    public void initView() {
        // fill in standard texts
        this.mainTextLine.setText(mainActivity.getString(R.string.pretriage));
        this.mainTextLine.setBackgroundColor(mainActivity.getResources().getColor(R.color.black));
        this.mainTextLine.setTextColor(mainActivity.getResources().getColor(R.color.white));
        this.mainHeadLine.setTextColor(mainActivity.getResources().getColor(R.color.white));
        this.mainTextLine.setTextSize(40);
        this.fillMainTextAddSymbol(0);
        headLeft.setText(R.string.pretriage);
        drawTimeView();
    }

    /**
     * Fills Footer with current time. Only, when a patient number is read
     */
    public void drawTimeView() {
        // Set the clock to the current time
        Calendar calendar = Calendar.getInstance();
        int minute = calendar.get(Calendar.MINUTE);
        // If the minute is 0 to 9, a 0 must be added before the minute
        if (minute < 10) {
            footerRightTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":0"
                    + minute);
            // If the minute is 10 to 59, nothing must be added before the
            // minute
        } else {
            footerRightTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + minute);
        }
    }

    public void drawOverviewView() {
        initView();

    }

    public void updateOverviewView(int[] manvInfo){
        //TODO: textfelder machen fuer overview screen
        //this.greenCatAmount.setText("GREEN: "+ manvInfo[Constants.CATEGORY_GREEN]);
        // this.yellowCatAmount.setText("YELLOW: "+manvInfo[Constants.CATEGORY_YELLOW]);
        //this.redCatAmount.setText("RED: "+manvInfo[Constants.CATEGORY_RED]);
        //this.blackCatAmount.setText("BLACK: "+manvInfo[Constants.CATEGORY_BLACK]);

        Log.d("BLACK", manvInfo[Constants.CATEGORY_GREEN]+"");
        Log.d("RED", manvInfo[Constants.CATEGORY_YELLOW]+"");
        Log.d("YELLOW", manvInfo[Constants.CATEGORY_RED]+"");
        Log.d("GREEN", manvInfo[Constants.CATEGORY_BLACK]+"");
    }

    /**
     * Fills Header with Patient id.
     *
     * @param id
     */
    public void fillLeftHeaderWithPatientID(String id) {
        headLeft.setText(mainActivity.getString(R.string.pretriage) + ": "
                + mainActivity.getString(R.string.patient) + " " + id);
        mainHeadLine.setText(mainActivity.getString(R.string.patientId) + " " + id);
    }

    /**
     * Fills the right header text with navigation infos
     *
     * @param headerRight
     */
    public void fillRightHeaderText(String headerRight) {
        headRight.setText(headerRight);
    }

    /**
     * Text for the previous question and answer
     *
     * @param text
     */
    public void fillMainHeaderText(String text) {
        mainHeadLine.setText(text);
    }

    /**
     * Text for Questions, instructions and other texts
     *
     * @param line1
     */
    public void fillMainTextLine(String line1) {
        mainTextLine.setText(line1);
    }

    /**
     * Fills the background of the main text field with a defined color. Used in
     * order to show the classification infos in the color of the categories.
     *
     * @param color
     */
    public void fillMainTextFieldWithColor(int color) {
        if (color == 1) {
            mainTextLine.setBackgroundColor(mainActivity.getResources()
                    .getColor(R.color.red));
        } else if (color == 2) {
            mainTextLine.setBackgroundColor(mainActivity.getResources().getColor(
                    R.color.yellow));
        } else if (color == 3) {
            mainTextLine.setBackgroundColor(mainActivity.getResources().getColor(
                    R.color.green));
        } else {
            mainTextLine.setBackgroundColor(mainActivity.getResources().getColor(
                    R.color.black));
        }
    }

    /**
     * Sets the color of the main text to white or black.
     *
     * @param color
     */
    public void setMainTextColor(int color) {
        if (color == 1) {
            mainTextLine.setTextColor(mainActivity.getResources().getColor(R.color.white));
        } else {
            mainTextLine.setTextColor(mainActivity.getResources().getColor(R.color.black));
        }
    }

    /**
     * Sets the size of the text in the main header line to the given value
     *
     * @param size
     */
    public void setMainHeadLineTextSize(int size) {
        mainHeadLine.setTextSize(size);
    }

    /**
     * Sets the size of the main text to the given value
     *
     * @param size
     */
    public void setMainTextSize(int size) {
        mainTextLine.setTextSize(size);
    }

    /**
     * Adds a symbol to the main text line. This symbols appears to the left of
     * the text. If 0, no symbol will appear.
     *
     * @param line1
     */
    public void fillMainTextAddSymbol(int line1) {
        mainTextLine.setCompoundDrawablesWithIntrinsicBounds(line1, 0, 0, 0);
    }

    // Methods, that are used to fill the text fields of the layout - end



}
