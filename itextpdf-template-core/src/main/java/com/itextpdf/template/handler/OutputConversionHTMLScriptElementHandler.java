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
package com.itextpdf.template.handler;

import org.docx4j.convert.out.ConversionHTMLScriptElementHandler;
import org.docx4j.openpackaging.packages.OpcPackage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * TODO
 * @author <a href="https://github.com/vindell">vindell</a>
 */
public class OutputConversionHTMLScriptElementHandler implements ConversionHTMLScriptElementHandler {

	private static final OutputConversionHTMLScriptElementHandler OUTPUT_CONVERSION_HTML_SCRIPT_ELEMENT_HANDLER = new OutputConversionHTMLScriptElementHandler();

	/**
	 * Generate a OutputConversionHTMLScriptElementHandler.
	 * @return the OutputConversionHTMLScriptElementHandler
	 */
	public static OutputConversionHTMLScriptElementHandler getScriptElementHandler() {
		return OUTPUT_CONVERSION_HTML_SCRIPT_ELEMENT_HANDLER;
	}

	protected OutputConversionHTMLScriptElementHandler() {

	}

	@Override
	public Element createScriptElement(OpcPackage opcPackage, Document document, String scriptDefinition) {
		Element ret = null;
		if ((scriptDefinition != null) && (scriptDefinition.length() > 0)) {
			ret = document.createElement("script");
			ret.setAttribute("type", "text/javascript");
			ret.appendChild(document.createComment(scriptDefinition));
		}
		return ret;
	}

}
