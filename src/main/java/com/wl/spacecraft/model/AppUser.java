package com.wl.spacecraft.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "app_user")
public class AppUser {
    /**
     * 用户id
     */
    @Id
    private Integer userid;

    /**
     * 社区id
     */
    @Column(name = "community_id")
    private Integer communityId;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 电话号码
     */
    private String phonenum;

    /**
     * 积分总数
     */
    private Integer amount;

    private String token;
    /**
     * 真实用户标志
     */
    @Column(name="is_real")
    private Integer isReal;

    public Integer getIsReal() {
        return isReal;
    }

    public void setIsReal(Integer isReal) {
        this.isReal = isReal;
    }

    /**
     * 上一次登录时间
     */
    @Column(name = "last_login_time")
    private Date lastLoginTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建人
     */
    private Integer creator;

    /**
     * 更新人
     */
    private Integer updator;

    /**
     * 获取用户id
     *
     * @return userid - 用户id
     */
    public Integer getUserid() {
        return userid;
    }

    /**
     * 设置用户id
     *
     * @param userid 用户id
     */
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    /**
     * 获取昵称
     *
     * @return nick_name - 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置昵称
     *
     * @param nickName 昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 获取电话号码
     *
     * @return phonenum - 电话号码
     */
    public String getPhonenum() {
        return phonenum;
    }

    /**
     * 设置电话号码
     *
     * @param phonenum 电话号码
     */
    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    /**
     * 获取积分总数
     *
     * @return amount - 积分总数
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * 设置积分总数
     *
     * @param amount 积分总数
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取上一次登录时间
     *
     * @return last_login_time - 上一次登录时间
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 设置上一次登录时间
     *
     * @param lastLoginTime 上一次登录时间
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public Integer getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * 获取更新人
     *
     * @return updator - 更新人
     */
    public Integer getUpdator() {
        return updator;
    }

    /**
     * 设置更新人
     *
     * @param updator 更新人
     */
    public void setUpdator(Integer updator) {
        this.updator = updator;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "userid=" + userid +
                ", communityId=" + communityId +
                ", nickName='" + nickName + '\'' +
                ", phonenum='" + phonenum + '\'' +
                ", amount=" + amount +
                ", token='" + token + '\'' +
                ", isReal=" + isReal +
                ", lastLoginTime=" + lastLoginTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", creator=" + creator +
                ", updator=" + updator +
                '}';
    }
}