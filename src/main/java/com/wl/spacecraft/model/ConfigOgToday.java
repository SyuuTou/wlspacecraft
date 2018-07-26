package com.wl.spacecraft.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "config_og_today")
public class ConfigOgToday {
    /**
     * 记录id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 今日领取OG上限
     */
    @Column(name = "og_today")
    private Integer ogToday;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 运营人员id
     */
    private Integer creator;

    /**
     * 游戏类型(1表示飞船游戏，2...)
     */
    private Integer type;

    /**
     * 获取记录id
     *
     * @return id - 记录id
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
     * 获取今日领取OG上限
     *
     * @return og_today - 今日领取OG上限
     */
    public Integer getOgToday() {
        return ogToday;
    }

    /**
     * 设置今日领取OG上限
     *
     * @param ogToday 今日领取OG上限
     */
    public void setOgToday(Integer ogToday) {
        this.ogToday = ogToday;
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
     * 获取运营人员id
     *
     * @return creator - 运营人员id
     */
    public Integer getCreator() {
        return creator;
    }

    /**
     * 设置运营人员id
     *
     * @param creator 运营人员id
     */
    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * 获取游戏类型(1表示飞船游戏，2...)
     *
     * @return type - 游戏类型(1表示飞船游戏，2...)
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置游戏类型(1表示飞船游戏，2...)
     *
     * @param type 游戏类型(1表示飞船游戏，2...)
     */
    public void setType(Integer type) {
        this.type = type;
    }
}