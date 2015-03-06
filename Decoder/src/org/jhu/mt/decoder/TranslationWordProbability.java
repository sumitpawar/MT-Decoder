/**
 * 
 */
package org.jhu.mt.decoder;

/**
 * @author sumit
 *
 */
public class TranslationWordProbability {

	// Foreign language (french) word
	//private String foreignWord;
	
	// Target language (English) word
	private String targetWord;
	
	// Translation/Alignment log - probability
	private double translationProbability;

	/**
	 * @return the foreignWord
	 */
	/*public String getForeignWord() {
		return foreignWord;
	}*/

	/**
	 * @param foreignWord the foreignWord to set
	 */
	/*public void setForeignWord(String foreignWord) {
		this.foreignWord = foreignWord;
	}*/

	/**
	 * @return the targetWord
	 */
	public String getTargetWord() {
		return targetWord;
	}

	/**
	 * @param targetWord the targetWord to set
	 */
	public void setTargetWord(String targetWord) {
		this.targetWord = targetWord;
	}

	/**
	 * @return the translationProbability
	 */
	public double getTranslationProbability() {
		return translationProbability;
	}

	/**
	 * @param translationProbability the translationProbability to set
	 */
	public void setTranslationProbability(double translationProbability) {
		this.translationProbability = translationProbability;
	}
}
