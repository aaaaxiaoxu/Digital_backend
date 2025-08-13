package com.dj.digitalplatform.model.dto.email;

import lombok.Data;

@Data
public class EmailRequest {
    /**
     * Email
     */
    private String email;

    /**
     * verify code
     */
    private String code;
} 