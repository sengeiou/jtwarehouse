package com.wing.socialcontact.service.wx.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wing.socialcontact.common.model.DataGrid;
import com.wing.socialcontact.common.model.PageParam;
import com.wing.socialcontact.service.wx.api.IMeetingService;
import com.wing.socialcontact.service.wx.api.IMeetingSignupService;
import com.wing.socialcontact.service.wx.api.IMeetingWhitelistService;
import com.wing.socialcontact.service.wx.bean.Meeting;
import com.wing.socialcontact.service.wx.bean.MeetingGuest;
import com.wing.socialcontact.service.wx.bean.MeetingProject;
import com.wing.socialcontact.service.wx.bean.MeetingWhitelist;
import com.wing.socialcontact.service.wx.bean.Project;
import com.wing.socialcontact.service.wx.dao.MeetingDao;
import com.wing.socialcontact.service.wx.dao.MeetingGuestDao;
import com.wing.socialcontact.service.wx.dao.MeetingProjectDao;
import com.wing.socialcontact.service.wx.dao.MeetingSignupDao;
import com.wing.socialcontact.service.wx.dao.ProjectDao;
import com.wing.socialcontact.sys.bean.SyDistrict;
import com.wing.socialcontact.sys.dao.DistrictDao;
import com.wing.socialcontact.sys.dao.ListValuesDao;
import com.wing.socialcontact.util.RedisCache;
import com.wing.socialcontact.vhall.api.BaseAPI;
import com.wing.socialcontact.vhall.api.WebinarAPI;

/**
 * 
 * @author liangwj
 * @date 2017-04-04 00:12:35
 * @version 1.0
 */
@Service
@CacheConfig(cacheNames = "default")
public class MeetingServiceImpl implements IMeetingService{
	@Resource
	private IMeetingSignupService meetingSignupService;
	@Resource
	private MeetingDao meetingDao;
	
	@Resource
	private MeetingProjectDao meetingProjectDao;
	
	@Resource
	private MeetingGuestDao meetingGuestDao;
	
	@Resource
	private ProjectDao projectDao;
	
	@Resource
	private MeetingSignupDao meetingSignupDao;
	
	@Resource
	private RedisCache redisCache;
	
	@Resource
	private ListValuesDao listValuesDao;
	
	@Resource
	private DistrictDao districtDao;
	

	@Resource
	private IMeetingWhitelistService meetingWhitelistService;
	
	/**
	 * ??????
	 * @param t
	 * @author liangwj
	 * @date 2017-04-04 00:12:35
	 */
	public int insertMeeting(Meeting t) {
		t.setId(t.getId()==null||t.getId().trim().length()==0?null:t.getId());
		int n = meetingDao.insert(t);
		//???????????????????????????
		t.parse();
		
		List<Project> list1 = t.getMeetingProjects();
		for(Project g : list1){
			MeetingProject e = new MeetingProject();
			e.setMeetingId(t.getId());
			e.setProjectId(g.getId());
			e.setCreateTime(new Date());
			meetingProjectDao.insert(e);
		}
		
		List<MeetingGuest> list2 = t.getMeetingGuests();
		Integer i=1;
		for(MeetingGuest g : list2){
			g.setSort(i++);
			meetingGuestDao.insert(g);
		}
		return n;
	}
	/**
	 * ??????
	 * @param t
	 * @return
	 * @author liangwj
	 * @date 2017-04-04 00:12:35
	 */
	@CacheEvict(key="'TJY:Meeting:'+#id",allEntries=true)
	public int updateMeeting(Meeting t) {
		int n = meetingDao.updateByPrimaryKey(t);
		//???????????????????????????
		t.parse();
		
		meetingProjectDao.deleteByMeetingId(t.getId());
		
		meetingGuestDao.deleteByMeetingId(t.getId());
		
		List<Project> list1 = t.getMeetingProjects();
		for(Project g : list1){
			MeetingProject e = new MeetingProject();
			e.setMeetingId(t.getId());
			e.setProjectId(g.getId());
			e.setCreateTime(new Date());
			meetingProjectDao.insert(e);
		}
		
		List<MeetingGuest> list2 = t.getMeetingGuests();
		Integer i=1;
		for(MeetingGuest g : list2){
			g.setSort(i++);
			meetingGuestDao.insert(g);
		}
		return n;
	}
	/**
	 * ??????
	 * @param t
	 * @return
	 * @author liangwj
	 * @date 2017-04-04 00:12:35
	 */
	@CacheEvict(key="'TJY:Meeting:'+#id",allEntries=true)
	public int deleteMeeting(Meeting t) {
		return meetingDao.delete(t);
	}
	/**
	 * ??????
	 * @param t
	 * @return
	 * @author liangwj
	 * @date 2017-04-04 00:12:35
	 */
	@Cacheable(key="'TJY:Meeting:'+#id")
	public List<Meeting> queryMeeting(Meeting t) {
		return meetingDao.select(t);
	}
	/**
	 * ????????????
	 * @param t
	 * @return
	 * @author liangwj
	 * @date 2017-04-04 00:12:35
	 */
	@Cacheable(key="'TJY:Meeting:'+#id")
	public Meeting getMeeting(String id) {
		Meeting m =  meetingDao.selectByPrimaryKey(id);
		
		//??????????????????????????????
		List<MeetingGuest>  guests = meetingGuestDao.selectByMeetingId(m.getId());
		m.setMeetingGuests(guests);
		
		List<Project> projects =  projectDao.selectByMeetingId(m.getId());
		m.setMeetingProjects(projects);
		//?????????????????????
		m.setPname(place(m.getProvince()));
		m.setCname(place(m.getCity()));
		m.setConame(place(m.getCounty()));
		return m;
	}
	
	public String place (String districkid){
		String name = "";
		SyDistrict d = districtDao.selectByPrimaryKey(districkid);
		if(null!=d){
			name = d.getDisName();
		}
		return name ;
	}
	/**
	 * ????????????
	 * @param param
	 * @param t
	 * @return
	 * @author liangwj
	 * @date 2017-04-04 00:12:35
	 */
	public DataGrid selectAllMeeting(PageParam param, Meeting t) {
		
		PageHelper.startPage(param.getPage(), param.getRows());
		List<Meeting> list = meetingDao.selectByParam(t);
		PageInfo<Meeting> page = new PageInfo<Meeting>(list);
		DataGrid data=new DataGrid(page);
		return data;
	}
	/**
	 * vhall k??????
	 * @param openK ????????????k?????? 1??????0?????????
	 * @param tjyUserId
	 * @param webinar_id
	 * @return
	 */
	public String createVhallKey(String openK,String tjyUserId,String webinar_id) {
		String k = BaseAPI.createVedioSignWithoutRegisterUser(tjyUserId, tjyUserId, webinar_id).get("sign").toString();
		String key = "TJY:VHALL:K:"+k;
		redisCache.put(key, k, 3600L);//1??????????????????
		
		WebinarAPI.wholeAuthUrlWebinar("1".equals(openK)?1:0,BaseAPI.AUTH_URL,BaseAPI.FAILURE_URL,1);
		return k;
	}
	/**
	 * vhall k??????
	 * @param k
	 * @return
	 */
	public String validVhallKey(String k) {
		if(k==null){
			return "fail";
		}else{
			String key = "TJY:VHALL:K:"+k;
			
			ValueWrapper obj = redisCache.get(key);
			String value = obj!=null?(String)obj.get():null;
			
			if(value!=null&&value.trim().length()>0){
				redisCache.evict(key);
				return "pass";
			}else{
				return "fail";
			}
		}
	}
	public DataGrid selectMyCollectMeeting(PageParam param, Meeting t) {
		PageHelper.startPage(param.getPage(), param.getRows());
		List<Meeting> list = meetingDao.selectMyCollectMeeting(t);
		PageInfo<Meeting> page = new PageInfo<Meeting>(list);
		DataGrid data=new DataGrid(page);
		return data;
	}
	/**
	 * ?????????????????????
	 * @param param
	 * @param t
	 * @return
	 */
	public DataGrid selectAllMeetingWhitelist(PageParam param, MeetingWhitelist t) {
		return meetingWhitelistService.selectAllMeetingWhitelist(param, t);
	}
	/**
	 * ?????????????????????
	 * @param meetingId
	 * @param ids
	 * @return
	 */
	public String insertWhitelists(String meetingId,String ids) {
		return meetingWhitelistService.insertWhitelists(meetingId, ids);
	}
	/**
	 * ?????????????????????
	 * @param ids
	 * @return
	 */
	public String deleteGlobalWhitelists(String ids) {
		return meetingWhitelistService.deleteMeetingWhitelists(ids);
	}
	/**
	 * ????????????????????????
	 * @param userId
	 * @param meetingId
	 * @return
	 */
	public boolean isWhitelist(String userId, String meetingId) {
		int s = meetingDao.isWhitelist(userId, meetingId);
		return s>0?true:false;
	}
}