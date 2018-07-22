package com.wl.spacecraft.dto.responsedto;

public class GameStartOutputDto {
    /**
     * 金币扣除标志位
     */
    private boolean result;
    /**
     * 用户手机
     */
    private String phone;
    /**
     * 用户og总数
     */
    private Integer amount;
    /**
     * 该轮游戏id
     */
    private String GameId;

    public boolean isResult() {
        return result;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getGameId() {
        return GameId;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setGameId(String gameId) {
        GameId = gameId;
    }

    @Override
    public String toString() {
        return "GameOverOutputDto{" +
                "result=" + result +
                ", phone='" + phone + '\'' +
                ", amount=" + amount +
                ", GameId='" + GameId + '\'' +
                '}';
    }
}

