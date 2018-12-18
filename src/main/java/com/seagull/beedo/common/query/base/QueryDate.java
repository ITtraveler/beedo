package com.seagull.beedo.common.query.base;

import java.util.Date;

/**
 * @author guosheng.huang
 * @version $Id: QueryDate, v1.0 2018年11月05日 15:40 guosheng.huang Exp $
 */

public class QueryDate extends QueryBase {
    private static final long serialVersionUID = 5864775123156261810L;
    /** 创建起始时间 */
    private Date              startDate;

    /** 创建终止时间 */
    private Date              endDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
