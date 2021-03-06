package com.wing.socialcontact.action;


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wing.socialcontact.common.model.DataGrid;
import com.wing.socialcontact.common.model.PageParam;
import com.wing.socialcontact.config.MsgConfig;
import com.wing.socialcontact.service.wx.api.IMeetingService;
import com.wing.socialcontact.service.wx.api.IMeetingSignupService;
import com.wing.socialcontact.service.wx.api.IProjectService;
import com.wing.socialcontact.service.wx.api.ITjyUserService;
import com.wing.socialcontact.service.wx.api.IWxUserService;
import com.wing.socialcontact.service.wx.bean.Meeting;
import com.wing.socialcontact.service.wx.bean.MeetingSignup;
import com.wing.socialcontact.service.wx.bean.MeetingSignupRemind;
import com.wing.socialcontact.service.wx.bean.MeetingWhitelist;
import com.wing.socialcontact.service.wx.bean.Project;
import com.wing.socialcontact.service.wx.bean.TjyUser;
import com.wing.socialcontact.sys.action.BaseAction;
import com.wing.socialcontact.sys.bean.ListValues;
import com.wing.socialcontact.sys.bean.SyDistrict;
import com.wing.socialcontact.sys.service.IDistrictService;
import com.wing.socialcontact.sys.service.IListValuesService;
import com.wing.socialcontact.util.BeanMapUtils;
import com.wing.socialcontact.util.DateUtil;
import com.wing.socialcontact.util.DateUtils;
import com.wing.socialcontact.util.POIUtil;
import com.wing.socialcontact.util.ServletUtil;
import com.wing.socialcontact.util.StringUtil;
import com.wing.socialcontact.util.UUIDGenerator;
import com.wing.socialcontact.vhall.api.WebinarAPI;
import com.wing.socialcontact.vhall.resp.WebinarListResp;

/**
 * ?????????-????????????
 */
@Controller
@RequestMapping("/meeting")
public class MeetingAction extends BaseAction{
	private static Logger logger = LoggerFactory.getLogger(Meeting.class);
	@Autowired
	private IMeetingService meetingService;
	@Autowired
	private IProjectService projectService;
	@Autowired
	private IMeetingSignupService meetingSignupService;
	@Autowired
	private ITjyUserService tjyUserService;
	@Autowired
	private IWxUserService wxUserService;
	@Autowired
	private IListValuesService listValuesService;
	@Autowired
	private IDistrictService districtService;
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping("meetingindex")
	public String loadMeeting(){
		return "meeting/meetingindex";
	}
	/**
	 * ??????????????????
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("meetingquery")
	public ModelAndView queryMeeting(PageParam param,Meeting t){
		DataGrid grid = this.meetingService.selectAllMeeting(param, t);
		List<Meeting> list = grid.getRows();
		for(Meeting m : list){
			//??????????????????
			m.calcStatus();
		}
		return ajaxJsonEscape(grid);
	}
	/**
	 * ?????????????????????
	 * @return
	 */
	@RequestMapping("meetingwhitelist")
	public String loadMeetingWhitelist(ModelMap map,String meetingId){
		map.addAttribute("meetingId", StringUtils.isEmpty(meetingId)?"-1":meetingId);
		return "meeting/meetingwhitelist";
	}
	/**
	 * ?????????????????????
	 * @return
	 */
	@RequestMapping("meetingwhitelistquery")
	public ModelAndView queryMeetingWhitelist(PageParam param,MeetingWhitelist t){
		DataGrid grid = this.meetingService.selectAllMeetingWhitelist(param, t);
		return ajaxJsonEscape(grid);
	}
	/**
	 * ??????????????????????????????
	 * @return
	 */
	@RequestMapping("meetingaddpage")
	public String addPageMeeting(ModelMap map){
		// ?????????
		List provinceList = districtService.selectDistrictByType("1");
		map.addAttribute("provinceList", provinceList);
		return "meeting/meetingaddorupdate";
	}
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping("meetingupdatepage")
	public String updatePageMeeting(ModelMap modelMap,String id){
		Meeting m = this.meetingService.getMeeting(id);
		modelMap.addAttribute("obj", m);
		// ?????????
		List provinceList = districtService.selectDistrictByType("1");
		modelMap.addAttribute("provinceList", provinceList);
		return "meeting/meetingaddorupdate";
	}
	/**
	 * ???????????????????????????
	 * @return
	 */
	@RequestMapping(value="meetingsaveorupdate",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveOrUpdateMeeting(Meeting t){
		try {
			if(t.getId()==null||t.getId().trim().length()==0){
				t.setId(UUIDGenerator.getUUID());
				t.setCreateTime(new Date());
				t.setCreateUserId(ServletUtil.getMember().getId());
				this.meetingService.insertMeeting(t);
				return getAjaxResult("0", "", null);
			}else{
				this.meetingService.updateMeeting(t);
				return getAjaxResult("0", "", null);
			}
		} catch (Exception e) {
			logger.error("????????????????????????", e);
			return getAjaxResult("-1", "????????????", null);
		}
	}
	/**
	 * ????????????
	 * @param id
	 * @return
	 */
	@RequestMapping("meetingdetail")
	public String detailMeeting(ModelMap modelMap,String id){	
		Meeting m = this.meetingService.getMeeting(id);
		modelMap.addAttribute("obj", m);
		return "meeting/meetingdetail";
	}
	/**
	 * ??????????????????????????????
	 * @param ids
	 * @return
	 */
	@RequestMapping("meetingdel")
	@ResponseBody
	public Map<String,Object> deleteMeeting(Meeting t){	
		try {
			this.meetingService.deleteMeeting(t);
			return getAjaxResult("0", "", null);
		} catch (Exception e) {
			return getAjaxResult("-1", "????????????", null);
		}
	}
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping("signupindex")
	public String loadSignup(ModelMap modelMap,String meetingId){
		modelMap.addAttribute("meetingId", meetingId);
		return "meeting/signupindex";
	}
	/**
	 *  ????????????????????????
	 * @param param 
	 * @param t 
	 * @return
	 */
	@RequestMapping("signupquery")
	public ModelAndView querySignup(PageParam param, String nickname,String mobile,String signupTime,
			String meetingId,Integer attendType,Integer payType){
		Map<String, Object> t = new HashMap<String, Object>();
		t.put("nickname", nickname);
		t.put("mobile", mobile);
		t.put("signupTime", signupTime);
		t.put("attendType", attendType);
		t.put("meetingId", meetingId);
		t.put("payType", payType);
		
		return ajaxJsonEscape(this.meetingSignupService.selectAllMeetingSignup(param, t));
	}
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping("signupdetail")
	public String detailSignup(ModelMap modelMap,String id){
		MeetingSignup ms = this.meetingSignupService.getMeetingSignup(id);
		TjyUser tjyUser = null;
		Map<String,Object> wxUser = null;
		if(ms!=null&&StringUtils.isNotBlank(ms.getUserId())){
			tjyUser = getTjyUserById(ms.getUserId());
			wxUser  = getWxUserById(ms.getUserId());
		}
		ms = ms==null?new MeetingSignup():ms;
		tjyUser =tjyUser==null?new TjyUser():tjyUser;
		wxUser = wxUser==null?new HashMap<String,Object>():wxUser;
		ms.setMobile(StringUtils.isBlank(ms.getMobile())?tjyUser.getMobile():ms.getMobile());
		
		modelMap.addAttribute("obj", ms);
		modelMap.addAttribute("user", tjyUser);
		modelMap.addAttribute("tjyUser",tjyUser);
		modelMap.addAttribute("wxUser",wxUser);
		return "meeting/signupdetail";
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
	/**
	 * ????????????
	 * @param request
	 * @param jsonp
	 * @param pic
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("uploadpic")
	@ResponseBody
	public Map uploadPic(HttpServletRequest request,String jsonp,MultipartFile file,String ysStyle,String moduleName) {
		return super.uploadImage(request, jsonp, file,ysStyle,moduleName);
	}
	/**
	 * ????????????
	 * @return
	 */
	//@RequiresPermissions("newsclass:read")
	@RequestMapping("projet/index")
	public String loadProject(){
		return "meeting/projectindex";
	}
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping("projet/query")
	public ModelAndView queryProject(PageParam param,Project t){
		return ajaxJsonEscape(this.projectService.selectAllProject(param, BeanMapUtils.toMap(t)));
	}
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping("vhall/index")
	public String loadVhall(){
		return "meeting/vhallindex";
	}
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping("vhall/query")
	public ModelAndView queryVhall(PageParam param,Project t){
		DataGrid data=new DataGrid();
		PageHelper.startPage(param.getPage(), param.getRows());
		WebinarListResp resp = WebinarAPI.listWebinar(param.getPage(),param.getRows());
		List<WebinarListResp.Webinar> list = resp.getList();
//		PageHelper.startPage(1, list.size()==0?20:list.size());
//		PageInfo<WebinarListResp.Webinar> page = new PageInfo<WebinarListResp.Webinar>(list);
		data.setRows(list);
		data.setTotal(resp.getTotal());
		return ajaxJsonEscape(data);
	}
	/**
	 * ????????????????????????
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/signup/export")
	public void exportSignup(HttpServletResponse response, HttpServletRequest request
			,String nickname,String mobile,String signupTime,String meetingId,Integer attendType,Integer payType) throws IOException{
		Map<String, Object> t = new HashMap<String, Object>();
		t.put("nickname", nickname);
		t.put("mobile", mobile);
		t.put("signupTime", signupTime);
		t.put("attendType", attendType);
		t.put("meetingId", meetingId);
		t.put("payType", payType);
		
		List<MeetingSignup> list = Lists.newArrayList();
		int pageNum = 1;
		int pages = 1;
		PageParam param = new PageParam(1, 200);
		
		for(;pageNum<=pages;pageNum++){
			param.setPage(pageNum);
			DataGrid grid = meetingSignupService.selectAllMeetingSignup(param , t);
			if(grid.getRows().size()>0){
				list.addAll(grid.getRows());
			}
			pages = grid.getPages();
		}
		
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet0 = wb.createSheet("????????????????????????");
		HSSFRow row0 = sheet0.createRow(0);

		HSSFCellStyle titleStyle = POIUtil.getTitleStyle(wb);
		HSSFCellStyle cellStyle = POIUtil.getCellStyle(wb);

		POIUtil.setCellValue(row0.createCell(0), titleStyle, "????????????");
		POIUtil.setCellValue(row0.createCell(1), titleStyle, "????????????");
		POIUtil.setCellValue(row0.createCell(2), titleStyle, "??????");
		POIUtil.setCellValue(row0.createCell(3), titleStyle, "????????????");
		POIUtil.setCellValue(row0.createCell(4), titleStyle, "?????????");
		POIUtil.setCellValue(row0.createCell(5), titleStyle, "????????????");
		POIUtil.setCellValue(row0.createCell(6), titleStyle, "??????");
		POIUtil.setCellValue(row0.createCell(7), titleStyle, "????????????");
		POIUtil.setCellValue(row0.createCell(8), titleStyle, "????????????");
//		POIUtil.setCellValue(row0.createCell(9), titleStyle, "????????????");
//		POIUtil.setCellValue(row0.createCell(10), titleStyle, "????????????(??????)");
//		POIUtil.setCellValue(row0.createCell(11), titleStyle, "????????????(??????)");
//		POIUtil.setCellValue(row0.createCell(12), titleStyle, "?????????(??????)");
//		POIUtil.setCellValue(row0.createCell(13), titleStyle, "????????????(??????)");
		POIUtil.setCellValue(row0.createCell(9), titleStyle, "????????????(???)");
		POIUtil.setCellValue(row0.createCell(10), titleStyle, "????????????");
		POIUtil.setCellValue(row0.createCell(11), titleStyle, "????????????");
		POIUtil.setCellValue(row0.createCell(12), titleStyle, "??????????????????");
		POIUtil.setCellValue(row0.createCell(13), titleStyle, "???????????????");
		POIUtil.setCellValue(row0.createCell(14), titleStyle, "?????????");
		POIUtil.setCellValue(row0.createCell(15), titleStyle, "????????????");
		POIUtil.setCellValue(row0.createCell(16), titleStyle, "????????????");
		POIUtil.setCellValue(row0.createCell(17), titleStyle, "?????????????????????");

		int rowIndex = 1;
			
		for(MeetingSignup ms :list){
			TjyUser tjyUser = getTjyUserById(ms.getUserId());
			Map<String,Object> wxUser = getWxUserById(ms.getUserId());
			tjyUser =tjyUser==null?new TjyUser():tjyUser;
			wxUser = wxUser==null?new HashMap<String,Object>():wxUser;
			ms.setMobile(StringUtils.isBlank(ms.getMobile())?tjyUser.getMobile():ms.getMobile());
			
			HSSFRow row = sheet0.createRow(rowIndex++);
			POIUtil.setCellValue(row.createCell(0), cellStyle,formatValue(ms.getTitles(),""));//????????????
			POIUtil.setCellValue(row.createCell(1), cellStyle, formatValue(ms.getNickname(),""));//????????????
			POIUtil.setCellValue(row.createCell(2), cellStyle, wxUser.get("sex")==null?"":("2".equals(wxUser.get("sex").toString())?"???":"???"));//??????
			POIUtil.setCellValue(row.createCell(3), cellStyle, formatValue(wxUser.get("birthday"),""));//????????????
			POIUtil.setCellValue(row.createCell(4), cellStyle, formatValue(ms.getMobile(),""));//?????????
			POIUtil.setCellValue(row.createCell(5), cellStyle, formatValue(ms.getComName(),""));//????????????
			POIUtil.setCellValue(row.createCell(6), cellStyle, formatValue(tjyUser.getJob(),""));//??????
			POIUtil.setCellValue(row.createCell(7), cellStyle, formatValue(tjyUser.getIndustry(),""));//????????????
			POIUtil.setCellValue(row.createCell(8), cellStyle, formatValue(tjyUser.getProvince()," ")+formatValue(tjyUser.getCity()," ")+formatValue(tjyUser.getRegion(),""));//????????????
//			POIUtil.setCellValue(row.createCell(9), cellStyle, formatValue(ms.getMainBusiness(),""));//????????????
//			POIUtil.setCellValue(row.createCell(10), cellStyle, formatValue(ms.getRegCapital(),""));//????????????
//			POIUtil.setCellValue(row.createCell(11), cellStyle, formatValue(ms.getPayCapital(),""));//????????????
//			POIUtil.setCellValue(row.createCell(12), cellStyle, formatValue(ms.getTotalAssets(),""));//?????????
//			POIUtil.setCellValue(row.createCell(13), cellStyle, formatValue(ms.getAnnualSales(),""));//????????????
			POIUtil.setCellValue(row.createCell(9), cellStyle, ms.getTicketPrice()==null||ms.getTicketPrice().doubleValue()<=0?"??????":StringUtil.fixed(ms.getTicketPrice()));
			POIUtil.setCellValue(row.createCell(10), cellStyle, ms.getAttendType()!=null||ms.getAttendType().intValue()==1?"????????????":"????????????");
			POIUtil.setCellValue(row.createCell(11), cellStyle, formatValue(ms.getOtherReq(),""));//????????????
			POIUtil.setCellValue(row.createCell(12), cellStyle, formatValue(ms.getKfTelephone(),""));//??????????????????
			POIUtil.setCellValue(row.createCell(13), cellStyle, formatValue(ms.getTjLinkMan(),""));//???????????????
			POIUtil.setCellValue(row.createCell(14), cellStyle, formatValue(ms.getRecLinkMan(),""));//?????????
			POIUtil.setCellValue(row.createCell(15), cellStyle, DateUtil.date2String(ms.getSignupTime(), "yyyy-MM-dd HH:mm:ss"));//????????????
			POIUtil.setCellValue(row.createCell(16), cellStyle, (ms.getOrderStatus()!=null&&ms.getOrderStatus().intValue()>1)?"?????????":"?????????");//????????????
			POIUtil.setCellValue(row.createCell(17), cellStyle, (ms.getPayType()!=null&&ms.getPayType().intValue()==2)?"???":"???");//?????????????????????
		}
		//????????????
		POIUtil.autoSizeColumn(row0);
		POIUtil.exportForExcle(response, wb, "????????????????????????");
	}
	/**   
	 *  ?????????????????????
	 */
	@RequestMapping("addwhitelistpage")
	public String addWhitelistPage(ModelMap map,String meetingId) {
		map.addAttribute("meetingId", meetingId);
		return "meeting/meetingaddwhitelist";
	}
	
	@RequestMapping("addwhitelist")
	public ModelAndView classadd(String meetingId,String ids) {
		if(StringUtils.isEmpty(meetingId)){
			return ajaxDoneError(MsgConfig.MSG_KEY_FAIL);
		}
		if(StringUtils.isEmpty(ids)){
			return ajaxDone(MsgConfig.MSG_KEY_SUCCESS);
		}
		try {
			return ajaxDone(meetingService.insertWhitelists(meetingId,ids));
		} catch (Exception e) {
			return ajaxDoneError(MsgConfig.MSG_KEY_FAIL);
		}

	}
	/**
	 *  ?????????????????????
	 */
	@RequestMapping("/whitelistpage/del")
	public ModelAndView del(String ids) {
		if(StringUtils.isEmpty(ids)){
			return ajaxDone(MsgConfig.MSG_KEY_SUCCESS);
		}
		try {
			return ajaxDone(meetingService.deleteGlobalWhitelists(ids)+"");
		} catch (Exception e) {
			return ajaxDoneError(MsgConfig.MSG_KEY_FAIL);
		}
	}
	
	private String formatValue(Object obj,String defaultValue){
		if(obj==null){
			return defaultValue;
		}
		if(obj instanceof Date){
			return DateUtils.dateToString((Date)obj, "yyyy-MM-dd");
		}else if(obj instanceof String){
			return obj.toString();
		}else if(obj instanceof Double){
			DecimalFormat a = new DecimalFormat("#.##"); 
			a.applyPattern("0.00"); 
			return a.format((Double)obj);
		}else if(obj instanceof Integer){
			return obj.toString();
		}else{
			return obj.toString();
		}
	}
	
	/**
	 * ???????????????????????????????????????
	 * @return
	 */
	@RequestMapping("/signupremind/index")
	public String loadSignupRemind(ModelMap modelMap,String meetingId){
		modelMap.addAttribute("meetingId", meetingId);
		return "meeting/signupremindindex";
	}
	/**
	 *  ????????????????????????
	 * @param param 
	 * @param t 
	 * @return
	 */
	@RequestMapping("/signupremind/query")
	public ModelAndView querySignupRemind(PageParam param, MeetingSignupRemind t){
		return ajaxJsonEscape(this.meetingSignupService.selectAllMeetingSignupRemind(param, t));
	}
	/**
	 *  ??????????????????????????????
	 * @param id 
	 * @return
	 */
	@RequestMapping("/signup/changestatus")
	public ModelAndView updateForChangestatus(String id){
		Map<String,String> result = Maps.newHashMap();
		int s = this.meetingSignupService.updateForChangestatus(id);
		if(s==0){
			result.put("code", "1");
		}else{
			result.put("code", "0");
		}
		return ajaxJsonEscape(result);
	}
}
