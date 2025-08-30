package com.dj.digitalplatform.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 专业信息表
 * @TableName program
 */
@TableName(value ="program")
@Data
public class Program {
    /**
     * 专业ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 专业名称
     */
    @TableField(value = "programName")
    private String programName;

    /**
     * 专业英文名称
     */
    @TableField(value = "programNameEnglish")
    private String programNameEnglish;

    /**
     * 学位类型 (例如：本科、硕士、博士)
     */
    @TableField(value = "degreeType")
    private String degreeType;

    /**
     * 学制 (例如：4年、2年)
     */
    @TableField(value = "programDuration")
    private String programDuration;

    /**
     * 授课语言 (例如：中文、英文)
     */
    @TableField(value = "languageOfInstruction")
    private String languageOfInstruction;

    /**
     * 课程模式 (例如：全日制、兼职、在线、混合)
     */
    @TableField(value = "deliveryMode")
    private String deliveryMode;

    /**
     * 专业概述
     */
    @TableField(value = "programOverview")
    private String programOverview;

    /**
     * 核心课程
     */
    @TableField(value = "coreCourses")
    private String coreCourses;

    /**
     * 研究方向
     */
    @TableField(value = "researchFocus")
    private String researchFocus;

    /**
     * 专业亮点
     */
    @TableField(value = "programHighlights")
    private String programHighlights;

    /**
     * 师资力量
     */
    @TableField(value = "facultyInformation")
    private String facultyInformation;

    /**
     * 项目案例
     */
    @TableField(value = "projectExamples")
    private String projectExamples;

    /**
     * 就业方向
     */
    @TableField(value = "careerProspects")
    private String careerProspects;

    /**
     * 合作机构
     */
    @TableField(value = "partnerInstitutions")
    private String partnerInstitutions;

    /**
     * 认证情况
     */
    @TableField(value = "accreditationStatus")
    private String accreditationStatus;

    /**
     * 所属机构ID
     */
    @TableField(value = "institution_id")
    private Long institutionId;

    /**
     * 创建时间
     */
    @TableField(value = "createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime")
    private Date updateTime;

    /**
     * 是否删除 (0: 否, 1: 是)
     */
    @TableLogic
    @TableField(value = "isDelete")
    private Integer isDelete;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Program other = (Program) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProgramName() == null ? other.getProgramName() == null : this.getProgramName().equals(other.getProgramName()))
            && (this.getProgramNameEnglish() == null ? other.getProgramNameEnglish() == null : this.getProgramNameEnglish().equals(other.getProgramNameEnglish()))
            && (this.getDegreeType() == null ? other.getDegreeType() == null : this.getDegreeType().equals(other.getDegreeType()))
            && (this.getProgramDuration() == null ? other.getProgramDuration() == null : this.getProgramDuration().equals(other.getProgramDuration()))
            && (this.getLanguageOfInstruction() == null ? other.getLanguageOfInstruction() == null : this.getLanguageOfInstruction().equals(other.getLanguageOfInstruction()))
            && (this.getDeliveryMode() == null ? other.getDeliveryMode() == null : this.getDeliveryMode().equals(other.getDeliveryMode()))
            && (this.getProgramOverview() == null ? other.getProgramOverview() == null : this.getProgramOverview().equals(other.getProgramOverview()))
            && (this.getCoreCourses() == null ? other.getCoreCourses() == null : this.getCoreCourses().equals(other.getCoreCourses()))
            && (this.getResearchFocus() == null ? other.getResearchFocus() == null : this.getResearchFocus().equals(other.getResearchFocus()))
            && (this.getProgramHighlights() == null ? other.getProgramHighlights() == null : this.getProgramHighlights().equals(other.getProgramHighlights()))
            && (this.getFacultyInformation() == null ? other.getFacultyInformation() == null : this.getFacultyInformation().equals(other.getFacultyInformation()))
            && (this.getProjectExamples() == null ? other.getProjectExamples() == null : this.getProjectExamples().equals(other.getProjectExamples()))
            && (this.getCareerProspects() == null ? other.getCareerProspects() == null : this.getCareerProspects().equals(other.getCareerProspects()))
            && (this.getPartnerInstitutions() == null ? other.getPartnerInstitutions() == null : this.getPartnerInstitutions().equals(other.getPartnerInstitutions()))
            && (this.getAccreditationStatus() == null ? other.getAccreditationStatus() == null : this.getAccreditationStatus().equals(other.getAccreditationStatus()))
            && (this.getInstitutionId() == null ? other.getInstitutionId() == null : this.getInstitutionId().equals(other.getInstitutionId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProgramName() == null) ? 0 : getProgramName().hashCode());
        result = prime * result + ((getProgramNameEnglish() == null) ? 0 : getProgramNameEnglish().hashCode());
        result = prime * result + ((getDegreeType() == null) ? 0 : getDegreeType().hashCode());
        result = prime * result + ((getProgramDuration() == null) ? 0 : getProgramDuration().hashCode());
        result = prime * result + ((getLanguageOfInstruction() == null) ? 0 : getLanguageOfInstruction().hashCode());
        result = prime * result + ((getDeliveryMode() == null) ? 0 : getDeliveryMode().hashCode());
        result = prime * result + ((getProgramOverview() == null) ? 0 : getProgramOverview().hashCode());
        result = prime * result + ((getCoreCourses() == null) ? 0 : getCoreCourses().hashCode());
        result = prime * result + ((getResearchFocus() == null) ? 0 : getResearchFocus().hashCode());
        result = prime * result + ((getProgramHighlights() == null) ? 0 : getProgramHighlights().hashCode());
        result = prime * result + ((getFacultyInformation() == null) ? 0 : getFacultyInformation().hashCode());
        result = prime * result + ((getProjectExamples() == null) ? 0 : getProjectExamples().hashCode());
        result = prime * result + ((getCareerProspects() == null) ? 0 : getCareerProspects().hashCode());
        result = prime * result + ((getPartnerInstitutions() == null) ? 0 : getPartnerInstitutions().hashCode());
        result = prime * result + ((getAccreditationStatus() == null) ? 0 : getAccreditationStatus().hashCode());
        result = prime * result + ((getInstitutionId() == null) ? 0 : getInstitutionId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", programName=").append(programName);
        sb.append(", programNameEnglish=").append(programNameEnglish);
        sb.append(", degreeType=").append(degreeType);
        sb.append(", programDuration=").append(programDuration);
        sb.append(", languageOfInstruction=").append(languageOfInstruction);
        sb.append(", deliveryMode=").append(deliveryMode);
        sb.append(", programOverview=").append(programOverview);
        sb.append(", coreCourses=").append(coreCourses);
        sb.append(", researchFocus=").append(researchFocus);
        sb.append(", programHighlights=").append(programHighlights);
        sb.append(", facultyInformation=").append(facultyInformation);
        sb.append(", projectExamples=").append(projectExamples);
        sb.append(", careerProspects=").append(careerProspects);
        sb.append(", partnerInstitutions=").append(partnerInstitutions);
        sb.append(", accreditationStatus=").append(accreditationStatus);
        sb.append(", institutionId=").append(institutionId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append("]");
        return sb.toString();
    }
}