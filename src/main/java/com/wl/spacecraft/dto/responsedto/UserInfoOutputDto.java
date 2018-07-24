package com.wl.spacecraft.dto.responsedto;

import com.wl.spacecraft.dto.commondto.GameConfigCommonOutputDto;
import com.wl.spacecraft.dto.commondto.UserInfoCommonOutputDto;

public class UserInfoOutputDto {
    /**
     * 用户数据
     */
    private UserInfoCommonOutputDto userData;
    /**
     * 游戏数据
     */
    private GameConfigCommonOutputDto gameData;

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
                '}';
    }
}
