/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.component.mongo;

import java.util.List;

/**
 * 解析数据组件
 *
 * @author guosheng.huang
 * @version ParseDataComponent.java, v 0.1 2018年09月26日 22:28 guosheng.huang Exp $
 */

public interface ParseDataComponent {

    /**
     * 保存数据
     *
     * @param object
     * @param collectionName
     * @param indexFields
     */
    void saveData(Object object, String collectionName, List<String> indexFields);
}
