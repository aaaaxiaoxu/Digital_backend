package com.dj.digitalplatform.model.dto.program;

import lombok.Data;

import java.io.Serializable;

/**
 * 专业创建请求
 */
@Data
public class ProgramAddRequest implements Serializable {

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

    private static final long serialVersionUID = 1L;
}
