package com.common.utils.modle;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by on 2017/2/10.
 */
public class User implements Serializable{

    private Integer id;
    /**真实姓名*/
    private String realName;
    private int userId;
    /** 姓名 */
    private String userName;
    /** 1.l老师  2. 学生 */
    private Integer type;
    /** 学生帐号 */
    private String studentCode;
    /** 手机 */
    private String mobile;
    /** 邮箱 */
    private String email;
    /** 用户密码 */
    private String password;
    /** 学校id */
    private Integer schoolId;
    /** 学校名称 */
    private String schoolName;
    /** ( 老师 )  省/直辖市id */
    private Integer provinceId;
    /** ( 老师 )  省/直辖市name */
    private String provinceName;
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (userId != user.userId) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (realName != null ? !realName.equals(user.realName) : user.realName != null) return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null) return false;
        if (type != null ? !type.equals(user.type) : user.type != null) return false;
        if (studentCode != null ? !studentCode.equals(user.studentCode) : user.studentCode != null) return false;
        if (mobile != null ? !mobile.equals(user.mobile) : user.mobile != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (schoolId != null ? !schoolId.equals(user.schoolId) : user.schoolId != null) return false;
        if (schoolName != null ? !schoolName.equals(user.schoolName) : user.schoolName != null) return false;
        if (provinceId != null ? !provinceId.equals(user.provinceId) : user.provinceId != null) return false;
        if (provinceName != null ? !provinceName.equals(user.provinceName) : user.provinceName != null) return false;
        if (cityId != null ? !cityId.equals(user.cityId) : user.cityId != null) return false;
        if (cityName != null ? !cityName.equals(user.cityName) : user.cityName != null) return false;
        if (countyId != null ? !countyId.equals(user.countyId) : user.countyId != null) return false;
        if (countyName != null ? !countyName.equals(user.countyName) : user.countyName != null) return false;
        return createTime != null ? createTime.equals(user.createTime) : user.createTime == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (realName != null ? realName.hashCode() : 0);
        result = 31 * result + userId;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (studentCode != null ? studentCode.hashCode() : 0);
        result = 31 * result + (mobile != null ? mobile.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (schoolId != null ? schoolId.hashCode() : 0);
        result = 31 * result + (schoolName != null ? schoolName.hashCode() : 0);
        result = 31 * result + (provinceId != null ? provinceId.hashCode() : 0);
        result = 31 * result + (provinceName != null ? provinceName.hashCode() : 0);
        result = 31 * result + (cityId != null ? cityId.hashCode() : 0);
        result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
        result = 31 * result + (countyId != null ? countyId.hashCode() : 0);
        result = 31 * result + (countyName != null ? countyName.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }

    /** ( 老师 )  市/市辖区id */

    private Integer cityId;
    /** ( 老师 )  市/市辖区name */
    private String cityName;
    /** ( 老师 )  区县id */
    private Integer countyId;
    /** ( 老师 )  区县name */
    private String countyName;
    /** 创建时间 */
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getCountyId() {
        return countyId;
    }

    public void setCountyId(Integer countyId) {
        this.countyId = countyId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
