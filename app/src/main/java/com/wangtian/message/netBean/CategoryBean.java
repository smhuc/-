package com.wangtian.message.netBean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Archy Wang
 * @Date 2019/3/30
 * @Description
 */

public class CategoryBean implements Serializable{

    /**
     * pageNum : 1
     * pageSize : 10
     * total : 0
     * list : []
     * rows : []
     * where :
     * params : []
     * id : 2c929ebd6720694c01672085bfaf0013
     * pid : 2c929ebd6720694c016720856efa0012
     * bq : ]
     * cj : 2
     * mgdj : null
     * zt : 启用
     * cjsj : 2018-11-17T07:14:55.000+0000
     * subList : null
     * yhBq : false
     */

    private int pageNum;
    private int pageSize;
    private int total;
    private String where;
    private String id;
    private String pid;
    private String bq;
    private String cj;
    private Object mgdj;
    private String zt;
    private String cjsj;
    private Object subList;
    private boolean yhBq;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBq() {
        return bq;
    }

    public void setBq(String bq) {
        this.bq = bq;
    }

    public String getCj() {
        return cj;
    }

    public void setCj(String cj) {
        this.cj = cj;
    }

    public Object getMgdj() {
        return mgdj;
    }

    public void setMgdj(Object mgdj) {
        this.mgdj = mgdj;
    }

    public String getZt() {
        return zt;
    }

    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getCjsj() {
        return cjsj;
    }

    public void setCjsj(String cjsj) {
        this.cjsj = cjsj;
    }

    public Object getSubList() {
        return subList;
    }

    public void setSubList(Object subList) {
        this.subList = subList;
    }

    public boolean isYhBq() {
        return yhBq;
    }

    public void setYhBq(boolean yhBq) {
        this.yhBq = yhBq;
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
