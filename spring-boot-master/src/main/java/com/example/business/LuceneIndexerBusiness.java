package com.example.business;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;

import com.example.constant.SearchEngineConstants;
import com.example.utility.SearchEngineUtility;

public class LuceneIndexerBusiness {

	SearchEngineUtility searchEngineUtility = new SearchEngineUtility();

	/**
	 * TODO Change hard coded index directory path and accept it from UI. The
	 * method writes the created index into a file and stores it locally. Helps
	 * in searching the document efficiently
	 * @param stream
	 * @param path
	 */
	public void indexDocsFromDirectory(TokenStream stream, Path path) {
		// standard analyzer uses default stop words
		Analyzer analyzer = new StandardAnalyzer();
		
		// IndexWriter Configuration
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
		
		Document document = new Document();
		
		try {
			// org.apache.lucene.store.Directory instance
			Directory dir = FSDirectory.open(Paths.get(SearchEngineConstants.INDEX_DIR));
			
			// IndexWriter writes new index files to the directory
			IndexWriter writer = new IndexWriter(dir,iwc);
			String content = "";
			
			FileTime time = Files.getLastModifiedTime(path);
			
			while (stream.incrementToken()) {
				content = content + " " + (stream.getAttribute(CharTermAttribute.class).toString());
			}
			
			
			String text = new String(Files.readAllBytes(path));
			String title = "";
			
			//Use Json library for parsing HTML files
			if (searchEngineUtility.isHTMLDocument(path.toFile())) {
				org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(text);
				title = jsoupDoc.title().toString() + "\n";
				document.add(new StringField("HTMLtitle", title, Store.YES));
			}
			/*
			 * Fetching only required fields depending on business, here only
			 * filename, path, last modified date and contents
			 */
			document.add(new StringField("name", path.getFileName().toString(), Store.YES));
			document.add(new StringField("path", path.toString(), Store.YES));
			document.add(new StringField("modifiedDate", time.toString(), Store.YES));
			document.add(new TextField("contents", content, Store.YES));

			/*
			 * Updates the existing index by deleting everything and then
			 * writing indexes into new document
			 */
			writer.updateDocument(new Term("path", path.toString()), document);
			// List all the parsed files:
			System.out.println(document.get("name"));

			writer.close();
		} catch (IOException e) {
			System.out.println("Exception thrown inside indexDocsFromDirectory() method: " + e);
			e.printStackTrace();
		}
	}
}
