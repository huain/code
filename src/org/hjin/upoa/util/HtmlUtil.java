package org.hjin.upoa.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 * 
 * @author Administrator
 * 
 * ===Selectorѡ��������
 * tagname: ͨ����ǩ����Ԫ�أ����磺a
 * ns|tag: ͨ����ǩ�������ռ����Ԫ�أ����磺������ fb|name �﷨������ <fb:name> Ԫ��
 * #id: ͨ��ID����Ԫ�أ����磺#logo
 * .class: ͨ��class���Ʋ���Ԫ�أ����磺.masthead
 * [attribute]: �������Բ���Ԫ�أ����磺[href]
 * [^attr]: ����������ǰ׺������Ԫ�أ����磺������[^data-] �����Ҵ���HTML5 Dataset���Ե�Ԫ��
 * [attr=value]: ��������ֵ������Ԫ�أ����磺[width=500]
 * [attr^=value], [attr$=value], [attr*=value]: ����ƥ������ֵ��ͷ����β���������ֵ������Ԫ�أ����磺[href*=/path/]
 * [attr~=regex]: ��������ֵƥ��������ʽ������Ԫ�أ����磺 img[src~=(?i)\.(png|jpe?g)]
 * *: ������Ž�ƥ������Ԫ��
 * ===Selectorѡ�������ʹ��
 * el#id: Ԫ��+ID�����磺 div#logo
 * el.class: Ԫ��+class�����磺 div.masthead
 * el[attr]: Ԫ��+class�����磺 a[href]
 * ������ϣ����磺a[href].highlight
 * ancestor child: ����ĳ��Ԫ������Ԫ�أ����磺������.body p ������"body"Ԫ���µ����� pԪ��
 * parent > child: ����ĳ����Ԫ���µ�ֱ����Ԫ�أ����磺������div.content > p ���� p Ԫ�أ�Ҳ������body > * ����body��ǩ������ֱ����Ԫ��
 * siblingA + siblingB: ������AԪ��֮ǰ��һ��ͬ��Ԫ��B�����磺div.head + div
 * siblingA ~ siblingX: ����AԪ��֮ǰ��ͬ��XԪ�أ����磺h1 ~ p
 * el, el, el:���ѡ������ϣ�����ƥ����һѡ������ΨһԪ�أ����磺div.masthead, div.logo
 * ===αѡ����selectors
 * :lt(n): ������ЩԪ�ص�ͬ������ֵ������λ����DOM��������������ĸ��ڵ㣩С��n�����磺td:lt(3) ��ʾС�����е�Ԫ��
 * :gt(n):������ЩԪ�ص�ͬ������ֵ����n�����磺 div p:gt(2)��ʾ��Щdiv���а���2�����ϵ�pԪ��
 * :eq(n): ������ЩԪ�ص�ͬ������ֵ��n��ȣ����磺form input:eq(1)��ʾ����һ��input��ǩ��FormԪ��
 * :has(seletor): ����ƥ��ѡ��������Ԫ�ص�Ԫ�أ����磺div:has(p)��ʾ��Щdiv������pԪ��
 * :not(selector): ������ѡ������ƥ���Ԫ�أ����磺 div:not(.logo) ��ʾ������ class=logo Ԫ�ص����� div �б�
 * :contains(text): ���Ұ��������ı���Ԫ�أ����������ִ�д�����磺 p:contains(jsoup)
 * :containsOwn(text): ����ֱ�Ӱ��������ı���Ԫ��
 * :matches(regex): ������ЩԪ�ص��ı�ƥ��ָ����������ʽ�����磺div:matches((?i)login)
 * :matchesOwn(regex): ������������ı�ƥ��ָ��������ʽ��Ԫ��
 * ===ע�⣺����αѡ���������Ǵ�0��ʼ�ģ�Ҳ����˵��һ��Ԫ������ֵΪ0���ڶ���Ԫ��indexΪ1��
 *
 */
public class HtmlUtil {

	
	public static HtmlUtil htmlUtil;
	
	private Document doc;
	
	private HtmlUtil(){}
	
	private HtmlUtil(String html){
		this.doc =  Jsoup.parse(html);
	}
	
	public static HtmlUtil getInstance(String html){
		if(null == htmlUtil){
			htmlUtil = new HtmlUtil(html);
		}
		return htmlUtil;
	}
	

}
