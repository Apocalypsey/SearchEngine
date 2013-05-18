/**
 *@author ���񣬴�������:2013-5-16
 *
 */
package com.yangxu.searchengine.extractor;

import java.io.*;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.ObjectFindingVisitor;

public class ExtratorCsdn extends Extractor {

	@Override
	public void extract() {

		String fileName = this.getInuputFilePath().substring(
				this.getInuputFilePath().lastIndexOf("\\") + 1);
		// System.out.println(fileName);
		OutputStreamWriter writer = null;
		// ��ȡURL
		// ��ȡTIME
		try {

			writer = new OutputStreamWriter(new FileOutputStream(
					this.getOutputPath() + fileName + ".txt"), "UTF-8");
			LineNumberReader lbr = new LineNumberReader(new InputStreamReader(
					this.getFis(), "UTF-8"));
			String line = null;
			while ((line = lbr.readLine()) != null) {
				if (lbr.getLineNumber() > 2) {
					break;
				}
				// System.out.println(line);
				writer.append(line);
				writer.append(NEWLINE);
			}

			lbr.close();
			// ��ȡTITLE
			// ���������
			NodeFilter filter_title = new TagNameFilter("title");// title�ڵ����
			NodeList nodelist1 = this.getParser().extractAllNodesThatMatch(
					filter_title);// ���˵÷��Ϲ���Ҫ��Ľڵ��LIST
			Node node_title = nodelist1.elementAt(0);// ȡ�ڵ�
			String title = null;
			if (node_title == null) {// �ж��Ƿ�Ϊ��
				title = null;
			} else {
				title = node_title.toPlainTextString();// �ѽڵ�����ı��ڵ�ת��ΪString
														// �ӵ�buftitle��
			}
			writer.append("Title:" + title);
			writer.append(NEWLINE);
			// ��ȡContent
			this.getParser().reset();// ����
			NodeFilter article_content_Filter = new AndFilter(
					new TagNameFilter("div"), new HasAttributeFilter("class",
							"article_content"));
			NodeList article_content_list = this.getParser().parse(
					article_content_Filter);
			String content = null;
			if (article_content_list.size() == 0) {
				content = "ҳ�����ݲ����ڣ�Ҳ����ҳ�治���ڵ��£�";
			} else {
				Node contentNode = article_content_list.elementAt(0);
				content = contentNode.toPlainTextString().trim().toString();
				content = content.replaceAll("&nbsp;", "");
				// �޳�content�е�һЩ�հ��ַ�
				if (content.contains("&nbsp")) {
					content.replaceAll("&nbsp", "");
				}
			}
			writer.append("Content:");
			writer.append(NEWLINE);
			writer.append(content.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

		Extractor extractor = new ExtratorCsdn();
		extractor
				.setOutputPath("e:\\crawler\\temp\\blog.csdn.net\\processed\\");
		extractor
				.setResourceDir("e:\\crawler\\temp\\blog.csdn.net\\detailPage\\");
		try {
			traverse(extractor, new File(extractor.getResourceDir()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("finised!");

	}

}
