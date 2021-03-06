package com.wing.socialcontact.front.action;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.wing.socialcontact.common.action.BaseAction;
import com.wing.socialcontact.common.model.DataGrid;
import com.wing.socialcontact.common.model.Member;
import com.wing.socialcontact.common.model.PageParam;
import com.wing.socialcontact.front.util.ApplicationPath;
import com.wing.socialcontact.service.wx.api.IAttentionService;
import com.wing.socialcontact.service.wx.api.ICouponGenerateService;
import com.wing.socialcontact.service.wx.api.ICouponLogService;
import com.wing.socialcontact.service.wx.api.ICouponService;
import com.wing.socialcontact.service.wx.api.IKeywordsService;
import com.wing.socialcontact.service.wx.api.IMeetingService;
import com.wing.socialcontact.service.wx.api.IMeetingSignupService;
import com.wing.socialcontact.service.wx.api.IMessageInfoService;
import com.wing.socialcontact.service.wx.api.IRefundInstructionService;
import com.wing.socialcontact.service.wx.api.ITjyUserService;
import com.wing.socialcontact.service.wx.api.IWxUserService;
import com.wing.socialcontact.service.wx.bean.Attention;
import com.wing.socialcontact.service.wx.bean.Coupon;
import com.wing.socialcontact.service.wx.bean.CouponGenerate;
import com.wing.socialcontact.service.wx.bean.CouponLog;
import com.wing.socialcontact.service.wx.bean.Keywords;
import com.wing.socialcontact.service.wx.bean.Meeting;
import com.wing.socialcontact.service.wx.bean.MeetingSignup;
import com.wing.socialcontact.service.wx.bean.MeetingSignupRemind;
import com.wing.socialcontact.service.wx.bean.MessageInfo;
import com.wing.socialcontact.service.wx.bean.TjyUser;
import com.wing.socialcontact.sys.bean.ListValues;
import com.wing.socialcontact.sys.bean.SyDistrict;
import com.wing.socialcontact.sys.service.IDistrictService;
import com.wing.socialcontact.sys.service.IListValuesService;
import com.wing.socialcontact.util.AldyMessageUtil;
import com.wing.socialcontact.util.ConfigUtil;
import com.wing.socialcontact.util.DateUtil;
import com.wing.socialcontact.util.DateUtils;
import com.wing.socialcontact.util.MD5UtilWx;
import com.wing.socialcontact.util.PayCommonUtil;
import com.wing.socialcontact.util.ServletUtil;
import com.wing.socialcontact.util.UUIDGenerator;
import com.wing.socialcontact.utils.CtxHolder;
import com.wing.socialcontact.vhall.api.BaseAPI;
import com.wing.socialcontact.vhall.api.WebinarAPI;
import com.wing.socialcontact.wechat.api.PayMchAPI;
import com.wing.socialcontact.wechat.entity.MchOrderquery;
import com.wing.socialcontact.wechat.entity.Unifiedorder;
import com.wing.socialcontact.wechat.resp.MchOrderInfoResult;
import com.wing.socialcontact.wechat.resp.UnifiedorderResult;

/**
 * ?????????-????????????
 */
@Controller
@RequestMapping("")
public class MeetingAction extends BaseAction{
	private static final Logger logger = LoggerFactory.getLogger(MeetingAction.class);
	
	@Autowired
	private IMeetingService meetingService;
	@Autowired
	private IMeetingSignupService meetingSignupService;
	@Autowired
	private ITjyUserService tjyUserService;
	@Autowired
	private IKeywordsService keywordsService;
	@Autowired
	private IAttentionService attentionService;
	@Autowired
	private IWxUserService wxUserService;
	@Autowired
	private IListValuesService listValuesService;
	@Autowired
	private IDistrictService districtService;
	@Autowired
	private IRefundInstructionService refundInstructionService;
	@Autowired
	private IMessageInfoService messageInfoService;
	
	@Autowired
	private ICouponService couponService; 
	@Autowired
	private ICouponGenerateService couponGenerateService; 
	@Autowired
	private ICouponLogService couponLogService;
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping("/m/meeting/index")
	public String meetingload(HttpServletRequest request,ModelMap modelMap){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return "login";
		}
		//?????????????????????
		modelMap.addAttribute("list1", getMeetingAtSignup(1,20,1,1,null).getRows());
		//?????????????????????
		modelMap.addAttribute("list2", getMeetingAtExecuting(1,20,1,1,null).getRows());
		//?????????????????????
		modelMap.addAttribute("list3", getMeetingAtHerald(1,2,1,1,null).getRows());
		//?????????????????????
		modelMap.addAttribute("list4", getMeetingAtEnd(1,20,1,1,null).getRows());
		//????????????????????????
		modelMap.addAttribute("list5", getMeetingAtSignupEnd(1,20,1,1,null).getRows());
		
		return "investment/meeting/index";
	}
	/**
	 * ????????????
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/m/meeting/searchlist")
	public String searchMeeting(HttpServletRequest request,ModelMap modelMap,String keywords){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return "login";
		}
		/*PageParam param3 = new PageParam();
		param3.setRows(10);
		param3.setPage(1);
		param3.setOrder("startTime desc");
		
		Meeting t = new Meeting();
		t.setShowEnable(1);
		t.setTitles(keywords);
		
		DataGrid grid3 = meetingService.selectAllMeeting(param3 , t);
		List<Meeting> list = grid3.getRows();
		for(Meeting m : list){
			m.calcStatus();
			m.setExtProp("wacthCount",getWatchViedoCount(m));
			m.setExtProp("chatCount",getCommentCount(m));
			m.setExtProp("signupCount",getMeetingSignupCount(m));
		}
		
		modelMap.addAttribute("list", list);*/
		modelMap.addAttribute("keywords", keywords);
		
		if(keywords!=null&&keywords.trim().length()>0){
			Keywords t1 = new Keywords();
			t1.setCreateTime(new Date());
			t1.setTypes(1);
			t1.setUserId(CtxHolder.getUserId());
			t1.setKeywords(keywords);
			this.keywordsService.insertKeywords(t1 );
		}
		return "investment/meeting/searchlist";
	}
	
	/**
	 * ?????????????????????
	 * @return
	 */
	@RequestMapping("/m/meeting/meetingsearch")
	@ResponseBody
	public Map<String,Object> meetingSearch(HttpServletRequest request,String keywords,Integer page,Integer size){
		Map result = Maps.newHashMap();
		try {
			Member me = ServletUtil.getMember(request);
			if (null == me||me.getId()==null||me.getId().trim().length()==0) {
				return super.getAjaxResult("302", "????????????", null);
			}
			
			PageParam param3 = new PageParam();
			param3.setRows(size);
			param3.setPage(page);
			param3.setOrder("startTime desc");
			
			Meeting t = new Meeting();
			t.setShowEnable(1);
			t.setTitles(keywords);
			
			DataGrid grid3 = meetingService.selectAllMeeting(param3 , t);
			List<Meeting> list = grid3.getRows();
			for(Meeting m : list){
				m.calcStatus();
				m.setExtProp("wacthCount",getWatchViedoCount(m));
				m.setExtProp("chatCount",getCommentCount(m));
				m.setExtProp("signupCount",getMeetingSignupCount(m));
			}
			result.put("list", list);
			return getAjaxResult("0", "", result);
		} catch (Exception e) {
			result.put("result_msg", "????????????");
			return getAjaxResult("0", "", result);
		}
	} 
	
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping("/m/meeting/detail/index")
	public String detailMeeting(HttpServletRequest request,ModelMap modelMap,String id){
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
		
		Meeting t = meetingService.getMeeting(id);
//		t.setStartTime(new Date(System.currentTimeMillis()-1000*60*60*24*10));
//		t.setEndTime(new Date(System.currentTimeMillis()+1000*60*60*24*10));
//		t.setStartSignupTime(new Date(System.currentTimeMillis()-1000*60*60*24*10));
//		t.setEndSignupTime(new Date(System.currentTimeMillis()+1000*60*60*24*10));
		t.calcStatus();
		
		//??????????????????
		MeetingSignupRemind msr = meetingSignupService.getMeetingSignupRemindByMeetingIdAndUserId(t.getId(), userId);
		t.setExtProp("signupRemindStatus", msr==null?0:1);
		modelMap.put("signupRemindStatus", msr==null?0:1);
		
		//????????????
		modelMap.put("signupStatus", checkMeetingSignupStatus(t)?1:0);
		long signupCount = getMeetingSignupCount(t);
		t.setExtProp("signupCount",signupCount);
		modelMap.put("signupCount", signupCount);
		
		//????????????
		Attention at = attentionService.getAttentionByFkIdAndUserId(CtxHolder.getUserId(), id);
		t.setExtProp("attentionStatus", at==null?0:1);
		modelMap.put("attentionStatus", at==null?0:1);
		modelMap.put("obj", t);
		return "investment/meeting/detail";
	}
	/**
	 * ????????????
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/m/my/meeting/signup/index")
	public String signupIndex(HttpServletRequest request,ModelMap modelMap,String id){
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
		if (StringUtils.isEmpty(id)) {
			modelMap.put("message", "????????????");
			modelMap.put("tips", "??????????????????");
			return "exception";
		}
		
		Meeting meeting = meetingService.getMeeting(id);
		TjyUser tjyUser =getTjyUserById(me.getId());
		
		if(meeting==null){
			modelMap.put("message", "????????????");
			modelMap.put("tips", "??????????????????");
			return "exception";
		}else if(meeting.getTypes()!=2&&StringUtils.isBlank(meeting.getWebinarId())){
			modelMap.put("message", "????????????");
			modelMap.put("tips", "??????????????????");
			return "exception";
		}else if(tjyUser==null){
			modelMap.put("message", "????????????");
			modelMap.put("tips", "??????????????????");
			return "exception";
		}else if(checkMeetingSignupStatus(meeting)){//?????????
			modelMap.put("message", "????????????");
			modelMap.put("tips", "????????????");
			return "exception";
		}else if(meeting.getUpperlimit()!=null&&meeting.getUpperlimit()>0
				&&getMeetingSignupCount(meeting)>=meeting.getUpperlimit().intValue()){
			modelMap.put("message", "????????????");
			modelMap.put("tips", "????????????????????????");
			return "exception";
		}
		
		modelMap.addAttribute("obj", meeting);
		modelMap.addAttribute("tjyUser",tjyUser);
		modelMap.addAttribute("wxUser",getWxUserById(CtxHolder.getUserId()));
		return "investment/meeting/signupindex";
	}
	/**
	 * ????????????
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/m/my/meetingsignup")
	public String signup(HttpServletRequest request,ModelMap modelMap,MeetingSignup ms){
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
		TjyUser tjyUser  = tjyUserService.selectByPrimaryKey(CtxHolder.getUserId());
		modelMap.addAttribute("signupObj", ms);
		modelMap.addAttribute("obj", meetingService.getMeeting(ms.getMeetingId()));
		modelMap.addAttribute("tjyUser",tjyUser);
		String instruction = refundInstructionService.getrefundinstructionByid(2).getContent();
		modelMap.put("instruction", instruction);
		return "investment/meeting/signup";
	}
	
	/**
	 * ????????????
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/m/my/meetingsignup2")
	public String signup2(HttpServletRequest request,ModelMap modelMap,MeetingSignup ms){
		Member me = ServletUtil.getMember(request);
		if (null == me||me.getId()==null||me.getId().trim().length()==0) {
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
		
		Meeting meeting = meetingService.getMeeting(ms.getMeetingId());
		TjyUser tjyUser = tjyUserService.selectByPrimaryKey(me.getId());
		
		if(meeting==null){
			modelMap.put("message", "????????????");
			modelMap.put("tips", "??????????????????");
			return "exception";
		}else if(tjyUser==null){
			modelMap.put("message", "????????????");
			modelMap.put("tips", "??????????????????");
			return "exception";
		}
		
		//???????????????????????????
		MeetingSignup signup = meetingSignupService.selectByMeetingIdAndUserId(me.getId(), ms.getMeetingId());
		
		signup = signup!=null?signup:new MeetingSignup();
		signup.setMeetingId(StringUtils.isBlank(signup.getMeetingId())?ms.getMeetingId():signup.getMeetingId());
		signup.setUserId(StringUtils.isBlank(signup.getUserId())?me.getId():signup.getUserId());
		signup.setSignupTime(signup.getSignupTime()==null?new Date():signup.getSignupTime());
		signup.setDeleted(0);
		signup.setIsFree(0);
		signup.setTicketPrice(meeting.getTicketPrice());
		signup.setOrderId(StringUtils.isBlank(signup.getOrderId())?UUIDGenerator.getUUID():signup.getOrderId());
		signup.setMainBusiness(ms.getMainBusiness());
		signup.setRegCapital(ms.getRegCapital());
		signup.setPayCapital(ms.getPayCapital());
		signup.setTotalAssets(ms.getTotalAssets());
		signup.setAnnualSales(ms.getAnnualSales());
		signup.setAttendType(ms.getAttendType());
		signup.setOtherReq(ms.getOtherReq());
		signup.setTjLinkMan(ms.getTjLinkMan());
		signup.setRecLinkMan(ms.getRecLinkMan());
		signup.setMobile(ms.getMobile()==null?tjyUser.getMobile():ms.getMobile());
		signup.setPayType(1);
		signup.setRemindTime(signup.getRemindTime());
		
		if(signup.getOrderStatus()!=null&&(signup.getOrderStatus().intValue()==2||signup.getOrderStatus().intValue()==3)){
			modelMap.put("message", "????????????");
			modelMap.put("tips", "?????????????????????????????????");
		}else{
			signup.setOrderStatus(2);
		}
		meeting.setTicketPrice((meeting.getTicketPrice()==null||meeting.getTicketPrice()<0)
									?0D:meeting.getTicketPrice());
		//??????????????????????????????????????????
		boolean isWhite = meetingService.isWhitelist(me.getId(), meeting.getId());
		signup.setOrderStatus(isWhite?3:2);
		signup.setPayType(isWhite?2:1);
		//signup.setPayType(meeting.getTicketPrice().doubleValue()==0?1:(isWhite?2:1));
		signup.setPayTime(null);
		if(StringUtils.isNotBlank(signup.getId())){
			meetingSignupService.updateMeetingSignup(signup);
		}else{
			meetingSignupService.insertMeetingSignup(signup);
		}
		sendMessage(ms.getMeetingId(), tjyUser.getId());
		modelMap.addAttribute("obj",signup);
		modelMap.addAttribute("tjyUser",tjyUser);
		return "investment/meeting/signupok2";
	}
	
	/**
	 * ????????????
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/m/meeting/signupok")
	public String signupok(HttpServletRequest request,ModelMap modelMap,MeetingSignup ms){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return "login";
		}
		
		TjyUser tjyUser=tjyUserService.selectByPrimaryKey(userId);
//		if(!"1".equals(tjyUser.getIsRealname()+"")){
//			return "commons/to_recon";
//		}
		
		String mobile = tjyUser.getMobile();
		if(StringUtils.isEmpty(ms.getId())){
			MeetingSignup signup = meetingSignupService.selectByMeetingIdAndUserId(userId, ms.getMeetingId());
			if(signup!=null&&StringUtils.isNotBlank(signup.getMobile())){
				mobile = signup.getMobile();
			}
		}
		
		modelMap.addAttribute("mobile",mobile);
		modelMap.addAttribute("tjyUser",tjyUser);
		return "investment/meeting/signupok";
	}
	/**
	 * ????????????(JSSKDK??????)
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/m/meeting/live")
	public String live(HttpServletRequest request,ModelMap modelMap,String id){
		Member me = ServletUtil.getMember(request);
		if (null == me||me.getId()==null||me.getId().trim().length()==0) {
			return "login";
		}
//		if(!"1".equals(me.getIsRealname())){
//		      return "commons/to_recon";
//		}
		
		TjyUser tjyUser = tjyUserService.selectByPrimaryKey(me.getId());
		Meeting meeting = meetingService.getMeeting(id);
		Map<String, Object> signMap = BaseAPI.createVedioSign(tjyUser.getId(),tjyUser.getNickname(), meeting.getWebinarId());
		modelMap.addAttribute("signObj", signMap);
		modelMap.addAttribute("obj", meeting);
		return "investment/meeting/live";
	}
	/**
	 * ????????????(??????????????????)
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/m/meeting/liveuseweb")
	public String liveUseWeb(HttpServletRequest request,ModelMap modelMap,String id){
		Member me = ServletUtil.getMember(request);
		if (null == me||me.getId()==null||me.getId().trim().length()==0) {
			return "login";
		}
//		if(!"1".equals(me.getIsRealname())){
//		      return "commons/to_recon";
//		}
		
		Meeting meeting = meetingService.getMeeting(id);
		TjyUser tjyUser = tjyUserService.selectByPrimaryKey(me.getId());
		
		if(meeting==null){
			modelMap.put("message", "????????????");
			modelMap.put("tips", "??????????????????");
			return "exception";
		}else if(StringUtils.isBlank(meeting.getWebinarId())){
			modelMap.put("message", "????????????");
			modelMap.put("tips", "??????????????????");
			return "exception";
		}else if(tjyUser==null){
			modelMap.put("message", "????????????");
			modelMap.put("tips", "??????????????????");
			return "exception";
		}else{
			String openK = ApplicationPath.getParameter("vhall_open_k");
			String k = meetingService.createVhallKey(openK,me.getId(), meeting.getWebinarId());
			try {
				return "redirect:http://e.vhall.com/webinar/inituser/"+meeting.getWebinarId()+"?"
						+ "email="+MD5UtilWx.MD5Encode(tjyUser.getId(), "UTF-8").toLowerCase()+"@vhall.com&"
						+ "name="+URLEncoder.encode(tjyUser.getNickname(), "UTF-8")+"&"
						+ "k="+k;
			} catch (UnsupportedEncodingException e) {
				return "redirect:http://e.vhall.com/webinar/inituser/"+meeting.getWebinarId()+"?"
						+ "email="+MD5UtilWx.MD5Encode(tjyUser.getId(), "UTF-8").toLowerCase()+"@vhall.com&"
						+ "k="+k;
			}
		}
		
	}
	/**
	 * ??????????????????
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/m/meeting/video")
	public String video(HttpServletRequest request,ModelMap modelMap){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return "login";
		}
//		TjyUser user=tjyUserService.selectByPrimaryKey(userId);
//		if(!"1".equals(user.getIsRealname()+"")){
//			return "commons/to_recon";
//		}
		return "investment/meeting/video";
	}
	/**
	 * ????????????
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/m/meeting/search")
	public String search(HttpServletRequest request,ModelMap modelMap){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return "login";
		}
		DataGrid grid = keywordsService.selectAllTop(1, 5);
		modelMap.put("list", grid.getRows());
		return "investment/meeting/search";
	}
	/**
     * ????????????????????????
     * @param unifiedorder
     * @param key
     * @return
	 * @throws IOException 
	 * @throws JDOMException 
     */
	@RequestMapping("/m/my/meeting/signup/prepay")
	@ResponseBody
    public Map<String,Object> payUnifiedorder(HttpServletRequest request,MeetingSignup ms,String couponlogid){
		try 
		{
			Map<String,String> resultObj = Maps.newHashMap();
			resultObj.put("result_code", "-1");
			resultObj.put("result_msg", "????????????");
			
			Member me = (Member) request.getSession().getAttribute("me");
			if (null == me) {
				return super.getAjaxResult("302", "????????????", null);
			}
			
			if(ms.getMeetingId()==null||ms.getMeetingId().trim().length()==0){
				resultObj.put("result_code", "-1");
				resultObj.put("result_msg", "??????????????????");
				return getAjaxResult("0", "", resultObj);
			}
			
			Meeting meeting = meetingService.getMeeting(ms.getMeetingId());
			if (meeting == null) {
				resultObj.put("result_msg", "???????????????");
				return super.getAjaxResult("0", "???????????????", resultObj);
			}
			
			
			meeting.setTicketPrice((meeting.getTicketPrice()==null||meeting.getTicketPrice()<0)
					?0D:meeting.getTicketPrice());
			
			Double ticketprice = meeting.getTicketPrice().doubleValue();
			/**
			 * ?????????????????????
			 */
			DecimalFormat df = new DecimalFormat("#.00");
			Double couponprice = 0.00;
			CouponLog  clog = null;
			Coupon cou = null;
			CouponGenerate couponGenerate = null;
			if(!StringUtils.isEmpty(couponlogid)){
				clog = couponLogService.selectById(couponlogid);
				if(null!=clog){
					couponGenerate = couponGenerateService.selectById(clog.getFkId());
					if(null!=couponGenerate){
						cou = couponService.selectById(couponGenerate.getFkId());
					}
				}
			}
			if(null!=clog&&null!=cou){
				/**
				 * ???????????????????????????
				 * @param  ??????????????????ID
				 * @param  ??????ID
				 * @param  ???????????????
				 * @param useRange 1????????????2?????????3?????????4????????????          ????????????
				 * @param currency    ??????   1:J???     2:RMB  
				 */
				Integer useRange = 2;
				Integer currency = 2;
				boolean canUse = couponLogService.checkCanUse(couponlogid,me.getId(),meeting.getTicketPrice(),useRange,currency);
				if(canUse){
					couponprice= cou.getCouponAmount();
					if(ticketprice<=couponprice){
						ticketprice=0.00;
					}else{
						ticketprice =Double.parseDouble(df.format( ticketprice - couponprice));
					}
				}
			};
			
			
			//???????????????????????????
			MeetingSignup signup = meetingSignupService.selectByMeetingIdAndUserId(me.getId(), ms.getMeetingId());
			
			signup = signup!=null?signup:new MeetingSignup();
			signup.setMeetingId(StringUtils.isBlank(signup.getMeetingId())?ms.getMeetingId():signup.getMeetingId());
			signup.setUserId(StringUtils.isBlank(signup.getUserId())?me.getId():signup.getUserId());
			signup.setSignupTime(signup.getSignupTime()==null?new Date():signup.getSignupTime());
			signup.setDeleted(0);
			signup.setIsFree(0);
			signup.setOrderId(StringUtils.isBlank(signup.getOrderId())?UUIDGenerator.getUUID():signup.getOrderId());
			signup.setMainBusiness(ms.getMainBusiness());
			signup.setRegCapital(ms.getRegCapital());
			signup.setPayCapital(ms.getPayCapital());
			signup.setTotalAssets(ms.getTotalAssets());
			signup.setAnnualSales(ms.getAnnualSales());
			signup.setAttendType(ms.getAttendType());
			signup.setOtherReq(ms.getOtherReq());
			signup.setTjLinkMan(ms.getTjLinkMan());
			signup.setRecLinkMan(ms.getRecLinkMan());
			signup.setMobile(ms.getMobile());
			signup.setPayType(1);
			signup.setRemindTime(signup.getRemindTime());
			signup.setCoupon(couponprice);
			
			if(signup.getOrderStatus()!=null&&signup.getOrderStatus().intValue()==2){
				resultObj.put("result_msg", "?????????");
				return super.getAjaxResult("0", "?????????", resultObj);
			}else{
				signup.setTicketPrice(ticketprice);
				signup.setOrderStatus(1);
			}
			//??????????????????????????????????????????
			boolean isWhite = meetingService.isWhitelist(me.getId(), meeting.getId());
			//????????????
			if(ticketprice==0||isWhite){
				signup.setOrderStatus(2);
				signup.setPayType(ticketprice==0?1:(isWhite?2:1));
				if(StringUtils.isNotBlank(signup.getId())){
					signup.setPayTime(null);
					meetingSignupService.updateMeetingSignup(signup);
				}else{
					signup.setId(UUIDGenerator.getUUID());
					signup.setPayTime(null);
					meetingSignupService.insertMeetingSignup(signup);
				}
				resultObj.put("id", signup.getId());
				resultObj.put("result_code", "0");
				resultObj.put("pay_status", "0");
				resultObj.put("result_msg", "????????????");
				sendMessage(meeting.getId(), me.getId());
				return super.getSuccessAjaxResult("????????????", resultObj);
			}
			
			//?????????????????????????????????????????????????????????????????????????????????????????????????????????
			if(StringUtils.isNotBlank(signup.getId())){
				MchOrderquery mchOrderquery = new MchOrderquery();
				
				mchOrderquery.setAppid(ApplicationPath.getParameter("wx_appid"));
				mchOrderquery.setMch_id(ConfigUtil.MCH_ID);
				mchOrderquery.setTransaction_id(signup.getOrderId());
				mchOrderquery.setNonce_str(PayCommonUtil.CreateNoncestr());
				
				MchOrderInfoResult o = PayMchAPI.payOrderquery(mchOrderquery);
				if(o.isSuccess()&&"SUCCESS".equalsIgnoreCase(o.getTrade_state())){
					signup.setOrderStatus(2);
					signup.setPayTime(new Date(Long.valueOf(o.getTime_end())));
					meetingSignupService.updateMeetingSignup(signup);
					resultObj.put("result_msg", "?????????");
					return super.getAjaxResult("0", "?????????", resultObj);
				}
				
				signup.setOrderId(UUIDGenerator.getUUID());
			}
			
			Unifiedorder unifiedorder = new Unifiedorder();
			unifiedorder.setAppid(ApplicationPath.getParameter("wx_appid"));
			unifiedorder.setMch_id(ConfigUtil.MCH_ID);
			unifiedorder.setNonce_str(PayCommonUtil.CreateNoncestr());
			unifiedorder.setBody("????????????");
			unifiedorder.setOut_trade_no(signup.getOrderId());
			unifiedorder.setTotal_fee(String.valueOf((int)(signup.getTicketPrice().doubleValue()*100)));
			unifiedorder.setSpbill_create_ip(request.getRemoteAddr());
			unifiedorder.setNotify_url(ConfigUtil.NOTIFY_WALLET_URL);
			unifiedorder.setTrade_type("JSAPI");
			unifiedorder.setOpenid(me.getOpenId());
			UnifiedorderResult res = PayMchAPI.payUnifiedorder(unifiedorder);
			if(!res.isSuccess()){
				resultObj.put("result_code", "-1");
				resultObj.put("result_msg", "????????????");
				return super.getAjaxResult("0", "????????????", resultObj);
			}

			if(StringUtils.isNotBlank(signup.getId())){
				signup.setOrderStatus(1);
				signup.setPayTime(null);
				meetingSignupService.updateMeetingSignup(signup);
			}else{
				signup.setId(UUIDGenerator.getUUID());
				signup.setOrderStatus(1);
				signup.setPayTime(null);
				meetingSignupService.insertMeetingSignup(signup);
			}
			
			SortedMap<Object, Object> params = new TreeMap<Object, Object>();
			params.put("appId", ApplicationPath.getParameter("wx_appid"));
			params.put("timeStamp", Long.toString(new Date().getTime()));
			params.put("nonceStr", PayCommonUtil.CreateNoncestr());
			params.put("package", "prepay_id=" + res.getPrepay_id());
			params.put("signType", ConfigUtil.SIGN_TYPE);
			
			String paySign = PayCommonUtil.createSign("UTF-8", params);
			// ?????????packageValue?????????package???????????????js???????????????
			params.put("packageValue", "prepay_id=" + res.getPrepay_id()); 
			params.put("paySign", paySign); // paySign??????????????????Sign?????????????????????
			
			String userAgent = request.getHeader("user-agent");
			char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger") + 15);
			// ?????????????????????????????????????????????????????????????????????????????????5.0???????????????
			params.put("agent", new String(new char[] { agent }));
			params.put("id", signup.getId());
			params.put("result_code", "0");
			params.put("orderId", signup.getOrderId());
			return super.getSuccessAjaxResult("????????????", params);
		} catch (Throwable e) {
			logger.error("???????????????", e);
			Map<String,String> resultObj = Maps.newHashMap();
			resultObj.put("result_code", "-1");
			resultObj.put("result_msg", "????????????");
			return super.getSuccessAjaxResult("????????????", resultObj);
		}
    }
	
	@RequestMapping("/m/my/meeting/signup/changepaystatus")
	@ResponseBody
    public Map<String,Object> updateOrderStatus(HttpServletRequest request,String id){
		MeetingSignup t = meetingSignupService.getMeetingSignup(id);
		t.setOrderStatus(2);
		t.setPayTime(new Date());
		meetingSignupService.updateMeetingSignup(t);
		sendMessage(t.getMeetingId(), t.getUserId());
		return super.getSuccessAjaxResult("????????????", "");
	}
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping("/m/meeting/attention")
	@ResponseBody
	public Map<String,Object> saveForAttention(String id){
		Map<String,String> result = Maps.newHashMap();
		result.put("result_code", "-1");
		result.put("result_msg", "????????????");
		try {
			String userId = CtxHolder.getUserId();
			if (StringUtils.isBlank(userId)) {
				return super.getAjaxResult("302", "????????????", null);
			}
			
			if(StringUtils.isBlank(id)){
				result.put("result_msg", "??????????????????");
				return getAjaxResult("0", "", result); 
			}
			
			
			Attention p = attentionService.getAttentionByFkIdAndUserId(CtxHolder.getUserId(), id);
			//?????????
			if(p!=null){
				result.put("result_code", "0");
				return getAjaxResult("0", "", result); 
			}else{
				Attention t = new Attention();
				t.setAttType("meeting");
				t.setCreateTime(new Date());
				t.setDeleted(0);
				t.setFkId(id);
				t.setUserId(CtxHolder.getUserId());
				attentionService.insertAttention(t);
			}
			result.put("result_code", "0");
			return getAjaxResult("0", "", result);
		} catch (Exception e) {
			result.put("result_msg", "????????????");
			return getAjaxResult("0", "", result);
		}
	}
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping("/m/meeting/removeattention")
	@ResponseBody
	public Map<String,Object> removeForAttention(String id){
		Map<String,String> result = Maps.newHashMap();
		result.put("result_code", "-1");
		result.put("result_msg", "????????????");
		try {
			String userId = CtxHolder.getUserId();
			if (StringUtils.isBlank(userId)) {
				return super.getAjaxResult("302", "????????????", null);
			}
			
			if(StringUtils.isBlank(id)){
				result.put("result_msg", "??????????????????");
				return getAjaxResult("0", "", result); 
			}
			
			
			Attention p = attentionService.getAttentionByFkIdAndUserId(CtxHolder.getUserId(), id);
			//?????????
			if(p!=null){
				attentionService.deleteAttention(p);
			}
			result.put("result_code", "0");
			return getAjaxResult("0", "", result);
		} catch (Exception e) {
			result.put("result_msg", "??????????????????");
			return getAjaxResult("0", "", result);
		}
	}
	/**
	 * ?????????????????????
	 * @return
	 */
	@RequestMapping("/m/meeting/signupremind")
	@ResponseBody
	public Map<String,Object> saveForSignupRemind(HttpServletRequest request,String id){
		Map<String,String> result = Maps.newHashMap();
		result.put("result_code", "-1");
		result.put("result_msg", " ");
		try {
			Member me = ServletUtil.getMember(request);
			if (null == me||me.getId()==null||me.getId().trim().length()==0) {
				return super.getAjaxResult("302", "????????????", null);
			}
//			if(!"1".equals(me.getIsRealname())){
//				return super.getAjaxResult("600", "???????????????", null);
//			}
			
			if(StringUtils.isBlank(id)){
				result.put("result_msg", "??????????????????");
				return getAjaxResult("0", "", result); 
			}
			String userId = me.getId();
			MeetingSignupRemind msr = meetingSignupService.getMeetingSignupRemindByMeetingIdAndUserId(id, userId );
			if(msr==null){
				meetingSignupService.insertMeetingSignupRemind(new MeetingSignupRemind(id, userId, 0));
			}
			result.put("result_code", "0");
			return getAjaxResult("0", "", result);
		} catch (Exception e) {
			result.put("result_msg", "????????????");
			return getAjaxResult("0", "", result);
		}
	} 
	/**
	 * ?????????????????????????????????
	 * @param m
	 * @return
	 */
	private boolean checkMeetingSignupStatus(Meeting m){
		if(m==null) return false;
		
		MeetingSignup ms = meetingSignupService.selectByMeetingIdAndUserId(CtxHolder.getUserId(),m.getId());
		if(ms==null) return false;
		//????????????
		if(m.getTicketPrice()==null||m.getTicketPrice().doubleValue()<=0){//??????
			m.setExtProp("signupStatus", 1);
			return true;
		}
		//?????????
		if(ms.getOrderStatus()!=null&&(ms.getOrderStatus().intValue()==2||ms.getOrderStatus().intValue()==3)){
			m.setExtProp("signupStatus", 1);
			return true;
		}
		if(ms.getOrderStatus()!=null&&ms.getOrderStatus().intValue()==1){
			//???????????????????????????
			MchOrderquery mchOrderquery = new MchOrderquery();
			mchOrderquery.setAppid(ApplicationPath.getParameter("wx_appid"));
			mchOrderquery.setMch_id(ConfigUtil.MCH_ID);
			mchOrderquery.setTransaction_id(ms.getOrderId());
			mchOrderquery.setNonce_str(PayCommonUtil.CreateNoncestr());
			
			MchOrderInfoResult o = PayMchAPI.payOrderquery(mchOrderquery);
			if(o.isSuccess()&&"SUCCESS".equals(o.getTrade_state())){
				ms.setOrderStatus(2);
				ms.setPayTime(new Date(Long.valueOf(o.getTime_end())));
				meetingSignupService.updateMeetingSignup(ms);
				m.setExtProp("signupStatus", 1);
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	/**
	 * ??????????????????????????????
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DataGrid getMeetingAtSignup(int page,int rows,
			Integer showEnable,Integer investmentEnable,Integer recommendEnable){
		//?????????????????????????????????????????????
		Meeting param = new Meeting();
		param.setShowEnable(showEnable);
		param.setInvestmentEnable(investmentEnable);
		param.setRecommendEnable(recommendEnable);
		param.setLtStartSignupTime(new Date());
		param.setGtEndSignupTime(new Date());
		param.setGtStartTime(new Date());
		param.setOrderBy("order by m.sort desc,m.create_time desc");
		
		DataGrid grid1 = meetingService.selectAllMeeting(new PageParam(page,rows) , param);
		List<Meeting> list1 = (List<Meeting>) grid1.getRows();
		
		for(Meeting m : list1){
			m.calcStatus();
			//?????????????????????????????????
			m.setExtProp("signupStatus", checkMeetingSignupStatus(m)?1:0);
			m.setExtProp("signupCount",getMeetingSignupCount(m));//???????????????
		}
		return grid1;
	}
	/**
	 * ?????????????????????????????????
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DataGrid getMeetingAtSignupEnd(int page,int rows,
			Integer showEnable,Integer investmentEnable,Integer recommendEnable){
		//?????????????????????????????????????????????
		Meeting param = new Meeting();
		param.setShowEnable(showEnable);
		param.setInvestmentEnable(investmentEnable);
		param.setRecommendEnable(recommendEnable);
		param.setLtEndSignupTime(new Date());
		param.setGtStartTime(new Date());
		param.setOrderBy("order by  m.sort desc,m.create_time desc");
		
		DataGrid grid1 = meetingService.selectAllMeeting(new PageParam(page,rows) , param);
		List<Meeting> list1 = (List<Meeting>) grid1.getRows();
		
		for(Meeting m : list1){
			m.calcStatus();
			//?????????????????????????????????
			m.setExtProp("signupStatus", checkMeetingSignupStatus(m)?1:0);
		}
		return grid1;
	}
	/**
	 * ??????????????????????????????
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DataGrid getMeetingAtExecuting(int page,int rows,
			Integer showEnable,Integer investmentEnable,Integer recommendEnable){
		//?????????????????????????????????????????????
		Meeting param = new Meeting();
		param.setShowEnable(showEnable);
		param.setInvestmentEnable(investmentEnable);
		param.setRecommendEnable(recommendEnable);
		param.setLtStartSignupTime(null);
		param.setGtEndSignupTime(null);
		param.setLtStartTime(new Date());
		param.setGtEndTime(new Date());
		param.setOrderBy("order by  m.sort desc,m.start_time asc");
		
		DataGrid grid1 = meetingService.selectAllMeeting(new PageParam(page,rows) , param);
		List<Meeting> list1 = (List<Meeting>) grid1.getRows();
		
		for(Meeting m : list1){
			m.calcStatus();
			//?????????????????????????????????
			m.setExtProp("signupStatus", checkMeetingSignupStatus(m)?1:0);
		}
		return grid1;
	}
	/**
	 * ??????????????????????????????
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DataGrid getMeetingAtHerald(int page,int rows,
			Integer showEnable,Integer investmentEnable,Integer recommendEnable){
		//?????????????????????????????????????????????
		Meeting param = new Meeting();
		param.setShowEnable(showEnable);
		param.setInvestmentEnable(investmentEnable);
		param.setRecommendEnable(recommendEnable);
		param.setGtStartSignupTime(new Date());
		param.setOrderBy("order by  m.sort desc,m.start_time asc");
		
		DataGrid grid = meetingService.selectAllMeeting(new PageParam(page,rows) , param);
		List<Meeting> list = (List<Meeting>) grid.getRows();
		
		for(Meeting m : list){
			m.calcStatus();
			//??????????????????
			MeetingSignupRemind msr = meetingSignupService.getMeetingSignupRemindByMeetingIdAndUserId(m.getId(), CtxHolder.getUserId());
			m.setExtProp("signupRemindStatus", msr==null?0:1);
		}
		return grid;
	}
	/**
	 * ??????????????????????????????
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DataGrid getMeetingAtEnd(int page,int rows,
			Integer showEnable,Integer investmentEnable,Integer recommendEnable){
		//?????????????????????????????????????????????
		Meeting param = new Meeting();
		param.setShowEnable(showEnable);
		param.setInvestmentEnable(investmentEnable);
		param.setRecommendEnable(recommendEnable);
		param.setLtEndTime(new Date());
		param.setOrderBy("order by  m.sort desc,m.start_time desc");
		
		DataGrid grid = meetingService.selectAllMeeting(new PageParam(page,rows) , param);
		List<Meeting> list = (List<Meeting>) grid.getRows();
		for(Meeting m : list){
			m.calcStatus();
			m.setExtProp("wacthCount",getWatchViedoCount(m));
			m.setExtProp("chatCount",getCommentCount(m));
			m.setExtProp("signupCount",getMeetingSignupCount(m));
		}
		return grid;
	}
	/**
	 * ??????????????????
	 * @return
	 */
	private long getMeetingSignupCount(Meeting m){
		Map<String, Object> parm = Maps.newHashMap();
		parm.put("meetingId", m.getId());
		parm.put("orderStatus",2);
		long s = this.meetingSignupService.selectAllMeetingSignup(new PageParam(1, 1), parm).getTotal();
		return s;
	}
	/**
	 * ????????????????????????
	 * @param webinarId
	 * @return
	 */
	private int getWatchViedoCount(Meeting m){
		try {
			if(m==null||StringUtils.isBlank(m.getWebinarId())||m.getStartTime()==null){
				return 0;
			}else{
				int unlivingCount = WebinarAPI.trackWebinar(m.getWebinarId(), 2).getTotal();
				return unlivingCount;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 * ??????????????????
	 * @param webinarId
	 * @return
	 */
	private int getCommentCount(Meeting m){
		try {
			if(m==null||StringUtils.isBlank(m.getWebinarId())||m.getStartTime()==null){
				return 0;
			}else{
				String startTime = DateUtil.date2String(m.getStartTime(), "yyyy-MM-dd HH:mm:ss");
				String endTime = DateUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss");
				int chatCount = WebinarAPI.chatWebinar(m.getWebinarId(), startTime, endTime, 1, 1).getTotal();
				return chatCount;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	private TjyUser getTjyUserById(String id){
		TjyUser tjyUser  = tjyUserService.selectByPrimaryKey(id);
		if(tjyUser==null) return null;
		
		// ????????????
		if(StringUtils.isNotBlank(tjyUser.getJob())){
			ListValues job = listValuesService.selectByPrimaryKey(tjyUser.getJob());
			tjyUser.setJob(job!=null?job.getListValue():tjyUser.getJob());
		}
		// ????????????
		if(StringUtils.isNotBlank(tjyUser.getIndustry())){
			ListValues industry = listValuesService.selectByPrimaryKey(tjyUser.getIndustry());
			tjyUser.setIndustry(industry!=null?industry.getListValue():tjyUser.getIndustry());
		}
		// ?????????
		if (StringUtils.isNotBlank(tjyUser.getProvince())) {
			SyDistrict province = districtService.selectByPrimaryKey(tjyUser.getProvince());
			tjyUser.setProvince(province!=null?province.getDisName():tjyUser.getProvince());
		}
		// ?????????
		if (StringUtils.isNotBlank(tjyUser.getCity())) {
			SyDistrict city = districtService.selectByPrimaryKey(tjyUser.getCity());
			tjyUser.setCity(city!=null?city.getDisName():tjyUser.getCity());
		}
		// ?????????
		if (StringUtils.isNotBlank(tjyUser.getRegion())) {
			SyDistrict region = districtService.selectByPrimaryKey(tjyUser.getRegion());
			tjyUser.setRegion(region!=null?region.getDisName():tjyUser.getRegion());
		}
		return tjyUser;
	}
	private Map<String,Object> getWxUserById(String id){
		Map<String, Object> user = wxUserService.queryUsersByid(id);

		if (!org.springframework.util.StringUtils.isEmpty(user.get("province"))) {
			SyDistrict sd = districtService.selectByPrimaryKey((String) user.get("province"));
			user.put("province", sd.getDisName());
		}
		if (!org.springframework.util.StringUtils.isEmpty(user.get("city"))) {
			SyDistrict sd = districtService.selectByPrimaryKey((String) user.get("city"));
			user.put("city", sd.getDisName());
		}
		if (!org.springframework.util.StringUtils.isEmpty(user.get("county"))) {
			SyDistrict sd = districtService.selectByPrimaryKey((String) user.get("county"));
			user.put("county", sd.getDisName());
		}
		return user==null?new HashMap<String, Object>():user;
	}
	/**
	 * ??????????????????
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@RequestMapping("/m/meeting/listpage")
	@ResponseBody
	public Map<String,Object> queryPageMeeting(ModelMap modelMap,int type,int pageIndex){
		try {
			PageParam pageParam = new PageParam(pageIndex<=0?1:pageIndex,5);
			Meeting param = new Meeting();
			param.setShowEnable(1);
			param.setUserId(CtxHolder.getUserId());
			if(type==1){//??????????????????
				param.setIsSignuped(1);
				param.setGtStartTime(new Date());//????????????????????????????????????
				param.setOrderBy("order by  m.sort desc,m.start_time desc");
				DataGrid grid = meetingService.selectAllMeeting(pageParam , param);
				return getSuccessAjaxResult("", grid);
			}else if(type==2){//??????????????????
				param.setIsSignuped(1);
				param.setLtStartTime(new Date());//????????????????????????????????????
				param.setOrderBy("order by  m.sort desc,m.start_time desc");
				DataGrid grid = meetingService.selectAllMeeting(pageParam , param);
				return getSuccessAjaxResult("", grid);
			}else if(type==3){//??????????????????
				param.setIsAttentioned(1);
				//????????????????????????
				//param.setOrderBy("order by m.start_time desc");
				//DataGrid grid = meetingService.selectAllMeeting(pageParam , param);
				//??????????????????
				param.setOrderBy("order by b.create_time desc");
				DataGrid grid = meetingService.selectMyCollectMeeting(pageParam , param);
				return getSuccessAjaxResult("", grid);
			}else{
				return getSuccessAjaxResult("", new DataGrid());
			}
		} catch (Throwable e) {
			logger.error("??????????????????", e);
			return getSuccessAjaxResult("", new DataGrid());
		}
	}
	/**
	 * vhall k??????
	 * @param email
	 * @param k
	 * @return
	 */
	@RequestMapping("/m/vhall/validk")
	@ResponseBody
	public String validk(String email,String k){
		try {
			logger.info("vhall k:"+k);
			logger.info("vhall email:"+email);
			
			return meetingService.validVhallKey(k);
		} catch (Throwable e) {
			logger.error("k???????????????",e);
			return "fail";
		}
	}
	/**
	 * ??????
	 * @param email
	 * @param k
	 * @return
	 */
	@RequestMapping("/m/meeting/more/index")
	public String moreIndex(ModelMap modelMap,Integer status){
		status = status==null?-1:status;
		if(status.intValue()==1){//?????????
			modelMap.addAttribute("dataGrid", getMeetingAtHerald(1,20,1,null,null));
			modelMap.put("titles", "????????????");
		}else if(status.intValue()==2){//?????????
			modelMap.addAttribute("dataGrid", getMeetingAtSignup(1,20,1,null,null));
			modelMap.put("titles", "???????????????");
		}else if(status.intValue()==3){//????????????
			modelMap.addAttribute("dataGrid", getMeetingAtSignupEnd(1,20,1,null,null));
			modelMap.put("titles", "????????????");
		}else if(status.intValue()==4){//?????????
			modelMap.addAttribute("dataGrid", getMeetingAtExecuting(1,20,1,null,null));
			modelMap.put("titles", "?????????");
		}else if(status.intValue()==5){//?????????
			modelMap.addAttribute("dataGrid", getMeetingAtEnd(1,20,1,null,null));
			modelMap.put("titles", "????????????");
		}
		modelMap.addAttribute("status", status);
		return "investment/meeting/more";
	}
	/**
	 * ??????
	 * @param type ???????????? 1?????????2???????????????3?????????
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@RequestMapping("/m/meeting/more/list")
	@ResponseBody
	public Map<String,Object> queryMorelist(Integer status,int pageIndex){
		try {
			Meeting param = new Meeting();
			param.setShowEnable(1);
			//param.setInvestmentEnable(1);
			param.setUserId(CtxHolder.getUserId());
			param.setOrderBy("order by  m.sort desc,m.create_time desc");
			
			status = status==null?-1:status;
			if(status.intValue()==1){//?????????
				return getSuccessAjaxResult("", getMeetingAtHerald(pageIndex<=0?1:pageIndex,20,1,null,null));
			}else if(status.intValue()==2){//?????????
				return getSuccessAjaxResult("", getMeetingAtSignup(pageIndex<=0?1:pageIndex,20,1,null,null));
			}else if(status.intValue()==3){//????????????
				return getSuccessAjaxResult("", getMeetingAtSignupEnd(pageIndex<=0?1:pageIndex,20,1,null,null));
			}else if(status.intValue()==4){//?????????
				return getSuccessAjaxResult("", getMeetingAtExecuting(pageIndex<=0?1:pageIndex,20,1,null,null));
			}else if(status.intValue()==5){//?????????
				return getSuccessAjaxResult("", getMeetingAtEnd(pageIndex<=0?1:pageIndex,20,1,null,null));
			}else{
				return getSuccessAjaxResult("", new DataGrid());
			}
		} catch (Throwable e) {
			logger.error("??????????????????", e);
			return getSuccessAjaxResult("", new DataGrid());
		}
	}
	/**
	 * ????????????????????????
	 * @param meetingId
	 * @param userId
	 */
	private void sendMessage(String meetingId,String userId){
		MeetingSignup ms = null;
		try {
			ms = meetingSignupService.selectByMeetingIdAndUserId(userId,meetingId);
			if(ms.getOrderStatus()==null||ms.getOrderStatus().intValue()==1){
				return;
			}
		} catch (Throwable e) {
			logger.error("???????????????"+userId+"????????????????????????", e);
		}
		if(ms==null){
			return;
		}
		//??????????????????
		try {
			String message =AldyMessageUtil.meetingsignup(ms.getNickname(), DateUtils.dateToString(ms.getSignupTime(), "yyyy-MM-dd HH:mm"), ms.getTitles(), ms.getMobile());
//			String message ="???"+AldyMessageUtil.SMSPRE+"????????????"+ms.getNickname()+"???"
//					+ "??????"+DateUtils.dateToString(ms.getSignupTime(), "yyyy-MM-dd HH:mm")
//					+"????????????"+ms.getTitles()+"????????????????????????????????????"+ms.getNickname()+"????????????"+ms.getMobile()+"???";
			MessageInfo messageInfo = new MessageInfo();
			messageInfo.setId(UUIDGenerator.getUUID());
			messageInfo.setDeleted(0);
			messageInfo.setType(4);
			messageInfo.setToUserId(userId);
			messageInfo.setCreateTime(new Date());
			messageInfo.setContent(message);
			messageInfo.setTemplateId("ACTIVITY_BM_TX");
			messageInfo.setStatus(0);
			messageInfo.setWxMsgType(1);/// ** ?????????????????????1???????????????2?????????????????? */
			messageInfoService.addMessageInfo(messageInfo);
		} catch (Throwable e) {
			logger.error("?????????????????????????????????userId:"+userId+"?????????", e);
		}
		//????????????
		try {
			if(ms.getMobile()==null){
				logger.error("???????????????????????????userId:"+userId+"???????????????????????????");
				return;
			}else if(ms.getMobile().length()!=11){
				logger.error("???????????????");
				logger.error("???????????????????????????userId:"+userId+"??????????????????????????????mobile:"+ms.getMobile()+"???");
				return;
			}
			
			Map<String,String> params = Maps.newHashMap();
			params.put("name", ms.getNickname());
			params.put("time", DateUtils.dateToString(ms.getSignupTime(), "yyyy-MM-dd HH:mm"));
			params.put("hyname", ms.getTitles());
			params.put("uname", ms.getNickname());
			params.put("mobilem", ms.getMobile());
			
			MessageInfo messageInfo = new MessageInfo();
			messageInfo.setId(UUIDGenerator.getUUID());
			messageInfo.setDeleted(0);
			messageInfo.setMobile(ms.getMobile());
			messageInfo.setType(1);// ??????
			messageInfo.setCreateTime(new Date());
			messageInfo.setContent(JSON.toJSONString(params));
			messageInfo.setStatus(0);// ?????????
			messageInfo.setTemplateId(AldyMessageUtil.MsmTemplateId.MEETING_SIGNUP_SUCCESS);
			messageInfoService.addMessageInfo(messageInfo);
		} catch (Throwable e) {
			logger.error("?????????????????????????????????userId:"+userId+"?????????", e);
		}
	}
}