/**
 * 
 */
package org.jhu.mt.decoder;

/**
 * @author sumit
 *
 */
public class LanguageWordProbability {
	
	// the word (english or target language)
	private String word;
	
	// the word log - probability
	private double wordProbability;
	
	// the back-off log - probability
	private double backoffProbability;

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return the wordProbability
	 */
	public double getWordProbability() {
		return wordProbability;
	}

	/**
	 * @param wordProbability the wordProbability to set
	 */
	public void setWordProbability(double wordProbability) {
		this.wordProbability = wordProbability;
	}

	/**
	 * @return the backoffProbability
	 */
	public double getBackoffProbability() {
		return backoffProbability;
	}

	/**
	 * @param backoffProbability the backoffProbability to set
	 */
	public void setBackoffProbability(double backoffProbability) {
		this.backoffProbability = backoffProbability;
	}
	
	
}
