package com.itextpdf.template.core.document.helper;

import com.itextpdf.template.core.document.draw.Arrow;
import com.itextpdf.template.core.document.elements.ItextXMLElement;

/**
 * @package com.itextpdf.template.core.document.helper
 * @className: VerticalPositionMarkHelper
 * @description: TODO
 * @author : <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date : 2014-1-14
 * @time : 下午6:42:43
 */
public class ArrowMarkHelper{
	
	private static ArrowMarkHelper instance = null;
	private ArrowMarkHelper(){}
	
	public static ArrowMarkHelper getInstance(){
		instance = instance==null?new ArrowMarkHelper():instance;
		return instance;
	}
	
	public Arrow getArrow(ItextXMLElement element){
		return null;
	}
	
}
