package ngrams;

/**
 * basically a wrapper class around a char array
 * @author nanhu
 *
 */
public class CharNGram {
	// the char array
	private char[] Ngram;
	
	// constructor
	public CharNGram(char[] Ngram) {
		this.Ngram = Ngram;
	}
	
	// getter
	public char[] getCharNgram() {
		return Ngram;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        CharNGram other = (CharNGram) obj;
        // loop through char array and check that all are the same
        char[] otherCharNgram = other.getCharNgram();
        for (int i=0; i<this.Ngram.length; i++) {
        	if (this.Ngram[i]!=otherCharNgram[i]) {
        		return false;
        	}
        }
        return true;
        
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		double i=0.0;
		for (Character character:this.Ngram) {
			hash+=Math.pow(7.0, i)*character.hashCode();
			i++;
		}
		return hash;
	}
	
	@Override
	public String toString() {
		String toReturn = "";
		toReturn+="[";
		for (int i=0; i<this.Ngram.length; i++) {
			toReturn = toReturn + this.Ngram[i] + " ";
		}
		toReturn+="]";
		return toReturn;
	}
}
