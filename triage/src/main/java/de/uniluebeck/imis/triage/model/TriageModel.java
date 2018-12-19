package de.uniluebeck.imis.triage.model;

import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

import de.uniluebeck.imis.triage.R;
import de.uniluebeck.imis.triage.activities.MainActivity;
import de.uniluebeck.imis.triage.model.algorithm.Algorithm;
import de.uniluebeck.imis.triage.model.algorithm.MSTaRT;
import de.uniluebeck.imis.triage.model.algorithm.PRIOR;
import de.uniluebeck.imis.triage.utilities.Constants;
import de.uniluebeck.imis.triage.utilities.xml.XMLReader;
import de.uniluebeck.imis.triage.utilities.xml.XMLWriter;
import de.uniluebeck.imis.triage.view.TriageView;

public class TriageModel {

    private MainActivity mainActivity;

    private boolean algorithmSwitch = true;

    private XMLWriter xmlWriter;
    private XMLReader xmlReader;

    private Algorithm mSTaRT;
    private Algorithm prior;

    private String currentPatientID;


    public TriageModel(MainActivity mainActivity){
        this.mainActivity = mainActivity;

        this.mSTaRT = new MSTaRT(mainActivity);
        this.prior = new PRIOR(mainActivity);

        this.readXML();
    }

    public void startTriage(TriageView triageView){
        if(!algorithmSwitch){
            mSTaRT.setState(1,true, triageView);
        }else{
            prior.setState(1,true, triageView);
        }
    }

    public void positiveAnswer(TriageView triageView){
        if(!algorithmSwitch){
            mSTaRT.setStateYes(triageView);
        }else{
            prior.setStateYes(triageView);
        }
    }

    public void negativeAnswer(TriageView triageView){
        if(!algorithmSwitch){
            mSTaRT.setStateNo(triageView);
        }else{
            prior.setStateNo(triageView);
        }
    }

    public void back(TriageView triageView){
        if(!algorithmSwitch){
            mSTaRT.setPreviousState(triageView);
        }else{
            prior.setPreviousState(triageView);
        }
    }

    public void showOverview(TriageView triageView) {
        readXML();
        triageView.updateOverviewView(xmlReader.getManvInfo());
        triageView.drawOverviewView();
        setSpeechRecognitionAndMenus(false,false, false, true, true);
    }

    public void exit(){
        mainActivity.finish();
    }

    public void confirm(TriageView triageView){
        //TODO: CHECK IF ERROR OTHERWISE PLACE AFTER IF ELSE
        mainActivity.readQRCode();
        if(!algorithmSwitch){
            writeXml(mSTaRT);
            mSTaRT.clearHistory();
        }else{
            writeXml(prior);
            prior.clearHistory();
        }
    }

    /**
     * Activate or deactivate speech and tap recognition for "ok yes", "ok no",
     * "ok back", "ok menu", and "next patient"
     *
     * @param confirm
     * @param yes
     * @param no
     * @param back
     * @param stop
     */
    public void setSpeechRecognitionAndMenus(boolean confirm, boolean yes, boolean no,
                                             boolean back, boolean stop) {

        boolean activateSpeechConfirm = confirm;
        boolean activateSpeechYes = yes;
        boolean activateSpeechNo = no;
        boolean activateSpeechBack = back;
        boolean activateSpeechStop = stop;


        if (mainActivity.getTapMenu() != null) {
            mainActivity.getTapMenu().clear();
        }
        if (mainActivity.getVoiceMenu() != null) {
            mainActivity.getVoiceMenu().clear();
        }
        if (!activateSpeechConfirm && activateSpeechYes && activateSpeechNo && activateSpeechBack && activateSpeechStop) {
            if (mainActivity.getTapMenu() != null) {
                mainActivity.getMenuInflater().inflate(R.menu.menu_yes_no_back_overview_stop,
                        mainActivity.getTapMenu());
            }
            if (mainActivity.getVoiceMenu() != null) {
                mainActivity.getMenuInflater().inflate(R.menu.menu_yes_no_back_overview_stop,
                        mainActivity.getVoiceMenu());
            }
        } else if (!activateSpeechConfirm && activateSpeechYes && !activateSpeechNo && activateSpeechBack && activateSpeechStop) {
            if (mainActivity.getTapMenu() != null) {
                mainActivity.getMenuInflater().inflate(R.menu.menu_yes_back_overview_stop,
                        mainActivity.getTapMenu());
            }
            if (mainActivity.getVoiceMenu() != null) {
                mainActivity.getMenuInflater().inflate(R.menu.menu_yes_back_overview_stop,
                        mainActivity.getVoiceMenu());
            }
        } else if (activateSpeechConfirm && !activateSpeechYes && !activateSpeechNo &&activateSpeechBack&& activateSpeechStop) {
            if (mainActivity.getTapMenu() != null) {
                mainActivity.getMenuInflater().inflate(R.menu.menu_confirm_back_overview_stop,
                        mainActivity.getTapMenu());
            }
            if (mainActivity.getVoiceMenu() != null) {
                mainActivity.getMenuInflater().inflate(R.menu.menu_confirm_back_overview_stop,
                        mainActivity.getVoiceMenu());
            }
        }
    }


    public void readXML(){
        this.xmlReader = new XMLReader();
        this.xmlReader.readFromXML();
    }

    /**
     * Method that writes the patient history for documentation into the xml
     * file for the patient using the XMLWriter
     */
    public void writeXml(Algorithm algorithm) {
        this.xmlWriter = new XMLWriter();
        this.xmlWriter.writeToXML(currentPatientID + "", algorithm.getClassification(), algorithm.getHistoryForXML());
    }


    public String getCurrentPatientID() {
        return currentPatientID;
    }
    public void setCurrentPatientID(String currentPatientID) {
        this.currentPatientID = currentPatientID;
    }

}
