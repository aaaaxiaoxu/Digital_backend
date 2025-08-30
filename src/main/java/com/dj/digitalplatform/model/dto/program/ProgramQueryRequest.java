package com.dj.digitalplatform.model.dto.program;

import com.dj.digitalplatform.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 专业查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProgramQueryRequest extends PageRequest implements Serializable {

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
     * 授课语言 (例如：中文、英文)
     */
    private String languageOfInstruction;

    /**
     * 课程模式 (例如：全日制、兼职、在线、混合)
     */
    private String deliveryMode;

    /**
     * 所属机构ID
     */
    private Long institutionId;

    private static final long serialVersionUID = 1L;
}
