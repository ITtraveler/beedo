/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.dao.dataobject;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
 * 一个页面为一个Document，一个Document下有多个Element
 * @author guosheng.huang
 * @version $id:DocumentParseDO.java, v 0.1 2018年08月11日 20:51 guosheng.huang Exp $
 */
@Entity
@Table(name = "beedo_document")
@EntityListeners(AuditingEntityListener.class)
@Data
public class DocumentParseDO {
    /**
     * 每个document都有一个uid,作为区分
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(length = 8)
    private String protocol;

    /**
     * 被解析的url
     */
    private String url;


    @Column(length = 32)
    private String name;

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
