package com.seagull.beedo.dao;

import java.util.List;

import com.seagull.beedo.common.query.ElementQuery;
import com.seagull.beedo.dao.domain.BeedoDocument;
import com.seagull.beedo.dao.domain.BeedoElement;

public interface BeedoElementDao {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_element
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_element
     *
     * @mbg.generated
     */
    int insert(BeedoElement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_element
     *
     * @mbg.generated
     */
    int insertSelective(BeedoElement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_element
     *
     * @mbg.generated
     */
    BeedoElement selectByPrimaryKey(Integer id);

    List<BeedoElement> selectByQuery(ElementQuery query);

    List<BeedoElement> selectByDocumentId(Integer documentId);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_element
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(BeedoElement record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_element
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(BeedoElement record);
}