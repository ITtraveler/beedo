package com.seagull.beedo.dao;

import com.seagull.beedo.common.query.TaskNodeQuery;
import com.seagull.beedo.common.query.TaskParseQuery;
import com.seagull.beedo.dao.domain.BeedoTaskNode;
import com.seagull.beedo.dao.domain.BeedoTaskParse;

import java.util.List;

public interface BeedoTaskParseDao {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_task_parse
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_task_parse
     *
     * @mbg.generated
     */
    int insert(BeedoTaskParse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_task_parse
     *
     * @mbg.generated
     */
    int insertSelective(BeedoTaskParse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_task_parse
     *
     * @mbg.generated
     */
    BeedoTaskParse selectByPrimaryKey(Integer id);

    BeedoTaskParse selectByUid(String id);

    List<BeedoTaskParse> selectByQuery(TaskParseQuery query);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_task_parse
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(BeedoTaskParse record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_task_parse
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(BeedoTaskParse record);
}