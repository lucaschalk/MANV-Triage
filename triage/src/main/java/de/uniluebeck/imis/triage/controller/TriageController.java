package de.uniluebeck.imis.triage.controller;

import de.uniluebeck.imis.triage.R;
import de.uniluebeck.imis.triage.activities.MainActivity;
import de.uniluebeck.imis.triage.model.TriageModel;
import de.uniluebeck.imis.triage.view.TriageView;

import android.util.Log;
import android.view.MenuItem;

/**
 * Class that is used in order to control the menu and speech elements that are
 * shown according to the state of the algorithm
 *
 * @author Henrik Berndt
 */
public class TriageController {

    private MainActivity mainActivity;

    private TriageModel triageModel;

    private TriageView triageView;

    /**
     * The application uses different menus for the states. For example a
     * question needs the answers yes and no, while an instruction only needs
     * yes in order to continue. If a user is at the last state, there should be
     * the possibility to triage another patient. This class contains the logic
     * in order to change the menu items according to the current state
     *
     * @param mainActivity
     */
    public TriageController(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.triageView = new TriageView(mainActivity);
        this.triageModel = new TriageModel(mainActivity);

    }

    /**
     * Method that gets the selected item from voice or tap menu. According to
     * the item it has some standard actions like set the state in the state
     * controller, get back or start a new triage
     *
     * @param item
     * @return
     */
    public boolean selectItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.confirm_menu_item:
                triageModel.confirm(triageView);
                return true;
            case R.id.yes_menu_item:
                triageModel.positiveAnswer(triageView);
                return true;
            // negative answer (question)
            case R.id.no_menu_item:
                triageModel.negativeAnswer(triageView);
                return true;
            // function for getting back
            case R.id.back_menu_item:
                triageModel.back(triageView);
                return true;
            // stops the application
            case R.id.stop_menu_item:
                // stops the application
                triageModel.exit();
                return true;
            case R.id.overview_menu_item:
                // shows overview of patients
                triageModel.showOverview(triageView);
                return true;
            default:
                return mainActivity.onMenuItemSelected(item.getItemId(), item);
        }
    }


    public TriageModel getTriageModel() {
        return triageModel;
    }

    public TriageView getTriageView() {
        return triageView;
    }




}
