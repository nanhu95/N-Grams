package ngrams;


public class NGram {
	// string array to store words
	private String[] Ngram;
	
	// constructor
	public NGram(String[] Ngram) {
		this.Ngram = Ngram;
	}
	
	// getter
	public String[] getNgram() {
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
        NGram other = (NGram) obj;
        // loop through the string array and check that all values are same
        String[] otherNgram = other.getNgram();
        for (int i=0; i<this.Ngram.length; i++) {
        	if (!this.Ngram[i].equals(otherNgram[i])) {
        		return false;
        	}
        }
        return true;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		for (String word:this.Ngram) {
			hash+=word.hashCode();
		}
		return hash;
	}
	
	@Override
	public String toString() {
		String out = "";
		out+="[";
		for (String word:this.Ngram) {
			out=out+word+" ";
		}
		out+="]";
		return out;
	}
}
