package com.itextpdf.template.core.document.resolver;

import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.template.core.document.elements.ItextXMLElement;
import com.jeefw.fastxml.core.utils.XMLColorUtils;
import com.jeefw.fastxml.jdom.xhtml.ColorResolver;

/**
 * 
 * @package com.itextpdf.template.core.document.resolver
 * @className: ItextBaseColorResolver
 * @description: TODO
 * @author : <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date : 2014-1-22
 * @time : 下午4:16:28
 */
public class ItextBaseColorResolver implements ColorResolver<BaseColor,ItextXMLElement>{

	private static ItextBaseColorResolver instance = null;
	
	public static ItextBaseColorResolver getInstance(){
		instance = instance==null?new ItextBaseColorResolver():instance;
		return instance;
	}

	public BaseColor getColor(ItextXMLElement element) {
		 return this.getColor(element.styles(), new String[]{
			 "background-color",
			 "border-color",
			 "border-bottom-color",
			 "border-left-color",
			 "border-right-color",
			 "border-top-color",
			 "color"});
	}
	
	public BaseColor getColor(Map<String,Object> styles,String[] cssNames) {
		for (int i = 0; i < cssNames.length; i++) {
			Object color = styles.get(cssNames[i]);
			if(color!=null&&color instanceof String){
				try {
					return this.getColor(color.toString());
				} catch (Exception e) {
					
				}
			}
		}
		return BaseColor.BLACK;
	}

	public BaseColor getColor(String color) {
		if(color!=null&&color instanceof String){
			int[] rgb = XMLColorUtils.rgb(color.toString());
			System.out.println(rgb[0]+","+rgb[1]+","+rgb[2]);
			return new BaseColor(rgb[0],rgb[1], rgb[2]);
		}
		return BaseColor.BLACK;
	}

	

	
}
