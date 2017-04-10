
import java.util.Scanner;

import javax.swing.*;
import javax.swing.text.JTextComponent;

public class SwingValidator {

	public boolean isPresent(JTextComponent c, String title) {
		if (c.getText().length() == 0) {
			showNegativeMessage(c, title + " is a required field.");
			c.requestFocusInWindow();
			return false;
		}
		return true;
	}

	public void showNegativeMessage(JTextComponent c, String message) {
		JOptionPane.showMessageDialog(c, message, "Invalid Entry", JOptionPane.ERROR_MESSAGE);
	}

	public void showPositiveMessage(JTextComponent c, String message) {
		JOptionPane.showMessageDialog(c, message, "Success", JOptionPane.DEFAULT_OPTION);
	}

	public boolean isValidName(JTextComponent c, String title) {
		String name = c.getText();

		for (char z : name.toCharArray()) {
			if (!Character.isAlphabetic(z) && z != ' ' && z != '.' && z != ',') {
				showNegativeMessage(c, title + " must be valid name.");
				c.requestFocusInWindow();
				return false;
			}

		}
		return true;

	}

	public boolean isValidEmail(JTextComponent c, String title) {
		String name = c.getText();

		if (name.contains("@") && name.contains("."))
			return true;
		else {
			showNegativeMessage(c, title + " must be valid email address.");
			c.requestFocusInWindow();
			return false;
		}

	}

	public boolean isValidPhoneNumber(JTextComponent c, String title) {
		String name = c.getText();

		for (char z : name.toCharArray()) {
			if (!Character.isDigit(z) && z != '-' && z != '(' && z != ')' && z != ' ' && z != '.') {
				showNegativeMessage(c, title + " must be valid phone number.");
				c.requestFocusInWindow();
				return false;
			}

		}
		return true;
	}
}
