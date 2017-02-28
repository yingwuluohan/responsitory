package com.common.convert;

import org.apache.commons.lang3.StringUtils;
import org.apache.oro.text.regex.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc

/**

 */
public class AntiXSSUtils {

	/** 常量   ANTIXSS_NO. */
	public static final int ANTIXSS_NO = 0;
	
	/** 常量   ANTIXSS_STRING. */
	public static final int ANTIXSS_STRING = 1;
	
	/** 常量   ANTIXSS_HTML. */
	public static final int ANTIXSS_HTML = 2;

	/** 常量   SCRIPT_TAG_PATTERN. */
	static final Pattern SCRIPT_TAG_PATTERN = Pattern.compile(
			"<script[^>]*>.*</script[^>]*>", Pattern.CASE_INSENSITIVE);

	/** 常量   SCRIPT_TAG_DANGOEROUS_PATTERN. */
	static final Pattern SCRIPT_TAG_DANGOEROUS_PATTERN = Pattern.compile(
			"<div[^>]*background-image[^>]*>" +
			"|<?import[^>]*implementation[^>]*>" +
			"|<style[^>]*>.*@\\\\?i\\\\?m\\\\?p\\\\?o\\\\?r\\\\?t.*</style[^>]*>" +
			"|<style[^>]*javascript[^>]*>.*</style[^>]*>" +
			"|<img[^>]*j[\\s|\f|\b|\0]*a[\\s|\f|\b|\0]*v[\\s|\f|\b|\0]*a[\\s|\f|\b|\0]*s[\\s|\f|\b|\0]*c[\\s|\f|\b|\0]*r[\\s|\f|\b|\0]*i[\\s|\f|\b|\0]*p[\\s|\f|\b|\0]*t[^>]*" +
			"|<object[^>]*>.*</object[^>]*>" +
			"|<xml[^>]*>.*</xml[^>]*>" +
			"|<META[^>]*content[^>]*>" +
			//"|<a[^>]*http://[^>]*>" +   //去除绝对链接，在发布系统中可以不去除
			"|<LINK[^>]*HREF[^>]*>" +
			"|<layer[^>]*src=[^>]*>.*</layer[^>]*>" +
			"|<iframe|</iframe>" +
			"|<xml[^>]*src[^>]*>"+
			"|<script[^>]*>" +
			"|<br[^>]*&[^>]*>"+
			"<div[^>]*style[^>]*url[^>]*>"+
			"|<frameset[^>]*>.*</frameset[^>]*>" ,
			Pattern.CASE_INSENSITIVE);

	/** 常量   pc. */
	static final PatternCompiler pc = new Perl5Compiler();

	/** 常量   matcher. */
	static final PatternMatcher matcher = new Perl5Matcher();

	/**
	 * 过滤文本中的脚本.
	 *
	 * @param content the content
	 * @return the String
	 */
	public synchronized static String antiXSS(String content) {
		String old = content;
		String ret = _antiXSS(content);
		while (!ret.equals(old)) {
			old = ret;
			ret = _antiXSS(ret);
		}
		return ret;
	}

	/**
	 * (这里用一句话描述这个方法的作用) _antiXSS.
	 * (这里描述这个方法适用条件 – 可选)<br>
	 * (这里描述这个方法的执行流程 – 可选)<br>
	 * (这里描述这个方法的使用方法 – 可选)<br>
	 * (这里描述这个方法的注意事项 – 可选)<br>
	 *
	 * @param content the content
	 * @return the String
	 */
	private  static String _antiXSS(String content) {
		try {
			return stripAllowScriptAccess(stripProtocol(stripCssExpression(stripAsciiAndHex(stripEvent(stripScriptTag(content))))));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * (这里用一句话描述这个方法的作用) stripScriptTag.
	 * (这里描述这个方法适用条件 – 可选)<br>
	 * (这里描述这个方法的执行流程 – 可选)<br>
	 * (这里描述这个方法的使用方法 – 可选)<br>
	 * (这里描述这个方法的注意事项 – 可选)<br>
	 *
	 * @param content the content
	 * @return the String
	 */
	private static String stripScriptTag(String content) {
		Matcher m = SCRIPT_TAG_PATTERN.matcher(content);
		content = m.replaceAll("");
		
		m = SCRIPT_TAG_DANGOEROUS_PATTERN.matcher(content);
		content = m.replaceAll("");
		return content;
	}

	/**
	 * (这里用一句话描述这个方法的作用) stripEvent.
	 * (这里描述这个方法适用条件 – 可选)<br>
	 * (这里描述这个方法的执行流程 – 可选)<br>
	 * (这里描述这个方法的使用方法 – 可选)<br>
	 * (这里描述这个方法的注意事项 – 可选)<br>
	 *
	 * @param content the content
	 * @return the String
	 * @throws Exception the exception
	 */
	private static String stripEvent(String content) throws Exception {
		String[] events = { "onmouseover", "onmouseout", "onmousedown",
				"onmouseup", "onmousemove", "onclick", "ondblclick",
				"onkeypress", "onkeydown", "onkeyup", "ondragstart",
				"onerrorupdate", "onhelp", "onreadystatechange", "onrowenter",
				"onrowexit", "onselectstart", "onload", "onunload",
				"onbeforeunload", "onblur", "onerror", "onfocus", "onresize",
				"onscroll", "oncontextmenu" };
		for ( int i=0; i<events.length; i++ ) {
			String event = events[i];
			org.apache.oro.text.regex.Pattern p = pc.compile("(<[^>]*)("
					+ event + ")([^>]*>)", Perl5Compiler.CASE_INSENSITIVE_MASK);
			if (null != p)
				content = Util.substitute(matcher, p, new Perl5Substitution(
						"$1" + event.substring(2) + "$3"), content,
						Util.SUBSTITUTE_ALL);

		}
		return content;
	}

	/**
	 * (这里用一句话描述这个方法的作用) stripAsciiAndHex.
	 * (这里描述这个方法适用条件 – 可选)<br>
	 * (这里描述这个方法的执行流程 – 可选)<br>
	 * (这里描述这个方法的使用方法 – 可选)<br>
	 * (这里描述这个方法的注意事项 – 可选)<br>
	 *
	 * @param content the content
	 * @return the String
	 * @throws Exception the exception
	 */
	private static String stripAsciiAndHex(String content) throws Exception {
		// filter &# \00xx
		org.apache.oro.text.regex.Pattern p = pc.compile(
				"(<[^>]*)(&#|\\\\00)([^>]*>)",
				Perl5Compiler.CASE_INSENSITIVE_MASK);
		if (null != p)
			content = Util
					.substitute(matcher, p, new Perl5Substitution("$1$3"),
							content, Util.SUBSTITUTE_ALL);
		return content;
	}

	/**
	 * (这里用一句话描述这个方法的作用) stripCssExpression.
	 * (这里描述这个方法适用条件 – 可选)<br>
	 * (这里描述这个方法的执行流程 – 可选)<br>
	 * (这里描述这个方法的使用方法 – 可选)<br>
	 * (这里描述这个方法的注意事项 – 可选)<br>
	 *
	 * @param content the content
	 * @return the String
	 * @throws Exception the exception
	 */
	private static String stripCssExpression(String content) throws Exception {
		org.apache.oro.text.regex.Pattern p = pc.compile(
				"(<[^>]*style=.*)/\\*.*\\*/([^>]*>)",
				Perl5Compiler.CASE_INSENSITIVE_MASK);
		if (null != p)
			content = Util
					.substitute(matcher, p, new Perl5Substitution("$1$2"),
							content, Util.SUBSTITUTE_ALL);

		p = pc
				.compile(
						"(<[^>]*style=[^>]+)(expression|javascript|vbscript|-moz-binding)([^>]*>)",
						Perl5Compiler.CASE_INSENSITIVE_MASK);
		if (null != p)
			content = Util
					.substitute(matcher, p, new Perl5Substitution("$1$3"),
							content, Util.SUBSTITUTE_ALL);

		p = pc.compile("(<style[^>]*>.*)/\\*.*\\*/(.*</style[^>]*>)",
				Perl5Compiler.CASE_INSENSITIVE_MASK);
		if (null != p) 
		{
			//System.out.print("=============="+content+"\n");
			content = Util
					.substitute(matcher, p, new Perl5Substitution("$1$2"),
							content, Util.SUBSTITUTE_ALL);
		}

		p = pc
				.compile(
						"(<style[^>]*>[^>]+)(expression|javascript|vbscript|-moz-binding)(.*</style[^>]*>)",
						Perl5Compiler.CASE_INSENSITIVE_MASK);
		if (null != p)
			content = Util
					.substitute(matcher, p, new Perl5Substitution("$1$3"),
							content, Util.SUBSTITUTE_ALL);
		return content;
	}

	/**
	 * (这里用一句话描述这个方法的作用) stripProtocol.
	 * (这里描述这个方法适用条件 – 可选)<br>
	 * (这里描述这个方法的执行流程 – 可选)<br>
	 * (这里描述这个方法的使用方法 – 可选)<br>
	 * (这里描述这个方法的注意事项 – 可选)<br>
	 *
	 * @param content the content
	 * @return the String
	 * @throws Exception the exception
	 */
	private static String stripProtocol(String content) throws Exception {
		String[] protocols = { "javascript", "vbscript", "livescript",
				"ms-its", "mhtml", "data", "firefoxurl", "mocha" };
		String protocol = null;
		for ( int i=0; i<protocols.length; i++ ) {
			protocol = protocols[i];
			org.apache.oro.text.regex.Pattern p = pc.compile("(<[^>]*)"
					+ protocol + ":([^>]*>)",
					Perl5Compiler.CASE_INSENSITIVE_MASK);
			if (null != p)
				content = Util.substitute(matcher, p, new Perl5Substitution(
						"$1/$2"), content, Util.SUBSTITUTE_ALL);
		}
		return content;
	}

	/**
	 * (这里用一句话描述这个方法的作用) stripAllowScriptAccess.
	 * (这里描述这个方法适用条件 – 可选)<br>
	 * (这里描述这个方法的执行流程 – 可选)<br>
	 * (这里描述这个方法的使用方法 – 可选)<br>
	 * (这里描述这个方法的注意事项 – 可选)<br>
	 *
	 * @param content the content
	 * @return the String
	 * @throws Exception the exception
	 */
	private static String stripAllowScriptAccess(String content)
			throws Exception {
		org.apache.oro.text.regex.Pattern p = pc.compile(
				"(<[^>]*)AllowScriptAccess([^>]*>)",
				Perl5Compiler.CASE_INSENSITIVE_MASK);
		if (null != p)
			content = Util.substitute(matcher, p, new Perl5Substitution(
					"$1Allow_Script_Access$2"), content, Util.SUBSTITUTE_ALL);
		return content;
	}
	
	
    /**
     * 过滤文本中sql，防止sql注入攻击.
     * (这里描述这个方法适用条件 – 可选)<br>
     * (这里描述这个方法的执行流程 – 可选)<br>
     * (这里描述这个方法的使用方法 – 可选)<br>
     * (这里描述这个方法的注意事项 – 可选)<br>
     *
     * @param str the str
     * @return the String
     */
    public static String antiSqlInject( String str )
    {
    	if ( str==null || str.length()==0 ) return str;
    	
    	//过滤下面字符
    	String[] chars = {"@", "\\-\\-", "/\\*", "\\*/", "%", ";", "'", "\\*" };
    	String[] repChars = {"＠", "——", "/×", "×/", "％", "；", "‘", "×" };
    	for ( int i=0; i<chars.length; i++ ) {
    		str = str.replaceAll( chars[i], repChars[i] );
    	}
    	
    	//下面字符后加"`"
    	String[] keywords = {"insert","select","delete","update","create","truncate","drop","count","declare","sum","desc","exec","and","or","join","union"};
    	
		Pattern pattern = null;
		Matcher matcher = null;

    	for ( int i=0; i<keywords.length; i++ ) {
    		pattern = Pattern.compile( "("+keywords[i]+")(\\s)", Pattern.CASE_INSENSITIVE );
    		matcher = pattern.matcher(str);
    		while ( matcher.find() ) {
    			str = str.replaceAll( matcher.group(0), matcher.group(1)+"`" );
    		}
    	}
    	
    	
    	return str;
    }

    

	/** 常量   DISABLE_SCRIPT. */
	private static final String[] DISABLE_SCRIPT = new String[] { "%3C", "%3c",
			"%3E", "%3e", "<", ">" };

	/**
	 * Exist disable script.
	 * 
	 * 如果成功返回 true
	 *
	 * @param s the s
	 * @return true, if exist disable script
	 */
	public static boolean existDisableScript(String s) {
		if (s!=null && !"".equals(s)) {
			for (int i = 0; i < DISABLE_SCRIPT.length; i++) {
				if (StringUtils.contains(s, DISABLE_SCRIPT[i])) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * (这里用一句话描述这个方法的作用) stripDisableChar.
	 * (这里描述这个方法适用条件 – 可选)<br>
	 * (这里描述这个方法的执行流程 – 可选)<br>
	 * (这里描述这个方法的使用方法 – 可选)<br>
	 * (这里描述这个方法的注意事项 – 可选)<br>
	 *
	 * @param value the value
	 * @return the String
	 */
	private static String stripDisableChar( String value )
	{
		if ( StringUtils.isBlank(value) ) return value;
		
		for ( int i=0; i<DISABLE_SCRIPT.length; i++ ) {
			value = value.replaceAll( DISABLE_SCRIPT[i], "" );
		}
		return value;
	}
	
	
	/**
	 * 过滤文本.
	 *
	 * @param value the value
	 * @param type the type 过滤类型
	 * @return the String
	 */
	public static String filter( String value, int type )
	{
		if ( value==null || value.length()==0 || type==ANTIXSS_NO ) return value;
		if ( type==ANTIXSS_HTML ) {
			value = AntiXSSUtils.antiXSS(value);
		}
		else {
			value = AntiXSSUtils.stripDisableChar(value);
			value = AntiXSSUtils.antiSqlInject(value);
		}
		return value;
	}
	
	

	


	// html 代码用 _antiXSS 验证，普通字段用一下方法验证
}