package com.wl.spacecraft.controller;

import com.wl.spacecraft.dto.commondto.CommonDto;
import com.wl.spacecraft.dto.commondto.PagingInputDto;
import com.wl.spacecraft.dto.commondto.PagingOutputDto;
import com.wl.spacecraft.dto.commondto.UserInfoCommonOutputDto;
import com.wl.spacecraft.dto.requestdto.*;
import com.wl.spacecraft.dto.responsedto.*;
import com.wl.spacecraft.exception.project.ProjectException;
import com.wl.spacecraft.service.common.GenericService;
import com.wl.spacecraft.service.user.MsgService;
import com.wl.spacecraft.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController extends GenericService {

    @Resource
    UserService userService;

    @Resource
    MsgService msgService;

    /**
     * 短信验证码发送
     * @param phone
     * @return
     */
    @GetMapping("msg/send")
    public CommonDto<SendMsgOutputDto> sendMsg(String phone){
        CommonDto<SendMsgOutputDto> result=new CommonDto<>();
        try{
            result = msgService.senMsg(phone);
        }catch(Exception e){
            this.LOGGER.info(e.getMessage(),e.fillInStackTrace());

            result.setData(null);
            result.setMessage(e instanceof ProjectException ? e.getMessage() : "failed");
            result.setStatus(500);
        }
        return result;
    }

    /**
     * 用户登录
     * @param body
     * @return
     */
    @PostMapping("login")
    public CommonDto<LoginOutputDto> login(@RequestBody LoginInputDto body){
        System.err.println(body);
        CommonDto<LoginOutputDto> result = new CommonDto<>();
        try {
            result = userService.login(body);

        }catch (Exception e){
            this.LOGGER.info(e.getMessage(),e.fillInStackTrace());

            result.setData(null);
            result.setMessage(e instanceof ProjectException ? e.getMessage() : "failed");
            result.setStatus(500);
        }
        return result;
    }

    /**
     * 查询用户数据
     * @param body
     * @return
     */
    @PostMapping("user/info")
    public CommonDto<UserInfoCommonOutputDto> getUserInfo(@RequestBody UserInfoInputDto body ){
        System.err.println(body);

        CommonDto<UserInfoCommonOutputDto> result=new CommonDto<>();
        try{
            result=userService.getUserInfo(body);
        }catch(Exception e){
            this.LOGGER.info(e.getMessage(),e.fillInStackTrace());

            result.setData(null);
            result.setMessage(e instanceof ProjectException ? e.getMessage() : "failed");
            result.setStatus(500);
        }
        return result;
    }

    /**
     * 用户开始游戏
     * @param body
     * @return
     */
    @PostMapping("game/start")
    public CommonDto<GameStartOutputDto> startGame(@RequestBody GameStartInputDto body ){
        System.err.println(body);

        CommonDto<GameStartOutputDto> result=new CommonDto<>();
        try{
            result=userService.startGame(body);
        }catch(Exception e){
            this.LOGGER.info(e.getMessage(),e.fillInStackTrace());

            result.setData(null);
            result.setMessage(e instanceof ProjectException ? e.getMessage() : "failed");
            result.setStatus(500);
        }
        return result;
    }

    /**
     * 游戏结束
     * @param body
     * @return
     */
    @PostMapping("game/over")
    public CommonDto<GameOverOutputDto> overGame(@RequestBody GameOverInputDto body){
        System.err.println(body);

        CommonDto<GameOverOutputDto> result=new CommonDto<>();
        try{
            result=userService.overGame(body);
        }catch(Exception e){
            this.LOGGER.info(e.getMessage(),e.fillInStackTrace());

            result.setData(null);
            result.setMessage(e instanceof ProjectException ? e.getMessage() : "failed");
            result.setStatus(500);
        }
        return result;
    }

    /**
     * 提取金币
     * @param body
     * @return
     */
    @PostMapping("coin/to/account")
    public CommonDto<CoinToAccountOutputDto> coinToAccount(@RequestBody CoinToAccountInputDto body ){
        System.err.println(body);

        CommonDto<CoinToAccountOutputDto> result=new CommonDto<>();
        try{
            result=userService.coinToAccount(body);
        }catch(Exception e){
            this.LOGGER.info(e.getMessage(),e.fillInStackTrace());

            result.setData(null);
            result.setMessage(e instanceof ProjectException ? e.getMessage() : "failed");
            result.setStatus(500);
        }
        return result;
    }

    /**
     * 用户获取金币排行榜
     * @return
     */
    @PostMapping("og/rank")
    public CommonDto<GameRankOutputDto> ogObtainRank(@RequestBody PagingInputDto body){

        System.err.println("分页参数--》》 "+body);

        CommonDto<GameRankOutputDto> result=new CommonDto<>();
        try{
            result=userService.gameRank(body);
        }catch(Exception e){
            this.LOGGER.info(e.getMessage(),e.fillInStackTrace());

            result.setData(null);
            result.setMessage(e instanceof ProjectException ? e.getMessage() : "failed");
            result.setStatus(500);
        }
        return result;
    }





}
