package com.wing.socialcontact.front.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tk.mybatis.mapper.util.StringUtil;

import com.tojoycloud.common.report.ResponseCode;
import com.tojoycloud.common.report.base.BaseAppAction;
import com.tojoycloud.report.RequestReport;
import com.tojoycloud.report.ResponseReport;
import com.wing.socialcontact.common.model.DataGrid;
import com.wing.socialcontact.common.model.PageParam;
import com.wing.socialcontact.service.wx.api.IActivityCancelService;
import com.wing.socialcontact.service.wx.api.IActivityDelayService;
import com.wing.socialcontact.service.wx.api.IActivityRefundService;
import com.wing.socialcontact.service.wx.api.IActivityService;
import com.wing.socialcontact.service.wx.api.IActivityUserService;
import com.wing.socialcontact.service.wx.api.IAttentionService;
import com.wing.socialcontact.service.wx.api.IBusinessDisscussService;
import com.wing.socialcontact.service.wx.api.IBusinessService;
import com.wing.socialcontact.service.wx.api.ICommentService;
import com.wing.socialcontact.service.wx.api.IDynamicService;
import com.wing.socialcontact.service.wx.api.IMessageInfoService;
import com.wing.socialcontact.service.wx.api.IMyCollectionService;
import com.wing.socialcontact.service.wx.api.INewsService;
import com.wing.socialcontact.service.wx.api.IProjectRecommendService;
import com.wing.socialcontact.service.wx.api.IProjectService;
import com.wing.socialcontact.service.wx.api.IProjectWillService;
import com.wing.socialcontact.service.wx.api.IRewardService;
import com.wing.socialcontact.service.wx.api.ITopicService;
import com.wing.socialcontact.service.wx.bean.Activity;
import com.wing.socialcontact.service.wx.bean.ActivityCancel;
import com.wing.socialcontact.service.wx.bean.ActivityDelay;
import com.wing.socialcontact.service.wx.bean.ActivityRefund;
import com.wing.socialcontact.service.wx.bean.ActivityUser;
import com.wing.socialcontact.service.wx.bean.Attention;
import com.wing.socialcontact.service.wx.bean.BusinessDisscuss;
import com.wing.socialcontact.service.wx.bean.Comment;
import com.wing.socialcontact.service.wx.bean.MessageInfo;
import com.wing.socialcontact.service.wx.bean.Project;
import com.wing.socialcontact.service.wx.bean.ProjectRecommend;
import com.wing.socialcontact.service.wx.bean.ProjectWill;
import com.wing.socialcontact.service.wx.bean.TjyNews;
import com.wing.socialcontact.service.wx.bean.Topic;
import com.wing.socialcontact.util.AldyMessageUtil;
import com.wing.socialcontact.util.BeanMapUtils;
import com.wing.socialcontact.util.UUIDGenerator;
import com.wing.socialcontact.util.WxMsmUtil;
/**
 * APP??????controller history
 *
 */
@Controller
@RequestMapping("/m/app/history")  //32
public class AppHistoryAction extends BaseAppAction{

	@Autowired
	private IActivityUserService activityUserService;
	@Autowired
    private IActivityService activityService;
	@Autowired
	private IMessageInfoService messageInfoService;
	@Autowired
	private IActivityRefundService activityRefundService;
	@Autowired
	private IActivityDelayService activityDelayService;
	@Autowired
	private IActivityCancelService activityCancelService;
	@Autowired
	private IProjectWillService projectWillService;
	@Autowired
	private IProjectService projectService;
	@Autowired
	private IProjectRecommendService projectRecommendService;
	@Autowired
	private IAttentionService attentionService;
	@Autowired
	private ITopicService topicService; 
	@Autowired
	private IBusinessService businessService; 
	@Autowired
	private IBusinessDisscussService businessDisscussService;
	@Autowired
	private IRewardService rewardService; 
	@Autowired
	private ICommentService commentService;
	@Autowired
	private IDynamicService dynamicService;
	@Autowired
	private INewsService newsService; 
	/**
	 * ??????????????????
	 */
	@RequestMapping("activity/iJoinedList")
	public  @ResponseBody ResponseReport iJoinedList(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			List list  = activityUserService.getmysignupbyid(userId,Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", list);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????  List list  = activityService.getmyactivitiesbyid(userId,page,size,status);
	 * 1-6   status
  	 */
	@RequestMapping("activity/myActivities")
	public  @ResponseBody ResponseReport myActivities(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		String status = rr.getDataValue("status");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(status)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			List list  = activityService.getmyactivitiesbyid(userId,Integer.valueOf(pageNum), Integer.valueOf(pageSize), Integer.valueOf(status));
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", list);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ??????????????????     status 1 ?????????       2?????????     5?????????
	 */
	@RequestMapping("activity/activitySignupList")
	public  @ResponseBody ResponseReport activitySignupList(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		String id = rr.getDataValue("activityId");
		String status = rr.getDataValue("status");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????pageSize??????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????pageNum??????", null);			
		}
		if(StringUtil.isEmpty(id)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????id??????", null);			
		}
		if(StringUtil.isEmpty(status)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????id??????", null);			
		}
		try{
			int signupStatus = 1;
			if("2".equals(status)){
				signupStatus = 4;
			}else if("3".equals(status)){
				signupStatus = 5;
			}
			Map res =  activityUserService.selectbyactivityid1(id,Integer.parseInt(pageNum),Integer.parseInt(pageSize),signupStatus);
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", res);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????
	 */
	@RequestMapping("activity/signupCheck")
	public  @ResponseBody ResponseReport signupCheck(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String signupId = rr.getDataValue("signupId");
		String checkType = rr.getDataValue("checkType");//1??????   2?????????
		
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(signupId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(checkType)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????checkType??????", null);			
		}
		try{
			int type = 1;
			if("1".equals(checkType)){
				type = 4;
			}else if("2".equals(checkType)){
				type = 5;
			}else{
					return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????checkType??????", null);			
			};
			String[] ids= signupId.split(",");
			for(String id :ids){
				activityUserService.updatestatusbyid(id, type);
				if(type==4){
					ActivityUser user = activityUserService.getactivityUserByid(id);
					Activity activity = activityService.getActivityByid(user.getActivityId());
					// ????????????
					String content = AldyMessageUtil.activityaccept(user.getUserName(),activity.getTitles(),activity.getId());
					sendmessage(user.getUserId(),content,2,"ACTIVITY_ACCEPT");
					sendmessage(user.getUserId(),content,4,"ACTIVITY_ACCEPT");
				}else if(type==5){
					ActivityUser user = activityUserService.getactivityUserByid(id);
					if(user.getPayPrice()!=0&&!StringUtils.isEmpty(user.getTransactionId())){
						ActivityRefund refund = new ActivityRefund();
						refund.setAmount(user.getPayPrice());
						refund.setCreateTime(new Date());
						refund.setStatus(0);
						refund.setType(1);
						refund.setUserId(user.getUserId());
						refund.setOrderId(user.getOrderId());
						refund.setActivityId(user.getActivityId());
						refund.setTransactionId(user.getTransactionId());
						boolean bo = activityRefundService.insertRefund(refund);
					}
					Activity activity = activityService.getActivityByid(user.getActivityId());
					String content = AldyMessageUtil.activityrefuse(user.getUserName(),activity.getTitles(),activity.getId());
					sendmessage(user.getUserId(),content,2,"ACTIVITY_REFUSE");
					sendmessage(user.getUserId(),content,4,"ACTIVITY_REFUSE");
				}
		}
			return super.getAjaxResult(rr,ResponseCode.OK, "??????", null);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "?????????", null);
		}
	}
	/**
	 * ???????????????????????????
	 */
	@RequestMapping("activity/signupRefund")
	public  @ResponseBody ResponseReport signupRefund(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String signupId = rr.getDataValue("signupId");
		String checkType = rr.getDataValue("checkType");//1??????   2?????????
		
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(signupId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			String[] ids= signupId.split(",");
			for(String id :ids){
				ActivityUser user = activityUserService.getactivityUserByid(id);
				user.setOrderStatus(3);
				user.setStatus(5);
				activityUserService.updatesignup(user);
				if(user.getPayPrice()!=0&&!StringUtils.isEmpty(user.getTransactionId())){
					ActivityRefund refund = new ActivityRefund();
					refund.setAmount(user.getPayPrice());
					refund.setCreateTime(new Date());
					refund.setStatus(0);
					refund.setType(1);
					refund.setUserId(user.getUserId());
					refund.setOrderId(user.getOrderId());
					refund.setActivityId(user.getActivityId());
					refund.setTransactionId(user.getTransactionId());
					boolean bo = activityRefundService.insertRefund(refund);
				}
			}
			return super.getAjaxResult(rr,ResponseCode.OK, "??????", null);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "?????????", null);
		}
	}
	/**
	 * ????????????
	 */
	@RequestMapping("activity/delayApply")
	public  @ResponseBody ResponseReport delayApply(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String activityId = rr.getDataValue("activityId");
		String startTime = rr.getDataValue("startTime");
		String endTime = rr.getDataValue("endTime");
		String signupTime = rr.getDataValue("signupTime");
		String description = rr.getDataValue("description");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(activityId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????activityId??????", null);			
		}
		if(StringUtil.isEmpty(startTime)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????startTime??????", null);			
		}
		if(StringUtil.isEmpty(endTime)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????endTime??????", null);			
		}
		if(StringUtil.isEmpty(signupTime)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????signupTime??????", null);			
		}
		if(StringUtil.isEmpty(description)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????description??????", null);			
		}
		try{
			ActivityDelay dto = new ActivityDelay();
			dto.setDescription(description);
			dto.setStartTime(new Date(Long.parseLong(startTime)));
			dto.setEndTime(new Date(Long.parseLong(endTime)));
			dto.setSignupTime(new Date(Long.parseLong(signupTime)));
			//??????????????????????????????????????????
			ActivityDelay parm = new ActivityDelay();
			parm.setActivityId(activityId);
			parm.setStatus(0);
			List<ActivityDelay> lst = activityDelayService.selectbyTerm(parm);
			if(lst.size()>0){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????????????????????????????????????????????????????", null);	
			}
			Activity activity = activityService.getActivityByid(activityId);
			activity.setIsdelay(1);
			String reString = activityService.updateActivity(activity);
			dto.setCreateTime(new Date());
			dto.setStatus(0);
			dto.setUserId(userId);
			activityDelayService.addDelay(dto);
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", null);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????
	 */
	@RequestMapping("activity/cancelApply")
	public  @ResponseBody ResponseReport cancelApply(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String activityId = rr.getDataValue("activityId");
		String description = rr.getDataValue("description");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(activityId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????activityId??????", null);			
		}
		if(StringUtil.isEmpty(description)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????description??????", null);			
		}
		try{
			ActivityCancel dto = new ActivityCancel();
			dto.setDescription(description);
			//??????????????????????????????????????????
			ActivityCancel parm = new ActivityCancel();
			parm.setActivityId(dto.getActivityId());
			parm.setStatus(0);
			List<ActivityCancel> lst = activityCancelService.selectbyTerm(parm);
			
			if(lst.size()>0){
				return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????????????????????????????????????????????????????", null);
			}
			
			Activity activity = activityService.getActivityByid(dto.getActivityId());
			activity.setIscancel(1);
			String reString = activityService.updateActivity(activity);
			
			dto.setCreateTime(new Date());
			dto.setStatus(0);
			dto.setUserId(userId);
			activityCancelService.addCancel(dto);
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", null);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????   type ??????  ???????????????0??????    ??????  1?????????    2?????????    4:??????
	 */
	/**
	 * ??????????????????
	 */
	@RequestMapping("article/collectedList")
	public  @ResponseBody ResponseReport articlecollectedList(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			List  res  = null;
//			res =  myCollectionService.getCollections(userId,1,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", res);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????
	 */
	@RequestMapping("article/addAttention")
	public  @ResponseBody ResponseReport articleaddAttention(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String id = rr.getDataValue("articleId");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(id)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
//			if(myCollectionService.iscollected(userId,id,1)){
//				return super.getAjaxResult(rr,ResponseCode.OK, "?????????", null);
//			}
//			myCollectionService.addcollect(userId,id,1);
			return super.getAjaxResult(rr,ResponseCode.OK, "?????????", null);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ??????????????????
	 */
	@RequestMapping("article/deleteAttention")
	public  @ResponseBody ResponseReport articleDeleteAttention(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String id = rr.getDataValue("articleId");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(id)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
//			myCollectionService.delCollection(id,userId,1);
			return super.getAjaxResult(rr,ResponseCode.OK, "???????????????", null);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????
	 */
	@RequestMapping("project/projectList")
	public  @ResponseBody ResponseReport projectList(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		String type = rr.getDataValue("type");//1??????   2 ?????????   3?????? 
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????pageSize??????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????pageNum??????", null);			
		}
		if(StringUtil.isEmpty(type)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "??????type??????", null);			
		}
		try{
			DataGrid grid = null;
			PageParam pageParam = new PageParam(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
			Project param = new Project();
			param.setShowEnable(1);
			param.setUserId(userId);
			if(Integer.parseInt(type)==1){//??????????????????
				param.setIsAttentioned(1);
				grid = projectService.selectAllProject(pageParam , BeanMapUtils.toMap(param));
			}else if(Integer.parseInt(type)==2){//?????????????????????
				param.setIsWilled(1);
				grid = projectService.selectAllProject(pageParam , BeanMapUtils.toMap(param));
				List<Project> list = grid.getRows();
				for(Project p : list){
					ProjectWill t = new ProjectWill();
					t.setPrjId(p.getId());
					DataGrid  dg = projectWillService.selectAllProjectWill(new PageParam(1,1),BeanMapUtils.toMap(t));
					p.setExtProp("will", dg.getRows().size()>0?dg.getRows().get(0):null);
				}
			}else if(Integer.parseInt(type)==3){//??????????????????
				ProjectRecommend t = new ProjectRecommend();
				grid = projectRecommendService.selectAllProjectRecommend(pageParam,BeanMapUtils.toMap(t));
			}
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", grid.getRows());
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????
	 */
	@RequestMapping("project/addAttention")
	public  @ResponseBody ResponseReport projectaddAttention(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String id = rr.getDataValue("projectId");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(id)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			Attention p = attentionService.getAttentionByFkIdAndUserId(userId, id);
			//?????????
			if(p!=null){
				return super.getAjaxResult(rr,ResponseCode.OK, "????????????", null);
			}else{
				Attention t = new Attention();
				t.setAttType("prj");
				t.setCreateTime(new Date());
				t.setDeleted(0);
				t.setFkId(id);
				t.setUserId(userId);
				attentionService.insertAttention(t);
			}
			//????????????????????????
			int counts = attentionService.selectCount(new Attention(null,id));
			Map map = new HashMap();
			map.put("attentionCounts", counts);
			return super.getAjaxResult(rr,ResponseCode.OK, "??????", map);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "?????????", null);
		}
	}
	/**
	 * ??????????????????
	 */
	@RequestMapping("project/deleteAttention")
	public  @ResponseBody ResponseReport projectdeleteAttention(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String id = rr.getDataValue("projectId");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(id)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			Attention p = attentionService.getAttentionByFkIdAndUserId(userId, id);
			//?????????
			if(p!=null){
				attentionService.deleteAttention(p);
			}
			//????????????????????????
			int counts = attentionService.selectCount(new Attention(null,id));
			Map map = new HashMap();
			map.put("attentionCounts", counts);
			return super.getAjaxResult(rr,ResponseCode.OK, "??????", map);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "?????????", null);
		}
	}
	/**
	 * ?????? ????????????
	 */
	@RequestMapping("topic/topicJoinedList")
	public  @ResponseBody ResponseReport topicJoinedList(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			List list = new ArrayList();
			list = topicService.selectMyVoteApp(userId,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", list);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ?????? ????????????
	 */
	@RequestMapping("topic/topicPublishList")
	public  @ResponseBody ResponseReport topicPublishList(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			List list = new ArrayList();
			list = topicService.selectMyTopicMy(userId,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", list);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ??????  (??????)
	 */
	@RequestMapping("topic/topicCollectedList")
	public  @ResponseBody ResponseReport topicCollectedList(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			List list = new ArrayList();
			list = topicService.selectMyAttentionApp(userId,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", list);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????/???????????? 
	 */
	@RequestMapping("topic/updataAttention")
	public  @ResponseBody ResponseReport updataTopicAttention(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String id = rr.getDataValue("topicId");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(id)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			Attention attention = new Attention();
			attention.setCreateTime(new Date());
			attention.setFkId(id);
			attention.setUserId(userId);
			attention.setAttType("1");
			String resultStr = attentionService.saveOrDelAttention(attention);
			return super.getAjaxResult(rr,ResponseCode.OK, resultStr, null);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ???????????? ?????????
	 */
	@RequestMapping("business/businessPublishList")
	public  @ResponseBody ResponseReport businessPublishList(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			List list = new ArrayList();
			//??????????????????
			list = businessService.selectMyBusiness(userId,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", list);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????
	 */
	@RequestMapping("business/myBusinessList")
	public  @ResponseBody ResponseReport myBusinessList(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			List list = new ArrayList();
			//????????????
			list = businessDisscussService.selectMyBD(Integer.parseInt(pageNum),Integer.parseInt(pageSize), userId);
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", list);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ??????????????????
	 */
	@RequestMapping("business/businessCollectedList")
	public  @ResponseBody ResponseReport businessCollectedList(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			List list = new ArrayList();
			//??????????????????
			list = businessService.selectMyAttention(userId,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", list);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ??????/??????????????????
	 */
	@RequestMapping("business/updataAttention")
	public  @ResponseBody ResponseReport updataBusinessAttention(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String id = rr.getDataValue("businessId");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(id)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			Attention attention = new Attention();
			attention.setCreateTime(new Date());
			attention.setFkId(id);
			attention.setUserId(userId);
			attention.setAttType("2");
			String resultStr = attentionService.saveOrDelAttention(attention);
			return super.getAjaxResult(rr,ResponseCode.OK, resultStr, null);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	
	/**
	 * ?????? ????????????
	 */
	@RequestMapping("reward/topicJoinedList")
	public  @ResponseBody ResponseReport rewardJoinedList(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			List list = new ArrayList();
			list = rewardService.selectMyReward(userId,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", list);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????????????????
	 */
	@RequestMapping("reward/rewardPublishList")
	public  @ResponseBody ResponseReport rewardPublishList(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			List list = new ArrayList();
			list = rewardService.selectFrontReward(Integer.parseInt(pageNum),Integer.parseInt(pageSize), null, null, userId,2, null,null);
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", list);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 *?????? (??????)
	 */
	@RequestMapping("reward/rewardCollectedList")
	public  @ResponseBody ResponseReport rewardCollectedList(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			List list = new ArrayList();
			list = rewardService.selectMyAttention(userId,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", list);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????/???????????? 
	 */
	@RequestMapping("reward/updataAttention")
	public  @ResponseBody ResponseReport updatarewardAttention(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String id = rr.getDataValue("rewardId");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(id)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			Attention attention = new Attention();
			attention.setCreateTime(new Date());
			attention.setFkId(id);
			attention.setUserId(userId);
			attention.setAttType("reward");
			String resultStr = attentionService.saveOrDelAttention(attention);
			return super.getAjaxResult(rr,ResponseCode.OK, resultStr, null);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????
	 */
	@RequestMapping("mycomment/dynamic")
	public  @ResponseBody ResponseReport mycommentdynamic(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			Comment comment = new Comment();
			comment.setUserId(userId);
			comment.setCmeType("5");// ????????????
			
			List<Map<String, Object>> commentList = commentService
					.selectAllCommentApp(comment,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
			for (Map<String, Object> m : commentList) {
				Map fkMap = dynamicService.getDynamicMapById((String)( m.get("fkId")));//??????
				m.put("fkMap", fkMap);
			}
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", commentList);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????
	 */
	@RequestMapping("mycomment/news")
	public  @ResponseBody ResponseReport mycommentnews(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			Comment comment = new Comment();
			comment.setUserId(userId);
			comment.setCmeType("1");// ????????????
			
			List<Map<String, Object>> commentList = commentService
					.selectAllCommentApp(comment,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
			for (Map<String, Object> m : commentList) {
				TjyNews  fkMap= newsService.selectByPrimaryKey((String) (m.get("fkId")));//??????
				m.put("fkMap", fkMap);
			}
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", commentList);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????
	 */
	@RequestMapping("mycomment/topic")
	public  @ResponseBody ResponseReport mycommenttopic(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			Comment comment = new Comment();
			comment.setUserId(userId);
			comment.setCmeType("3");// ????????????
			
			List<Map<String, Object>> commentList = commentService
					.selectAllCommentApp(comment,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
			for (Map<String, Object> m : commentList) {
				Topic fkMap = topicService.selectByPrimaryKey((String) (m.get("fkId")));
			    m.put("fkMap", fkMap);
			}
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", commentList);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ????????????
	 */
	@RequestMapping("mycomment/activity")
	public  @ResponseBody ResponseReport mycommentactivity(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			Comment comment = new Comment();
			comment.setUserId(userId);
			comment.setCmeType("4");// ????????????
			
			List<Map<String, Object>> commentList = commentService
					.selectAllCommentApp(comment,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
			for (Map<String, Object> m : commentList) {
				Activity  fkMap= activityService.getActivityByid((String) (m.get("fkId")));//??????
			    m.put("fkMap", fkMap);
			}
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", commentList);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ??????????????????
	 */ 
	@RequestMapping("mycomment/business")
	public  @ResponseBody ResponseReport mycommentbusiness(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			Comment comment = new Comment();
			comment.setUserId(userId);
			comment.setCmeType("2");// ????????????
			
			List<Map<String, Object>> commentList = commentService
					.selectAllCommentApp(comment,Integer.parseInt(pageNum),Integer.parseInt(pageSize));
			for (Map<String, Object> m : commentList) {
				BusinessDisscuss fkMap = businessDisscussService.selectByPrimaryKey((String) (m.get("fkId")));//??????
			    m.put("fkMap", fkMap);
			}
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", commentList);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ??????????????????
	 */
	/**
	 * ??????????????????
	 */
	/**
	 * ??????
	 */
	@RequestMapping("e9")
	public  @ResponseBody ResponseReport e9(@RequestBody RequestReport rr,HttpSession session, HttpServletResponse response) throws IOException{
		String userId =rr.getUserProperty().getUserId();
		String pageSize = rr.getDataValue("pageSize");
		String pageNum = rr.getDataValue("pageNum");
		if(StringUtil.isEmpty(userId)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "?????????", null);			
		}
		if(StringUtil.isEmpty(pageSize)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		if(StringUtil.isEmpty(pageNum)){
			return super.getAjaxResult(rr,ResponseCode.NotSupport, "????????????", null);			
		}
		try{
			return super.getAjaxResult(rr,ResponseCode.OK, "????????????", null);
		}catch(Exception e){
			return super.getAjaxResult(rr,ResponseCode.Error, "???????????????", null);
		}
	}
	/**
	 * ???????????????
	 */
	public void sendmessage(String touser,String content,int type,String templateid ){
		MessageInfo messageInfo = new MessageInfo();
		 messageInfo.setId(UUIDGenerator.getUUID());
		 messageInfo.setDeleted(0);
		 messageInfo.setType(type);// ??????
		 messageInfo.setToUserId(touser);
		 messageInfo.setCreateTime(new Date());
		 if(type==2){
				String con = WxMsmUtil.getTextMessageContent(content);
				messageInfo.setContent(con);
		}else{
			messageInfo.setContent(content);
		}
		 messageInfo.setTemplateId(templateid);
		 messageInfo.setStatus(0);// ?????????
		 messageInfo.setWxMsgType(1);///** ?????????????????????1???????????????2?????????????????? */
		 messageInfoService.addMessageInfo(messageInfo);
	}
}
