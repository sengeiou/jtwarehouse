package com.wing.socialcontact.front.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wing.socialcontact.common.action.BaseAction;
import com.wing.socialcontact.common.model.Member;
import com.wing.socialcontact.front.util.ApplicationPath;
import com.wing.socialcontact.service.wx.api.ILibraryClassService;
import com.wing.socialcontact.service.wx.api.ILibraryLiveService;
import com.wing.socialcontact.service.wx.api.ILibraryOpLogService;
import com.wing.socialcontact.service.wx.api.ILibraryService;
import com.wing.socialcontact.service.wx.api.IMeetingService;
import com.wing.socialcontact.service.wx.api.IMessageInfoService;
import com.wing.socialcontact.service.wx.api.IMyCollectionService;
import com.wing.socialcontact.service.wx.api.ISysconfigService;
import com.wing.socialcontact.service.wx.api.ITjyUserService;
import com.wing.socialcontact.service.wx.api.IWalletLogService;
import com.wing.socialcontact.service.wx.api.IWxUserService;
import com.wing.socialcontact.service.wx.api.IliveSignupService;
import com.wing.socialcontact.service.wx.bean.DynamicOpLog;
import com.wing.socialcontact.service.wx.bean.LibraryOpLog;
import com.wing.socialcontact.service.wx.bean.MessageInfo;
import com.wing.socialcontact.service.wx.bean.TjyLibrary;
import com.wing.socialcontact.service.wx.bean.TjyLibraryClass;
import com.wing.socialcontact.service.wx.bean.TjyLibraryLive;
import com.wing.socialcontact.service.wx.bean.TjyLiveSignup;
import com.wing.socialcontact.service.wx.bean.TjyUser;
import com.wing.socialcontact.service.wx.bean.Topic;
import com.wing.socialcontact.service.wx.bean.WalletLog;
import com.wing.socialcontact.service.wx.bean.WxUser;
import com.wing.socialcontact.util.AldyMessageUtil;
import com.wing.socialcontact.util.CommUtil;
import com.wing.socialcontact.util.Constants;
import com.wing.socialcontact.util.DateUtils;
import com.wing.socialcontact.util.DoubleUtil;
import com.wing.socialcontact.util.MD5UtilWx;
import com.wing.socialcontact.util.ServletUtil;
import com.wing.socialcontact.util.UUIDGenerator;
import com.wing.socialcontact.util.WxMsmUtil;
import com.wing.socialcontact.vhall.api.BaseAPI;

import tk.mybatis.mapper.util.StringUtil;

/**
 * 
 * @author zhangzheng ?????????????????????
 */
@Controller
@RequestMapping("/library/m")
public class LibraryMoAction extends BaseAction {

	@Autowired
	private ILibraryClassService libraryclassservice;
	@Autowired
	private ILibraryService libraryService;
	@Autowired
	private IMyCollectionService myCollectionService;
	@Autowired
	private ITjyUserService tjyUserService;
	@Autowired
	private ISysconfigService sysconfigService;
	
	@Autowired
	private ILibraryLiveService libraryLiveService;
	@Autowired
	private IliveSignupService liveSignupService;
	
	@Autowired
	private IMeetingService meetingService;
	@Autowired
	private IWxUserService wxUserService;

	@Autowired
	private IWalletLogService walletLogService;
	@Autowired
	private IMessageInfoService messageInfoService;
	@Autowired
	private ILibraryOpLogService libraryOpLogService;
	

	/**
	 * ????????????????????????????????????
	 */
	@RequestMapping(value = "/recommendclass")
	public @ResponseBody Map recommendclass(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		List<Map> ylist = libraryclassservice.recommendclass();
		return super.getSuccessAjaxResult("???????????????", ylist);

	}

	/**
	 * ????????????????????????????????????
	 */
	@RequestMapping(value = "/classList")
	public @ResponseBody Map classList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		List<Map> res = new ArrayList<Map>();
		/**
		 * ???????????????
		 */
		List<Map> ylist = libraryclassservice.selectonelevelclass();
		if (ylist.size() > 0) {
			for (Map cls : ylist) {
				/**
				 * ?????????????????????????????????
				 */
				String id = (String) cls.get("id");
				List<Map> elist = libraryclassservice.querybyparent(id);
				cls.put("son", elist);
				res.add(cls);
			}
		}
		return super.getSuccessAjaxResult("???????????????", res);

	}

	/**
	 * ????????????????????????????????????
	 */
	@RequestMapping(value = "/onelevelclass")
	public @ResponseBody Map onelevelclass(HttpServletRequest request, HttpServletResponse response, Integer position)
			throws IOException {
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}

		/**
		 * ???????????????
		 */
		List<Map> res = libraryclassservice.onelevelclass(position);
		return super.getSuccessAjaxResult("???????????????", res);

	}

	/**
	 * ?????????????????????
	 */
	@RequestMapping(value = "/list")
	public @ResponseBody Map libraryList(HttpServletRequest request, HttpServletResponse response, String classid,
			Integer page, Integer size, Integer today, String key, Integer readtimes) throws IOException {
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}

		if (!StringUtils.isEmpty(key)) {
			/**
			 * post??????????????????
			 */
			// key = new String(key.getBytes("ISO-8859-1"), "utf-8");
		}
		if (StringUtils.isEmpty(page) || page < 1) {
			page = 1;
		}
		if (StringUtils.isEmpty(size) || size < 1) {
			size = 10;
		}
		/**
		 * today ??????0???????????????
		 */
		if (StringUtils.isEmpty(today)) {
			today = 0;
		}
		if (StringUtils.isEmpty(readtimes)) {
			readtimes = 0;
		}

		// List<Map> res= new ArrayList<Map>();
		// res = libraryService.getTjyLibraryByclassid(classid);
		List<Map> res1 = new ArrayList<Map>();
		res1 = libraryService.getTjyLibraryByTerm(classid, page, size, today, key, readtimes);

		if (res1.size() == 0) {
			res1 = libraryService.getLibraryByoneLevel(classid, page, size, today, key, readtimes);
		}

		return super.getSuccessAjaxResult(Constants.AJAX_MSG_SUCCESS, res1);
	}

	/**
	 * ????????????id????????????
	 */
	@RequestMapping(value = "/selbyonelevelid")
	public @ResponseBody Map selbyonelevelid(HttpServletRequest request, HttpServletResponse response, String classid)
			throws IOException {
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}

		List<Map> res = libraryService.selbyonelevelid(classid);

		return super.getSuccessAjaxResult(Constants.AJAX_MSG_SUCCESS, res);
	}

	/**
	 * ?????????????????????
	 */
	@RequestMapping(value = "librarydetail")
	public String librarydetail(HttpServletRequest request, String id, ModelMap map, Integer type) {
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			type = 2;
		}
		map.addAttribute("id", id);
		map.addAttribute("type", type);
		String path = request.getContextPath();
		map.addAttribute("web_site", ApplicationPath.getParameter("domain") + path);
		return "wisdomGroup/textDesign-detail";
	}

	/**
	 * ??????????????????
	 */
	@RequestMapping(value = "/detail")
	public @ResponseBody Map libraryDetail(HttpServletRequest request, HttpServletResponse response, String libraryid)
			throws IOException {
		Member me = (Member) request.getSession().getAttribute("me");

		/*
		 * if (StringUtils.isEmpty(userId)) { return super.getAjaxResult("302",
		 * "??????????????????????????????", null); }
		 */
		if (StringUtils.isEmpty(libraryid)) {
			return super.getAjaxResult("999", "????????????", null);
		}

		TjyUser tjyUser = null;
		if (me != null) {
			tjyUser = tjyUserService.selectByPrimaryKey(me.getId());
		}
		Map res = new HashMap();
		res = libraryService.getLibraryByid(libraryid);
		boolean iscol = false;
		if (null != me) {
			String userId = me.getId();
			iscol = myCollectionService.iscollected(userId, libraryid, 1);
		}
		// ????????????
		res.put("iscollection", iscol);

		Map<String, Object> signMap = null;
		String roomId = "";
		if (me != null && res != null) {
			roomId = res.get("webinarId") == null ? "" : res.get("webinarId").toString();
			if (roomId != null && !"".equals(roomId)) {
				signMap = BaseAPI.createVedioSign(tjyUser.getId(), tjyUser.getNickname(), roomId);
			}
		}
		int rewardCount = 0;//????????????
		int tpCount = 0;//????????????
		boolean like_flag = false;//????????????????????????
		rewardCount = walletLogService.selectRewardSum("17", libraryid);
		tpCount = libraryOpLogService.getCountByFkIdAndType(libraryid, "1", "1");
		LibraryOpLog opLog = new LibraryOpLog();
		opLog.setUserId(me.getId());
		opLog.setOpType(1);
		opLog.setType(1);
		opLog.setFkId(libraryid);
		List llist = libraryOpLogService.selectAllOpLog(opLog);
		if(llist!=null&&llist.size()>0){
			like_flag = true;
		}
		res.put("signObj", signMap);
		res.put("rewardCount", rewardCount);
		res.put("tpCount", tpCount);
		res.put("like_flag", like_flag);
		/**
		 * ??????????????????
		 */
		libraryService.addreadtimes(libraryid);
		return super.getSuccessAjaxResult("???????????????", res);
	}
	
	@RequestMapping(value = "/all-classes")
	public String allclasses(ModelMap map, String id,int level){
		map.addAttribute("classid", id);
		map.addAttribute("level", level);
		return "wisdomGroup/all-classes";
	}
	@RequestMapping(value = "/dynamiczf")
	public String zhuanfa(ModelMap map, String id,String fromDynamicId,int type){
		TjyLibrary res = libraryService.getTjyLibraryByid(id);
		TjyLibraryClass  cla = libraryclassservice.getTjyLibraryClassByid(res.getOneclass());
		map.addAttribute("obj", res);
		map.addAttribute("classobj", cla);
		map.addAttribute("type", type);
		map.addAttribute("fromDynamicId", fromDynamicId);
		return "wisdomGroup/dynamiczf";
	}
	
	/*---------------------------------------------------???????????????---------------------------------------------------------------*/
	
	/**
	 * ???????????????????????????
	 * 
	 * @param type  1???????????????   2???????????????   3??????????????????  4??????????????????
	 */
	 @RequestMapping("live/listPage")
	 public String listPage(ModelMap map, Integer type){
		 if(type==1){
			 return "wisdomGroup/jqjhlists";
		 }else if(type==2){
			 return "wisdomGroup/zttxlists";
		 }else if(type==3){
			 return "wisdomGroup/zbxlists";
		 }else if(type==4){
			 return "wisdomGroup/dshlists";
		 }
		return "wisdomGroup/jqjhlists";
	}
	
	/**
	 * ????????????????????????????????????   ??????    ????????????   ?????? 
	 * @param type
	 * @param page
	 * @param size
	 * @param key
	 */
	@RequestMapping(value = "live/list")
	public @ResponseBody Map libraryliveList(HttpServletRequest request, HttpServletResponse response, Integer type,
			Integer page, Integer size,  String key) throws IOException {
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		if (StringUtils.isEmpty(page) || page < 1) {
			page = 1;
		}
		if (StringUtils.isEmpty(size) || size < 1) {
			size = 10;
		}
		List<Map> res = new ArrayList<Map>();
		res = libraryLiveService.libraryLiveList(page, size, type, key);
		return super.getSuccessAjaxResult(Constants.AJAX_MSG_SUCCESS, res);
	}
	
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping("live/detail")
	public String detailLive(HttpServletRequest request,ModelMap res,String id){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return "login";
		}
		TjyUser user=tjyUserService.selectByPrimaryKey(userId);
		if(!"1".equals(user.getIsRealname()+"")){
			return "commons/to_recon";
		}
		TjyLibraryLive t = libraryLiveService.getLibraryLive(id);
		res.put("obj", t);
		//????????????
		int signupStatus=liveSignupService.usersignupedorno(userId, t.getId());
		res.put("signupStatus", signupStatus);
		//????????????
		boolean iscol = false;
		if (null != me) {
			iscol = myCollectionService.iscollected(userId, id, 4);
		}
		// ????????????
		if(iscol){
			res.put("iscollection", 1);
		}else{
			res.put("iscollection", 0);
		}
		return "wisdomGroup/livedetail";
	}
	
	/**
	 * ??????????????????
	 */
	 @RequestMapping("live/signupPage")
    public String signupPage(HttpServletRequest request,HttpServletResponse response,String id,ModelMap map){
		/**
		 * ??????????????????
		 */ 
		TjyLibraryLive l = libraryLiveService.getLibraryLive(id);
		map.addAttribute("detail", l);
	    /**
	     * ??????????????????
	     */
	    Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return "login";
		}
		TjyUser user=tjyUserService.selectByPrimaryKey(userId);
		/*if(!"1".equals(user.getIsRealname()+"")){
			return "commons/to_recon";
		}*/
		map.addAttribute("user", user);
		WxUser wxUser = wxUserService.selectByPrimaryKey(userId);
		if(wxUser != null){
			map.addAttribute("jbAmount", wxUser.getJbAmount() == null? 0: wxUser.getJbAmount().intValue());
		}
		//????????????
		int signupStatus=liveSignupService.usersignupedorno(userId, id);
		map.put("signupStatus", signupStatus);
        return "wisdomGroup/livesignup";
    }
	 @RequestMapping("live/payorder")
	 public @ResponseBody Map payorder(HttpServletRequest request,HttpServletResponse response,String id){
		 Map res = new HashMap();
		 Member me = ServletUtil.getMember(request);
		 if(null==me){
			 return super.getAjaxResult("302","?????????", null);
		 }
		 String userId = me.getId();
		 if(null==userId){
			 return super.getAjaxResult("302","?????????", null);
		 }
		 TjyUser user=tjyUserService.selectByPrimaryKey(userId);
		 if(null==user){
			 return super.getAjaxResult("302","?????????", null);
		 }
		 Double jbAmount = 0.00;
		 WxUser wxUser = wxUserService.selectByPrimaryKey(userId);
		 if(null==wxUser){
			 return super.getAjaxResult("302","?????????", null);
		 }else{
			 jbAmount = wxUser.getJbAmount();
		 }
		 TjyLibraryLive l = libraryLiveService.getLibraryLive(id);
		 if(null==l){
			 return super.getAjaxResult("999","??????????????????", null);
		 }
		 Integer price = l.getTicketPrice();
		 if(jbAmount<price){
			 return super.getAjaxResult("999","J???????????????", null);
		 }
		 try{
			 /**
			  * ???????????????????????????
			  */
			 int i = liveSignupService.usersignupedorno(userId, id);
			 if(i==1){
				 return super.getAjaxResult("999","????????????", null);
			 };
			 
			 /**
			  * ??????????????????
			  */
			 TjyLiveSignup signup = new  TjyLiveSignup();
			 signup.setAmount(price);
			 signup.setLiveid(id);
			 signup.setUserid(userId);
			 signup.setCreatetime(new Date());
			 signup.setName(user.getNickname());
			 signup.setMobile(user.getMobile());
			 signup.setOrderstatus(1);
			 signup.setPaystatus(2);
			 signup.setIsremind(0);
			 signup.setPaytime(new Date());
			 if(price==0){
				 liveSignupService.addSignup(signup);
				 /**
				  * ?????????
				  */
				 String message =AldyMessageUtil.liveSignupSuccess(wxUser.getNickName(), 
						 DateUtils.dateToString(signup.getCreatetime(), "yyyy-MM-dd HH:mm:ss"), l.getTitle(),
						 DateUtils.dateToString(l.getStartTime(), "yyyy-MM-dd HH:mm:ss")); 
				 sendmessage(userId, message, 2, "LIVE_SIGNUP",null);
				 sendmessage(userId, message, 4, "LIVE_SIGNUP",null);
				//??????${name}???????????????????????????${paytime}????????????${livename}?????????????????????????????????${playtime}????????????????????????
				String content1="{name:\"" + user.getNickname() + "\",paytime:\"" + DateUtils.datetimeToString(signup.getPaytime()) + "\",livename:\"" + l.getTitle() + "\",playtime:\"" + DateUtils.datetimeToString(l.getStartTime()) + "\"}";
				sendmessage(user.getId(),content1,1,AldyMessageUtil.MsmTemplateId.LIVE_SIGNUP,user.getMobile());
				 return super.getSuccessAjaxResult(Constants.AJAX_MSG_SUCCESS, res);
			 }
			 // ??????????????????
			 WalletLog walletLog = new WalletLog();
			 walletLog.setAmount(price.doubleValue());
			 walletLog.setCreateTime(new Date());
			 walletLog.setDeleted("0");
			 walletLog.setPayStatus("1");// ????????????
			 walletLog.setPdType("2");
			 walletLog.setRemark("????????????");
			 walletLog.setType("2");
			 walletLog.setUserId(me.getId());
			 walletLog.setBusinessType(16);
			 String out_trade_no = walletLogService.addWalletLog(walletLog);
			 if (StringUtils.isEmpty(out_trade_no)) {
				 return super.getAjaxResult("999", "????????????", null);
			 }
			 wxUser.setJbAmount(CommUtil.subtract(wxUser.getJbAmount(), walletLog.getAmount()));
			 wxUserService.updateWxUser(wxUser);
			 liveSignupService.addSignup(signup);
			 /**
			  * ?????????
			  */
			//??????${name}???????????????????????????${paytime}????????????${livename}?????????????????????????????????${playtime}????????????????????????
				String content1="{name:\"" + user.getNickname() + "\",paytime:\"" + DateUtils.datetimeToString(signup.getPaytime()) + "\",livename:\"" + l.getTitle() + "\",playtime:\"" + DateUtils.datetimeToString(l.getStartTime()) + "\"}";
				sendmessage(user.getId(),content1,1,AldyMessageUtil.MsmTemplateId.LIVE_SIGNUP,user.getMobile());
			 
			 String message =AldyMessageUtil.liveSignupSuccess(wxUser.getNickName(), 
					 DateUtils.dateToString(signup.getCreatetime(), "yyyy-MM-dd HH:mm"), l.getTitle(),
					 DateUtils.dateToString(l.getStartTime(), "yyyy-MM-dd HH:mm:ss")); 
//			 String message ="???"+AldyMessageUtil.SMSPRE+"????????????"+wxUser.getNickName()+"???"
//					 + "??????"+DateUtils.dateToString(signup.getCreatetime(), "yyyy-MM-dd HH:mm")
//					 +"????????????"+l.getTitle()+"????????????????????????????????????"+wxUser.getNickName()+"????????????"+wxUser.getMobile()+"???"; 
			 sendmessage(userId, message, 2, "LIVE_SIGNUP",null);
			 sendmessage(userId, message, 4, "LIVE_SIGNUP",null);
			 
			 return super.getSuccessAjaxResult(Constants.AJAX_MSG_SUCCESS, res);
		 }catch(Exception e){
			 return super.getAjaxResult("999", "????????????", null);
		 }
	 }
	/**
	 * ???????????????????????????
	 */
	 @RequestMapping("live/signupsuccess")
	 public String signupsuccessPage(){
			return "wisdomGroup/signupsuccess";
	}
	

	 /**
	  * ????????????
	  */
	 
	/**
	 * ????????????????????????
	 */
	 @RequestMapping(value = "live/mysignuplist")
	public @ResponseBody Map mylivelist(HttpServletRequest request, HttpServletResponse response, Integer type,Integer size,Integer page)
			throws IOException {
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		if (StringUtils.isEmpty(size)) {
			size=10;
		}
		if (StringUtils.isEmpty(page)||page==0) {
			page=1;
		}

		List<Map> res = liveSignupService.selectmysignups(userId,type,page,size);
		return super.getSuccessAjaxResult("???????????????", res);

	}
	/**
	 * ??????(??????????????????)
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/liveuseweb")
	public String liveUseWeb(HttpServletRequest request,ModelMap modelMap,String id){
		Member me = ServletUtil.getMember(request);
		if (null == me||me.getId()==null||me.getId().trim().length()==0) {
			return "login";
		}
//		if(!"1".equals(me.getIsRealname())){
//		      return "commons/to_recon";
//		}
		TjyLibraryLive t = libraryLiveService.getLibraryLive(id);
		TjyUser tjyUser = tjyUserService.selectByPrimaryKey(me.getId());
		
		if(t==null){
			modelMap.put("message", "????????????");
			modelMap.put("tips", "??????????????????");
			return "exception";
		}else if(StringUtils.isEmpty(t.getWebinarId())){
			modelMap.put("message", "????????????");
			modelMap.put("tips", "??????????????????");
			return "exception";
		}else if(tjyUser==null){
			modelMap.put("message", "????????????");
			modelMap.put("tips", "??????????????????");
			return "exception";
		}else{
			String openK = ApplicationPath.getParameter("vhall_open_k");
			String k = meetingService.createVhallKey(openK,me.getId(), t.getWebinarId());
			try {
				return "redirect:http://e.vhall.com/webinar/inituser/"+t.getWebinarId()+"?"
						+ "email="+MD5UtilWx.MD5Encode(tjyUser.getId(), "UTF-8").toLowerCase()+"@vhall.com&"
						+ "name="+URLEncoder.encode(tjyUser.getNickname(), "UTF-8")+"&"
						+ "k="+k;
			} catch (UnsupportedEncodingException e) {
				return "redirect:http://e.vhall.com/webinar/inituser/"+t.getWebinarId()+"?"
						+ "email="+MD5UtilWx.MD5Encode(tjyUser.getId(), "UTF-8").toLowerCase()+"@vhall.com&"
						+ "k="+k;
			}
		}
		
	}
	
	/**
	 * ?????????????????????
	 */
	 @RequestMapping("live/myfootPrint_live")
	 public String myfootPrintLive(HttpServletRequest request,HttpServletResponse response){
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
			return "mine/myfootPrint_live";
	}
	
	/**
	 * ???????????????  1 ??????  2 ??????    4????????????
	 */
	public void sendmessage(String touser, String content, int type, String templateid,String mobile) {
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.setId(UUIDGenerator.getUUID());
		messageInfo.setDeleted(0);
		messageInfo.setType(type);// ??????
		messageInfo.setToUserId(touser);
		messageInfo.setCreateTime(new Date());
		// ????????????
		if(type==2){
			String con = WxMsmUtil.getTextMessageContent(content);
			messageInfo.setContent(con);
		}else{
			messageInfo.setContent(content);
		}
		messageInfo.setTemplateId(templateid);
		messageInfo.setMobile(mobile);
		messageInfo.setStatus(0);// ?????????
		messageInfo.setWxMsgType(1);///** ?????????????????????1???????????????2?????????????????? */
		messageInfoService.addMessageInfo(messageInfo);
	}
	
	/**
	 * ????????????
	 */
	@RequestMapping("rewardPage")
	public String rewardPage(HttpServletRequest request,ModelMap map,String fkId){
		String userId =  super.checkLogin(request);
		WxUser wxUser = wxUserService.selectByPrimaryKey(userId);
		if(wxUser != null){
			map.addAttribute("jbAmount", wxUser.getJbAmount() == null? 0: wxUser.getJbAmount().intValue());
		}
		map.addAttribute("fkId", fkId);
		return "wisdomGroup/rewardPage";
	}
	/**
	 * ??????j???
	 * @param request
	 * @param response
	 * @param fkId
	 * @param jcount
	 * @return
	 */
	@RequestMapping("rewardJ")
	public @ResponseBody Map rewardJ(HttpServletRequest request,HttpServletResponse response,String fkId,String jcount){
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		if(StringUtil.isEmpty(jcount)){
			return super.getAjaxResult(Constants.AJAX_CODE_ERROR_PARAM, "???????????????????????????", "");
		}else{
			TjyLibrary tl = libraryService.getTjyLibraryByid(fkId);
			//??????????????????j???
			WxUser user = wxUserService.selectById(userId);
			Double doub = DoubleUtil.sub(user.getJbAmount()==null?0:user.getJbAmount(), Double.valueOf(jcount));
			if(doub<0){
				return super.getAjaxResult("501", "J????????????", null);
			}else{
				user.setJbAmount(doub);
				wxUserService.updateWxUser(user);
				WalletLog log = new WalletLog();
				log.setCreateTime(new Date());
				log.setType("2");
				log.setPdType("2");
				log.setUserId(userId);
				log.setAmount(Double.valueOf(jcount));
				log.setRemark("????????????");
				log.setPayStatus("1");
				log.setYeAmount(user.getJbAmount());
				log.setBusinessType(17);
				log.setSourceUser(tl.getCreateUserId());
				log.setFkId(fkId);
				walletLogService.addWalletLog(log);
			}
			//??????????????????????????????j???
			WxUser userf = wxUserService.selectById(tl.getRewardUser());
			userf.setJbAmount(DoubleUtil.add(userf.getJbAmount(), Double.valueOf(jcount)));
			wxUserService.updateWxUser(userf);
			WalletLog logf = new WalletLog();
			logf.setCreateTime(new Date());
			logf.setType("2");
			logf.setPdType("1");
			logf.setUserId(tl.getCreateUserId());
			logf.setAmount(Double.valueOf(jcount));
			logf.setRemark("????????????");
			logf.setPayStatus("1");
			logf.setYeAmount(userf.getJbAmount());
			logf.setBusinessType(15);
			logf.setSourceUser(userId);
			walletLogService.addWalletLog(logf);
		}
		return super.getSuccessAjaxResult();
	}
	
	/**
	 * ??????
	 * 
	 * @return
	 */
	@RequestMapping("thumbup")
	public @ResponseBody Map thumbup(HttpServletRequest request, String id) throws IOException {
		
		String userId =  super.checkLogin(request);
		if(userId == null || "".equals(userId))	{
			return super.getAjaxResult(Constants.AJAX_CODE_ERROR_NOTLOGIN, "??????????????????????????????", "");
		}	
		if (StringUtil.isEmpty(id)) {
			return super.getAjaxResult(Constants.AJAX_CODE_ERROR_PARAM, "????????????",
					null);
		}
		
		LibraryOpLog opLog = new LibraryOpLog();
		opLog.setUserId(userId);
		opLog.setOpType(1);
		opLog.setType(1);
		opLog.setFkId(id);
		List oplogList = libraryOpLogService.selectAllOpLog(opLog);
		
		if (oplogList.size() <= 0) {
			opLog.setId(UUID.randomUUID().toString());
			opLog.setActionTime(new Date());
			libraryOpLogService.addOpLog(opLog);
			return super.getSuccessAjaxResult("0");
		} else {
			Map opLog1 = (Map)oplogList.get(0);
			libraryOpLogService.deleteOpLogById((String)opLog1.get("id"));
			return super.getSuccessAjaxResult("1");
		}
	}
}
