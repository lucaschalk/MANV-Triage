package de.uniluebeck.imis.triage.model.algorithm;

import android.util.Log;

import java.util.Arrays;

import de.uniluebeck.imis.triage.R;
import de.uniluebeck.imis.triage.activities.MainActivity;
import de.uniluebeck.imis.triage.utilities.Constants;
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
public class MSTaRT extends Algorithm {

	public MSTaRT(MainActivity activity) {
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

		// State 1 to 5 and 7 to 8: Normal questions of the algorithm
		if (newState == 1) {
			triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.question) + " 1");
            triageView.fillMainTextLine(super.getMainActivity().getString(R.string.q1));
            triageView.fillMainTextAddSymbol(0);
			super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
					true, true, true);
			setNextStatesForAnswers(100, 2);
		} else if (newState == 2) {
			triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.question) + " 2");
			triageView.fillMainTextLine(super.getMainActivity().getString(R.string.q2));
			triageView.fillMainTextAddSymbol(0);
			super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
					true, true, true);
			setNextStatesForAnswers(80, 3);
		} else if (newState == 3) {
			triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.question) + " 3");
			triageView.fillMainTextLine(super.getMainActivity().getString(R.string.q3));
			triageView.fillMainTextAddSymbol(0);
			super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
					true, true, true);
			setNextStatesForAnswers(31, 4);
		} else if (newState == 4) {
			triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.question) + " 4");
			triageView.fillMainTextLine(super.getMainActivity().getString(R.string.q4));
			triageView.fillMainTextAddSymbol(0);
			super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
					true, true, true);
			setNextStatesForAnswers(300, 5);
		} else if (newState == 5) {
			triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.question) + " 5");
			triageView.fillMainTextLine(super.getMainActivity().getString(R.string.q5));
			triageView.fillMainTextAddSymbol(0);
			super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
					true, true, true);
			setNextStatesForAnswers(6, 7);
		} else if (newState == 7) {
			triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.question) + " 6");
			triageView.fillMainTextLine(super.getMainActivity().getString(R.string.q6));
			triageView.fillMainTextAddSymbol(0);
			super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
					true, true, true);
			setNextStatesForAnswers(300, 8);
		} else if (newState == 8) {
			triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.question) + " 7");
			triageView.fillMainTextLine(super.getMainActivity().getString(R.string.q7));
			triageView.fillMainTextAddSymbol(0);
			super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
					true, true, true);
			setNextStatesForAnswers(300, 700);


			// State 100 to 800: Classification states
		} else if (newState == 100) {
			triageView.fillMainTextFieldWithColor(3);
			triageView.setMainTextSize(38);
			triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.classification));
			triageView.fillMainTextLine(super.getMainActivity().getString(R.string.card) + "\n"
					+ super.getMainActivity().getString(R.string.kat3));
			triageView.fillMainTextAddSymbol(R.drawable.hand_big);
			super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(true,false,
					false, true, true);

		} else if (newState == 800) {
			triageView.fillMainTextFieldWithColor(0);
			triageView.setMainTextSize(38);
			triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.classification));
			triageView.fillMainTextLine(super.getMainActivity().getString(R.string.card) + "\n"
					+ super.getMainActivity().getString(R.string.katblack));
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


		}

		// States 6,31,80 are questions and instructions between the normal
		// questions of the algorithm and the classification
		else if (newState == 80) {
			triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.inspectionIfDead));
			triageView.fillMainTextLine(super.getMainActivity().getString(R.string.inspectionIfDeadQuestion));
			triageView.fillMainTextAddSymbol(0);
			super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
					false, true, true);
			setNextStatesForAnswers(800, 1);
		} else if (newState == 31) {
			triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.spontaneousBreathing));
			triageView.fillMainTextLine(super.getMainActivity().getString(R.string.spontaneousBreathingQuestion));
			triageView.fillMainTextAddSymbol(R.drawable.hand_h);
			triageView.setMainTextSize(32);
			super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
					true, true, true);
			setNextStatesForAnswers(300, 80);
		} else if (newState == 6) {
			triageView.fillRightHeaderText(super.getMainActivity().getString(R.string.bleedingControl));
			triageView.fillMainTextLine(super.getMainActivity().getString(R.string.bleedingControlInst));
			triageView.fillMainTextAddSymbol(R.drawable.hand_big);
			super.getMainActivity().getMenuAndSpeechController().getTriageModel().setSpeechRecognitionAndMenus(false,true,
					false, true, true);
			setNextStatesForAnswers(7, 1);
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
					triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q1no));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q1));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.no));
				} else if (newState == 100) {
					triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q1yes));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q1));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
				}
			} else if (previousState == 2) {
				if (newState == 3) {
					triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q2no));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q2));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.no));
				} else if (newState == 80) {
					triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q2yes));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q2));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
				}
			} else if (previousState == 3) {
				if (newState == 4) {
					triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q3no));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q3));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.no));
				} else if (newState == 31) {
					triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q3yes));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q3));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
				}
			} else if (previousState == 4) {
				if (newState == 5) {
					triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q4no));
					triageView.setMainHeadLineTextSize(17);
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q4oneline));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.no));
				} else if (newState == 300) {
					triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q4yes));
					triageView.setMainHeadLineTextSize(17);
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q4oneline));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
				}
			} else if (previousState == 5) {
				if (newState == 7) {
					triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q5no));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q5));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.no));
				} else if (newState == 6) {
					triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q5yes));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q5));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
				}
			} else if (previousState == 7) {
				if (newState == 8) {
					triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q6no));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q6));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.no));
				} else if (newState == 300) {
					triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q6yes));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q6));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
				}
			} else if (previousState == 8) {
				if (newState == 700) {
					triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q7no));
					triageView.setMainHeadLineTextSize(16);
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q7));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.no));
				} else if (newState == 300) {
					triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_q7yes));
					triageView.setMainHeadLineTextSize(16);
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.q1));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
				}
			}
			// States 6, 31 and 80
			else if (previousState == 80) {
				triageView.fillMainHeaderText(super.getMainActivity().getString(R.string.last_inspectedDead));
				super.getHistoryForXML().add(super.getMainActivity().getString(R.string.inspectionIfDead));
				super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
			} else if (previousState == 31) {
				if (newState == 80) {
					triageView.fillMainHeaderText(super.getMainActivity()
							.getString(R.string.last_spontaneBreathingNo));
					super.getHistoryForXML().add(super.getMainActivity()
							.getString(R.string.spontaneousBreathing));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.no));
				} else if (newState == 300) {
					triageView.fillMainHeaderText(super.getMainActivity()
							.getString(R.string.last_spontaneBreathingYes));
					super.getHistoryForXML().add(super.getMainActivity()
							.getString(R.string.spontaneousBreathing));
					super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
				}
			} else if (previousState == 6) {
				triageView.fillMainHeaderText(super.getMainActivity()
						.getString(R.string.last_bleedingControlled));
				super.getHistoryForXML().add(super.getMainActivity().getString(R.string.bleedingControl));
				super.getHistoryForXML().add(super.getMainActivity().getString(R.string.yes));
			}
		}

	}

	@Override
	public int getClassification() {
		// category 3
		if (super.getCurrentState() == 100) {
			return Constants.CATEGORY_GREEN;
			// category 1
		} else if (super.getCurrentState() == 300) {
			return Constants.CATEGORY_RED;
			// category 2
		} else if (super.getCurrentState() == 700) {
			return Constants.CATEGORY_YELLOW;
			// category black is 0
		} else if (super.getCurrentState() == 800) {
			return Constants.CATEGORY_BLACK;
		} else
			return -1;
	}

}
