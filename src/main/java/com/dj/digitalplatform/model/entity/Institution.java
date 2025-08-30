package com.dj.digitalplatform.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 机构信息表
 * @TableName institution
 */
@TableName(value ="institution")
@Data
public class Institution {
    /**
     * 机构ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 机构名称
     */
    @TableField(value = "institutionName")
    private String institutionName;

    /**
     * 机构英文名称
     */
    @TableField(value = "institutionNameEnglish")
    private String institutionNameEnglish;

    /**
     * 机构类型 (例如：大学，研究机构)
     */
    @TableField(value = "institutionType")
    private String institutionType;

    /**
     * 国家/地区 (例如：中国，美国)
     */
    @TableField(value = "countryRegion")
    private String countryRegion;

    /**
     * 城市 (例如：北京，上海)
     */
    @TableField(value = "city")
    private String city;

    /**
     * 地理坐标 (例如：经纬度)
     */
    @TableField(value = "geographicalCoordinates")
    private String geographicalCoordinates;

    /**
     * 机构网站
     */
    @TableField(value = "institutionWebsite")
    private String institutionWebsite;

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
        Institution other = (Institution) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getInstitutionName() == null ? other.getInstitutionName() == null : this.getInstitutionName().equals(other.getInstitutionName()))
            && (this.getInstitutionNameEnglish() == null ? other.getInstitutionNameEnglish() == null : this.getInstitutionNameEnglish().equals(other.getInstitutionNameEnglish()))
            && (this.getInstitutionType() == null ? other.getInstitutionType() == null : this.getInstitutionType().equals(other.getInstitutionType()))
            && (this.getCountryRegion() == null ? other.getCountryRegion() == null : this.getCountryRegion().equals(other.getCountryRegion()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getGeographicalCoordinates() == null ? other.getGeographicalCoordinates() == null : this.getGeographicalCoordinates().equals(other.getGeographicalCoordinates()))
            && (this.getInstitutionWebsite() == null ? other.getInstitutionWebsite() == null : this.getInstitutionWebsite().equals(other.getInstitutionWebsite()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getInstitutionName() == null) ? 0 : getInstitutionName().hashCode());
        result = prime * result + ((getInstitutionNameEnglish() == null) ? 0 : getInstitutionNameEnglish().hashCode());
        result = prime * result + ((getInstitutionType() == null) ? 0 : getInstitutionType().hashCode());
        result = prime * result + ((getCountryRegion() == null) ? 0 : getCountryRegion().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getGeographicalCoordinates() == null) ? 0 : getGeographicalCoordinates().hashCode());
        result = prime * result + ((getInstitutionWebsite() == null) ? 0 : getInstitutionWebsite().hashCode());
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
        sb.append(", institutionName=").append(institutionName);
        sb.append(", institutionNameEnglish=").append(institutionNameEnglish);
        sb.append(", institutionType=").append(institutionType);
        sb.append(", countryRegion=").append(countryRegion);
        sb.append(", city=").append(city);
        sb.append(", geographicalCoordinates=").append(geographicalCoordinates);
        sb.append(", institutionWebsite=").append(institutionWebsite);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append("]");
        return sb.toString();
    }
}