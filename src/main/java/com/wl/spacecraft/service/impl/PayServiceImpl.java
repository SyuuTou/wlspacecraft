package com.wl.spacecraft.service.impl;

import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.responsedto.RechargeInfoOutputDto;
import com.wl.spacecraft.dto.responsedto.TransactionInfoOutputDto;
import com.wl.spacecraft.mapper.BlockStationMapper;
import com.wl.spacecraft.mapper.ConfigWechatMapper;
import com.wl.spacecraft.model.BlockStation;
import com.wl.spacecraft.model.ConfigMinRechargeAmount;
import com.wl.spacecraft.model.ConfigWechat;
import com.wl.spacecraft.service.common.GenericService;
import com.wl.spacecraft.service.game.GameService;
import com.wl.spacecraft.service.pay.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayServiceImpl extends GenericService implements PayService {

    @Resource
    private GameService gameService;

    @Autowired
    private BlockStationMapper blockStationMapper;

    @Autowired
    private ConfigWechatMapper configWechatMapper;

    /**
     * 获取所有的区块交易记录
     * 按照时间降序排序
     *
     * @return
     */
    private List<BlockStation> echoTransaction() {
        Example example = new Example(BlockStation.class);
        example.and().andEqualTo("delFlag", 0);
        example.setOrderByClause("create_time desc");

        List<BlockStation> list = blockStationMapper.selectByExample(example);
        System.err.println("交易记录" + list);
        return list;
    }

    @Override
    @Transactional(readOnly = true)
    public CommonDto<RechargeInfoOutputDto> getRechargeInfo() {

        CommonDto<RechargeInfoOutputDto> result = new CommonDto<>();
        //设置最小充值数量
        RechargeInfoOutputDto obj = new RechargeInfoOutputDto();
        //设置默认的最小充值金额
        obj.setMinRechargeAmount(new BigDecimal(500));

        //重置最小充值金额
        ConfigMinRechargeAmount record = gameService.getMinRechargeAmountRecord();
        if (record != null && record.getAmount() != null) {
            obj.setMinRechargeAmount(record.getAmount());
        }
        //设置微信客服
        List<ConfigWechat> configWechats = configWechatMapper.selectAllOrderBySort();
        System.err.println(configWechats);
        obj.setWechats(configWechats);
        //设置og的兑换比率
        obj.setConfigOgPrice(gameService.getConfigOgPrice());

        result.setStatus(200);
        result.setMessage("success");
        result.setData(obj);
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public CommonDto<List<TransactionInfoOutputDto>> echoTransactionInfo() {
        CommonDto<List<TransactionInfoOutputDto>> result = new CommonDto<>();

        List<TransactionInfoOutputDto> list = new ArrayList<>();

        List<BlockStation> blockStations = this.echoTransaction();
        for (BlockStation e : blockStations
        ) {
            TransactionInfoOutputDto obj = new TransactionInfoOutputDto();
            obj.setUserId(e.getUserid());
            //TODO 是否需要在SQL上做类型的匹配
            obj.setType(e.getType());
            switch(e.getType()){
                case 1:{
                   obj.setTypeNote("充币");
                }break;
                case 2:{
                    obj.setTypeNote("提币");
                }break;
                default:{}
            }
            obj.setOgChange(e.getOgChange());
            obj.setTxHash(e.getTxhash());
            list.add(obj);
        }

        result.setStatus(200);
        result.setMessage("success");
        result.setData(list);
        return result;
    }
}
