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
package com.itextpdf.template.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.docx4j.Docx4jProperties;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import com.itextpdf.template.WordprocessingMLTemplate;
import com.itextpdf.template.utils.ConfigUtils;
import com.itextpdf.template.xhtml.WordprocessingMLHtmlTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.HtmlEscape;
import freemarker.template.utility.XmlEscape;

/**
 * 该模板仅负责使用Freemarker模板引擎将指定模板生成HTML并将HTML转换成XHTML后，作为模板生成WordprocessingMLPackage对象
 * @author <a href="https://github.com/vindell">vindell</a>
 */
public class WordprocessingMLFreemarkerTemplate extends WordprocessingMLTemplate {

	protected final Logger LOG = LoggerFactory.getLogger(WordprocessingMLFreemarkerTemplate.class);
	protected Configuration engine;
	protected Properties freemarkerSettings;
	protected Map<String, Object> freemarkerVariables;
	protected String defaultEncoding;
	protected final List<TemplateLoader> templateLoaders = new ArrayList<TemplateLoader>();
	protected List<TemplateLoader> preTemplateLoaders;
	protected List<TemplateLoader> postTemplateLoaders;
	protected TemplateModel templateModel;
	protected WordprocessingMLHtmlTemplate mlHtmlTemplate;

	public WordprocessingMLFreemarkerTemplate() {
		this(false, false);
	}

	public WordprocessingMLFreemarkerTemplate(boolean landscape, boolean altChunk) {
		this.mlHtmlTemplate = new WordprocessingMLHtmlTemplate(landscape, altChunk) ;
	}

	public WordprocessingMLFreemarkerTemplate(WordprocessingMLHtmlTemplate template) {
		this.mlHtmlTemplate = template;
	}


	/**
	 * 使用Freemarker模板引擎渲染模板
	 * @param template ：模板内容
	 * @param variables ：变量
	 * @return {@link WordprocessingMLPackage} 对象
	 * @throws Exception ：异常对象
	 */
	@Override
	public WordprocessingMLPackage process(String template,Map<String, Object> variables)  throws Exception{
		variables.put("String", this.templateModel);
		// 创建模板输出内容接收对象
		StringWriter output = new StringWriter();
		// 使用Freemarker模板引擎渲染模板
		getEngine().getTemplate(template).process(variables, output);
		//获取模板渲染后的结果
		String html = output.toString();
		//使用HtmlTemplate进行渲染
		return mlHtmlTemplate.process(html, variables);
	}

	public Configuration getEngine() throws IOException, TemplateException {
		return engine == null ? getInternalEngine() : engine;
	}

	public void setEngine(Configuration engine) {
		this.engine = engine;
	}

	protected Configuration getInternalEngine() throws IOException, TemplateException{

		try {
			BeansWrapper beansWrapper = new BeansWrapper(Configuration.VERSION_2_3_23);
			this.templateModel = beansWrapper.getStaticModels().get(String.class.getName());
		} catch (TemplateModelException e) {
			throw new IOException(e.getMessage(),e.getCause());
		}

		// 创建 Configuration 实例
		Configuration config = new Configuration(Configuration.VERSION_2_3_23);

		Properties props = ConfigUtils.filterWithPrefix("docx4j.freemarker.", "docx4j.freemarker.", Docx4jProperties.getProperties(), false);

		// FreeMarker will only accept known keys in its setSettings and
		// setAllSharedVariables methods.
		if (!props.isEmpty()) {
			config.setSettings(props);
		}

		if (this.freemarkerVariables != null && !this.freemarkerVariables.isEmpty()) {
			config.setAllSharedVariables(new SimpleHash(this.freemarkerVariables, config.getObjectWrapper()));
		}

		if (this.defaultEncoding != null) {
			config.setDefaultEncoding(this.defaultEncoding);
		}

		List<TemplateLoader> templateLoaders = new LinkedList<TemplateLoader>(this.templateLoaders);

		// Register template loaders that are supposed to kick in early.
		if (this.preTemplateLoaders != null) {
			templateLoaders.addAll(this.preTemplateLoaders);
		}

		postProcessTemplateLoaders(templateLoaders);

		// Register template loaders that are supposed to kick in late.
		if (this.postTemplateLoaders != null) {
			templateLoaders.addAll(this.postTemplateLoaders);
		}

		TemplateLoader loader = getAggregateTemplateLoader(templateLoaders);
		if (loader != null) {
			config.setTemplateLoader(loader);
		}
		//config.setClassLoaderForTemplateLoading(classLoader, basePackagePath);
		//config.setCustomAttribute(name, value);
		//config.setDirectoryForTemplateLoading(dir);
		//config.setServletContextForTemplateLoading(servletContext, path);
		//config.setSharedVariable(name, value);
		//config.setSharedVariable(name, tm);
		config.setSharedVariable("fmXmlEscape", new XmlEscape());
		config.setSharedVariable("fmHtmlEscape", new HtmlEscape());
		//config.setSharedVaribles(map);

		// 设置模板引擎，减少重复初始化消耗
        this.setEngine(config);
        return config;
	}

	/**
	 * Return a TemplateLoader based on the given TemplateLoader list.
	 * If more than one TemplateLoader has been registered, a FreeMarker
	 * MultiTemplateLoader needs to be created.
	 * @param templateLoaders the final List of TemplateLoader instances
	 * @return the aggregate TemplateLoader
	 */
	protected TemplateLoader getAggregateTemplateLoader(List<TemplateLoader> templateLoaders) {
		int loaderCount = templateLoaders.size();
		switch (loaderCount) {
			case 0:
				LOG.info("No FreeMarker TemplateLoaders specified");
				return null;
			case 1:
				return templateLoaders.get(0);
			default:
				TemplateLoader[] loaders = templateLoaders.toArray(new TemplateLoader[loaderCount]);
				return new MultiTemplateLoader(loaders);
		}
	}


	/**
	 * Set properties that contain well-known FreeMarker keys which will be
	 * passed to FreeMarker's {@code Configuration.setSettings} method.
	 * @param settings properties
	 * @see freemarker.template.Configuration#setSettings
	 */
	public void setFreemarkerSettings(Properties settings) {
		this.freemarkerSettings = settings;
	}

	/**
	 * Set a Map that contains well-known FreeMarker objects which will be passed
	 * to FreeMarker's {@code Configuration.setAllSharedVariables()} method.
	 * @param variables   variables
	 * @see freemarker.template.Configuration#setAllSharedVariables
	 */
	public void setFreemarkerVariables(Map<String, Object> variables) {
		this.freemarkerVariables = variables;
	}

	/**
	 * Set the default encoding for the FreeMarker configuration.
	 * If not specified, FreeMarker will use the platform file encoding.
	 * <p>Used for template rendering unless there is an explicit encoding specified
	 * for the rendering process (for example, on Spring's FreeMarkerView).
	 * @param defaultEncoding Default Encoding
	 * @see freemarker.template.Configuration#setDefaultEncoding
	 */
	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}

	/**
	 * Set a List of {@code TemplateLoader}s that will be used to search
	 * for templates. For example, one or more custom loaders such as database
	 * loaders could be configured and injected here.
	 * <p>The {@link TemplateLoader TemplateLoaders} specified here will be
	 * registered <i>before</i> the default template loaders that this factory
	 * registers (such as loaders for specified "templateLoaderPaths" or any
	 * loaders registered in {@link #postProcessTemplateLoaders}).
	 * @param preTemplateLoaders the Array of TemplateLoader instances,
	 * to be modified by a subclass
	 * @see #postProcessTemplateLoaders
	 */
	public void setPreTemplateLoaders(TemplateLoader... preTemplateLoaders) {
		this.preTemplateLoaders = Arrays.asList(preTemplateLoaders);
	}

	/**
	 * Set a List of {@code TemplateLoader}s that will be used to search
	 * for templates. For example, one or more custom loaders such as database
	 * loaders can be configured.
	 * <p>The {@link TemplateLoader TemplateLoaders} specified here will be
	 * registered <i>after</i> the default template loaders that this factory
	 * registers (such as loaders for specified "templateLoaderPaths" or any
	 * loaders registered in {@link #postProcessTemplateLoaders}).
	 * @param postTemplateLoaders the Array of TemplateLoader instances,
	 * to be modified by a subclass
	 * @see #postProcessTemplateLoaders
	 */
	public void setPostTemplateLoaders(TemplateLoader... postTemplateLoaders) {
		this.postTemplateLoaders = Arrays.asList(postTemplateLoaders);
	}

	/**
	 * To be overridden by subclasses that want to register custom
	 * TemplateLoader instances after this factory created its default
	 * template loaders.
	 * <p>Called by {@code createConfiguration()}. Note that specified
	 * "postTemplateLoaders" will be registered <i>after</i> any loaders
	 * registered by this callback; as a consequence, they are <i>not</i>
	 * included in the given List.
	 * @param templateLoaders the current List of TemplateLoader instances,
	 * to be modified by a subclass
	 * @see #setPostTemplateLoaders
	 */
	protected void postProcessTemplateLoaders(List<TemplateLoader> templateLoaders) {
		templateLoaders.add(new ClassTemplateLoader(WordprocessingMLFreemarkerTemplate.class, ""));
		LOG.info("ClassTemplateLoader for WordprocessingMLFreemarkerTemplate added to FreeMarker configuration");
	}


}
