/*
 * Copyright 2008 Grant Slender <gslender@iinet.com.au>
 * 
 * Ideas/concepts borrowed from Thorsten Suckow-Homberg <ts@siteartwork.de>
 * http://www.siteartwork.de/wizardcomponent
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.nomsic.pconfig.gxt;

import java.util.ArrayList;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.CardPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;

/**
 * A wizard window intended to display wizard cards.</br></br>
 * 
 * // setup an array of WizardCards</br> ArrayList<WizardCard> cards = new
 * ArrayList<WizardCard>();</br> </br> // 1st card - a welcome</br> WizardCard
 * wc = new WizardCard("Welcome");</br> wc.setHtmlText(
 * "Welcome to the example for ext.ux.WizardWindow, "</br> + "a ExtGWT user
 * extension for creating wizards.<br/>
 * <br/>
 * "</br> +
 * "Please click the \"next\"-button and fill out all form values.");</br>
 * cards.add(wc);</br> </br> // 2nd or more cards...</br> // wc = new
 * WizardCard("More cards...");</br> // cards.add(wc);</br> // ...</br> </br>
 * WizardWindow wizwin = new WizardWindow(cards);</br>
 * wizwin.setHeading("A simple example for a wizard");</br>
 * wizwin.setHeaderTitle("Simple Wizard Example");</br> </br>
 * wizwin.show();</br>
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>WizardStep</b> : WizardStepEvent<br>
 * <div>Fires when the wizard step changes.</div>
 * <ul>
 * <li>currentStep : WizardCard</li>
 * <li>currentStepNumber : int</li>
 * <li>previousStep : WizardCard</li>
 * <li>previousStepNumber : int</li>
 * <li>isForward : boolean</li>
 * </ul>
 * </dd>
 * <dd><b>WizardFinish</b> : BaseEvent()<br>
 * <div>Fires when the wizard finishes.</div>
 * </dd>
 * </dl>
 */
public class WizardWindow extends Window {

	/**
	 * Indicator type enumeration.
	 */
	public enum Indicator {
		/** NONE */
		NONE,
		/** DOT */
		DOT,
		/** PROGRESSBAR */
		PROGRESSBAR
	}

	/** The status bar text. */
	private String statusBarText = "Saving...";

	/** The previous button text. */
	private String previousButtonText = "< Previous";

	/** The next button text. */
	private String nextButtonText = "Next >";

	/** The cancel button text. */
	private String cancelButtonText = "Cancel";

	/** The finish button text. */
	private String finishButtonText = "Finish";

	/** The indicate step text. */
	private String indicateStepText = "Step ";

	/** The indicate of text. */
	private String indicateOfText = " of ";

	/** The current step. */
	private int currentStep = 0;

	/** The wizard cards. */
	private ArrayList<WizardCard> cards = new ArrayList<WizardCard>();

	/** The header title. */
	private String headerTitle;

	/** The header panel. */
	private Header headerPanel;

	/** The card panel. */
	private CardPanel cardPanel;

	/** The tool bar. */
	protected ToolBar toolBar;

	/** The prev button. */
	private Button prevBtn;

	/** The next button. */
	private Button nextBtn;

	/** The cancel button. */
	private Button cancelBtn;

	/** The progress indicator. */
	private Indicator progressIndicator = Indicator.DOT;

	/** The hide on finish. */
	private boolean hideOnFinish = true;

	/** The showWestImageContainer. */
	private boolean showWestImageContainer = false;

	/** The panelBackgroundColor. */
	private String panelBackgroundColor = "#F6F6F6";

	private Status status;
	
	private ImageResource wizardMainImage;

	/**
	 * Creates a new wizard window.
	 */
	public WizardWindow() {
		super();
		setSize(540, 500);
		setClosable(true);
		setResizable(false);
		setModal(true);
	}
	
	/**
	 * Creates a new wizard window.
	 * 
	 * @param cards
	 *            an ArrayList of WizardCard/s
	 */
	public WizardWindow(ArrayList<WizardCard> cards) {
		this();
		this.cards.addAll(cards);
	}

	public void addCard(WizardCard card){
		this.cards.add(card);
	}
	/**
	 * On button pressed.
	 * 
	 * @param button
	 *            the button
	 */
	private void onButtonPressed(Button button) {
		if (button == cancelBtn) {
			hide(button);
			return;
		}
		if (button == prevBtn) {
			if (this.currentStep > 0) {
				updateWizard(currentStep--);
			}
		}
		if (button == nextBtn) {
			if (!cards.get(currentStep).isValid())
				return;	
			
			AsyncCallback<Void> validationCallback = new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					if (caught != null)
						MessageBox.alert("Validation Error", caught.getMessage(), null);
				}

				@Override
				public void onSuccess(Void result) {										
					if (currentStep + 1 == cards.size()) {		
						status.setBusy(statusBarText);
						toolBar.disable();
						cards.get(currentStep).fireEvent(Events.WizardStep, new WizardStepEvent(Events.WizardStep));
						cards.get(currentStep).fireEvent(Events.WizardFinish);
						fireEvent(Events.WizardFinish, new BaseEvent(Events.WizardFinish));
						if (hideOnFinish)
							hide();
					} else {
						cards.get(currentStep).fireEvent(Events.WizardStep, new WizardStepEvent(Events.WizardStep));
						updateWizard(currentStep++);
					}
				}
			};
			
			WizardCard wc = cards.get(currentStep);
			if (wc.getValidator() != null){
				wc.getValidator().validate(cards.get(currentStep), currentStep, validationCallback);
			} else {
				validationCallback.onSuccess(null);
			}
		}
	}

	/**
	 * Update wizard.
	 * @param previousStep 
	 */
	private void updateWizard(int previousStep) {		
		WizardStepEvent be = new WizardStepEvent(Events.WizardStep);
		be.setCurrentStep(cards.get(currentStep));
		be.setCurrentStepNumber(currentStep);
		if (previousStep > 0) {
			be.setPreviousStep(cards.get(previousStep));
			be.setPreviousStepNumber(previousStep);
		}
		be.setForward(true);
		
		if (! fireEvent(Events.WizardStep, be)){
			return;		
		}
	
		WizardCard wc = cards.get(currentStep);
		headerPanel.updateIndicatorStep(wc.getCardTitle());
		this.cardPanel.setActiveItem(wc);
		wc.layout();

		if (currentStep + 1 == cards.size()) {
			nextBtn.setText(finishButtonText);
		} else {
			nextBtn.setText(nextButtonText);
		}

		if (currentStep == 0) {
			prevBtn.setVisible(false);
		} else {
			prevBtn.setVisible(true);
		}
	}
		
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.extjs.gxt.ui.client.widget.Window#onRender(com.google.gwt.user.client
	 * .Element, int)
	 */
	@Override
	protected void onRender(Element parent, int pos) {
		setLayout(new BorderLayout());
		super.onRender(parent, pos);

		headerPanel = new Header();
		add(headerPanel, new BorderLayoutData(LayoutRegion.NORTH, 60));
		cardPanel = new CardPanel();
		cardPanel.setStyleAttribute("padding", "20px 15px 5px 5px");
		cardPanel.setStyleAttribute("backgroundColor", panelBackgroundColor);

		LayoutContainer lc = new LayoutContainer();
		lc.setStyleAttribute("backgroundColor", panelBackgroundColor);
		add(lc, new BorderLayoutData(LayoutRegion.EAST, 20));

		if (showWestImageContainer) {
			lc = new LayoutContainer();
			lc.setStyleAttribute("backgroundColor", panelBackgroundColor);
			int width = 100; // min width allowed
			if (wizardMainImage != null) {
				Image leftimage = new Image(wizardMainImage);
				lc.add(leftimage);
				width = Math.max(100, leftimage.getWidth());
			}
			add(lc, new BorderLayoutData(LayoutRegion.WEST, width));
		} else
			add(lc, new BorderLayoutData(LayoutRegion.WEST, 20));

		add(cardPanel, new BorderLayoutData(LayoutRegion.CENTER));
		for (WizardCard wizardCard : cards) {
			cardPanel.add(wizardCard);
		}

		SelectionListener<ButtonEvent> btnListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				onButtonPressed(ce.getButton());
			}
		};

		prevBtn = new Button(previousButtonText);
		prevBtn.setId("prevButton");
		prevBtn.addSelectionListener(btnListener);
		nextBtn = new Button(nextButtonText);
		nextBtn.setId("nextButton");
		nextBtn.addSelectionListener(btnListener);
		cancelBtn = new Button(cancelButtonText);
		cancelBtn.setId("cancelButton");
		cancelBtn.addSelectionListener(btnListener);

		status = new Status();
		
		toolBar = new ToolBar();
		toolBar.add(status);
		toolBar.add(new FillToolItem());
		toolBar.add(prevBtn);
		toolBar.add(nextBtn);
		toolBar.add(cancelBtn);
		add(toolBar, new BorderLayoutData(LayoutRegion.SOUTH, 30));
		
		if (cards.size() > 0) {
			updateWizard(-1);
		}
	}

	/**
	 * Returns the currently set header title.
	 * 
	 * @return the header title
	 */
	public String getHeaderTitle() {
		return headerTitle;
	}

	/**
	 * Sets the title located in the top header.
	 * 
	 * @param hdrtitle
	 *            string value
	 */
	public void setHeaderTitle(String hdrtitle) {
		this.headerTitle = hdrtitle;
	}

	/**
	 * Returns true if the West image container will be shown.
	 * 
	 * @return value of showWestImageContainer
	 */
	public boolean isShowWestImageContainer() {
		return showWestImageContainer;
	}

	/**
	 * Sets if the West image container will be shown.
	 * 
	 * @param showWestImageContainer
	 *            boolean value
	 */
	public void setShowWestImageContainer(boolean showWestImageContainer) {
		this.showWestImageContainer = showWestImageContainer;
	}

	/**
	 * Returns the hex color value of the main panel background. Defaults to
	 * #F6F6F6.
	 * 
	 * @return hex color String
	 */
	public String getPanelBackgroundColor() {
		return panelBackgroundColor;
	}

	/**
	 * Sets the hex color value of the main panel background.
	 * 
	 * @param panelBackgroundColor
	 *            String value
	 */
	public void setPanelBackgroundColor(String panelBackgroundColor) {
		this.panelBackgroundColor = panelBackgroundColor;
	}

	/**
	 * Sets the progress indicator type. Defaults to DOT
	 * 
	 * @param value
	 *            the value
	 */
	public void setProgressIndicator(Indicator value) {
		progressIndicator = value;
	}

	/**
	 * Sets the wizard image picture, or set to null if you don't wish to
	 * display an image.
	 * 
	 * @param image
	 *            the image resource
	 */
	public void setMainImg(ImageResource image) {
		wizardMainImage = image;
	}

	/**
	 * The Class Header.
	 */
	protected class Header extends VerticalPanel {

		/** The indicator panel. */
		private HorizontalPanel indicatorPanel;

		/** The indicator bar. */
		private ProgressBar indicatorBar;

		/** The step html. */
		private Html stepHTML;

		/** The title html. */
		private Html titleHTML;

		/**
		 * Creates a new header.
		 */
		protected Header() {
			super();
			setTableWidth("100%");
			setTableHeight("100%");
			setStyleName("ext-ux-wiz-Header");
			setBorders(true);

			titleHTML = new Html("");
			titleHTML.setStyleName("ext-ux-wiz-Header-title");
			add(titleHTML);

			if (progressIndicator == Indicator.DOT) {

				stepHTML = new Html("");
				stepHTML.setStyleName("ext-ux-wiz-Header-step");
				add(stepHTML);

				indicatorPanel = new HorizontalPanel();
				indicatorPanel.setStyleName("ext-ux-wiz-Header-stepIndicator-container");
				for (int i = 0; i < cards.size(); i++) {
					Image img = new Image(Resources.INSTANCE.wizardStepOff());
					img.setStyleName("ext-ux-wiz-Header-stepIndicator");
					indicatorPanel.add(img);
				}
				TableData td = new TableData();
				td.setHorizontalAlign(HorizontalAlignment.RIGHT);
				add(indicatorPanel, td);
			}
			if (progressIndicator == Indicator.PROGRESSBAR) {
				indicatorBar = new ProgressBar();
				LayoutContainer lc = new LayoutContainer();
				lc.add(indicatorBar);
				lc.setWidth("50%");
				TableData td = new TableData();
				td.setHorizontalAlign(HorizontalAlignment.RIGHT);
				td.setPadding(5);
				add(lc, td);
			}
		}

		/**
		 * Update indicator step.
		 * 
		 * @param cardtitle
		 *            the cardtitle
		 */
		protected void updateIndicatorStep(String cardtitle) {

			final String stepStr = indicateStepText + (1 + currentStep)
					+ indicateOfText + cards.size() + " : " + cardtitle;
			final double stepRatio = (double) (1 + currentStep)
					/ (double) cards.size();
			titleHTML.setHtml(headerTitle);

			if (progressIndicator == Indicator.DOT) {
				stepHTML.setHtml(stepStr);
				indicatorPanel.removeAll();
				for (int i = 0; i < cards.size(); i++) {

					Image img = new Image(Resources.INSTANCE.wizardStepOff());
					if (i == currentStep) {
						 img = new Image(Resources.INSTANCE.wizardStepOn());
					}
					img.setStyleName("ext-ux-wiz-Header-stepIndicator");
					indicatorPanel.add(img);
				}
				indicatorPanel.layout();
			}
			if (progressIndicator == Indicator.PROGRESSBAR) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						indicatorBar.updateProgress(stepRatio, stepStr);
					}
				});
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.extjs.gxt.ui.client.widget.VerticalPanel#onRender(com.google.
		 * gwt.user.client.Element, int)
		 */
		@Override
		protected void onRender(Element parent, int pos) {
			super.onRender(parent, pos);
			setStyleAttribute("borderLeft", "none");
			setStyleAttribute("borderRight", "none");
			setStyleAttribute("borderTop", "none");
		}
	}
	
	/**
	 * Gets the previous button text.
	 * 
	 * @return the previousButtonText
	 */
	public String getPreviousButtonText() {
		return previousButtonText;
	}

	/**
	 * Sets the previous button text.
	 * 
	 * @param previousButtonText
	 *            the previousButtonText to set. Defaults to "< Previous".
	 */
	public void setPreviousButtonText(String previousButtonText) {
		this.previousButtonText = previousButtonText;
	}

	/**
	 * Gets the next button text.
	 * 
	 * @return the nextButtonText
	 */
	public String getNextButtonText() {
		return nextButtonText;
	}

	/**
	 * Sets the next button text.
	 * 
	 * @param nextButtonText
	 *            the nextButtonText to set. Defaults to "Next >".
	 */
	public void setNextButtonText(String nextButtonText) {
		this.nextButtonText = nextButtonText;
	}

	/**
	 * Gets the cancel button text.
	 * 
	 * @return the cancelButtonText
	 */
	public String getCancelButtonText() {
		return cancelButtonText;
	}

	/**
	 * Sets the cancel button text.
	 * 
	 * @param cancelButtonText
	 *            the cancelButtonText to set. Defaults to "Cancel".
	 */
	public void setCancelButtonText(String cancelButtonText) {
		this.cancelButtonText = cancelButtonText;
	}

	/**
	 * Gets the finish button text.
	 * 
	 * @return the finishButtonText
	 */
	public String getFinishButtonText() {
		return finishButtonText;
	}

	/**
	 * Sets the finish button text.
	 * 
	 * @param finishButtonText
	 *            the finishButtonText to set. Defaults to "Finish".
	 */
	public void setFinishButtonText(String finishButtonText) {
		this.finishButtonText = finishButtonText;
	}

	/**
	 * Gets the indicate step text.
	 * 
	 * @return the indicateStepText
	 */
	public String getIndicateStepText() {
		return indicateStepText;
	}

	/**
	 * Sets the indicate step text.
	 * 
	 * @param indicateStepText
	 *            the indicateStepText to set. Defaults to "Step ".
	 */
	public void setIndicateStepText(String indicateStepText) {
		this.indicateStepText = indicateStepText;
	}

	/**
	 * Gets the indicate of text.
	 * 
	 * @return the indicateOfText
	 */
	public String getIndicateOfText() {
		return indicateOfText;
	}

	/**
	 * Sets the indicate of text.
	 * 
	 * @param indicateOfText
	 *            the indicateOfText to set. Defaults to " of ".
	 */
	public void setIndicateOfText(String indicateOfText) {
		this.indicateOfText = indicateOfText;
	}

	/**
	 * Sets the status bar text.
	 * 
	 * @param statusBarText
	 *            the new status bar text
	 */
	public void setStatusBarText(String statusBarText) {
		this.statusBarText = statusBarText;
	}

	/**
	 * Gets the status bar text.
	 * 
	 * @return the status bar text
	 */
	public String getStatusBarText() {
		return statusBarText;
	}

	/**
	 * Checks if hide on finish is set.
	 * 
	 * @return true, if hide on finish is enabled
	 */
	public boolean isHideOnFinish() {
		return hideOnFinish;
	}

	/**
	 * Enables hide on finish.
	 * 
	 * @param hideOnFinish
	 *            true to hide when finish button pressed. Defaults to true.
	 */
	public void setHideOnFinish(boolean hideOnFinish) {
		this.hideOnFinish = hideOnFinish;
	}
	
	public ArrayList<WizardCard> getCards(){
		return cards;
	}
	
	/**
	 * Only show the wizard if it has cards. Otherwise don't show it and fire a WizardFinish event.
	 * 
	 * @see com.extjs.gxt.ui.client.widget.Window#show()
	 */
	@Override
	public void show() {
		if (cards.isEmpty()){
			fireEvent(Events.WizardFinish, new BaseEvent(Events.WizardFinish));
		} else {
			super.show();
		}
	}
}
