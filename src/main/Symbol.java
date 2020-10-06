package main;

/**
 * Ein Symbol enth�lt einen String und dessen H�ufigkeit.
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
