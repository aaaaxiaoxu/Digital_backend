package com.dj.digitalplatform.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 专业及其机构信息视图对象
 */
@Data
public class ProgramWithInstitutionVO implements Serializable {

    /**
     * 专业ID
     */
    private Long id;

    /**
     * 专业名称
     */
    private String programName;

    /**
     * 专业英文名称
     */
    private String programNameEnglish;

    /**
     * 学位类型 (例如：本科、硕士、博士)
     */
    private String degreeType;

    /**
     * 学制 (例如：4年、2年)
     */
    private String programDuration;

    /**
     * 授课语言 (例如：中文、英文)
     */
    private String languageOfInstruction;

    /**
     * 课程模式 (例如：全日制、兼职、在线、混合)
     */
    private String deliveryMode;

    /**
     * 专业概述
     */
    private String programOverview;

    /**
     * 核心课程
     */
    private String coreCourses;

    /**
     * 研究方向
     */
    private String researchFocus;

    /**
     * 专业亮点
     */
    private String programHighlights;

    /**
     * 师资力量
     */
    private String facultyInformation;

    /**
     * 项目案例
     */
    private String projectExamples;

    /**
     * 就业方向
     */
    private String careerProspects;

    /**
     * 合作机构
     */
    private String partnerInstitutions;

    /**
     * 认证情况
     */
    private String accreditationStatus;

    /**
     * 所属机构ID
     */
    private Long institutionId;

    /**
     * 专业创建时间
     */
    private Date createTime;

    /**
     * 专业更新时间
     */
    private Date updateTime;

    /**
     * 机构信息
     */
    private InstitutionVO institution;

    private static final long serialVersionUID = 1L;
}
