package com.lhjl.tzzs.proxy.service;

import com.lhjl.tzzs.proxy.model.InvestorDemandSpeedway;

import java.util.List;

/**
 * Created by lanhaijulang on 2018/1/20.
 */
public interface InvestorDemandSpeedwayService {
    Integer save(InvestorDemandSpeedway investorDemandSpeedway);

    void deleteAll(Integer investorId);

    void delete(InvestorDemandSpeedway investorDemandSpeedway);

    Integer insertList(List<InvestorDemandSpeedway> investorDemandSpeedwayList);

    List<InvestorDemandSpeedway> select(InvestorDemandSpeedway investorDemandSpeedway);
}
