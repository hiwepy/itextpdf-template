package com.jeefw.fastpdf.core.document.helper;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.jeefw.fastpdf.core.document.elements.ItextXMLElement;
import com.jeefw.fastpdf.core.document.style.PDFStyleTransformer;
import com.jeefw.fastxml.jdom.xhtml.css.ElementStyleRender;

/**
 * @package com.jeefw.fastpdf.core.document.helper
 * @className: ChunkHelper
 * @description: TODO
 * @author : <a href="mailto:hnxyhcwdl1003@163.com">wandalong</a>
 * @date : 2014-1-14
 * @time : 下午2:31:21
 */
public final class ParagraphHelper {

	private static ParagraphHelper instance = null;
	private ParagraphHelper(){}
	
	public static ParagraphHelper getInstance(){
		instance = instance==null?new ParagraphHelper():instance;
		return instance;
	}
	
	public <T> Paragraph getParagraph(ItextXMLElement element,T data){
		Paragraph p = new Paragraph(element.text(data));
		
		//"p","h1","h2","h3","h4","h5","h6","h7"
		
		
		
		LineSeparator underLine =LineHelper.getInstance().getDeleteLine(element);
		p.add(underLine);
		ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(p, element);
		return p;
		
	}
	
	
	public Paragraph getUnderLineParagraph(ItextXMLElement element){
		Paragraph p = new Paragraph(element.text()); 
		LineSeparator underLine = LineHelper.getInstance().getUnderLine(element);
		p.add(underLine);
		ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(p, element);
		return p;
		
	}
	
	
	public Paragraph getDeleteLineParagraph(ItextXMLElement element){
		Paragraph p = new Paragraph(element.text()); 
		LineSeparator underLine =LineHelper.getInstance().getDeleteLine(element);
		p.add(underLine);
		ElementStyleRender.getInstance(PDFStyleTransformer.getInstance()).render(p, element);
		return p;
	}
	
}
