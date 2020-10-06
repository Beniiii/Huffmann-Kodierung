package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Julia Mueller
 * @author Benjamin Huber
 *
 */
public class Huffmann {

	// Aufgabe 01. ********************************************************************
	
	// normalen String aus einem File auslesen
	public static String readFile(String filename) throws IOException {
		String content = null;
		File file = new File(filename);
		FileReader reader = null;
		try {
			reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return content;
	}
	
	
	// Aufgabe 02. ********************************************************************
	
	/*
	 * Generiert ein Array mit grösse 128, dass alle Ascii-zeichen enthält 
	 * und bei jedem Symbol die Häufigkeit auf 0 initialisiert.
	 */
	public static Symbol[] generateAsciiArray() {
		Symbol[] asciiArray = new Symbol[128];
		for (int i = 0; i < asciiArray.length; i++) {
			asciiArray[i] = new Symbol("" + (char) i, 0);
		}
		return asciiArray;
	}

	/**
	 * Zählt, wie oft ein char in einem char-Array vorkommt 
	 * und speichert die Anzahl entprechenden Symbol im ascii-Array ab.
	 * 
	 * @param asciiArray: Enthält alle 128 ascii-Zeichen in Form von Symbolen und Häufigkeit 0
	 * @param textArray: Enthält den zu codierenden String als char-Array
	 * @return asciiArray: Enthält alle 128 ascii-Zeichen in Form von Symbolen und abgezählter Häufigkeit.
	 */
	public static Symbol[] countFrequency(Symbol[] asciiArray, char[] textArray) {
		for (int i = 0; i < textArray.length; i++) {
			for (int j = 0; j < asciiArray.length; j++) {
				if (textArray[i] == asciiArray[j].symbol.charAt(0)) {
					asciiArray[j].frequency++;
				}
			}
		}
		return asciiArray;
	}

	/**
	 * Gibt eine Liste aus mit nur Symbolen, die eine Häufigkeit von mehr als 0 haben (also diejenigen, die im Text vorkommen).
	 * 
	 * @param frequencyArray: Enthält alle 128 ascii-Zeichen in Form von Symbolen und abgezählter Häufigkeit.
	 * @return appereanceOfCharsList: Liste von Symbolen die eine Häufigkeit von mehr als 0 haben.
	 */
	public static List<Symbol> calculateAppereanceProbability(Symbol[] frequencyArray) {
		List<Symbol> appereanceOfCharsList = new ArrayList<>();
		for (int i = 0; i < frequencyArray.length; i++) {
			if (frequencyArray[i].frequency > 0) {
				appereanceOfCharsList.add(frequencyArray[i]);
			}
		}
		return appereanceOfCharsList;
	}

	
	// Aufgabe 03. ********************************************************************	
	
	/**
	 * Erstellt die Nodes für den Tree und platziert alle in die Root Node.  
	 * Die Elemente der Häufigkeitstabelle werden dabei bei jedem Schritt zusammengefasst.
	 * Anschliessend wird buildTree aufgerufen um den Baum zu bauen.
	 * 
	 * @param appereanceOfCharsList: die Häufigkeitstabelle
	 * @return root: Die root Node, die alle anderen Nodes enthält
	 */
	public static Node buildNodes(List<Symbol> appereanceOfCharsList) {
		Node root = new Node("", "");
		boolean leftOrRight = false;
		while (appereanceOfCharsList.size() > 1) {
			int frequency = 0;
			String chars = null;
			Symbol symbol1 = appereanceOfCharsList.get(appereanceOfCharsList.size() - 1);
			Symbol symbol2 = appereanceOfCharsList.get(appereanceOfCharsList.size() - 2);
			frequency = symbol1.frequency + symbol2.frequency;
			chars = "" + symbol1.symbol + symbol2.symbol;
			Symbol combined = new Symbol(chars, frequency);
			Node test = new Node(chars, "");
			test.addChildren(new Node(symbol1.symbol, "0"), new Node(symbol2.symbol, "1"));
			appereanceOfCharsList.remove(symbol1);
			appereanceOfCharsList.remove(symbol2);
			appereanceOfCharsList.add(combined);
			appereanceOfCharsList.sort((c1, c2) -> c1.compareTo(c2));
			Collections.reverse(appereanceOfCharsList);
			if (leftOrRight == false) {
				test.nodeType = "0";
			} else if (leftOrRight == true) {
				test.nodeType = "1";
			}
			leftOrRight = !leftOrRight;
			root.addChildren(test);
		}
		root.children.remove(root.children.size() - 1);
		Collections.reverse(root.children);
		return buildTree(root);
	}

	/**
	 * Platziert die Nodes in der Root node an der richtigen Stelle und konstruiert so den Baum.
	 * Hinweis: System outs entkommentieren, um zu sehen, welche Nodes gespeichert werden.
	 * 
	 * @param node: die Root Node
	 * @return node: Root Node, die den kompletten Baum enthält
	 */
	public static Node buildTree(Node node) {
		for (int i = node.children.size() - 1; i >= 2; i--) {
			for (int j = i - 1; j >= 0; j--) {
				Node child1 = node.children.get(j).children.get(0);
				Node child2 = node.children.get(j).children.get(1);
					if (node.children.get(i).name == child1.name) {
						node.children.get(j).children.remove(child1);
						node.children.get(j).addChildren(new Node(node.children.get(i)));
						if(node.children.get(j).children.get(0).nodeType == "0") {
							node.children.get(j).children.get(1).nodeType = "1";
						}else if(node.children.get(j).children.get(0).nodeType == "1") {
							node.children.get(j).children.get(1).nodeType = "0";
						}
//						System.out.println("Saved in child: "+child1.name);
//						System.out.println(child1.children.size());
					}
					if (node.children.get(i).name == child2.name) {
						node.children.get(j).children.remove(child2);
						node.children.get(j).addChildren(new Node(node.children.get(i)));
						if(node.children.get(j).children.get(0).nodeType == "0") {
							node.children.get(j).children.get(1).nodeType = "1";
						}else if(node.children.get(j).children.get(0).nodeType == "1") {
							node.children.get(j).children.get(1).nodeType = "0";
						}
//						System.out.println("Saved in child: "+child2.name);
//						System.out.println(child2.children.size());
					}
				
			}
			node.children.remove(i);
		}

		return node;
	}
	
	/**
	 * Iteriert rekursiv durch die Nodes des Baumes und generiert so das Codewort.
	 * 
	 * @param root: Der Baum
	 * @param symbol: Das ascii-Zeichen, zu dem ein Codewort generiert werden soll
	 * @param codeWord: Das aufbauende Codewort (wird benötigt weil rekursiv)
	 * @return result: Das Codewort
	 */
	public static String buildCodeWord(Node root, String symbol, String codeWord) {
		String result = codeWord;
		for (Node child : root.children) {
			if (child.name.contains(symbol)) {
				result += child.nodeType;
			}
			result = buildCodeWord(child, symbol, result);
		}
		return result;
	}
	
	
	// Aufgabe 04. ********************************************************************
	
	/**
	 * Gibt eine Stringliste zurück, wo die einzelnen Elemente im Format <ASCII-CODE von Zeichen: Code von Zeichen> gespeichert werden, 
	 * die dann in ein File geschrieben werden kann.
	 * 
	 * @param characters: Array aus Symbolen, die im Text vorkommen mit deren Häufigkeit
	 * @param root: der Baum
	 * @return codeTable: Elemente der Liste im vorgegebenen Format
	 */
	public static List<String> buildCodeTable(Symbol[] characters, Node root) {
		List<String> codeTable = new ArrayList<>();
		String code;		
		String ascii;
		for (Symbol symbol : characters) {
			ascii = ""+(int)symbol.symbol.charAt(0);
			code = buildCodeWord(root, symbol.symbol, "");
			if(code.length()>0) {
				code = ascii + ":" + code + "-";
				codeTable.add(code);
			}
		}
		String endString = codeTable.get(codeTable.size()-1);
		codeTable.remove(endString);
		endString = endString.substring(0, endString.length()-1);
		codeTable.add(endString);
		return codeTable;
	}
	
	/**
	 * Schreibt die codeTable in das File "dec_tab.txt" 
	 * 
	 * @param codeTable: Elemente der Liste im vorgegebenen Format
	 * @throws IOException: Falls das File nicht gefunden wird.
	 */
	public static void writeCodeTableInFile(List<String> codeTable) throws IOException {
		String stringOfCodes = "";
		for (String string : codeTable) {
			stringOfCodes += string;
		}
		writeInFile(stringOfCodes, "dec_tab.txt");
	}
	
	/**
	 * Präpariert die codeTable damit die Strings richtig gelesen werden können (löscht alle '-')
	 * 
	 * @param codeTable: Elemente der Liste im vorgegebenen Format
	 * @return preparedList: vorbereitete Liste
	 */
	public static List<String> prepareForEncoding(List<String> codeTable){
		List<String> preparedList = new ArrayList<>();
		for (String string : codeTable) {
			String corrected = string;
			corrected = corrected.replaceAll("-", "");
			preparedList.add(corrected);
		}
		return preparedList;
	}

	/**
	 * Schreibt einen String in ein File.
	 * 
	 * @param text: Der zu schreibende String
	 * @param filename: Das File, in das geschrieben werden soll
	 * @throws IOException: Falls das File nicht gefunden wird.
	 */
	public static void writeInFile(String text, String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(text);
        writer.close();
    }
	
	/**
	 * Kreiert die Kodiertabelle als Map aus der vorbereitenen Liste.
	 * 
	 * @param preparedList: Die vorbereitete Liste
	 * @return encodeTable: Die Kodiertabelle als Map
	 */
	public static Map<Character, String> createEncodeTable (List<String> preparedList){
		Map<Character, String> encodeTable= new HashMap<>();
		char symbol;
		String codeWord = null;
		String[] splittedString;
		for (String string : preparedList) {
			splittedString = string.split(":");
			String ascii = splittedString[0];
			symbol = (char)Integer.parseInt(ascii);
			codeWord = splittedString[1];
			encodeTable.put(symbol, codeWord);
		}
		return encodeTable;
	}
	
	
	// Aufgabe 05. ********************************************************************
	
	/**
	 * Kodiert den Text in einen Bitstring mit Hilfe der Kodiertabelle.
	 * 
	 * @param message: Der zu kodierende Text als String
	 * @param encodeTable: Die Kodiertabelle als Map
	 * @return encodedMessage: Text kodiert als Bitstring
	 */
	public static String encode(String message, Map<Character, String> encodeTable) {
		char[] messageArray = message.toCharArray();
		String encodedMessage = "";
		for (char c : messageArray) {
			encodedMessage = encodedMessage + encodeTable.get(c);
		}
		return encodedMessage;
	}
	
	
	// Aufgabe 06. ********************************************************************
	
	/**
	 * Hängt an einen Bitstring eine 1 und anschliessend so viele Nullen, bis der Bitstring eine Länge hat, 
	 * die ein vielfaches von 8 ist.
	 * 
	 * @param encodedMessage: Der kodierte Text als Bitstring
	 * @return den ergänzten BitString
	 */
	public static String appendBits (String encodedMessage) {
		StringBuilder builder = new StringBuilder(encodedMessage);
		builder.append("1");
		while(builder.length() %8 != 0) {
			builder.append("0");
		}
		return builder.toString();
	}
	
	
	// Aufgabe 07. ********************************************************************
	
	/**
	 * Konvertiert einen zu einem byte-Array, in dem je 8 aufeinanderfolgende Zeichen zu je 
	 * einem byte zusammengefasst werden.
	 * 
	 * @param byteStringMessage: der ergänzte Bitstring
	 * @return den Bitstring zusammengefasst in einem byte-Array
	 */
	public static byte[] convertStringToByteArray (String byteStringMessage) {
		Byte[] byteArray = new Byte[byteStringMessage.length() / 8];
		byte[] convertedByteArray = new byte[byteArray.length];
		List<Byte> byteList = new ArrayList<>();
		for(int i = 0; i < byteStringMessage.length(); i += 8) {
			String oneByte = byteStringMessage.substring(i, i+8);
			byteList.add((byte)Integer.parseInt(oneByte, 2)); 
		}
		byteArray = byteList.toArray(new Byte[byteList.size()]);
		for (int i = 0; i < byteArray.length; i++) {
			convertedByteArray[i] = byteArray[i];
		}
		return convertedByteArray;
	}
	
	
	// Aufgabe 08. ********************************************************************
	
	/**
	 * Schreibt ein byte-Array in ein File
	 * 
	 * @param byteArray: Das zu schreibende byte-Array
	 * @param fileName: Das File, in das geschrieben werden soll
	 * @throws IOException: Wenn das File nicht gefinden wird
	 */
	public static void writeByteArrayToFile (byte[] byteArray, String fileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(new File(fileName));
		fos.write(byteArray);
		fos.close();
	}
	
	
	// Aufgabe 09. ********************************************************************
	
	/**
	 * Liest ein byte-Array aus einem File aus.
	 * 
	 * @param fileName: Das File, aus dem gelesen werden soll
	 * @return bytes: Das eingelesene byte-Array
	 * @throws IOException: Falls das File nicht gefunden wird
	 */
	public static byte[] readByteArrayFromFile (String fileName) throws IOException {
		File file = new File(fileName);
		byte[] bytes = new byte[(int) file.length()];
		FileInputStream fis = new FileInputStream(file);
		fis.read(bytes);
		fis.close();
		return bytes;
	}
	
	/**
	 * Convertiert ein byte-Array zu einem Bitstring
	 * 
	 * @param bytes: Das byte-Array
	 * @return byteString
	 */
	public static String convertByteArrayToString (byte[] bytes) {
		String byteString = "";
		for (byte b : bytes) {
			byteString = byteString + String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
		}
		return byteString;
	}
	
	/**
	 * Schneidet die letzte 1 und alle folgenden Nullen eines BitStrings ab.
	 * 
	 * @param byteString: Der zu bearbeitende Bitstring
	 * @return den bearbeitenten Bitstring
	 */
	public static String cutLastBits (String byteString) {
		StringBuilder builder = new StringBuilder(byteString);
		int i = byteString.length()-1;
		while(builder.charAt(i) != '1') {
			i--;
		}
		builder.delete(i, byteString.length());
		return builder.toString();	
	}
	
	/**
	 * Erstellt eine Codetabelle in Form eines zweimensionalen String-Arrays 
	 * aus einer eingelesenen Kodierungstabelle in Form eines Strings.
	 * 
	 * @param stringOfCodes: Kodierungstabelle als String
	 * @return codeTable: Kodierungstabelle als zweidimensionales String-Array
	 */
	public static String[][] createCodeTableFromString (String stringOfCodes){
		int counter = 0;
		for (int i = 0; i < stringOfCodes.length(); i++) {
			if (stringOfCodes.charAt(i) == ':') counter++;
		}
		String[][] codeTable = new String[counter][2];
		String value = "";
		String key = "";
		int j = 0;
		for (int i = 0; i < stringOfCodes.length(); i++) {
			if(stringOfCodes.charAt(i) != ':' && stringOfCodes.charAt(i) != '-') {
				value = value + stringOfCodes.charAt(i);
			}else if(stringOfCodes.charAt(i) == ':') {
				key = value;
				value = "";
			}
			if(stringOfCodes.charAt(i) == '-') {
				codeTable[j][0] = key;
				codeTable[j][1] = value;
				value = "";
				j++;
			}
		}
		codeTable[j][0] = key;
		codeTable[j][1] = value;
		return codeTable;
	}
	
	/**
	 * Dekodiert einen Bitstring zu einem lesbaren String.
	 * Hinweis: Die System.out entkommentieren um beobachten zu können, 
	 *          wie die dekodierte Nachricht aufgebaut wird.
	 * 
	 * @param decodeTable: Die Kodierungstabelle als zweidimensionales Array
	 * @param encodedMessage: Der kodierte Text als Bitstring
	 * @return message: Der dekodierte Text als String
	 */
	public static String decode (String[][] decodeTable, String encodedMessage) {
		String message = "";
		String codeWord = "";

		for (int i = 0; i < encodedMessage.length(); i++) {
			codeWord = codeWord + encodedMessage.charAt(i);
//			System.out.println("codeWord:" +codeWord);
			for (int j = 0; j < decodeTable.length; j++) {
				if (codeWord.equals(decodeTable[j][1])) {
					message = message + (char)Integer.parseInt(decodeTable[j][0]);
					codeWord = "";
//					System.out.println("message: " + message);
				}
			}
			
		}
		return message;
	}

	
	public static void main(String[] args) throws IOException {
		String fileName = "text.txt";
		String text = readFile(fileName);
		char[] textArray = text.toCharArray();
		Symbol[] frequencyArray = generateAsciiArray();
		frequencyArray = countFrequency(frequencyArray, textArray);
		Symbol[] charArray = frequencyArray;
		List<Symbol> appereanceOfCharsList = calculateAppereanceProbability(frequencyArray);

		appereanceOfCharsList.sort((c1, c2) -> c1.compareTo(c2));
		Collections.reverse(appereanceOfCharsList);

		Node root = buildNodes(appereanceOfCharsList);

		
		List<String> codeTable = buildCodeTable(charArray, root);

		
		writeCodeTableInFile(codeTable);
		codeTable = prepareForEncoding(codeTable);
		
		Map<Character, String> encodeTable = createEncodeTable(codeTable);
		
		String message = readFile(fileName);
		String encodedMessage = encode(message, encodeTable);
		System.out.println("encodedMessage: "+encodedMessage);
		String byteStringMessage = appendBits(encodedMessage);
		System.out.println("appendedByteString: "+byteStringMessage);
		byte[] byteArray = convertStringToByteArray(byteStringMessage);
		
		writeByteArrayToFile(byteArray, "output.dat");
		byte[] bytes = readByteArrayFromFile("output.dat");
		
		String byteString = convertByteArrayToString(bytes);
		System.out.println("byteString: " +byteString);
		
		String cutedString = cutLastBits(byteString);
		System.out.println("cutedString: "+cutedString);
		
		String stringOfCodes = readFile("dec_tab.txt");
		System.out.println("stringOfCodes from dec_tab.txt: "+stringOfCodes);
		
		String[][] decodeTable = createCodeTableFromString(stringOfCodes);
		System.out.println();
		System.out.println("Kodiertabelle");
		for (int i = 0; i < decodeTable.length; i++) {
			System.out.println("char: "+(char)Integer.parseInt(decodeTable[i][0]));
			System.out.println("code: "+decodeTable[i][1]);
		}
		
		String decodedMessage = decode(decodeTable, cutedString);
		System.out.println();
		System.out.println(decodedMessage);
		
		writeInFile(decodedMessage, "decompress.txt");
		
		
		// Aufgabe 10. ********************************************************************
		
		/**
		 * Die Textdatei (text.txt) hat eine Grösse von 51 Bytes, während das die Byte-Array-Datei (output.dat) eine Grösse von 26 Bytes hat. 
		 * Der Binärstring hat eine Grösse von 208 Bits (26*8). Eingespart werden in diesem Beispiel also 25 Bytes (knapp die Hälfte).
		 * 
		 * In einem anderen Beispiel mit folgendem Text: 
		 * Wir sparen Platz da es sich lohnt, Platz zu sparen! Aber was wenn der Text weiter Zeilen Code enthaeldt? lohnt es sich dann immer noch?
		 * Hier hatte die Textdatei eine Grösse von 135 Bytes und der Binärstring 72 Bytes.
		 */
		
		
		// Aufgabe 11. ********************************************************************
		
		byte[] madaBytes = readByteArrayFromFile("output-mada.dat");
		String madaByteString = convertByteArrayToString(madaBytes);
		String madaCutedString = cutLastBits(madaByteString);
		String madaStringOfCodes = readFile("dec_tab-mada.txt");
		String[][] madaDecodeTable = createCodeTableFromString(madaStringOfCodes);
		String madaDecodedMessage = decode(madaDecodeTable, madaCutedString);
		System.out.println();
		System.out.println("Text aus dem AD:");
		System.out.println(madaDecodedMessage);
		writeInFile(madaDecodedMessage, "decompress-mada.txt");
		
	}
	
}