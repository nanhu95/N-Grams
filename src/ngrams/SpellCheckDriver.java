package ngrams;

import java.io.IOException;


public class SpellCheckDriver {
	
	public static void main(String[] args) {
		
		SpellCheck test = new SpellCheck(0.05);
		try {
			test.readFile("test.txt");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		test.inputErrors();
		System.out.println(test.getChars());
		System.out.println(test.getErrChars());
		
		
		//construct char-level n-gram model
		test.buildModel();
		
		System.out.println(test.getModel().getCharBigram());
		System.out.println(test.getModel().getCharTrigram());
		// run viterbi
		test.viterbi();
	}
}
