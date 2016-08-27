package ngrams;

import java.util.ArrayList;


public class NGramConstructDriver {
	// driver code for NGramConstruct
	public static void main(String[] args) {
		// create instance of NGramConstruct
		NGramConstruct test = new NGramConstruct(true);
		// read in file
		test.readFile("LockeNoPunc.txt");
		test.readFile("MetamorphosisNoPunc.txt"); // can't use; processing destroyed the text
		// print the words array
		System.out.println(test.getWords());
		// create bigram and trigram frequencies
		test.createBigram();
		test.createTrigram();
		// print bigram frequencies
		for (NGram bigram:test.getBigramFreq().keySet()) {
			System.out.print(bigram);
			System.out.print("Freq: "+test.getBigramFreq().get(bigram)+"    ");
		}
		System.out.println(" ");
		// print trigram frequencies
		for (NGram trigram:test.getTrigramFreq().keySet()) {
			System.out.print(trigram);
			System.out.print("Freq: "+test.getTrigramFreq().get(trigram)+"    ");
		}
		System.out.println("");
		System.out.println(test.getSumBigram());
		System.out.println(test.getSumTrigram());
		
		// finding probabilities
		String[] input = {"on","the","floor"};
		double prob = test.findProb(input);
		System.out.println(prob);
		
		// produce most likely next word
		// create list of 10 bigrams to test on
		String[][] in1 = {{"on","the"},
							{"next","to"},
							{"in", "the"},
							{"this","is"},
							{"of","his"},
							{"of","the"},
							{"it","was"},
							{"because","of"},
							{"such","a"},
							{"if","such"}};
		// construct bigrams from these
		ArrayList<NGram> bigramList = new ArrayList<NGram>();
		for (int i=0; i<in1.length; i++) {
			NGram bigram = new NGram(in1[i]);
			bigramList.add(bigram);
		}
		// get words
		for (NGram in:bigramList) {
			NGram out = test.nextWord(in);
			System.out.println(out);
		}
		
		
		String[] asdf = {"next","to"};
		NGram bigram = new NGram(asdf);
		// use the probability distribution to find next word
		System.out.println(test.predict(bigram));

		// start with two words, and predict more
		ArrayList<String> start = new ArrayList<String>();
		start.add("there");
		start.add("is");
		// get up to 50 words
		for (int i=0; i<50; i++) {
			String[] next = new String[2];
			next[0] = start.get(i);
			next[1] = start.get(i+1);
			NGram trigram = test.predict(new NGram(next));

			start.add(trigram.getNgram()[2]);
		}

		// print the words
		for (String word : start) {
			if (word.length() == 0) System.out.print(".");
			else System.out.print(" " + word);
		}
//
//		System.out.println(seed.get(26));
		
	}
}
