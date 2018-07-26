package com.wl.spacecraft.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "config_game_difficulty")
public class ConfigGameDifficulty {
    /**
     * 记录id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 难度系数
     */
    private Integer difficulty;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    private Integer creator;

    /**
     * 游戏类型（兼容字段1表示飞船游戏，2...）
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
     * 获取难度系数
     *
     * @return difficulty - 难度系数
     */
    public Integer getDifficulty() {
        return difficulty;
    }

    /**
     * 设置难度系数
     *
     * @param difficulty 难度系数
     */
    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
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
     * @return creator
     */
    public Integer getCreator() {
        return creator;
    }

    /**
     * @param creator
     */
    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * 获取游戏类型（兼容字段1表示飞船游戏，2...）
     *
     * @return type - 游戏类型（兼容字段1表示飞船游戏，2...）
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置游戏类型（兼容字段1表示飞船游戏，2...）
     *
     * @param type 游戏类型（兼容字段1表示飞船游戏，2...）
     */
    public void setType(Integer type) {
        this.type = type;
    }
}