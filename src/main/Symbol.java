package main;

/**
 * Ein Symbol enthält einen String und dessen Häufigkeit.
 * 
 * @author Benjamin Huber
 * @author Julia Mueller
 *
 */
public class Symbol {
	String symbol;
	int frequency;
	
	public Symbol(String buchstabe, int frequency) {
		this.symbol = buchstabe;
		this.frequency = frequency;
	}

	public int compareTo(Symbol c2) {
		return Integer.valueOf(this.frequency).compareTo(Integer.valueOf(c2.frequency));
	}
}
