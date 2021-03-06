package com.wing.socialcontact.front.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wing.socialcontact.common.action.BaseAction;
import com.wing.socialcontact.common.model.DataGrid;
import com.wing.socialcontact.common.model.Member;
import com.wing.socialcontact.common.model.PageParam;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wing.socialcontact.config.OssConfig;
import com.wing.socialcontact.service.wx.api.IMyCollectionService;
import com.wing.socialcontact.service.wx.api.INewsClassService;
import com.wing.socialcontact.service.wx.api.INewsPayLogService;
import com.wing.socialcontact.service.wx.api.INewsService;
import com.wing.socialcontact.service.wx.api.ITjyUserService;
import com.wing.socialcontact.service.wx.api.IUserIntegralLogService;
import com.wing.socialcontact.service.wx.api.IWalletLogService;
import com.wing.socialcontact.service.wx.api.IWxUserService;
import com.wing.socialcontact.service.wx.bean.NewsPayLog;
import com.wing.socialcontact.service.wx.bean.TjyNews;
import com.wing.socialcontact.service.wx.bean.TjyNewsClass;
import com.wing.socialcontact.service.wx.bean.TjyUser;
import com.wing.socialcontact.service.wx.bean.WalletLog;
import com.wing.socialcontact.service.wx.bean.WxUser;
import com.wing.socialcontact.sys.service.IListValuesService;
import com.wing.socialcontact.util.Constants;
import com.wing.socialcontact.util.DoubleUtil;
import com.wing.socialcontact.util.ServletUtil;
import com.wing.socialcontact.util.SpringContextUtil;
import com.wing.socialcontact.vhall.api.BaseAPI;

import tk.mybatis.mapper.util.StringUtil;

/**
 * ????????????
 * @author zhangfan
 *
 */
@Controller
@RequestMapping("/m/news")
public class NewsAction extends BaseAction{
	
	@Autowired
	private INewsClassService newsClassService; 			
	@Autowired
	private INewsService newsService; 
	@Autowired
	private IListValuesService listValuesService; 
	@Autowired
	private ITjyUserService tjyUserService;
	@Autowired
	private IWxUserService wxUserService;
	@Autowired
	private INewsPayLogService newsPayLogService;
	@Autowired
	private IWalletLogService walletLogService;
	@Autowired
	private IUserIntegralLogService userIntegralLogService;
	@Autowired
	private IMyCollectionService myCollectionService;
	/**
	 * ????????????
	 * 
	 * @return
	 */
	@RequestMapping("newsPage")
	public String newsPage(ModelMap map){
		map.put("bannerid", Constants.BANNER_NEWS_ID);
		return "news/indexPage";
	}
	@RequestMapping("selClassList")
	public @ResponseBody Map selClassList(){
		//??????
		List classList = newsClassService.selectFrontClass(Constants.NEWS_CLASS_ID);
		Map res = new HashMap();
		res.put("classList", classList);
		return super.getSuccessAjaxResult("???????????????", res);
	}
	
	@RequestMapping("selNewsList")
	public @ResponseBody Map selNewsList(String types,String newsTitle,Integer page,Integer size){
		Map res = new HashMap();
		if (page==null||page<1) {
			page = 1;
		}
		if (size==null||size<1) {
			size = 10;
		}
		//??????
		List list = newsService.selectByParamFront(null,newsTitle,page,size,null,1);
		res.put("list", list);
		//?????????????????????
		List blist = newsService.selectByParamFront(null,newsTitle,1,3,null,2);
		res.put("blist", blist);
		return super.getSuccessAjaxResult("???????????????", res);
	}
	/**
	 * ?????????
	 * @return
	 */
	@RequestMapping("detailPage")
	public String detailPage(HttpServletRequest request,HttpServletResponse response,ModelMap map,String id){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return "login";
		}
		map.addAttribute("id", id);
		return "news/newsDetail";
	}
	/**
	 * ????????????
	 * @param types
	 * @return
	 */
	@RequestMapping("detail")
	public @ResponseBody Map detail(HttpServletRequest request,HttpServletResponse response,String id){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		//????????????????????????
		userIntegralLogService.addLntAndEmp(userId, "task_0003");
				
		TjyUser tjyUser = null;
		if(me!=null){
			tjyUser = tjyUserService.selectByPrimaryKey(me.getId());
		}
		Map res = new HashMap();
		//??????????????????
		TjyNews tjyNews = newsService.selectById(id);
		tjyNews.setLookCount(tjyNews.getLookCount()+1);
		newsService.updateNews(tjyNews);
		Map<String, Object> news = newsService.selectNewsById(id);
		OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();
		Map<String, Object> signMap = null;
		String roomId = "";
		if(me!=null&&news!=null){
			roomId = news.get("webinarId")==null?"":news.get("webinarId").toString();
			if(roomId!=null&&!"".equals(roomId)){
				signMap = BaseAPI.createVedioSign(tjyUser.getId(),tjyUser.getNickname(), roomId);
			}
		}
		res.put("signObj", signMap);
		res.put("ossurl", ossurl);
		res.put("news", news);
		return super.getSuccessAjaxResult("???????????????", res);
	}
	/**
	 * ?????????????????????????????????
	 * @param type 1:??????   2 ????????????
	 * @return
	 */
	@RequestMapping("configdetail")
	public @ResponseBody Map configdetail(HttpServletRequest request,HttpServletResponse response,Integer type){
		String id=Constants.NEWS_CLASS_ID_TIANJIU;
		if(1==type){
			id=Constants.NEWS_CLASS_ID_SHARE;
		}
		Map res = new HashMap();
		Map<String, Object> news = newsService.selectNewsById(id);
		OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();
		res.put("ossurl", ossurl);
		res.put("news", news);
		return super.getSuccessAjaxResult("???????????????", res);
	}
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping("htmlDetailPage")
	public String htmlDetailPage(ModelMap map,String id){
		map.addAttribute("id", id);
		return "news/htmlDetail";
	}
	/**
	 * ??????????????????
	 * @param types
	 * @return
	 */
	@RequestMapping("htmlDetail")
	public @ResponseBody Map htmlDetail(String id){
		Map res = new HashMap();
		Map<String, Object> news = newsService.selectNewsById(id);
		res.put("news", news);
		return super.getSuccessAjaxResult("???????????????", res);
	}
	
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping("viewPage")
	public String viewPage(ModelMap map){
		return "news/viewList";
	}
	@RequestMapping("selViewList")
	public @ResponseBody Map selViewList(Integer page,Integer size){
		if (page==null||page<1) {
			page = 1;
		}
		if (size==null||size<1) {
			size = 10;
		}
		Map res = new HashMap();
		List list = newsService.selectViewNews(Constants.NEWS_CLASS_ID_VIEW,page,size);
		OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();
		res.put("ossurl", ossurl);
		res.put("list", list);
		return super.getSuccessAjaxResult("???????????????", res);
	}
	/**
	 * ????????????????????????
	 * @return
	 */
	@RequestMapping("viewDetailPage")
	public String viewDetailPage(HttpServletRequest request,HttpServletResponse response,ModelMap map,String id){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return "login";
		}
		TjyUser tjyUser=tjyUserService.selectByPrimaryKey(userId);
		if(!"1".equals(tjyUser.getIsRealname()+"")){
			return "commons/to_recon";
		}
		map.addAttribute("id", id);
		return "news/viewDetail";
	}
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping("selNewViewList")
	public @ResponseBody Map selNewViewList(){
		Map res = new HashMap();
		TjyNews news = new TjyNews();
		news.setClassRoot(Constants.NEWS_CLASS_ID_VIEW);
		news.setIsRecommend(1);
		PageParam param = new PageParam();
		param.setRows(2);
		param.setPage(1);
		DataGrid dg = newsService.selectAllNews(param, news, null, null);
		List list = dg.getRows();
		OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();
		res.put("ossurl", ossurl);
		res.put("list", list);
		return super.getSuccessAjaxResult("???????????????", res);
	}
	/**
	 * ????????????
	 * @param types
	 * @return
	 */
	@RequestMapping("viewDetail")
	public @ResponseBody Map viewDetail(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,String id){
		Map res = new HashMap();
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		TjyUser tjyUser = null;
		if(me!=null){
			tjyUser = tjyUserService.selectByPrimaryKey(me.getId());
		}
		Map<String, Object> news = newsService.selectNewsById(id);
		Map<String, Object> signMap = null;
		String roomId = "";
		if(me!=null&&news!=null){
			roomId = news.get("webinarId")==null?"":news.get("webinarId").toString();
			if(roomId!=null&&!"".equals(roomId)){
				signMap = BaseAPI.createVedioSign(tjyUser.getId(),tjyUser.getNickname(), roomId);
			}
		}
		OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();
		res.put("ossurl", ossurl);
		res.put("news", news);
		res.put("signObj", signMap);
		return super.getSuccessAjaxResult("???????????????", res);
	}
	/**
	 * ????????????????????????
	 * @return
	 */
	@RequestMapping("selNewsIndex")
	public @ResponseBody Map selNewsIndex(){
		Map res = new HashMap();
		List list = newsService.selectHotNews();
		res.put("list", list);
		//????????????
		List valueList = listValuesService.selectListByType(801);
		String timeSpan = "";
		if(valueList!=null&&valueList.size()>0){
			Map map = (Map) valueList.get(0);
			timeSpan = map.get("listValue")==null?"":map.get("listValue").toString();
		}
		res.put("timeSpan", timeSpan);
		return super.getSuccessAjaxResult("???????????????", res);
	}
	/**
	 * ??????
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("search")
	public String search(ModelMap modelMap){
		return "news/newssearch";
	}
	@RequestMapping("searchList")
	public String searchList(HttpServletRequest request,ModelMap map,String keywords){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		map.addAttribute("keywords", keywords);
		return "news/searchList";
	}
	/**
	 * ??????
	 * @param types
	 * @param newsTitle
	 * @param page
	 * @param size
	 * @return
	 */
	@RequestMapping("searchNewsList")
	public @ResponseBody Map searchNewsList(String newsTitle){
		List list = newsService.selectByParamFront(null,newsTitle,null,null,Constants.NEWS_CLASS_ID,null);
		return super.getSuccessAjaxResult("???????????????", list);
	}
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping("jflmDetailPage")
	public String jflmDetailPage(ModelMap map,String id){
		map.addAttribute("id", id);
		return "news/jflmDetail";
	}
	/**
	 * ????????????????????????
	 * @return
	 */
	@RequestMapping("jflmDetail")
	public @ResponseBody Map jflmDetail(HttpServletRequest request,String id){
		Map res = new HashMap();
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		WxUser user = wxUserService.selectById(userId);
		res.put("isAppLogin", user.getIsAppLogin());
		Map<String, Object> news = newsService.selectNewsById(id);
		res.put("news", news);
		return super.getSuccessAjaxResult("???????????????", res);
	}
	
	/**
	 * ???????????????
	 * @return
	 */
	@RequestMapping("hotDetailPage")
	public String hotDetailPage(ModelMap map,String id){
		map.addAttribute("id", id);
		return "news/hotDetail";
	}
	/**
	 * ??????
	 * @param types
	 * @return
	 */
	@RequestMapping("hotDetail")
	public @ResponseBody Map hotDetail(HttpServletRequest request,HttpServletResponse response,String id){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		TjyUser tjyUser = null;
		if(me!=null){
			tjyUser = tjyUserService.selectByPrimaryKey(me.getId());
		}
		//??????????????????
		TjyNews tjyNews = newsService.selectById(id);
		tjyNews.setLookCount(tjyNews.getLookCount()==null?0:tjyNews.getLookCount()+1);
		newsService.updateNews(tjyNews);
		Map res = new HashMap();
		TjyNews news = newsService.selectById(id);
		OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();
		Map<String, Object> signMap = null;
		String roomId = "";
		if(me!=null&&news!=null){
			roomId = news.getWebinarId()==null?"":news.getWebinarId();
			if(roomId!=null&&!"".equals(roomId)){
				signMap = BaseAPI.createVedioSign(tjyUser.getId(),tjyUser.getNickname(), roomId);
			}
		}
		res.put("signObj", signMap);
		res.put("ossurl", ossurl);
		res.put("news", news);
		return super.getSuccessAjaxResult("???????????????", res);
	}
	/**
	 * ??????j???
	 * @param request
	 * @param response
	 * @param fkId
	 * @param jcount
	 * @return
	 */
	@RequestMapping("rewardJb")
	public @ResponseBody Map rewardJb(HttpServletRequest request,HttpServletResponse response,String fkId,String jcount){
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		WxUser user = wxUserService.selectById(userId);
		if(StringUtil.isEmpty(jcount) || StringUtils.isEmpty(fkId)){
			return super.getAjaxResult(Constants.AJAX_CODE_ERROR_PARAM, "???????????????????????????", "");
		}else{
			//??????????????????j???
			Double doub = DoubleUtil.sub(user.getJbAmount()==null?0:user.getJbAmount(), Double.valueOf(jcount));
			if(doub<0){
				return super.getAjaxResult("501", "J???????????????????????????????????????", null);
			}else{
				user.setJbAmount(doub);
				wxUserService.updateWxUser(user);
				NewsPayLog npl = new NewsPayLog();
				npl.setFkId(fkId);
				npl.setUserId(userId);
				npl.setCreateTime(new Date());
				npl.setPayAmount(Long.valueOf(jcount));
				npl.setStatus(1);
				newsPayLogService.addNewsPayLog(npl);
				WalletLog log = new WalletLog();
				log.setCreateTime(new Date());
				log.setType("2");
				log.setPdType("2");
				log.setPayStatus("1");
				log.setUserId(userId);
				log.setAmount(Double.valueOf(jcount));
				log.setRemark("??????????????????");
				log.setYeAmount(doub);
				log.setBusinessType(5);
				walletLogService.addWalletLog(log);
			}
			return super.getSuccessAjaxResult();
		}
		
	}
	/**
	 * ????????????
	 * 
	 * @return
	 */
	@RequestMapping("newsListPage")
	public String newsListPage(ModelMap map,String types){
		map.put("bannerid", Constants.BANNER_NEWS_ID);
		map.put("types", types);
		return "news/newsList";
	}
	@RequestMapping("selNewsListzx")
	public @ResponseBody Map selNewsListzx(String types,String newsTitle,Integer page,Integer size,Integer isHot){
		Map res = new HashMap();
		List<Map<String, Object>> list = new ArrayList();
		if (page==null||page<1) {
			page = 1;
		}
		if (size==null||size<1) {
			size = 10;
		}
		list = newsService.selectByParamFront(types,newsTitle,page,size,null,isHot);
		res.put("list", list);
		return super.getSuccessAjaxResult("???????????????", res);
	}

	/**
	 * ???????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping("bossListPage")
	public String bossListPage(ModelMap map){
		map.put("bannerid", Constants.BANNER_LBXWSFZ_ID);
		return "news/bossList";
	}
	/**
	 * ??????????????????????????????
	 * @return
	 */
	@RequestMapping("summaryPage")
	public String summaryPage(HttpServletRequest request,HttpServletResponse response,ModelMap map,String id){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return "login";
		}
		TjyNews tjyNews = newsService.selectById(id);
		//?????????????????????????????????
		int canlook = 0;
		List list = newsPayLogService.selectPayLog(userId, id);
		if(tjyNews.getIsFree()==2||(list!=null&&list.size()>0&&tjyNews.getIsFree()==1)){//????????????????????????
			canlook = 1;
		}
		boolean iscol = false;
		if (null != me) {
			iscol = myCollectionService.iscollected(userId, id, 3);
		}
		// ????????????
		map.put("iscollection", iscol);
		map.addAttribute("canlook", canlook);
		map.addAttribute("id", id);
		return "news/summaryPage";
	}
	/**
	 * ?????????????????????????????????
	 * @return
	 */
	@RequestMapping("paynewsPage")
	public String paynewsPage(HttpServletRequest request,HttpServletResponse response,ModelMap map,String id){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return "login";
		}
		WxUser wxUser = wxUserService.selectByPrimaryKey(userId);
		TjyNews tjyNews = newsService.selectById(id);
		map.addAttribute("tjyNews", tjyNews);
		if(wxUser != null){
			map.addAttribute("jbAmount", wxUser.getJbAmount() == null? 0: wxUser.getJbAmount().intValue());
		}
		return "news/paynewsPage";
	}
	/**
	 * ??????????????????????????????
	 * @return
	 */
	@RequestMapping("bossdetailPage")
	public String bossdetailPage(HttpServletRequest request,HttpServletResponse response,ModelMap map,String id){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return "login";
		}
		boolean iscol = false;
		if (null != me) {
			iscol = myCollectionService.iscollected(userId, id, 3);
		}
		// ????????????
		map.put("iscollection", iscol);
		map.addAttribute("id", id);
		return "news/sfnewsDetail";
	}
	
	/**
	 * ?????????
	 * @return
	 */
	@RequestMapping("testdetailPage")
	public String testdetailPage(HttpServletRequest request,HttpServletResponse response,ModelMap map,String id){
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			throw new RuntimeException("??????????????????????????????!");
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			throw new RuntimeException("??????????????????????????????!");
		}
		map.addAttribute("id", id);
		return "news/testview";
	}
	/**
	 * ????????????
	 * @param types
	 * @return
	 */
	@RequestMapping("testdetail")
	public @ResponseBody Map testdetail(HttpServletRequest request,HttpServletResponse response,String id){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
				
		TjyUser tjyUser = null;
		if(me!=null){
			tjyUser = tjyUserService.selectByPrimaryKey(me.getId());
		}
		Map res = new HashMap();
		OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();
		Map<String, Object> signMap = null;
		String roomId = "335209044";
		if(me!=null){
			if(roomId!=null&&!"".equals(roomId)){
				signMap = BaseAPI.createVedioSign(tjyUser.getId(),tjyUser.getNickname(), roomId);
			}
		}
		res.put("signObj", signMap);
		res.put("ossurl", ossurl);
		return super.getSuccessAjaxResult("???????????????", res);
	}
}
