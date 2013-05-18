/* @author Xu Yang;
   @time 2013-5-17
 */
package com.yangxu.searchengine.service.impl;

import java.io.File;

import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;

import org.apache.lucene.index.IndexReader;

import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import com.yangxu.searchengine.bo.SearchRequest;
import com.yangxu.searchengine.bo.SearchResult;
import com.yangxu.searchengine.bo.SearchResults;
import com.yangxu.searchengine.config.PropertyConfiguration;
import com.yangxu.searchengine.service.SearchService;

public class SearchServiceImpl implements SearchService {
	private static final String indexPath = PropertyConfiguration
			.getCsdnIndexDir();

	/**
	 * ִ�м�������Ҫ�ӿ�
	 */
	public SearchResults getSearchResults(SearchRequest request, String field) {

		SearchResults searchResults = new SearchResults();

		// ��request��ȡһ���ؼ��֣�����һ��query
		String queryString = request.getQuery().trim();
		//System.out.println("queryString = " + queryString);
		if (queryString.equals("")) {
			//System.out.println("queryString is null");
			return searchResults;
		}
		ArrayList<SearchResult> list = new ArrayList<SearchResult>();

		int hitsPerPage = 10;
		try {
			IndexReader reader = IndexReader.open(FSDirectory.open(new File(
					indexPath)));
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_31);

			QueryParser parser = new QueryParser(Version.LUCENE_31, field,
					analyzer);
			Query query = parser.parse(queryString);

			TopDocs doc_results = searcher.search(query, 100 * hitsPerPage);
			ScoreDoc[] hits = doc_results.scoreDocs;

			// ȡ�ý���ĳ���
			int length = doc_results.totalHits;
			// ��SearchRequest��ȡ������Ҫ��¼����ʼλ��
			int startindex = request.getStartindex();
			// ��ʾ��ǰҳ�����һ����¼
			int endindex;

			if (startindex > length) {

			} else {
				endindex = startindex + 9;
				if (endindex >= length) {
					endindex = length;
				}
				for (int i = startindex; i <= endindex; i++) {
					Document doc = searcher.doc(hits[i - 1].doc);

					SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>","</font>");
					Highlighter highlighter = new Highlighter(simpleHTMLFormatter,new QueryScorer(query));
					
					SearchResult result = new SearchResult();
					String title = doc.get("title");
					if(!title.contains(queryString)){
						result.setTitle(title);
					}else{
						result.setTitle(highlighter.getBestFragment(analyzer,queryString,doc.get("title")));
					}
					
					result.setUrl(doc.get("url"));
					result.setIndexCreateTime(doc.get("indextime"));
					result.setUpLoadTime(doc.get("uploadtime"));
									
					String abstractContent = highlighter.getBestFragment(analyzer,queryString,doc.get("content"));
					result.setAbstractContent(abstractContent);
					
					list.add(result);
				}
			}
			searchResults.setResults(list);

			// ��ʼ���㵱ǰҳ����Ҫ��ʾ�ķֲ���ǩ�е���ʼҳ����ֹҳ
			int startpage;
			int endpage;

			// ������ʼҳ
			if (startindex % 100 == 0) {
				startpage = (startindex / 100 - 1) * 10 + 1;
			} else {
				startpage = (startindex / 100) * 10 + 1;
			}

			int span;
			int hasnext;

			float temp = ((float) (length - (startpage - 1) * 10)) / 10;
			if (temp > 10) {
				span = 9;
				hasnext = 1;
			} else if (temp == 10) {
				span = 9;
				hasnext = 0;
			} else {
				hasnext = 0;
				if ((int) temp < temp) {
					span = (int) temp;
				} else {
					span = (int) temp - 1;
				}
			}

			// ������ֹҳ
			endpage = startpage + span;

			// �������ֱ���SearchResults��
			searchResults.setMinpage(startpage);
			searchResults.setMaxpage(endpage);
			searchResults.setHasnext(hasnext);
			searchResults.setStartindex(startindex);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return searchResults;
	}

	public static void main(String[] args) {
		SearchRequest request = new SearchRequest();
		request.setQuery("java");
		request.setStartindex(1);
		SearchServiceImpl service = new SearchServiceImpl();
		SearchResults searchResults = service
				.getSearchResults(request, "title");
		ArrayList<SearchResult> results = searchResults.getResults();
		
		
		for (SearchResult ret : results) {
			System.out.println(ret.toString());
		}
		System.out.println(results.size());

	}
}
