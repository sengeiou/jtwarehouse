package com.wing.socialcontact.front.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.tojoycloud.common.report.ResponseCode;
import com.tojoycloud.common.report.base.BaseAppAction;
import com.tojoycloud.report.RequestReport;
import com.tojoycloud.report.ResponseReport;
import com.wing.socialcontact.common.model.Member;
import com.wing.socialcontact.config.OssConfig;
import com.wing.socialcontact.front.util.EsapiTest;
import com.wing.socialcontact.service.wx.api.IAttentionService;
import com.wing.socialcontact.service.wx.api.IBannerService;
import com.wing.socialcontact.service.wx.api.IBusinessClassService;
import com.wing.socialcontact.service.wx.api.IBusinessDisscussService;
import com.wing.socialcontact.service.wx.api.IBusinessService;
import com.wing.socialcontact.service.wx.api.ICommentService;
import com.wing.socialcontact.service.wx.api.ICommentThumbupService;
import com.wing.socialcontact.service.wx.api.IRewardAnswerService;
import com.wing.socialcontact.service.wx.api.ITjyUserService;
import com.wing.socialcontact.service.wx.api.IUserIntegralLogService;
import com.wing.socialcontact.service.wx.api.IWxUserService;
import com.wing.socialcontact.service.wx.bean.Attention;
import com.wing.socialcontact.service.wx.bean.Business;
import com.wing.socialcontact.service.wx.bean.BusinessDisscuss;
import com.wing.socialcontact.service.wx.bean.Comment;
import com.wing.socialcontact.service.wx.bean.CommentThumbup;
import com.wing.socialcontact.service.wx.bean.RewardAnswer;
import com.wing.socialcontact.service.wx.bean.TjyUser;
import com.wing.socialcontact.service.wx.bean.WxUser;
import com.wing.socialcontact.util.Constants;
import com.wing.socialcontact.util.ServletUtil;
import com.wing.socialcontact.util.SpringContextUtil;
import tk.mybatis.mapper.util.StringUtil;

/**
 * ????????????action
 * 
 * @author ??????
 *
 */
@Controller
@RequestMapping("/m/app/cooperation")
public class CooperativeInformation extends BaseAppAction
{
	@Autowired
	private IBusinessClassService businessClassService;
	@Autowired
	private IBusinessService businessService; 
	@Autowired
	private IWxUserService wxUserService;
	@Autowired
	private ITjyUserService tjyUserService;
	@Autowired
	private IBannerService bannerService;
	@Autowired
	private IBusinessDisscussService businessDisscussService;
	@Autowired
	private ICommentThumbupService commentThumbupService;
	@Autowired
	private ICommentService commentService;
	@Autowired
	private IAttentionService attentionService;
	@Autowired
	private IRewardAnswerService rewardAnswerService;
	@Autowired
	private IUserIntegralLogService userIntegralLogService;
	/**
	 * ???????????????
	 * @author ??????
	 * */
	@RequestMapping("selBannerList")
	public @ResponseBody ResponseReport selBannerList(@RequestBody RequestReport rr)
	{
		String userId = rr.getUserProperty().getUserId();
		if (StringUtil.isEmpty(userId))
		{
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		//????????? ????????????id
		String columnType = Constants.BANNER_BUSINESS_ID;
		WxUser wxUser = wxUserService.selectByPrimaryKey(userId);
		if(wxUser==null){
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
		}
		List list = bannerService.selectBannerByUserId(userId, columnType);
		
		return super.getAjaxResult(rr, ResponseCode.OK, "????????????", list);
	}
	/**
	 * ??????????????????
	 * @author ??????
	 * */
	@RequestMapping("selectAllClass")
	public @ResponseBody ResponseReport selectAllClass(@RequestBody RequestReport rr)
	{
		String userId = rr.getUserProperty().getUserId();
		if (StringUtil.isEmpty(userId))
		{
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		//??????????????????
		List classList = businessClassService.selectAllClass(null,null);
		
		return super.getAjaxResult(rr, ResponseCode.OK, "????????????", classList);
	}
	
	/**
	 * ??????????????????/????????????
	 * @author ??????
	 * */
	@RequestMapping("selectFrontBusiness")
	public @ResponseBody ResponseReport selectFrontBusiness(@RequestBody RequestReport rr)
	{
		String userId = rr.getUserProperty().getUserId();
		if (StringUtil.isEmpty(userId))
		{
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		String type = rr.getDataValue("type");//1???????????????2????????????
		String page = rr.getDataValue("page");
		String size = rr.getDataValue("size");
		String bizType = rr.getDataValue("bizType");
		if (StringUtil.isEmpty(page))
		{
			page = "1";// ???????????????
		}
		if (StringUtil.isEmpty(size))
		{
			size = "10"; // ??????10???
		}
		if(StringUtil.isEmpty(type))
			type = "2";
		List<Map<String, Object>> list = new ArrayList();
		if(type.equals("1")){
			//????????????
			list = businessService.selectFrontBusiness(Integer.parseInt(page),Integer.parseInt(size), null, bizType, 1,null,1,null);
		}else if(type.equals("2")){
			//????????????
			list = businessService.selectFrontBusiness(Integer.parseInt(page),Integer.parseInt(size), null, bizType, null,null,null,null);
		}
		
		return super.getAjaxResult(rr, ResponseCode.OK, "????????????", list);
	}
//	/**
//	 * ??????
//	 * @author ??????
//	 * */
//	@RequestMapping("selListByType")
//	public @ResponseBody ResponseReport selListByType(@RequestBody RequestReport rr)
//	{
//		String userId = rr.getUserProperty().getUserId();
//		if (StringUtil.isEmpty(userId))
//		{
//			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
//		}
//		String type = rr.getDataValue("type");//1???????????????2????????????
//		String titles = rr.getDataValue("titles");
//		String page = rr.getDataValue("page");
//		String size = rr.getDataValue("size");
//		String bizType = rr.getDataValue("bizType");
//		if (StringUtil.isEmpty(page))
//		{
//			page = "1";// ???????????????
//		}
//		if (StringUtil.isEmpty(size))
//		{
//			size = "10"; // ??????10???
//		}
//		List<Map<String, Object>> list = new ArrayList();
//		list = businessService.selectFrontBusiness(Integer.parseInt(page),Integer.parseInt(size), titles, bizType, null,null,null,userId);
//		for(Map<String, Object> m : list){
//			m.put("createTime", CommUtil.getTimesToNow(CommUtil
//					.formatLongDate(m.get("createTime"))));
//		}
//		
//		return super.getAjaxResult(rr, ResponseCode.OK, "????????????", list);
//	}
	
	/**
	 * ????????????
	 * @author ??????
	 * */
	@RequestMapping("detailPage")
	public @ResponseBody ResponseReport detailPage(@RequestBody RequestReport rr)
	{
		String userId = rr.getUserProperty().getUserId();
		if (StringUtil.isEmpty(userId))
		{
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		String id = rr.getDataValue("id");
		if(StringUtil.isEmpty(id)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
		}
		String type = rr.getDataValue("type");//1???????????????2????????????
		if(StringUtil.isEmpty(type))
			type = "1";
		Map res = new HashMap();
		TjyUser tjyUser=tjyUserService.selectByPrimaryKey(userId);
		if("2".equals(type)){//????????????
			if(!"1".equals(tjyUser.getIsRealname()+"")){
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
			}
		}
		
		res.put("userId", userId);
		Map<String, Object> business = businessService.selectBusinessById(id);
		String uid = business.get("createUserId").toString();
		List ul = tjyUserService.getUserListById(uid);
		Map ulMap = (HashMap)ul.get(0);
		business.put("trueName", ulMap.get("true_name").toString());
		business.put("headPortrait", ulMap.get("head_portrait").toString());
		business.put("job", ulMap.get("job_name").toString());
		business.put("honorFlag", ulMap.get("honor_flag").toString());
		business.put("honorTitle", ulMap.get("honor_title").toString());
		business.put("comName", ulMap.get("com_name").toString());
		
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
			int isThumbup = 0;
			if (thumbupList.size() > 0) {
				isThumbup =1;
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
		return super.getAjaxResult(rr, ResponseCode.OK, "????????????", res);
	}
	
	/**
	 * ??????
	 * @return
	 */
	@RequestMapping("addAttention")
	public @ResponseBody ResponseReport addAttention(@RequestBody RequestReport rr){
		String userId = rr.getUserProperty().getUserId();
		if (StringUtil.isEmpty(userId))
		{
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		String fkId = rr.getDataValue("fkId");
		
		Attention attention = new Attention();
		attention.setCreateTime(new Date());
		attention.setFkId(fkId);
		attention.setUserId(userId);
		attention.setAttType("2");
		String resultStr = attentionService.saveOrDelAttention(attention);
		return super.getAjaxResult(rr, ResponseCode.OK, "????????????", resultStr);
	}
	
	/**
	 * ??????????????????
	 * @author ??????
	 * */
	@RequestMapping("selMyHzPage")
	public @ResponseBody ResponseReport selMyHzPage(@RequestBody RequestReport rr)
	{
		String userId = rr.getUserProperty().getUserId();
		if (StringUtil.isEmpty(userId))
		{
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		String type = rr.getDataValue("type");
		if(StringUtil.isEmpty(type))
			type="1";
		String page = rr.getDataValue("page");
		String size = rr.getDataValue("size");
		if (StringUtil.isEmpty(page))
		{
			page = "1";// ???????????????
		}
		if (StringUtil.isEmpty(size))
		{
			size = "10"; // ??????10???
		}
		TjyUser tjyUser=tjyUserService.selectByPrimaryKey(userId);
		if(!"1".equals(tjyUser.getIsRealname()+"")){
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "???????????????", null);
		}else {
			List list = new ArrayList();
			if(type.equals("1")){
				//??????????????????
				list = businessService.selectMyBusiness(userId,Integer.parseInt(page),Integer.parseInt(size));
			}else if(type.equals("2")){
				//??????????????????
				list = businessService.selectMyAttention(userId,Integer.parseInt(page),Integer.parseInt(size));
			}else if(type.equals("3")){
				//????????????
				list = businessDisscussService.selectMyBD(Integer.parseInt(page), Integer.parseInt(page),userId);
			}
			return super.getAjaxResult(rr, ResponseCode.OK, "????????????", list);
		}

	}
	
	/**
	 * ??????????????????????????????
	 * ??????????????????????????????
	 * @return
	 */
//	@RequestMapping("isRealname")
//	public @ResponseBody ResponseReport isRealname(@RequestBody RequestReport rr){
//		String userId = rr.getUserProperty().getUserId();
//		if (StringUtil.isEmpty(userId))
//		{
//			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
//		}
//		TjyUser tjyUser=tjyUserService.selectByPrimaryKey(userId);
//		String fkId = rr.getDataValue("fkId");
//		if(!"1".equals(tjyUser.getIsRealname()+"")){
//			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
//		}
//		
//		return super.getAjaxResult(rr, ResponseCode.OK, "?????????", fkId);
//	}
	/**
	 * ????????????
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("addBD")
	public @ResponseBody ResponseReport addBD(@RequestBody RequestReport rr){
		String userId = rr.getUserProperty().getUserId();
		if (StringUtil.isEmpty(userId))
		{
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		String fkId = rr.getDataValue("fkId");
		String content = rr.getDataValue("content");
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
		return super.getAjaxResult(rr, ResponseCode.OK, "????????????", resultStr);
	}
	/**
	 * ???????????????
	 * 
	 * @return
	 */
	@RequestMapping("selCommentByPid")
	public @ResponseBody ResponseReport selCommentByPid(@RequestBody RequestReport rr){
		String userId = rr.getUserProperty().getUserId();
		if (StringUtil.isEmpty(userId))
		{
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		Map mv = new HashMap();
		TjyUser tjyUser = tjyUserService.selectByPrimaryKey(userId);
		
		Comment subcomment = new Comment();
		CommentThumbup commentThumbup = new CommentThumbup();
		String parentId = rr.getDataValue("parentId");// ????????????id
		if (StringUtil.isEmpty(parentId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
		} else {
			subcomment.setParentId(parentId);
			commentThumbup.setPId(parentId);
		}
		//zhangfan??????  ??????????????????
		String type = rr.getDataValue("type")==null?"":rr.getDataValue("type");
		Map<String, Object> user = new HashMap<String, Object>();
		Comment comment = new Comment();
		if(type.equals("1")){//type ???1??? ??????????????????
			BusinessDisscuss b = businessDisscussService.selectById(parentId);
			if (null != b) {
				String _userId = b.getCreateUserId();
				comment.setId(b.getId());
				comment.setCreateTime(b.getCreateTime());
				comment.setCmeDesc(b.getContent());
				comment.setCmeType("2");
				if (!StringUtil.isEmpty(_userId)) {
					// ????????????
					user = wxUserService.queryUsersByid(_userId);
				}
			}
		}else if(type.equals("2")){//type ???2??? ????????????
			RewardAnswer ra = rewardAnswerService.selectById(parentId);
			if (null != ra) {
				String _userId = ra.getCreateUserId();
				comment.setId(ra.getId());
				comment.setCreateTime(ra.getCreateTime());
				comment.setCmeDesc(ra.getContent());
				comment.setCmeType("6");
				if (!StringUtil.isEmpty(_userId)) {
					// ????????????
					user = wxUserService.queryUsersByid(_userId);
				}
			}
			
		}else{
			comment = commentService.selectById(parentId);
			if (null != comment) {
				String _userId = comment.getUserId();
				if (!StringUtil.isEmpty(_userId)) {
					// ????????????
					user = wxUserService.queryUsersByid(_userId);
				}
			}
		}
		
		mv.put("user", user);
		mv.put("comment", comment);
		int count = commentThumbupService.selectcount(commentThumbup);
		mv.put("count", count);
		
		//Member me = (Member) request.getSession().getAttribute("me");
		//String _userId = me.getId();
		commentThumbup.setUserId(userId);

		List<Map<String, Object>> thumbupList = commentThumbupService
				.selectAllCommentThumbup(commentThumbup);
		boolean isThumbup = false;
		if (thumbupList.size() > 0) {
			isThumbup =true;
		} 
		//??????????????????????????????
		mv.put("isThumbup",isThumbup);
		
		List<Map<String, Object>> subCommentList = commentService
				.queryCommentbyPid(parentId);
		for (Map<String, Object> m : subCommentList) {
			if (StringUtil.isEmpty((String) (m.get("userId")))) {
				m.put("formname", "");
			} else {
				// ????????????
				Map<String, Object> subuser = wxUserService
						.queryUsersByid((String) (m.get("userId")));
				m.put("formname", (String) (subuser.get("nickname")));
				m.put("head_portrait", (String) (subuser.get("head_portrait")));
			}
		}
		mv.put("subCommentList", subCommentList);
		OssConfig ossConfig = (OssConfig) SpringContextUtil
				.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();
		mv.put("ossurl", ossurl);
		return super.getAjaxResult(rr, ResponseCode.OK, "??????", mv);
	}
	/**
	 * ????????????
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("addComment")
	public @ResponseBody ResponseReport addComment(@RequestBody RequestReport rr){
		String userId = rr.getUserProperty().getUserId();
		if (StringUtil.isEmpty(userId))
		{
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}

		Comment comment = new Comment();
		
		String cmeDesc = rr.getDataValue("cmeDesc");// ????????????
		String imgUrl = rr.getDataValue("imgUrl");// ????????????url
		String fkId = rr.getDataValue("fkId");// ????????????Id
		String cmeType = rr.getDataValue("cmeType");// ????????????
		String parentId = rr.getDataValue("parentId");// ????????????id
		if (StringUtil.isEmpty(cmeDesc)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
		} else {
			comment.setCmeDesc(cmeDesc);
		}
		if (!StringUtil.isEmpty(fkId)) {
			comment.setFkId(fkId);
		}
		cmeDesc = EsapiTest.stripXSS(cmeDesc);
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

		return super.getAjaxResult(rr, ResponseCode.OK, "????????????", null);
	}
	
	
	/**
	 * ????????????
	 * 
	 * @return
	 */
	
	@RequestMapping("thumbup")
	public @ResponseBody ResponseReport thumbup(@RequestBody RequestReport rr){
		String userId = rr.getUserProperty().getUserId();
		if (StringUtil.isEmpty(userId))
		{
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}

		Comment comment = new Comment();
		String id = rr.getDataValue("id");// ??????id
		if (StringUtil.isEmpty(id)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
		}
		//
		CommentThumbup commentThumbup = new CommentThumbup();
		commentThumbup.setPId(id);
		comment = commentService.selectById(id);
		String cmeType = "";
		if(comment!=null){
			cmeType = comment.getCmeType();
		}
		if(!StringUtil.isEmpty(cmeType)){
			if(cmeType.equals("1")){
				//????????????
				userIntegralLogService.addLntAndEmp(userId, "task_0014");
			}
		}
		
		commentThumbup.setUserId(userId);

		List<Map<String, Object>> thumbupList = commentThumbupService
				.selectAllCommentThumbup(commentThumbup);
		if (thumbupList.size() <= 0) {
			commentThumbupService.addCommentThumbup(commentThumbup);
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", "0");
		} else {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", "1");
		}
	}
	
}
