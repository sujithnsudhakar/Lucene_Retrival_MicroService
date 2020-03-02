package com.example.utility;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.jsoup.Jsoup;

public class SearchEngineUtility {
	/**
	 * For all pre-processing tasks - remove stopwords, stemming. Parse HTML
	 * documents using Jsoup
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public TokenStream processTokens(Path path) throws IOException {
		Analyzer analyzer = new StandardAnalyzer();
		
		String text = new String(Files.readAllBytes(path));
		String title = "";
		if (isHTMLDocument(path.toFile())) {
			org.jsoup.nodes.Document doc = Jsoup.parse(text);
			title = doc.title().toString() + "\n";
			text = doc.body().text();
			text = title + text;
		}
		//To fetch default stop words from Lucene English Analyzer
		CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();
		
		/*fieldName - the name of the field the created TokenStream is used
		text - the String the streams source reads from*/
		TokenStream stream = analyzer.tokenStream("fieldName", new StringReader(text));
		
		stream = new StopFilter(stream, stopWords);
		
		//Stem words using porter stemmer
		stream = new PorterStemFilter(stream);
		stream.reset();
		return stream;	
	}
	/**
	 * 
	 * @param file
	 * @return
	 * utility method to check if the document is an html document
	 */
	public boolean isHTMLDocument(File file) {
		if (file.isHidden() || file.isDirectory()) {
			return false;
		}
		if (file.getName().endsWith("html") || file.getName().endsWith("htm")) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 
	 * @param file
	 * @return
	 * utility method to check if the document is an text or html document
	 */
	public boolean checkDocumentType(File file) {
		if (file.isHidden() || file.isDirectory()) {
			return false;
		}
		if (!file.getName().endsWith("txt") && !file.getName().endsWith("html") && !file.getName().endsWith("htm")) {
			return false;
		} else {
			return true;
		}
	}
	public void printStemmedTokens(TokenStream streamFromTokenizer) {
		CharTermAttribute cattr = streamFromTokenizer.addAttribute(CharTermAttribute.class);
		try {
			while (streamFromTokenizer.incrementToken()) {
				System.out.println(cattr.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
