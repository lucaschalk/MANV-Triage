package de.uniluebeck.imis.triage.model.algorithm;

import android.util.Log;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import de.uniluebeck.imis.triage.R;
import de.uniluebeck.imis.triage.activities.MainActivity;
import de.uniluebeck.imis.triage.utilities.Constants;
import de.uniluebeck.imis.triage.utilities.xml.XMLWriter;
import de.uniluebeck.imis.triage.view.TriageView;

/**
 * This application uses states in order to know the actual question and the
 * following questions for the answers. The states are also used to create a
 * history. This can be used in order to document the user commands for a
 * patient and for the getting back function. This class saves and sets the
 * states. It also contains the information, which menu elements are needed for
 * a state and which texts and symbols should be displayed
 *
 * @author Henrik Berndt
 *
 */
public class PRIOR extends Algorithm {

    public PRIOR(MainActivity activity) {
        super(activity);
    }

    /**
     * This method defines for each state which elements and which texts should
     * be shown. It also contains the definition of the confirm states for the
     * answers "Yes" and "No"
     *
     * Each state number represents a question, instruction or classification of
     * the algorithm. The states are sorted in a way, so that states with a
     * higher number always represent states which follow states with a smaller
     * number in the algorithm. This is used in order to recognize whether the
     * user wanted to get back or to one of the confirm states
     *
     * @param newState
     * @param createHistory
     */
    public void setState(int newState, boolean createHistory, TriageView triageView) {

        // Calls Method in order to show the previous answers
        showPreviousQuestionAndAnswer(super.getCurrentState(), newState, createHistory, triageView);
        //set new state
        super.setCurrentState(newState);
        // Calls Method in order to set the initial values to the fields
        super.setInitialValuesToFields(newState,triageView);

        // State 1 to 7: Normal questions of the algorithm
        if (newState == 1) {
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.question) + " 1");
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.q21));
            triageView.fillMainTextAddSymbol(0);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
                    true, true, true);
            setNextStatesForAnswers(10, 2);
        } else if (newState == 2) {
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.question) + " 2");
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.q22));
            triageView.fillMainTextAddSymbol(0);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
                    true, true, true);
            setNextStatesForAnswers(20, 3);
        } else if (newState == 3) {
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.question) + " 3");
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.q23));
            triageView.fillMainTextAddSymbol(0);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
                    true, true, true);
            setNextStatesForAnswers(30, 4);
        } else if (newState == 4) {
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.question) + " 4");
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.q24));
            triageView.fillMainTextAddSymbol(0);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
                    true, true, true);
            setNextStatesForAnswers(40, 5);
        } else if (newState == 5) {
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.question) + " 5");
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.q25));
            triageView.fillMainTextAddSymbol(0);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
                    true, true, true);
            setNextStatesForAnswers(500, 6);
        } else if (newState == 6) {
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.question) + " 6");
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.q26));
            triageView.fillMainTextAddSymbol(0);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
                    true, true, true);
            setNextStatesForAnswers(600, 7);
        } else if (newState == 7) {
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.question) + " 7");
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.q27));
            triageView.fillMainTextAddSymbol(0);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
                    true, true, true);
            setNextStatesForAnswers(700, 800);

            // State 100 to 800: Classification states
        } else if (newState == 100) {
            triageView.fillMainTextFieldWithColor(1);
            triageView.setMainTextSize(38);
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.classification));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.card) + "\n"
                    + super.getMainActivity().getString(R.string.kat1));
            triageView.fillMainTextAddSymbol(R.drawable.hand_big);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(true,false,
                    false, true, true);
        } else if (newState == 200) {
            triageView.fillMainTextFieldWithColor(1);
            triageView.setMainTextSize(38);
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.classification));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.card) + "\n"
                    + super.getMainActivity().getString(R.string.kat1));
            triageView.fillMainTextAddSymbol(R.drawable.hand_big);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(true,false,
                    false, true, true);

        } else if (newState == 300) {
            triageView.fillMainTextFieldWithColor(1);
            triageView.setMainTextSize(38);
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.classification));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.card) + "\n"
                    + super.getMainActivity().getString(R.string.kat1));
            triageView.fillMainTextAddSymbol(R.drawable.hand_big);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(true,false,
                    false, true, true);

        } else if (newState == 400) {
            triageView.fillMainTextFieldWithColor(1);
            triageView.setMainTextSize(38);
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.classification));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.card) + "\n"
                    + super.getMainActivity().getString(R.string.kat1));
            triageView.fillMainTextAddSymbol(R.drawable.hand_big);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(true,false,
                    false, true, true);
        } else if (newState == 500) {
            triageView.fillMainTextFieldWithColor(1);
            triageView.setMainTextSize(38);
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.classification));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.card) + "\n"
                    + super.getMainActivity().getString(R.string.kat1));
            triageView.fillMainTextAddSymbol(R.drawable.hand_big);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(true,false,
                    false, true, true);
        } else if (newState == 600) {
            triageView.fillMainTextFieldWithColor(1);
            triageView.setMainTextSize(38);
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.classification));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.card) + "\n"
                    + super.getMainActivity().getString(R.string.kat1));
            triageView.fillMainTextAddSymbol(R.drawable.hand_big);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(true,false,
                    false, true, true);

        } else if (newState == 700) {
            triageView.fillMainTextFieldWithColor(2);
            triageView.setMainTextSize(38);
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.classification));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.card) + "\n"
                    + super.getMainActivity().getString(R.string.kat2));
            triageView.setMainTextColor(0);
            triageView.fillMainTextAddSymbol(R.drawable.hand_big_black);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(true,false,
                    false, true, true);

        } else if (newState == 800) {
            triageView.fillMainTextFieldWithColor(3);
            triageView.setMainTextSize(38);
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.classification));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.card) + "\n"
                    + super.getMainActivity().getString(R.string.kat3));
            triageView.fillMainTextAddSymbol(R.drawable.hand_big);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(true,false,
                    false, true, true);

        }

        // States 10-30 are questions and instructions between the normal
        // questions of the algorithm and the classification
        else if (newState == 10) {
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.stopbleeding));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.stopbleeding));
            triageView.fillMainTextAddSymbol(0);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
                    false, true, true);
            setNextStatesForAnswers(11, 1);
        } else if (newState == 11) {
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.transport));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.transport));
            triageView.fillMainTextAddSymbol(0);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
                    false, true, true);
            setNextStatesForAnswers(100, 1);
        } else if (newState == 20) {
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.spontaneousBreathingQuestion));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.spontaneousBreathingQuestion));
            triageView.fillMainTextAddSymbol(0);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
                    false, true, true);
            setNextStatesForAnswers(21, 1);
        } else if (newState == 21) {
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.lateralposition));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.lateralposition));
            triageView.fillMainTextAddSymbol(0);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
                    false, true, true);
            setNextStatesForAnswers(22, 1);
        } else if (newState == 22) {
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.stopbleeding));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.stopbleeding));
            triageView.fillMainTextAddSymbol(0);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
                    false, true, true);
            setNextStatesForAnswers(200, 1);
        } else if (newState == 30) {
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.spontaneousBreathingQuestion));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.spontaneousBreathingQuestion));
            triageView.fillMainTextAddSymbol(0);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
                    false, true, true);
            setNextStatesForAnswers(31, 1);
        } else if (newState == 31) {
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.stopbleeding));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.stopbleeding));
            triageView.fillMainTextAddSymbol(0);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
                    false, true, true);
            setNextStatesForAnswers(300, 1);
        } else if (newState == 40) {
            triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.stopbleeding));
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.stopbleeding));
            triageView.fillMainTextAddSymbol(0);
            super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
                    false, true, true);
            setNextStatesForAnswers(400, 1);
        }
    }


    /**
     * Method that shows the previous question and answer based on the previous
     * and the new state
     *
     * @param previousState
     * @param newState
     * @param createHistory
     */
    public void showPreviousQuestionAndAnswer(int previousState, int newState,
                                              boolean createHistory, TriageView triageView) {

        // If the state should be saved in the history, this will be done here
        if (createHistory) {
            super.getHistory().add(newState);
        }

        triageView.fillMainHeaderText("");
        triageView.setMainHeadLineTextSize(18);
        // if the back function was used, this should be displayed
        if (previousState > newState) {
            triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_getBack));
        } else {
            // input was yes or no
            // Questions 1 to 7
            if (previousState == 1) {
                if (newState == 2) {
                    triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q21no));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q21));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.no));
                } else if (newState == 10) {
                    triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q21yes));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q21));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
                }
            } else if (previousState == 2) {
                if (newState == 3) {
                    triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q22no));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q22));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.no));
                } else if (newState == 20) {
                    triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q22yes));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q22));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
                }
            } else if (previousState == 3) {
                if (newState == 4) {
                    triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q23no));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q23));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.no));
                } else if (newState == 30) {
                    triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q23yes));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q23));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
                }
            } else if (previousState == 4) {
                if (newState == 5) {
                    triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q24no));
                    triageView.setMainHeadLineTextSize(17);
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q24));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.no));
                } else if (newState == 40) {
                    triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q24yes));
                    triageView.setMainHeadLineTextSize(17);
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q24));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
                }
            } else if (previousState == 5) {
                if (newState == 6) {
                    triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q25no));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q25));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.no));
                } else if (newState == 500) {
                    triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q25yes));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q25));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
                }
            } else if (previousState == 6) {
                if (newState == 7) {
                    triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q26no));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q26));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.no));
                } else if (newState == 600) {
                    triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q26yes));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q26));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
                }
            } else if (previousState == 7) {
                if (newState == 800) {
                    triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q27no));
                    triageView.setMainHeadLineTextSize(16);
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q27));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.no));
                } else if (newState == 700) {
                    triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q27yes));
                    triageView.setMainHeadLineTextSize(16);
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q27));
                    super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
                }
            }

            // States 10-30
            else if (previousState == 10) {
                triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_bleedingControlled));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.stopbleeding));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
            } else if (previousState == 11) {
                triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_transport));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.transport));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
            } else if (previousState == 20) {
                triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_spontaneBreathingYes));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.spontaneousBreathingQuestion));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
            } else if (previousState == 21) {
                triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_lateralposition));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.lateralposition));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
            } else if (previousState == 22) {
                triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_bleedingControlled));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.stopbleeding));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
            } else if (previousState == 30) {
                triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_spontaneBreathingYes));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.spontaneousBreathingQuestion));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
            } else if (previousState == 31) {
                triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_bleedingControlled));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.stopbleeding));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
            } else if (previousState == 40) {
                triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_bleedingControlled));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.stopbleeding));
                super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));

            }


        }


    }

    @Override
    public int getClassification() {
        // category 1
        if  (super.getCurrentState() == 100) {
            return Constants.CATEGORY_RED;
        } else if  (super.getCurrentState() == 200) {
            return Constants.CATEGORY_RED;
        } else if (super.getCurrentState() == 300) {
            return Constants.CATEGORY_RED;
        } else if (super.getCurrentState() == 400) {
            return Constants.CATEGORY_RED;
        } else if (super.getCurrentState() == 500) {
            return Constants.CATEGORY_RED;
        } else if (super.getCurrentState() == 600) {
            return Constants.CATEGORY_RED;
            // category 2
        } else if (super.getCurrentState() == 700) {
            return Constants.CATEGORY_YELLOW;
            // category 3
        } else if (super.getCurrentState() == 800) {
            return Constants.CATEGORY_GREEN;
        } else
            return -1;
    }


}
