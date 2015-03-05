/**
 * 
 */
package org.jhu.mt.decoder;

/**
 * @author sumit
 *
 */
public class TranslatedSentences {
	
	// best translated sentence
	private String translatedSentence;
	
	// final language model probability
	private double lmprob;
	
	// final translation model probability
	private double tmprob;

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
	
	
}
