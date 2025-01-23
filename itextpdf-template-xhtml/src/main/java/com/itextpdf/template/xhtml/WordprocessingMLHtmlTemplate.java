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
package com.itextpdf.template.xhtml;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.docx4j.model.structure.PageSizePaper;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import com.itextpdf.template.WordprocessingMLTemplate;
import com.itextpdf.template.xhtml.handler.DocumentHandler;
import com.itextpdf.template.xhtml.handler.def.XHTMLDocumentHandler;
import com.itextpdf.template.xhtml.io.WordprocessingMLPackageBuilder;
import org.jsoup.nodes.Document;

/**
 * 该模板仅负责将原生的HTML元素转换成XHTML后，作为模板生成WordprocessingMLPackage对象
 * @author <a href="https://github.com/vindell">vindell</a>
 */
public class WordprocessingMLHtmlTemplate extends WordprocessingMLTemplate {

	protected DocumentHandler docHandler = XHTMLDocumentHandler.getDocumentHandler();
	protected WordprocessingMLPackageBuilder wordMLPackageBuilder = WordprocessingMLPackageBuilder.getWMLPackageBuilder();
	protected boolean altChunk;
	protected boolean landscape;

	public WordprocessingMLHtmlTemplate() {
		this(false, false);
	}

	public WordprocessingMLHtmlTemplate(boolean landscape, boolean altChunk) {
		this.landscape = landscape;
		this.altChunk = altChunk;
	}

	public WordprocessingMLPackage process(File htmlFile) throws Exception {
		// 返回WordprocessingMLPackage对象
		return wordMLPackageBuilder.buildWhithXhtml(htmlFile, landscape, altChunk);
	}

	public WordprocessingMLPackage process(File htmlFile, PageSizePaper pageSize) throws Exception {
		// 返回WordprocessingMLPackage对象
		return wordMLPackageBuilder.buildWhithXhtml(htmlFile, pageSize, landscape, altChunk);
	}

	public WordprocessingMLPackage process( Document doc) throws Exception {
		// 返回WordprocessingMLPackage对象
		return wordMLPackageBuilder.buildWhithDoc(doc, landscape, altChunk);
	}

	public WordprocessingMLPackage process( Document doc, PageSizePaper pageSize) throws Exception {
		// 返回WordprocessingMLPackage对象
		return wordMLPackageBuilder.buildWhithDoc(doc, pageSize, landscape, altChunk);
	}

	@SuppressWarnings("deprecation")
	public WordprocessingMLPackage process( InputStream input) throws Exception {
		WordprocessingMLPackage wordMLPackage = null;
		try {
			wordMLPackage = wordMLPackageBuilder.buildWhithDoc(docHandler.handle(input), landscape, altChunk);
		} finally {
			IOUtils.closeQuietly(input);
		}
		// 返回WordprocessingMLPackage对象
		return wordMLPackage;
	}

	@SuppressWarnings("deprecation")
	public WordprocessingMLPackage process( InputStream input, PageSizePaper pageSize) throws Exception {
		WordprocessingMLPackage wordMLPackage = null;
		try {
			wordMLPackage = wordMLPackageBuilder.buildWhithDoc(docHandler.handle(input), pageSize, landscape, altChunk);
		} finally {
			IOUtils.closeQuietly(input);
		}
		// 返回WordprocessingMLPackage对象
		return wordMLPackage;
	}

	public WordprocessingMLPackage process( URL url) throws Exception {
		// 返回WordprocessingMLPackage对象
		return wordMLPackageBuilder.buildWhithURL(url, landscape, altChunk);
	}

	public WordprocessingMLPackage process( String url, Map<String, String> params, PageSizePaper pageSize) throws Exception {
		DataMap dataMap = new DataMap();
		dataMap.setData2(params);
		// 返回WordprocessingMLPackage对象
		return wordMLPackageBuilder.buildWhithURL(url, dataMap, pageSize, landscape, altChunk);
	}

	/**
	 * 将 {@link org.jsoup.nodes.Document} 对象转为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage}
	 * @param template ：模板内容
	 * @param variables ：变量
	 * @return {@link WordprocessingMLPackage} 对象
	 * @throws Exception ：异常对象
	 */
	@Override
	public WordprocessingMLPackage process(String template, Map<String, Object> variables) throws Exception {
		// 返回WordprocessingMLPackage对象
		return wordMLPackageBuilder.buildWhithXhtml(template, altChunk);
	}

	public DocumentHandler getDocHandler() {
		return docHandler;
	}

	public void setDocHandler(DocumentHandler docHandler) {
		this.docHandler = docHandler;
	}

	public WordprocessingMLPackageBuilder getWordMLPackageBuilder() {
		return wordMLPackageBuilder;
	}

	public void setWordMLPackageBuilder(WordprocessingMLPackageBuilder wordMLPackageBuilder) {
		this.wordMLPackageBuilder = wordMLPackageBuilder;
	}

	public boolean isAltChunk() {
		return altChunk;
	}

	public void setAltChunk(boolean altChunk) {
		this.altChunk = altChunk;
	}

}
