package com.wl.spacecraft.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "config_dropog_amount")
public class ConfigDropogAmount {
    /**
     * 记录id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 当前已空投OG数量
     */
    @Column(name = "current_drop_og")
    private Integer currentDropOg;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建者
     */
    private Integer creator;

    /**
     * 游戏类型(兼容字段,1表示飞船游戏，2...)
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
     * 获取当前已空投OG数量
     *
     * @return current_drop_og - 当前已空投OG数量
     */
    public Integer getCurrentDropOg() {
        return currentDropOg;
    }

    /**
     * 设置当前已空投OG数量
     *
     * @param currentDropOg 当前已空投OG数量
     */
    public void setCurrentDropOg(Integer currentDropOg) {
        this.currentDropOg = currentDropOg;
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
     * 获取创建者
     *
     * @return creator - 创建者
     */
    public Integer getCreator() {
        return creator;
    }

    /**
     * 设置创建者
     *
     * @param creator 创建者
     */
    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * 获取游戏类型(兼容字段,1表示飞船游戏，2...)
     *
     * @return type - 游戏类型(兼容字段,1表示飞船游戏，2...)
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置游戏类型(兼容字段,1表示飞船游戏，2...)
     *
     * @param type 游戏类型(兼容字段,1表示飞船游戏，2...)
     */
    public void setType(Integer type) {
        this.type = type;
    }
}