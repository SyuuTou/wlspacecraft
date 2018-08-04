package com.wl.spacecraft.dto.responsedto;

import com.wl.spacecraft.dto.commondto.GameConfigCommonOutputDto;
import com.wl.spacecraft.dto.commondto.UserInfoCommonOutputDto;
import com.wl.spacecraft.model.Community;

import java.util.List;

public class UserInfoOutputDto {
    /**
     * 用户数据
     */
    private UserInfoCommonOutputDto userData;
    /**
     * 游戏数据
     */
    private GameConfigCommonOutputDto gameData;

    /**
     * 社群列表
     */
    private List<Community> communities;

    public List<Community> getCommunities() {
        return communities;
    }

    public void setCommunities(List<Community> communities) {
        this.communities = communities;
    }

    public GameConfigCommonOutputDto getGameData() {
        return gameData;
    }

    public void setGameData(GameConfigCommonOutputDto gameData) {
        this.gameData = gameData;
    }

    public UserInfoCommonOutputDto getUserData() {

        return userData;
    }

    public void setUserData(UserInfoCommonOutputDto userData) {
        this.userData = userData;
    }

    @Override
    public String toString() {
        return "UserInfoOutputDto{" +
                "userData=" + userData +
                ", gameData=" + gameData +
                ", communities=" + communities +
                '}';
    }
}
