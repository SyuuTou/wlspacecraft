package com.wl.spacecraft.dto.responsedto;


import com.wl.spacecraft.dto.commondto.GameConfigCommonOutputDto;
import com.wl.spacecraft.dto.commondto.UserInfoCommonOutputDto;
import com.wl.spacecraft.model.Community;

import java.util.List;

public class LoginOutputDto {

    /**
     * 登录结果
     */
    private boolean result;
    /**
     * 登录提示
     */
    private String note;
    /**
     * 用户状态
     * register/login
     */
    private String userStatus;
    /**
     * 社区
     */
    private List<Community> communities;

    /**
     * 用户数据
     */
    private UserInfoCommonOutputDto userData;


    /**
     * 游戏数据
     */
    private GameConfigCommonOutputDto gameData;

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public List<Community> getCommunities() {
        return communities;
    }

    public void setCommunities(List<Community> communities) {
        this.communities = communities;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isResult() {
        return result;
    }

    public UserInfoCommonOutputDto getUserData() {
        return userData;
    }

    public GameConfigCommonOutputDto getGameData() {
        return gameData;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setUserData(UserInfoCommonOutputDto userData) {
        this.userData = userData;
    }

    public void setGameData(GameConfigCommonOutputDto gameData) {
        this.gameData = gameData;
    }

    @Override
    public String toString() {
        return "LoginOutputDto{" +
                "result=" + result +
                ", note='" + note + '\'' +
                ", userStatus='" + userStatus + '\'' +
                ", communities=" + communities +
                ", userData=" + userData +
                ", gameData=" + gameData +
                '}';
    }
}
