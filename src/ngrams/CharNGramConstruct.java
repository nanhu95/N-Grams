package ngrams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class CharNGramConstruct {
	// map to store file - characters in file pairs
	private HashMap<String, ArrayList<Character>> chars;
	// map to store char bigram - freq pairs
	private HashMap<CharNGram, Integer> charBigramFreq;
	// map to store char trigram - freq pairs
	private HashMap<CharNGram, Integer> charTrigramFreq;

	// sum of freq of char bigrams
	private int sumCharBigram;
	// sum of freq of char trigrams
	private int sumCharTrigram;

	// count of unique bigrams
	private int CharBigram;
	// count of unique trigrams
	private int CharTrigram;


	// the alphabet, including space
	char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' '};


	// random number generator
	private Random random = new Random(0);

	/**
	 * constructor
	 * @param toLower		whether to make all characters lowercase
	 */
	public CharNGramConstruct() {
		// initialize data structures
		chars = new HashMap<String, ArrayList<Character>>();
		charBigramFreq = new HashMap<CharNGram, Integer>();
		charTrigramFreq = new HashMap<CharNGram, Integer>();

		// initialize sums
		sumCharBigram = 0;
		sumCharTrigram = 0;

		// initialize counts
		CharBigram = 0;
		CharTrigram = 0;


		// add all possible trigrams to the trigram freq map
		for (int i=0; i<alphabet.length; i++) {
			for (int j=0; j<alphabet.length; j++) {
				for (int k=0; k<alphabet.length; k++) {
					// get char array to put into freq map
					char[] in = {alphabet[i], alphabet[j], alphabet[k]};
					// put into map
					CharNGram trigram = new CharNGram(in);
					charTrigramFreq.put(trigram, 1);
					CharTrigram++;
				}
			}
		}

//		// add all possible bigrams to the bigram freq map
//		for (int i=0; i<alphabet.length; i++) {
//			for (int j=0; j<alphabet.length; j++) {
//				char[] in = {alphabet[i], alphabet[j]};
//				CharNGram bigram = new CharNGram(in);
//				charBigramFreq.put(bigram, 1);
//				CharBigram++;
//			}
//		}

	}

	/**
	 * overloaded constructor for spell checking
	 * @param chars		list of characters
	 */
	public CharNGramConstruct(ArrayList<Character> charsList) {
		// initialize data structures
		chars = new HashMap<String, ArrayList<Character>>();
		chars.put("input", charsList);
		charBigramFreq = new HashMap<CharNGram, Integer>();
		charTrigramFreq = new HashMap<CharNGram, Integer>();

		// initialize sums
		sumCharBigram = 0;
		sumCharTrigram = 0;

		// initialize counts
		CharBigram = 0;
		CharTrigram = 0;

		// add all possible trigrams to the trigram freq map
		for (int i=0; i<alphabet.length; i++) {
			for (int j=0; j<alphabet.length; j++) {
				for (int k=0; k<alphabet.length; k++) {
					// get char array to put into freq map
					char[] in = {alphabet[i], alphabet[j], alphabet[k]};
					// put into map
					CharNGram trigram = new CharNGram(in);
					charTrigramFreq.put(trigram, 1);
					CharTrigram++;
				}
			}
		}

//		// add all possible bigrams to the bigram freq map
//		for (int i=0; i<alphabet.length; i++) {
//			for (int j=0; j<alphabet.length; j++) {
//				char[] in = {alphabet[i], alphabet[j]};
//				CharNGram bigram = new CharNGram(in);
//				charBigramFreq.put(bigram, 1);
//				CharBigram++;
//			}
//		}
//		
		// create the bigram and trigram maps!
		createCharBigram();
		createCharTrigram();

	}


	/**
	 * method to "train"
	 * read in characters from files
	 * @param filename		file to read in from
	 * @throws IOException
	 */
	public void readFile(String filename) throws IOException {
		Scanner input = null;
		// initialize the scanner
		try {
			input = new Scanner(new File(filename));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// initialize list to store characters
		ArrayList<Character> charsInFile = new ArrayList<Character>();
		// string of all words in the document
		String w = "";

		while (input.hasNextLine()) {
			// get next line
			String line = input.nextLine();
			// ignore blank lines
			if (line.isEmpty()||line.trim().equals("")||line.trim().equals("\n")) {
				continue;
			}
			// add each word to string of all words
			for (String word : line.trim().split("\\s+")) {
				w += word;
				w += " ";
			}
		}
		// go through all characters in words
		for (int i = 0; i < w.length(); i++) {
			char c = w.charAt(i);
			int n = (int) c;
			// make sure char is in alphabet or a space
			if ((65<=n&&n<=90)||(97<=n&&n<=122)||n==32) {
				// add to list of chars
				charsInFile.add(c);
			}
		}

		input.close();
		// put into map
		chars.put(filename, charsInFile);
	}


	/**
	 * method to create bigram counts
	 */
	public void createCharBigram() {
		// go through all files
		for (String file:chars.keySet()) {
			ArrayList<Character> charsInFile = chars.get(file);
			// go through all characters, get two at a time
			for (int i=0; i<charsInFile.size()-1; i++) {
				// get two characters
				char char1 = charsInFile.get(i);
				char char2 = charsInFile.get(i+1);
				// put into array
				char[] in = {char1, char2};
				CharNGram bigram = new CharNGram(in);
				// if not in bigram map, add it
				if (!charBigramFreq.containsKey(bigram)) {
					charBigramFreq.put(bigram, 1);
					CharBigram++;
				}
				// if already exist, update freq
				else {
					int freq = charBigramFreq.get(bigram);
					charBigramFreq.put(bigram, freq+1);
				}
				sumCharBigram++;
			}
		}
	}


	/**
	 * method to create tirgram freqs
	 */
	public void createCharTrigram() {
		// go through all files
		for (String file:chars.keySet()) {
			ArrayList<Character> charsInFile = chars.get(file);
			// go through all characters, get three at a time
			for (int i=0; i<charsInFile.size()-2; i++) {
				// get two characters
				char char1 = charsInFile.get(i);
				char char2 = charsInFile.get(i+1);
				char char3 = charsInFile.get(i+2);
				// put into array
				char[] in = {char1, char2, char3};
				CharNGram trigram = new CharNGram(in);
				// if not in bigram map, add it
				if (!charTrigramFreq.containsKey(trigram)) {
					charTrigramFreq.put(trigram, 1);
					CharTrigram++;
				}
				// if already exist, update freq
				else {
					int freq = charTrigramFreq.get(trigram);
					charTrigramFreq.put(trigram, freq+1);
				}
				sumCharTrigram++;
			}
		}
	}

	/**
	 * method to find the next character given two characters
	 * @param charBigram	the two characters, wrapped as a ngram
	 * @return result		the most likely next character
	 */
	public CharNGram nextChar(CharNGram charBigram) {
		// if bigram doesn't exist, return null 
		// since we can't get a next character anyway
		if (!charBigramFreq.containsKey(charBigram)) {
			System.out.println("Given bigram does not exist!");
			return null;
		}
		// int to store frequency
		int freq = 0;
		// charNgram to store what should be returned
		CharNGram result = null;
		// loop through all trigrams
		for (CharNGram charTrigram:charTrigramFreq.keySet()) {
			// if trigram contains the bigram
			if(charBigram.getCharNgram()[0]==charTrigram.getCharNgram()[0]&&charBigram.getCharNgram()[1]==charTrigram.getCharNgram()[1]) {
				// if trigram freq is greater than current freq, replace
				if (charTrigramFreq.get(charTrigram)>freq) {
					result = charTrigram;
					// update frequency
					freq = charTrigramFreq.get(result);
				}
			}
		}
		return result;
	}


	/**
	 * method to get next character based on probability distribution
	 * @param bigram	the two characters wrapped in a bigram
	 * @return trigram	the next character, along with the first two
	 */
	public CharNGram predict(CharNGram bigram) {
		// if bigram doesn't exists, then we cannot proceed
		if (!charBigramFreq.containsKey(bigram)) {
			System.out.println("No such bigram.");
			return null;
		}
		// get list to store trigrams
		ArrayList<CharNGram> trigrams = new ArrayList<CharNGram>();
		// create map to store probabilities of each trigram
		HashMap<CharNGram, Double> probMap = new HashMap<CharNGram, Double>();

		// loop through all trigrams
		for (CharNGram trigram:charTrigramFreq.keySet()) {
			// if bigram is contained, add the trigram to the list
			if (bigram.getCharNgram()[0]==trigram.getCharNgram()[0]
					&& bigram.getCharNgram()[1]==trigram.getCharNgram()[1]) {
				trigrams.add(trigram);
			}
		}

		// for all trigrams, get the probability
		for (CharNGram trigram:trigrams) {
			probMap.put(trigram, findProb(trigram.getCharNgram()));
		}

		// create vector of probabilities
		double[] pVector = new double[probMap.size()];
		// put probabilities in said vector
		int i = 0;
		for (CharNGram trigram:trigrams) {
			pVector[i] = probMap.get(trigram);
			i++;
		}

		StdArrayIO.print(pVector);

		// normalize vector (just in case)
		double[] pVectorNormalized = Matrix.normalizeVector(pVector);
		// put normalized vector into probability map
		int j = 0;
		for (CharNGram trigram:trigrams) {
			probMap.put(trigram, pVectorNormalized[j]);
			j++;
		}

		StdArrayIO.print(pVectorNormalized);

		// get a random number between 0 and 1
		double rand = random.nextDouble();
		double totalProb = 0.0;
		for (CharNGram trigram:probMap.keySet()) {
			// get next character according to prob distribution
			// essentially, we are going through all trigrams
			// and whichever has the probability that makes it
			// greater than the random num gets returned
			totalProb += probMap.get(trigram);
			if (rand<totalProb) {
				return trigram;
			}
		}

		return null;
	}

	/**
	 * method to find the probability of a certain trigram
	 * @param input		the trigram
	 * @return			the probability
	 */
	public double findProb(char[] input) {
		if (input.length!=3) {
			System.out.println("Input is not a trigram!");
			return 0.0;
		}

		double probOfBigram = 0;
		double probOfTrigram = 0;
		char[] c1c2 = {input[0], input[1]};
		CharNGram bigram = new CharNGram(c1c2);
		CharNGram trigram = new CharNGram(input);

		if (!charBigramFreq.containsKey(bigram)) {
			//System.out.println("No such bigram.");
			// if bigram doesn't exist, assume it does and give it the 
			// lowest possible probability
			return 1.0/sumCharBigram;
		}
		else {
			probOfBigram = (double)charBigramFreq.get(bigram)/sumCharBigram;
		}

		if (!charTrigramFreq.containsKey(trigram)) {
			System.out.println("No such trigram.");
			return 0.0;
		}
		else {
			probOfTrigram = (double)charTrigramFreq.get(trigram)/sumCharTrigram;
		}

		return probOfTrigram/probOfBigram;

	}



	// getters
	public HashMap<String, ArrayList<Character>> getChars() {
		return chars;
	}

	public HashMap<CharNGram, Integer> getCharBigramFreq() {
		return charBigramFreq;
	}

	public HashMap<CharNGram, Integer> getCharTrigramFreq() {
		return charTrigramFreq;
	}

	public int getSumCharBigram() {
		return sumCharBigram;
	}

	public int getSumCharTrigram() {
		return sumCharTrigram;
	}

	public int getCharBigram() {
		return CharBigram;
	}

	public int getCharTrigram() {
		return CharTrigram;
	}
}
