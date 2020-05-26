package com.wangtian.message.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Archy Wang
 * @Date 2019/4/1
 * @Description
 */

public class ShareNetBean implements Serializable {


    /**
     * pageNum : 1
     * pageSize : 10
     * total : 0
     * list : []
     * rows : []
     * where :
     * params : []
     * nm_Id : 8a929ec0699efd1d01699f150ddc0001
     * selectUsername : null
     * selectStartTime : null
     * selectEndTime : null
     * selectContentQuery : null
     * dt_CREATEDATE : 2019-03-21 15:09:11.0
     * vc_REMARKS : dsada
     * vc_TITLE : 今天好开心
     * vc_URL : 123213123
     * vc_USERID : cba178b1-a76f-41a7-8de2-a0c87b881b34
     * dt_SHAREDATE : 2019-03-19 00:00:00.0
     */
    private String name;
    private String userName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private int pageNum;
    private int pageSize;
    private int total;
    private String where;
    private String nm_Id;
    private Object selectUsername;
    private Object selectStartTime;
    private Object selectEndTime;
    private Object selectContentQuery;
    private String dt_CREATEDATE;
    private String vc_REMARKS;
    private String vc_TITLE;
    private String vc_URL;
    private String vc_USERID;
    private String dt_SHAREDATE;
    private List<?> list;
    private List<?> rows;
    private List<?> params;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getNm_Id() {
        return nm_Id;
    }

    public void setNm_Id(String nm_Id) {
        this.nm_Id = nm_Id;
    }

    public Object getSelectUsername() {
        return selectUsername;
    }

    public void setSelectUsername(Object selectUsername) {
        this.selectUsername = selectUsername;
    }

    public Object getSelectStartTime() {
        return selectStartTime;
    }

    public void setSelectStartTime(Object selectStartTime) {
        this.selectStartTime = selectStartTime;
    }

    public Object getSelectEndTime() {
        return selectEndTime;
    }

    public void setSelectEndTime(Object selectEndTime) {
        this.selectEndTime = selectEndTime;
    }

    public Object getSelectContentQuery() {
        return selectContentQuery;
    }

    public void setSelectContentQuery(Object selectContentQuery) {
        this.selectContentQuery = selectContentQuery;
    }

    public String getDt_CREATEDATE() {
        return dt_CREATEDATE;
    }

    public void setDt_CREATEDATE(String dt_CREATEDATE) {
        this.dt_CREATEDATE = dt_CREATEDATE;
    }

    public String getVc_REMARKS() {
        return vc_REMARKS;
    }

    public void setVc_REMARKS(String vc_REMARKS) {
        this.vc_REMARKS = vc_REMARKS;
    }

    public String getVc_TITLE() {
        return vc_TITLE;
    }

    public void setVc_TITLE(String vc_TITLE) {
        this.vc_TITLE = vc_TITLE;
    }

    public String getVc_URL() {
        return vc_URL;
    }

    public void setVc_URL(String vc_URL) {
        this.vc_URL = vc_URL;
    }

    public String getVc_USERID() {
        return vc_USERID;
    }

    public void setVc_USERID(String vc_USERID) {
        this.vc_USERID = vc_USERID;
    }

    public String getDt_SHAREDATE() {
        return dt_SHAREDATE;
    }

    public void setDt_SHAREDATE(String dt_SHAREDATE) {
        this.dt_SHAREDATE = dt_SHAREDATE;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }

    public List<?> getParams() {
        return params;
    }

    public void setParams(List<?> params) {
        this.params = params;
    }
}
