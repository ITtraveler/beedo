package com.seagull.beedo.dao;

import com.seagull.beedo.common.query.DocumentQuery;
import com.seagull.beedo.dao.domain.BeedoDocument;

import java.util.List;

public interface BeedoDocumentDao {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_document
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_document
     *
     * @mbg.generated
     */
    int insert(BeedoDocument record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_document
     *
     * @mbg.generated
     */
    int insertSelective(BeedoDocument record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_document
     *
     * @mbg.generated
     */
    BeedoDocument selectByPrimaryKey(Integer id);

    List<BeedoDocument> selectByQuery(DocumentQuery query);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_document
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(BeedoDocument record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table beedo_document
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(BeedoDocument record);
}