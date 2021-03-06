package com.wing.socialcontact.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.wing.socialcontact.common.model.PageParam;
import com.wing.socialcontact.commons.util.ApplicationPath;
import com.wing.socialcontact.commons.util.ConstantDefinition;
import com.wing.socialcontact.config.MsgConfig;
import com.wing.socialcontact.config.OssConfig;
import com.wing.socialcontact.service.wx.api.IBusinessClassService;
import com.wing.socialcontact.service.wx.api.IHonorService;
import com.wing.socialcontact.service.wx.api.IInviteRecordService;
import com.wing.socialcontact.service.wx.api.IMessageInfoService;
import com.wing.socialcontact.service.wx.api.IReconPhotosService;
import com.wing.socialcontact.service.wx.api.IRewardClassService;
import com.wing.socialcontact.service.wx.api.ISysconfigService;
import com.wing.socialcontact.service.wx.api.ITjyUserService;
import com.wing.socialcontact.service.wx.api.IUserEmpiricalService;
import com.wing.socialcontact.service.wx.api.IUserFavService;
import com.wing.socialcontact.service.wx.api.IUserHonorService;
import com.wing.socialcontact.service.wx.api.IUserIntegralLogService;
import com.wing.socialcontact.service.wx.api.IWxUserService;
import com.wing.socialcontact.service.wx.bean.Honor;
import com.wing.socialcontact.service.wx.bean.InviteRecord;
import com.wing.socialcontact.service.wx.bean.MessageInfo;
import com.wing.socialcontact.service.wx.bean.TjyUser;
import com.wing.socialcontact.service.wx.bean.UserEmpirical;
import com.wing.socialcontact.service.wx.bean.UserFav;
import com.wing.socialcontact.service.wx.bean.UserHonor;
import com.wing.socialcontact.service.wx.bean.WxUser;
import com.wing.socialcontact.sys.action.BaseAction;
import com.wing.socialcontact.sys.bean.ListValues;
import com.wing.socialcontact.sys.service.IDistrictService;
import com.wing.socialcontact.sys.service.IListValuesService;
import com.wing.socialcontact.util.AldyMessageUtil;
import com.wing.socialcontact.util.RedisCache;
import com.wing.socialcontact.util.ServletUtil;
import com.wing.socialcontact.util.SpringContextUtil;
import com.wing.socialcontact.util.UUIDGenerator;
import com.wing.socialcontact.util.WeixinUtil;
import com.wing.socialcontact.util.WxMsmUtil;
import com.wing.socialcontact.util.im.IMUtil;

/**
 * ??????????????????
 * 
 * @author gaojun
 * 
 */
@Controller
@RequestMapping("/userhonor")
public class UserHonorAction extends BaseAction {

	@Autowired
	private IUserHonorService userHonorService;
	@Autowired
	private IHonorService honorService;
	@Autowired
	private ITjyUserService tjyUserService;
	@Autowired
	private IListValuesService listValuesService;
	@Autowired
	private IReconPhotosService reconPhotosService;
	@Autowired
	private IWxUserService wxUserService;
	@Autowired
	private ISysconfigService sysconfigService;
	@Autowired
	private IMessageInfoService messageInfoService;
	@Autowired
	private IUserFavService userFavService;
	@Autowired
	private IDistrictService districtService;
	@Autowired
	private IBusinessClassService businessClassService;
	@Autowired
	private IRewardClassService rewardClassService;
	@Resource
	protected IUserIntegralLogService userIntegralLogService;
	@Autowired
	private IInviteRecordService inviteRecordService;
	@Autowired
	private IUserEmpiricalService userEmpiricalService;

	private String imPrefix = ApplicationPath.getParameter(ConstantDefinition.IM_PREFIX);

	/**
	 * ????????????
	 * 
	 * @return
	 */
	@RequiresPermissions("userHonor:read")
	@RequestMapping("load")
	public String load(ModelMap map) {
		List<ListValues> values = listValuesService.selectListByType(12);
		map.addAttribute("jobs", values);
		List<ListValues> industrys = listValuesService.selectListByType(8001);
		map.addAttribute("industrys", industrys);

		List businesslist = businessClassService.selectAllClass(null, null);
		map.addAttribute("businesslist", businesslist);

		List rewardlist = rewardClassService.selectAllClass(null, null);
		map.addAttribute("rewardlist", rewardlist);

		List<UserEmpirical> userLevelList = userEmpiricalService.selectAllUserEmpirical();
		map.addAttribute("userLevelList", userLevelList);

		return "userHonor/userHonor_load";

	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequiresPermissions("userHonor:read")
	@RequestMapping("load1")
	public String load1(ModelMap map) {
		List<ListValues> values = listValuesService.selectListByType(12);
		map.addAttribute("jobs", values);
		List<ListValues> industrys = listValuesService.selectListByType(8001);
		map.addAttribute("industrys", industrys);
		return "userHonor/userHonor_load1";

	}

	/**
	 * ????????????????????????
	 * 
	 * @Title: load_ye
	 * @Description: TODO
	 * @param map
	 * @return
	 * @return: String
	 * @author: zengmin
	 * @date: 2017???5???23??? ??????2:18:45
	 */
	@RequestMapping("load_ye")
	public String load_ye(ModelMap map) {
		List<ListValues> values = listValuesService.selectListByType(12);
		map.addAttribute("jobs", values);
		List<ListValues> industrys = listValuesService.selectListByType(8001);
		map.addAttribute("industrys", industrys);
		return "userHonor/userHonor_load_ye";

	}

	/**
	 * ?????????id?????????
	 */
	@RequestMapping("citys")
	public ModelAndView getcitise(String pid) {

		return ajaxJsonEscape(districtService.selectDistrictBySuperId(pid));
	}

	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequiresPermissions("userHonor:read")
	@RequestMapping("load2")
	public String load2(ModelMap map) {
		List<ListValues> values = listValuesService.selectListByType(12);
		map.addAttribute("jobs", values);
		List<ListValues> industrys = listValuesService.selectListByType(8001);
		map.addAttribute("industrys", industrys);
		return "userHonor/userHonor_load2";

	}

	@RequestMapping("lookUpPage1")
	public String lookUpPage1(Integer type) {
		System.out.println("hello");
		return "userHonor/lookup";

	}

	@RequiresPermissions("userHonor:read")
	@RequestMapping("query")
	public ModelAndView query(PageParam param, String nickname, String true_name, String job, String industry,
			String mobile, String recon_mobile, String bind_phone, String orderBy, String level, String address) {
		return ajaxJsonEscape(userHonorService.queryUserListByparam(param, nickname, true_name, job, industry, mobile,
				recon_mobile, bind_phone, orderBy, level, address));
	}

	@RequiresPermissions("userHonor:read")
	@RequestMapping("query_select")
	public ModelAndView query_select(PageParam param, String nickname, String true_name, String job, String industry,
			String mobile, String recon_mobile, String bind_phone) {
		return ajaxJsonEscape(userHonorService.queryUserListByparam_select(param, nickname, true_name, job, industry,
				mobile, recon_mobile, bind_phone));
	}

	@RequestMapping("query2")
	public ModelAndView query(PageParam param, String userId) {
		UserHonor userHonor = new UserHonor();
		userHonor.setUserId(userId);
		return ajaxJsonEscape(userHonorService.selectAllUserHonor(param, userHonor));
	}

	/**
	 * ??????????????????
	 * 
	 * @Title: querySh
	 * @Description: TODO
	 * @param param
	 * @param nickname
	 * @param job
	 * @param industry
	 * @param mobile
	 * @param reconStatus
	 * @return
	 * @return: ModelAndView
	 * @author: zengmin
	 * @date: 2017???5???14??? ??????11:36:51
	 */
	@RequestMapping("querySh")
	public ModelAndView querySh(PageParam param, String true_name, String recon_mobile, String nickname, String job,
			String industry, String mobile, String reconStatus, String bind_phone) {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		if (StringUtils.hasLength(nickname)) {
			searchMap.put("nickname", nickname);
		}
		if (StringUtils.hasLength(job)) {
			searchMap.put("job", job);
		}
		if (StringUtils.hasLength(industry)) {
			searchMap.put("industry", industry);
		}
		if (StringUtils.hasLength(mobile)) {
			searchMap.put("mobile", mobile);
		}
		if (StringUtils.hasLength(true_name)) {
			searchMap.put("true_name", true_name);
		}
		if (StringUtils.hasLength(recon_mobile)) {
			searchMap.put("recon_mobile", recon_mobile);
		}
		if (StringUtils.hasLength(bind_phone)) {
			searchMap.put("bind_phone", bind_phone);
		}
		if (StringUtils.hasLength(reconStatus)) {
			searchMap.put("reconStatus", reconStatus);
		}

		return ajaxJsonEscape(userHonorService.queryShUserListByParam(param, searchMap));
	}

	/**
	 * ?????????banner????????????
	 * 
	 * @return
	 */
	@RequiresPermissions("userHonor:add")
	@RequestMapping("addPage")
	public String addPage(ModelMap map, String userId) {
		TjyUser user = tjyUserService.selectByPrimaryKey(userId);
		map.addAttribute("user", user);
		Honor honor = new Honor();
		List<Honor> honors = honorService.selectAllHonor(honor);
		map.addAttribute("honors", honors);
		return "userHonor/userHonor_add";

	}

	@RequiresPermissions("userHonor:delete")
	@RequestMapping("del")
	public ModelAndView del(String[] ids) {
		return ajaxDone(userHonorService.deleteUserHonor(ids));
	}

	@RequiresPermissions("userHonor:add")
	@RequestMapping("adduserHonor")
	public ModelAndView adduserHonor(UserHonor userHonor) {

		List<UserHonor> uhList = userHonorService.selectByUserIdAndHonorId(userHonor.getUserId(),
				userHonor.getHonorId());
		if (null != uhList && uhList.size() > 0) {
			return ajaxDoneTextError("??????????????????????????????");
		}
		userHonor.setCreateTime(new Date());
		return ajaxDone(userHonorService.addUserHonor(userHonor));

	}

	/**
	 * ?????????userHonor????????????
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("userHonor:update")
	@RequestMapping("updatePage")
	public String updatePage(String id, ModelMap map) {
		UserHonor userHonor = userHonorService.selectByPrimaryKey(id);
		if (userHonor == null) {
			return NODATA;
		}
		TjyUser user = tjyUserService.selectByPrimaryKey(userHonor.getUserId());
		map.addAttribute("user", user);
		map.addAttribute("dto", userHonor);
		Honor honor = new Honor();
		List<Honor> honors = honorService.selectAllHonor(honor);
		map.addAttribute("honors", honors);
		map.addAttribute("userHonor", userHonor);

		return "userHonor/userHonor_update";
	}

	/**
	 * ??????honor
	 * 
	 * @param honor
	 * @param errors
	 * @return
	 */
	@RequiresPermissions("userHonor:update")
	@RequestMapping("update")
	public ModelAndView update(UserHonor userHonor, Errors errors) {
		if (errors.hasErrors()) {
			ModelAndView mav = getValidationMessage(errors);
			if (mav != null)
				return mav;
		}
		List<UserHonor> uhList = userHonorService.selectByUserIdAndHonorId(userHonor.getUserId(),
				userHonor.getHonorId());
		if (null != uhList && uhList.size() > 0) {
			return ajaxDoneTextError("??????????????????????????????");
		}
		userHonor.setCreateTime(new Date());
		return ajaxDone(userHonorService.updateUserHonor(userHonor));

	}

	@RequestMapping("view")
	public String view(String id, ModelMap map) {
		List<Map<String, Object>> tjyUsers = userHonorService.queryUserListByparam(id);
		Map<String, Object> tjyUser = tjyUsers.get(0);
		// ??????????????????
		List<Map<String, Object>> imgList = reconPhotosService.selectByUserId(id);
		map.addAttribute("imgList", imgList);
		map.addAttribute("tjyUser", tjyUser);
		OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();
		map.addAttribute("ossurl", ossurl);
		return "userHonor/userHonor_view";
	}

	@RequestMapping("user_view")
	public String userView(String id, ModelMap map) {
		List<Map<String, Object>> tjyUsers = userHonorService.queryUserListByparam(id);

		UserFav userFav = new UserFav();
		userFav.setUserId(id);
		List<Map<String, Object>> userFavs = userFavService.selectAllUserFav(userFav);
		for (int i = 0; i < userFavs.size(); i++) {
			Map<String, Object> uf = (Map<String, Object>) userFavs.get(i);
			String fid = (String) uf.get("favId");
			ListValues lv = listValuesService.selectByPrimaryKey(fid);
			uf.put("favId", lv.getListValue());
		}
		map.addAttribute("userFavs", userFavs);

		Map<String, Object> tjyUser = tjyUsers.get(0);
		// ??????????????????
		List<Map<String, Object>> imgList = reconPhotosService.selectByUserId(id);
		map.addAttribute("imgList", imgList);
		map.addAttribute("tjyUser", tjyUser);
		OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();
		map.addAttribute("ossurl", ossurl);
		return "userHonor/user_view";
	}

	// ??????????????????

	@RequestMapping("validate")
	public ModelAndView validate(String phone) {
		boolean data = false;
		WxUser wxUserNew = wxUserService.selectByMobile(phone);
		if (null != wxUserNew) {
			data = true;
		}
		return ajaxJson(data);
	}

	// ????????????

	@RequestMapping("addUserPage")
	public String addUserPage(ModelMap map) {
		List<ListValues> values = listValuesService.selectListByType(12);
		map.addAttribute("jobs", values);
		List<ListValues> industrys = listValuesService.selectListByType(8001);
		map.addAttribute("industrys", industrys);
		// ?????????
		List provinceList = districtService.selectDistrictByType("1");
		map.addAttribute("provinceList", provinceList);

		OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();
		map.addAttribute("ossurl", ossurl);
		return "userHonor/user_add";
	}

	// ??????????????????
	@RequestMapping("addUser")
	public ModelAndView addUser(WxUser wxUserDto, Errors errors, TjyUser tjyUserDto) {
		if (errors.hasErrors()) {
			ModelAndView mav = getValidationMessage(errors);
			if (mav != null)
				return mav;
		}
		WxUser wxUserNew = wxUserService.selectByMobile(wxUserDto.getMobile());
		if (null != wxUserNew) {
			return ajaxDone(MsgConfig.MSG_KEY_FAIL);
		}
		OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();

		String imgUrl = wxUserDto.getImgUrl();
		if (null != imgUrl && imgUrl != "") {
			imgUrl = ossurl + imgUrl;
			wxUserDto.setImgUrl(imgUrl);

		}
		// ??????IM
		IMUtil.sendUser(imPrefix + wxUserNew.getId() + "", UUID.randomUUID().toString() + wxUserNew.getId(), "", "");
		IMUtil.updateUserOne(imPrefix + wxUserNew.getId() + "", wxUserNew.getNickName(), wxUserNew.getImgUrl());
		return ajaxDone(wxUserService.addWxUser(wxUserDto, tjyUserDto));
	}

	/**
	 * ????????????
	 */
	@RequestMapping("updateUserPage")
	public String updateUserPage(String id, ModelMap map) {

		TjyUser tjyUser = tjyUserService.selectByPrimaryKey(id);
		map.addAttribute("tjyUser", tjyUser);

		WxUser wxUser = wxUserService.selectByPrimaryKey(id);
		map.addAttribute("wxUser", wxUser);

		List<ListValues> values = listValuesService.selectListByType(12);
		map.addAttribute("jobs", values);
		List<ListValues> industrys = listValuesService.selectListByType(8001);
		map.addAttribute("industrys", industrys);

		OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();
		map.addAttribute("ossurl", ossurl);
		// ?????????
		List provinceList = districtService.selectDistrictByType("1");
		map.addAttribute("provinceList", provinceList);
		return "userHonor/user_update";
	}

	// ??????????????????
	@RequestMapping("updateUser")
	public ModelAndView updateUser(WxUser wxUserDto, Errors errors, TjyUser tjyUserDto) {
		if (errors.hasErrors()) {
			ModelAndView mav = getValidationMessage(errors);
			if (mav != null)
				return mav;
		}

		OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
		String ossurl = ossConfig.getOss_getUrl();
		String imgUrl = wxUserDto.getImgUrl();
		if (null != imgUrl && imgUrl.indexOf("http") == -1) {
			imgUrl = ossurl + imgUrl;
		} 
//		else {
//			String domain = ApplicationPath.getParameter("domain");
//			imgUrl = domain + "/wxfront/resource/img/icons/weixinHeader.jpg";
//		}

		WxUser wxUserNew = wxUserService.selectByPrimaryKey(tjyUserDto.getId());
		// ?????????????????????????????????????????????
		if (!wxUserNew.getMobile().equals(wxUserDto.getMobile())) {

			try {
				/**
				 * ???????????????
				 */
				WxUser wxUser = wxUserService.selectByMobile(wxUserDto.getMobile());
				if (null != wxUser) {
					if (!tjyUserDto.getId().equals(wxUser.getId() + "")) {
						return ajaxDone(MsgConfig.MSG_KEY_FAIL);
					}
				}
			} catch (Exception e) {
				System.out.println("?????????????????????");
				return ajaxDone(MsgConfig.MSG_KEY_FAIL);
			}
		}
		String oldMobile = wxUserNew.getMobile();
		wxUserNew.setNickName(wxUserDto.getNickName());
		wxUserNew.setUsername(wxUserDto.getNickName());
		wxUserNew.setMobile(wxUserDto.getMobile());
		wxUserNew.setSex(wxUserDto.getSex());
		wxUserNew.setImgUrl(imgUrl);
		boolean ret1 = wxUserService.updateWxUser(wxUserNew);

		TjyUser tjyUserNew = tjyUserService.selectByPrimaryKey(tjyUserDto.getId());
		tjyUserNew.setNickname(wxUserDto.getNickName());
		tjyUserNew.setTrueName(wxUserDto.getNickName());
		tjyUserNew.setProvince(tjyUserDto.getProvince());
		tjyUserNew.setCity(tjyUserDto.getCity());
		tjyUserNew.setAddress(tjyUserDto.getAddress());
		tjyUserNew.setCounty(tjyUserDto.getCounty());
		tjyUserNew.setComName(tjyUserDto.getComName());
		tjyUserNew.setAddress(tjyUserDto.getAddress());
		tjyUserNew.setJob(tjyUserDto.getJob());
		tjyUserNew.setIndustry(tjyUserDto.getIndustry());
		tjyUserNew.setHeadPortrait(imgUrl);
		tjyUserNew.setMobile(wxUserDto.getMobile());
		tjyUserNew.setReconMobile(wxUserDto.getMobile());
		tjyUserNew.setUserProfile(tjyUserDto.getUserProfile());
		boolean ret2 = tjyUserService.updateTjyUser(tjyUserNew);
		try {
			tjyUserService.remotingUpdateTjyUser(tjyUserNew, oldMobile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ??????IM
		TjyUser tNew = tjyUserService.selectByPrimaryKey(tjyUserDto.getId());
		IMUtil.updateUserOne(imPrefix + tNew.getId(), tNew.getNickname(), tNew.getHeadPortrait());

		// ?????????redis
		RedisCache redisCache = (RedisCache) SpringContextUtil.getBean("redisCache");

		redisCache.removeall("selPeerElite");
		redisCache.removeall("selcityElite");

		if (ret1 && ret2) {
			return ajaxDone(MsgConfig.MSG_KEY_SUCCESS);
		} else {
			return ajaxDone(MsgConfig.MSG_KEY_FAIL);
		}
	}

	@RequestMapping("shenhe")
	public ModelAndView shenhe(TjyUser tjyUser, Errors errors) {
		if (null == tjyUser.getReconStatus() || "".equals(tjyUser.getReconStatus())) {
			return ajaxDoneError();
		}

		WxUser wxUser = wxUserService.selectByPrimaryKey(tjyUser.getId());
		TjyUser o = tjyUserService.selectByPrimaryKey(tjyUser.getId());
		String oldMobile = o.getMobile();
		String loginid = ServletUtil.getMember().getId();// ????????????id
		boolean sendMsgFlag = true;
		// ?????????????????????????????????????????????
		if (tjyUser.getReconStatus().intValue() == o.getReconStatus().intValue()) {
			sendMsgFlag = false;
		}
		if (tjyUser.getReconStatus() == 2) {
			o.setReconStatus(tjyUser.getReconStatus());
			o.setReconUserId(loginid);
			o.setReconDate(new Date());
			o.setKfTelephone(tjyUser.getKfTelephone());
			o.setIsRealname(1);
			o.setNickname(o.getTrueName());
			o.setTjName(tjyUser.getTjName());
			o.setTjMobile(tjyUser.getTjMobile());
			o.setLastRegDate(tjyUser.getLastRegDate());
		} else if (tjyUser.getReconStatus() == 3) {
			o.setReconStatus(tjyUser.getReconStatus());
			o.setKfTelephone(tjyUser.getKfTelephone());
			o.setReconUserId(loginid);
			o.setReconDate(new Date());
			o.setNickname(o.getTrueName());
			o.setIsRealname(0);
			o.setTjName(tjyUser.getTjName());
			o.setTjMobile(tjyUser.getTjMobile());
			o.setLastRegDate(tjyUser.getLastRegDate());
		}
		boolean bo = tjyUserService.updateTjyUser(o);
//		tjyUserService.remotingUpdateTjyUser(o, oldMobile);
		if (bo) {
			// ??????????????????????????????
			wxUser.setNickName(o.getNickname());
			wxUser.setUsername(o.getNickname());
			wxUserService.updateWxUser(wxUser);
		}
		if (sendMsgFlag) {
			String result = "";
			String name = o.getNickname();
			String content = AldyMessageUtil.userreconaccept(name);
			// String content = "???" + AldyMessageUtil.SMSPRE + "????????????" + name +
			// "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
			// ????????????
			if (tjyUser.getReconStatus() == 2) {
				result = "??????";
				if (bo) {

					// ????????????1???????????????
					userIntegralLogService.addLntAndEmp(wxUser.getWxUserId(), "task_0018");

					// ???????????????????????????????????????????????????
					String openId = o.getOpenId();
					if (!StringUtils.isEmpty(openId)) {
						List<Map<String, Object>> irList = inviteRecordService.selectInviteRecordPageByOpenId(openId);
						if (null != irList && irList.size() > 0) {
							for (Map<String, Object> m : irList) {
								String userId = (String) m.get("userId");
								String id = (String) m.get("id");
								InviteRecord inviteRecord = inviteRecordService.selectById(id);
								String isIntegral = "0";
								if (!StringUtils.isEmpty(inviteRecord.getIsIntegral())) {
									isIntegral = inviteRecord.getIsIntegral();
								}
								if ("0".equals(isIntegral)) {
									// ?????????????????????+??????:task_0023
									if (!StringUtils.isEmpty(userId)) {
										userIntegralLogService.addLntAndEmp(userId, "task_0023");
									}
									inviteRecord.setIsIntegral("1");
									inviteRecordService.updateInviteRecord(inviteRecord);
								}
							}
						}
					}
					createWxUser(ApplicationPath.getParameter("wx_corpid_qyh"),
							ApplicationPath.getParameter("wx_secret_qyh"), tjyUser.getId());
				}
			} else {// ???????????????
				result = "?????????";
				content = AldyMessageUtil.userreconrefuse(name);
				// content = "???" + AldyMessageUtil.SMSPRE + "????????????" + name +
				// "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
			}

			MessageInfo messageInfo = new MessageInfo();
			messageInfo.setId(UUIDGenerator.getUUID());
			messageInfo.setDeleted(0);
			messageInfo.setType(3);// ??????
			messageInfo.setToUserId(wxUser.getWxUserId());
			messageInfo.setCreateTime(new Date());
			messageInfo.setContent(content);
			if (tjyUser.getReconStatus() == 2) {
				messageInfo.setTemplateId("recon_accept");
			} else {
				messageInfo.setTemplateId("recon_refuse");
			}
			messageInfo.setStatus(0);// ????????????????????????
			messageInfoService.addMessageInfo(messageInfo);

			messageInfo = new MessageInfo();
			messageInfo.setId(UUIDGenerator.getUUID());
			messageInfo.setDeleted(0);
			messageInfo.setType(2);// ??????
			messageInfo.setToUserId(wxUser.getWxUserId());
			messageInfo.setCreateTime(new Date());
			String con = WxMsmUtil.getTextMessageContent(content);
			messageInfo.setContent(con);
			messageInfo.setStatus(0);// ?????????
			messageInfo.setWxMsgType(1);
			messageInfoService.addMessageInfo(messageInfo);

			// ????????????
			messageInfo = new MessageInfo();
			messageInfo.setId(UUIDGenerator.getUUID());
			messageInfo.setDeleted(0);
			messageInfo.setMobile(wxUser.getMobile());
			messageInfo.setType(1);// ??????
			messageInfo.setCreateTime(new Date());
			messageInfo.setStatus(0);// ?????????
			if (tjyUser.getReconStatus() == 2) {
				messageInfo.setContent("{name:\"" + name + "\"}");
				messageInfo.setTemplateId(AldyMessageUtil.MsmTemplateId.RECON_SUCCESS);
			} else {
				messageInfo.setContent(
						"{name:\"" + name + "\",qm:\"" + AldyMessageUtil.SMSPRE + "\",dh:\"010-53118922\"}");
				messageInfo.setTemplateId(AldyMessageUtil.MsmTemplateId.RECON_FAILURE);
			}
			messageInfoService.addMessageInfo(messageInfo);
		}

		// ?????????redis
		RedisCache redisCache = (RedisCache) SpringContextUtil.getBean("redisCache");
		redisCache.removeall("selPeerElite");
		redisCache.removeall("selcityElite");

		return ajaxDoneSuccess();
	}

	/**
	 * ??????????????????
	 * 
	 * @Title: createWxUser
	 * @Description: TODO
	 * @param corpid
	 * @param secret
	 * @param userId
	 * @return
	 * @return: boolean
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:14:43
	 */
	private boolean createWxUser(String corpid, String secret, String userId) {
		WxUser user = wxUserService.selectByPrimaryKey(userId);
		if (null == user) {
			return false;
		}
		if (StringUtils.isEmpty(user.getWxUserId())) {
			user.setWxUserId(user.getId() + "");
			wxUserService.updateWxUser(user);
		}
		JSONObject oj = WeixinUtil.getUserByUserId(corpid, secret, user.getWxUserId());
		// ??????????????????
		if (null != oj) {
			try {
				int errcode = oj.getInt("errcode");
				if (errcode == 0) {
					// ????????????
					try {
						// String weixinid = oj.getString("weixinid");
						// ????????????: 1=????????????2=????????????4=?????????
						int status = oj.getInt("status");
						// user.setWxUserId(weixinid);
						if (status == 1) {
							user.setStatus(2);// ?????????????????????
							wxUserService.updateWxUser(user);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
						System.out.println(" weixinid is not dinfend ");
					}
					return true;
				} else if (errcode == 60111) {
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("userid", user.getId());
					m.put("name", user.getNickName());
					m.put("gender", user.getSex());
					String[] depts = { "4" };// ???????????????
					m.put("department", depts);
					m.put("mobile", user.getMobile());
					m.put("email", user.getEmail());
					m.put("weixinid", user.getWxUserId());
					JSONObject jsonObject = WeixinUtil.createUser(corpid, secret, WeixinUtil.toJson(m));
					if (null != jsonObject) {
						try {
							if (jsonObject.getInt("errcode") == 0) {
								System.out.println("?????????????????? id:" + user.getId() + " ?????????" + user.getUsername());
							} else if ("invalid weixinid".equals(jsonObject.getString("errmsg"))) {
								m.remove("weixinid");
								jsonObject = WeixinUtil.createUser(corpid, secret, WeixinUtil.toJson(m));
								if (null != jsonObject) {
									try {
										if (jsonObject.getInt("errcode") == 0) {
											System.out.println("invalid weixinid ?????????????????? id:" + user.getId() + " ?????????"
													+ user.getUsername());
										} else {
											System.out.println("?????????????????? errcode:{" + jsonObject.getInt("errcode")
													+ "} errmsg:{" + jsonObject.getString("errmsg") + "}  id:"
													+ user.getId() + " ?????????" + user.getUsername());
										}
									} catch (JSONException e) {
										System.out.println(
												"invalid weixinid ?????????????????? errcode:{" + jsonObject.getInt("errcode")
														+ "} errmsg:{" + jsonObject.getString("errmsg") + "}  id:"
														+ user.getId() + " ?????????" + user.getUsername());
									}
								}
							} else {
								System.out.println("?????????????????? errcode:{" + jsonObject.getInt("errcode") + "} errmsg:{"
										+ jsonObject.getString("errmsg") + "}  id:" + user.getId() + " ?????????"
										+ user.getUsername());
							}
						} catch (JSONException e) {
							System.out.println("?????????????????? errcode:{" + jsonObject.getInt("errcode") + "} errmsg:{"
									+ jsonObject.getString("errmsg") + "}  id:" + user.getId() + " ?????????"
									+ user.getUsername());
						}
					}
				}
			} catch (JSONException e) {
				// ????????????
				e.printStackTrace();
				System.out.println("??????user detail?????? errcode:{" + oj.getInt("errcode") + "} errmsg:{"
						+ oj.getString("errmsg") + "}  id:" + user.getId() + " ?????????" + user.getUsername());
			}
		}
		return false;
	}

}
