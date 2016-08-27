package ngrams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class NGramConstruct {
	// map to store file - words in file pairs
	private HashMap<String, ArrayList<String>> words;
	// map to store bigram - freq pairs
	private HashMap<NGram, Integer> bigramFreq;
	// map to store trigram - feq pairs
	private HashMap<NGram, Integer> trigramFreq;

	// sum of freq of bigrams
	private int sumBigram;
	// sum of feq of trigrams
	private int sumTrigram;


	// option to transform to all lower
	private boolean toLower;
	
	// random number generator
	private Random random = new Random(0);


	/**
	 * constructor
	 * @param toLower	boolean indicating whether to transform to all lowercase
	 */
	public NGramConstruct(boolean toLower) {
		// initialize data structures
		words = new HashMap<String, ArrayList<String>>();
		bigramFreq = new HashMap<NGram, Integer>();
		trigramFreq = new HashMap<NGram, Integer>();

		// initialize boolean
		this.toLower = toLower;

		// initialize sumes
		sumBigram = 0;
		sumTrigram = 0;
	}

	/**
	 * method to "train" 
	 * reads in words from files
	 * @param filename		name of file to read in
	 */
	public void readFile(String filename) {
		// create list to store words
		ArrayList<String> wordsInFile = new ArrayList<String>();
		Scanner input=null;
		// make scanner to read in file
		try {
			input = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// read in line by line
		String line;
		while(input.hasNextLine()) {
			line = input.nextLine();
			// we don't want to count in new lines or blank lines
			if (line.trim().equals("\n")||line.trim().equals("")||line.isEmpty()) {
				continue;
			}
			if (toLower==true) {
				line = line.toLowerCase();
			}
			// split by whitespace
			String[] tokens = line.trim().split(" ");
			// add words individually to words list
			for (int i=0; i<tokens.length; i++) {
				if (tokens[i].equals(" ")||tokens[i].equals("\n")) {
					continue;
				}
				wordsInFile.add(tokens[i]);
			}
		}
		// close input
		input.close();
		// add to word map
		words.put(filename, wordsInFile);
	}


	/**
	 * method to create bigram frequencies
	 */
	public void createBigram() {
		// go through all files
		for (String file:words.keySet()) {
			ArrayList<String> wordsInFile = words.get(file);
			// go through all words in file, getting two at a time
			for (int i=0; i<wordsInFile.size()-1; i++) {
				// get two words
				String word1 = wordsInFile.get(i);
				String word2 = wordsInFile.get(i+1);
				// put them in array
				String[] in = {word1, word2};
				NGram bigram = new NGram(in);
				// if array not yet in bigram map, add it
				if (!bigramFreq.containsKey(bigram)) {
					bigramFreq.put(bigram, 1);
				}
				// if array already present, add to frequency
				else {
					//System.out.println("Got a duplicate!");
					Integer freq = bigramFreq.get(bigram);
					bigramFreq.put(bigram, freq+1);
				}
				sumBigram++;
			}
		}
	}

	/**
	 * method to create trigram frequencies
	 */
	public void createTrigram() {
		// go through all files
		for (String file:words.keySet()) {
			ArrayList<String> wordsInFile = words.get(file);
			// go through all words in file, getting two at a time
			for (int i=0; i<wordsInFile.size()-2; i++) {
				// get three words
				String word1 = wordsInFile.get(i);
				String word2 = wordsInFile.get(i+1);
				String word3 = wordsInFile.get(i+2);
				// put them in array
				String[] in = {word1, word2, word3};
				NGram trigram = new NGram(in);
				// if array not yet in trigram map, add it
				if (!trigramFreq.containsKey(trigram)) {
					trigramFreq.put(trigram, 1);
				}
				// if array already present, add to frequency
				else {
					//System.out.println("Got a duplicate!");
					Integer freq = trigramFreq.get(trigram);
					trigramFreq.put(trigram, freq+1);
				}
				sumTrigram++;
			}
		}
	}

	/**
	 * method to find probability of a three word combination
	 * @param input		the three words as an array
	 * @return prob		the probability of it occurring
	 */
	public double findProb(String[] input) {
		// we make assumption that we get an array length 3
		// get the bigram from the string
		double probInput = 0;
		double probBigram = 0;
		String[] in = {input[0],input[1]};
		NGram bigram = new NGram(in);
		NGram trigram = new NGram(input);
		//System.out.println(bigram);
		// get the string a
		//String a = input[2];
		// get probability of bigram
		if (!bigramFreq.containsKey(bigram)) {
			System.out.println("Bigram not found!");
			return 0.0;
		}
		else {
			probBigram = (double)bigramFreq.get(bigram)/sumBigram;
		}
		// get probability of trigram
		if (!trigramFreq.containsKey(trigram)) {
			System.out.println("Trigram not found!");
			return 0.0;
		}
		else {
			probInput = (double)trigramFreq.get(trigram)/sumTrigram;
		}
		// return overall probability
		return probInput/probBigram;

	}

	/**
	 * method to find the most probable next word given two words
	 * @param bigram	the two words, wrapped in a NGram
	 * @return result	a trigram with the most likely next word
	 */
	public NGram nextWord(NGram bigram) {
		// if bigram doesn't exist, return null 
		// since we can't get a next word anyway
		if (!bigramFreq.containsKey(bigram)) {
			System.out.println("Given bigram does not exist!");
			return null;
		}
		// int to store frequency
		int freq = 0;
		// NGram to store what should be returned
		NGram result = null;
		// go through all trigrams
		for(NGram trigram:trigramFreq.keySet()) {
			// if trigram contains the bigram
			if(bigram.getNgram()[0].equals(trigram.getNgram()[0])&&bigram.getNgram()[1].equals(trigram.getNgram()[1])) {
				// if trigram freq is greater than current freq, replace
				if (trigramFreq.get(trigram)>freq) {
					result = trigram;
					// update frequency
					freq = trigramFreq.get(result);
				}
			}
		}
		return result;
	}

//	/**
//	 * method to count n-grams
//	 * @param n		the n in n-grams
//	 * @return freq	number of n-grams
//	 */
//	public HashMap<NGram, Integer> countNGrams(int n) {
//
//		HashMap<NGram, Integer> freq = new HashMap<NGram, Integer>();
//
//		for (String doc : words.keySet()) {
//			ArrayList<String> w = words.get(doc);
//			// Can't process n-gram if n > words in the document
//			if (n > words.size()) continue;
//			for (int i = 0; i < w.size() - n; i++) {
//				String[] nGramWords = new String[n];
//				for (int j = 0; j < n; j++) {
//					nGramWords[j] = w.get(i + j);
//				}
//				NGram nGram = new NGram(nGramWords);
//				if (!freq.containsKey(nGram)) {
//					freq.put(nGram, 1);
//				} else {
//					Integer frequency = freq.get(nGram);
//					freq.put(nGram, frequency + 1);
//				}
//			}
//		}
//		return freq;
//	}


	/**
	 * method to return next word based on probability distribution
	 * @param bigram	the first two words
	 * @return trigram	the three words together
	 */
	public NGram predict(NGram bigram) {
		// if bigram doesn't exist, then we cannot proceed
		if (!bigramFreq.containsKey(bigram)) {
			System.out.println("No such bigram.");
			return null;
		}
		// create list of trigrams
		ArrayList<NGram> trigrams = new ArrayList<NGram>();
		// create map of probabilities
		HashMap<NGram, Double> probMap = new HashMap<NGram, Double>();
		
		// go through all trigrams
		for (NGram trigram:trigramFreq.keySet()) {
			// if bigram is contained, add to list
			if (bigram.getNgram()[0].equals(trigram.getNgram()[0])
					&& bigram.getNgram()[1].equals(trigram.getNgram()[1])) {
				trigrams.add(trigram);
			}
		}
		// go through list and add trigrams and probabilities to map
		for (NGram trigram:trigrams) {
			probMap.put(trigram, findProb(trigram.getNgram()));
		}
		// vector to store probabilities
		double[] pVector = new double[probMap.size()];
		// populate vector
		int i = 0;
		for (NGram trigram:trigrams) {
			pVector[i] = probMap.get(trigram);
			i++;
		}

		StdArrayIO.print(pVector);
		// normalize probabilities (just in case)
		double[] pVectorNormalized = Matrix.normalizeVector(pVector);
		// put into map normalized prob
		int j = 0;
		for (NGram trigram:trigrams) {
			probMap.put(trigram, pVectorNormalized[j]);
			j++;
		}

		StdArrayIO.print(pVectorNormalized);

		// get a random number between 0 and 1
		double rand = random.nextDouble();
		double totalProb = 0.0;
		for (NGram trigram:probMap.keySet()) {
			// get word based on probability distribution
			// essentially, we are going through all trigrams
			// and whichever has the probability that makes it
			// greater than the random num gets returned
			totalProb+=probMap.get(trigram);
			if (rand<totalProb) {
				return trigram;
			}
		}

		return null;
	}



	// getter methods
	public HashMap<String, ArrayList<String>> getWords() {
		return words;
	}

	public HashMap<NGram, Integer> getBigramFreq() {
		return bigramFreq;
	}

	public HashMap<NGram, Integer> getTrigramFreq() {
		return trigramFreq;
	}

	public int getSumBigram() {
		return sumBigram;
	}

	public int getSumTrigram() {
		return sumTrigram;
	}



}
