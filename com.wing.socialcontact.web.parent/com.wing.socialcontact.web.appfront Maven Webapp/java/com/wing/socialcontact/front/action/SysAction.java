package com.wing.socialcontact.front.action;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.wing.socialcontact.common.action.BaseAction;
import com.wing.socialcontact.common.model.IpInfo;
import com.wing.socialcontact.common.model.Member;
import com.wing.socialcontact.config.MsgConfig;
import com.wing.socialcontact.front.util.ApplicationPath;
import com.wing.socialcontact.service.wx.api.IIndexAdService;
import com.wing.socialcontact.service.wx.api.IInviteRecordService;
import com.wing.socialcontact.service.wx.api.IMessageInfoService;
import com.wing.socialcontact.service.wx.api.IRefundInstructionService;
import com.wing.socialcontact.service.wx.api.ISysBlacklistService;
import com.wing.socialcontact.service.wx.api.ISysconfigService;
import com.wing.socialcontact.service.wx.api.ITjyUserService;
import com.wing.socialcontact.service.wx.api.IUserIntegralLogService;
import com.wing.socialcontact.service.wx.api.IWxUserService;
import com.wing.socialcontact.service.wx.bean.IndexAd;
import com.wing.socialcontact.service.wx.bean.InviteRecord;
import com.wing.socialcontact.service.wx.bean.MessageInfo;
import com.wing.socialcontact.service.wx.bean.RefundInstruction;
import com.wing.socialcontact.service.wx.bean.TjyUser;
import com.wing.socialcontact.service.wx.bean.WxUser;
import com.wing.socialcontact.sys.service.IListValuesService;
import com.wing.socialcontact.util.AldyMessageUtil;
import com.wing.socialcontact.util.ConfigUtil;
import com.wing.socialcontact.util.Constants;
import com.wing.socialcontact.util.DateUtil;
import com.wing.socialcontact.util.EmojiFilterUtils;
import com.wing.socialcontact.util.HttpClientUtil;
import com.wing.socialcontact.util.IpUtil;
import com.wing.socialcontact.util.JsonUtil;
import com.wing.socialcontact.util.MD5Util;
import com.wing.socialcontact.util.MsmValidateBean;
import com.wing.socialcontact.util.RedisCache;
import com.wing.socialcontact.util.ServletUtil;
import com.wing.socialcontact.util.Sign;
import com.wing.socialcontact.util.UUIDGenerator;
import com.wing.socialcontact.util.WeixinUtil;
import com.wing.socialcontact.util.im.IMUtil;
import com.wing.socialcontact.util.pojo.AccessToken;
import com.wing.socialcontact.util.wxres.AesException;
import com.wing.socialcontact.util.wxres.WXBizMsgCrypt;

/**
 * ??????????????????????????????
 * 
 * @ClassName: SysAction
 * @Description: TODO
 * @author: zengmin
 * @date:2017???4???2??? ??????5:57:56
 */
@Controller
@RequestMapping("/m/sys")
public class SysAction extends BaseAction {

	@Autowired
	private IWxUserService wxUserService;

	@Autowired
	private IMessageInfoService messageInfoService;

	@Autowired
	private ITjyUserService tjyUserService;

	@Autowired
	private ISysconfigService sysconfigService;
	@Resource
	protected RedisCache redisCache;
	@Resource
	protected IUserIntegralLogService userIntegralLogService;

	@Autowired
	private IIndexAdService indexAdService;
	@Autowired
	private IInviteRecordService inviteRecordService;
	@Autowired
	private ISysBlacklistService sysBlacklistService;

	@Autowired
	private IListValuesService listValuesService;
	@Autowired
	private IRefundInstructionService refundInstructionService;

	/**
	 * ??????url???????????????
	 * 
	 * @Title: doget
	 * @Description: TODO
	 * @param msg_signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @param request
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws AesException
	 * @return: String
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:13:45
	 */
	@RequestMapping(value = "check", method = RequestMethod.GET)
	public @ResponseBody String doget(String signature, String timestamp, String nonce, String echostr,
			HttpServletRequest req) throws NoSuchAlgorithmException {
		String token = ApplicationPath.getParameter("wx_token");
		if (checkSignature(signature, timestamp, nonce, token)) {
			System.out.println(echostr);
			return echostr;
		}
		return "";
	}

	private static final String RESPONSE_TXT = "<xml><ToUserName><![CDATA[%s]]></ToUserName><FromUserName><![CDATA[%s]]></FromUserName><CreateTime>%s</CreateTime><MsgType><![CDATA[%s]]></MsgType><Content><![CDATA[%s]]></Content></xml>";

	@RequestMapping(value = "check", method = RequestMethod.POST)
	public @ResponseBody String dopost(String msg_signature, String timestamp, String nonce, String echostr,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		System.out.println("------------------type check.do-------------------------");
		PrintWriter out = response.getWriter();

		StringBuffer buffer = new StringBuffer();
		InputStream inputStream = request.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}
		bufferedReader.close();
		inputStreamReader.close();
		// ????????????
		inputStream.close();
		inputStream = null;
		String inputstr = buffer.toString();
		System.out.println(inputstr);

		Document doc = null;
		SAXReader reader = new SAXReader();
		// InputStream in = request.getInputStream();
		try {
			doc = reader.read(new ByteArrayInputStream(inputstr.getBytes("UTF-8")));
			Element root = doc.getRootElement();
			String toUserName = root.element("ToUserName").getTextTrim();
			String fromUserName = root.element("FromUserName").getTextTrim();
			String type = root.element("MsgType").getTextTrim();
			String createTime = root.element("CreateTime").getTextTrim();
			String msgId = "";
			String content = "     ";
			System.out.println("------------------type " + type + "-fromUserName " + fromUserName + "-createTime "
					+ createTime + "-------------------------");
			if ("event".equals(type)) {
				String event = root.element("Event").getTextTrim();
				System.out.println("-------event:" + event + " ------");
				String eventKey = "";
				if (root.element("EventKey") != null) {
					eventKey = root.element("EventKey").getTextTrim();
				}
				if ("subscribe".equals(event) || "SCAN".equals(event)) {
					System.out
							.println("-------subscribe eventKey:" + eventKey + " fromUserName(openid):" + fromUserName);
					if (StringUtils.hasLength(fromUserName)) {
						WxUser wxUser = wxUserService.selectByOpenId(fromUserName);
						if (wxUser == null) {
							System.out.println("-------subscribe user is null-------");
							String idstr[] = eventKey.split("_");
							if (idstr.length > 1) {
								String yqUserId = idstr[1];
								if (StringUtils.hasLength(yqUserId)) {
									InviteRecord inviteRecord = new InviteRecord();
									inviteRecord.setByqOpenId(fromUserName);
									inviteRecord.setUserId(yqUserId);
									inviteRecord.setCreateTime(new Date());
									try {
										String wx_secret = ApplicationPath.getParameter("wx_secret");
										String wx_appid = ApplicationPath.getParameter("wx_appid");
										AccessToken a = WeixinUtil.getAccessToken(wx_appid, wx_secret);
										String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="
												+ a.getToken() + "&openid=" + fromUserName + "&lang=zh_CN";
										String rs = HttpClientUtil.sendGetRequest(url, null);
										System.out.println("???????????????????????????" + rs);
										Map<String, Object> rsmap = JsonUtil.parseJSON2Map(rs);
										if (null != rsmap) {
											String nickname = EmojiFilterUtils
													.filterEmoji((String) rsmap.get("nickname"));
											inviteRecord.setByqNickName(nickname);
										}
									} catch (Exception e) {
										e.printStackTrace();
										System.out.println("??????????????????????????????");
									}
									String bo = inviteRecordService.addInviteRecord(inviteRecord);
									if (bo.equals(MsgConfig.MSG_KEY_SUCCESS)) {
										// ???????????? ????????? ?????????
										/// userIntegralLogService.addLntAndEmp(yqUserId,
										// "task_0022");
										System.out.println(" insert into inviteRecord success...");
									}
								}
							}
						} else {
							System.out.println("user existed ...");
						}
					}
					// String retstr =
					// cn.xinleju.mall.wx.util.HttpClientUtil.sendPostXMLRequest(weimengurl,
					// inputstr);
					/*String retstr = "?????????????????????????????????\n" + "\n" + "???????????????:????????????????????????????????????\n" + "\n" + "???????????????:\n"
							+ "??????????????????????????????????????????????????????????????????\n" + "??????????????????????????????????????????????????????????????????\n" + "??????????????????????????????????????????????????????????????????\n \n"
							+ "?????????????????????????????????????????????????????????????????????????????????http://t.cn/R94bxGx \n \n"
							+ "???134???????????????????????????????????????????????????http://t.cn/R963Die";*/
					
					RefundInstruction dto = refundInstructionService.getrefundinstructionByid(4);
					String retstr = dto.getContent().replaceAll("\r\n", "\n");
					// System.out.println(retstr);
					out.printf(RESPONSE_TXT, fromUserName, toUserName, System.currentTimeMillis(), "text", retstr);
				} else if ("unsubscribe".equals(event)) {// ????????????

				}
			} else {
				// String retstr =
				// cn.xinleju.mall.wx.util.HttpClientUtil.sendPostXMLRequest(weimengurl,
				// inputstr);
				// String retstr = "Hi???????????????????????????";
				// System.out.println(retstr);
				// out.printf(RESPONSE_TXT, fromUserName, toUserName,
				// System.currentTimeMillis(), "text", retstr);
				// msgId = root.element("MsgId").getTextTrim();
				// if (root.element("Content") != null) {
				// content = root.element("Content").getTextTrim();
				// System.out.println("-------content:" + content);
				// }
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ??????url???????????????
	 * 
	 * @Title: check_qyh
	 * @Description: TODO
	 * @param msg_signature
	 * @param timestamp
	 * @param nonce
	 * @param echostr
	 * @param request
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws AesException
	 * @return: String
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:13:45
	 */
	@RequestMapping(value = "check_qyh", method = RequestMethod.GET)
	public @ResponseBody String check_qyh(String msg_signature, String timestamp, String nonce, String echostr,
			HttpServletRequest request) throws NoSuchAlgorithmException, AesException {
		System.out.println("msg_signature=" + msg_signature + " timestamp=" + timestamp + " nonce=" + nonce
				+ " echostr=" + echostr);
		String wx_token_qyh = ApplicationPath.getParameter("wx_token_qyh");
		String wx_aeskey_qyh = ApplicationPath.getParameter("wx_aeskey_qyh");
		String wx_corpid_qyh = ApplicationPath.getParameter("wx_corpid_qyh");
		WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(wx_token_qyh, wx_aeskey_qyh, wx_corpid_qyh);
		String sVerifyMsgSig = msg_signature;
		String sVerifyTimeStamp = timestamp;
		String sVerifyNonce = nonce;
		String sVerifyEchoStr = echostr;
		String sEchoStr; // ?????????????????????
		try {
			sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sVerifyEchoStr);
			return sEchoStr;
		} catch (Exception e) {
			// ??????URL????????????????????????????????????
			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping(value = "check_qyh", method = RequestMethod.POST)
	public @ResponseBody String check_qyh(String msg_signature, String timestamp, String nonce, String echostr,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		Document doc = null;
		String wx_token_qyh = ApplicationPath.getParameter("wx_token_qyh");
		String wx_aeskey_qyh = ApplicationPath.getParameter("wx_aeskey_qyh");
		String wx_corpid_qyh = ApplicationPath.getParameter("wx_corpid_qyh");
		WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(wx_token_qyh, wx_aeskey_qyh, wx_corpid_qyh);

		SAXReader reader = new SAXReader();
		InputStream in = request.getInputStream();
		String sReqData = inputStream2String(in);
		String sMsg = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, sReqData);
		try {
			doc = reader.read(new StringReader(sMsg));
			Element root = doc.getRootElement();
			String fromUserName = root.element("FromUserName").getTextTrim();
			String type = root.element("MsgType").getTextTrim();
			// String toUserName = root.element("ToUserName").getTextTrim();
			// String createTime = root.element("CreateTime").getTextTrim();
			// String msgId = "";
			// String content = "";
			if ("event".equals(type)) {
				String event = root.element("Event").getTextTrim();
				System.out.println(" FromUserName:" + fromUserName + " type:" + type + " event:" + event);
				if ("subscribe".equals(event)) {// ??????
					if (StringUtils.hasLength(fromUserName)) {
						WxUser wxUser = wxUserService.selectByWxUserId(fromUserName);
						if (null == wxUser) {
							wxUser = wxUserService.selectByPrimaryKey(fromUserName);
						}
						if (null != wxUser) {
							TjyUser tjyUser = tjyUserService.selectByPrimaryKey(wxUser.getId() + "");
							if (null != tjyUser) {
								tjyUser.setStatus(2);
								tjyUserService.updateTjyUser(tjyUser);
							}
						}
					}
				} else if ("unsubscribe".equals(event)) {// ????????????
					if (StringUtils.hasLength(fromUserName)) {
						WxUser wxUser = wxUserService.selectByWxUserId(fromUserName);
						if (null == wxUser) {
							wxUser = wxUserService.selectByPrimaryKey(fromUserName);
						}
						if (null != wxUser) {
							TjyUser tjyUser = tjyUserService.selectByPrimaryKey(wxUser.getId() + "");
							if (null != tjyUser) {
								tjyUser.setStatus(1);
								tjyUserService.updateTjyUser(tjyUser);
							}
						}
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;

	}

	private static boolean checkSignature(String signature, String timestamp, String nonce, String token) {
		if (StringUtils.isEmpty(token) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(nonce)) {
			return false;
		}
		String[] arr = new String[] { token, timestamp, nonce };
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;

		try {
			md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		content = null;
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}

	// ???????????????????????????????????????
	private static String byteToHexStr(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];

		String s = new String(ob);
		return s;
	}

	// ?????????????????????????????????????????????
	private static String byteToStr(byte[] bytearray) {
		String strDigest = "";
		for (int i = 0; i < bytearray.length; i++) {
			strDigest += byteToHexStr(bytearray[i]);
		}
		return strDigest;
	}

	public static String inputStream2String(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	/**
	 * ???????????????
	 * 
	 * @Title: mlogin
	 * @Description: TODO
	 * @param appno
	 * @param request
	 * @param code
	 * @param state
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return: String
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:13:55
	 */
	@RequestMapping(value = "mlogin", method = RequestMethod.GET)
	public String mlogin(HttpServletRequest request, String code, String state) throws UnsupportedEncodingException {

		if (ServletUtil.isLogin(request)) {
			// ????????????????????????????????????
			boolean isBlack = sysBlacklistService.selectSysBlacklistByUserId(ServletUtil.getMember(request).getId());
			if (isBlack) {
				request.getSession().removeAttribute("me");
				return "redirect:blackPage.do";
			}
			state = encodeURL(state);
			if (state.indexOf("loginPage.do") != -1) {
				return "redirect:index.do";
			}
			return "redirect:" + state;
		}

		String wx_secret = ApplicationPath.getParameter("wx_secret");
		String wx_appid = ApplicationPath.getParameter("wx_appid");

		System.out.println("++++corpid=" + wx_appid + " secret=" + wx_secret + " domain:" + request.getServerName());
		System.out.println("++++code=" + code + " state=" + state);
		HttpSession session = request.getSession(false);
		String woid = (String) session.getAttribute("wxopenid");
		String wxopenid = "";
		String access_token = "";
		String url = "";
		String rs = "";
		Map<String, Object> rsmap = null;
		/*if (StringUtils.isEmpty(woid)) {
			if (!StringUtils.isEmpty(code)) {
				url = "https://api.weixin.qq.com/sns/oauth2/access_token?" + "appid=" + wx_appid + "&secret="
						+ wx_secret + "&code=" + code + "&grant_type=authorization_code";
				System.out.println("url???" + url);
				rs = HttpClientUtil.sendGetRequest(url, null);
				rsmap = JsonUtil.parseJSON2Map(rs);
				wxopenid = (String) rsmap.get("openid");
				access_token = (String) rsmap.get("access_token");
			}
		} else {
			wxopenid = woid;
			access_token = (String) session.getAttribute("access_token");
		}*/
		if (!StringUtils.isEmpty(code)) {
			url = "https://api.weixin.qq.com/sns/oauth2/access_token?" + "appid=" + wx_appid + "&secret="
					+ wx_secret + "&code=" + code + "&grant_type=authorization_code";
			System.out.println("url???" + url);
			rs = HttpClientUtil.sendGetRequest(url, null);
			rsmap = JsonUtil.parseJSON2Map(rs);
			wxopenid = (String) rsmap.get("openid");
			access_token = (String) rsmap.get("access_token");
		}
		System.out.println("openid---???" + wxopenid);
		System.out.println("access_token---???" + access_token);
		state = encodeURL(state, request);
		System.out.println("-----" + state);
		if (StringUtils.isEmpty(wxopenid) || StringUtils.isEmpty(access_token)) {
			return "error";
		} else {
			session.setAttribute("wxopenid", wxopenid);
			session.setAttribute("access_token", access_token);
		}
		WxUser u = wxUserService.selectByOpenId(wxopenid);
		if (u != null) {
			// ????????????????????????????????????
			boolean isBlack = sysBlacklistService.selectSysBlacklistByUserId(u.getId() + "");
			if (isBlack) {
				request.getSession().removeAttribute("me");
				return "redirect:blackPage.do";
			}

			// ??????????????????????????????
			url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + wxopenid
					+ "&lang=zh_CN";
			rs = HttpClientUtil.sendGetRequest(url, null);
			System.out.println("rs???" + rs);
			rsmap = JsonUtil.parseJSON2Map(rs);
			if (null != rsmap) {
				String headimgurl = (String) rsmap.get("headimgurl");
				if (StringUtils.isEmpty(headimgurl) || "/0".equals(headimgurl)) {
					String contentPath = sysconfigService.getSysconfig().getWebSite();
					String path = request.getContextPath();
					String cp = contentPath + path;
					headimgurl = cp + "/resource/img/icons/weixinHeader.jpg";
				}
				String nickname = EmojiFilterUtils.filterEmoji((String) rsmap.get("nickname"));
				u.setImgUrl(headimgurl);
				if (StringUtils.isEmpty(u.getNickName())) {
					u.setNickName(nickname);
				}
				Long sex = (Long) rsmap.get("sex");
				u.setSex(Integer.valueOf(sex + ""));
				wxUserService.updateWxUser(u);
			}
			TjyUser tjyUser = tjyUserService.selectByPrimaryKey(u.getId() + "");
			if (null != tjyUser) {
				if (StringUtils.isEmpty(tjyUser.getNickname())) {
					tjyUser.setNickname(u.getNickName());
				}
				tjyUser.setHeadPortrait(u.getImgUrl());
				tjyUser.setOpenId(wxopenid);
				// tjyUser.setFirstBindTime(new Date());
				tjyUserService.updateTjyUser(tjyUser);
			} else {
				tjyUser = new TjyUser();
				tjyUser.setId(u.getId() + "");
				tjyUser.setNickname(u.getNickName());
				tjyUser.setStatus(1);
				tjyUser.setHeadPortrait(u.getImgUrl());
				tjyUser.setOpenId(wxopenid);
				tjyUser.setLastVisitDate(new Date());
				tjyUser.setFirstBindTime(new Date());
				tjyUserService.addTjyUser(tjyUser);
			}
			String loginIp = IpUtil.getIpAddr(request);
			IpInfo ipInfo = IpUtil.getIpInfo(loginIp);
			Member me = new Member();// ??????????????????session ???????????????
			me.setId(u.getId() + "");
			me.setIpInfo(ipInfo);
			me.setLoginTime(DateUtil.currentTimestamp());
			if (null != tjyUser) {
				me.setIsRealname(tjyUser.getIsRealname() + "");
				me.setOpenId(wxopenid);
				me.setKfTelephone(tjyUser.getKfTelephone());
			}
			int loginCount = null == u.getLogincount() ? 0 : u.getLogincount();
			u.setLogincount(loginCount + 1);
			u.setLastlogindate(new Date());
			wxUserService.updateWxUser(u);
			// ??????????????????????????????
			if (u.getLogincount() == 1) {
				// ???????????? ????????? ?????????
				userIntegralLogService.addLntAndEmp(tjyUser.getId(), "task_0001");

				// ???????????????????????????????????????????????????

				if (!StringUtils.isEmpty(wxopenid)) {
					List<Map<String, Object>> irList = inviteRecordService.selectInviteRecordPageByOpenId(wxopenid);
					if (null != irList && irList.size() > 0) {
						for (Map<String, Object> m : irList) {
							String userId = (String) m.get("userId");
							// ?????????????????????:task_0022
							if (!StringUtils.isEmpty(userId)) {
								userIntegralLogService.addLntAndEmp(userId, "task_0022");
							}
						}
					}
				}

				MessageInfo messageInfo = new MessageInfo();
				messageInfo.setId(UUIDGenerator.getUUID());
				messageInfo.setDeleted(0);
				messageInfo.setMobile(u.getMobile());
				messageInfo.setType(3);// ??????
				messageInfo.setCreateTime(new Date());
				messageInfo.setToUserId(tjyUser.getId());
				String name = tjyUser.getNickname();
				messageInfo.setContent(AldyMessageUtil.userZcSuccess(name));
				// messageInfo.setContent(
				// "???" + AldyMessageUtil.SMSPRE + "????????????" + name +
				// "??????????????????????????????????????????????????????????????????????????????????????????????????????");
				messageInfo.setStatus(0);
				messageInfoService.addMessageInfo(messageInfo);

				// ?????????????????????????????????
				try {
					String userName = (tjyUser.getTrueName() == "" || null == tjyUser.getTrueName())
							? tjyUser.getNickname() : tjyUser.getTrueName();
					System.out.println("+++++++++++++++++++++???????????????????????????" + userName);
					AldyMessageUtil.sendSms("SMS_80520021", u.getMobile(), "{name:\"" + userName + "\"}");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			request.getSession().setAttribute("me", me);
			request.getSession().setAttribute(Constants.SESSION_WXUSER_ID, me.getId() + "");
			request.getSession().setAttribute(Constants.SESSION_WXUSER_NICKNAME, u.getNickName());
			request.getSession().setAttribute(Constants.SESSION_WXUSER_HDPIC, u.getImgUrl());
			// SyLoginLog log = new SyLoginLog();
			// log.setUserId(u.getId());
			// log.setLoginType((short) 1);
			// log.setLoginDesc("????????????");
			// log.setIpInfoCountry(ipInfo.getCountry());
			// log.setIpInfoRegion(ipInfo.getRegion());
			// log.setIpInfoCity(ipInfo.getCity());
			// log.setIpInfoIsp(ipInfo.getIsp());
			// log.setLoginIp(loginIp);
			// log.setLoginTime(DateUtil.currentTimestamp());
			// logService.saveLog(log);// ??????????????????
			state = encodeURL(state);
			return "redirect:" + state;
		}
		return "redirect:guidePage.do";
	}

	/**
	 * ????????????
	 * 
	 * @Title: getSignature
	 * @Description: TODO
	 * @param url
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @return: Map
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:14:14
	 */
	@RequestMapping(value = "getSignature", method = RequestMethod.POST)
	public @ResponseBody Map getSignature(String url, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		/*
		 * Member me = ServletUtil.getMember(request); if (null == me) { return
		 * super.getAjaxResult("302", "??????????????????????????????", null); } String userId =
		 * me.getId(); if (StringUtils.isEmpty(userId)) { return
		 * super.getAjaxResult("302", "??????????????????????????????", null); }
		 */

		if (StringUtils.isEmpty(url)) {
			return super.getAjaxResult("999", "????????????", null);
		}
		String wx_secret = ApplicationPath.getParameter("wx_secret");
		String wx_appid = ApplicationPath.getParameter("wx_appid");
		if (StringUtils.isEmpty(wx_appid)) {
			return super.getAjaxResult("999", "??????corpid??????????????????????????????", null);
		}
		AccessToken a = WeixinUtil.getAccessToken(wx_appid, wx_secret);
		Map<String, String> map = null;
		if (null != a && !StringUtils.isEmpty(a.getJsapi_ticket())) {
			map = Sign.sign(a.getJsapi_ticket(), url);
		}
		map.put("appId", wx_appid);
		return super.getSuccessAjaxResult("????????????", map);
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @Title: getWxConfig
	 * @Description: TODO
	 * @param url
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @return: Map
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:14:22
	 */
	@RequestMapping(value = "getWxConfig", method = RequestMethod.POST)
	public @ResponseBody Map getWxConfig(String url, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String wx_corpid_qyh = ApplicationPath.getParameter("wx_corpid_qyh");
		String wx_token_qyh = ApplicationPath.getParameter("wx_token_qyh");
		String wx_aeskey_qyh = ApplicationPath.getParameter("wx_aeskey_qyh");
		String wx_secret_qyh = ApplicationPath.getParameter("wx_secret_qyh");
		String wx_appno_qyh = ApplicationPath.getParameter("wx_appno_qyh");
		String wx_appid = ApplicationPath.getParameter("wx_appid");
		String wx_token = ApplicationPath.getParameter("wx_token");
		String wx_aeskey = ApplicationPath.getParameter("wx_aeskey");
		String wx_secret = ApplicationPath.getParameter("wx_secret");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("wx_corpid_qyh", wx_corpid_qyh);
		map.put("wx_token_qyh", wx_token_qyh);
		map.put("wx_aeskey_qyh", wx_aeskey_qyh);
		map.put("wx_secret_qyh", wx_secret_qyh);
		map.put("wx_appno_qyh", wx_appno_qyh);
		map.put("wx_appid", wx_appid);
		map.put("wx_token", wx_token);
		map.put("wx_aeskey", wx_aeskey);
		map.put("wx_secret", wx_secret);
		return super.getSuccessAjaxResult("????????????", map);
	}

	/**
	 * ??????URL
	 * 
	 * @Title: encodeURL
	 * @Description: TODO
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return: String
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:14:36
	 */
	private String encodeURL(String url) throws UnsupportedEncodingException {
		StringBuffer result = null;
		if (!StringUtils.isEmpty(url)) {
			url = url.replaceAll("@@2@@", "&");
			result = new StringBuffer();
			for (int i = 0; i < url.length(); i++) {
				if ((url.charAt(i) + "").getBytes().length > 1) {
					result.append(URLEncoder.encode(url.charAt(i) + "", "UTF-8"));
				} else {
					result.append(url.charAt(i));
				}
			}
			return result.toString();
		} else {
			return null;
		}
	}

	/**
	 * ???????????????????????????
	 * 
	 * @Title: regPage
	 * @Description: TODO
	 * @return
	 * @return: String
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:15:01
	 */
	@RequestMapping("guidePage")
	public String guidePage(HttpServletRequest request, ModelMap modelMap) {
		String opid = request.getParameter("opid");
		if (StringUtils.hasLength(opid)) {
			request.getSession().setAttribute("wxopenid", opid);
			String last_url = request.getParameter("last_url");
			modelMap.addAttribute("last_url", last_url);
		}
		return "register/guide";

	}

	/**
	 * ?????????????????????
	 * 
	 * @Title: regPage
	 * @Description: TODO
	 * @return
	 * @return: String
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:15:01
	 */
	@RequestMapping("regPage")
	public String regPage(HttpServletRequest request, ModelMap modelMap) {
		String opid = request.getParameter("opid");
		if (StringUtils.hasLength(opid)) {
			request.getSession().setAttribute("wxopenid", opid);
			String last_url = request.getParameter("last_url");
			modelMap.addAttribute("last_url", last_url);
		}
		return "register/register";

	}

	/**
	 * ???????????????????????????
	 * 
	 * @Title: regPage
	 * @Description: TODO
	 * @return
	 * @return: String
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:15:01
	 */
	@RequestMapping("subscribePage")
	public String subscribePage() {
		return "subscribe";

	}

	/**
	 * ????????????????????????
	 * 
	 * @Title: guidePage
	 * @Description: TODO
	 * @param request
	 * @param modelMap
	 * @return
	 * @return: String
	 * @author: zengmin
	 * @date: 2017???8???8??? ??????11:24:22
	 */
	@RequestMapping("blackPage")
	public String blackPage(HttpServletRequest request, ModelMap modelMap) {
		List<Map<String, Object>> listValues = listValuesService.selectListByType(999);
		if (null != listValues && !listValues.isEmpty()) {
			modelMap.put("yqConfig", listValues.get(0));
		}
		return "black";
	}

	/**
	 * ????????????
	 * 
	 * @Title: regSave
	 * @Description: TODO
	 * @param mobile
	 * @param yz
	 * @param dyz
	 * @param pwd
	 * @param session
	 * @return
	 * @return: Map
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:15:18
	 */
	@RequestMapping("regSave")
	public @ResponseBody Map regSave(String mobile, String yz, String dyz, String pwd, String nickName,
			HttpSession session) {
		// String imgCode = (String) session.getAttribute("imgCode");
		// System.out.println("imgCode:" + imgCode + "\tymyz" + yz);
		// if (!yz.equals(imgCode)) {
		// return super.getAjaxResult("999", "???????????????", null);
		// }

		boolean bo = MsmValidateBean.validateCode(mobile, dyz, session);
		if (!bo) {
			return super.getAjaxResult("999", "?????????????????????????????????", null);
		}
		String wxopenid = (String) session.getAttribute("wxopenid");
		String access_token = (String) session.getAttribute("access_token");
		if (StringUtils.isEmpty(wxopenid) || StringUtils.isEmpty(access_token)) {
			return super.getAjaxResult("401", "????????????[wxopenid_or_access_token]", null);
		}

		// ??????????????????????????????
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + wxopenid
				+ "&lang=zh_CN";
		String rs = HttpClientUtil.sendGetRequest(url, null);
		System.out.println("rs???" + rs);
		Map<String, Object> rsmap = JsonUtil.parseJSON2Map(rs);
		String imgUrl = "";
		String nickname = "";
		String sex = "";
		if (null != rsmap) {
			imgUrl = (String) rsmap.get("headimgurl");
			nickname = EmojiFilterUtils.filterEmoji((String) rsmap.get("nickname"));
			sex = rsmap.get("sex") + "";
		}
		WxUser o_wxUser = wxUserService.selectByMobile(mobile);
		if (null != o_wxUser) {
			// ?????????????????????????????????
			System.out.println("userid-------" + o_wxUser.getId());
			String openId = o_wxUser.getQqOpenid();
			if (StringUtils.isEmpty(openId) || !wxopenid.equals(openId)) {
				o_wxUser.setQqOpenid(wxopenid);
				o_wxUser.setUsername(mobile);
				o_wxUser.setImgUrl(imgUrl);
				if (StringUtils.isEmpty(o_wxUser.getNickName())) {
					o_wxUser.setNickName(nickname);
				}
				if (StringUtils.hasLength(sex)) {
					o_wxUser.setSex(Integer.valueOf(sex));
				}
				wxUserService.updateWxUser(o_wxUser);
				TjyUser tjyUser = tjyUserService.selectByPrimaryKey(o_wxUser.getId() + "");
				if (null == tjyUser) {
					tjyUser = new TjyUser();
					tjyUser.setId(o_wxUser.getId() + "");
					tjyUser.setMallUser(o_wxUser.getId());
					tjyUser.setNickname(o_wxUser.getNickName());
					tjyUser.setIsRealname(0);
					tjyUser.setStatus(1);
					tjyUser.setIsdk(0);
					tjyUser.setMobile(o_wxUser.getMobile());
					tjyUser.setHeadPortrait(o_wxUser.getImgUrl());
					tjyUser.setOpenId(o_wxUser.getQqOpenid());
					tjyUser.setFirstBindTime(new Date());
					tjyUser.setReconStatus(0);
					tjyUserService.addTjyUser(tjyUser);
				} else {
					tjyUser.setNickname(o_wxUser.getNickName());
					tjyUser.setHeadPortrait(o_wxUser.getImgUrl());
					tjyUser.setOpenId(o_wxUser.getQqOpenid());
					tjyUser.setFirstBindTime(new Date());
					tjyUserService.updateTjyUser(tjyUser);
				}
				//??????IM
				IMUtil.sendUser(tjyUser.getId());
				IMUtil.updateUserOne(tjyUser.getId(), tjyUser.getNickname(), tjyUser.getHeadPortrait());
				session.removeAttribute("imgCode");
				System.out.println("update openId success ...");
				return super.getSuccessAjaxResult("????????????");
			} else {
				return super.getAjaxResult("999", "?????????????????????", null);
			}
		} else {
			WxUser wxUser = new WxUser();
			wxUser.setMobile(mobile);
			if (StringUtils.hasLength(pwd)) {
				wxUser.setPassword(MD5Util.md5Hex(pwd));
			}
			wxUser.setDeletestatus(false);
			wxUser.setAddtime(new Date());
			wxUser.setQqOpenid(wxopenid);
			wxUser.setUsername(mobile);
			wxUser.setUsertype((byte) 1);
			wxUser.setNickName(nickname);
			wxUser.setImgUrl(imgUrl);
			if (StringUtils.hasLength(sex)) {
				wxUser.setSex(Integer.valueOf(sex));
			}
			bo = wxUserService.addWxUser(wxUser);

			if (bo) {
				session.removeAttribute("imgCode");
				return super.getSuccessAjaxResult("????????????");
			} else {
				return super.getAjaxResult("999", "????????????", null);
			}
		}
	}

	/**
	 * PC?????????
	 * 
	 * @Title: login
	 * @Description: TODO
	 * @param userName
	 * @param pwd
	 * @param request
	 * @return
	 * @return: Map
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:15:28
	 */
	@RequestMapping("login")
	public @ResponseBody Map login(String userName, String pwd, HttpServletRequest request) {
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(pwd)) {
			return super.getAjaxResult("999", "????????????", null);
		}
		WxUser wxUser = wxUserService.selectByUserName(userName);
		if (null == wxUser) {
			wxUser = wxUserService.selectByMobile(userName);
			if (null == wxUser) {
				return super.getAjaxResult("999", "???????????????", null);
			}
		}
		// ????????????????????????????????????
		boolean isBlack = sysBlacklistService.selectSysBlacklistByUserId(wxUser.getId() + "");
		if (isBlack) {
			return super.getAjaxResult("700", "?????????", null);
		}
		if (!MD5Util.md5Hex(pwd).equals(wxUser.getPassword())) {
			return super.getAjaxResult("999", "?????????????????????", null);
		}
		String loginIp = IpUtil.getIpAddr(request);
		IpInfo ipInfo = IpUtil.getIpInfo(loginIp);

		Member me = new Member();// ??????????????????session ???????????????
		me.setId(wxUser.getId() + "");
		me.setIpInfo(ipInfo);
		me.setLoginTime(DateUtil.currentTimestamp());
		me.setUserName(wxUser.getUsername());
		me.setWxUserId(wxUser.getWxUserId());
		TjyUser tjyUser = tjyUserService.selectById(me.getId());
		if (null != tjyUser) {
			me.setIsRealname(tjyUser.getIsRealname() + "");
			me.setKfTelephone(tjyUser.getKfTelephone());
			// ???????????? ????????? ?????????
			userIntegralLogService.addLntAndEmp(tjyUser.getId(), "task_0001");

		} else {
			me.setIsRealname("0");
		}

		int loginCount = null == wxUser.getLogincount() ? 0 : wxUser.getLogincount();
		wxUser.setLogincount(loginCount);// ??????????????????
		wxUser.setLastlogindate(new Date());
		wxUserService.updateWxUser(wxUser);
		request.getSession().setAttribute("me", me);
		request.getSession().setAttribute(Constants.SESSION_WXUSER_ID, me.getId() + "");
		request.getSession().setAttribute(Constants.SESSION_WXUSER_NICKNAME, tjyUser.getNickname());
		request.getSession().setAttribute(Constants.SESSION_WXUSER_HDPIC, tjyUser.getHeadPortrait());
		return super.getSuccessAjaxResult("????????????");
	}

	/**
	 * ?????????????????????
	 * 
	 * @Title: loginPage
	 * @Description: TODO
	 * @return
	 * @return: String
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:15:42
	 */
	@RequestMapping("loginPage")
	public String loginPage() {
		return "login";
	}

	/**
	 * ???????????????
	 * 
	 * @Title: index
	 * @Description: TODO
	 * @return
	 * @return: String
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:15:51
	 */
	@RequestMapping("index")
	public String index(HttpServletRequest req, ModelMap modelMap) {
		if (!ServletUtil.isLogin(req)) {
			return "login";
		}
		// ??????????????????
		IndexAd indexAd = indexAdService.selectIndexAdByUserId(ServletUtil.getMember(req).getId());
		modelMap.addAttribute("indexAd", indexAd);
		return "index";
	}

	/**
	 * ???????????????
	 * 
	 * @Title: getImg
	 * @Description: TODO
	 * @param session
	 * @param response
	 * @return: void
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:16:07
	 */
	@RequestMapping("imgNum")
	public void getImg(HttpSession session, HttpServletResponse response) {

		ServletOutputStream out = null;
		try {
			DefaultKaptcha captchaProducer = (DefaultKaptcha) ServletUtil.getApplicationContext()
					.getBean("captchaProducer");

			response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
			response.addHeader("Cache-Control", "post-check=0, pre-check=0");
			response.setHeader("Pragma", "no-cache");
			response.setContentType("image/jpeg");
			String capText = captchaProducer.createText();
			session.setAttribute("imgCode", capText);
			BufferedImage bi = captchaProducer.createImage(capText);
			out = response.getOutputStream();
			ImageIO.write(bi, "jpg", out);
			out.flush();
		} catch (Exception e) {

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * ?????????????????????
	 * 
	 * @Title: send_code
	 * @Description: TODO
	 * @param session
	 * @param response
	 * @param mobile
	 * @param type
	 * @return
	 * @return: Map
	 * @author: zengmin
	 * @date: 2017???4???7??? ??????10:16:24
	 */
	@RequestMapping("send_code")
	public @ResponseBody Map send_code(HttpSession session, HttpServletResponse response, String yz, String mobile,
			String type) {
		try {
			if (!StringUtils.hasLength(mobile)) {
				return super.getAjaxResult("999", "????????????", null);
			}

			// WxUser wxUser = wxUserService.selectByMobile(mobile);
			// if (null != wxUser &&
			// StringUtils.hasLength(wxUser.getQqOpenid())) {
			// return super.getAjaxResult("999", "????????????????????????????????????", null);
			// }

			/*String imgCode = (String) session.getAttribute("imgCode");
			System.out.println("imgCode-send_code:" + imgCode + "\tymyz" + yz);
			if (!yz.equalsIgnoreCase(imgCode)) {
				return super.getAjaxResult("999", "???????????????", null);
			}*/
			String vcode = (int) ((Math.random() * 9 + 1) * 100000) + "";
			System.out.println("-----?????????????????????----" + vcode);

			MessageInfo messageInfo = new MessageInfo();
			messageInfo.setId(UUIDGenerator.getUUID());
			messageInfo.setDeleted(0);
			messageInfo.setMobile(mobile);
			messageInfo.setType(1);// ??????
			messageInfo.setCreateTime(new Date());
			if ("reg".equals(type)) {
				messageInfo.setContent("{code:\"" + vcode + "\",product:\"" + AldyMessageUtil.SMSPRE + "\"}");
			}
			messageInfo.setStatus(0);// ?????????
			messageInfo.setTemplateId(AldyMessageUtil.MsmTemplateId.REG);
			messageInfoService.addMessageInfo(messageInfo);
			if (!StringUtils.hasLength(vcode)) {
				return super.getAjaxResult("999", "?????????????????????", null);
			}
			MsmValidateBean msmValidateBean = new MsmValidateBean(mobile, new Date(), vcode);
			session.setAttribute("mvb", msmValidateBean);
			return super.getSuccessAjaxResult("???????????????????????????");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.getAjaxResult("999", "?????????????????????", null);
	}

	/**
	 * ????????????????????????
	 * 
	 * @Title: is_recon
	 * @Description: TODO
	 * @param session
	 * @param response
	 * @param mobile
	 * @param type
	 * @return
	 * @return: Map
	 * @author: zengmin
	 * @date: 2017???5???5??? ??????10:16:24
	 */
	@RequestMapping("is_recon")
	public @ResponseBody Map is_recon(HttpServletRequest request, HttpServletResponse response) {
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		TjyUser tjyUser = tjyUserService.selectByPrimaryKey(userId);
		if (null == tjyUser) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		if ("1".equals(tjyUser.getIsRealname() + "")) {
			return super.getSuccessAjaxResult();
		}
		if (null != tjyUser.getReconStatus() && tjyUser.getReconStatus().intValue() == 2) {
			return super.getSuccessAjaxResult();
		}
		return super.getAjaxResult("600", "?????????", null);
	}

	private String encodeURL(String url, HttpServletRequest request) throws UnsupportedEncodingException {
		StringBuffer result = null;
		if (StringUtils.hasLength(url)) {
			url = url.replaceAll("@@2@@", "&");
			result = new StringBuffer();
			for (int i = 0; i < url.length(); i++) {
				if ((url.charAt(i) + "").getBytes().length > 1) {
					result.append(URLEncoder.encode(url.charAt(i) + "", "UTF-8"));
				} else {
					result.append(url.charAt(i));
				}
			}
			String res = result.toString();
			String contentPath = sysconfigService.getSysconfig().getWebSite();
			String path = request.getContextPath();
			String cp = contentPath + path;
			if (res.indexOf(cp) == -1 || !"http://".equals(res.substring(0, 7))) {
				return cp + res;
			}
			return res;
		} else {
			return null;
		}
	}

	/**
	 * ???????????????????????????????????????????????????????????????
	 * 
	 * @Title: getTjyUser
	 * @Description: TODO
	 * @param session
	 * @param response
	 * @param mobile
	 * @param type
	 * @return
	 * @return: Map
	 * @author: zengmin
	 * @date: 2017???5???5??? ??????10:16:24
	 */
	@RequestMapping("getTjyUser")
	public @ResponseBody Map getTjyUser(HttpServletRequest request, HttpServletResponse response) {
		if (!ServletUtil.isLogin(request)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		Member me = ServletUtil.getMember(request);
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		TjyUser tjyUser = tjyUserService.selectByPrimaryKey(userId);
		if (null == tjyUser) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		if (StringUtils.isEmpty(tjyUser.getKfTelephone())) {
			tjyUser.setKfTelephone(sysconfigService.getSysconfig().getServiceTelphoneList());
		}
		return super.getSuccessAjaxResult("????????????", tjyUser);
	}
	/**
	 * ???????????????????????????????????????????????????????????????
	 * 
	 * @Title: getTjyUser
	 * @Description: TODO
	 * @param session
	 * @param response
	 * @param mobile
	 * @param type
	 * @return
	 * @return: Map
	 * @author: zengmin
	 * @date: 2017???5???5??? ??????10:16:24
	 */
	@RequestMapping("getTjyUserByid")
	public @ResponseBody Map getTjyUserByid(HttpServletRequest request, HttpServletResponse response,String uid) {
		if (!ServletUtil.isLogin(request)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		Member me = ServletUtil.getMember(request);
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		TjyUser tjyUser = tjyUserService.selectByPrimaryKey(uid);
		if (null == tjyUser) {
			return super.getAjaxResult("0", "???????????????", null);
		}
		if (StringUtils.isEmpty(tjyUser.getKfTelephone())) {
			tjyUser.setKfTelephone(sysconfigService.getSysconfig().getServiceTelphoneList());
		}
		return super.getSuccessAjaxResult("????????????", tjyUser);
	}

	@RequestMapping("getWxUser")
	public @ResponseBody Map getWxUser(HttpServletRequest request, HttpServletResponse response) {
		if (!ServletUtil.isLogin(request)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		Member me = ServletUtil.getMember(request);
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		WxUser wxUser = wxUserService.selectByUserId(userId);
		if (null == wxUser) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		return super.getSuccessAjaxResult("????????????", wxUser);
	}

	@RequestMapping("shareconfig")
	public @ResponseBody Map shareconfig(HttpServletRequest request, HttpServletResponse response) {
		return super.getSuccessAjaxResult("????????????", null);
	}

	public static void main(String[] args) {
		String ss = "{\"DeviceId\":\"6bd7af4cd63738b8c548f9754659c72d\",\"OpenId\":\"oTxXHwwoJVO6W_eN4ulo5JKbVt1c\"}";
		JSONObject jo = JSONObject.fromObject(ss);
		String wxuserId = (String) jo.get("OpenId");
		System.out.println("-------" + StringUtils.hasLength(wxuserId));
		String mobile = "13760262786";
		String nickN = mobile.replace(mobile.substring(3, 8), "**");
		System.out.println(nickN);

		WxUser u = new WxUser();
		u.setUsertype((byte) 1);
		System.out.println(u.getUsertype());

		System.out.println(StringUtils.hasLength(null));

		System.out.println(StringUtils.isEmpty(null));
	}
}
