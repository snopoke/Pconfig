package com.nomsic.pconfig.gxt;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

public class WizardStepEvent extends BaseEvent {

	private WizardCard previousStep;
	private WizardCard currentStep;
	private int previousStepNumber;
	private int currentStepNumber;
	private boolean isForward;
	
	public WizardCard getPreviousStep() {
		return previousStep;
	}

	public void setPreviousStep(WizardCard previousStep) {
		this.previousStep = previousStep;
	}

	public WizardCard getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(WizardCard currentStep) {
		this.currentStep = currentStep;
	}

	public int getPreviousStepNumber() {
		return previousStepNumber;
	}

	public void setPreviousStepNumber(int previousStepNumber) {
		this.previousStepNumber = previousStepNumber;
	}

	public int getCurrentStepNumber() {
		return currentStepNumber;
	}

	public void setCurrentStepNumber(int currentStepNumber) {
		this.currentStepNumber = currentStepNumber;
	}

	public boolean isForward() {
		return isForward;
	}

	public void setForward(boolean isForward) {
		this.isForward = isForward;
	}

	public WizardStepEvent(EventType type) {
		super(type);
	}

}
