package com.lhjl.tzzs.proxy.controller.bluewave;

import com.lhjl.tzzs.proxy.dto.CommonDto;
import com.lhjl.tzzs.proxy.dto.bluewave.UserHeadPicOutputDto;
import com.lhjl.tzzs.proxy.dto.bluewave.UserInfomationInputDto;
import com.lhjl.tzzs.proxy.dto.bluewave.UserInformationOutputDto;
import com.lhjl.tzzs.proxy.model.MetaIdentityType;
import com.lhjl.tzzs.proxy.service.GenericService;
import com.lhjl.tzzs.proxy.service.bluewave.BlueUserInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class BlueUserInfoController extends GenericService{

    @Resource
    private BlueUserInfoService blueUserInfoService;

    @GetMapping("/v{appid}/headpic")
    public CommonDto<UserHeadPicOutputDto> getUserHeadpic( String token,@PathVariable Integer appid){
        CommonDto<UserHeadPicOutputDto> result = new CommonDto<>();

        try {
            result = blueUserInfoService.getUserHeadpic(appid, token);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setData(null);
            result.setStatus(502);
            result.setMessage("服务器端发生错误");
        }

        return result;
    }

    /**
     * 修改用户头像接口
     * @param appid
     * @param body
     * @return
     */
    @PostMapping("/v{appid}/update/headpic")
    public CommonDto<String> updateUserHeadpic(@PathVariable Integer appid, @RequestBody Map<String,Object> body){
        CommonDto<String> result = new CommonDto<>();
        try {
            result = blueUserInfoService.updateUserHeadpic(appid, body);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setStatus(502);
            result.setData(null);
        }

        return result;
    }

    /**
     * 读取用户信息接口
     * @param appid
     * @param token
     * @return
     */
    @GetMapping("/v{appid}/read/infomation")
    public CommonDto<UserInformationOutputDto> readUserInfomation(@PathVariable Integer appid,String token){
        CommonDto<UserInformationOutputDto> result = new CommonDto<>();
        try {
            result = blueUserInfoService.getUserInformation(appid, token);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setStatus(502);
            result.setMessage("服务器端发生错误");
            result.setData(null);

            return result;
        }
        return result;
    }

    @GetMapping("/v{appid}/meta/identitytype")
    public CommonDto<List<MetaIdentityType>> getMetaIdentityType(@PathVariable Integer appid){
        CommonDto<List<MetaIdentityType>> result = new CommonDto<>();

        try {
            result =blueUserInfoService.getMetaIdentityType();
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setData(null);
            result.setMessage("服务器端发生错误");
            result.setStatus(502);
        }

        return result;
    }

    @PostMapping("/v{appid}/edit/userinfo")
    public CommonDto<String> editUserInfo(@PathVariable Integer appid, @RequestBody UserInfomationInputDto body){
        CommonDto<String> result = new CommonDto<>();
        try {
            result = blueUserInfoService.editUserInfo(body,appid);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            result.setMessage("服务器端发生错误");
            result.setStatus(502);
            result.setData(null);
        }

        return result;
    }

    /**
     * 检查手机号是否存在接口
     * @param appid
     * @param token
     * @return
     */
    @GetMapping("/v{appid}/check/phonenumber")
    public CommonDto<Integer> checkUserPhonenumber(@PathVariable Integer appid,String token){
        try {
            return blueUserInfoService.checkUserPonenumber(token,appid);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            return new CommonDto<>(0,"服务器端发生错误",502);
        }
    }

    /**
     * 检查用户头像是否存在的接口
     * @param appid
     * @param token
     * @return
     */
    @GetMapping("/v{appid}/check/headpic")
    public CommonDto<Integer> checkUserHeadpic(@PathVariable Integer appid,String token){
        try {
            return blueUserInfoService.checkUserHeadpic(token, appid);
        }catch (Exception e){
            this.LOGGER.error(e.getMessage(),e.fillInStackTrace());
            return new CommonDto<>(0,"服务器端发生错误",502);
        }
    }
}
