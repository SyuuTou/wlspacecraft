package com.wl.spacecraft.controller;

import com.wl.spacecraft.controller.common.GenericController;
import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.responsedto.RechargeInfoOutputDto;
import com.wl.spacecraft.dto.responsedto.TransactionInfoOutputDto;
import com.wl.spacecraft.exception.project.ProjectException;
import com.wl.spacecraft.service.pay.PayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 支付、充值、提币
 */
@RestController
public class PayController extends GenericController {

    @Resource
    private PayService payService;

    /**
     * 用户充值信息的回显
     * @return 充值信息回显
     */
    @GetMapping("recharge/info")
    public CommonDto<RechargeInfoOutputDto> echoRechargeInfo() {
        CommonDto<RechargeInfoOutputDto> result = new CommonDto<>();
        try {
            result = payService.getRechargeInfo();
        } catch (Exception e) {
            this.LOGGER.info(e.getMessage(), e.fillInStackTrace());

            result.setData(null);
            result.setMessage(e instanceof ProjectException ? e.getMessage() : "failed");
            result.setStatus(500);
        }
        return result;
    }

    /**
     * 用户充值信息的回显,区块站
     * @return 充值信息回显
     */
    @GetMapping("transaction/info")
    public CommonDto<List<TransactionInfoOutputDto>> echoTransactionInfo() {
        CommonDto<List<TransactionInfoOutputDto>> result = new CommonDto<>();
        try {
            result = payService.echoTransactionInfo();
        } catch (Exception e) {
            this.LOGGER.info(e.getMessage(), e.fillInStackTrace());

            result.setData(null);
            result.setMessage(e instanceof ProjectException ? e.getMessage() : "failed");
            result.setStatus(500);
        }
        return result;
    }
}
