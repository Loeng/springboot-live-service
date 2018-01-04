//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sgc.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通用实体（通用字段）
 * Created by Bruin
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class BaseModel<T extends BaseModel<?>> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;

    private String createUserId;

    private Date createTime;

    private String updateUserId;

    private Date updateTime;

    protected Serializable pkVal() {
        // TODO Auto-generated method stub
        return id;
    }
}
