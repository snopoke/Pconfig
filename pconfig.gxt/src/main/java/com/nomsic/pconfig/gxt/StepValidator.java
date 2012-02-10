package com.nomsic.pconfig.gxt;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Used to validate a wizard step before proceeding to the next step.
 * 
 * @author Simon Kelly
 */
public interface StepValidator {

	public void validate(WizardCard wizardCard, int currentStep, AsyncCallback<Void> callback);

}
