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
package org.docx4j.template.wml;

import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ObjectFactory;

/**
 * TODO
 * @author <a href="https://github.com/vindell">vindell</a>
 */
public class ParagraphWmlRender {

	protected WordprocessingMLPackage wmlPackage;
	protected ObjectFactory factory;
	
	public ParagraphWmlRender(WordprocessingMLPackage wmlPackage){
		this.wmlPackage = wmlPackage;
		this.factory = Context.getWmlObjectFactory();  
	}
	
	
	
}
