package com.wl.spacecraft.controller;

import com.wl.spacecraft.model.MetaGameData;
import com.wl.spacecraft.service.game.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class GameController {
    @Resource
    GameService gameService;

    @GetMapping("game/config")
    public Object get(){
        MetaGameData gameConfig = gameService.getGameConfig();
        return  gameConfig;
    }
}
