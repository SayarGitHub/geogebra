package geogebra.web.util.keyboard;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * A button of the {@link OnScreenKeyBoard}
 */
public class KeyBoardButton extends SimplePanel {

	private String feedBack;
	private Label label;
	private boolean isNavigationButton = false;

	/**
	 * @param caption
	 *            text of the button
	 * @param handler
	 *            {@link ClickHandler}
	 */
	public KeyBoardButton(String caption, ClickHandler handler) {
		this(caption, caption, handler);
	}

	/**
	 * @param caption
	 *            text of the button
	 * @param feedBack
	 *            String to send if click occurs
	 * @param handler
	 *            {@link ClickHandler}
	 */
	public KeyBoardButton(String caption, String feedBack, ClickHandler handler) {
		this.label = new Label(caption);
		this.feedBack = feedBack;
		addDomHandler(handler, ClickEvent.getType());
		addStyleName("KeyBoardButton");
		this.add(label);
	}

	/**
	 * @return the String to be sent if a click occurs
	 */
	public String getText() {
		return feedBack;
	}

	/**
	 * @return text of the button
	 */
	public String getCaption() {
		return label.getText();
	}

	/**
	 * @param caption
	 *            text of the button
	 * @param setAsFeedback
	 *            if {@code true} the text of the {@link #feedBack} is set to
	 *            the given caption
	 */
	public void setCaption(String caption, boolean setAsFeedback) {
		label.setText(caption);
		if (setAsFeedback) {
			feedBack = caption;
		}
	}

	/**
	 * @return true if the button is only used for navigation; false otherwise
	 */
	public boolean isNavigationButton() {
		return isNavigationButton;
	}

	/**
	 * @param isNavigationButton
	 *            sets whether the button is only used for navigation (true) or
	 *            not (false)
	 */
	public void setNavigationButton(boolean isNavigationButton) {
		this.isNavigationButton = isNavigationButton;
	}

}
