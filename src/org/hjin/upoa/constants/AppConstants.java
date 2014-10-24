package org.hjin.upoa.constants;

import org.apache.http.client.CookieStore;
import org.hjin.upoa.model.Secretary;
import org.hjin.upoa.ui.view.calendar.MyDate;

public class AppConstants {
	
	public static String appname = "UltraOA";
	
	public static String onlinesum = "0";
	
	public static String loginname = "";
	
	public static Secretary sSecretary;
	
	public static MyDate sToday;
	
	public static String sHost_pmo = "http://60.247.77.194";
	
	public static String sHost_pasm = "http://60.247.77.186";
	
	/**��¼��֤��ַ1 */
	public static String sUrl_login1 = "http://60.247.77.186/ucas/login";
	/**��¼��֤��ַ2 */
	public static String sUrl_login2 = "http://60.247.77.186/ucas/user/auth/generator.htm";
	/**��¼��֤��ַ3 */
	public static String sUrl_login3 = "http://60.247.77.186/ucas/user/auth/validator.htm?code=";
	/**��¼��֤��ַ4 */
	public static String sUrl_login4 = "http://60.247.77.186/ucas/login;jsessionid=";
	/**��¼��֤��ַ5 */
	public static String sUrl_login5 = "http://60.247.77.194/ultrapmo/portal/index.action";
	
	/**��¼header���� */
	public static String sReq_Referer_login4 = "http://60.247.77.186/ucas/login";
	/**��¼header���� */
	public static String sReq_Origin_login4 = "http://60.247.77.186";
	/**��¼header���� */
	public static String sReq_Referer_login5 = "http://60.247.77.186/ucas/login;jsessionid=";

	public static String sHost_login1 = "http://60.247.77.194/ultrapmo/portal/index.action";
	/** ����cookie�е�JessionId*/
	public static String sJessionId;
	
	public static String sLt;
	/**��¼���cookie����*/
	public static CookieStore sCookieStore = null;
	
	public static String token = null;
	
	public static String sReq_Secretary =  "http://60.247.77.194/ultrapmo/portal/getTaskCountTimer.action";
	
	public static String sReq_Calendar =  "http://60.247.77.194/ultrapmo/portal/getCalendarInfo.action";
	
	public static String sReq_Calendar_Referer = "http://60.247.77.194/ultrapmo/portal/pmo_qiandao.action";
	
	/**�������ĵ�ַ*/
	public static String sReq_NewsList = "http://60.247.77.194/ultrapmo/news/newsDetailList.action";
	/**��������refer��ַ*/
	public static String sReq_NewsList_Referer = "http://60.247.77.194/ultrapmo/news/newsCenter.action?newstype=1";
	/**����������������Ŀ��*/
	public static int sNews_Sum = 0;
	/**��������������ҳ��*/
	public static int sNews_Page_Sum = 0;
	/**�����������ŵ�ǰҳ��*/
	public static int sNews_Page_Current = 0;
	/**�����������������ַ*/
	public static String sReq_NewsInfo = "http://60.247.77.194/ultrapmo/news/newsDetail.action";
	/**����������������refer��ַ*/
	public static String sReq_NewsInfo_Referer = "http://60.247.77.194/ultrapmo/news/newsDetailList.action?newstype=1";
	
	/**С�ֱ���ַ*/
	public static String sReq_SmallNoteList = "http://60.247.77.194/ultrapmo/talk/SmallnoteList.action";
	/**С�ֱ�refer��ַ*/
	public static String sReq_SmallNoteList_Referer = "http://60.247.77.194/ultrapmo/talk/SmallnoteList.action";
	/**С�ֱ�����Ŀ��*/
	public static int sSmallNote_Sum = 0;
	/**С�ֱ���ҳ��*/
	public static int sSmallNote_Page_Sum = 0;
	/**С�ֱ���ǰҳ��*/
	public static int sSmallNote_Page_Current = 0;
	/**С�ֱ������ַ*/
	public static String sReq_SmallNoteInfo = "http://60.247.77.194/ultrapmo/talk/Showsmallnote.action";
	/**С�ֱ�����refer��ַ*/
	public static String sReq_SmallNoteInfo_Referer = "http://60.247.77.194/ultrapmo/talk/SmallnoteList.action";
	/**С�ֱ������ַ*/
	public static String sReq_SaveSmallNote = "http://60.247.77.194/ultrapmo/talk/saveSmallnote.action";
	/**С�ֱ�����refer��ַ*/
	public static String sReq_SaveSmallNote_Referer = "http://60.247.77.194/ultrapmo/talk/Smallnote.action";

	
	/**��ϵ���б��ַ*/
	public static String sReq_InfoList = "http://60.247.77.194/ultrapmo/talk/SearchInfoList_New.action";
	/**��ϵ���б�refer��ַ*/
	public static String sReq_InfoList_Referer = "http://60.247.77.194/ultrapmo/portal/index.action";
	
	/**�ձ������б��ַ*/
	public static String sReq_DailyList = "http://60.247.77.194/ultrapmo/workDailyPaperInfo/detailWorkDailyList.action";
	/**�ձ������б�refer��ַ*/
	public static String sReq_DailyList_Referer = "http://60.247.77.194/ultrapmo/workDailyPaperInfo/detailWorkDailyList.action";
	/**�ձ���������Ŀ��*/
	public static int sDaily_Sum = 0;
	/**�ձ�������ҳ��*/
	public static int sDaily_Page_Sum = 0;
	/**�ձ����鵱ǰҳ��*/
	public static int sDaily_Page_Current = 0;
	
	/**�ձ���д����ȡ��Ŀ�б��ַ*/
	public static String sReq_DailyNewProList = "http://60.247.77.194/ultrapmo/myproject/dailySelectProjectCode.action";
	/**�ձ���д����ȡ��Ŀ�б�refer��ַ*/
	public static String sReq_DailyNewProList_Referer = "http://60.247.77.194/ultrapmo/ultrawork/dailySelectProjectCodeIframe.jsp";
	/**�ձ���д����ȡһ��refer��ַ*/
	public static String sReq_DailyNewCommon_Referer = "http://60.247.77.194/ultrapmo/workDailyPaperInfo/currentWorkDailyList.action";
	/**�ձ���д��������־ʱ����֤��ַ*/
	public static String sReq_DailyNew_CheckSupplementHour = "http://60.247.77.194/ultrapmo/myproject/checkSupplementHour.action";
	/**�ձ���д������־ʱ����֤��ַ*/
	public static String sReq_DailyNew_CheckHour = "http://60.247.77.194/ultrapmo/myproject/checkSupplementHour.action";
	/**�ձ���д������־��Ŀ��֤��ַ*/
	public static String sReq_DailyNew_CheckProjectCode = "http://60.247.77.194/ultrapmo/myproject/checkProjectCode.action";
	/**�ձ���д��������־��Ŀ��֤��ַ*/
	public static String sReq_DailyNew_PostSupplement = "http://60.247.77.194/ultrapmo/myproject/supplementSaveDaily.action";
	/**�ձ���д������־��Ŀ��֤��ַ*/
	public static String sReq_DailyNew_Post = "http://60.247.77.194/ultrapmo/myproject/saveDaily.action";
	/**�ձ���д����������Ƿ�Ϊ�ڼ���*/
	public static String sReq_DailyNew_CheckCreateDate = "http://60.247.77.194/ultrapmo/workDailyPaperInfo/isCreateDaily.action";
	/**ȡ�õ�ǰ�����û�����*/	
	public static String sReq_GetOnLine = "http://60.247.77.194/ultrapmo/portal/getUserOnlineCount.action";
	
	public static String sReq_GetOnLine_Referer = "http://60.247.77.194/ultrapmo/portal/index.action";
	
	public static String sReq_IndexUserInfo = "http://60.247.77.194/ultrapmo/kmArchivesInfo/getUserInfo.action";
	
	public static String sReq_IndexUserInfo_Referer = "http://60.247.77.194/ultrapmo/kmArchivesInfo/getUserInfo.action";
	
	public static String sReq_IndexUserHeaderInfo = "http://60.247.77.186/pasm/userHead/";
	
	public static String sReq_IndexOnLineSum = "http://60.247.77.194/ultrapmo/portal/getUserOnlineCount.action";
	
	public static String sReq_IndexOnLineSum_Referer = "http://60.247.77.194/ultrapmo/portal/index.action";
	
	public static String sReq_IndexWaitDealSum = "http://60.247.77.194/ultrapmo/portal/getTaskCountTimer.action";
	
	public static String sReq_IndexWaitDealSum_Referer = "http://60.247.77.194/ultrapmo/portal/index.action";
	
	public static String sReq_IndexDailyInfo = "http://60.247.77.194/ultrapmo/workDailyPaperInfo/historyWorkDailyList.action";
	
	public static String sReq_IndexDailyInfo_Referer = "http://60.247.77.194/ultrapmo/portal/pmo_workContent.action?menuid=4028ea194546091401454a97c487000d";
	
	public static String sReq_IndexWaitDealInfo = "http://60.247.77.194/ultrapmo/requirementCheck/wraitForMakeList.action";
	
	public static String sReq_IndexWaitDealInfo_Referer = "http://60.247.77.194/ultrapmo/portal/pmo_workContent.action?menuid=4028ea194546091401454a97c487000d";
	

}
