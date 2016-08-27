package ngrams;

import java.io.IOException;
import java.util.ArrayList;




public class CharNGramConstructDriver {

	public static void main(String[] args) {
		CharNGramConstruct test = new CharNGramConstruct();
		try {
			test.readFile("LockeNoPunc.txt");
			test.readFile("MetamorphosisNoPunc.txt");
			//test.readFile(" GulliverNoPunc.txt"); 	 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(test.getChars());

		test.createCharBigram();
		test.createCharTrigram();

		System.out.println(test.getCharBigramFreq());
		System.out.println(test.getCharTrigramFreq());

		System.out.println(test.getSumCharBigram());
		System.out.println(test.getSumCharTrigram());

		System.out.println(test.getCharBigram());
		System.out.println(test.getCharTrigram());

		char[][] in1 = {{'a','r'},
				{'h','i'},
				{'m','o'},
				{'f','o'},
				{'n','o'},
				{'b','r'},
				{'t','a'},
				{'n','e'},
				{'w','h'},
				{'w','e'}};
		for (int i=0; i<in1.length; i++) {
			System.out.println(test.nextChar(new CharNGram(in1[i])));
		}

//		for (int i=0; i<in1.length; i++) {
//			System.out.println(test.predict(new CharNGram(in1[i])));
//		}
		
//		// start with two chars, and predict more
//		ArrayList<Character> start = new ArrayList<Character>();
//		start.add('a');
//		start.add('t');
//		// get next 50 chars
//		for (int i=0; i<50; i++) {
//			char[] next = new char[2];
//			next[0] = start.get(i);
//			next[1] = start.get(i+1);
//			CharNGram trigram = test.predict(new CharNGram(next));
//
//			start.add(trigram.getCharNgram()[2]);
//		}
//		// print out the chars
//		for (char c:start) {
//			System.out.print(c);
//		}

	
	//System.out.println(test.nextChar(new CharNGram(in)));
	}
}
