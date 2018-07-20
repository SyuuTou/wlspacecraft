package com.wl.spacecraft.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user_game")
public class UserGame {
    /**
     * 记录id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 电话号码
     */
    private String phonenum;

    /**
     * 用户token
     */
    private String token;

    /**
     * 此轮游戏的id
     */
    @Column(name = "game_id")
    private Integer gameId;

    /**
     * 此轮游戏消耗的og币
     */
    @Column(name = "og_consume")
    private Integer ogConsume;

    /**
     * 此轮游戏创建时间
     */
    @Column(name = "begin_time")
    private Date beginTime;

    /**
     * 此轮游戏获得的og币
     */
    @Column(name = "og_score")
    private Integer ogScore;

    /**
     * 游戏结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

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
     * 获取用户token
     *
     * @return token - 用户token
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置用户token
     *
     * @param token 用户token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取此轮游戏的id
     *
     * @return game_id - 此轮游戏的id
     */
    public Integer getGameId() {
        return gameId;
    }

    /**
     * 设置此轮游戏的id
     *
     * @param gameId 此轮游戏的id
     */
    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    /**
     * 获取此轮游戏消耗的og币
     *
     * @return og_consume - 此轮游戏消耗的og币
     */
    public Integer getOgConsume() {
        return ogConsume;
    }

    /**
     * 设置此轮游戏消耗的og币
     *
     * @param ogConsume 此轮游戏消耗的og币
     */
    public void setOgConsume(Integer ogConsume) {
        this.ogConsume = ogConsume;
    }

    /**
     * 获取此轮游戏创建时间
     *
     * @return begin_time - 此轮游戏创建时间
     */
    public Date getBeginTime() {
        return beginTime;
    }

    /**
     * 设置此轮游戏创建时间
     *
     * @param beginTime 此轮游戏创建时间
     */
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    /**
     * 获取此轮游戏获得的og币
     *
     * @return og_score - 此轮游戏获得的og币
     */
    public Integer getOgScore() {
        return ogScore;
    }

    /**
     * 设置此轮游戏获得的og币
     *
     * @param ogScore 此轮游戏获得的og币
     */
    public void setOgScore(Integer ogScore) {
        this.ogScore = ogScore;
    }

    /**
     * 获取游戏结束时间
     *
     * @return end_time - 游戏结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置游戏结束时间
     *
     * @param endTime 游戏结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}