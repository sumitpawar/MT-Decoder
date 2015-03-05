/**
 * Assignment 2
 * Author: Sumit Pawar (spawar3@jhu.edu)
 * Instructor: Philipp Koehn
 */
package org.jhu.mt.decoder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sumit
 *
 */
public class StackDecoder {

	// Translation Model file 
	private final String tmFile = "./data/tm";
	// Language Model file
	private final String lmFile = "./data/lm";
	// Input file
	private final String inputFile = "./data/input";
	
	// Input String list
	private List<String> input = new ArrayList<String>();
	// translation model map
	private Map<String, TranslationWordProbability> tmMap = new HashMap<String, TranslationWordProbability>();
	// language model map
	private Map<String, LanguageWordProbability> lmMap = new HashMap<String, LanguageWordProbability>();
	// list of translated sentences
	private List<TranslatedSentences> translatedSentences = new ArrayList<TranslatedSentences>();
	
	
	/**
	 * @return the input
	 */
	public List<String> getInput() {
		return input;
	}

	/**
	 * @param input the input to set
	 */
	public void setInput(List<String> input) {
		this.input = input;
	}

	/**
	 * @return the tmMap
	 */
	public Map<String, TranslationWordProbability> getTmMap() {
		return tmMap;
	}

	/**
	 * @param tmMap the tmMap to set
	 */
	public void setTmMap(Map<String, TranslationWordProbability> tmMap) {
		this.tmMap = tmMap;
	}

	/**
	 * @return the lmMap
	 */
	public Map<String, LanguageWordProbability> getLmMap() {
		return lmMap;
	}

	/**
	 * @param lmMap the lmMap to set
	 */
	public void setLmMap(Map<String, LanguageWordProbability> lmMap) {
		this.lmMap = lmMap;
	}

	/**
	 * @return the translatedSentences
	 */
	public List<TranslatedSentences> getTranslatedSentences() {
		return translatedSentences;
	}

	/**
	 * @param translatedSentences the translatedSentences to set
	 */
	public void setTranslatedSentences(List<TranslatedSentences> translatedSentences) {
		this.translatedSentences = translatedSentences;
	}

	private void loadLanguageModel() {
		lmMap = new HashMap<String, LanguageWordProbability>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(lmFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] lmLine = line.trim().split("\\s+\\|\\|\\|\\s+");
				LanguageWordProbability lmwp = new LanguageWordProbability();
				String key = lmLine[1];
				lmwp.setWord(key);
				lmwp.setWordProbability(Double.parseDouble(lmLine[0]));
				if(lmLine.length == 3) {
					lmwp.setBackoffProbability(Double.parseDouble(lmLine[2]));
				} else {
					lmwp.setBackoffProbability(1.0); // for trigram models
				}
				lmMap.put(key, lmwp);
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: ");
			System.err.println(System.getProperty("user.dir"));
			e.printStackTrace();
		}catch (IOException e) {
			System.err.println("IOException: ");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadTranslationModel() {
		tmMap = new HashMap<String, TranslationWordProbability>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(tmFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tmLine = line.trim().split("\\s+\\|\\|\\|\\s+");
				TranslationWordProbability tmwp = new TranslationWordProbability();
				String key = tmLine[0];
				tmwp.setForeignWord(key);
				tmwp.setTargetWord(tmLine[1]);
				tmwp.setTranslationProbability(Double.parseDouble(tmLine[2]));
				tmMap.put(key, tmwp);
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: ");
			System.err.println(System.getProperty("user.dir"));
			e.printStackTrace();
		}catch (IOException e) {
			System.err.println("IOException: ");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadForeignSentences() {
		input = new ArrayList<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				input.add(line);
			}
		} catch (FileNotFoundException e) {
			System.err.println("FileNotFoundException: ");
			System.err.println(System.getProperty("user.dir"));
			e.printStackTrace();
		}catch (IOException e) {
			System.err.println("IOException: ");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void decode() {
		for(int i = 0 ; i < input.size(); i++) {
			String[] sen = input.get(i).split("\\s+");
			for(int j = 0 ; j < sen.length ; j++) {
				
			}
		}
	}
	
	private void decodeSentences() {
		loadLanguageModel();			// load language model parameters
		loadTranslationModel();			// load translational model parameters
		loadForeignSentences();			// load foreign sentences
		decode(); 						// decode foreign sentences to target sentences
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new StackDecoder().decodeSentences();
	}

}
