package com.yangxu.searchengine.bo;

import java.util.ArrayList;

public class SearchResults {
	
	private ArrayList<SearchResult> results = new ArrayList<SearchResult>();
	// ��ǰҳ����ʼ��������
	private int startindex;
	// ��ǰҳ������Ҫ��ʾ����Сҳ
	private int minpage;
	// ��ǰҳ������Ҫ��ʾ�����ҳ
	private int maxpage;
	// ���б�maxpage�����ҳ����
	private int hasnext;

	public int getHasnext() {
		return hasnext;
	}

	public void setHasnext(int hasnext) {
		this.hasnext = hasnext;
	}

	public int getMaxpage() {
		return maxpage;
	}

	public void setMaxpage(int maxpage) {
		this.maxpage = maxpage;
	}

	public int getMinpage() {
		return minpage;
	}

	public void setMinpage(int minpage) {
		this.minpage = minpage;
	}

	public ArrayList<SearchResult> getResults() {
		return results;
	}

	public void setResults(ArrayList<SearchResult> results) {
		this.results = results;
	}

	public int getStartindex() {
		return startindex;
	}

	public void setStartindex(int startindex) {
		this.startindex = startindex;
	}

}
