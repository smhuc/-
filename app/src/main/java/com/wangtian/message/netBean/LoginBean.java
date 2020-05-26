package com.wangtian.message.netBean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Archy Wang
 * @Date 2019/3/19
 * @Description
 */

public class LoginBean implements Serializable{


    /**
     * user : {"pageNum":1,"pageSize":10,"total":0,"list":[],"rows":[],"where":"","params":[],"id":"cba178b1-a76f-41a7-8de2-a0c87b881b34","username":"jones","password":"e10adc3949ba59abbe56e057f20f883e","name":"王琼","gender":"男","idCard":"","attachManagerId":"manager1","mobilePhone":"","officePhone":"","isManage":null,"isAppManage":"02","status":null,"loginTime":"2017-08-04T11:38:49.000+0000","loginCount":1927,"createTime":null,"remark":null,"caCardId":"","code":null,"requestIP":"10.18.1.100","inputAuthCode":"null","organizationId":"7908914b-c14c-43e2-b148-e1b50b976fa0","roleName":null,"email":"","position":"","loginType":"混合登录","isVisible":"可见","isPermitOperate":"允许","loginDirection":"前台","loginErrorTime":"2019-03-19T06:25:46.000+0000","userType":"长期用户","macAddress":null,"operateTime":null,"loginErrorCount":0,"passwordEditTime":"2017-08-04T11:38:49.000+0000","portraitName":null,"userPosition":null,"systemFlag":"cdr,HNSJMT","userStatus":"启用","roleIds":null,"organizationName":null}
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6ImUxMGFkYzM5NDliYTU5YWJiZTU2ZTA1N2YyMGY4ODNlIiwiZXhwIjoxNTUzNTg0MDUxLCJ1c2VybmFtZSI6ImpvbmVzIn0.ElOT0nWuBv2Aex09tkf4ZwLbRYEz3fN_M5IGIv4Z0do
     */

    private UserBean user;
    private String token;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class UserBean {
        /**
         * pageNum : 1
         * pageSize : 10
         * total : 0
         * list : []
         * rows : []
         * where :
         * params : []
         * id : cba178b1-a76f-41a7-8de2-a0c87b881b34
         * username : jones
         * password : e10adc3949ba59abbe56e057f20f883e
         * name : 王琼
         * gender : 男
         * idCard :
         * attachManagerId : manager1
         * mobilePhone :
         * officePhone :
         * isManage : null
         * isAppManage : 02
         * status : null
         * loginTime : 2017-08-04T11:38:49.000+0000
         * loginCount : 1927
         * createTime : null
         * remark : null
         * caCardId :
         * code : null
         * requestIP : 10.18.1.100
         * inputAuthCode : null
         * organizationId : 7908914b-c14c-43e2-b148-e1b50b976fa0
         * roleName : null
         * email :
         * position :
         * loginType : 混合登录
         * isVisible : 可见
         * isPermitOperate : 允许
         * loginDirection : 前台
         * loginErrorTime : 2019-03-19T06:25:46.000+0000
         * userType : 长期用户
         * macAddress : null
         * operateTime : null
         * loginErrorCount : 0
         * passwordEditTime : 2017-08-04T11:38:49.000+0000
         * portraitName : null
         * userPosition : null
         * systemFlag : cdr,HNSJMT
         * userStatus : 启用
         * roleIds : null
         * organizationName : null
         */

        private int pageNum;
        private int pageSize;
        private int total;
        private String where;
        private String id;
        private String username;
        private String password;
        private String name;
        private String gender;
        private String idCard;
        private String attachManagerId;
        private String mobilePhone;
        private String officePhone;
        private Object isManage;
        //isAPPManage：是否APP管理员（01：管理员，02：普通用户）
        private String isAppManage;
        private Object status;
        private String loginTime;
        private int loginCount;
        private Object createTime;
        private String remark;
        private String caCardId;
        private Object code;
        private String requestIP;
        private String inputAuthCode;
        private String organizationId;
        private Object roleName;
        private String email;
        private String position;
        private String loginType;
        private String isVisible;
        private String isPermitOperate;
        private String loginDirection;
        private String loginErrorTime;
        private String userType;
        private Object macAddress;
        private Object operateTime;
        private int loginErrorCount;
        private String passwordEditTime;
        private Object portraitName;
        private Object userPosition;
        private String systemFlag;
        private String userStatus;
        private Object roleIds;
        private Object organizationName;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getAttachManagerId() {
            return attachManagerId;
        }

        public void setAttachManagerId(String attachManagerId) {
            this.attachManagerId = attachManagerId;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public String getOfficePhone() {
            return officePhone;
        }

        public void setOfficePhone(String officePhone) {
            this.officePhone = officePhone;
        }

        public Object getIsManage() {
            return isManage;
        }

        public void setIsManage(Object isManage) {
            this.isManage = isManage;
        }

        public String getIsAppManage() {
            return isAppManage;
        }

        public void setIsAppManage(String isAppManage) {
            this.isAppManage = isAppManage;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public String getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(String loginTime) {
            this.loginTime = loginTime;
        }

        public int getLoginCount() {
            return loginCount;
        }

        public void setLoginCount(int loginCount) {
            this.loginCount = loginCount;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCaCardId() {
            return caCardId;
        }

        public void setCaCardId(String caCardId) {
            this.caCardId = caCardId;
        }

        public Object getCode() {
            return code;
        }

        public void setCode(Object code) {
            this.code = code;
        }

        public String getRequestIP() {
            return requestIP;
        }

        public void setRequestIP(String requestIP) {
            this.requestIP = requestIP;
        }

        public String getInputAuthCode() {
            return inputAuthCode;
        }

        public void setInputAuthCode(String inputAuthCode) {
            this.inputAuthCode = inputAuthCode;
        }

        public String getOrganizationId() {
            return organizationId;
        }

        public void setOrganizationId(String organizationId) {
            this.organizationId = organizationId;
        }

        public Object getRoleName() {
            return roleName;
        }

        public void setRoleName(Object roleName) {
            this.roleName = roleName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getLoginType() {
            return loginType;
        }

        public void setLoginType(String loginType) {
            this.loginType = loginType;
        }

        public String getIsVisible() {
            return isVisible;
        }

        public void setIsVisible(String isVisible) {
            this.isVisible = isVisible;
        }

        public String getIsPermitOperate() {
            return isPermitOperate;
        }

        public void setIsPermitOperate(String isPermitOperate) {
            this.isPermitOperate = isPermitOperate;
        }

        public String getLoginDirection() {
            return loginDirection;
        }

        public void setLoginDirection(String loginDirection) {
            this.loginDirection = loginDirection;
        }

        public String getLoginErrorTime() {
            return loginErrorTime;
        }

        public void setLoginErrorTime(String loginErrorTime) {
            this.loginErrorTime = loginErrorTime;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public Object getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(Object macAddress) {
            this.macAddress = macAddress;
        }

        public Object getOperateTime() {
            return operateTime;
        }

        public void setOperateTime(Object operateTime) {
            this.operateTime = operateTime;
        }

        public int getLoginErrorCount() {
            return loginErrorCount;
        }

        public void setLoginErrorCount(int loginErrorCount) {
            this.loginErrorCount = loginErrorCount;
        }

        public String getPasswordEditTime() {
            return passwordEditTime;
        }

        public void setPasswordEditTime(String passwordEditTime) {
            this.passwordEditTime = passwordEditTime;
        }

        public Object getPortraitName() {
            return portraitName;
        }

        public void setPortraitName(Object portraitName) {
            this.portraitName = portraitName;
        }

        public Object getUserPosition() {
            return userPosition;
        }

        public void setUserPosition(Object userPosition) {
            this.userPosition = userPosition;
        }

        public String getSystemFlag() {
            return systemFlag;
        }

        public void setSystemFlag(String systemFlag) {
            this.systemFlag = systemFlag;
        }

        public String getUserStatus() {
            return userStatus;
        }

        public void setUserStatus(String userStatus) {
            this.userStatus = userStatus;
        }

        public Object getRoleIds() {
            return roleIds;
        }

        public void setRoleIds(Object roleIds) {
            this.roleIds = roleIds;
        }

        public Object getOrganizationName() {
            return organizationName;
        }

        public void setOrganizationName(Object organizationName) {
            this.organizationName = organizationName;
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
}
