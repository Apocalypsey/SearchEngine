/* @author Xu Yang;
   @time 2013-5-17
*/
package com.yangxu.searchengine.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Parser;

/**
 * ��ҳ��ȡ�ุ��
 * @author yangxu
 *
 */
public abstract class Extractor {

	protected static final String NEWLINE = "\r\n";

	/**
	 * ��ʾ���н�������·��
	 */
	private String outputPath;

	/**
	 * ��ʾ��ǰ���ڱ�������ļ�
	 */
	protected String inuputFilePath;

	/**
	 * ��ʾ��ǰ���б�ץȡ����ҳ��Ŀ¼
	 */
	private String resourceDir;

	private FileInputStream fis ;
	/**
	 * HTMLParser��ʵ��
	 */
	private Parser parser;

	/**
	 * ��ͼƬ·�����й�ϣ���㷨���������MD5�㷨
	 */
	protected static final String HASH_ALGORITHM = "md5";

	/**
	 * �ָ���
	 */
	public static final String SEPARATOR = "======================";

	/**
	 * װ����Ҫ����ҳ�ļ�
	 * 
	 */
	public void loadFile(String path) {
		try {
			parser = new Parser(path);
			inuputFilePath = path;
			fis = new FileInputStream(path);
			parser.setEncoding("utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ�����·��
	 */
	public String getOutputPath() {
		return outputPath;
	}

	/**
	 * ���������·����ͨ���ڳ�ʼ��Extractorʱ��Ӧ����
	 */
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public Parser getParser() {
		return parser;
	}

	/**
	 * ʹ��������ƥ�䲢�����ҳ�е��ַ���
	 */
	protected String getProp(String pattern, String match, int index) {
		Pattern sp = Pattern.compile(pattern);
		Matcher matcher = sp.matcher(match);
		while (matcher.find()) {
			return matcher.group(index);
		}
		return null;
	}

	

	/**
	 * ���󷽷������ڹ�����ʵ�֡� �书����Ҫ�ǽ�����ҳ�ļ� ����Ʒ��Ϣ���浽
	 * 
	 */
	public abstract void extract();

	/**
	 * ��ȡ���ڴ�����ļ���·��
	 */
	public String getInuputFilePath() {
		return inuputFilePath;
	}
	
	/**
	 * ��ȡ��Դ�ļ�Ŀ¼
	 * @return
	 */
	public String getResourceDir() {
		return resourceDir;
	}

	public void setResourceDir(String resourceDir) {
		this.resourceDir = resourceDir;
	}

	public void setInuputFilePath(String inuputFilePath) {
		this.inuputFilePath = inuputFilePath;
	}


	// ͳ���ļ�����
	static int count = 0;

	/**
	 * �������е��ļ�������extractor����
	 * @param extractor
	 * @param path
	 * @throws Exception
	 */
	public static void traverse(Extractor extractor, File path)
			throws Exception {
		if (path == null) {
			return;
		}
		if (path.isDirectory()) {
			String[] files = path.list();
			for (int i = 0; i < files.length; i++) {
				traverse(extractor, new File(path, files[i]));
			}
		} else {
			String pathname = path.getAbsolutePath();
			System.out.println(pathname);
			count++;
			extractor.loadFile(path.getAbsolutePath());
			extractor.extract();
		}
	}
	
	
	public FileInputStream getFis() {
		return fis;
	}

	public void setFis(FileInputStream fis) {
		this.fis = fis;
	}

	

}
