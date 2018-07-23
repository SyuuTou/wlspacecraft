package com.wl.spacecraft.service.impl;

import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.commondto.GameConfigCommonOutputDto;
import com.wl.spacecraft.mapper.MetaGameDataMapper;
import com.wl.spacecraft.model.MetaGameData;
import com.wl.spacecraft.service.common.GenericService;
import com.wl.spacecraft.service.game.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameServiceImpl extends GenericService implements GameService {

    @Autowired
    MetaGameDataMapper metaGameDataMapper;

    @Override
    @Transactional(readOnly = true)
    public CommonDto<GameConfigCommonOutputDto> getGameConfig() {
        CommonDto<GameConfigCommonOutputDto> result =new CommonDto<>();

        MetaGameData config = metaGameDataMapper.getGameConfig();
        if(config !=null){
            GameConfigCommonOutputDto outputData=new GameConfigCommonOutputDto();

            outputData.setStoneCreateSpeed(config.getStoneCreateSpeed());
            outputData.setStoneMoveSpeed(config.getStoneMoveSpeed());
            outputData.setOgCreateSpeed(config.getOgCreateSpeed());
            outputData.setDifficulty(config.getDifficulty());
            outputData.setEthValue(config.getEthValue());

            result.setData(outputData);
        }


        result.setMessage("游戏配置元数据");
        result.setStatus(200);

        return result;
    }



}
