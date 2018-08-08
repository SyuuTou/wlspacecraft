package com.wl.spacecraft.service.pay;

import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.responsedto.RechargeInfoOutputDto;
import com.wl.spacecraft.dto.responsedto.TransactionInfoOutputDto;

import java.util.List;

public interface PayService {

    /**
     * 获取
     * @return
     */
    CommonDto<RechargeInfoOutputDto> getRechargeInfo();

    /**
     * 交易信息输出
     * @return 交易信息输出
     */
    CommonDto<List<TransactionInfoOutputDto>> echoTransactionInfo();
}
