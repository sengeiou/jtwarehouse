package com.wing.socialcontact.sys.service;

import com.wing.socialcontact.common.model.DataGrid;
import com.wing.socialcontact.common.model.PageParam;
import com.wing.socialcontact.sys.bean.Organization;

import java.util.List;

/**
 * Created by fenggang on 12/26/17.
 *
 * @author fenggang
 * @date 12/26/17
 */
public interface OrganizationService {


    List<Organization> findAll();

    /**
     * 条件查询
     * @param param
     * @return
     */
    DataGrid select(PageParam param, Organization organization);

    /**
     *
     * //TODO 获取所有的幻灯片所属的快捷入口
     * @return
     */
    List selectAll();
    /**
     *
     * //TODO 新增
     * @return
     */
    String add(Organization organization);

    /**
     *
     * //TODO 更新
     * @return
     */
    String update(Organization organization);

    /**
     *
     * //TODO 根据主键查询
     * @param key
     * @return
     */
    Organization selectByPrimaryKey(Long key);

    /**
     * 删除
     * //TODO 添加方法功能描述
     * @return
     */
    boolean delete(String[] ids);
}
