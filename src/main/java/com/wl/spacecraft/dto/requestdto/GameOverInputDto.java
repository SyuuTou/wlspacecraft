package com.wl.spacecraft.dto.requestdto;

import java.util.Date;

public class GameOverInputDto {
    /**
     * 用户手机
     */
    private String phone;
    /**
     * 用户token
     */
    private String token;
    /**
     * 过期时间
     */
    private Date expire;
    /**
     * token校验字符串
     */
    private String tokenValidateStr;

    /**
     *此轮游戏id
     */
    private String gameId;
    /**
     * 游戏应用的分类
     */
//    private String appKey;
    /**
     * 获取的游戏金币
     */
    private Integer score;


    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }

    public Date getExpire() {
        return expire;
    }



    public String getGameId() {
        return gameId;
    }

    public Integer getScore() {
        return score;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }



    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getTokenValidateStr() {
        return tokenValidateStr;
    }

    public void setTokenValidateStr(String tokenValidateStr) {
        this.tokenValidateStr = tokenValidateStr;
    }

    @Override
    public String toString() {
        return "GameOverInputDto{" +
                "phone='" + phone + '\'' +
                ", token='" + token + '\'' +
                ", expire=" + expire +
                ", tokenValidateStr='" + tokenValidateStr + '\'' +
                ", gameId='" + gameId + '\'' +
                ", score=" + score +
                '}';
    }
}
