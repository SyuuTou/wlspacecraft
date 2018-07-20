package com.wl.spacecraft.service.impl;

import com.wl.spacecraft.mapper.MetaGameDataMapper;
import com.wl.spacecraft.model.MetaGameData;
import com.wl.spacecraft.service.common.GenericService;
import com.wl.spacecraft.service.game.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl extends GenericService implements GameService {

    @Autowired
    MetaGameDataMapper metaGameDataMapper;

    @Override
    public MetaGameData getGameConfig() {
        return metaGameDataMapper.getGameConfig();
    }
}
