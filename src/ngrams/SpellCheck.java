package ngrams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class SpellCheck {

	// list to store characters in file
	private ArrayList<Character> chars;
	// list to store the error characters
	private ArrayList<Character> errChars;
	// store error rate (0.10, 0.20, etc.)
	private double errRate;

	// the alphabet, NOT including space
	char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' '};

	// random number generator
	Random random  = new Random(0);

	// need a CharNGram object (need access to probabilities)
	CharNGramConstruct model;

	/**
	 * constructor
	 * @param errRate	the rate at which errors should occur
	 */
	public SpellCheck(double errRate) {
		// initialize list
		chars = new ArrayList<Character>();
		errChars = new ArrayList<Character>();
		// initialize errRate
		this.errRate = errRate;

	}

	/**
	 * method to read in a file
	 * @param filename		file to read in
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
				chars.add(c);
			}
		}

		input.close();
	}


	// create the CharNGram object
	public void buildModel() {
		this.model = new CharNGramConstruct(chars);
	}


	/**
	 * method that introduces errors to the text
	 */
	public void inputErrors() {
		// loop through the list of chars
		for (int i=0; i<chars.size(); i++) {
			// get random double between 0 and 1
			double err = random.nextDouble();
			// if err is less than errRate, we introduce error
			// but only if character is not a whitespace
			if (err<errRate&&chars.get(i)!=' ') {
				// get a random next character
				char rand = alphabet[random.nextInt(alphabet.length-1)];
				// put this random char as the new one
				errChars.add(rand);
			}
			// otherwise, if no error, we add the original character
			else {
				errChars.add(chars.get(i));
			}
		}
		//System.out.println(chars);
	}


	//	/**
	//	 * find the actual error rate by comparing the
	//	 * original list of characters and list of characters
	//	 * that have errors
	//	 * @return rate		the error rate
	//	 */
	//	public double findErrRate() {
	//		// int to keep track of number of differences
	//		int diffs = 0;
	//		// loop through the original (correct) characters
	//		for (int i=0; i<chars.size(); i++) {
	//			if (chars.get(i)!=errChars.get(i)) {
	//				diffs++;
	//			}
	//		}
	//		// get actual error rate
	//		// diffs/total size
	//		double rate = (double)diffs/chars.size();
	//		return rate;
	//	}



	public void viterbi() {
		// map to keep prior probabilities
		HashMap<Character, Double> priors = new HashMap<Character, Double>();
		// initialize with all zeroes
		for (char character:alphabet) {
			priors.put(character, 0.0);
		}
		// map to store current probabilities
		HashMap<Character, Double> probs = null;
		// list of maps to store backpointers
		// the first char in the map is the current character,
		// and the second char is where it came from
		ArrayList<HashMap<Character, Character>> backpointers = new ArrayList<HashMap<Character, Character>>();

		// go through the observations (text with errors)
		// we start at third char; assume first two are correct
		for (int i=2; i<errChars.size(); i++) {

			probs = new HashMap<Character, Double>();
			// create a backpointer (single backpointer)
			HashMap<Character, Character> backpointer = new HashMap<Character, Character>();
			
			// go through all characters in the prior probabilities
			for (Character prior:priors.keySet()) {
				// get transitions and updates
				// go through entire alphabet
				for (char c:alphabet) {
					// create a new trigram
					char[] trigram = {errChars.get(i-2), errChars.get(i-1), c};
					// NOTE: all probability-related numbers are log probabilities
					// get the prediction (transition step)
					double transition = priors.get(prior) + Math.log(this.model.findProb(trigram));
					// update step
					double update;
					// if chars match large likelihood it's correct
					if (c==errChars.get(i)) {
						update = 1 - this.errRate;
					}
					else {
						update = this.errRate;
					}
					// get final probability
					double prob = transition + Math.log(update);

					// if character is not yet in probabilities or we get a higher one
					if (!probs.containsKey(c)||prob > probs.get(c)) {
						// put into probability map
						probs.put(c, prob);
						// add backpointer
						backpointer.put(c, prior);
					}
				}
			}
			// update prior probabilities
			priors = probs;
			// add to overall backpointers
			backpointers.add(backpointer);

		}
		
		// get most optimal characters at last step
		// start with a highest probability
		double highest = Double.MAX_VALUE * -1;
		Character optimal = null;
		// go through all characters in probability 
		for (Character c:probs.keySet()) {
			//System.out.println(c + " --> " + probs.get(c));
			// if higher prob, replace
			if (probs.get(c)>highest) {
				highest = probs.get(c);
				optimal = c;
			}
		}
		//System.out.println("optimal is " + optimal);

		// create path to store most likely sequence
		ArrayList<Character> path = new ArrayList<Character>();
		// add char with highest probability at end of sequence
		path.add(optimal);
		
		// go through backpointers and add to path
		for (int i = backpointers.size()-1; i>0; i--) {
			Character prev = backpointers.get(i).get(optimal);
			path.add(prev);
			optimal=prev;
		}
		
		// add first two chars 
		path.add(errChars.get(1));
		path.add(errChars.get(0));
		// reverse
		Collections.reverse(path);

		for (Character c : path) {
			System.out.print(c);
		}

		System.out.println();
		// find error
		int diffs = 0;
		// loop through the original (correct) characters
		for (int i=0; i<chars.size(); i++) {
			if (chars.get(i)!=path.get(i)) {
				diffs++;
			}
		}
		// get actual error rate
		// diffs/total size
		double rate = (double)diffs/chars.size();
		System.out.println(rate);
	}



	// getters
	public ArrayList<Character> getChars() {
		return chars;
	}

	public ArrayList<Character> getErrChars() {
		return errChars;
	}

	public CharNGramConstruct getModel() {
		return model;
	}


}
