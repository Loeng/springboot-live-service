package com.sgc.comm.filter;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sgc.comm.util.HttpClientService;
import com.sgc.comm.util.HttpResult;
import com.sgc.comm.util.JedisClusterUtils;
import com.sgc.domain.User;
import com.sgc.domain.YunZhiBo;
import com.sgc.app.service.UserService;

import io.swagger.annotations.Api;
@Api(value = "app - Token")
@Controller
public class JsonWebToken {
	
	@Autowired
	private UserService userService;
	@Autowired
	private JedisClusterUtils jedisClusterUtils;
    @Autowired  
    private JwtInfo jwtInfo;  
    /**
     * 测试接口http://localhost:8899/v1/oauth/token?clientId=098f6bcd4621d373cade4e832627b4f6&userName=test&password=test
     * @param loginPara   15013278988
     * @return
     */
    //@SwaggerDefinition(host="8899",basePath="/v1/oauth/token")@RequestBody
    @RequestMapping("/oauth/token")
    @ResponseBody
    public Object getAccessToken( @RequestBody LoginPara loginPara)  {  
    	System.out.println("00进来了");
    	System.out.println("姓名"+loginPara.getUserName());
    	System.out.println("姓名"+loginPara.getClientId());
    	System.out.println("姓名"+loginPara.getPassword());
        ResultMsg resultMsg;  
        try  
        {  
        	System.out.println("0进来了");
            if(loginPara.getClientId() == null || (loginPara.getClientId().compareTo(jwtInfo.getClientId()) != 0))  
            {  
                resultMsg = new ResultMsg(ResultStatusCode.INVALID_CLIENTID.getCode(),ResultStatusCode.INVALID_CLIENTID.getmsg(), null);  
                return resultMsg;  
            }  
              
            //验证码校验在后面章节添加  
              
              
            //验证用户名密码  
            //UserInfo user = userRepositoy.findUserInfoByName(loginPara.getUserName()); 
           EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
            entityWrapper.where(" username ={0}", loginPara.getUserName());
            User user = userService.selectOne(entityWrapper);
            if (user == null)  
            {  
                resultMsg = new ResultMsg(ResultStatusCode.INVALID_PASSWORD.getCode(),  
                        ResultStatusCode.INVALID_PASSWORD.getmsg(), null);  
                return resultMsg;  
            }  
            else  
            {  
                String md5Password = DigestUtils.md5Hex(loginPara.getPassword()+user.getSalt());  
                  System.out.println("系统加盐后密码"+md5Password);
                if (md5Password.compareTo(user.getPwd()) != 0)  
                {  
                    resultMsg = new ResultMsg(ResultStatusCode.INVALID_PASSWORD.getCode(),  
                            ResultStatusCode.INVALID_PASSWORD.getmsg(), null);  
                    return resultMsg;  
                }  
            }  
            System.out.println("1进来了");
            //拼装accessToken  
            String accessToken = JwtHelper.createJWT(loginPara.getUserName(), String.valueOf(user.getUsername() ),"", jwtInfo.getClientId(), jwtInfo.getName(),  
            		jwtInfo.getExpiresSecond() * 1000, jwtInfo.getBase64Secret());  
            System.out.println("2进来了");
            //返回accessToken  
            AccessToken accessTokenEntity = new AccessToken();  
            accessTokenEntity.setAccess_token(accessToken);  
            accessTokenEntity.setExpires_in(jwtInfo.getExpiresSecond());  
            accessTokenEntity.setToken_type("bearer");  
           
            accessTokenEntity.setUser(user);
            jedisClusterUtils.saveString("loginToken", accessToken+","+user.getUsername());
            HttpClientService clientService = new HttpClientService();
            YunZhiBo zhiBo = new YunZhiBo(); 
            zhiBo.setId("user_"+user.getId());
            zhiBo.setPwd("123456");
            zhiBo.setAppId("1255625061");
            String json =  JSONObject.toJSONString(zhiBo).toString();
            HttpResult result = clientService.doDostWithJSON("http://zby.iiiie.top/?svc=account&cmd=login", json);
			System.out.println(result.getStatus());
			String content = result.getContent();
			Object json2 = JSONObject.toJSON(content);
			Map<String, Object> map = toMap(json2);
			String YunToken =(String) map.get("token");
			String userSig =(String) map.get("userSig");
			accessTokenEntity.setUserSig(userSig);
			accessTokenEntity.setLiveToken(YunToken);
            resultMsg = new ResultMsg(ResultStatusCode.OK.getCode(), ResultStatusCode.OK.getmsg(), accessTokenEntity);  
            System.out.println("3进来了");
            System.out.println(resultMsg);
            
            return resultMsg;  
              
        }  
        catch(Exception ex)  {  
        	System.out.println("4进来了");
            resultMsg = new ResultMsg(ResultStatusCode.SYSTEM_ERR.getCode(),   
                    ResultStatusCode.SYSTEM_ERR.getmsg(), null);  
            return resultMsg;  
        } 
    }  
	private Map<String, Object> toMap(Object result) {
		Map<String, Object> map = new Gson().fromJson(result.toString(), new TypeToken<HashMap<String, Object>>() {
		}.getType());
		return map;
	}

    
}
