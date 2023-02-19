package application;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javafx.util.Pair;

public class Processor {

	private static String[] choices = new String[] { "BDT", "USD", "INR" }; // data for choice box
	private static Double[][] factors = new Double[][] { { 1.0, 0.012, 0.87 }, { 84.67, 1.0, 73.78 },
			{ 1.5, 0.014, 1.0 } }; // multiplication factors for currency converter
	private static Map<Pair<String, String>, Double> factor = new HashMap<>(); // collection to map conversion factors

	public static void store(String input, String output) { // function to store calculation log to file
		try {
			FileWriter fw = new FileWriter("log.txt", true);
			Date date = new Date();
			fw.write(date.toString() + "\n");
			fw.write(input);
			fw.write(output + "\n\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void clearLog() { // clearing the log file
		try {
			FileWriter fw = new FileWriter("log.txt");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String erase(String s) { // function to erase to last char of a string
		StringBuffer sb = new StringBuffer(s);
		sb.deleteCharAt(sb.length() - 1); // erasing last character
		return sb.toString();
	}

	public static double calculate(double a, double b, char c) {
		switch (c) {
		case '+':
			return a + b;
		case '-':
			return a - b;
		case '×':
			return a * b;
		case '÷':
			return a / b;
		}
		return 0;
	}

	public static boolean isOperator(char c) {
		if (c == '+' || c == '-' || c == '÷' || c == '×')
			return true;
		return false;
	}

	public static String[] getChoice() {
		return choices;
	}

	public static void setFactor() { // mapping factors
		for (int i = 0; i < choices.length; i++) {
			for (int j = 0; j < choices.length; j++) {
				factor.put(new Pair<String, String>(choices[i], choices[j]), factors[i][j]);
			}
		}
	}

	public static double getFactor(String a, String b) {
		return factor.get(new Pair<String, String>(a, b));
	}
}
