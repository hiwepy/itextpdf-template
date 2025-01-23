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
package com.itextpdf.template.utils;

import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import com.itextpdf.template.fonts.ChineseFont;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;

/**
 * TODO
 * @author <a href="https://github.com/vindell">vindell</a>
 */
public class PhysicalFontUtils {

	private static Mapper newFontMapper() throws Exception {

		// Set up font mapper (optional)

		//  example of mapping font Times New Roman which doesn't have certain Arabic glyphs
		// eg Glyph "ي" (0x64a, afii57450) not available in font "TimesNewRomanPS-ItalicMT".
		// eg Glyph "ج" (0x62c, afii57420) not available in font "TimesNewRomanPS-ItalicMT".
		// to a font which does
		PhysicalFonts.get("Arial Unicode MS");


		/*
		 * This mapper uses Panose to guess the physical font which is a closest fit for the font used in the document.
		 * （这个映射器使用Panose算法猜测最适合这个文档使用的物理字体。）
		 *
		 * Panose是一种依照字体外观来进行分类的方法。我们可以通过PANOSE体系将字体的外观特征进行整理，并且与其它字体归类比较。
		 * Panose的原形在1985年由Benjamin Bauermeister开发，当时一种字体由7位16进制数字定义，现在则发展为10位，也就是字体的十种特征。这每一位数字都给出了它定义的一种视觉外观的量度，如笔划的粗细或是字体衬线的样式等。
		 * Panose定义的范围：Latin Text，Latin Script，Latin Decorative，Iconographic，Japanese Text，Cyrillic Text，Hebrew。
		 *
		 * It is most likely to be suitable on Linux or OSX systems which don't have Microsoft's fonts installed.
		 * （它很可能适用于没有安装Microsoft字体的Linux或OSX系统。）
		 *
		 * 1、获取Microsoft字体我们需要这些：a.在Microsoft平台上，嵌入PDF输出; b. docx4all - 所有平台 - 填充字体下拉列表
		 * setupMicrosoftFontFilenames();
		 * 2、 自动检测系统上可用的字体
		 * PhysicalFonts.discoverPhysicalFonts();
		 *
		 */
		//Mapper fontMapper = new BestMatchingMapper();
		/*
		 *
		 * This mapper automatically maps document fonts for which the exact font is physically available.
	     * Think of this as an identity mapping.  For  this reason, it will work best on Windows, or a system on
	     * which Microsoft fonts have been installed.
	     * （此映射器自动映射确切可用的文档字体，将此视为标识映射；基于这个原因，它在Windows系统或安装了微软字体库的系统运行的更好。）
	     * You can manually add your own additional mappings if you wish.
	     * 如果需要，你可以手动添加自己的字体映射
		 *
		 * 1、 自动检测系统上可用的字体
		 * PhysicalFonts.discoverPhysicalFonts();
		 *
		 */
		Mapper fontMapper = new IdentityPlusMapper();
		//进行中文字体兼容处理
		fontMapper.put("微软雅黑",PhysicalFonts.get("Microsoft Yahei"));
        fontMapper.put("黑体",PhysicalFonts.get("SimHei"));
        fontMapper.put("楷体",PhysicalFonts.get("KaiTi"));
        fontMapper.put("隶书", PhysicalFonts.get("LiSu"));
        fontMapper.put("宋体",PhysicalFonts.get("SimSun"));
        fontMapper.put("宋体扩展",PhysicalFonts.get("simsun-extB"));
        fontMapper.put("新宋体",PhysicalFonts.get("NSimSun"));
        fontMapper.put("仿宋",PhysicalFonts.get("FangSong"));
        fontMapper.put("仿宋_GB2312",PhysicalFonts.get("FangSong_GB2312"));
        fontMapper.put("幼圆",PhysicalFonts.get("YouYuan"));
        fontMapper.put("华文宋体",PhysicalFonts.get("STSong"));
        fontMapper.put("华文仿宋", PhysicalFonts.get("STFangsong"));
        fontMapper.put("华文中宋",PhysicalFonts.get("STZhongsong"));
        fontMapper.put("华文行楷", PhysicalFonts.get("STXingkai"));

        return fontMapper;
	}

	/*
	 * 为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 配置中文字体;解决中文乱码问题
	 */
	public static void setWmlPackageFonts(WordprocessingMLPackage wmlPackage) throws Docx4JException {
		try {
			//字体映射;
			Mapper fontMapper = newFontMapper();
			//设置文档字体库
			wmlPackage.setFontMapper(fontMapper, true);
		} catch (Exception e) {
			throw new Docx4JException(e.getMessage(),e.getCause());
		}
    }

	/*
	 * 为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 配置默认字体
	 */
	public static void setDefaultFont(WordprocessingMLPackage wmlPackage,String fontName) throws Docx4JException {
        //设置文件默认字体
        RFonts rfonts = Context.getWmlObjectFactory().createRFonts();
        rfonts.setAsciiTheme(null);
        rfonts.setAscii(fontName);
        rfonts.setHAnsi(fontName);
        rfonts.setEastAsia(fontName);
        RPr rpr = wmlPackage.getMainDocumentPart().getPropertyResolver().getDocumentDefaultRPr();
        rpr.setRFonts(rfonts);
    }

	/*
	 * 为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 配置中文字体
	 */
	public static void setSimSunFont(WordprocessingMLPackage wmlPackage) throws Docx4JException {
        //设置文件默认字体
		setDefaultFont(wmlPackage, ChineseFont.SIMSUM.getFontName());
    }

	/*
	 * 为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 增加新的字体
	 */
	public static void setPhysicalFont(WordprocessingMLPackage wmlPackage,PhysicalFont physicalFont) throws Exception {
		Mapper fontMapper = wmlPackage.getFontMapper() == null ? new IdentityPlusMapper() : wmlPackage.getFontMapper();
		//分别设置字体名和别名对应的字体库
		fontMapper.put(physicalFont.getName(), physicalFont );
		//设置文档字体库
		wmlPackage.setFontMapper(fontMapper, true);
    }

	/*
	 * 为 {@link org.docx4j.openpackaging.packages.WordprocessingMLPackage} 增加新的字体
	 */
	public static void setPhysicalFont(WordprocessingMLPackage wmlPackage,String fontName) throws Exception {
		//Mapper fontMapper = new BestMatchingMapper();
		Mapper fontMapper = wmlPackage.getFontMapper() == null ? new IdentityPlusMapper() : wmlPackage.getFontMapper();
		//获取字体库
		PhysicalFont physicalFont = PhysicalFonts.get(fontName);
		//分别设置字体名和别名对应的字体库
		fontMapper.put(fontName, physicalFont );
		//设置文档字体库
		wmlPackage.setFontMapper(fontMapper, true);
    }
}
