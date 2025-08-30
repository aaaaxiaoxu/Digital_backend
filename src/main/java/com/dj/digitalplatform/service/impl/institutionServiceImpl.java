package com.dj.digitalplatform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.digitalplatform.model.dto.institution.InstitutionQueryRequest;
import com.dj.digitalplatform.model.entity.Institution;
import com.dj.digitalplatform.model.vo.InstitutionVO;
import com.dj.digitalplatform.service.institutionService;
import com.dj.digitalplatform.mapper.institutionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author xuliwei
* @description 针对表【institution(机构信息表)】的数据库操作Service实现
* @createDate 2025-08-30 00:52:34
*/
@Service
@Slf4j
public class institutionServiceImpl extends ServiceImpl<institutionMapper, Institution>
    implements institutionService{

    @Override
    public InstitutionVO getInstitutionVO(Institution institution) {
        if (institution == null) {
            return null;
        }
        InstitutionVO institutionVO = new InstitutionVO();
        BeanUtils.copyProperties(institution, institutionVO);
        return institutionVO;
    }

    @Override
    public List<InstitutionVO> getInstitutionVOList(List<Institution> institutionList) {
        if (CollUtil.isEmpty(institutionList)) {
            return CollUtil.newArrayList();
        }
        return institutionList.stream().map(this::getInstitutionVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<Institution> getQueryWrapper(InstitutionQueryRequest institutionQueryRequest) {
        QueryWrapper<Institution> queryWrapper = new QueryWrapper<>();
        if (institutionQueryRequest == null) {
            log.info("InstitutionQueryRequest is null, returning empty query wrapper");
            return queryWrapper;
        }

        Long id = institutionQueryRequest.getId();
        String institutionName = institutionQueryRequest.getInstitutionName();
        String institutionNameEnglish = institutionQueryRequest.getInstitutionNameEnglish();
        String institutionType = institutionQueryRequest.getInstitutionType();
        String countryRegion = institutionQueryRequest.getCountryRegion();
        String city = institutionQueryRequest.getCity();

        log.info("Query parameters: id={}, institutionName={}, institutionType={}, countryRegion={}, city={}", 
                id, institutionName, institutionType, countryRegion, city);

        // 根据条件构建查询
        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.like(StrUtil.isNotBlank(institutionName), "institutionName", institutionName);
        queryWrapper.like(StrUtil.isNotBlank(institutionNameEnglish), "institutionNameEnglish", institutionNameEnglish);
        queryWrapper.eq(StrUtil.isNotBlank(institutionType), "institutionType", institutionType);
        queryWrapper.eq(StrUtil.isNotBlank(countryRegion), "countryRegion", countryRegion);
        queryWrapper.eq(StrUtil.isNotBlank(city), "city", city);
        
        // MyBatis Plus会自动处理逻辑删除，不需要手动添加isDelete条件
        
        // 默认按创建时间降序排序
        queryWrapper.orderByDesc("createTime");
        
        log.info("Generated SQL: {}", queryWrapper.getTargetSql());
        return queryWrapper;
    }
}




