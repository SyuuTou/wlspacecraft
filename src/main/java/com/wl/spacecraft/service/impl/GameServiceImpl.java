package com.wl.spacecraft.service.impl;

import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.commondto.GameConfigCommonOutputDto;
import com.wl.spacecraft.exception.project.ProjectException;
import com.wl.spacecraft.mapper.*;
import com.wl.spacecraft.model.*;
import com.wl.spacecraft.service.common.GenericService;
import com.wl.spacecraft.service.game.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GameServiceImpl extends GenericService implements GameService {

    @Autowired
    MetaGameDataMapper metaGameDataMapper;

    @Autowired
    ConfigOgTodayMapper configOgTodayMapper;
    @Autowired
    ConfigOgPriceMapper configOgPriceMapper;
    @Autowired
    ConfigGameDifficultyMapper configGameDifficultyMapper;
    @Autowired
    ConfigDropogAmountMapper configDropogAmountMapper;


    @Override
    @Transactional
    public Integer getConfigOgToday() {

        Example example = new Example(ConfigOgToday.class);
//        example.and().andEqualTo("type",1);
        example.setOrderByClause("create_time desc");

        List<ConfigOgToday> list = configOgTodayMapper.selectByExample(example);
        //TODO 是否增加默认值
        Integer ogToday = 0;
        if (list != null && list.size() > 0) {
            ogToday = list.get(0).getOgToday();
            if (ogToday == null) {
                throw new ProjectException("DB数据存在异常,最新 OG当日领取上限 记录为null");
            }
        }

        return ogToday;

    }

    @Override
    @Transactional
    public ConfigOgPrice getConfigOgPrice() {
        Example example = new Example(ConfigOgPrice.class);
//        example.and().andEqualTo("type",1);
        example.setOrderByClause("create_time desc");

        List<ConfigOgPrice> list = configOgPriceMapper.selectByExample(example);

        ConfigOgPrice configOgPrice = null;
        if (list != null && list.size() > 0) {
            configOgPrice = list.get(0);
        }

        return configOgPrice;
    }

    @Override
    @Transactional
    public Integer getConfigGameDifficulty() {
        Example example = new Example(ConfigGameDifficulty.class);
//        example.and().andEqualTo("type",1);
        example.setOrderByClause("create_time desc");

        List<ConfigGameDifficulty> list = configGameDifficultyMapper.selectByExample(example);
        Integer difficulty = null;
        if (list != null && list.size() > 0) {
            difficulty = list.get(0).getDifficulty();
            if (difficulty == null) {
                throw new ProjectException("DB数据存在异常,最新 难度 记录为null");
            }

        }

        return difficulty;
    }

    @Override
    @Transactional
    public Integer getConfigDropogAmount() {
        Example example = new Example(ConfigDropogAmount.class);
//        example.and().andEqualTo("type",1);
        example.setOrderByClause("create_time desc");

        List<ConfigDropogAmount> list = configDropogAmountMapper.selectByExample(example);
        ConfigDropogAmount configDropogAmount = list.get(0);

        Integer currentDropOg = null;
        if (list != null && list.size() > 0) {
            currentDropOg = list.get(0).getCurrentDropOg();
            if (currentDropOg == null) {
                throw new ProjectException("DB数据存在异常,最新 空投OG 记录为null");
            }
        }

        return currentDropOg;
    }


    @Override
    @Transactional(readOnly = true)
    public CommonDto<GameConfigCommonOutputDto> getGameConfig() {
        CommonDto<GameConfigCommonOutputDto> result = new CommonDto<>();

        MetaGameData config = metaGameDataMapper.getGameConfig();

        GameConfigCommonOutputDto outputData = new GameConfigCommonOutputDto();

        //设置难度
        outputData.setDifficulty(this.getConfigGameDifficulty());

        //设置OG币同其他的兑换比率
        //配置表可能存在没有数据的情况，引发空指针异常
        try {
            outputData.setCurrentBasePrice(this.getConfigOgPrice().getCurrentBasePrice());
            outputData.setCurrentBonus(this.getConfigOgPrice().getCurrentBonus());
        } catch (Exception e) {

        }

        if (config != null) {
            outputData.setStoneCreateSpeed(config.getStoneCreateSpeed());
            outputData.setStoneMoveSpeed(config.getStoneMoveSpeed());
            outputData.setOgCreateSpeed(config.getOgCreateSpeed());
        }

        result.setData(outputData);
        result.setMessage("游戏配置元数据");
        result.setStatus(200);

        return result;
    }


}
