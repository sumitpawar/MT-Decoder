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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

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
	// max sentences to consider before pruning
	private final int s = 10000;
	
	// Input String list
	private List<String> input = new ArrayList<String>();
	// translation model map
	private Map<String, List<TranslationWordProbability>> tmMap = new HashMap<String, List<TranslationWordProbability>>();
	// language model map
	private Map<String, LanguageWordProbability> lmMap = new HashMap<String, LanguageWordProbability>();
	// list of translated sentences
	private List<TranslatedSentences> translatedSentences = new ArrayList<TranslatedSentences>();
	
	// Stack: priority queue
	private PriorityQueue<TranslatedSentences> stack = 
			new PriorityQueue<TranslatedSentences>(s, new Comparator<TranslatedSentences>() {
				@Override
				public int compare(TranslatedSentences ts1,
						TranslatedSentences ts2) {
					Double ls1prob = ts1.getLmprob() + ts1.getTmprob();
					Double ls2prob = ts2.getLmprob() + ts2.getTmprob();
					return (ls1prob.compareTo(ls2prob) * (-1));
				}
			});
	
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
	public Map<String, List<TranslationWordProbability>> getTmMap() {
		return tmMap;
	}

	/**
	 * @param tmMap the tmMap to set
	 */
	public void setTmMap(Map<String, List<TranslationWordProbability>> tmMap) {
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
				if(line.startsWith("\\") 
						|| line.startsWith("ngram") || line.trim().isEmpty()) {
					continue;
				}
				String[] lmLine = line.trim().split("\\s+");
				LanguageWordProbability lmwp = new LanguageWordProbability();
				String key = lmLine[1];
				lmwp.setWordProbability(Double.parseDouble(lmLine[0]));
				if(lmLine.length >= 3) {
					int j = lmLine.length - 1;
					if(lmLine[j].matches("\\-\\d+\\.\\d+")) {
						lmwp.setBackoffProbability(Double.parseDouble(lmLine[j]));
						j--;
					}
					for(int i = 2; i <= j; i++) {
						key += " " + lmLine[i];
					}
				} else {
					lmwp.setBackoffProbability(0.0); // for trigram models
				}
				lmwp.setWord(key);
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
		tmMap = new HashMap<String, List<TranslationWordProbability>>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(tmFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] tmLine = line.trim().split("\\s+\\|\\|\\|\\s+");
				TranslationWordProbability tmwp = new TranslationWordProbability();
				String key = tmLine[0];
				//tmwp.setForeignWord(key);
				tmwp.setTargetWord(tmLine[1]);
				tmwp.setTranslationProbability(Double.parseDouble(tmLine[2]));
				List<TranslationWordProbability> list = new ArrayList<TranslationWordProbability>();
				if(tmMap.containsKey(key)) {
					list = tmMap.get(key);
				}
				list.add(tmwp);
				tmMap.put(key, list);
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
	
	private double logadd10(double x, double y) {
		double logsum = 0.0;
		if(x >= y) {
			logsum = x + Math.log10(1 + Math.exp((y-x)));
		} else {
			logsum = y + Math.log10(1 + Math.exp((x-y)));
		}
		return logsum;
	}
	
	private void decode() {
		for(int i = 0 ; i < input.size(); i++) { 										// for each sentence
			String[] sen = input.get(i).split("\\s+");
			Map<Integer, List<TranslatedSentences>> senMap = 									// hold map of possible English translations
										new HashMap<Integer, List<TranslatedSentences>>();		// for each French word
			for(int j = 0; j < sen.length; j++) {
				List<TranslatedSentences> tsList = new ArrayList<TranslatedSentences>();
				String word = sen[j];
				if(tmMap.containsKey(word)) {
					List<TranslationWordProbability> trList = tmMap.get(word);
					for(int k = 0; k < trList.size(); k++) {
						TranslationWordProbability twp = trList.get(k);
						String tw = twp.getTargetWord();
						TranslatedSentences ts = new TranslatedSentences();
						ts.setS(j);
						ts.setT(j+1);
						ts.setTranslatedSentence(tw);
						ts.setTmprob(twp.getTranslationProbability());
						int[] b = new int[sen.length];
						b[j] = 1;
						ts.setB(b);
						if(lmMap.containsKey(tw)) {
							ts.setLmprob(lmMap.get(tw).getWordProbability());
						}
						tsList.add(ts);
						
						String firstphr = "<s> " + tw;
						if(lmMap.containsKey(firstphr)) {
							LanguageWordProbability lwp = lmMap.get(firstphr);
							TranslatedSentences ts2 = new TranslatedSentences();
							ts2.setS(j);
							ts2.setT(j+1);
							ts2.setLmprob(lwp.getWordProbability() 
									+ lmMap.get(tw).getWordProbability() 
									+ lmMap.get(tw).getBackoffProbability());
							ts2.setTmprob(twp.getTranslationProbability());
							ts2.setTranslatedSentence(firstphr);
							ts2.setB(b);
							stack.add(ts2);
						}
					}
				}else {
					TranslatedSentences ts = new TranslatedSentences();
					ts.setS(j);
					ts.setT(j+1);
					ts.setTmprob(0.0);
					ts.setTranslatedSentence(word);
					if(lmMap.containsKey(word)) {
						ts.setLmprob(lmMap.get(word).getWordProbability());
					} else {
						ts.setLmprob(0.0);
					}
					int[] b = new int[sen.length];
					b[j] = 1;
					ts.setB(b);
					tsList.add(ts);
					
					String firstphr = "<s> " + word;
					if(lmMap.containsKey(firstphr)) {
						LanguageWordProbability lwp = lmMap.get(firstphr);
						TranslatedSentences ts2 = new TranslatedSentences();
						ts2.setS(j);
						ts2.setT(j+1);
						ts2.setLmprob(lwp.getWordProbability());
						ts2.setTmprob(0.0);
						ts2.setTranslatedSentence(firstphr);
						ts2.setB(b);
						stack.add(ts2);
					}else {
						TranslatedSentences ts2 = new TranslatedSentences();
						ts2.setS(j);
						ts2.setT(j+1);
						ts2.setLmprob(0.0);
						ts2.setTmprob(0.0);
						ts2.setTranslatedSentence(firstphr);
						ts2.setB(b);
						stack.add(ts2);
					}
				}
				senMap.put((j+1), tsList);
			}
			
			// run until we get the best complete translation
			for(int k = 1; k < sen.length; k++) {
				int limit = s;
				if(stack.size() < s) {
					limit = stack.size();
				}
				List<TranslatedSentences> trsentences = new ArrayList<TranslatedSentences>();
				for(int j =0; j < limit ; j++) {
					trsentences.add(stack.poll());
				}
				stack.clear();
				for(int j = 0; j < limit ; j++) {
					TranslatedSentences ts = trsentences.get(j);
					for(int l = 0; l < sen.length; l++) {
						if (ts.getB()[l] == 0) {
							for(TranslatedSentences t : senMap.get(l+1)) {
								String snt = ts.getTranslatedSentence();
								snt += " " + t.getTranslatedSentence();
								String[] sntnce = snt.split("\\s+");
								double lmp = ts.getLmprob();
								if(sntnce.length == 2) {
									if(lmMap.containsKey("<s> " + snt)) {
										String t1 = "<s> " + snt;
										lmp += 	lmMap.get(t1).getBackoffProbability() 
												+ lmMap.get(t1).getWordProbability();
									}
									if(lmMap.containsKey(snt)) {
										lmp += 	lmMap.get(snt).getBackoffProbability() 
												+ lmMap.get(snt).getWordProbability();
									}
									lmp += lmMap.get(t.getTranslatedSentence()).getWordProbability()
											+ lmMap.get(t.getTranslatedSentence()).getBackoffProbability();
								}else if(sntnce.length > 2) {
									String t2 = sntnce[sntnce.length - 2] + " " 
											+ sntnce[sntnce.length - 1];
									String t1 = sntnce[sntnce.length - 3] + " " + t2;
									if(lmMap.containsKey(t1)) {
										lmp += 	lmMap.get(t1).getBackoffProbability() 
												+ lmMap.get(t1).getWordProbability();
									}
									if(lmMap.containsKey(t2)) {
										lmp += 	lmMap.get(t2).getBackoffProbability() 
												+ lmMap.get(t2).getWordProbability();
									}
									if(lmMap.containsKey(t.getTranslatedSentence())) {
										lmp += lmMap.get(t.getTranslatedSentence()).getWordProbability()
											+ lmMap.get(t.getTranslatedSentence()).getBackoffProbability();
									}
								}else {
									System.err.println("Error!!! : invalid sentence: " + sntnce);
								}
								TranslatedSentences ts2 = new TranslatedSentences();
								ts2.setLmprob(lmp);
								ts2.setTmprob(ts.getTmprob() + t.getTmprob());
								int[] b1 = new int[sen.length];
								for(int m =0 ; m< sen.length; m++) {
									b1[m] = ts.getB()[m];
								}
								b1[l] = 1;
								ts2.setB(b1);
								snt = snt.substring(snt.indexOf(' '), snt.length()); // remove the BOS: <s>
								ts2.setTranslatedSentence(snt);
								stack.add(ts2);
							}
						}
					}
				}
			}
			
			// Consider last sequence w_{n-1} w_{n} </s>
			{
				int limit = s;
				if(stack.size() < s) {
					limit = stack.size();
				}
				List<TranslatedSentences> trsentences = new ArrayList<TranslatedSentences>();
				for(int j =0; j < limit ; j++) {
					trsentences.add(stack.poll());
				}
				stack.clear();
				for(int j = 0; j < limit ; j++) {
					TranslatedSentences ts = trsentences.get(j);
					String snt = ts.getTranslatedSentence();
					snt += " </s>";
					String[] sntnce = snt.split("\\s+");
					double lmp = ts.getLmprob();
					String t2 = sntnce[sntnce.length - 2] + " " 
							+ sntnce[sntnce.length - 1];
					String t1 = sntnce[sntnce.length - 3] + " " + t2;
					if(lmMap.containsKey(t1)) {
						lmp += 	lmMap.get(t1).getBackoffProbability() 
								+ lmMap.get(t1).getWordProbability();
					}
					if(lmMap.containsKey(t2)) {
						lmp += 	lmMap.get(t2).getBackoffProbability() 
								+ lmMap.get(t2).getWordProbability();
					}
					lmp += lmMap.get("</s>").getWordProbability();
					ts.setLmprob(lmp);
					stack.add(ts);
				}
			}
			TranslatedSentences ts = stack.poll();
			/*System.out.println("sentence: " + ts.getTranslatedSentence() 
					+ " #### tmprob: " + ts.getTmprob()
					+ " #### lmbrob: " + ts.getLmprob()
					+ " #### bitmap: " + ts.getB().toString());*/
			System.out.println(ts.getTranslatedSentence());
			stack.clear();
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
