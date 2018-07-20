package com.lhjl.tzzs.proxy.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "user_money_record")
public class UserMoneyRecord {
    /**
     * 记录id
     */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户输入金额
     */
    private BigDecimal money;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "scene_key")
    private String sceneKey;

    @Column(name = "app_id")
    private Integer appId;


    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    /**
     * 获取记录id
     *
     * @return ID - 记录id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置记录id
     *
     * @param id 记录id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户输入金额
     *
     * @return money - 用户输入金额
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 设置用户输入金额
     *
     * @param money 用户输入金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
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
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return scene_key
     */
    public String getSceneKey() {
        return sceneKey;
    }

    /**
     * @param sceneKey
     */
    public void setSceneKey(String sceneKey) {
        this.sceneKey = sceneKey;
    }
}