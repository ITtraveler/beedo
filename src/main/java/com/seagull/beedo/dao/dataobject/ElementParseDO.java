/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.dao.dataobject;

import com.seagull.beedo.common.enums.ParseDataTypeEnum;
import com.seagull.beedo.common.enums.ParseStructureTypeEnum;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 一个Element对应一个document表达式
 * @author guosheng.huang
 * @version $id:ElementParseDO.java, v 0.1 2018年08月11日 20:51 guosheng.huang Exp $
 */
@Entity
@Table(name = "beedo_element")
@EntityListeners(AuditingEntityListener.class)
@Data
public class ElementParseDO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 11)
    private Integer documentId;

    /**
     * document表达式
     */
    private String cssQuery;

    /**
     * 数据类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private ParseDataTypeEnum dataType;

    /**
     * 结构类型
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private ParseStructureTypeEnum structureType;

    /**
     * 如果解析的是属性则有值
     */
    @Column(length = 32)
    private String attr;

    /**
     * 备注
     */
    private String memo;

    /**
     * 创建时间
     */
    @CreatedDate
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @LastModifiedDate
    private Date gmtModified;
}
