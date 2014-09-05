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
	
	/**登录验证地址1 */
	public static String sUrl_login1 = "http://60.247.77.186/ucas/login";
	/**登录验证地址2 */
	public static String sUrl_login2 = "http://60.247.77.186/ucas/user/auth/generator.htm";
	/**登录验证地址3 */
	public static String sUrl_login3 = "http://60.247.77.186/ucas/user/auth/validator.htm?code=";
	/**登录验证地址4 */
	public static String sUrl_login4 = "http://60.247.77.186/ucas/login;jsessionid=";
	/**登录验证地址5 */
	public static String sUrl_login5 = "http://60.247.77.194/ultrapmo/portal/index.action";
	
	/**登录header参数 */
	public static String sReq_Referer_login4 = "http://60.247.77.186/ucas/login";
	/**登录header参数 */
	public static String sReq_Origin_login4 = "http://60.247.77.186";
	/**登录header参数 */
	public static String sReq_Referer_login5 = "http://60.247.77.186/ucas/login;jsessionid=";

	public static String sHost_login1 = "http://60.247.77.194/ultrapmo/portal/index.action";
	/** 请求cookie中的JessionId*/
	public static String sJessionId;
	
	public static String sLt;
	/**登录后的cookie保持*/
	public static CookieStore sCookieStore = null;
	
	public static String token = null;
	
	public static String sReq_Secretary =  "http://60.247.77.194/ultrapmo/portal/getTaskCountTimer.action";
	
	public static String sReq_Calendar =  "http://60.247.77.194/ultrapmo/portal/getCalendarInfo.action";
	
	public static String sReq_Calendar_Referer = "http://60.247.77.194/ultrapmo/portal/pmo_qiandao.action";
	
	/**新闻中心地址*/
	public static String sReq_NewsList = "http://60.247.77.194/ultrapmo/news/newsDetailList.action";
	/**新闻中心refer地址*/
	public static String sReq_NewsList_Referer = "http://60.247.77.194/ultrapmo/news/newsCenter.action?newstype=1";
	/**新闻中心新闻总条目数*/
	public static int sNews_Sum = 0;
	/**新闻中心新闻总页数*/
	public static int sNews_Page_Sum = 0;
	/**新闻中心新闻当前页码*/
	public static int sNews_Page_Current = 0;
	/**新闻中心新闻详情地址*/
	public static String sReq_NewsInfo = "http://60.247.77.194/ultrapmo/news/newsDetail.action";
	/**新闻中心新闻详情refer地址*/
	public static String sReq_NewsInfo_Referer = "http://60.247.77.194/ultrapmo/news/newsDetailList.action?newstype=1";
	
	/**小字报地址*/
	public static String sReq_SmallNoteList = "http://60.247.77.194/ultrapmo/talk/SmallnoteList.action";
	/**小字报refer地址*/
	public static String sReq_SmallNoteList_Referer = "http://60.247.77.194/ultrapmo/talk/SmallnoteList.action";
	/**小字报总条目数*/
	public static int sSmallNote_Sum = 0;
	/**小字报总页数*/
	public static int sSmallNote_Page_Sum = 0;
	/**小字报当前页码*/
	public static int sSmallNote_Page_Current = 0;
	/**小字报详情地址*/
	public static String sReq_SmallNoteInfo = "http://60.247.77.194/ultrapmo/talk/Showsmallnote.action";
	/**小字报详情refer地址*/
	public static String sReq_SmallNoteInfo_Referer = "http://60.247.77.194/ultrapmo/talk/SmallnoteList.action";
	/**小字报保存地址*/
	public static String sReq_SaveSmallNote = "http://60.247.77.194/ultrapmo/talk/saveSmallnote.action";
	/**小字报保存refer地址*/
	public static String sReq_SaveSmallNote_Referer = "http://60.247.77.194/ultrapmo/talk/Smallnote.action";

	
	/**联系人列表地址*/
	public static String sReq_InfoList = "http://60.247.77.194/ultrapmo/talk/SearchInfoList_New.action";
	/**联系人列表refer地址*/
	public static String sReq_InfoList_Referer = "http://60.247.77.194/ultrapmo/portal/index.action";
	
	/**日报详情列表地址*/
	public static String sReq_DailyList = "http://60.247.77.194/ultrapmo/workDailyPaperInfo/detailWorkDailyList.action";
	/**日报详情列表refer地址*/
	public static String sReq_DailyList_Referer = "http://60.247.77.194/ultrapmo/workDailyPaperInfo/detailWorkDailyList.action";
	/**日报详情总条目数*/
	public static int sDaily_Sum = 0;
	/**日报详情总页数*/
	public static int sDaily_Page_Sum = 0;
	/**日报详情当前页码*/
	public static int sDaily_Page_Current = 0;
	
	/**日报填写：获取项目列表地址*/
	public static String sReq_DailyNewProList = "http://60.247.77.194/ultrapmo/myproject/dailySelectProjectCode.action";
	/**日报填写：获取项目列表refer地址*/
	public static String sReq_DailyNewProList_Referer = "http://60.247.77.194/ultrapmo/ultrawork/dailySelectProjectCodeIframe.jsp";
	/**日报填写：获取一般refer地址*/
	public static String sReq_DailyNewCommon_Referer = "http://60.247.77.194/ultrapmo/workDailyPaperInfo/currentWorkDailyList.action";
	/**日报填写：补填日志时间验证地址*/
	public static String sReq_DailyNew_CheckSupplementHour = "http://60.247.77.194/ultrapmo/myproject/checkSupplementHour.action";
	/**日报填写：填日志时间验证地址*/
	public static String sReq_DailyNew_CheckHour = "http://60.247.77.194/ultrapmo/myproject/checkSupplementHour.action";
	/**日报填写：填日志项目验证地址*/
	public static String sReq_DailyNew_CheckProjectCode = "http://60.247.77.194/ultrapmo/myproject/checkProjectCode.action";
	/**日报填写：补填日志项目验证地址*/
	public static String sReq_DailyNew_PostSupplement = "http://60.247.77.194/ultrapmo/myproject/supplementSaveDaily.action";
	/**日报填写：填日志项目验证地址*/
	public static String sReq_DailyNew_Post = "http://60.247.77.194/ultrapmo/myproject/saveDaily.action";
	/**日报填写：检查日期是否为节假日*/
	public static String sReq_DailyNew_CheckCreateDate = "http://60.247.77.194/ultrapmo/workDailyPaperInfo/isCreateDaily.action";
	/**取得当前在线用户数量*/	
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
