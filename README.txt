README:

There is a Perl script, puncstrip.pl, that is provided as part of the assignment whose job is to remove all punctuation. You do not need to run this script, but it is included as additional information.

The code for word-level n-grams is incuded in NGramConstruct.java. The testing code is in the corresponding driver. In the driver, the code above line 64 will print out the words, the bigrams and trigrams with their frequencies, and also the predictions from 10 bigrams. The code after line 64 will try to generate a sentence of length 50 (the length can be changed in line 74 in the for loop).

The code for character-level n-grams is included in CharNGramConstruct.java. The testing code is in the corresponding driver. In the driver, the code above line 49 will print out all the characters, the bigrams and trigrams and their frequencies, and also the predictions. The code after line 49 will try to generate a sentence of length 50 (the length can be changed in line 58 in the for loop).

The code for spell checking is in SpellCheck.java. The testing code is in the corresponding driver. In the driver, you can change which text to feed in, though note that shorter ones perform better and require much less runtime. 