/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.dao.dataobject;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.Map;

/**
 * 要执行的Document/Element集合
 *
 * @author guosheng.huang
 * @version $id:TaskNodeDO.java, v 0.1 2018年08月11日 20:51 tao.hu Exp $
 */
@Entity
@Table(name = "beedo_task_node")
@Data
public class TaskNodeDO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * TaskParseDO id
     */
    @Column(length = 32)
    private String taskParseUid;

    /**
     * DocumentParseDO id
     */
    @Column(length = 11)
    private Integer documentId;

    /**
     * k:ElementParseDO id    v:保存在数据库中的属性名   json数据
     */
    @Column(length = 512)
    private String elementMap;

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
