package com.wing.socialcontact.front.action;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tk.mybatis.mapper.util.StringUtil;

import com.tojoycloud.common.report.ResponseCode;
import com.tojoycloud.common.report.base.BaseAppAction;
import com.tojoycloud.report.RequestReport;
import com.tojoycloud.report.ResponseReport;
import com.wing.socialcontact.common.model.Member;
import com.wing.socialcontact.config.OssConfig;
import com.wing.socialcontact.front.util.EsapiTest;
import com.wing.socialcontact.service.wx.api.IActivityService;
import com.wing.socialcontact.service.wx.api.IActivityTagService;
import com.wing.socialcontact.service.wx.api.IAttentionService;
import com.wing.socialcontact.service.wx.api.IBusinessClassService;
import com.wing.socialcontact.service.wx.api.IBusinessDisscussService;
import com.wing.socialcontact.service.wx.api.IBusinessService;
import com.wing.socialcontact.service.wx.api.ICommentService;
import com.wing.socialcontact.service.wx.api.ICommentThumbupService;
import com.wing.socialcontact.service.wx.api.IDynamicOpLogService;
import com.wing.socialcontact.service.wx.api.IDynamicPayLogService;
import com.wing.socialcontact.service.wx.api.IDynamicPicService;
import com.wing.socialcontact.service.wx.api.IDynamicService;
import com.wing.socialcontact.service.wx.api.ILibraryClassService;
import com.wing.socialcontact.service.wx.api.ILibraryService;
import com.wing.socialcontact.service.wx.api.IMyCollectionService;
import com.wing.socialcontact.service.wx.api.INewsService;
import com.wing.socialcontact.service.wx.api.ITjyUserService;
import com.wing.socialcontact.service.wx.api.IUserIntegralLogService;
import com.wing.socialcontact.service.wx.api.IWalletLogService;
import com.wing.socialcontact.service.wx.api.IWxUserService;
import com.wing.socialcontact.service.wx.bean.Activity;
import com.wing.socialcontact.service.wx.bean.ActivityTag;
import com.wing.socialcontact.service.wx.bean.Attention;
import com.wing.socialcontact.service.wx.bean.Business;
import com.wing.socialcontact.service.wx.bean.Comment;
import com.wing.socialcontact.service.wx.bean.CommentThumbup;
import com.wing.socialcontact.service.wx.bean.Dynamic;
import com.wing.socialcontact.service.wx.bean.DynamicOpLog;
import com.wing.socialcontact.service.wx.bean.DynamicPayLog;
import com.wing.socialcontact.service.wx.bean.DynamicPic;
import com.wing.socialcontact.service.wx.bean.TjyLibrary;
import com.wing.socialcontact.service.wx.bean.TjyLibraryClass;
import com.wing.socialcontact.service.wx.bean.TjyUser;
import com.wing.socialcontact.service.wx.bean.WalletLog;
import com.wing.socialcontact.service.wx.bean.WxUser;
import com.wing.socialcontact.sys.service.IDistrictService;
import com.wing.socialcontact.sys.service.IListValuesService;
import com.wing.socialcontact.util.Constants;
import com.wing.socialcontact.util.DoubleUtil;
import com.wing.socialcontact.util.ServletUtil;
import com.wing.socialcontact.util.SpringContextUtil;

@Controller
@RequestMapping("/m/app/")
public class PublicAction extends BaseAppAction{
	 
	@Autowired
    private IActivityService activityService;
	@Autowired
	private ICommentService commentService;
	@Autowired
	private INewsService newsService; 
	@Autowired
	private IDynamicService dynamicService;
	@Autowired
	private ILibraryClassService libraryclassservice;
	@Autowired
	private ILibraryService libraryService;
	@Autowired
	private IUserIntegralLogService userIntegralLogService;
	@Autowired
	private IDynamicPicService dynamicPicService;
	@Autowired
	private  IDynamicPayLogService dynamicPayLogService;
	@Autowired
	private IWxUserService wxUserService;
	@Autowired
	private IDynamicOpLogService dynamicOpLogService;
	@Autowired
	private IListValuesService listValuesService;
	@Autowired
	private IWalletLogService walletLogService;
	@Autowired
	private IBusinessClassService businessClassService;
	@Autowired
	private IBusinessService businessService; 
	@Autowired
    private IActivityTagService activityTagService;
	@Autowired
	private IDistrictService districtService;
	@Autowired
	private ITjyUserService tjyUserService;
	@Autowired
	private IBusinessDisscussService businessDisscussService;
	@Autowired
	private ICommentThumbupService commentThumbupService;
	@Autowired
	private IAttentionService attentionService;
//	@Autowired
//	private IMyCollectionService  myCollectionService;
	/**
	 * ????????????????????????
	 * @param type ????????????
	 * @return
	 */
	@RequestMapping("aboutTojoy")
	public @ResponseBody ResponseReport abouttojoy(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response){
		try{
			String id=Constants.NEWS_CLASS_ID_TIANJIU;
			Map res = new HashMap();
			Map<String, Object> news = newsService.selectNewsById(id);
			OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
			String ossurl = ossConfig.getOss_getUrl();
			res.put("ossurl", ossurl);
			res.put("news", news);
			return super.getAjaxResult(rr,ResponseCode.OK, "???????????????", res);
		}catch(Exception e){
			e.printStackTrace();
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????????????????
	 * @param type 1:??????
	 * @return
	 */
	@RequestMapping("shareView")
	public @ResponseBody ResponseReport shareView(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response){
		try{
			String id=Constants.NEWS_CLASS_ID_SHARE;
			Map res = new HashMap();
			Map<String, Object> news = newsService.selectNewsById(id);
			OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
			String ossurl = ossConfig.getOss_getUrl();
			res.put("ossurl", ossurl);
			res.put("news", news);
			return super.getAjaxResult(rr,ResponseCode.OK, "???????????????", res);
		}catch(Exception e){
			e.printStackTrace();
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ????????????????????????
	 * @param request
	 * @param pageNum
	 * @param pageSize
	 * @param dynamicLoadTime ?????? 
	 * @param dyContent  ??????
	 * @return
	 */
	@RequestMapping("/dynamic/selectMyFriendDynamic")
	public @ResponseBody ResponseReport selectMyFriendDynamic(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) {
		
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		String loadtime = rr.getDataValue("dynamicLoadTime");
		String dyContent = rr.getDataValue("dyContent");
		String userId =rr.getUserProperty().getUserId();
		Long dynamicloadtime = null;
		if(!StringUtil.isEmpty(loadtime)){
			dynamicloadtime = Long.parseLong(loadtime);		}
		if(StringUtils.isEmpty(pageSize)){
			pageSize = "10";
		}
		//????????????userId ???????????????????????????????????????userId  ??????userId???????????????  
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);
		}
		if(userId == null || "".equals(userId))	{
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);
		}	
		if(StringUtil.isEmpty(pageNum) || StringUtil.isEmpty(pageSize) || !com.wing.socialcontact.util.StringUtil.isNumeric(pageNum) || !com.wing.socialcontact.util.StringUtil.isNumeric(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);
		}else{
			try{
				List dynamicList =dynamicService.selectAllUserDynamic1(userId, Integer.parseInt(pageNum), Integer.parseInt(pageSize),dyContent,dynamicloadtime);
				OssConfig ossConfig = (OssConfig) SpringContextUtil
						.getBean("ossConfig");
				String ossurl = ossConfig.getOss_getUrl();
				Map valueMap = new HashMap();
				valueMap.put("ossurl", ossurl);
				valueMap.put("dynamicList", dynamicList);
				return super.getAjaxResult(rr,ResponseCode.OK, "???????????????", valueMap);
			}catch(Exception e){
				e.printStackTrace();
				return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
			}
		}
	}
	
	/**
	 * ??????????????????????????????
	 * @param request
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/dynamic/selectMyFollowDynamic")
	public @ResponseBody ResponseReport selectMyFollowDynamic(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) {
		String userId =rr.getUserProperty().getUserId();
		String pageNum= rr.getDataValue("pageNum");
		String pageSize= rr.getDataValue("pageSize");
		if(userId == null || "".equals(userId))	{
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}	
		if(StringUtil.isEmpty(pageNum) || StringUtil.isEmpty(pageSize) || !com.wing.socialcontact.util.StringUtil.isNumeric(pageNum) || !com.wing.socialcontact.util.StringUtil.isNumeric(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);
		}else{
			try{
				List dynamicList =dynamicService.selectMyFollowDynamic1(userId, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
				OssConfig ossConfig = (OssConfig) SpringContextUtil
						.getBean("ossConfig");
				String ossurl = ossConfig.getOss_getUrl();
				Map valueMap = new HashMap();
				valueMap.put("ossurl", ossurl);
				valueMap.put("dynamicList", dynamicList);
				return super.getAjaxResult(rr,ResponseCode.OK, "???????????????", valueMap);
			}catch(Exception e){
				e.printStackTrace();
				return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
			}
		}
	}
	/**
	 * ??????????????????????????????
	 * @param request
	 * @param dynamicLoadTime
	 * @return
	 */
	@RequestMapping("/dynamic/selectNewMyFriendDynamicCount")
	public @ResponseBody ResponseReport selectNewMyFriendDynamicCount(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) {
			try{
				String loadtime = rr.getDataValue("dynamicLoadTime");
				String userId =rr.getUserProperty().getUserId();
				if(StringUtil.isEmpty(loadtime)){
					return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);		
				}
				Long dynamicloadtime = Long.parseLong(loadtime);
				if(StringUtil.isEmpty(userId)){
					return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);		
				}
				int newDynamicCount = dynamicService.selectCountAllUserDynamicByUserId(userId, dynamicloadtime);
				return super.getAjaxResult(rr,ResponseCode.OK, "???????????????", newDynamicCount);
			}catch(Exception e){
				e.printStackTrace();
				return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
			}
	}
	/**
	 * 
	 * dyContent ????????????
	 * visitType  ???????????????1???????????? 2??????????????????
	 * dynamicImg   ????????????
	 * mediaSeconds  ????????????
	 * mediaId  ??????id
	 * mediaPrice ????????????
	 * articlIid
	 * ??????????????????
	 */
	
	@RequestMapping("/add/dynamic/insertDynamic")
	public @ResponseBody ResponseReport insertDynamic(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) {
		try {
			Dynamic dynamic = new Dynamic();
			String dyContent = rr.getDataValue("dyContent");
			if(StringUtil.isEmpty(dyContent)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);		
			}
			String visitType = rr.getDataValue("visitType");
			String mediaSeconds = rr.getDataValue("mediaSeconds");
			String mediaId = rr.getDataValue("mediaId");
			String mediaPrice = rr.getDataValue("mediaPrice");
			dynamic = (Dynamic) rr.toBean(rr.getCommandInfo().getData(), Dynamic.class);
			String zjImgerJson = rr.getDataValue("dynamicImg");
			String articleId = rr.getDataValue("articleId");
			dynamic.setArticleid(articleId);
			dynamic.setDyContent(dyContent);
			dynamic.setVisitType(Integer.parseInt(visitType));
			if (!StringUtil.isEmpty(mediaId)) {
				dynamic.setMediaId(mediaId);
				dynamic.setMediaPrice(Long.parseLong(mediaPrice));
				dynamic.setMediaSeconds(Integer.parseInt(mediaSeconds));
			}
			dynamic.setDyContent(EsapiTest.stripXSS(dynamic.getDyContent()));
			String userId =rr.getUserProperty().getUserId();
			if(StringUtil.isEmpty(userId)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);		
			}
			//??????????????????????????????????????????10?????????????????????
			userIntegralLogService.addLntAndEmp(userId, "task_0004");
			
			String dynamicId = UUID.randomUUID().toString().replace("-", "");
			List<DynamicPic> dynamicPicList = new ArrayList(); 
			if(!StringUtil.isEmpty(zjImgerJson)){
				String[] zjImgerJsons = zjImgerJson.split(",");
				for (int i = 0; i < zjImgerJsons.length; i++) {
					String imgUrl = zjImgerJsons[i];
					if(!StringUtil.isEmpty(imgUrl)&&!"".equals(imgUrl)){
						int type = 1;
						DynamicPic dynamicPic = new DynamicPic();
						dynamicPic.setId(UUID.randomUUID().toString());
						dynamicPic.setDynamicId(dynamicId);
						dynamicPic.setCreateTime(new Date());
						dynamicPic.setPicUrl(imgUrl);
						dynamicPic.setUserId(userId);
						dynamicPic.setSortNum((double)i);
						dynamicPicList.add(dynamicPic);
					}
				}
			}
//			}else	if(StringUtil.isEmpty(mediaId)){
//				return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????????????????????????????", null);		
//			}
			dynamic.setStatus(1);
			dynamic.setUserId(userId);
			Date now = new Date();
			dynamic.setIssuedDate(now);
			dynamic.setCreateTime(now);
			dynamic.setIsStick(0);
			dynamic.setId(dynamicId);
			dynamic.setDyType("0");
			String articleid = dynamic.getArticleid();
			if(null!=articleid&&!"".equals(articleid)){
				TjyLibrary li = libraryService.getTjyLibraryByid(articleid);
				if(null!=li){
					TjyLibraryClass lic = libraryclassservice.getTjyLibraryClassByid(li.getOneclass());
					dynamic.setAdate(li.getCreateTime());
					dynamic.setAtitle(li.getTitle());
					dynamic.setAimgpath(li.getImgpath());
					dynamic.setAclassname(lic.getName());
				}
			}
			int count = dynamicService.insertDynamicSignup(dynamic);
			int dynamicPicCount = dynamicPicService.insertDynamicPicList(dynamicPicList);
			Map map = new HashMap();
			map.put("dynamicId", dynamicId);
			return super.getAjaxResult(rr,ResponseCode.OK, "???????????????", map);
		}catch(Exception e){
			e.printStackTrace();
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????????????????
	 *  String dynamicId
	 * @return
	 */
	@RequestMapping("/dynamic/isPayOfMedia")
	public @ResponseBody ResponseReport isPayOfMedia(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException {
		String userId =rr.getUserProperty().getUserId();
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);		
		}
		String dynamicId =rr.getDataValue("dynamicId");
		if (StringUtil.isEmpty(dynamicId)) {
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????",null);
		}
		Dynamic dynamic = dynamicService.getDynamicSignup(dynamicId);
		if(userId.equals(dynamic.getUserId())){
			return super.getAjaxResult(rr,ResponseCode.OK, "??????????????????????????????", 1);
		}
		long mediaPrice = dynamic.getMediaPrice();
		DynamicPayLog dynamicPayLog = new DynamicPayLog();
		dynamicPayLog.setUserId(userId);
		dynamicPayLog.setDynamicId(dynamicId);
		dynamicPayLog.setActionType(2);
		List dynamicPaylogList = dynamicPayLogService.selectAllDynamicPayLog(dynamicPayLog);
		if (dynamicPaylogList.size() <= 0) {
			if(mediaPrice == 0){
				return super.getAjaxResult(rr,ResponseCode.OK, "???????????????????????????", 1);
			}else{
				return super.getAjaxResult(rr,ResponseCode.Error, "???????????????????????????",0);
			}
		} else {
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????????????????", 1);
		}
	}
	/**
	 * ????????????
	 * @param request
	 * @param response
	 * @param dynamicId
	 * @param jcount
	 * @return
	 */
	@RequestMapping("/dynamic/payMedia")
	public @ResponseBody ResponseReport paymedia(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response){
		try{
			String userId =rr.getUserProperty().getUserId();
			String jcount = rr.getDataValue("jcount");
			String dynamicId = rr.getDataValue("dynamicId");
			if(StringUtil.isEmpty(userId)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
			}
			if(userId == null || "".equals(userId))	{
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
			}	
			if(StringUtil.isEmpty(jcount) || StringUtils.isEmpty(dynamicId)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????",null);
			}else{
					WxUser wxUser = wxUserService.selectByPrimaryKey(userId);
					if((wxUser.getJbAmount() != null ?wxUser.getJbAmount():0)  < Double.parseDouble(jcount)){
						return super.getAjaxResult(rr,ResponseCode.Error, "???????????????????????????!",null);
					}
					dynamicPayLogService.insertMediaPay(userId, dynamicId, jcount);
					return super.getAjaxResult(rr,ResponseCode.OK, "???????????????",null);
			}
		}catch(Exception e){
			e.printStackTrace();
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????",null);
		}
	}
	/**
	 * ??????????????????
	 * String  dynamicId
	 */
	@RequestMapping("/dynamic/selectDynamicById")
	public @ResponseBody ResponseReport selectDynamicById(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) {
		try{
			String userId =rr.getUserProperty().getUserId();
			String dynamicId = rr.getDataValue("dynamicId");
			if(StringUtil.isEmpty(userId)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
			}
			if(userId == null || "".equals(userId))	{
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
			}	
			if(StringUtils.isEmpty(dynamicId)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????",null);
			}else{
					Map dynamicMap =dynamicService.selectDynamicById1(userId,dynamicId);
					Dynamic dynamic = dynamicService.getDynamicSignup(dynamicId);
					if(dynamic != null){
						if(dynamic.getVisitQuantity() != null){
							dynamic.setVisitQuantity(dynamic.getVisitQuantity()+1);
						}else{
							dynamic.setVisitQuantity(1l);
						}
						dynamicService.updateDynamicSignup(dynamic);
					}
					List dynamicList = new ArrayList();
					dynamicList.add(dynamicMap);
					OssConfig ossConfig = (OssConfig) SpringContextUtil
							.getBean("ossConfig");
					String ossurl = ossConfig.getOss_getUrl();
					Map valueMap = new HashMap();
					valueMap.put("ossurl", ossurl);
					valueMap.put("dynamicList", dynamicList);
					return super.getAjaxResult(rr,ResponseCode.OK, "???????????????",valueMap);
			}
		}catch(Exception e){
			e.printStackTrace();
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????",null);
		}
	}
	/**
	 * ????????????
	 * dynamicImg
	 * fromDynamicId
	 */
	@RequestMapping("/add/dynamic/insertForwardDynamic")
	public @ResponseBody ResponseReport insertForwardDynamic(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) {
		try {
			Dynamic dynamic = new Dynamic();
			String userId =rr.getUserProperty().getUserId();
			String fromDynamicId = rr.getDataValue("fromDynamicId");
			String zjImgerJson = rr.getDataValue("dynamicImg");
			String dyContent = rr.getDataValue("dyContent");
			String visitType = rr.getDataValue("visitType");
			dynamic = (Dynamic) rr.toBean(rr.getCommandInfo().getData(), Dynamic.class);
			String articleId = rr.getDataValue("articleId");
			dynamic.setArticleid(articleId);
			dynamic.setDyContent(dyContent);
			dynamic.setVisitType(Integer.parseInt(visitType));
			dynamic.setDyContent(EsapiTest.stripXSS(dynamic.getDyContent()));
			if(StringUtil.isEmpty(userId)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
			}
			String dynamicId = UUID.randomUUID().toString().replace("-", "");
			dynamic.setDyContent(EsapiTest.stripXSS(dynamic.getDyContent()));
			DynamicOpLog dynamicOpLogOld = new DynamicOpLog();
			dynamicOpLogOld.setUserId(userId);
			dynamicOpLogOld.setDynamicId(fromDynamicId);
			dynamicOpLogOld.setOpType(2);
			List dynamicOplogListOld = dynamicOpLogService.selectAllDynamicOpLog(dynamicOpLogOld);
			if (dynamicOplogListOld.size() > 0) {
				return super.getAjaxResult(rr,ResponseCode.Error, "????????????????????????????????????????????????", null);
			} 
			String[] zjImgerJsons = zjImgerJson.split(",");
			List<DynamicPic> dynamicPicList = new ArrayList(); 
			if(!StringUtil.isEmpty(zjImgerJson)){
				for (int i = 0; i < zjImgerJsons.length; i++) {
					String imgUrl = zjImgerJsons[i];
					if(!StringUtil.isEmpty(imgUrl)&&!"".equals(imgUrl)){
						int type = 1;
						DynamicPic dynamicPic = new DynamicPic();
						dynamicPic.setId(UUID.randomUUID().toString());
						dynamicPic.setDynamicId(dynamicId);
						dynamicPic.setCreateTime(new Date());
						dynamicPic.setPicUrl(imgUrl);
						dynamicPic.setUserId(userId);
						dynamicPic.setSortNum((double)i);
						dynamicPicList.add(dynamicPic);
					}
				}
			}
			dynamic.setStatus(1);
			dynamic.setUserId(userId);
			Date now = new Date();
			dynamic.setIssuedDate(now);
			dynamic.setCreateTime(now);
			dynamic.setIsStick(0);
			dynamic.setId(dynamicId);
			dynamic.setDyType("0");
			dynamic.setAllowComment(1);
			dynamic.setAllowReword(1);
			String articleid = dynamic.getArticleid();
			if(null!=articleid&&!"".equals(articleid)){
				TjyLibrary li = libraryService.getTjyLibraryByid(articleid);
				if(null!=li){
					TjyLibraryClass lic = libraryclassservice.getTjyLibraryClassByid(li.getOneclass());
					dynamic.setAdate(li.getCreateTime());
					dynamic.setAtitle(li.getTitle());
					dynamic.setAimgpath(li.getImgpath());
					dynamic.setAclassname(lic.getName());
				}
			}
			int count = dynamicService.insertDynamicSignup(dynamic);
			int dynamicPicCount = dynamicPicService.insertDynamicPicList(dynamicPicList);
			//??????????????????
			DynamicOpLog dynamicOpLog = new DynamicOpLog();
			dynamicOpLog.setUserId(userId);
			dynamicOpLog.setDynamicId(fromDynamicId);
			dynamicOpLog.setOpType(2);
			dynamicOpLog.setId(UUID.randomUUID().toString());
			dynamicOpLog.setActionTime(new Date());
			dynamicOpLogService.addDynamicOpLog(dynamicOpLog);
			Map map = new HashMap();
			map.put("dynamicId", dynamicId);
			return super.getAjaxResult(rr,ResponseCode.OK, "???????????????", map);
		} catch (Exception e) {
			e.printStackTrace();
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????
	 * 
	 * @return
	 * @param dynamicId
	 */
	@RequestMapping("/dynamic/thumbUp")
	public @ResponseBody ResponseReport thumbUp(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException {
		
		try{
			String userId =rr.getUserProperty().getUserId();
			String id = rr.getDataValue("dynamicId");
			if(StringUtil.isEmpty(userId)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
			}
			if(StringUtils.isEmpty(id)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????",null);
			}
			DynamicOpLog dynamicOpLog = new DynamicOpLog();
			dynamicOpLog.setUserId(userId);
			dynamicOpLog.setDynamicId(id);
			dynamicOpLog.setOpType(1);
			
			List dynamicOplogList = dynamicOpLogService.selectAllDynamicOpLog(dynamicOpLog);
			
			if (dynamicOplogList.size() <= 0) {
				dynamicOpLog.setId(UUID.randomUUID().toString());
				dynamicOpLog.setActionTime(new Date());
				dynamicOpLogService.addDynamicOpLog(dynamicOpLog);
				return super.getAjaxResult(rr,ResponseCode.OK, "???????????????", null);
			} else {
				Map opLog = (Map)dynamicOplogList.get(0);
				dynamicOpLogService.deleteOpLogById((String)opLog.get("id"));
				return super.getAjaxResult(rr,ResponseCode.OK, "??????????????????", null);
			}
		}catch(Exception e){
			e.printStackTrace();
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ???????????????????????????????????????????????????
	 * 1??????   2????????????  3??????  4????????????   5????????????  6??????PK
	 */
	@RequestMapping("searchKeys")
	public @ResponseBody ResponseReport getSearchKeys(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response){
		
		try{
			String userId =rr.getUserProperty().getUserId();
			String type = rr.getDataValue("type");
			if(StringUtil.isEmpty(userId)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
			}
			if(StringUtils.isEmpty(type)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????",null);
			}
			Integer typeIn =0; 
			if("1".equals(type)){//????????????
				typeIn=9002;
			}else if("2".equals(type)){//??????????????????
				typeIn=9003;
			}else if("3".equals(type)){//????????????
				typeIn=9004;
			}else if("4".equals(type)){//??????????????????
				typeIn=9005;
			}else if("5".equals(type)){//??????????????????
				typeIn=9006;
			}else if("6".equals(type)){//??????PK??????
				typeIn=9007;
			}else{
				typeIn=0;
			}
			List list = new ArrayList();
			if(typeIn!=0){
				list = listValuesService.selectListByType(typeIn);
				return super.getAjaxResult(rr,ResponseCode.OK, "????????????",list);
			}else{
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????",null);
			}
		}catch(Exception e ){
			e.printStackTrace();
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ??????/??????????????????
	 * @param cmeDesc  ????????????
	 * @param fkId ????????????Id
	 * @param imgUrl  ????????????url
	 * @param cmeType ????????????  //1:??????   2????????? 3?????????  4????????? 5?????????
	 * @param parentId  ????????????id
	 */
	@RequestMapping("/add/comment/addComment")
	public @ResponseBody  ResponseReport addComment(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response)
			throws IOException {
		try{
			String userId =rr.getUserProperty().getUserId();
			String cmeDesc = rr.getDataValue("cmeDesc");
			String fkId = rr.getDataValue("fkId");
			String cmeType = rr.getDataValue("cmeType");
			String parentId = rr.getDataValue("parentId");
			String imgUrl = rr.getDataValue("imgUrl");
			if(StringUtil.isEmpty(userId)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
			}
			if(StringUtils.isEmpty(cmeDesc)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????",null);
			}
			Comment comment = new Comment();
			cmeDesc = EsapiTest.stripXSS(cmeDesc);//????????????
			comment.setCmeDesc(cmeDesc);
			String commentId = UUID.randomUUID().toString().replace("-", "");
			comment.setId(commentId);
			if (!StringUtil.isEmpty(fkId)) {
				comment.setFkId(fkId);
			}
			if (!StringUtil.isEmpty(cmeType)) {
				comment.setCmeType(cmeType);
				//cmeType;//1:??????   2????????? 3?????????  4????????? 5?????????
				if("5".equals(cmeType)){
					//???????????????????????????
					userIntegralLogService.addLntAndEmp(userId, "task_0005");
				}else if("3".equals(cmeType)){
					//???????????????????????????
					userIntegralLogService.addLntAndEmp(userId, "task_0010");
				}else if("1".equals(cmeType)){
					//???????????????????????????
					userIntegralLogService.addLntAndEmp(userId, "task_0015");
				}else{
					//???????????????????????????
					//userIntegralLogService.addLntAndEmp(userId, "task_0011");
				}
			}
			if (!StringUtil.isEmpty(imgUrl)) {
				comment.setImgUrl(imgUrl);
			}
			if (!StringUtil.isEmpty(parentId)) {
				comment.setParentId(parentId);
			}
			comment.setCreateTime(new Date());
			comment.setUserId(userId);
			Comment cm = new Comment();
			cm.setFkId(fkId);
			List cmList = commentService.selectAllComment(cm);
			if(null==cmList||cmList.size()==0){
				comment.setStatus(1);//1?????????
			}
			commentService.addComment(comment);
			Map map = new HashMap();
			map.put("commentId", commentId);
			return super.getAjaxResult(rr,ResponseCode.OK, "???????????????", map);
		}catch(Exception e){
			e.printStackTrace();
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	
	/**
	 * ????????????
	 */
	@RequestMapping("deleteComment")
	public @ResponseBody ResponseReport deleteComment(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response){
		
		try{
			String userId =rr.getUserProperty().getUserId();
			String commentId = rr.getDataValue("commentId");
			if(StringUtil.isEmpty(userId)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
			}
			if(StringUtils.isEmpty(commentId)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????",null);
			}
			commentService.deleteCommentsApp(commentId);
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????",null);
		}catch(Exception e ){
			e.printStackTrace();
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????
	 */
	@RequestMapping("getBusinessClasses")
	public @ResponseBody ResponseReport getBusinessClasses(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response){
		
		try{
			String userId =rr.getUserProperty().getUserId();
			if(StringUtil.isEmpty(userId)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
			}
			List list = businessClassService.selectAllClass(null,null);
				return super.getAjaxResult(rr,ResponseCode.OK, "????????????",list);
		}catch(Exception e ){
			e.printStackTrace();
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????
	 * @return
	 * @param titles  ??????
	 * @param bizType  ???????????????????????????????????? 
	 * @param appealType ???????????????1??????2??????
	 * @param appealSummary ????????????
	 * @param reward //J?????????
	 * @param startTime //????????????
	 * @param endTime//????????????
	 * @param appealDesc ????????????
	 * @param allowComment ??????????????????,1??????2?????????
	 * @param isShow ????????????????????? 1:???  2:???
	 * @throws ParseException 
	 * 
	 */
	@RequestMapping("/add/business/addBusiness")
	public @ResponseBody ResponseReport addBusiness(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws ParseException{
		try{
			String userId =rr.getUserProperty().getUserId();
			String titles = rr.getDataValue("titles");
			String bizType = rr.getDataValue("bizType");
			String appealType = rr.getDataValue("appealType");
			String appealSummary = rr.getDataValue("appealSummary");
			String reward = rr.getDataValue("reward");
			String startTime = rr.getDataValue("startTime");
			String endTime = rr.getDataValue("endTime");
			String appealDesc = rr.getDataValue("appealDesc");
			String allowComment = rr.getDataValue("allowComment");
			String isShow = rr.getDataValue("isShow");
			
			if(StringUtil.isEmpty(userId)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
			}
			if(StringUtils.isEmpty(reward)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????",null);
			}
			if(StringUtils.isEmpty(titles)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????",null);
			}
			titles= EsapiTest.stripXSS(titles);
			bizType= EsapiTest.stripXSS(bizType);
			appealType= EsapiTest.stripXSS(appealType);
			appealSummary= EsapiTest.stripXSS(appealSummary);
			appealDesc= EsapiTest.stripXSS(appealDesc);
			allowComment= EsapiTest.stripXSS(allowComment);
			WxUser user = wxUserService.selectById(userId);
			
			if(reward!=null&&!"".equals(reward)){
				//??????????????????j???
				Double doub = DoubleUtil.sub(user.getJbAmount()==null?0:user.getJbAmount(), Double.valueOf(reward));
				if(doub<0){
					return super.getAjaxResult(rr,ResponseCode.Error, "J????????????????????????",null);
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
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Long stime = Long.parseLong(startTime);
			Long etime = Long.parseLong(endTime);
			Business business = new Business();
			business.setTitles(titles);
			business.setCreateTime(new Date());
			business.setBizType(bizType);
			business.setAppealType(Integer.parseInt(appealType));
			business.setAppealSummary(appealSummary);
			business.setStartTime(new Date(stime));
			business.setEndTime(new Date(etime));
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
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????",null);
		}catch(Exception e){
			e.printStackTrace();
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ??????????????????
	 * ???????????? calssId
	 */
	@RequestMapping("/activity/selTagsList")
    public @ResponseBody ResponseReport selTagsList(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response){
		try{
			String userId =rr.getUserProperty().getUserId();
			if(StringUtil.isEmpty(userId)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
			}
			List returnList = new ArrayList();
			Map map = new HashMap();
			Map map1 = new HashMap();
			List<ActivityTag> tags = activityTagService.selTags("1");
			map.put("className", "????????????");
			map.put("tags", tags);
			returnList.add(map);
			 //?????? 
			tags = activityTagService.selTags("2");
			map1.put("className", "????????????");
	        map1.put("tags", tags);
	        returnList.add(map1);
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????",returnList);
		}catch(Exception e ){
			e.printStackTrace();
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
    }
	
	/**
	 * ????????????
	 */
	@RequestMapping("/district/getAllDistricts")
    public @ResponseBody ResponseReport selectDistricts(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response){
		try{
			String userId =rr.getUserProperty().getUserId();
			if(StringUtil.isEmpty(userId)){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
			}
			List returnList = new ArrayList();
			// ?????????
			List provinceList = districtService.selectDistrictByType("1");
			returnList = getDistricts(provinceList,1);
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????",returnList);
		}catch(Exception e ){
			e.printStackTrace();
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
    }
	//????????????
	public List getDistricts(List list,int x){
		x++;
		if(x<4){
			for(int i=0;i<list.size();i++){
				Map districts = (Map) list.get(i);
				String parentId= (String) districts.get("id");
				List sonList = districtService.selectDistrictBySuperId(parentId);
				if(sonList.size()>0){
					districts.put("sonList", sonList);
					getDistricts(sonList,x);
				}
			}
		}
		return list;
	}
	
	/**
	 * ????????????
	 * 
	 * titles  ??????
	   contents   ??????
	   imagePath  ??????
	   province  ???id
	   city   ???id
	   county  ???id
	   place  ????????????
	   startTime ????????????
	   endTime
	   signupTime
	   showUser
	   classId
	   tag
	   commentEnable
	   pattern
	   ticketPrice
	 * 
	 */
	 @RequestMapping("/add/activity/activityAdd")
	 public @ResponseBody ResponseReport addActivity(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response){
		 try{
			 String titles = rr.getDataValue("titles");//??????
			 String id = rr.getDataValue("id");//??????
			 String contents =  rr.getDataValue("contents");
			 String imagePath = rr.getDataValue("imagePath");
			 String province = rr.getDataValue("province");
			 String city = rr.getDataValue("city");
			 String county = rr.getDataValue("county");
			 String place = rr.getDataValue("place");
			 String showUser = rr.getDataValue("showUser");
			 String classId =  rr.getDataValue("classId");
			 String tag =  rr.getDataValue("tag");
			 String commentEnable =  rr.getDataValue("commentEnable");
			 String pattern =  rr.getDataValue("pattern");
			 String ticketPrice =  rr.getDataValue("ticketPrice");
			 String startTime =  rr.getDataValue("startTime");
			 String endTime =  rr.getDataValue("endTime");
			 String signupTime =  rr.getDataValue("signupTime");
			 if(StringUtil.isEmpty(startTime)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????startTime", null);			
			 }
			 if(StringUtil.isEmpty(endTime)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????endTime", null);			
			 }
			 if(StringUtil.isEmpty(signupTime)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????signupTime", null);			
			 }
			 if(StringUtil.isEmpty(ticketPrice)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????ticketPrice", null);			
			 }
			 if(StringUtil.isEmpty(titles)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????titles", null);			
			 }
			 if(StringUtil.isEmpty(pattern)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????pattern", null);			
			 }
			 if(StringUtil.isEmpty(contents)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????contents", null);			
			 }
			 if(StringUtil.isEmpty(imagePath)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????imagePath", null);			
			 }
			 if(StringUtil.isEmpty(province)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????province", null);			
			 }
			 if(StringUtil.isEmpty(city)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????city", null);			
			 }
			 if(StringUtil.isEmpty(county)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????county", null);			
			 }
			 if(StringUtil.isEmpty(place)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????place", null);			
			 }
			 if(StringUtil.isEmpty(showUser)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????showUser", null);			
			 }
			 if(StringUtil.isEmpty(classId)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????classId", null);			
			 }
			 if(StringUtil.isEmpty(tag)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????tag", null);			
			 }
			 if(StringUtil.isEmpty(commentEnable)){
				 return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????commentEnable", null);			
			 }
			 Activity activity= new Activity();
			 if(!StringUtil.isEmpty(id)){
				 activity.setId(id);
			 }
			 Long stime = Long.parseLong(startTime);
			 Long etime = Long.parseLong(endTime);
			 Long sptime = Long.parseLong(signupTime);
			 activity.setStartTime(new Date(stime));
			 activity.setEndTime(new Date(etime));
			 activity.setSignupTime(new Date(sptime));
			 activity.setTitles(titles);
			 activity.setClassId(classId);
			 activity.setTag(tag);
			 activity.setCity(city);
			 activity.setContents(contents);
			 activity.setCounty(county);
			 activity.setShowUser(Integer.parseInt(showUser));
			 activity.setCommentEnable(Integer.parseInt(commentEnable));
			 activity.setImagePath(imagePath);
			 activity.setPattern(Integer.parseInt(pattern));
			 activity.setPlace(place);
			 activity.setProvince(province);
			 activity.setTicketPrice(Double.parseDouble(ticketPrice));
			 activity.setTitles(EsapiTest.stripXSS(activity.getTitles()));
	    	 activity.setPlace(EsapiTest.stripXSS(activity.getPlace()));
	    	 activity.setContents(EsapiTest.stripXSS(activity.getContents()));
	    	 String userId =rr.getUserProperty().getUserId();
			 if(StringUtil.isEmpty(userId)){
			 	 return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
			 }
			TjyUser user = tjyUserService.selectById(userId);
			activity.setCreateTime(new Date());
			activity.setCreateUserId(userId);
			activity.setCreateUserName(user.getNickname());
			activity.setIsdelay(0);
			activity.setIscancel(0);
			activity.setSponsor(user.getComName());
			activity.setSponsorIntroduce(user.getComProfile());
			activity.setRecommendEnable(0);
			activity.setRecommendList(0);
			activity.setShowEnable(1);
			activity.setSort(0);
			activity.setIscod(0);
			if(null==activity.getId()||"".equals(activity.getId())){
				activity.setStatus(2);
				activityService.addActivity(activity);
			}else{
				activity.setStatus(1);
				activityService.updateActivity(activity);
			}
			//???????????????????????????30
			userIntegralLogService.addLntAndEmp(userId, "task_0007");
			 return super.getAjaxResult(rr,ResponseCode.OK, "???????????????", null);
		 }catch(Exception e){
			e.printStackTrace();
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		 }
	    }
	 
	 /**
		 * ??????j???
		 * @param request
		 * @param response
		 * @param dynamicId
		 * @param jCount
		 * @return
		 */
		@RequestMapping("/dynamic/rewardJ")
		public @ResponseBody ResponseReport rewardJ(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response){
			
			try{
				 String userId =rr.getUserProperty().getUserId();
				 if(StringUtil.isEmpty(userId)){
				 	 return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
				 }
				 String jcount = rr.getDataValue("jCount");
				 String dynamicId = rr.getDataValue("dynamicId");
				if(StringUtil.isEmpty(jcount) || StringUtils.isEmpty(dynamicId)){
						return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????",null);
				}else{
					WxUser user = wxUserService.selectById(userId);
					Double doub = DoubleUtil.sub(user.getJbAmount()==null?0:user.getJbAmount(), Double.valueOf(jcount));
					if(doub<0){
						return super.getAjaxResult(rr,ResponseCode.Error, "J????????????????????????",null);
					}
						dynamicPayLogService.insertGratuity(userId, dynamicId, jcount);
						return super.getAjaxResult(rr,ResponseCode.OK, "???????????????", null);
				}
			}catch(Exception e){
				e.printStackTrace();
				return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
			}
		}
		
		/**
		 * ????????????
		 * @param businessId
		 */
		@RequestMapping("/business/businessDetail")
		public @ResponseBody ResponseReport businessDetail(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response){
			try{
				 String userId =rr.getUserProperty().getUserId();
				 if(StringUtil.isEmpty(userId)){
				 	 return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
				 }
				 String id = rr.getDataValue("businessId");
				if(StringUtil.isEmpty(id)){
						return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????",null);
				}else{
					Map res = new HashMap();
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
						return super.getAjaxResult(rr,ResponseCode.OK, "???????????????", null);
				}
			}catch(Exception e){
				e.printStackTrace();
				return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
			}
		}
		/**
		 * ????????????
		 * @param activityId
		 */
		@RequestMapping("/activity/activityDetail")
	    public @ResponseBody ResponseReport activityDetail(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response){
			try{
				 String userId =rr.getUserProperty().getUserId();
				 String id = rr.getDataValue("activityId");
				 if(StringUtil.isEmpty(userId)){
				 	 return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
				 }
				if(StringUtil.isEmpty(id)){
						return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????",null);
				}
				Map map = new HashMap();
				map = activityService.getactivityDetailByid(id);
//				boolean iscol=myCollectionService.iscollected(userId, id, 3);
				boolean iscol= false;
				map.put("collected", iscol);
				return super.getAjaxResult(rr,ResponseCode.OK, "???????????????", map);
			}catch(Exception e){
				e.printStackTrace();
				return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
			}
	    }
		//TDDO
		/**
		 * ??????????????????
		 */
		/**
		 * ????????????/????????????
		 */
}
