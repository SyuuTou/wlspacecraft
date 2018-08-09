package com.wl.spacecraft.dto.responsedto;

import com.wl.spacecraft.model.ConfigOgPrice;
import com.wl.spacecraft.model.ConfigWechat;

import java.math.BigDecimal;
import java.util.List;

/**
 * 充值信息输出dto
 */
public class RechargeInfoOutputDto {
    /**
     * 最小充值金额
     */
    private BigDecimal minRechargeAmount;

    /**
     * 客服人员
     */
    List<ConfigWechat> wechats;

    /**
     * Eth同OG兑换比例
     */
    private ConfigOgPrice configOgPrice;

    public List<ConfigWechat> getWechats() {
        return wechats;
    }

    public void setWechats(List<ConfigWechat> wechats) {
        this.wechats = wechats;
    }

    public BigDecimal getMinRechargeAmount() {
        return minRechargeAmount;
    }

    public void setMinRechargeAmount(BigDecimal minRechargeAmount) {
        this.minRechargeAmount = minRechargeAmount;
    }


    public ConfigOgPrice getConfigOgPrice() {
        return configOgPrice;
    }

    public void setConfigOgPrice(ConfigOgPrice configOgPrice) {
        this.configOgPrice = configOgPrice;
    }

    @Override
    public String toString() {
        return "RechargeInfoOutputDto{" +
                "minRechargeAmount=" + minRechargeAmount +
                ", wechats=" + wechats +
                ", configOgPrice=" + configOgPrice +
                '}';
    }
}
