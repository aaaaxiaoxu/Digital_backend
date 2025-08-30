package com.dj.digitalplatform.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dj.digitalplatform.model.dto.institution.InstitutionQueryRequest;
import com.dj.digitalplatform.model.entity.Institution;
import com.dj.digitalplatform.model.vo.InstitutionVO;

import java.util.List;

/**
* @author xuliwei
* @description 针对表【institution(机构信息表)】的数据库操作Service
* @createDate 2025-08-30 00:52:34
*/
public interface institutionService extends IService<Institution> {

    /**
     * 获取机构视图对象
     *
     * @param institution 机构实体
     * @return InstitutionVO
     */
    InstitutionVO getInstitutionVO(Institution institution);

    /**
     * 获取机构视图对象列表
     *
     * @param institutionList 机构实体列表
     * @return List<InstitutionVO>
     */
    List<InstitutionVO> getInstitutionVOList(List<Institution> institutionList);

    /**
     * 获取查询条件
     *
     * @param institutionQueryRequest 查询请求
     * @return QueryWrapper<institution>
     */
    QueryWrapper<Institution> getQueryWrapper(InstitutionQueryRequest institutionQueryRequest);
}
