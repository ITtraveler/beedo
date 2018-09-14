/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.dao.dataobject;

import com.seagull.beedo.common.enums.TaskStatusEnum;
import lombok.Data;
import lombok.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;


/**
 * 任务解析的信息
 * @author guosheng.huang
 * @version $id:TaskParseDO.java, v 0.1 2018年08月11日 20:45 tao.hu Exp $
 */
@Entity
@Table(name = "beedo_task_parse")
@EntityListeners(AuditingEntityListener.class)
@Data
public class TaskParseDO {
    /**
     * 任务uid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * 任务uid
     */
    @Column(length = 32)
    private String uid;

    /**
     * 任务名
     */
    @Column(length = 32)
    private String name;

    /**
     * 任务执行的cron表达式
     */
    @Column(length = 32)
    private String cron;


    /**
     * 线程池大小
     */
    @Column(length = 4)
    private Integer threadCoolSize;

    /**
     * 该解析信息的任务状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private TaskStatusEnum taskStatus;

    /**
     * 数据库名
     */
    @Column(length = 32)
    private String dbname;

    /**
     * memo
     */
    @Column(length = 512)
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
