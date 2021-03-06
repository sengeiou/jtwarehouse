package com.wing.socialcontact.front.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.wing.socialcontact.config.OssConfig;
import com.wing.socialcontact.front.util.EsapiTest;
import com.wing.socialcontact.service.wx.api.IAttentionService;
import com.wing.socialcontact.service.wx.api.IBusinessClassService;
import com.wing.socialcontact.service.wx.api.IBusinessDisscussService;
import com.wing.socialcontact.service.wx.api.IBusinessService;
import com.wing.socialcontact.service.wx.api.ICommentService;
import com.wing.socialcontact.service.wx.api.ICommentThumbupService;
import com.wing.socialcontact.service.wx.api.IMessageInfoService;
import com.wing.socialcontact.service.wx.api.ITjyUserService;
import com.wing.socialcontact.service.wx.api.IWalletLogService;
import com.wing.socialcontact.service.wx.api.IWxUserService;
import com.wing.socialcontact.service.wx.bean.Attention;
import com.wing.socialcontact.service.wx.bean.Business;
import com.wing.socialcontact.service.wx.bean.BusinessDisscuss;
import com.wing.socialcontact.service.wx.bean.Comment;
import com.wing.socialcontact.service.wx.bean.CommentThumbup;
import com.wing.socialcontact.service.wx.bean.MessageInfo;
import com.wing.socialcontact.service.wx.bean.TjyUser;
import com.wing.socialcontact.service.wx.bean.WalletLog;
import com.wing.socialcontact.service.wx.bean.WxUser;
import com.wing.socialcontact.util.AldyMessageUtil;
import com.wing.socialcontact.util.CommUtil;
import com.wing.socialcontact.util.Constants;
import com.wing.socialcontact.util.DoubleUtil;
import com.wing.socialcontact.util.ServletUtil;
import com.wing.socialcontact.util.SpringContextUtil;
import com.wing.socialcontact.util.UUIDGenerator;
import com.wing.socialcontact.util.WxMsmUtil;


/**
 * ????????????
 * @author zhangfan
 *
 */
@Controller
@RequestMapping("")
public class BusinessAction extends BaseAction{
	
	@Autowired
	private IBusinessService businessService; 
	@Autowired
	private IBusinessClassService businessClassService;
	@Autowired
	private IBusinessDisscussService businessDisscussService;
	@Autowired
	private IAttentionService attentionService;
	@Autowired
	private ICommentThumbupService commentThumbupService;
	@Autowired
	private ICommentService commentService;
	@Autowired
	private IWalletLogService walletLogService;
	@Autowired
	private IWxUserService wxUserService;
	@Autowired
	private ITjyUserService tjyUserService;
	@Autowired
	private IMessageInfoService messageInfoService;
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping("/m/business/indexPage")
	public String indexPage(ModelMap map){
		map.put("bannerid", Constants.BANNER_BUSINESS_ID);
		return "netWork/cooperate";
	}
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping("/m/business/selClassList")
	public @ResponseBody Map selClassList(){
		//??????
		List classList = businessClassService.selectAllClass(7,1);
		Map res = new HashMap();
		res.put("classList", classList);
		OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();
		res.put("ossurl", ossurl);
		return super.getSuccessAjaxResult("???????????????", res);
	}
	/**
	 * ??????????????????
	 * @param type
	 * @return
	 */
	@RequestMapping("/m/business/selBusinessList")
	public @ResponseBody Map selBusinessList(HttpServletRequest request,HttpServletResponse response,String type,Integer page,Integer size){
		List<Map<String, Object>> list = new ArrayList();
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		if (page==null||page<1) {
			page = 1;
		}
		if (size==null||size<1) {
			size = 10;
		}
		if(type.equals("1")){
			//????????????
			list = businessService.selectFrontBusiness(page,size, null, null, 1,null,1,userId);
		}else if(type.equals("2")){
			//????????????
			list = businessService.selectFrontBusiness(page,size, null, null, null,null,null,userId);
		}
		for(Map<String, Object> m : list){
			m.put("createTime", m.get("createTime")==null?"":CommUtil.getTimesToNow(CommUtil
					.formatLongDate(m.get("createTime"))));
		}
		return super.getSuccessAjaxResult("???????????????", list);
	}
	/**
	 * ??????????????????
	 * @param map
	 * @return
	 */
	@RequestMapping("/m/business/classPage")
	public String classPage(ModelMap map){
		return "netWork/classPage";
	}
	@RequestMapping("/m/business/selAllClassList")
	public @ResponseBody Map selAllClassList(){
		//??????
		List classList = businessClassService.selectAllClass(null,null);
		Map res = new HashMap();
		res.put("classList", classList);
		OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();
		res.put("ossurl", ossurl);
		return super.getSuccessAjaxResult("???????????????", res);
	}
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping("/m/business/addBusinessPage")
	public String addBusinessPage(HttpServletRequest request,HttpServletResponse response,ModelMap map){
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
		List list = businessClassService.selectAllClass(null,null);
		map.addAttribute("list", list);
		return  "netWork/businessAdd";
	}
	/**
	 * ????????????
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/add/m/business/addBusiness")
	public @ResponseBody Map addBusiness(HttpServletRequest request,HttpServletResponse response,String titles,
			String bizType,String appealType,String appealSummary,String reward,String startTime,String endTime,
			String appealDesc,String allowComment,String isShow) throws ParseException{
		titles= EsapiTest.stripXSS(titles);
		bizType= EsapiTest.stripXSS(bizType);
		appealType= EsapiTest.stripXSS(appealType);
		appealSummary= EsapiTest.stripXSS(appealSummary);
		reward= EsapiTest.stripXSS(reward);
		appealDesc= EsapiTest.stripXSS(appealDesc);
		allowComment= EsapiTest.stripXSS(allowComment);
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		WxUser user = wxUserService.selectById(userId);
		if(reward!=null&&!"".equals(reward)){
			//??????????????????j???
			Double doub = DoubleUtil.sub(user.getJbAmount()==null?0:user.getJbAmount(), Double.valueOf(reward));
			if(doub<0){
				return super.getAjaxResult("501", "J????????????", null);
			}else{
				user.setJbAmount(doub);
				wxUserService.updateWxUser(user);
				WalletLog log = new WalletLog();
				log.setCreateTime(new Date());
				log.setType("2");
				log.setPdType("2");
				log.setPayStatus("1");
				log.setUserId(userId);
				log.setAmount(Double.valueOf(reward));
				log.setRemark("????????????");
				log.setYeAmount(doub);
				log.setBusinessType(5);
				walletLogService.addWalletLog(log);
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Business business = new Business();
		business.setTitles(titles);
		business.setCreateTime(new Date());
		business.setBizType(bizType);
		business.setAppealType(Integer.parseInt(appealType));
		business.setAppealSummary(appealSummary);
		business.setStartTime(sdf.parse(startTime));
		business.setEndTime(sdf.parse(endTime));
		business.setReward(reward.equals("")?0:Integer.parseInt(reward));
		business.setAppealDesc(appealDesc);
		business.setAllowComment(Integer.parseInt(allowComment));
		business.setCreateUserId(userId);
		business.setCreateUserName(user.getUsername());
		business.setStatus(2);
		business.setLookCount(0);
		business.setRewardFinish(2);
		business.setIsShow(Integer.parseInt(isShow));
		String resultStr = businessService.addBusiness(business);
		return super.getSuccessAjaxResult();
	}
	/**
	 * ???????????????
	 * @return
	 */
	@RequestMapping("/m/business/detailPage")
	public String detailPage(HttpServletRequest request,HttpServletResponse response,ModelMap map,String id,String type){
		if("2".equals(type)){//????????????
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
		}
		map.addAttribute("id", id);
		return "netWork/my-cooperate";
	}
	/**
	 * ????????????
	 * @param id
	 * @return
	 */
	@RequestMapping("/m/business/detail")
	public @ResponseBody Map detail(HttpServletRequest request,HttpServletResponse response,String id){
		Map res = new HashMap();
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		res.put("userId", userId);
		Map<String, Object> business = businessService.selectBusinessById(id);
		//??????????????????
		Business b = businessService.selectById(id);
		b.setLookCount(b.getLookCount()==null?1:b.getLookCount()+1);
		businessService.updateBusiness(b);
		//??????????????????????????????
		List<Map<String, Object>> list = businessDisscussService.selectBDByFkId(id,null);
		int count = 0;
		int subcount = 0;
		for (Map<String, Object> m : list) {
			// ???????????????
			CommentThumbup commentThumbup = new CommentThumbup();
			commentThumbup.setPId((String) (m.get("id")));
			count = commentThumbupService.selectcount(commentThumbup);
			m.put("count", count);
			
			commentThumbup.setUserId(userId);
			List<Map<String, Object>> thumbupList = commentThumbupService
					.selectAllCommentThumbup(commentThumbup);
			boolean isThumbup = false;
			if (thumbupList.size() > 0) {
				isThumbup =true;
			} 
			//??????????????????????????????
			m.put("isThumbup", isThumbup);
			// ???????????????
			Comment subcomment = new Comment();
			subcomment.setParentId((String) (m.get("id")));
			List<Map<String, Object>> subCommentList = commentService
					.queryCommentbyPid((String) (m.get("id")));
			if (null != subCommentList) {
				subcount = subCommentList.size();
			}
			m.put("subcount", subcount);
			
		}
		//??????????????????
		Attention att = new Attention();
		att.setFkId(id);
		att.setUserId(userId);
		List<Attention> attlist = attentionService.queryAttention(att);
		int isAttention = 0;
		if(attlist.size()>0&&attlist!=null){
			isAttention = 1;
		}
		res.put("isAttention", isAttention);
		res.put("bdlist", list);
		res.put("business", business);
		return super.getSuccessAjaxResult("???????????????", res);
	}
	/**
	 * ????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping("/m/business/listPage")
	public String listPage(ModelMap map,String bizType){
		map.put("bizType", bizType);
		return "netWork/cooperation-list";
	}
	/**
	 * ??????????????????
	 * @param type
	 * @return
	 */
	@RequestMapping("/m/business/selListByType")
	public @ResponseBody Map selListByType(HttpServletRequest request,HttpServletResponse response,
			String bizType,String titles,Integer page,Integer size){
		List<Map<String, Object>> list = new ArrayList();
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		if (page==null||page<1) {
			page = 1;
		}
		if (size==null||size<1) {
			size = 10;
		}
		list = businessService.selectFrontBusiness(page,size, titles, bizType, null,null,null,userId);
		for(Map<String, Object> m : list){
			m.put("createTime", CommUtil.getTimesToNow(CommUtil
					.formatLongDate(m.get("createTime"))));
		}
		return super.getSuccessAjaxResult("???????????????", list);
	}
	
	/**
	 * ????????????????????????
	 * @return
	 */
	@RequestMapping("/m/business/addBDPage")
	public String addBDPage(HttpServletRequest request,HttpServletResponse response,ModelMap map,String fkId){
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
		map.addAttribute("fkId", fkId);
		return  "netWork/addDBPage";
	}
	/**
	 * ??????????????????
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/add/m/business/addBD")
	public @ResponseBody Map addBD(HttpServletRequest request,HttpServletResponse response,String content,String fkId) throws ParseException{
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		WxUser user = wxUserService.selectById(userId);
		BusinessDisscuss bd = new BusinessDisscuss();
		bd.setCreateTime(new Date());
		bd.setContent(content);
		bd.setFkId(fkId);
		bd.setCreateUserId(userId);
		bd.setCreateUserName(user.getUsername());
		bd.setIsAccept(2);
		bd.setIsShow(1);
		String resultStr = businessDisscussService.addBD(bd);
		return super.getSuccessAjaxResult();
	}
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping("/m/business/selMyHzPage")
	public String selMyHzPage(HttpServletRequest request,HttpServletResponse response,ModelMap map,String fkId){
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
		map.addAttribute("fkId", fkId);
		return  "netWork/myh";
	}
	/**
	 * ????????????????????????
	 * @param type
	 * @return
	 */
	@RequestMapping("/m/business/selMyBusinessList")
	public @ResponseBody Map selMyBusinessList(HttpServletRequest request,HttpServletResponse response,
			String type,Integer page,Integer size){
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		if (page==null||page<1) {
			page = 1;
		}
		if (size==null||size<1) {
			size = 10;
		}
		List list = new ArrayList();
		if(type.equals("1")){
			//??????????????????
			list = businessService.selectMyBusiness(userId,page,size);
		}else if(type.equals("2")){
			//??????????????????
			list = businessService.selectMyAttention(userId,page,size);
		}else if(type.equals("3")){
			//????????????
			list = businessDisscussService.selectMyBD(page, size, userId);
		}
		return super.getSuccessAjaxResult("???????????????", list);
	}
	
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping("/m/business/addAttention")
	public @ResponseBody Map addAttention(HttpServletRequest request,HttpServletResponse response,String fkId){
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		Attention attention = new Attention();
		attention.setCreateTime(new Date());
		attention.setFkId(fkId);
		attention.setUserId(userId);
		attention.setAttType("2");
		String resultStr = attentionService.saveOrDelAttention(attention);
		return super.getAjaxResult("0",resultStr,null);
	}
	
	/**
	 * ?????????????????????????????????
	 * @return
	 */
	@RequestMapping("/m/business/myDetailPage")
	public String myDetailPage(ModelMap map,String id){
		map.addAttribute("id", id);
		return "netWork/my-cooperateMe";
	}
	
	/**
	 * ??????
	 * @return
	 */
	@RequestMapping("/m/business/accept")
	public @ResponseBody Map accept(HttpServletRequest request,HttpServletResponse response,String id,String fkId){
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		Business b = businessService.selectById(fkId);
		//?????????????????????????????????
		List<Map<String, Object>> list = businessDisscussService.selectBDByFkId(fkId,1);
		//????????????????????????????????????????????????
		if(list!=null&&list.size()>0){
			return super.getAjaxResult("602", "?????????????????????????????????????????????!", null);
		}else{
			//???????????????????????????j???
			Integer num = b.getReward()==null?0:b.getReward();
			BusinessDisscuss bd = businessDisscussService.selectById(id);
			WalletLog logf = new WalletLog();
			logf.setCreateTime(new Date());
			logf.setType("2");
			logf.setPdType("1");
			logf.setUserId(bd.getCreateUserId());
			logf.setAmount(Double.valueOf(num));
			logf.setRemark("??????????????????");
			logf.setPayStatus("1");
			logf.setBusinessType(12);
			logf.setSourceUser(userId);
			walletLogService.addWalletLog(logf);
			WxUser userf = wxUserService.selectById(bd.getCreateUserId());
			userf.setJbAmount(DoubleUtil.add(userf.getJbAmount(), Double.valueOf(num)));
			wxUserService.updateWxUser(userf);
			b.setRewardFinish(1);
			businessService.updateBusiness(b);
			bd.setIsAccept(1);
			businessDisscussService.updateBD(bd);
			//????????????????????????????????????????????????????????????????????????
			// ????????????
			TjyUser tjyUser = tjyUserService.selectByPrimaryKey(userf.getId() + "");
			MessageInfo messageInfo = new MessageInfo();
			messageInfo.setId(UUIDGenerator.getUUID());
			messageInfo.setDeleted(0);
			messageInfo.setMobile(userf.getMobile());
			messageInfo.setType(1);// ??????
			messageInfo.setCreateTime(new Date());
			String name = tjyUser.getNickname();
			String contentdx="{name:\"" + name + "\",subname:\"" + b.getTitles() +  "\",type:\""+"????????????"+ "\",money:\"" + b.getReward() + "\"}";
			messageInfo.setContent(contentdx);
			messageInfo.setStatus(0);// ?????????
			messageInfo.setTemplateId(AldyMessageUtil.MsmTemplateId.NEGOTIATE_SUCCESS);
			messageInfoService.addMessageInfo(messageInfo);
			// ????????????
			String touser = bd.getCreateUserId();
			messageInfo = new MessageInfo();
			messageInfo.setId(UUIDGenerator.getUUID());
			messageInfo.setDeleted(0);
			messageInfo.setType(2);// ??????
			messageInfo.setToUserId(touser);
			messageInfo.setCreateTime(new Date());
			// ????????????
			String content = AldyMessageUtil.userRewardAccept(name, b.getTitles(), b.getReward());
//			String content = "???" + AldyMessageUtil.SMSPRE + "????????????" + name + ",??????" + b.getTitles()
//					+ "???????????????????????????????????????????????????"+b.getReward()+"J????????????????????????";
			String con = WxMsmUtil.getTextMessageContent(content);
			messageInfo.setContent(con);
			messageInfo.setTemplateId("BUSINESS_CAINA");
			messageInfo.setStatus(0);// ?????????
			messageInfo.setWxMsgType(1);///** ?????????????????????1???????????????2?????????????????? */
			messageInfoService.addMessageInfo(messageInfo);
			/**
			 * ??????????????????
			 */
			messageInfo = new MessageInfo();
			messageInfo.setId(UUIDGenerator.getUUID());
			messageInfo.setDeleted(0);
			messageInfo.setType(3);// ????????????
			messageInfo.setToUserId(touser);
			messageInfo.setCreateTime(new Date());
			messageInfo.setContent(content);
			messageInfo.setStatus(0);// ???????????????
			messageInfoService.addMessageInfo(messageInfo);
			return super.getSuccessAjaxResult();
		}
	}
	/**
	 * ????????????
	 * @param request
	 * @param response
	 * @param idbnyvb
	 * @return
	 */
	@RequestMapping("/m/business/rewardFinish")
	public @ResponseBody Map rewardFinish(HttpServletRequest request,HttpServletResponse response,String[] ids,String id){
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		Business b = businessService.selectById(id);
		if(b.getRewardFinish()==1){
			return super.getAjaxResult("503", "????????????????????????????????????????????????", null);
		}
	/*	//??????????????????j???
		WxUser user = wxUserService.selectById(userId);
		Double doub = DoubleUtil.sub(user.getJbAmount()==null?0:user.getJbAmount(), Double.valueOf(b.getReward()));
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
			log.setAmount(Double.valueOf(b.getReward()));
			log.setRemark("????????????");
			walletLogService.addWalletLog(log);
		}*/
		//???????????????????????????
		if(ids!=null&&ids.length>0){
			Integer cou = b.getReward()/ids.length;
			Integer yu = b.getReward()%ids.length;
			for (int i=0;i<ids.length;i++) {
				BusinessDisscuss bd = businessDisscussService.selectById(ids[i]);
				//?????????????????????j???
				Integer num = 0;
				if(i==ids.length-1){
					num = cou+yu;
				}else{
					num = cou;
				}
				WxUser userf = wxUserService.selectById(bd.getCreateUserId());
				Double doub = DoubleUtil.add(userf.getJbAmount(), Double.valueOf(num));
				userf.setJbAmount(doub);
				wxUserService.updateWxUser(userf);
				//??????
				WalletLog logf = new WalletLog();
				logf.setCreateTime(new Date());
				logf.setType("2");
				logf.setPdType("1");
				logf.setUserId(bd.getCreateUserId());
				logf.setAmount(Double.valueOf(num));
				logf.setRemark("??????????????????");
				logf.setPayStatus("1");
				logf.setBusinessType(12);
				logf.setSourceUser(userId);
				logf.setYeAmount(doub);
				walletLogService.addWalletLog(logf);
				//??????????????????
				bd.setIsAccept(1);
				businessDisscussService.updateBD(bd);
				//????????????????????????????????????????????????????????????????????????
				// ????????????
				TjyUser tjyUser = tjyUserService.selectByPrimaryKey(userf.getId() + "");
				MessageInfo messageInfo = new MessageInfo();
				messageInfo.setId(UUIDGenerator.getUUID());
				messageInfo.setDeleted(0);
				messageInfo.setMobile(userf.getMobile());
				messageInfo.setType(1);// ??????
				messageInfo.setCreateTime(new Date());
				String name = tjyUser.getNickname();
				String contentdx="{name:\"" + name + "\",subname:\"" + AldyMessageUtil.SMSPRE + "\",type:\"???????????????????????????????????????\",money:\"" + num + "\"}";
				messageInfo.setContent(contentdx);
				messageInfo.setStatus(0);// ?????????
				messageInfo.setTemplateId(AldyMessageUtil.MsmTemplateId.NEGOTIATE_SUCCESS);
				messageInfoService.addMessageInfo(messageInfo);
				// ????????????
				String touser = bd.getCreateUserId();
				messageInfo = new MessageInfo();
				messageInfo.setId(UUIDGenerator.getUUID());
				messageInfo.setDeleted(0);
				messageInfo.setType(2);// ??????
				messageInfo.setToUserId(touser);
				messageInfo.setCreateTime(new Date());
				// ????????????
//				String content = "???" + AldyMessageUtil.SMSPRE + "????????????" + name + ",??????" + b.getTitles()
//						+ "???????????????????????????????????????????????????"+num+"J????????????????????????";
				String content = AldyMessageUtil.userRewardAccept(name, AldyMessageUtil.SMSPRE,num);
				String con = WxMsmUtil.getTextMessageContent(content);
				messageInfo.setContent(con);
				messageInfo.setTemplateId("BUSINESS_CAINA");
				messageInfo.setStatus(0);// ?????????
				messageInfo.setWxMsgType(1);///** ?????????????????????1???????????????2?????????????????? */
				messageInfoService.addMessageInfo(messageInfo);
				/**
				 * ??????????????????
				 */
				messageInfo = new MessageInfo();
				messageInfo.setId(UUIDGenerator.getUUID());
				messageInfo.setDeleted(0);
				messageInfo.setType(3);// ????????????
				messageInfo.setToUserId(touser);
				messageInfo.setCreateTime(new Date());
				messageInfo.setContent(content);
				messageInfo.setStatus(0);// ???????????????
				messageInfoService.addMessageInfo(messageInfo);
			}
			b.setRewardFinish(1);
			businessService.updateBusiness(b);
			return super.getSuccessAjaxResult();
		}else{
			return super.getAjaxResult("502", "?????????????????????????????????????????????", null);
		}
	}
	/**
	 * ??????
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/m/business/search")
	public String search(ModelMap modelMap){
		return "netWork/coopsearch";
	}
	@RequestMapping("/m/business/searchList")
	public String searchList(HttpServletRequest request,ModelMap map,String keywords){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		map.addAttribute("keywords", keywords);
		return "netWork/coopSearchList";
	}
	/**
	 * ??????
	 * @return
	 */
	@RequestMapping("/m/business/searchCoopList")
	public @ResponseBody Map searchCoopList(HttpServletRequest request,String title){
		List<Map<String, Object>> list = new ArrayList();
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		list = businessService.selectFrontBusiness(null,null, title, null, null,null,null,userId);
		for(Map<String, Object> m : list){
			m.put("createTime", CommUtil.getTimesToNow(CommUtil
					.formatLongDate(m.get("createTime"))));
		}
		return super.getSuccessAjaxResult("???????????????", list);
	}
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping("/m/business/acceptPage")
	public String acceptPage(HttpServletRequest request,HttpServletResponse response,String id,ModelMap map){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		map.addAttribute("id", id);
		return "netWork/acceptPage";
	}
}