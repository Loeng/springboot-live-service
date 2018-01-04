package com.sgc.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.subject.Subject;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sgc.comm.ValidateCodeUtils;
import com.sgc.comm.util.HttpClientService;
import com.sgc.comm.util.HttpResult;
import com.sgc.comm.util.JedisClusterUtils;
import com.sgc.comm.util.MailUtils;
import com.sgc.comm.util.RandomUtils;
import com.sgc.comm.util.SendMessageUtils;
import com.sgc.comm.util.ShiroUtils;
import com.sgc.domain.User;
import com.sgc.domain.YunZhiBo;
import com.sgc.domain.vm.ResultVM;
import com.sgc.app.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by Eddie
 */
@Api(value = "API - LoginController")
@RestController("login")
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private UserService userService;
	@Autowired
	private JedisClusterUtils jedisClusterUtils;
	String regx1="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$";
	private String regx = "5b77e34ff78f3b53";
	
	/**
	 * 自动登录
	 *  author Eddie
	 * @throws Exception 
	 */
	@ApiOperation(value = "自动登录", httpMethod = "GET", response = ResultVM.class)
	@PostMapping("/autoLogin")
	public ResultVM autoLogin(@RequestBody JSONObject JSONObject) throws Exception {
		String token = (String) JSONObject.get("token");
		System.out.println(token);
//		System.out.println(token);
		String loginToken = JedisClusterUtils.getString("loginToken");
		System.out.println(loginToken);
		String[] split = loginToken.split(",");
		String sysToken = split[0];
		System.out.println(sysToken);
		String username = split[1];
		System.out.println(username);
		if(token.equals(sysToken)){
			EntityWrapper<User> wrapper = new EntityWrapper<>();
			wrapper.where("username={0}", username);
			User user = userService.selectOne(wrapper);
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
				HashMap<String, Object> res = new HashMap<>();
				res.put("userInfo", user);
				res.put("LiveToken", YunToken);
				res.put("userSig", userSig);
				res.put("msg", "自动登录成功");
				System.out.println("进来了");
			return ResultVM.ok(res);
		}else {
			return ResultVM.error(1, "自动登录已过期,请重新登录");
		}
		
	
	}

	
	
	
	
	
	
	
	/**
	 * 检测手机号是否已注册 author Eddie
	 */
	@ApiOperation(value = "检测手机号是否已注册", httpMethod = "GET", response = ResultVM.class)
	@GetMapping("/checkMobil/{username}")
	public ResultVM checkMobil(@PathVariable("username") String username) {
		EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
		entityWrapper.where("username={0}", username);
		User user = userService.selectOne(entityWrapper);
		if (user != null) {
			return ResultVM.error(1, "该手机已被注册");
		} else {
			return ResultVM.ok();
		}
	}

	/**
	 * 发送手机验证码
	 * 
	 */
	@ApiOperation(value = "发送手机验证码", httpMethod = "GET", response = ResultVM.class)
	@GetMapping("/sendMobileCode/{username}/{method}")
	public ResultVM sendMobileCodes(@PathVariable("username") String userName, @PathVariable("method") String method)
			throws UnsupportedEncodingException {
		try {
			String randNum = ValidateCodeUtils.getRandNum();

			if ("1".equals(method)) {
				EntityWrapper<User> wrapper = new EntityWrapper<>();
				wrapper.where("username={0}", userName);
				User one = userService.selectOne(wrapper);
				if (one != null) {
					return ResultVM.error(1, "用户已存在");
				} else {
					// -------------------------------------------------------->发送注册时手机验证码
					JedisClusterUtils.saveString("zc" + userName + randNum, randNum, 999);
					SendMessageUtils.sendSms(userName, randNum);
				}

			} else if ("2".equals(method)) {
				// -------------------------------------------------------->发送忘记密码的手机验证码
				JedisClusterUtils.saveString("wjmm" + userName + randNum, randNum, 999);
				System.out.println("wjmm" + userName + randNum);
				SendMessageUtils.sendSms(userName, randNum);
			} else if ("3".equals(method)) {
				// -------------------------------------------------------->发送修改密码的手机验证码
				JedisClusterUtils.saveString("xgmm" + userName + randNum, randNum, 999);
				SendMessageUtils.sendSms(userName, randNum);
			} else if ("4".equals(method)) {
				// -------------------------------------------------------->发送修改支付密码的手机验证码
				String substring = randNum.substring(0, 4);
				System.out.println(substring);
				JedisClusterUtils.saveString("xgzfmm" + userName + substring, substring, 999);
				SendMessageUtils.sendSms(userName, substring);

			} else if ("5".equals(method)) {
				// -------------------------------------------------------->发送绑定手机验证码
				JedisClusterUtils.saveString("bdsj" + userName + randNum, randNum, 999);
				SendMessageUtils.sendSms(userName, randNum);
			} else if ("6".equals(method)) {
				String substring = randNum.substring(0, 4);
				// -------------------------------------------------------->发送忘记支付密码
				JedisClusterUtils.saveString("wjzfmm" + userName + substring, substring, 999);
				SendMessageUtils.sendSms(userName, substring);
			} else if ("7".equals(method)) {
				String substring = randNum.substring(0, 4);
				// -------------------------------------------------------->发送设置支付密码
				JedisClusterUtils.saveString("szzfmm" + userName + substring, substring, 999);
				SendMessageUtils.sendSms(userName, substring);
			} else if ("8".equals(method)) {
				// -------------------------------------------------------->发送设置支付密码
				JedisClusterUtils.saveString("bdnsj" + randNum, randNum, 999);
				System.out.println("系統發送的" + randNum);
				System.out.println("系統發送的姓名" + userName);
				SendMessageUtils.sendSms(userName, randNum);
			} else {
				return ResultVM.error(1, "发送失败");
			}
			return ResultVM.ok("发送成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultVM.error("发送失败");
		}
	}

	/**
	 * 注册 author Eddie
	 */
	@ApiOperation(value = "注册", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/register")
	public ResultVM register(
			@RequestBody JSONObject JSONObject)
			throws UnsupportedEncodingException {
		String username = (String) JSONObject.get("username");
		String password = (String) JSONObject.get("password");
		String email = (String) JSONObject.get("email");
		String code = (String) JSONObject.get("code");
		
		System.out.println(email);
		String registerCode = JedisClusterUtils.getString("zc" + username + code);
		if (registerCode != null) {
			
			if (registerCode.equals(code)) {
				if(!password.matches(regx1)) {
					return ResultVM.error(1, "密码格式不对，请输入6-12位数字或字母组合的密码");
				}
				EntityWrapper<User> emailWrapper = new EntityWrapper<User>();
				emailWrapper.where("email = {0}", email);
				User one1 = userService.selectOne(emailWrapper);
				if (one1 != null) {
					return ResultVM.error(1, "邮箱已被注册使用");
				}

				EntityWrapper<User> wrapper = new EntityWrapper<User>();
				wrapper.where("username = {0}", username);
				User one = userService.selectOne(wrapper);
				if (one == null) {

					User user = new User();
					// 设置用户名
					user.setUsername(username);
					// 設置郵件
					user.setPwd(password);
					user.setEmail(email);
					ResultVM register = userService.register(user);
					return register;
				} else {
					return ResultVM.error(1, "用户名已存在,注册失败");
				}

			} else {

				return ResultVM.error(1, "验证码不正确或者已经过期");
			}

		} else {

			return ResultVM.error(1, "验证码不正确或者已经过期");
		}

	}

	/**
	 * 登录
	 * 
	 */
	@ApiOperation(value = "登录", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/login")
	public ResultVM login(@RequestParam("password") String password, @RequestParam("username") String username) {
		System.out.println("进来了");
		UsernamePasswordToken token = null;
		try {

			Subject subject = ShiroUtils.getSubject();
			// sha256加密

			password = new Sha256Hash(password).toHex();
			token = new UsernamePasswordToken(username, password);
			subject.login(token);

		} catch (UnknownAccountException e) {
			return ResultVM.error(e.getMessage());
		} catch (IncorrectCredentialsException e) {
			return ResultVM.error(e.getMessage());
		} catch (LockedAccountException e) {
			return ResultVM.error(e.getMessage());
		}
		return ResultVM.ok();
	}

	/**
	 * 忘記密碼---->發送驗證碼 type 0發送短信
	 * 
	 * 
	 */
	@ApiOperation(value = "忘記密碼---->發送驗證碼", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/sendForgetPassWordCode")
	public ResultVM sendForgetPassWordCode(@RequestBody JSONObject JSONObject) throws Exception {
		String type = (String) JSONObject.get("type");
		String username = (String) JSONObject.get("username");
		if (type.equals("0")) {

			String randNum = ValidateCodeUtils.getRandNum();
			JedisClusterUtils.saveString("wjmm" + username + randNum, randNum, 999);
			System.out.println("wjmm" + username + randNum);
			SendMessageUtils.sendSms(username, randNum);
			return ResultVM.ok("發送忘記密碼的手機驗證碼成功");
		} else {
			String email = (String) JSONObject.get("email");
			EntityWrapper<User> wrapper = new EntityWrapper<>();
			wrapper.where("email={0}", email);
			User user = userService.selectOne(wrapper);
			if (user != null) {
				String randNum = ValidateCodeUtils.getRandNum();
				 
			        
			        
				// SGC的验证码邮件
				String title = "SGC忘记密码邮件";
				// 的客户,您修改邮箱验证码为 + valiDateCode
				String content = "尊敬的客户,您忘記密碼驗證碼為:" + randNum;
				// DeliveredMail deliveredMail = new DeliveredMail();
				// Address[] recipients = new Address[1];
				// recipients[0]=new InternetAddress(user.getEmail());
				// recipients[0]=new InternetAddress("759565854@qq.com");
				// deliveredMail.transport("smtp.mxhichina.com", 25, "customer@kanjinzhao.vip‍",
				// "kjz.2018", title, content, "customer@kanjinzhao.vip‍",recipients );
				MailUtils.sendMail(user.getEmail(), title, content);
				return ResultVM.ok("發送忘記密碼的郵箱驗證碼成功");
			} else {
				return ResultVM.error(1, "邮箱并未被注册");
			}

		}

	}

	/**
	 * 忘记密码 ---> 修改密码 忘记密码 --->发送重置密码至手机短信 author Eddie
	 * 
	 * @throws Exception
	 */
	@ApiOperation(value = "忘记密码 ---> 修改密码 ", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/changPassWord")
	public ResultVM changPassWord(@RequestBody JSONObject JSONObject) throws Exception {

		String type = (String) JSONObject.get("type");

		if (type.equals("0")) {
			String username = (String) JSONObject.get("username");
			String code = (String) JSONObject.get("code");
			String changPassWordCode = JedisClusterUtils.getString("wjmm" + username + code);
			System.out.println(changPassWordCode);
			if (changPassWordCode != null) {
				EntityWrapper<User> wrapper = new EntityWrapper<>();
				wrapper.where("username = {0}", username);
				User user = userService.selectOne(wrapper);
				StringBuilder randomPassWord = RandomUtils.getRandomPassWord();
			    char c = (char) (Math.random() * 26 + 'a');  
			    char a = (char) (Math.random() * 26 + 'a');
				String newpassword = a+randomPassWord.toString()+c;
				if (user != null) {

					String key1 = user.getUsername() + "_" + newpassword;
					String convert = convert(key1);
					String convert2 = convert(convert + user.getCreatetime());
					user.setKey(convert2);
					user.setPwd(DigestUtils.md5Hex(newpassword + regx));
					userService.updateById(user);
					SendMessageUtils.sendSms(username, newpassword);
					return ResultVM.ok("密码已发送至您手机上,请及时登录修改");

				} else {
					return ResultVM.error(1, "用户名不存在");
				}

			} else {
				return ResultVM.error(1, "验证码错误或者已经过期");
			}

		} else {
			String email = (String) JSONObject.get("email");
			String code = (String) JSONObject.get("code");
			String changPassWordCode = JedisClusterUtils.getString("wjmm" + email + code);
			if (changPassWordCode != null) {

				EntityWrapper<User> wrapper = new EntityWrapper<>();
				wrapper.where("email = {0}", email);
				User user = userService.selectOne(wrapper);
				if (user != null) {
					StringBuilder randomPassWord = RandomUtils.getRandomPassWord();
					String newpassword = randomPassWord.toString();
					String key1 = user.getUsername() + "_" + newpassword;
					String convert = convert(key1);
					String convert2 = convert(convert + user.getCreatetime());
					user.setKey(convert2);
					user.setPwd(DigestUtils.md5Hex(newpassword + regx));
					userService.updateById(user);

					// SGC的验证码邮件
//					String title = "SGC忘记密码邮件";
//					// 的客户,您修改邮箱验证码为 + valiDateCode
//					String content = "尊敬的客户,您重置后的密码为:" + newpassword;
//					DeliveredMail deliveredMail = new DeliveredMail();
//					Address[] recipients = new Address[1];
//					// recipients[0]=new InternetAddress(user.getEmail());
//					recipients[0] = new InternetAddress("759565854@qq.com");
//					deliveredMail.transport("smtp.aliyun.com", 25, "customer@kanjinzhao.vip‍", "kjz.2018", title,
//							content, "customer@kanjinzhao.vip‍", recipients);
					
					// SGC的验证码邮件
					String title = "SGC重置密码邮件";
					String content = "尊敬的客户,您重置后的密码为:" + newpassword;
					MailUtils.sendMail(user.getEmail(), title, content);
					return ResultVM.ok("密码已发送至您邮箱上,请及时登录修改");
				} else {
					return ResultVM.error(1, "該郵箱未綁定");
				}
			} else {
				return ResultVM.error(1, "郵箱驗證碼錯誤或已過期");
			}
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
			return new String(Base64.encodeBase64(value), Charsets.UTF_8);
		} catch (Exception e) {
		}
		return null;
	}
}
