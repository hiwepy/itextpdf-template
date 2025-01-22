/*
 * Copyright (c) 2018, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.docx4j.template.xhtml.handler.def;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.docx4j.Docx4jProperties;
import org.docx4j.template.Docx4jConstants;
import org.docx4j.template.xhtml.DataMap;
import org.docx4j.template.xhtml.handler.DocumentHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;

/**
 * 
 * TODO
 * @author <a href="https://github.com/vindell">vindell</a>
 */
public class XHTMLDocumentHandler implements DocumentHandler {

	private static final XHTMLDocumentHandler XHTML_DOCUMENT_HANDLER = new XHTMLDocumentHandler();

	/**
	 * Generate a DocumentHandler.
	 * @return the DocumentHandler
	 */
	public static DocumentHandler getDocumentHandler() {
		return XHTML_DOCUMENT_HANDLER;
	}
	
	protected XHTMLDocumentHandler() {
		
	}
	
	/**
	 * <p>Jsoup.parse(File in, String charsetName) : 它使用文件的路径做为 baseUri。 这个方法适用于如果被解析文件位于网站的本地文件系统，且相关链接也指向该文件系统</p>
	 * <p>Jsoup.parse(File in, String charsetName, String baseUri) : 这个方法用来加载和解析一个HTML文件。如在加载文件的时候发生错误，将抛出IOException，应作适当处理。
	 *	baseUri 参数用于解决文件中URLs是相对路径的问题。如果不需要可以传入一个空的字符串。</p>
	 */
	@Override
	public Document handle( File htmlFile) throws IOException {
		//获取Jsoup参数
		String charsetName = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_CHARSETNAME, Docx4jConstants.DEFAULT_CHARSETNAME );
		String baseUri = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_BASEURI,"");
		//使用Jsoup将html转换成Document对象
		Document doc = Jsoup.parse(htmlFile, charsetName, baseUri );
		//返回Document对象
		return doc;
	}

	/**
	 * Jsoup.parse(html)
	 * Jsoup.parse(html, baseUri)
	 * Jsoup.parseBodyFragment(bodyHtml)
	 * Jsoup.parseBodyFragment(bodyHtml, baseUri)
	 */
	@Override
	public Document handle( String html,boolean fragment) throws IOException{
		//获取Jsoup参数
		String baseUri = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_BASEURI,"");
		//使用Jsoup将html转换成Document对象
		Document doc = fragment ? Jsoup.parseBodyFragment( html, baseUri) : Jsoup.parse( html,baseUri);
		//返回Document对象
		return doc;
	}
	
	/**
	 * Jsoup.parse(String url, int timeoutMillis)
	 * Jsoup.connect(String url) 方法创建一个新的 Connection, 和  post() 取得和解析一个HTML文件。如果从该URL获取HTML时发生错误，便会抛出 IOException，应适当处理。
	 * 这两个方法只支持Web URLs (http和https 协议); 
	 */
	@Override
	public Document handle(URL url) throws IOException{
		//获取Jsoup参数
		String baseUri = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_BASEURI,"");
		int timeout = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_TIMEOUTMILLIS, Docx4jConstants.DEFAULT_TIMEOUTMILLIS);
		//fetch the specified URL and parse to a HTML DOM
		Document doc = Jsoup.parse(url,timeout);
		doc.setBaseUri(baseUri);
		//返回Document对象
		return doc;
	}
	
	/**
	 * Jsoup.parse(String url, int timeoutMillis)
	 * Jsoup.connect(String url) 方法创建一个新的 Connection, 和  post() 取得和解析一个HTML文件。如果从该URL获取HTML时发生错误，便会抛出 IOException，应适当处理。
	 * 这两个方法只支持Web URLs (http和https 协议); 
	 */
	@Override
	public Document handle(String url, DataMap dataMap) throws IOException{
		//获取Jsoup参数
		String baseUri = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_BASEURI,"");
		String userAgent = "Mozilla/5.0 (jsoup)";
		int timeout = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_TIMEOUTMILLIS, Docx4jConstants.DEFAULT_TIMEOUTMILLIS);
		//fetch the specified URL and parse to a HTML DOM
		Document doc = Jsoup.connect(url)
				  .data(dataMap.getData1())
				  .data(dataMap.getData2())
				  .userAgent(userAgent)
				  .cookies(dataMap.getCookies())
				  .timeout(timeout)
				  .post();
		doc.setBaseUri(baseUri);
		//返回Document对象
		return doc;
	}
	
	/**
	 * Jsoup.parse(in, charsetName, baseUri)
	 */
	@Override
	public Document handle( InputStream input) throws IOException{
		//获取Jsoup参数
		String charsetName = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_CHARSETNAME, Docx4jConstants.DEFAULT_CHARSETNAME );
		String baseUri = Docx4jProperties.getProperty(Docx4jConstants.DOCX4J_JSOUP_PARSE_BASEURI,"");
		//使用Jsoup将html转换成Document对象
		Document doc = Jsoup.parse(input, charsetName, baseUri);
		
		OutputSettings outputSettings = new OutputSettings();
		
		outputSettings.prettyPrint(false);
		
		/*
		outputSettings.syntax(syntax)
		outputSettings.charset(charset)
		outputSettings*/
		doc.outputSettings(outputSettings);
		
		//返回Document对象
		return doc;
	}
	
	public static void main(String[] args) {
		String baseUri = "http://www.baidu.com";
		String html = "<a href=\"http://www.baidu.com/gaoji/preferences.html\"name=\"tj_setting\">搜索设置</a>";
		String doc = Jsoup.clean(html, baseUri, Whitelist.none());
		System.out.println(doc);
		System.out.println("*******");
		doc = Jsoup.clean(html, baseUri, Whitelist.simpleText());
		System.out.println(doc);
		System.out.println("*******");
		doc = Jsoup.clean(html, baseUri, Whitelist.basic());
		System.out.println(doc);
		System.out.println("*******");
		doc = Jsoup.clean(html, baseUri, Whitelist.basicWithImages());
		System.out.println(doc);
		System.out.println("*******");
		doc = Jsoup.clean(html, baseUri, Whitelist.relaxed());
		System.out.println(doc);

	}


}
