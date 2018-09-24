/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.dao.dataobject;

import com.seagull.beedo.model.TaskElementInfo;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * 要执行的Document/Element集合
 *
 * @author guosheng.huang
 * @version $id:TaskNodeDO.java, v 0.1 2018年08月11日 20:51 guosheng.huang Exp $
 */
@Entity
@Table(name = "beedo_task_node")
@EntityListeners(AuditingEntityListener.class)
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
     * k:ElementParseDO id    v:任务元素信息
     */
    @Column(length = 512)
    private String elementInfoMap;

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
