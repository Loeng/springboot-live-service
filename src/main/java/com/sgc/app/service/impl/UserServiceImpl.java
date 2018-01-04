package com.sgc.app.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sgc.api.huanxin.impl.EasemobIMUsers;
import com.sgc.comm.util.HttpClientService;
import com.sgc.comm.util.HttpResult;
import com.sgc.domain.User;
import com.sgc.domain.YunZhiBo;
import com.sgc.domain.mapper.UserMapper;
import com.sgc.domain.vm.ResultVM;
import com.sgc.app.service.UserService;

import io.swagger.client.model.RegisterUsers;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
	private EasemobIMUsers easemobIMUsers = new EasemobIMUsers();
	@Autowired
	private UserMapper userMapper;
	private String regx = "5b77e34ff78f3b53";
	/**
	 * 注册用户
	 */
	public ResultVM register(User user) {
		try {
			// 调用环信接口注册用户,密码经过Md5加密
			RegisterUsers users = new RegisterUsers();
			io.swagger.client.model.User HXuser = new io.swagger.client.model.User().username(user.getUsername())
					.password("123456");
			users.add(HXuser);
			Object createNewIMUserSingle = easemobIMUsers.createNewIMUserSingle(users);
			System.out.println(createNewIMUserSingle);
			// 设置用户创建的默认值
			user.setUniacid(2);
			String nickname = "";

			String[] split = user.getUsername().split("");
			for (int i = 0; i < split.length; i++) {

				if (i == 4 || i == 5 || i == 6 || i == 7) {
					nickname += "x";
				} else {
					nickname += split[i];
				}
			}
//			EntityWrapper<User> wrapper = new EntityWrapper<>();
//			wrapper.orderBy("id", false);
//			List<User> selectList = userMapper.selectList(wrapper);
//			User user2 = selectList.get(0);
//			System.out.println(user2.getId());
//			
			user.setId(new Date().getTime()/1000);
			user.setNickname(nickname);//用户昵称 首次默认为手机号隐藏位数
			user.setOpenid("wap_user_2_" + user.getUsername());
			user.setComefrom("APP");
			user.setUsername(user.getUsername());//用户名
			user.setMobileverify("1");
			user.setSalt(regx);
			String showPwd = user.getPwd();
			user.setPwd(DigestUtils.md5Hex(user.getPwd() + regx));
			user.setCreatetime(new Date().getTime()/1000);//用户注册时间
			user.setMobile(user.getUsername());//手机号
			user.setIsmobile("1");
			user.setAuth("1");//邮箱未验证
			user.setAvatar("http://kjz-avator-upyun.test.upcdn.net/4c184a40a9a91026317dce7a0c66048e.jpg");//首次注册的默认头像
			user.setGender("0");// 性别 0男1女 首次注册默认为男

			 
            
            String key = user.getUsername()+"_"+showPwd;
            String convert = convert(key);
            String convert2 = convert(convert+user.getCreatetime());
            user.setKey(convert2);
			
            
            //直播聊天室注册
            HttpClientService clientService = new HttpClientService();
            YunZhiBo zhiBo = new YunZhiBo(); 
            zhiBo.setId("user_"+user.getId()+"");
            zhiBo.setPwd("123456");
            String json =  JSONObject.toJSONString(zhiBo).toString();
            HttpResult result = clientService.doDostWithJSON("http://zby.iiiie.top/?svc=account&cmd=regist", json);
			
			Integer insert = userMapper.insert(user);
			System.out.println(insert);
			if (insert == 1) {
				
				return ResultVM.ok("注册成功");
			} else {
				return ResultVM.error(1, "后台错误,注册用户失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResultVM.error(1, "系统错误");
		}

	}
	
	private Map<String, Object> toMap(Object result) {
		Map<String, Object> map = new Gson().fromJson(result.toString(), new TypeToken<HashMap<String, Object>>() {
		}.getType());
		return map;
	}

	public static String convert(String tagertStr) {  
        byte[] value;  
        try {  
            value = tagertStr.getBytes(Charsets.UTF_8);  
            return new String(Base64.encodeBase64(value),Charsets.UTF_8);  
        } catch (Exception e) {  
        }  
        return null;  
    }  
}
