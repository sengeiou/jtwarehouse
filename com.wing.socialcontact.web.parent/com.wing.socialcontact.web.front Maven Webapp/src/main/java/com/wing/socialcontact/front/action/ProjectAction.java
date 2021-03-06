package com.wing.socialcontact.front.action;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.owasp.esapi.ESAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.wing.socialcontact.common.action.BaseAction;
import com.wing.socialcontact.common.model.DataGrid;
import com.wing.socialcontact.common.model.Member;
import com.wing.socialcontact.common.model.PageParam;
import com.wing.socialcontact.config.OssConfig;
import com.wing.socialcontact.front.util.EsapiTest;
import com.wing.socialcontact.front.util.JavaMapUtil;
import com.wing.socialcontact.service.wx.api.IAttentionService;
import com.wing.socialcontact.service.wx.api.IKeywordsService;
import com.wing.socialcontact.service.wx.api.IMeetingService;
import com.wing.socialcontact.service.wx.api.IProjectImagesService;
import com.wing.socialcontact.service.wx.api.IProjectRecommendService;
import com.wing.socialcontact.service.wx.api.IProjectService;
import com.wing.socialcontact.service.wx.api.IProjectWillService;
import com.wing.socialcontact.service.wx.api.ITjyUserService;
import com.wing.socialcontact.service.wx.api.IUserIntegralLogService;
import com.wing.socialcontact.service.wx.bean.Attention;
import com.wing.socialcontact.service.wx.bean.Dynamic;
import com.wing.socialcontact.service.wx.bean.Keywords;
import com.wing.socialcontact.service.wx.bean.Project;
import com.wing.socialcontact.service.wx.bean.ProjectImages;
import com.wing.socialcontact.service.wx.bean.ProjectRecommend;
import com.wing.socialcontact.service.wx.bean.ProjectWill;
import com.wing.socialcontact.service.wx.bean.TjyUser;
import com.wing.socialcontact.service.wx.bean.UploadResultVo;
import com.wing.socialcontact.sys.bean.ListValues;
import com.wing.socialcontact.sys.service.IListValuesService;
import com.wing.socialcontact.util.BeanMapUtils;
import com.wing.socialcontact.util.Constants;
import com.wing.socialcontact.util.FileUploadUtil;
import com.wing.socialcontact.util.ServletUtil;
import com.wing.socialcontact.util.SpringContextUtil;
import com.wing.socialcontact.utils.CtxHolder;
import com.wing.socialcontact.vhall.api.BaseAPI;
/**
 * ?????????-??????
 */
@Controller
@RequestMapping("")
public class ProjectAction extends BaseAction{
	private static final Logger logger = LoggerFactory.getLogger(ProjectAction.class);
	@Autowired
	private IProjectService projectService;
	@Autowired
	private IProjectWillService projectWillService;
	@Autowired
	private IProjectRecommendService projectRecommendService;
	@Autowired
	private IListValuesService listValuesService;
	@Autowired
	private IAttentionService attentionService;
	@Autowired
	private IProjectImagesService projectImagesService;
	@Autowired
	private IKeywordsService keywordsService;
	@Autowired
	private ITjyUserService tjyUserService;
	@Autowired
	private IMeetingService meetingService;
	@Autowired
	private IUserIntegralLogService userIntegralLogService;
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping("/m/project/index")
	public String projectload(HttpServletRequest request,ModelMap modelMap){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return "login";
		}
		//response.addHeader("timeout", "1");
		DataGrid dataGrid1 = queryProjectByType(0, 1);
		modelMap.put("list1", dataGrid1.getRows());
		modelMap.put("pageSize1", dataGrid1.getPages());
		return "investment/project/index";
	}
	/**
	 * ??????????????????(1.1)
	 * @return
	 */
	@RequestMapping("/m/project/index1")
	public String projectindex(HttpServletRequest request,ModelMap modelMap){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return "login";
		}
		return "investment/project/xmlynav";
	}
	/**
	 * ????????????????????????
	 */
	@RequestMapping(value = "/m/project/project/list")
	public @ResponseBody Map libraryList(HttpServletRequest request, HttpServletResponse response, 
			Integer page, Integer size,  String key) throws IOException {
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		List<Map> res = new ArrayList<Map>();
		res = projectService.getTjyProjectByTerm(page,size,key,userId);

		return super.getSuccessAjaxResult(Constants.AJAX_MSG_SUCCESS, res);
	}
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping("/m/project/detail/index")
	public String detailProject(HttpServletResponse response,HttpServletRequest request,ModelMap modelMap,String id){
		Member me = ServletUtil.getMember(request);
		if (null == me) {
			return "login";
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return "login";
		}
		Project p = this.projectService.getProject(id,userId);
		modelMap.put("obj", p);
		
		if(org.apache.commons.lang3.StringUtils.isNotBlank(p.getWebinarId())){
			
			TjyUser tjyUser = tjyUserService.selectByPrimaryKey(me.getId());
			Map<String, Object> signMap = null;
			String roomId = "";
			if (me != null && p != null) {
				roomId = p.getWebinarId() == null ? "" : p.getWebinarId()
						.toString();
				if (roomId != null && !"".equals(roomId)) {
					signMap = BaseAPI.createVedioSign(tjyUser.getId(),
							tjyUser.getNickname(), roomId);
				}
			}
			modelMap.put("signObj", signMap);
			
			/*String openK = ApplicationPath.getParameter("vhall_open_k");
			String k = meetingService.createVhallKey(openK,me.getId(), p.getWebinarId());
			TjyUser tjyUser = tjyUserService.selectByPrimaryKey(me.getId());
			try {
				String url = "http://e.vhall.com/webinar/inituser/"+p.getWebinarId()+"?"
						+ "embed=video&mute=0&hideVideoControlBar=1&email="+MD5UtilWx.MD5Encode(tjyUser.getId(), "UTF-8").toLowerCase()+"@vhall.com&"
						+ "name="+URLEncoder.encode(tjyUser.getNickname(), "UTF-8")+"&"
						+ "k="+k;
				modelMap.put("url", url);
			} catch (UnsupportedEncodingException e) {
				String url ="redirect:http://e.vhall.com/webinar/inituser/"+p.getWebinarId()+"?"
						+ "embed=video&mute=0&hideVideoControlBar=1&email="
						+MD5UtilWx.MD5Encode(tjyUser.getId(), "UTF-8").toLowerCase()+"@vhall.com&"
						+ "k="+k;
				modelMap.put("url", url);
			}*/
		}
		
		return "investment/project/detail";
	}
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping("/m/project/attention")
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
				t.setAttType("prj");
				t.setCreateTime(new Date());
				t.setDeleted(0);
				t.setFkId(id);
				t.setUserId(CtxHolder.getUserId());
				attentionService.insertAttention(t);
			}
			//????????????????????????
			int counts = attentionService.selectCount(new Attention(null,id));
			result.put("result_code", "0");
			result.put("counts", counts+"");
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
	@RequestMapping("/m/project/removeattention")
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
			//????????????????????????
			int counts = attentionService.selectCount(new Attention(null,id));
			result.put("result_code", "0");
			result.put("counts", counts+"");
			return getAjaxResult("0", "", result);
		} catch (Exception e) {
			result.put("result_msg", "??????????????????");
			return getAjaxResult("0", "", result);
		}
	}
	/**
	 * ????????????????????????????????????
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/m/project/signup/index")
	public String indexSignup(ModelMap modelMap,String id,HttpServletRequest request){
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
		//????????????
		List list = listValuesService.selectListByType(8006004);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("id", id);
		return "investment/project/signup";
	}
	/**
	 * ??????????????????????????????
	 * @return
	 */
	@RequestMapping("/add/m/project/signup/save")
	@ResponseBody
	public Map<String,Object> saveForSignup(ProjectWill t){
		Map<String,String> result = Maps.newHashMap();
		result.put("result_code", "-1");
		result.put("result_msg", "????????????");
		t.setWillDesc(EsapiTest.stripXSS(t.getWillDesc()));
		try {
			String userId = CtxHolder.getUserId();
			if (StringUtils.isBlank(userId)) {
				return super.getAjaxResult("302", "????????????", null);
			}
			
		
			
			if(t==null||StringUtils.isBlank(t.getPrjId())){
				result.put("result_msg", "??????????????????");
				return getAjaxResult("0", "", result); 
			}
			
			ProjectWill p = new ProjectWill();
			p.setUserId(CtxHolder.getUserId());
			p.setPrjId(t.getPrjId());
			
			
			//???????????????
			if(projectWillService.queryProjectWill(p).size()>0){
				result.put("result_code", "2");
				return getAjaxResult("0", "", result); 
			}
			
			t.setUserId(CtxHolder.getUserId());
			t.setCreateTime(new Date());
			t.setDeleted(0);
			projectWillService.insertProjectWill(t);
			//???????????????????????????90
			userIntegralLogService.addLntAndEmp(userId, "task_0012");
			result.put("result_code", "0");
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
	@SuppressWarnings("rawtypes")
	@RequestMapping("/m/project/recommend/index")
	public String loadRecommend(HttpServletRequest request,ModelMap modelMap){
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
		//????????????
		List list = listValuesService.selectListByType(8006003);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("user", user);
		return "investment/project/recommend";
	}
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping("/m/project/recommend/detail/index")
	public String detailRecommend(HttpServletResponse response,ModelMap modelMap,String id){
		ProjectImages t = new ProjectImages();
		t.setProjectId(id);
		modelMap.put("obj", this.projectRecommendService.getProjectRecommend(id));
		modelMap.put("list", this.projectImagesService.queryProjectImages(t));
		return "investment/project/recommendDetail";
	}
	/**
	 * ????????????
	 * @return
	 */
	@RequestMapping(value="/m/project/recommend/saveorupdate",method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveOrUpdateRecommend(ProjectRecommend t){
		String userId = CtxHolder.getUserId();
		if(StringUtils.isBlank(userId)){
			return getAjaxResult("302", "", "");
		}
		t.setUserId(userId);
		//
		//????????????
		String[] protypes=t.getPrjType().split(",");
		for(int i = 0;i<protypes.length;i++){
			ListValues lv =  listValuesService.selectByPrimaryKey(protypes[i]);
			if(i==0){
				t.setPrjTypeName(lv.getListValue());
			}else{
				t.setPrjTypeName(t.getPrjTypeName()+","+lv.getListValue());
			}
		}
		if(t.getId()==null||t.getId().trim().length()==0){
			projectRecommendService.insertProjectRecommend(t);
		}else{
			projectRecommendService.updateProjectRecommend(t);
		}
		return getAjaxResult("0", "", "");
	}
	/**
	 * ??????????????????
	 * @param type ???????????? 1?????????2???????????????3?????????0??????
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@RequestMapping("/m/project/listpage")
	public String queryPageProject(HttpServletResponse response,ModelMap modelMap,Integer type,int pageIndex){
		DataGrid grid2 = queryProjectByType(type==null?0:type, pageIndex);
		modelMap.addAttribute("list", grid2.getRows());
		response.addHeader("ok", "1");
		return "investment/project/listpage";
	}
	/**
	 * ??????????????????
	 * @param type ???????????? 1?????????2???????????????3?????????
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DataGrid queryProjectByType(Integer type,int pageIndex){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("showEnable", 1);
		params.put("isApl", 1);
		params.put("orderBy", " order by p.is_recommend desc,p.sort desc, p.create_time desc ");
		type = type==null?0:type;
		if(1==type){
			//???????????????(??????????????????????????????????????????????????????????????????)
			params.put("leStartTime", new Date());
			params.put("geEndTime", new Date());
		}else if(2==type){
			//???????????????(???????????????????????????????????????????????????)
			params.put("gtStartTime", new Date());
			params.put("gtEndTime", new Date());
		}else if(3==type){
			//???????????????(???????????????????????????????????????????????????)
			params.put("ltStartTime", new Date());
			params.put("ltEndTime", new Date());
		}
		PageParam param2 = new PageParam();
		param2.setRows(10);
		param2.setPage(pageIndex);
		DataGrid grid2 = projectService.selectAllProject(param2 ,params);
		
		//??????????????????
		PageParam param = new PageParam(1, 1);
		List<Project> list = (List<Project>) grid2.getRows();
		Map<String, Object> parm = Maps.newHashMap();
		for(Project p : list){
			parm.put("prjId", p.getId());
			DataGrid grid3 = projectWillService.selectAllProjectWill(param , parm);
			p.setExtProp("willCount", grid3.getTotal());
		}
		return grid2;
	}
	/**
	 * ??????????????????
	 * @return
	 */
	@RequestMapping("/m/project/collect/index")
	public String loadCollect(HttpServletResponse response,ModelMap modelMap,String type){
		Map<String,Object> params = Maps.newHashMap();
		params.put("showEnable", 1);
		params.put("status", 1);
		DataGrid grid1 = projectRecommendService.selectAllProjectRecommend(new PageParam(1,20) ,params);
		//????????????????????????
		modelMap.addAttribute("prjCount", grid1.getTotal());
		//??????????????????
		modelMap.addAttribute("prjList", grid1.getRows());
		//????????????????????????
		int distinctUserCount = projectRecommendService.selectDistinctUserCount(new ProjectRecommend());
		modelMap.addAttribute("userCount", distinctUserCount);
		
		//??????????????????
		ProjectRecommend t2 = new ProjectRecommend();
		String userId = CtxHolder.getUserId();
		t2.setUserId(StringUtils.isBlank(userId)?"-1":userId);
		t2.setStatus(1);
		t2.setShowEnable(1);
		
		DataGrid grid2 = projectRecommendService.selectAllProjectRecommend(new PageParam(1,20) , BeanMapUtils.toMap(t2));
		
		modelMap.addAttribute("pageSize1", grid1.getPages());
		modelMap.addAttribute("pageSize2", grid2.getPages());
		modelMap.addAttribute("myPrjList", grid2.getRows());
		modelMap.addAttribute("type","2".equals(type)?2:1);
		return "investment/project/collect";
	}
	/**
	 * ????????????????????????
	 * @param type ???????????? 1?????????2???????????????3?????????
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@RequestMapping("/m/project/collect/listpage")
	public String queryPageProjectCollect(HttpServletResponse response,ModelMap modelMap,int type,int pageIndex){
		Map<String,Object> params = Maps.newHashMap();
		if(type==1){
			params.put("showEnable", 1);
			params.put("status", 1);
		}else{
			params.put("userId", CtxHolder.getUserId());
			params.put("showEnable", 1);
			params.put("status", 1);
		}
		
		PageParam param2 = new PageParam();
		param2.setRows(10);
		param2.setPage(pageIndex);
		param2.setOrder("createTime");
		DataGrid grid1 = projectRecommendService.selectAllProjectRecommend(param2 ,params);
		
		modelMap.addAttribute("list", grid1.getRows());
		return "investment/project/collectlistpage";
	}
	/**
	 * ????????????
	 * @param request
	 * @param jsonp
	 * @param pic
	 * @return
	 */
	@RequestMapping("/m/project/uploadpic")
	@ResponseBody
	public Map<String,Object> uploadImage(HttpServletRequest request,String jsonp,MultipartFile file) {
		try {
			UploadResultVo resultVo = new UploadResultVo(1,"????????????,???????????????");
			OssConfig ossConfig = (OssConfig) SpringContextUtil.getBean("ossConfig");
			String ossurl = ossConfig.getOss_getUrl();
			if (null == file) {
				resultVo.setMsg("???????????????????????????");
				return getAjaxResult("-1", "???????????????????????????", resultVo);
			}
			try {
				if(file.getSize() > 2097152){
					resultVo.setMsg("???????????????????????????:??????????????????2M??????");
					return getAjaxResult("-1", "???????????????????????????:??????????????????2M??????", resultVo);
				}
				InputStream io = file.getInputStream();
				String uploadFilePath = "recommend";
				String ext = FilenameUtils.getExtension(file.getOriginalFilename());
				String picPath = FileUploadUtil.uploadFileInputStream(io, ext, uploadFilePath);
				if (picPath!=""&&!"".equals(picPath)) {
					resultVo.setReturnCode(1);
					resultVo.setPicPath(picPath);
					resultVo.setMsg(picPath);
					resultVo.setImg_url(ossurl+picPath);
					return getAjaxResult("0", "", resultVo);
				}else{
					resultVo.setMsg("????????????????????????");
					return getAjaxResult("-1", "????????????????????????", resultVo);
				}
			} catch (Exception e) {
				resultVo.setMsg("????????????????????????");
				return getAjaxResult("-1", "????????????????????????", resultVo);
			}
		} catch (Throwable e) {
			return getAjaxResult("-1", "????????????????????????", null);
		}
	}
	/**
	 * ??????????????????
	 * @param type ???????????? 1?????????2???????????????3?????????
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/m/project/listpage2")
	public String queryPageProject2(ModelMap modelMap,int type,int pageIndex){
		DataGrid grid = null;
		try {
			PageParam pageParam = new PageParam(pageIndex<=0?1:pageIndex,5);
			Project param = new Project();
			param.setShowEnable(1);
			param.setUserId(CtxHolder.getUserId());
			if(type==1){//??????????????????
				param.setIsAttentioned(1);
				grid = projectService.selectAllProject(pageParam , BeanMapUtils.toMap(param));
			}else if(type==2){//?????????????????????
				param.setIsWilled(1);
				param.setUserId(CtxHolder.getUserId());
				grid = projectService.selectAllProject(pageParam , BeanMapUtils.toMap(param));
				List<Project> list = grid.getRows();
				for(Project p : list){
					ProjectWill t = new ProjectWill();
					t.setPrjId(p.getId());
					t.setUserId(CtxHolder.getUserId());
					DataGrid  dg = projectWillService.selectAllProjectWill(new PageParam(1,1),BeanMapUtils.toMap(t));
					
					p.setExtProp("will", dg.getRows().size()>0?dg.getRows().get(0):null);
				}
			}else if(type==3){//??????????????????
				ProjectRecommend t = new ProjectRecommend();
				t.setUserId(CtxHolder.getUserId()==null?"-1":CtxHolder.getUserId());
				grid = projectRecommendService.selectAllProjectRecommend(pageParam,BeanMapUtils.toMap(t));
			}
		} catch (Throwable e) {
			logger.error("??????????????????", e);
		}
		modelMap.addAttribute("type", type);
		modelMap.addAttribute("grid", grid==null?new DataGrid():grid);
		return "investment/project/myfootproject"; 
	}
	/**
	 * ??????????????????
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/m/project/search")
	public String search(HttpServletRequest request,ModelMap modelMap){
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
		DataGrid grid = keywordsService.selectAllTop(2, 5);
		modelMap.put("list", grid.getRows());
		return "investment/project/search";
	}
	/**
	 * ???????????????????????????
	 */
	@RequestMapping("/m/project/keywordlist")
	public @ResponseBody Map keywordlist(HttpServletRequest request,ModelMap modelMap,
			Integer pageIndex,String type,String keywords){
		Member me = (Member) request.getSession().getAttribute("me");
		if (null == me) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		String userId = me.getId();
		if (StringUtils.isEmpty(userId)) {
			return super.getAjaxResult("302", "??????????????????????????????", null);
		}
		
		List<Map> res = new ArrayList<Map>();
		
		PageParam param3 = new PageParam();
		param3.setRows(10);
		param3.setPage(pageIndex==null||pageIndex<=0?1:pageIndex.intValue());
		param3.setOrder("startTime desc");
		
		Project t = new Project();
		t.setTitles(keywords);
		t.setShowEnable(1);
		t.setIsApl(1);
		
		DataGrid grid3 = projectService.selectAllProject(param3 , BeanMapUtils.toMap(t));
		List<Project> list = grid3.getRows();
		Map<String,Object> parm = Maps.newHashMap();
		for(Project p : list){
			parm.put("prjId", p.getId());
			DataGrid grid = projectWillService.selectAllProjectWill(param3 , parm);
			p.setExtProp("willCount", grid.getTotal());
		}
		if(keywords!=null&&keywords.trim().length()>0){
			Keywords t1 = new Keywords();
			t1.setCreateTime(new Date());
			t1.setTypes(2);
			t1.setUserId(CtxHolder.getUserId());
			t1.setKeywords(keywords);
			this.keywordsService.insertKeywords(t1 );
		}
		return super.getSuccessAjaxResult("???????????????", list);
	}
	/**
	 * ????????????????????????
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/m/project/searchlist")
	public String searchProjectList(HttpServletRequest request,ModelMap modelMap,
			Integer pageIndex,String type,String keywords){
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
		PageParam param3 = new PageParam();
		param3.setRows(10);
		param3.setPage(pageIndex==null||pageIndex<=0?1:pageIndex.intValue());
		param3.setOrder("startTime desc");
		
		Project t = new Project();
		t.setTitles(keywords);
		t.setShowEnable(1);
		t.setIsApl(1);
		
		DataGrid grid3 = projectService.selectAllProject(param3 , BeanMapUtils.toMap(t));
		List<Project> list = grid3.getRows();
		Map<String,Object> parm = Maps.newHashMap();
		for(Project p : list){
			parm.put("prjId", p.getId());
			DataGrid grid = projectWillService.selectAllProjectWill(param3 , parm);
			p.setExtProp("willCount", grid.getTotal());
		}
		
		modelMap.addAttribute("list", grid3.getRows());
		modelMap.addAttribute("pages", grid3.getPages());
		modelMap.addAttribute("keywords", keywords);
		
		if(keywords!=null&&keywords.trim().length()>0){
			Keywords t1 = new Keywords();
			t1.setCreateTime(new Date());
			t1.setTypes(2);
			t1.setUserId(CtxHolder.getUserId());
			t1.setKeywords(keywords);
			this.keywordsService.insertKeywords(t1 );
		}
		return "2".equals(type)?"investment/project/searchlistpage":"investment/project/searchlist";
	}
	/**
	 * ??????????????????
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/m/project/collect/search")
	public String searchCollect(HttpServletRequest request,ModelMap modelMap){
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
		DataGrid grid = keywordsService.selectAllTop(3, 5);
		modelMap.put("list", grid.getRows());
		return "investment/project/searchcollect";
	}
	/**
	 * ????????????????????????
	 * @return
	 */
	@RequestMapping("/m/project/collect/searchlist")
	public String searchCollectList(HttpServletRequest request,ModelMap modelMap,
				Integer pageIndex,String type,String keywords){
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
		PageParam param3 = new PageParam();
		param3.setRows(10);
		param3.setPage(pageIndex==null||pageIndex<=0?1:pageIndex.intValue());
		param3.setOrder("startTime desc");
		
		ProjectRecommend t = new ProjectRecommend();
		t.setPrjName(keywords);
		t.setShowEnable(1);
		
		DataGrid grid3 = projectRecommendService.selectAllProjectRecommend(param3 , BeanMapUtils.toMap(t));
		modelMap.addAttribute("list", grid3.getRows());
		modelMap.addAttribute("pages", grid3.getPages());
		modelMap.addAttribute("keywords", keywords);
		
		if(keywords!=null&&keywords.trim().length()>0){
			Keywords t1 = new Keywords();
			t1.setCreateTime(new Date());
			t1.setTypes(1);
			t1.setUserId(CtxHolder.getUserId());
			t1.setKeywords(keywords);
			this.keywordsService.insertKeywords(t1 );
		}
		return "2".equals(type)?"investment/project/searchcollectlistpage":"investment/project/searchcollectlist";
	}
}
