package com.example.service;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.lucene.analysis.TokenStream;
import org.springframework.stereotype.Service;

import com.example.business.LuceneIndexerBusiness;
import com.example.constant.SearchEngineConstants;
import com.example.serviceinterface.DocumentServiceIntf;
import com.example.utility.SearchEngineUtility;

@Service
public class DocumentServiceImpl implements DocumentServiceIntf{
	/**
	 * Creates index for the documents contained in the corpus for effective
	 * search. Process the indexing for the documents for the given path(Path is
	 * hardcoded currently, will be changed to get from UI later on)
	 */
	@Override
	public String createIndex() {
		SearchEngineUtility searchEngineUtility = new SearchEngineUtility();
		
		//TODO Accept document path from user and remove the hard coded doc path
		final Path docDir = Paths.get(SearchEngineConstants.DOC_DIR);
		LuceneIndexerBusiness luceneIndexer = new LuceneIndexerBusiness();
		
		System.out.println("Currently parsing...");
		
		if (Files.isDirectory(docDir)) {
			try {
				Files.walkFileTree(docDir, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						boolean isTextHTMLDoc = searchEngineUtility.checkDocumentType(file.getFileName().toFile());
						if (isTextHTMLDoc) {
							TokenStream streamFromTokenizer;
							streamFromTokenizer = searchEngineUtility.processTokens(file);
							//Print tokens after stemming to test and compare with search keyword pre-processing
							searchEngineUtility.printStemmedTokens(streamFromTokenizer);
							luceneIndexer.indexDocsFromDirectory(streamFromTokenizer, file);
						}
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		return "Yo! Indexing is working cool with hardcoded list of documents!!";
	}
	/*
	 * TODO Service not yet implemented
	*/
	@Override
	public String searchDocument(String query) {
		return "No worries! search query functionality will be up soon!";		
	}
}
