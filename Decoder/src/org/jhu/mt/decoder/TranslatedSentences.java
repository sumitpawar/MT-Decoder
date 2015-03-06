/**
 * 
 */
package org.jhu.mt.decoder;

/**
 * @author sumit
 *
 */
public class TranslatedSentences {
	
	// best translated sentence - hypotheses
	private String translatedSentence;
	
	// language model probability
	private double lmprob;
	
	// translation model probability
	private double tmprob;
	
	// start of sequence words
	private int s;
	
	// end of sequence of words
	private int t;
	
	// bit set
	private int[] b;

	/**
	 * @return the translatedSentence
	 */
	public String getTranslatedSentence() {
		return translatedSentence;
	}

	/**
	 * @param translatedSentence the translatedSentence to set
	 */
	public void setTranslatedSentence(String translatedSentence) {
		this.translatedSentence = translatedSentence;
	}

	/**
	 * @return the lmprob
	 */
	public double getLmprob() {
		return lmprob;
	}

	/**
	 * @param lmprob the lmprob to set
	 */
	public void setLmprob(double lmprob) {
		this.lmprob = lmprob;
	}

	/**
	 * @return the tmprob
	 */
	public double getTmprob() {
		return tmprob;
	}

	/**
	 * @param tmprob the tmprob to set
	 */
	public void setTmprob(double tmprob) {
		this.tmprob = tmprob;
	}

	/**
	 * @return the s
	 */
	public int getS() {
		return s;
	}

	/**
	 * @param s the s to set
	 */
	public void setS(int s) {
		this.s = s;
	}

	/**
	 * @return the t
	 */
	public int getT() {
		return t;
	}

	/**
	 * @param t the t to set
	 */
	public void setT(int t) {
		this.t = t;
	}

	/**
	 * @return the b
	 */
	public int[] getB() {
		return b;
	}

	/**
	 * @param b the b to set
	 */
	public void setB(int[] b) {
		this.b = b;
	}
	
	
}
