package com.wing.socialcontact.service.wx.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wing.socialcontact.common.model.DataGrid;
import com.wing.socialcontact.common.model.PageParam;
import com.wing.socialcontact.config.MsgConfig;
import com.wing.socialcontact.service.wx.api.IActivityRefundService;
import com.wing.socialcontact.service.wx.api.IActivityService;
import com.wing.socialcontact.service.wx.api.IActivityUserService;
import com.wing.socialcontact.service.wx.api.IMessageInfoService;
import com.wing.socialcontact.service.wx.api.ITjyUserService;
import com.wing.socialcontact.service.wx.api.IWalletLogService;
import com.wing.socialcontact.service.wx.api.IWxUserService;
import com.wing.socialcontact.service.wx.bean.Activity;
import com.wing.socialcontact.service.wx.bean.ActivityRefund;
import com.wing.socialcontact.service.wx.bean.ActivityUser;
import com.wing.socialcontact.service.wx.bean.MessageInfo;
import com.wing.socialcontact.service.wx.bean.TjyUser;
import com.wing.socialcontact.service.wx.bean.WalletLog;
import com.wing.socialcontact.service.wx.bean.WxUser;
import com.wing.socialcontact.service.wx.dao.ActivityDao;
import com.wing.socialcontact.sys.service.impl.BaseServiceImpl;
import com.wing.socialcontact.util.AldyMessageUtil;
import com.wing.socialcontact.util.CommUtil;
import com.wing.socialcontact.util.ConfigUtil;
import com.wing.socialcontact.util.UUIDGenerator;
import com.wing.socialcontact.util.WxMsmUtil;

/**
 * 
 * <p>Description: </p>
 * <p>Company: </p> 
 * @author zhangzheng
 */
@Service
@CacheConfig(cacheNames = "default")
public class ActivityServiceImpl extends BaseServiceImpl<Activity> implements
		IActivityService {
	
	@Resource
	private ActivityDao	activityDao;
	
	@Resource
	private IActivityUserService activityUserService;

	@Resource
	private IMessageInfoService messageInfoService;

	@Resource
	private IWxUserService wxUserService;

	@Resource
	private IWalletLogService walletLogService;

	@Resource
	private ITjyUserService tjyUserService;

	@Resource
	private IActivityRefundService activityRefundService;
	
	 public DataGrid selActivitys(PageParam param, Activity activity) {
	        DataGrid data=new DataGrid();
	        
	        String orderStr = param.getOrderByString();
	        PageHelper.startPage(param.getPage(), param.getRows());
	        Map parm = new HashMap();
	        parm.put("pattern", activity.getPattern());
	        parm.put("orderStr", orderStr);
	        List lst = activityDao.getclassMap(parm);
	        PageInfo page = new PageInfo(lst);
	        data.setRows(lst);
	        data.setTotal(page.getTotal());
	        
	        return data;
	    }
	/**
	 * @param param
	 * @param activity
	 * @return
	 */
	@Override
	public Object selectactivity(PageParam param, Activity activity,String userId) {
		DataGrid data=new DataGrid();
		PageHelper.startPage(param.getPage(), param.getRows());
		Map parm = new HashMap();
		parm.put("titles", activity.getTitles());
		parm.put("createTime", activity.getCreateTime());
		parm.put("pattern", activity.getPattern());
		parm.put("showEnable", activity.getShowEnable());
		parm.put("classId", activity.getClassId());
		parm.put("tag", activity.getTag());
		parm.put("userId", userId);
		List<Map<String,Object>> lst=activityDao.getclassMap(parm);

		PageInfo page = new PageInfo(lst);

		data.setRows(lst);
		data.setTotal(page.getTotal());
		
		return data;
	}
	/**
	 * 
	 * @param dto
	 * @return
	 */
	@Override
	public String addActivity(Activity dto) {
			int res = activityDao.insert(dto);
			if(res > 0){
				return MsgConfig.MSG_KEY_SUCCESS;
			}else{
				return MsgConfig.MSG_KEY_FAIL;
			}
	}

	/**
	 * @param id
	 * @return
	 */
	@Override
	public Activity getActivityByid(String id) {
		return activityDao.selectByPrimaryKey(id);
	}

	@Override
	public String updateActivity(Activity dto) {
		int res =activityDao.updateByPrimaryKey(dto);
			if(res>0){
				return MsgConfig.MSG_KEY_SUCCESS;
			}else{
				return MsgConfig.MSG_KEY_FAIL;
			}
	}
	@Override
	public String deleteactivity(String id) {
		/*if(super.deleteByPrimaryKeyCache(id, Activity.class)){
			return MsgConfig.MSG_KEY_SUCCESS;
		}else{
		}*/
		return MsgConfig.MSG_KEY_SUCCESS;
	}
	@Override
	public List<Map> getActivityByTerm(String tagid,String classid, Integer page,
			Integer size, String key) {
		Map parm = new HashMap();
		parm.put("title", key);
		parm.put("start", (page-1)*size);
		parm.put("size", size);
		parm.put("classId", classid);
		parm.put("tagid", tagid);
		List<Map> lst=activityDao.getActivityByTerm(parm);
		return lst;
	}
	@Override
	public List<Map> getActivityByTermf(Activity activity, Integer page,
			Integer size,String userId) {
		Map parm = new HashMap();
		parm.put("title", activity.getTitles());
		parm.put("start", (page-1)*size);
		parm.put("size", size);
		parm.put("classId", activity.getClassId());
		parm.put("tagid", activity.getTag());
		parm.put("recommendEnable", activity.getRecommendEnable());
		parm.put("recommendList", activity.getRecommendList());
		parm.put("pattern", activity.getPattern());
		parm.put("status", activity.getStatus());
		parm.put("province",activity.getProvince() );
		parm.put("city",activity.getCity() );
		parm.put("userId",userId);
		
		List<Map> lst=activityDao.getActivityByTerm(parm);
		return lst;
	}
	/**
	 * ????????????id????????????
	 */
	@Override
	public Map getactivityDetailByid(String id) {

		Map parm = new HashMap();
		parm.put("id", id);
		return activityDao.getactivityDetailByid(parm);
	}
	/**
	 * ????????????????????????
	 */
	@Override
	public List getmyactivitiesbyid(String userId, Integer page, Integer size, Integer status) {
		Map parm = new HashMap();
		parm.put("userId", userId);
		parm.put("start", (page-1)*size);
		parm.put("size", size);
		parm.put("status", status);
		return activityDao.getmyactivitiesbyid(parm);
	}
	/**
	 * ??????????????????????????????
	 */
	@Override
	public List<Activity> getjxzActivity() {
		// TODO Auto-generated method stub
		return activityDao.getjxzActivity();
	}
	/**
	 *?????????????????????????????????
	 */
	@Override
	public List<Activity> getbmjsActivities() {
		// TODO Auto-generated method stub
		return activityDao.getbmjsActivities();
	}
	/**
	 * ??????????????????????????????
	 */
	@Override
	public List<Activity> getbmzActivities() {
		// TODO Auto-generated method stub
		return activityDao.getbmzActivities();
	}
	@Override
	public void updateActivityUseSql(Activity dto,int status) {
		Map parm = new HashMap();
		parm.put("id", dto.getId());
		parm.put("status", dto.getStatus());
		activityDao.updateStatusById(parm);
		
		if(status==4){
			refuseSignup(dto);
		}
		if(status==5){
			jiesuanSignup(dto);
			activityDao.updateByPrimaryKey(dto);
		}
	}
	
	
	/**
	 * ???????????????
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
	@Override
	public void refuseSignup(Activity dto) {
		List<ActivityUser> signupusers = activityUserService.getUncheckedUsersByActivityid(dto.getId(), 1);
		for (ActivityUser activityUser : signupusers) {
			if (activityUser.getPayPrice() != 0&&!StringUtils.isEmpty(activityUser.getTransactionId())){
				// ????????????????????????
				ActivityRefund refund = new ActivityRefund();
				refund.setAmount(activityUser.getPayPrice());
				refund.setCreateTime(new Date());
				refund.setStatus(0);
				refund.setType(1);
				refund.setUserId(activityUser.getUserId());
				refund.setOrderId(activityUser.getOrderId());
				refund.setActivityId(activityUser.getActivityId());
				refund.setTransactionId(activityUser.getTransactionId());
				activityRefundService.insertRefund(refund);
			}
				// ?????????????????????????????????1???????????????????????????5??????AldyMessageUtil.SMSPRE????????????***??????????????????****??????????????????????????????????????????????????????????????????????????????
				String touser = activityUser.getUserId();
				// ????????????
//				String content = "???"+AldyMessageUtil.SMSPRE+"????????????" + activityUser.getUserName() + ",???????????????" + dto.getTitles()
//						+ "??????????????????????????????????????????????????????????????????<a href='"+ConfigUtil.DOMAIN+"/wxfront/m/activity/activityDetailPage.do?id="+dto.getId()+"'>????????????</a>";
				String content =AldyMessageUtil.activityrefuse(activityUser.getUserName(), dto.getTitles(), dto.getId());
				//??????
				sendmessage(touser,content,2,"ACTIVITY_REFUSE",null);
				//im
				sendmessage(touser,content,4,"ACTIVITY_REFUSE",null);
				activityUser.setStatus(5);
				activityUserService.updatesignup(activityUser);
		}
		
	}
	@Override
	public void jiesuanSignup(Activity dto) {
		// ????????????id???????????????????????????4????????????????????????????????????????????????
					List<ActivityUser> allsignups = activityUserService.getUncheckedUsersByActivityid(dto.getId(), 4);
					Double amount = 0.00;
					for (ActivityUser activityUser : allsignups) {
						amount += activityUser.getPayPrice();
						activityUser.setBalance(1);
						activityUserService.updatesignup(activityUser);
					}
					WxUser wxUser = wxUserService.selectByPrimaryKey(dto.getCreateUserId());
					wxUser.setAvailablebalance(CommUtil.add(wxUser.getAvailablebalance(), amount));
					// ??????????????????
					WalletLog walletLog = new WalletLog();
					walletLog.setType("1");// 1?????? 2J??? 3?????????
					walletLog.setAmount(amount);
					walletLog.setPdType("1");
					walletLog.setUserId(dto.getCreateUserId());
					walletLog.setDeleted("0");
					walletLog.setRemark("????????????");
					walletLog.setCreateTime(new Date());
					walletLog.setYeAmount(wxUser.getAvailablebalance());
					walletLog.setBusinessType(11);
					String bo = walletLogService.addWalletLog(walletLog);
					// ????????? ??????????????????
					if (!bo.equals("")) {
						System.out.println("??????????????????");
						TjyUser tjyUser = tjyUserService.selectByPrimaryKey(wxUser.getId() + "");
						// ????????????
						String czType = "";
						Double fy = walletLog.getAmount();
						boolean bo2 = wxUserService.updateWxUser(wxUser);
						// ????????????
						String name = tjyUser.getNickname();
						String mobile = tjyUser.getMobile();
						String content1="{name:\"" + name + "\",hdname:\"" + dto.getTitles() + "\",fy:\"" + fy + "\"}";
						
						//??????
						sendmessage(tjyUser.getId(),content1,1,AldyMessageUtil.MsmTemplateId.ACTIVITY_FINISH,mobile);
						
						// ????????????
						String content =AldyMessageUtil.activityover(name, dto.getTitles(), fy); 
//						String content = "???"+AldyMessageUtil.SMSPRE+"????????????" + name + "????????????????????????" + dto.getTitles() + "????????????????????????????????????" + fy
//								+ "??????????????????????????????????????????";
						//??????
						sendmessage(tjyUser.getId(),content,2,"ACTIVITY_FINISH",null);
						//im
						sendmessage(tjyUser.getId(),content,4,"ACTIVITY_FINISH",null);
					};
		
	}
	
	
	
}
