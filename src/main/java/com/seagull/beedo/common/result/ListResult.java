package com.seagull.beedo.common.result;

import java.util.List;

/**
 * Created by hgs on 2017/7/5.
 */
public class ListResult<T> extends BaseResult {
    private static final long serialVersionUID = -6037489117648491280L;
    private int size;
    private List<T> dataList;

    public ListResult() {

    }

    public ListResult(int status, String message) {
        super(status, message);
    }

    public ListResult(int status, String message, List<T> dataList) {
        super(status, message);
        this.dataList = dataList;
        size = dataList.size();
    }

    public int getSize() {
        return size;
    }


    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
        size = dataList.size();
    }
}
