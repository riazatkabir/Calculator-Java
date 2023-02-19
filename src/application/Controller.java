package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller implements Initializable {

	@FXML // fx:id="display"
	private TextField display; // Value injected by FXMLLoader
	@FXML
	private TextField secondaryDisplay;
	@FXML
	private TextArea history;
	@FXML
	private TextArea log;
	@FXML
	private ChoiceBox<String> convertFrom;
	@FXML
	private ChoiceBox<String> convertTo;
	@FXML
	private TextField inputedCurrencyAmount;
	@FXML
	private TextField conversionResult;
	private double number = 0, memory = 0;
	private char operator = '+';
	private String displayText = "", secondaryDisplayText = "";

	public void digits(ActionEvent ae) { // program call this function when any digit buttons will be pressed
		if (!secondaryDisplayText.isEmpty()
				&& !Processor.isOperator(secondaryDisplayText.charAt(secondaryDisplayText.length() - 1))) {
			clear();
		}
		String newText = ((Button) ae.getSource()).getText();
		displayText += newText; // adding new input with the exising one
		display.setText(displayText);
		secondaryDisplay.setText(secondaryDisplayText);
	}

	public void operation(ActionEvent ae) { // program call this function when any operator buttons will be pressed
		if (displayText.isEmpty() && secondaryDisplayText.isEmpty())
			return;
		String newOperator = ((Button) ae.getSource()).getText();
		if (displayText.isEmpty()
				&& Processor.isOperator(secondaryDisplayText.charAt(secondaryDisplayText.length() - 1))) {
			secondaryDisplayText = Processor.erase(secondaryDisplayText);
		}
		double tempNumber = 0;
		if (operator == '/' || operator == '×') {
			tempNumber = 1;
		}
		if (!displayText.isEmpty())
			tempNumber = Double.parseDouble(displayText);
		number = Processor.calculate(number, tempNumber, operator);
		if (tempNumber < 0)
			secondaryDisplayText += "(" + displayText + ")" + newOperator;
		else
			secondaryDisplayText += displayText + newOperator;
		displayText = "";
		secondaryDisplay.setText(secondaryDisplayText);
		display.setText(Double.toString(number));
		operator = newOperator.charAt(0);
	}

	public void erase(ActionEvent ae) { // program call this function when backspace button will be pressed
		if (displayText.isEmpty()) // checking if anything to erase
			return;
		displayText = Processor.erase(displayText);
		if (displayText.isEmpty())
			display.setText("0");
		else
			display.setText(displayText);
	}

	public void clear(ActionEvent ae) { // program call this function when C/CE button will be pressed
		clear();
		display.setText("0");
		secondaryDisplay.setText("");
	}

	public void negate(ActionEvent ae) { // program call this function when +/- button will be pressed
		if (displayText.isEmpty())
			return;
		double temp = Double.parseDouble(displayText);
		temp *= -1;
		displayText = Double.toString(temp);
		display.setText(displayText);
	}

	public void square(ActionEvent ae) { // program call this function when root button will be pressed
		if (displayText.isEmpty()) { // checking if anything inserted
			return;
		}
		double tempNumber = Double.parseDouble(displayText);
		number = Processor.calculate(number, tempNumber * tempNumber, operator);
		secondaryDisplayText += "sqr(" + displayText + ")";
		secondaryDisplay.setText(secondaryDisplayText);
		display.setText(Double.toString(number));
		displayText = "";
		operator = '+';
	}

	public void squareRoot(ActionEvent ae) { // program call this function when square root button will be pressed
		if (displayText.isEmpty()) { // checking if anything inserted
			return;
		}
		double tempNumber = Double.parseDouble(displayText);
		number = Processor.calculate(number, Math.sqrt(tempNumber), operator);
		if (tempNumber < 0) { // negative square root is not possible
			display.setText("Invalid input");
			clear();
			return;

		} else
			secondaryDisplayText += "sqrt(" + displayText + ")";
		displayText = "";
		secondaryDisplay.setText(secondaryDisplayText);
		display.setText(Double.toString(number));
		displayText = "";
		operator = '+';
	}

	public void oneOverX(ActionEvent ae) { // program call this function when 1/X button will be pressed
		if (displayText.isEmpty()) { // checking if anything inserted
			return;
		}
		double tempNumber = Double.parseDouble(displayText);
		number = Processor.calculate(number, 1.0 / tempNumber, operator);
		if (tempNumber < 0)
			secondaryDisplayText += "(1/(" + displayText + "))";
		else
			secondaryDisplayText += "(1/" + displayText + ")";
		displayText = "";
		secondaryDisplay.setText(secondaryDisplayText);
		display.setText(Double.toString(number));
	}

	public void memoryRecall(ActionEvent ae) { // program call this function when MR button will be pressed
		displayText = Double.toString(memory);
		display.setText(displayText);
	}

	public void memoryClear(ActionEvent ae) { // program call this function when MC button will be pressed
		memory = 0;
	}

	public void memoryStore(ActionEvent ae) { // program call this function when MS button will be pressed
		memory = Double.parseDouble(displayText);
	}

	public void memoryPlus(ActionEvent ae) { // program call this function when M+ button will be pressed
		memory += Double.parseDouble(displayText);
	}

	public void memoryMinus(ActionEvent ae) { // program call this function when M- button will be pressed
		memory -= Double.parseDouble(displayText);
	}

	public void calculate(ActionEvent ae) { // program call this function when = button will be pressed
		if (secondaryDisplayText.isEmpty()) { // if nothing to calculate
			return;
		}
		if (displayText.isEmpty()) { // if no new input
			if (secondaryDisplayText.charAt(secondaryDisplayText.length() - 1) != ')')
				secondaryDisplayText = Processor.erase(secondaryDisplayText);
		} else {
			double tempNumber = Double.parseDouble(displayText);
			number = Processor.calculate(number, tempNumber, operator);
			if (tempNumber < 0)
				secondaryDisplayText += "(" + displayText + ")";
			else
				secondaryDisplayText += displayText;
		}
		secondaryDisplayText = secondaryDisplayText + " = ";
		secondaryDisplay.setText(secondaryDisplayText);
		displayText = Double.toString(number);
		display.setText(displayText);
		history.appendText(secondaryDisplayText);
		history.appendText(displayText + "\n\n");
		Processor.store(secondaryDisplayText, displayText);
		clear();
	}

	public void convert(ActionEvent ae) { // program call this function when convert button will be pressed
		if (inputedCurrencyAmount.getText().isEmpty() || convertFrom.getValue() == null || convertTo.getValue() == null) // checking
																															// validity
			return;
		Double amount = Double.parseDouble(inputedCurrencyAmount.getText());
		conversionResult
				.setText(Double.toString(amount * Processor.getFactor(convertFrom.getValue(), convertTo.getValue())));
	}

	public void clearHistory(ActionEvent ae) { // program call this function when Clear_History button will be pressed
		history.setText("");
	}

	public void refreshLog(ActionEvent ae) { // program call this function when Refresh button will be pressed
		log.setText("");
		try {
			FileInputStream fin = new FileInputStream("log.txt");
			int ch;
			while ((ch = fin.read()) != -1) {
				String str = "" + (char) ch;
				log.appendText(str);
			}

			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clearLog(ActionEvent ae) { // program call this function when Clear_Log button will be pressed
		Processor.clearLog();
		log.setText("");
	}

	private void clear() { // function to reset the calculator
		number = 0;
		operator = '+';
		displayText = "";
		secondaryDisplayText = "";
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Processor.setFactor();
		convertFrom.getItems().addAll(Processor.getChoice());// mapping the currency converter factors
		convertTo.getItems().addAll(Processor.getChoice()); // adding option to choice box
	}
}
