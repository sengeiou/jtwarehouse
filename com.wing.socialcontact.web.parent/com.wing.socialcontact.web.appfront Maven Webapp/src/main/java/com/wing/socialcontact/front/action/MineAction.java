package com.wing.socialcontact.front.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tk.mybatis.mapper.util.StringUtil;
import com.google.gson.Gson;
import com.tojoycloud.common.ConstantDefinition;
import com.tojoycloud.common.report.ResponseCode;
import com.tojoycloud.common.report.base.BaseAppAction;
import com.tojoycloud.report.RequestReport;
import com.tojoycloud.report.ResponseReport;
import com.wing.socialcontact.front.util.ApplicationPath;
import com.wing.socialcontact.service.im.api.IImFollowService;
import com.wing.socialcontact.service.im.api.IImFriendService;
import com.wing.socialcontact.service.wx.api.ICouponLogService;
import com.wing.socialcontact.service.wx.api.IDynamicService;
import com.wing.socialcontact.service.wx.api.IHonorService;
import com.wing.socialcontact.service.wx.api.IHzbManagerLogService;
import com.wing.socialcontact.service.wx.api.ILeaveMsgService;
import com.wing.socialcontact.service.wx.api.IMessageInfoService;
import com.wing.socialcontact.service.wx.api.IReconPhotosService;
import com.wing.socialcontact.service.wx.api.IThreeDimensionalCodeService;
import com.wing.socialcontact.service.wx.api.ITjyUserService;
import com.wing.socialcontact.service.wx.api.IUserFavService;
import com.wing.socialcontact.service.wx.api.IUserFriendimpressService;
import com.wing.socialcontact.service.wx.api.IUserHonorService;
import com.wing.socialcontact.service.wx.api.IUserIntegralEmpiricalService;
import com.wing.socialcontact.service.wx.api.IUserIntegralLogService;
import com.wing.socialcontact.service.wx.api.IUserLatestvistorService;
import com.wing.socialcontact.service.wx.api.IWalletLogService;
import com.wing.socialcontact.service.wx.api.IWxUserService;
import com.wing.socialcontact.service.wx.bean.Honor;
import com.wing.socialcontact.service.wx.bean.HzbManagerLog;
import com.wing.socialcontact.service.wx.bean.LeaveMsg;
import com.wing.socialcontact.service.wx.bean.MessageInfo;
import com.wing.socialcontact.service.wx.bean.ReconPhotos;
import com.wing.socialcontact.service.wx.bean.ThreeDimensionalCode;
import com.wing.socialcontact.service.wx.bean.TjyUser;
import com.wing.socialcontact.service.wx.bean.UserFav;
import com.wing.socialcontact.service.wx.bean.UserFriendimpress;
import com.wing.socialcontact.service.wx.bean.UserHonor;
import com.wing.socialcontact.service.wx.bean.UserIntegralEmpirical;
import com.wing.socialcontact.service.wx.bean.UserLatestvistor;
import com.wing.socialcontact.service.wx.bean.WxUser;
import com.wing.socialcontact.sys.bean.ListValues;
import com.wing.socialcontact.sys.bean.SyDistrict;
import com.wing.socialcontact.sys.service.IDistrictService;
import com.wing.socialcontact.sys.service.IListValuesService;
import com.wing.socialcontact.util.AldyMessageUtil;
import com.wing.socialcontact.util.DateUtils;
import com.wing.socialcontact.util.MD5Util;
import com.wing.socialcontact.util.MsmValidateBean;
import com.wing.socialcontact.util.RedisCache;
import com.wing.socialcontact.util.SpringContextUtil;
import com.wing.socialcontact.util.UUIDGenerator;
import com.wing.socialcontact.util.WxMsmUtil;
import com.wing.socialcontact.util.im.IMUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * APP??????????????????controller
 *
 */
@Controller
@RequestMapping("/m/app/mine")
public class MineAction extends BaseAppAction {

	@Autowired
	private IWxUserService wxUserService;
	@Autowired
	private IDynamicService dynamicService;
	@Autowired
	private ITjyUserService tjyUserService;
	@Autowired
	private IListValuesService listValuesService;
	@Autowired
	private IDistrictService districtService;
	@Autowired
	private IUserHonorService userHonorService;
	@Autowired
	private IUserLatestvistorService userLatestvistorService;
	@Autowired
	private IUserFriendimpressService userFriendimpressService;
	@Autowired
	private IImFollowService imFollowService;
	@Autowired
	private IImFriendService imFriendService;
	@Resource
	protected IUserIntegralLogService userIntegralLogService;
	@Autowired
	private IUserFavService userFavService;
	@Autowired
	private ILeaveMsgService leaveMsgService;
	@Autowired
	private IReconPhotosService reconPhotosService;
	@Autowired
	private IMessageInfoService messageInfoService;
	@Autowired
	private ICouponLogService couponLogService;
	@Autowired
	private IHzbManagerLogService hzbManagerLogService;
	@Autowired
	private IWalletLogService walletLogService;
	@Autowired
	private IHonorService honorService;
	@Autowired
	private IUserIntegralEmpiricalService userIntegralEmpiricalService;
	@Autowired
	private IThreeDimensionalCodeService threeDimensionalCodeService;
	private String imPrefix = ApplicationPath.getParameter(ConstantDefinition.IM_PREFIX);
	private static final String content = ApplicationPath.getParameter("tdcode.message.auditnotice.content");
	/**
	 * ??????/????????????  /m/app/mine/userInfo
	 */
	@RequestMapping(value = "myCenter")
	public @ResponseBody ResponseReport myCenter(@RequestBody RequestReport rr, HttpServletRequest request) {
		try {
			String userId = rr.getUserProperty().getUserId();
			if (StringUtil.isEmpty(userId)) {
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
			}
			Map map = new HashMap();
			Map returnuser = new HashMap();
			TjyUser tjyuser = tjyUserService.selectByPrimaryKey(userId);
			WxUser user = wxUserService.selectById(userId);
			if (!StringUtils.isEmpty(tjyuser.getIndustry())) {
				ListValues sd = listValuesService.selectByPrimaryKey(tjyuser.getIndustry());
				ListValues job = listValuesService.selectByPrimaryKey(tjyuser.getJob());
				tjyuser.setIndustry("");
				tjyuser.setJob("");
				if (null != sd) {
					tjyuser.setIndustry(sd.getListValue());
				}
				if (null != job) {
					tjyuser.setJob(job.getListValue());
				}
			}
			// ??????
			returnuser.put("sex", user.getSex());
			// ??????
			returnuser.put("birthday", user.getBirthday());
			// ???
			if (!StringUtils.isEmpty(tjyuser.getProvince())) {
				SyDistrict sc = districtService.selectByPrimaryKey(tjyuser.getProvince());
				String province = "";
				if (sc != null) {
					province = sc.getDisName();
				}
				returnuser.put("province", province);
			} else {
				returnuser.put("province", "");
			}
			// ???
			if (!StringUtils.isEmpty(tjyuser.getCity())) {
				SyDistrict sc = districtService.selectByPrimaryKey(tjyuser.getCity());
				String city = "";
				if (sc != null) {
					city = sc.getDisName();
				}
				returnuser.put("city", city);
			} else {
				returnuser.put("city", "");
			}
			// ???
			if (!StringUtils.isEmpty(tjyuser.getCounty())) {
				SyDistrict sc = districtService.selectByPrimaryKey(tjyuser.getCounty());
				String county = "";
				if (sc != null) {
					county = sc.getDisName();
				}
				returnuser.put("county", county);
			} else {
				returnuser.put("county", "");
			}
			if (StringUtils.isEmpty(tjyuser.getVisitQuantity())) {
				tjyuser.setVisitQuantity(0.0);
			}
			UserHonor userHonor = new UserHonor();
			userHonor.setUserId(userId);
			List<Map> myHonors = userHonorService.selectAllUserHonor(userHonor);
			List<Map> allHonors = honorService.selectAllHonor(new Honor());
			List honorLevel = new ArrayList();
			for (Map mh : myHonors) {
				Map resmap = new HashMap();
				String userhonorId = (String) mh.get("honor_id");// pic_path title
				resmap.put("title", mh.get("title"));// ???????????????
				resmap.put("picPath", mh.get("pic_path"));// ???????????????
				resmap.put("isLight", 1);
				resmap.put("honorId", userhonorId);
				honorLevel.add(resmap);
			}
			for (Map ah : allHonors) {
				boolean isLight = false;
				Map resmap = new HashMap();
				String id = (String) ah.get("id");
				resmap.put("title", ah.get("title"));
				resmap.put("picPath", ah.get("picPath2"));// ???????????????
				resmap.put("isLight", 0);
				resmap.put("honorId", id);
				for (Map mh : myHonors) {
					String userhonorId = (String) mh.get("honor_id");// pic_path title
					if (userhonorId.equals(id)) {
						isLight = true;
						break;
					}
				}
				if (!isLight) {
					honorLevel.add(resmap);
				}
			}
			map.put("honorLevel", honorLevel);
			map.put("tjyuser", tjyuser);
			map.put("user", returnuser);
			return super.getSuccessAjaxResult(rr, "????????????!", map);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "????????????", null);
		}
	}

	/**
	 *   ???????????? ??????
	 */
	@RequestMapping(value = "userHome")
	public @ResponseBody ResponseReport userHome(@RequestBody RequestReport rr, HttpServletRequest request) {
		try {
			String loginId = rr.getUserProperty().getUserId();
			String userId = rr.getDataValue("uid");
			if (StringUtil.isEmpty(userId)) {
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
			}
			if (StringUtil.isEmpty(loginId)) {
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
			}
			Map map = new HashMap();
			TjyUser tjyuser = tjyUserService.selectByPrimaryKey(userId);
			if (loginId.equals(userId)) {
				WxUser user = wxUserService.selectByPrimaryKey(userId);
				// modelMap.addAttribute("user", user);
				// map.put("user", user);
			} else {
				// ?????????+1
				double visitQuantity = 0.0;
				if (null == tjyuser.getVisitQuantity()) {
					visitQuantity = visitQuantity + 1;
				} else {
					visitQuantity = tjyuser.getVisitQuantity() + 1;
				}
				tjyuser.setVisitQuantity(visitQuantity);
				tjyUserService.updateTjyUser(tjyuser);
				// ??????????????????
				UserLatestvistor ul = new UserLatestvistor();
				ul.setStatus(0);
				ul.setUserId(userId);
				ul.setVistorUserId(loginId);
				List<Map<String, Object>> uls = userLatestvistorService.selectAllUserLatestvistor(ul);
				if ((null == uls) || uls.size() <= 0) {
					ul.setCreateTime(new Date());
					ul.setUpdateTime(new Date());
					userLatestvistorService.addUserLatestvistor(ul);
				} else {
					UserLatestvistor ultemp = userLatestvistorService.selectByPrimaryKey((String) uls.get(0).get("id"));
					ultemp.setUpdateTime(new Date());
					userLatestvistorService.updateUserLatestvistor(ultemp);
				}
			}
			if (!StringUtils.isEmpty(tjyuser.getIndustry())) {
				ListValues sd = listValuesService.selectByPrimaryKey(tjyuser.getIndustry());
				tjyuser.setIndustry("");
				if (null != sd) {
					tjyuser.setIndustry(sd.getListValue());
				}
			}
			if (!StringUtils.isEmpty(tjyuser.getJob())) {
				ListValues sd = listValuesService.selectByPrimaryKey(tjyuser.getJob());
				tjyuser.setJob("");
				if (null != sd) {
					tjyuser.setJob(sd.getListValue());
				}
			}
			if (StringUtils.isEmpty(tjyuser.getVisitQuantity())) {
				tjyuser.setVisitQuantity(0.0);
			}

			if (!StringUtils.isEmpty(tjyuser.getProvince())) {
				SyDistrict sd = districtService.selectByPrimaryKey(tjyuser.getProvince());
				tjyuser.setProvince(sd.getDisName());
			}
			if (!StringUtils.isEmpty(tjyuser.getCity())) {
				SyDistrict sd = districtService.selectByPrimaryKey(tjyuser.getCity());
				tjyuser.setCity(sd.getDisName());
			}
			if (!StringUtils.isEmpty(tjyuser.getCounty())) {
				SyDistrict sd = districtService.selectByPrimaryKey(tjyuser.getCounty());
				tjyuser.setCounty(sd.getDisName());
			}
			map.put("tjyuser", tjyuser);
			// ????????????
			List<Map<String, Object>> userFriendimpresss0 = userFriendimpressService.selectcountByUserId(userId, 0);
			// ?????????
			List<Map<String, Object>> INeed = userFriendimpressService.selectByUserIdAndType(userId, 1);
			// ????????????
			List<Map<String, Object>> IProvide = userFriendimpressService.selectByUserIdAndType(userId, 2);
			map.put("friendImpresss", userFriendimpresss0);
			map.put("INeed", INeed);
			map.put("IProvide", IProvide);
			// ?????????
			int attentionCount = imFollowService.findMyFollowUsersCount(userId);
			map.put("attentionCount", attentionCount);
			// ??????
			int friendCount = imFriendService.findMyFriendCountByUserId(userId);
			map.put("friendCount", friendCount);
			// ??????
			int fansCount = imFollowService.findMyFansUsersCount(userId);
			if (!loginId.equals(userId)) {
				/******** ???????????? ********/
				map.put("isFocus", imFollowService.isFollowUser(loginId, userId) ? "1" : "0");
			}
			map.put("fansCount", fansCount);
			return super.getSuccessAjaxResult(rr, "????????????!", map);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "????????????", null);
		}
	}

	/**
	 * ????????????id????????????????????????
	 * 
	 * List dynamicList =dynamicService.selectMyDynamicList(userId, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
	 */
	@RequestMapping(value = "getUserDynamicById")
	public @ResponseBody ResponseReport getUserDynamicById(@RequestBody RequestReport rr, HttpServletRequest request) {
		try {
			String uid = rr.getDataValue("uid");
			String userId = rr.getUserProperty().getUserId();
			String pageNum = rr.getDataValue("pageNum");
			String pageSize = rr.getDataValue("pageSize");
			if (StringUtil.isEmpty(userId)) {
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
			}
			if (StringUtil.isEmpty(uid)) {
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "??????uid??????", null);
			}
			if (StringUtil.isEmpty(pageNum)) {
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "??????pageNum??????", null);
			}
			if (StringUtil.isEmpty(pageSize)) {
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "??????pageSize??????", null);
			}
			List res = dynamicService.selectMyDynamicList(uid, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
			Map map = new HashMap();
			map.put("dynamicList", res);
			return super.getSuccessAjaxResult(rr, "????????????!", map);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "????????????", null);
		}
	}

	/**
	 * ????????????????????????
	 * 
	 */
	@RequestMapping("personalInfo")
	public @ResponseBody ResponseReport personalInfo(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) {
		try {
			String userId = rr.getUserProperty().getUserId();
			if (StringUtil.isEmpty(userId)) {
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
			}
			Map<String, Object> user = wxUserService.queryUsersByid(userId);

			if (!StringUtils.isEmpty(user.get("province"))) {
				SyDistrict sd = districtService.selectByPrimaryKey((String) user.get("province"));
				user.put("province", sd.getDisName());
			}
			if (!StringUtils.isEmpty(user.get("city"))) {
				SyDistrict sd = districtService.selectByPrimaryKey((String) user.get("city"));
				user.put("city", sd.getDisName());
			}
			if (!StringUtils.isEmpty(user.get("county"))) {
				SyDistrict sd = districtService.selectByPrimaryKey((String) user.get("county"));
				user.put("county", sd.getDisName());
			}
			Map map = new HashMap();
			map.put("user", user);
			UserFav userFav = new UserFav();
			userFav.setUserId(user.get("id") + "");
			List<Map<String, Object>> userFavs = userFavService.selectAllUserFav2(userFav);
			map.put("userFavs", userFavs);
			return super.getAjaxResult(rr, ResponseCode.OK, "???????????????", map);
		} catch (Exception e) {
			e.printStackTrace();
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ??????????????????
	 * userId
	 * mobile
	 * userProfile
	 * userSignature
	 * sex
	 * province
	 * city
	 * county
	 * birthday   yyyy-MM-dd
	 * homepagePic
	 * favs
	 * isfav  ??????????????????  ???1???
	 * 
	 *  
	 * app ????????????????????????
	 * nickname??????  ???  
	 * headPortrait ???????????????  ??? 
	 * industryId  ??????id
	 * comName ????????????
	 * comProfile  ????????????
	 * jobId ??????id
	 * 
	 */
	@RequestMapping(value = "editMyInfo")
	public @ResponseBody ResponseReport editMyInfo(@RequestBody RequestReport rr, HttpServletRequest request) {
		try {
			String userId = rr.getUserProperty().getUserId();
			if (StringUtil.isEmpty(userId)) {
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
			}
			TjyUser tjyuser = tjyUserService.selectByPrimaryKey(userId);
			WxUser user = wxUserService.selectByPrimaryKey(userId);
			String nickname = rr.getDataValue("nickname");
			if (!StringUtils.isEmpty(nickname)) {
				user.setTruename(nickname);
				user.setNickName(nickname);
				tjyuser.setNickname(nickname);
				tjyuser.setTrueName(nickname);
			}
			// ??????
			String headPortrait = rr.getDataValue("headPortrait");
			if (!StringUtils.isEmpty(headPortrait)) {
				tjyuser.setHeadPortrait(headPortrait);
				user.setImgUrl(headPortrait);
			}
			// ??????
			String industryId = rr.getDataValue("industryId");
			if (!StringUtils.isEmpty(industryId)) {
				tjyuser.setIndustry(industryId);
			}
			// ??????
			String job = rr.getDataValue("jobId");
			if (!StringUtils.isEmpty(job)) {
				tjyuser.setJob(job);
			}
			// ????????????
			String comName = rr.getDataValue("comName");
			if (!StringUtils.isEmpty(comName)) {
				tjyuser.setComName(comName);
			}
			// ????????????
			String comProfile = rr.getDataValue("comProfile");
			if (!StringUtils.isEmpty(comProfile)) {
				tjyuser.setComProfile(comProfile);
			}
			String mobile = rr.getDataValue("mobile");
			if (!StringUtils.isEmpty(mobile)) {
				user.setMobile(mobile);
			}
			String user_profile = rr.getDataValue("userProfile");
				tjyuser.setUserProfile(user_profile);
			String user_signature = rr.getDataValue("userSignature");
			if (!StringUtils.isEmpty(user_signature)) {
				tjyuser.setUserSignature(user_signature);
			}
			String sex = rr.getDataValue("sex");
			if (!StringUtils.isEmpty(sex)) {
				user.setSex(Integer.parseInt(sex));
			}
			String province = rr.getDataValue("province");
			if (!StringUtils.isEmpty(province)) {
				tjyuser.setProvince(province);
			}
			String city = rr.getDataValue("city");
			if (!StringUtils.isEmpty(city)) {
				tjyuser.setCity(city);
			}
			String county = rr.getDataValue("county");
			if (!StringUtils.isEmpty(county)) {
				if (!county.equals(tjyuser.getCounty())) {
					// ?????????redis,??????????????????redis
					RedisCache redisCache = (RedisCache) SpringContextUtil.getBean("redisCache");
					redisCache.evict("selcityElite" + userId);
				}
				tjyuser.setCounty(county);
			}

			String birthday = rr.getDataValue("birthday");
			if (!StringUtils.isEmpty(birthday)) {
				user.setBirthday(DateUtils.stringToDate(birthday, "yyyy-MM-dd"));
			}
			if (!StringUtils.isEmpty(user.getSex()) || !StringUtils.isEmpty(user.getBirthday())
					|| !StringUtils.isEmpty(tjyuser.getUserProfile()) || !StringUtils.isEmpty(tjyuser.getCity())) {
				// ???????????????????????????????????????????????????????????????
				userIntegralLogService.addLntAndEmp(tjyuser.getId(), "task_0019");
			}

			WxUser user2 = wxUserService.selectByPrimaryKey(userId);
			user2.setTruename(user.getTruename());
			user2.setMobile(user.getMobile());
			user2.setSex(user.getSex());
			user2.setBirthday(user.getBirthday());
			String homepagePic = rr.getDataValue("homepagePic");
			if (!StringUtils.isEmpty(homepagePic)) {
				tjyuser.setHomepagePic(homepagePic);
			}
			String favs = rr.getDataValue("favs");
			String isfav = rr.getDataValue("isfav");
			if ("1".equals(isfav) && StringUtils.isEmpty(favs)) {
				// ?????????????????????
				UserFav userFav = new UserFav();
				userFav.setUserId(userId);
				List<Map<String, Object>> userFavs = userFavService.selectAllUserFav(userFav);
				StringBuilder result = new StringBuilder();
				boolean flag = false;
				for (Map<String, Object> m : userFavs) {
					if (flag) {
						result.append(",");
					} else {
						flag = true;
					}
					result.append((String) m.get("id"));
				}
				// ?????????????????????
				String[] arr = { result.toString() };
				userFavService.deleteUserFavs(arr);
			} else if (!StringUtils.isEmpty(favs)) {
				String[] strArray = null;
				strArray = favs.split(",");
				// ????????????????????????
				UserFav userFav = new UserFav();
				userFav.setUserId(userId);
				List<Map<String, Object>> userFavs = userFavService.selectAllUserFav(userFav);
				StringBuilder result = new StringBuilder();
				boolean flag = false;
				for (Map<String, Object> m : userFavs) {
					if (flag) {
						result.append(",");
					} else {
						flag = true;
					}
					result.append((String) m.get("id"));
				}
				// ?????????????????????
				String[] arr = { result.toString() };
				userFavService.deleteUserFavs(arr);
				for (String m : strArray) {
					UserFav uf = new UserFav();
					uf.setUserId(userId);
					uf.setFavId(m);
					userFavService.addUserFav(uf);
				}
			}
			if (wxUserService.updateWxUser(user2) && tjyUserService.updateTjyUser(tjyuser)) {
				IMUtil.sendUser(imPrefix + user2.getId() + "", UUID.randomUUID().toString() + user2.getId(), "", "");
				IMUtil.updateUserOne(imPrefix + user2.getId() + "", user2.getNickName(), user2.getImgUrl());
				tjyUserService.remotingUpdateTjyUser(tjyuser, tjyuser.getMobile());
				return super.getAjaxResult(rr, ResponseCode.OK, "????????????", null);
			}
			return super.getAjaxResult(rr, ResponseCode.Error, "????????????", null);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "????????????", null);
		}
	}

	/**
	 * ??????????????????   List performs = imFriendService.findCommonFriendsByUserAndUser(userId, aUserId, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
	 */
	@RequestMapping("commonFriendList")
	public @ResponseBody ResponseReport commonFriendList(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) {
		try {
			String pageSize = rr.getDataValue("pageSize");
			String pageNum = rr.getDataValue("pageNum");
			String aUserId = rr.getDataValue("uid");
			String userId = rr.getUserProperty().getUserId();
			if (StringUtil.isEmpty(userId)) {
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
			}
			if (StringUtil.isEmpty(aUserId)) {
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
			}
			if (StringUtil.isEmpty(pageNum) || StringUtil.isEmpty(pageSize)
					|| !com.wing.socialcontact.util.StringUtil.isNumeric(pageNum)
					|| !com.wing.socialcontact.util.StringUtil.isNumeric(pageSize)) {
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
			}
			List res = imFriendService.findCommonFriendsByUserAndUser(userId, aUserId, Integer.parseInt(pageNum),
					Integer.parseInt(pageSize));
			return super.getAjaxResult(rr, ResponseCode.OK, "???????????????", res);
		} catch (Exception e) {
			e.printStackTrace();
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ????????????????????????????????????
	 */
	@RequestMapping("friendImpress")
	public @ResponseBody ResponseReport friendImpress(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		String uid = rr.getDataValue("uid");
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		if (uid == null || "".equals(uid)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
		}
		try {
			Map map = new HashMap();
			// ?????????????????????
			List<Map<String, Object>> impressList = listValuesService.selectListByType(8003);
			map.put("impressList", impressList);
			List<Map<String, Object>> userFriendimpresss = userFriendimpressService.selectByUserIdAndType(userId, uid,
					0);
			map.put("userFriendimpresss", userFriendimpresss);
			return super.getAjaxResult(rr, ResponseCode.OK, "????????????", map);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ????????????????????????????????????
	 */
	@RequestMapping("myImpress")
	public @ResponseBody ResponseReport myImpress(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		try {
			Map map = new HashMap();
			// ?????????????????????
			/*
			 * List<Map<String, Object>> impressList =
			 * listValuesService.selectListByType(8003); map.put("impressList",
			 * impressList);
			 */
			List<Map<String, Object>> yxList = userFriendimpressService.selectcountByUserId(userId, 0);
			map.put("userFriendimpresss", yxList);
			return super.getAjaxResult(rr, ResponseCode.OK, "????????????", map);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ??????????????????????????????
	 * impress   ??????id
	 */
	@RequestMapping("removeImpress")
	public @ResponseBody ResponseReport removeImpress(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		String impress = rr.getDataValue("impress");
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		if (impress == null || "".equals(impress)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
		}
		try {
			// ????????????
			if (!StringUtils.isEmpty(impress)) {
				String[] strArray = null;
				strArray = impress.split(",");
				for (String m : strArray) {
					UserFriendimpress uf = new UserFriendimpress();
					uf.setImpressId(m);
					uf.setType(0);
					List<Map<String, Object>> userFriendimpresss = userFriendimpressService
							.selectAllUserFriendimpress(uf);
					StringBuilder result = new StringBuilder();
					boolean flag = false;
					for (Map<String, Object> m0 : userFriendimpresss) {
						if (flag) {
							result.append(",");
						} else {
							flag = true;
						}
						result.append((String) m0.get("id"));
					}
					// ?????????????????????
					String[] arr = { result.toString() };
					// ????????????????????????
					userFriendimpressService.deleteUserFriendimpress(arr);
				}
			}
			return super.getAjaxResult(rr, ResponseCode.OK, "??????", null);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "?????????", null);
		}
	}

	/**
	 * ????????????????????????
	 * impress   id???????????????
	 * 
	 */
	@RequestMapping("addImpress")
	public @ResponseBody ResponseReport addImpress(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String loginId = rr.getUserProperty().getUserId();
		String userId = rr.getDataValue("uid");
		String impress = rr.getDataValue("impress");
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		try {
			if (!StringUtils.isEmpty(impress)) {
				String[] strArray = null;
				strArray = impress.split(",");
				UserFriendimpress userFriendimpress = new UserFriendimpress();
				userFriendimpress.setUserId(userId);
				userFriendimpress.setCreateUserId(loginId);
				userFriendimpress.setType(0);
				List<Map<String, Object>> userFriendimpresss = userFriendimpressService
						.selectAllUserFriendimpress(userFriendimpress);
				StringBuilder result = new StringBuilder();
				boolean flag = false;
				for (Map<String, Object> m : userFriendimpresss) {
					if (flag) {
						result.append(",");
					} else {
						flag = true;
					}
					result.append((String) m.get("id"));
				}
				// ?????????????????????
				String[] arr = { result.toString() };
				// ????????????????????????
				// userFriendimpressService.deleteUserFriendimpress(arr);
				for (String m : strArray) {
					UserFriendimpress uf = new UserFriendimpress();
					uf.setUserId(userId);
					uf.setImpressId(m);
					uf.setType(0);
					uf.setCreateUserId(loginId);
					List<Map<String, Object>> fiList = userFriendimpressService.selectAllUserFriendimpress(uf);
					if (fiList.size() == 0) {
						userFriendimpressService.addUserFriendimpress(uf);
					}
				}
			}
			return super.getAjaxResult(rr, ResponseCode.OK, "??????", null);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "?????????", null);
		}
	}

	/**
	 *???????????????
	 */
	@RequestMapping("iNeed")
	public @ResponseBody ResponseReport iNeed(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		try {
			Map map = new HashMap();
			// ?????????
			List<Map<String, Object>> impressList = listValuesService.selectListByType(8004);
			map.put("impressList", impressList);
			// ????????????
			List<Map<String, Object>> userFriendimpresss = userFriendimpressService.selectByUserIdAndType(userId, 1);
			map.put("userFriendimpresss", userFriendimpresss);
			return super.getAjaxResult(rr, ResponseCode.OK, "????????????", map);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ???????????????
	 */
	@RequestMapping("updateINeed")
	public @ResponseBody ResponseReport updateINeed(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String loginId = rr.getUserProperty().getUserId();
		String userId =rr.getUserProperty().getUserId();
		String impress = rr.getDataValue("impress");
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		try {
			// ????????????
			String type = "1";// ???????????????0??????????????? 1???????????? 2:???????????????
			UserFriendimpress userFriendimpress = new UserFriendimpress();
			userFriendimpress.setUserId(loginId);
			// userFriendimpress.setCreateUserId(loginId);
			userFriendimpress.setType(Integer.parseInt(type));
			List<Map<String, Object>> userFriendimpresss = userFriendimpressService
					.selectAllUserFriendimpress(userFriendimpress);
			StringBuilder result = new StringBuilder();
			boolean flag = false;
			for (Map<String, Object> m : userFriendimpresss) {
				if (flag) {
					result.append(",");
				} else {
					flag = true;
				}
				result.append((String) m.get("id"));
			}
			// ?????????????????????
			String[] arr = { result.toString() };
			// ????????????????????????????????????????????????
			userFriendimpressService.deleteUserFriendimpress(arr);
			if (!StringUtils.isEmpty(impress)) {
				String[] strArray = null;
				strArray = impress.split(",");
				if ("1".equals(type) || "2".equals(type)) {
					for (String m : strArray) {
						UserFriendimpress uf = new UserFriendimpress();
						uf.setUserId(loginId);
						uf.setImpressId(m);
						uf.setType(Integer.parseInt(type));
						uf.setCreateUserId(loginId);
						userFriendimpressService.addUserFriendimpress(uf);
					}
				}
			}
			return super.getAjaxResult(rr, ResponseCode.OK, "??????", null);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "?????????", null);
		}
	}

	/**
	 *??????????????????
	 */
	@RequestMapping("iProvide")
	public @ResponseBody ResponseReport iProvide(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		try {
			Map map = new HashMap();
			// ?????????
			List<Map<String, Object>> impressList = listValuesService.selectListByType(8005);
			map.put("impressList", impressList);
			// ????????????
			List<Map<String, Object>> userFriendimpresss = userFriendimpressService.selectByUserIdAndType(userId, 2);
			map.put("userFriendimpresss", userFriendimpresss);
			return super.getAjaxResult(rr, ResponseCode.OK, "????????????", map);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ??????????????????
	 */
	@RequestMapping("updateIProvide")
	public @ResponseBody ResponseReport updateIProvide(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		String loginId = rr.getUserProperty().getUserId();
		String impress = rr.getDataValue("impress");
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		try {
			// ????????????
			String type = "2";// ???????????????0??????????????? 1???????????? 2:???????????????
			UserFriendimpress userFriendimpress = new UserFriendimpress();
			userFriendimpress.setUserId(loginId);
			// userFriendimpress.setCreateUserId(loginId);
			userFriendimpress.setType(Integer.parseInt(type));
			List<Map<String, Object>> userFriendimpresss = userFriendimpressService
					.selectAllUserFriendimpress(userFriendimpress);
			StringBuilder result = new StringBuilder();
			boolean flag = false;
			for (Map<String, Object> m : userFriendimpresss) {
				if (flag) {
					result.append(",");
				} else {
					flag = true;
				}
				result.append((String) m.get("id"));
			}
			// ?????????????????????
			String[] arr = { result.toString() };
			// ????????????????????????????????????????????????
			userFriendimpressService.deleteUserFriendimpress(arr);
			if (!StringUtils.isEmpty(impress)) {
				String[] strArray = null;
				strArray = impress.split(",");
				if ("1".equals(type) || "2".equals(type)) {
					for (String m : strArray) {
						UserFriendimpress uf = new UserFriendimpress();
						uf.setUserId(loginId);
						uf.setImpressId(m);
						uf.setType(Integer.parseInt(type));
						uf.setCreateUserId(loginId);
						userFriendimpressService.addUserFriendimpress(uf);
					}
				}
			}
			return super.getAjaxResult(rr, ResponseCode.OK, "??????", null);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "?????????", null);
		}
	}

	/**
	 * ??????????????????
	 */
	@RequestMapping("getReconInfo")
	public @ResponseBody ResponseReport getReconInfo(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		try {
			TjyUser tjyUser = tjyUserService.selectByPrimaryKey(userId);
			Map map = new HashMap();
			// ????????????
			// List zwList = listValuesService.selectListByType(12);
			// map.put("zwList", zwList);
			// ????????????
			// List hyList = listValuesService.selectListByType(8001);
			// map.put("hyList",hyList );
			// ??????????????????
			if (!StringUtil.isEmpty(tjyUser.getIndustry())) {
				ListValues industry = listValuesService.selectByPrimaryKey(tjyUser.getIndustry());
				map.put("industryName", industry.getListValue());
			}
			// ??????????????????
			if (!StringUtil.isEmpty(tjyUser.getJob())) {
				ListValues job = listValuesService.selectByPrimaryKey(tjyUser.getJob());
				map.put("jobName", job.getListValue());
			}
			// ???????????????
			if (StringUtils.hasLength(tjyUser.getProvince())) {
				SyDistrict province = districtService.selectByPrimaryKey(tjyUser.getProvince());
				if (province != null) {
					map.put("provinceName", province.getDisName());
				}
			}
			// ???????????????
			if (StringUtils.hasLength(tjyUser.getCity())) {
				SyDistrict city = districtService.selectByPrimaryKey(tjyUser.getCity());
				if (city != null) {
					map.put("cityName", city.getDisName());
				}
			}
			// ???????????????
			if (StringUtils.hasLength(tjyUser.getCounty())) {
				SyDistrict county = districtService.selectByPrimaryKey(tjyUser.getCounty());
				if (county != null) {
					map.put("countyName", county.getDisName());
				}
			}
			// ??????????????????
			List<Map<String, Object>> imgList = reconPhotosService.selectByUserId(tjyUser.getId());
			map.put("imgList", imgList);
			map.put("tjyUser", tjyUser);
			return super.getAjaxResult(rr, ResponseCode.OK, "????????????", map);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ??????????????????
	 * @param rr
	 * @param session
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("getIndustry")
	public @ResponseBody ResponseReport getIndustry(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		try {
			Map map = new HashMap();
			// ????????????
			List hyList = listValuesService.selectListByType(8001);
			map.put("industryList", hyList);
			return super.getAjaxResult(rr, ResponseCode.OK, "????????????", map);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ??????????????????
	 * @param rr
	 * @param session
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("getJob")
	public @ResponseBody ResponseReport getJob(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		try {
			Map map = new HashMap();
			// ????????????
			List zwList = listValuesService.selectListByType(12);
			map.put("jobList", zwList);
			return super.getAjaxResult(rr, ResponseCode.OK, "????????????", map);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ??????????????????
	 * 
	 * trueName:trueName,
		comName:comName,
		job:zw,
		reconCapital:reconCapital,
		industry:hy,
		province:province,
		city:city,
		county:area,
		address:address,
		comProfile:comProfile,
		zjImgerJson:zjImgerJson
	 */
	@RequestMapping("reconSave")
	public @ResponseBody ResponseReport reconSave(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		String trueName = rr.getDataValue("trueName");
		String comName = rr.getDataValue("comName");
		String job = rr.getDataValue("jobId");
		String reconCapital = rr.getDataValue("reconCapital");
		String industry = rr.getDataValue("industryId");
		String province = rr.getDataValue("provinceId");
		String city = rr.getDataValue("cityId");
		String county = rr.getDataValue("countyId");
		String address = rr.getDataValue("address");
		String comProfile = rr.getDataValue("comProfile");
		String zjImgerJson = rr.getDataValue("zjImgerJson");
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		try {
			TjyUser oldTjyUser = tjyUserService.selectByPrimaryKey(userId);
			oldTjyUser.setAddress(address);
			oldTjyUser.setCity(city);
			oldTjyUser.setComName(comName);
			oldTjyUser.setComProfile(comProfile);
			oldTjyUser.setCounty(county);
			oldTjyUser.setIndustry(industry);
			oldTjyUser.setIsRealname(0);// ????????????
			oldTjyUser.setReconCapital(Double.valueOf(reconCapital));// ????????????
			oldTjyUser.setJob(job);
			oldTjyUser.setProvince(province);
			oldTjyUser.setTrueName(trueName);
			oldTjyUser.setReconStatus(1);// ??????????????????
			oldTjyUser.setReconMobile(oldTjyUser.getMobile());// ?????????????????????????????????????????????
			oldTjyUser.setTjReconDate(new Date());
			if (!StringUtils.isEmpty(comName) || !StringUtils.isEmpty(comProfile) || !StringUtils.isEmpty(industry)
					|| !StringUtils.isEmpty(job)) {
				// ???????????????????????????????????????????????????????????????
				userIntegralLogService.addLntAndEmp(oldTjyUser.getId(), "task_0020");
			}
			boolean bo = tjyUserService.updateTjyUser(oldTjyUser);
			if (bo) {
				// ???????????????????????????
				reconPhotosService.deleteByUserId(oldTjyUser.getId());
				System.out.println("zjImgerJson" + zjImgerJson);
				if (StringUtils.isEmpty(zjImgerJson)) {
					return super.getAjaxResult(rr, ResponseCode.Error, "???????????????????????????", null);
				}
				String[] images = zjImgerJson.split(",");
				for (int i = 0; i < images.length; i++) {
					String url = images[i];
					ReconPhotos reconPhotos = new ReconPhotos();
					reconPhotos.setUserId(oldTjyUser.getId());
					reconPhotos.setImgUrl(url);
					reconPhotos.setType(1);
					reconPhotosService.addReconPhotos(reconPhotos);
				}
				// ?????????????????????
				WxUser wxUser = wxUserService.selectByPrimaryKey(oldTjyUser.getId());
				MessageInfo messageInfo = new MessageInfo();
				messageInfo.setId(UUIDGenerator.getUUID());
				messageInfo.setDeleted(0);
				messageInfo.setType(2);// ??????
				messageInfo.setToUserId(wxUser.getWxUserId());
				messageInfo.setCreateTime(new Date());
				// ????????????
				String content = "?????????????????????????????????????????????";
				String con = WxMsmUtil.getTextMessageContent(content);
				messageInfo.setContent(con);
				messageInfo.setTemplateId("RECON_USER");
				messageInfo.setStatus(0);// ?????????
				messageInfo.setWxMsgType(1);/// ** ?????????????????????1???????????????2?????????????????? */
				messageInfoService.addMessageInfo(messageInfo);
				// ??????????????????
				/*
				 * // ????????????1??????????????? userIntegralLogService.addLntAndEmp(wxUser.getWxUserId(),
				 * "task_0018");
				 * 
				 * //??????????????????????????????????????????????????? String openId=oldTjyUser.getOpenId();
				 * if(!StringUtils.isEmpty(openId)){ List<Map<String, Object>> irList =
				 * inviteRecordService.selectInviteRecordPageByOpenId(openId); if(null != irList
				 * && irList.size()>0){ for(Map<String, Object> m:irList){ String userId =
				 * (String)m.get("userId"); //?????????????????????+??????:task_0023
				 * if(!StringUtils.isEmpty(userId)){
				 * userIntegralLogService.addLntAndEmp(wxUser.getWxUserId(), "task_0023"); } } }
				 * }
				 */
				return super.getAjaxResult(rr, ResponseCode.OK, "??????", null);
			} else {
				return super.getAjaxResult(rr, ResponseCode.Error, "???????????????????????????", null);
			}
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "?????????", null);

		}
	}

	/**
	 * ??????????????????
	 * @param rr
	 * @param session
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("personalFavourite")
	public @ResponseBody ResponseReport personalFavourite(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		try {
			Map map = new HashMap();
			// ??????
			List<Map<String, Object>> favList = listValuesService.selectListByType(8002);
			UserFav userFav = new UserFav();
			userFav.setUserId(userId);
			List<Map<String, Object>> userFavs = userFavService.selectAllUserFav(userFav);
			StringBuilder result = new StringBuilder();
			boolean flag = false;
			for (Map<String, Object> m : userFavs) {
				if (flag) {
					result.append(",");
				} else {
					flag = true;
				}
				result.append((String) m.get("favId"));
			}
			for (Map<String, Object> m : favList) {
				if (result.toString().indexOf((String) m.get("id")) != -1) {
					m.put("is_content", 1);
				} else {
					m.put("is_content", 0);
				}
			}
			map.put("favList", favList);

			return super.getAjaxResult(rr, ResponseCode.OK, "????????????", map);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ????????????????????????
	 */
	@RequestMapping("leaveMsgSave")
	public @ResponseBody ResponseReport leaveMsgSave(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		String content = rr.getDataValue("content");
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		try {
			LeaveMsg leaveMsg = new LeaveMsg();
			leaveMsg.setCreateTime(new Date());
			leaveMsg.setContent(content);
			leaveMsg.setDeleted(0);
			leaveMsg.setUserId(userId);
			leaveMsg.setSource("1");
			leaveMsg.setType("2");
			leaveMsgService.addLeaveMsg(leaveMsg);
			return super.getAjaxResult(rr, ResponseCode.OK, "??????", null);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "?????????", null);
		}
	}

	/**
	 * ???????????????????????????   disturbFlag 0:??????   disturbFlag:1??????    
	 */
	@RequestMapping("myDisturbSet")
	public @ResponseBody ResponseReport myDisturbSet(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		String isdisturb = rr.getDataValue("disturbFlag");
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		try {
			TjyUser tjyuser = tjyUserService.selectByPrimaryKey(userId);
			tjyuser.setIsdisturb(Integer.parseInt(isdisturb));
			Boolean resultStr = tjyUserService.updateTjyUser(tjyuser);
			String str = "???????????????";
			if ("0".equals(isdisturb)) {
				str = "????????????????????????";
			}
			if ("1".equals(isdisturb)) {
				str = "????????????????????????";
			}
			return super.getAjaxResult(rr, ResponseCode.OK, str, null);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ??????
	 */
	@RequestMapping("walletInfo")
	public @ResponseBody ResponseReport walletInfo(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		try {
			Map map = new HashMap();
			WxUser user = wxUserService.selectByPrimaryKey(userId);
			map.put("user", user);
			List<HzbManagerLog> logList = hzbManagerLogService.selectByUserId(userId);
			double ljcz = 0, ljxf = 0;
			if (null != logList && !logList.isEmpty()) {
				for (HzbManagerLog hzbManagerLog : logList) {
					double money = null == hzbManagerLog.getManagerMoney() ? 0 : hzbManagerLog.getManagerMoney();
					if (hzbManagerLog.getType() == 4 || hzbManagerLog.getType() == 7) {
						ljcz += money;
					} else if (hzbManagerLog.getType() == 8) {
						ljxf += money;
					}
				}
			}
			// ?????????????????????
			List<Map<String, Object>> list = new ArrayList();
			list = couponLogService.selMyCouponList(userId, 1, null, null);
			map.put("ccount", list.size());
			map.put("ljcz", ljcz);
			map.put("ljxf", ljxf);
			return super.getAjaxResult(rr, ResponseCode.OK, "??????", map);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "?????????", null);
		}
	}

	/**
	 * ????????????   ??????  1       J??? 2
	 */
	@RequestMapping("walletLog")
	public @ResponseBody ResponseReport walletLog(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		String pageNum = rr.getDataValue("pageNum");
		String pageSize = rr.getDataValue("pageSize");
		String type = rr.getDataValue("type");
		if (StringUtil.isEmpty(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		if (StringUtil.isEmpty(type)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "??????type??????", null);
		}
		if (StringUtil.isEmpty(pageNum)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "??????pageNum??????", null);
		}
		if (StringUtil.isEmpty(pageSize)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "??????pageSize??????", null);
		}
		try {
			List<Map<String, Object>> logList = walletLogService.selectWalletLogPageByUserIdAndType(userId, type,
					Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			return super.getAjaxResult(rr, ResponseCode.OK, "????????????", logList);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ???????????????   selMyCouponList   1  2  3
	 */
	@RequestMapping("myCouponList")
	public @ResponseBody ResponseReport myCouponList(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		String pageNum = rr.getDataValue("pageNum");
		String pageSize = rr.getDataValue("pageSize");
		String type = rr.getDataValue("type");
		if (StringUtil.isEmpty(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		if (StringUtil.isEmpty(type)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "??????type??????", null);
		}
		if (StringUtil.isEmpty(pageNum)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "??????pageNum??????", null);
		}
		if (StringUtil.isEmpty(pageSize)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "??????pageSize??????", null);
		}
		try {
			List<Map<String, Object>> list = new ArrayList();
			list = couponLogService.selMyCouponList(userId, Integer.valueOf(type), Integer.valueOf(pageNum),
					Integer.valueOf(pageSize));
			return super.getAjaxResult(rr, ResponseCode.OK, "????????????", list);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ????????????  List<Map<String, Object>> logList = userIntegralLogService.selectIntegralLogPageByUserId(userId, pageNum,
				pageSize);
	 */
	@RequestMapping("integralLog")
	public @ResponseBody ResponseReport integralLog(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		String pageNum = rr.getDataValue("pageNum");
		String pageSize = rr.getDataValue("pageSize");
		if (StringUtil.isEmpty(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		if (StringUtil.isEmpty(pageNum)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "??????pageNum??????", null);
		}
		if (StringUtil.isEmpty(pageSize)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "??????pageSize??????", null);
		}
		try {
			List<Map<String, Object>> logList = userIntegralLogService.selectIntegralLogPageByUserId(userId,
					Integer.valueOf(pageNum), Integer.valueOf(pageSize));
			return super.getAjaxResult(rr, ResponseCode.OK, "????????????", logList);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ??????????????????   1?????????     2?????????
	 */
	@RequestMapping("integralRule")
	public @ResponseBody ResponseReport integralRule(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		String type = rr.getDataValue("type");
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		if (type == null || "".equals(type)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
		}
		try {
			UserIntegralEmpirical userIntegralEmpirical = new UserIntegralEmpirical();
			if (!"1".equals(type) || !"2".equals(type)) {
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
			}
			userIntegralEmpirical = userIntegralEmpiricalService.selectByIeType(type);
			Map map = new HashMap();
			map.put("rule", userIntegralEmpirical.getRuleExplain());
			return super.getAjaxResult(rr, ResponseCode.OK, "????????????", map);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ??????????????????
	 */
	@RequestMapping("helpState")
	public @ResponseBody ResponseReport helpState(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		String userId = rr.getUserProperty().getUserId();
		if (userId == null || "".equals(userId)) {
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		try {
			Map map = new HashMap();
			List<Map<String, Object>> listValues = listValuesService.selectListByType(666);
			map.put("state", listValues.get(0).get("listDesc"));
			return super.getAjaxResult(rr, ResponseCode.OK, "????????????", map);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 *??????????????????????????????
	 */
	@RequestMapping("getSecretary")
	public @ResponseBody ResponseReport getSecretary(@RequestBody RequestReport rr, HttpSession session,
			HttpServletResponse response) throws IOException {
		try {
			String userId = rr.getUserProperty().getUserId();
			if (userId == null || "".equals(userId)) {
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
			}
			TjyUser tjyuser = tjyUserService.selectByPrimaryKey(userId);
			if (null == tjyuser) {
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "??????????????????", null);
			}
			Map map = new HashMap();
			map.put("reconStatus", tjyuser.getReconStatus());
			if (!"2".equals(tjyuser.getReconStatus() + "")) {
				return super.getAjaxResult(rr, ResponseCode.OK, "?????????????????????", map);
			}
			map.put("secretaryPhone", tjyuser.getKfTelephone());
			WxUser secretaryUser = wxUserService.selectByMobile(tjyuser.getKfTelephone());
			if (secretaryUser != null) {
				TjyUser secretarytjyuser = tjyUserService.selectByPrimaryKey(secretaryUser.getId() + "");
				map.put("headImg", secretaryUser.getImgUrl());

				map.put("name", secretarytjyuser.getNickname());
			}
			return super.getAjaxResult(rr, ResponseCode.OK, "????????????", map);
		} catch (Exception e) {
			return super.getAjaxResult(rr, ResponseCode.Error, "???????????????", null);
		}
	}

	/**
	 * ???????????????
	 * 
	 * @author ??????
	 * @return
	 */
	@RequestMapping("my_honor_info")
	public @ResponseBody ResponseReport my_honor_info(@RequestBody RequestReport rr)
	{
		String userId = rr.getUserProperty().getUserId();
		if (StringUtil.isEmpty(userId))
		{
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		String id = rr.getDataValue("id");
		if (StringUtil.isEmpty(id))
		{
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
		}
		Honor honor = honorService.selectByPrimaryKey(id);
		String remark = honor.getRemark();
		Map map = new HashMap();
		map.put("remark", remark);
		return super.getAjaxResult(rr, ResponseCode.OK, "????????????", map);
	}
	/**
	 * 
	 * ???????????????
	 */
	@RequestMapping("generateThreeCode")
	public @ResponseBody ResponseReport generateThreeCode(@RequestBody RequestReport rr)
	{
		String userId = rr.getUserProperty().getUserId();
		if (StringUtil.isEmpty(userId))
		{
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		String threeCodeUrl = "http://3weima.tojoycloud.org/";
		List list = tjyUserService.getUserListById(userId);
		if (list.size() > 0)
		{
			Map map = (HashMap) list.get(0);
			String name = map.get("true_name") == null ? "" : map.get("true_name").toString();
			String phone = map.get("mobile") == null ? "" : map.get("mobile").toString();
			String company = map.get("com_name") == null ? "" : map.get("com_name").toString();
			String position = map.get("job_name") == null ? "" : map.get("job_name").toString();
			String user_id = userId;
			String user_status = map.get("recon_status") == null ? "" : map.get("recon_status").toString();// 0?????????1?????????2???????????????
			if(!"2".equals(user_status))
				user_status = "1";
			String time = new Date().getTime() + "";
			time = time.substring(0, 10);
			String str = phone + user_status + user_id + time + "X4F4S1S34FDFSFSF";
			// token
			String token = MD5Util.to_MD5(str);
			String url = threeCodeUrl + "?name=" + URLEncoder.encode(name) + "&phone=" + URLEncoder.encode(phone) + "&company=" + URLEncoder.encode(company) + "&position=" + URLEncoder.encode(position) + "&user_id=" + user_id + "&user_status=" + user_status + "&time=" + time + "&token=" + URLEncoder.encode(token);
			try
			{
				DefaultHttpClient client = new DefaultHttpClient();
				// ??????get??????
				HttpGet threeCodeRequest = new HttpGet(url);
				HttpResponse threeCodeResponse = client.execute(threeCodeRequest);
				if (threeCodeResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String returnRul = "http://3weima.tojoycloud.org/?name="+name+"&phone="+phone+"&company="+company+"&position="+position+"&user_id="+user_id+"&user_status="+user_status+"&time="+time+"&token="+token;
					Map relMap = new HashMap();
					relMap.put("url", returnRul);
					return super.getAjaxResult(rr, ResponseCode.OK, "??????", relMap);
					
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "??????", null);
			}
			// String result = "";
			// BufferedReader in = null;
			// try {
			// URL serverUrl = new URL(url.toString());
			// HttpURLConnection conn = (HttpURLConnection) serverUrl
			// .openConnection();
			// conn.setRequestMethod("GET");
			//
			// conn.setInstanceFollowRedirects(false);
			// conn.addRequestProperty("Accept-Charset", "UTF-8;");
			// conn.setRequestProperty("connection", "Keep-Alive");
			// conn.addRequestProperty("User-Agent",
			// "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8)
			// Firefox/3.6.8");
			// conn.connect();
			//
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
		}
		return super.getAjaxResult(rr, ResponseCode.NotSupport, "??????", null);
	}

	/**
	 * 
	 * ???????????????????????????
	 */
	@RequestMapping("getThreeCodeList")
	public @ResponseBody ResponseReport getThreeCodeList(@RequestBody RequestReport rr)
	{
		String userId = rr.getUserProperty().getUserId();
		if (StringUtil.isEmpty(userId))
		{
			return super.getAjaxResult(rr, ResponseCode.NotSupport, "?????????", null);
		}
		String threeCodeUrl = "http://3weima.tojoycloud.org/apis";
		List list = tjyUserService.getUserListById(userId);
		if (list.size() > 0)
		{
			Map map = (HashMap) list.get(0);
			String name = map.get("true_name") == null ? "" : map.get("true_name").toString();
			String phone = map.get("mobile") == null ? "" : map.get("mobile").toString();
			String company = map.get("com_name") == null ? "" : map.get("com_name").toString();
			String position = map.get("job_name") == null ? "" : map.get("job_name").toString();
			String user_id = userId;
			String user_status = map.get("status") == null ? "" : map.get("status").toString();// 0?????????1?????????2???????????????
			String time = new Date().getTime() + "";
			time = time.substring(0, 10);
			String str = user_id + time + "X4F4S1S34FDFSFSF";
			// token
			String token = MD5Util.to_MD5(str);
			String url = threeCodeUrl + "?user_id=" + user_id + "&time=" + time + "&token=" + token;
			// get??????????????????
			JSONObject jsonResult = null;
			try
			{
				DefaultHttpClient client = new DefaultHttpClient();
				// ??????get??????
				HttpGet threeCodeRequest = new HttpGet(url);
				HttpResponse threeCodeResponse = client.execute(threeCodeRequest);
				/** ???????????????????????????????????? **/
				if (threeCodeResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
				{
					/** ??????????????????????????????json??????????????? **/
					String strResult = EntityUtils.toString(threeCodeResponse.getEntity());

					JSONArray arry = JSONArray.fromObject(strResult);
					List<Map<String, String>> rsList = new ArrayList<Map<String, String>>();
					for (int i = 0; i < arry.size(); i++)
					{
						JSONObject jsonObject = arry.getJSONObject(i);
						Map<String, String> threeCodeMap = new HashMap<String, String>();
						for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();)
						{
							String key = (String) iter.next();
							String value = jsonObject.get(key).toString();
							threeCodeMap.put(key, value);
						}
						rsList.add(threeCodeMap);
					}
					return super.getAjaxResult(rr, ResponseCode.OK, "????????????", rsList);
				}
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
			}
		}
		return super.getAjaxResult(rr, ResponseCode.NotSupport, "????????????", null);
	}
	/**
	 * ??????????????????
	 * ?????????????????????
	 */
	@RequestMapping("getTDCode")
	@ResponseBody
	public String getTDCode(HttpServletRequest request,HttpServletResponse response) {
		try
		{
			StringBuffer sb = new StringBuffer();
			InputStream is = request.getInputStream();
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(is,"utf-8"));
			String s = "";
			while((s=br.readLine())!=null){
			    sb.append(s);
			}
			if(sb.toString().length()>0){
				Gson gson = new Gson();
				Map<String, Object> map = new HashMap<String, Object>();
				map = gson.fromJson(sb.toString(), map.getClass());
				String threeCodeId = map.get("id")==null?"":map.get("id").toString();
				String user_id = map.get("user_id")==null?"":map.get("user_id").toString();
				String tdcreate_time = map.get("create_time")==null?"":map.get("create_time").toString();
				String picPath = map.get("picPath")==null?"":map.get("picPath").toString();
				 //0:??????1:??????
				String status = map.get("status")==null?"":map.get("status").toString();
				ThreeDimensionalCode tdc = new ThreeDimensionalCode();
				tdc.setTd_Id(Long.parseLong(threeCodeId));
				tdc.setUser_Id(Long.parseLong(user_id));
				if(!StringUtil.isEmpty(tdcreate_time)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					tdc.setTdcreate_Time(sdf.parse(tdcreate_time));
				}
				tdc.setPicPath(picPath);
				if("1".equals(status)) {
					//??????
					tdc = threeDimensionalCodeService.insertTDCode(tdc);
					if(tdc.getId()>0)
						return "????????????";
					return "????????????";
				}else if("0".equals(status)) {
					//??????
					int upCount = threeDimensionalCodeService.updateTDCode(tdc);
					if(upCount>0)
						return "????????????";
					return "????????????";
				}
				return "????????????";
	        }else {
	            return "???????????????";
	        }
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			return "????????????";
		}catch (ParseException e)
		{
			e.printStackTrace();
			return "?????????????????????";
		}
	}
	
	/**
	 * @author ??????
	 * ?????????????????????????????????????????????
	 * @param JSON
	 * 
	 * */
	@RequestMapping("auditNotice")
	@ResponseBody
	public String auditNotice(HttpServletRequest request,HttpServletResponse response) {
		try
		{
			StringBuffer sb = new StringBuffer();
			InputStream is = request.getInputStream();
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(is,"utf-8"));
			String s = "";
			while((s=br.readLine())!=null){
			    sb.append(s);
			}
			if(sb.toString().length()>0){
				Gson gson = new Gson();
				Map<String, Object> map = new HashMap<String, Object>();
				map = gson.fromJson(sb.toString(), map.getClass());
				String threeCodeId = map.get("id")==null?"":map.get("id").toString();//?????????id
				String oddNum = map.get("odd_num")==null?"":map.get("odd_num").toString();//???????????????
				String user_id = map.get("user_id")==null?"":map.get("user_id").toString();//??????id
				String mobile = map.get("mobile")==null?"":map.get("mobile").toString();//???????????????
				String status = map.get("status")==null?"":map.get("status").toString();//????????????0????????????1??????
				if("1".equals(status))
					status = "??????";
				else
					status = "?????????";
				//?????????????????????????????????
				TjyUser noticeUser = tjyUserService.selectByPrimaryKey(user_id);
				String name = noticeUser.getTrueName();
				String str[] = content.split("XX");
				//????????????????????????
				String content = str[0] + name + str[1] + oddNum + str[2]+status;
				MessageInfo mi = new MessageInfo();
				mi.setId(UUIDGenerator.getUUID());
				mi.setContent(content);
				mi.setType(5);
				mi.setTemplateId("");
				mi.setStatus(0);
				mi.setCreateTime(new Date());
				mi.setToUserId(user_id);
				//??????????????????
				String mess = messageInfoService.addMessageInfo(mi);
				
				//????????????
				String aldyMess = "";
				if(AldyMessageUtil.directSend(content,mobile)){
					MessageInfo messageInfo = new MessageInfo();
					messageInfo.setId(UUIDGenerator.getUUID());
					messageInfo.setDeleted(0);
					messageInfo.setMobile(mobile);
					messageInfo.setType(1);// ??????
					messageInfo.setCreateTime(new Date());
					messageInfo.setSendTime(new Date());
					messageInfo.setContent(content);
					messageInfo.setStatus(1);// ?????????
					messageInfo.setTemplateId(AldyMessageUtil.MsmTemplateId.REG);
					aldyMess = messageInfoService.addMessageInfo(messageInfo);
				}else{
					return "????????????";
				}
				return "????????????";
					
	        }else {
	            return "???????????????";
	        }
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			return "????????????";
		}
	}
}
