package com.wl.spacecraft.controller;

import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.commondto.GameConfigCommonOutputDto;
import com.wl.spacecraft.exception.project.ProjectException;
import com.wl.spacecraft.service.common.GenericService;
import com.wl.spacecraft.service.game.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class GameController extends GenericService {
    @Resource
    GameService gameService;

    /**
     * 获取游戏配置元数据
     * @return
     */
    @GetMapping("game/config")
    public CommonDto<GameConfigCommonOutputDto> getGameConfig(){

        CommonDto<GameConfigCommonOutputDto> result=new CommonDto<>();
        try{
            result = gameService.getGameConfig();
        }catch(Exception e){
            this.LOGGER.info(e.getMessage(),e.fillInStackTrace());

            result.setData(null);
            result.setMessage(e instanceof ProjectException ? e.getMessage() : "failed");
            result.setStatus(500);
        }
        return result;
    }


}
