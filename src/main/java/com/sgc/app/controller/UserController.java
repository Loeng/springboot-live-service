package com.sgc.app.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sgc.api.huanxin.impl.EasemobIMUsers;
import com.sgc.comm.ValidateCodeUtils;
import com.sgc.comm.util.JedisClusterUtils;
import com.sgc.comm.util.MailUtils;
import com.sgc.domain.User;
import com.sgc.domain.vm.ResultVM;
import com.sgc.app.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.client.model.Nickname;

@RestController("appUser")
@RequestMapping("/app/user")
@Api(value = "app - User")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private JedisClusterUtils jedisClusterUtils;
	private String regx = "5b77e34ff78f3b53";
	String regx1="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$";
	private EasemobIMUsers easemobIMUsers = new EasemobIMUsers();
	/**
	 * 修改昵称 author Eddie
	 */
	@ApiOperation(value = "修改昵称", httpMethod = "GET", response = ResultVM.class)
	@GetMapping("/setNickName/{username}/{nickname}")
	public ResultVM setNickName(@PathVariable("username") String username, @PathVariable("nickname") String nickname) {
			
		
			
			Nickname a = new Nickname();
			a.setNickname(nickname);
			Object modifyIMUserNickNameWithAdminToken = easemobIMUsers.modifyIMUserNickNameWithAdminToken(username, a);

				return ResultVM.ok(modifyIMUserNickNameWithAdminToken);

	}

	/**
	 * 修改性别 author Eddie
	 */
	@ApiOperation(value = "修改性别", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/updateGender")
	public ResultVM updateGender(@RequestParam("username") String username, @RequestParam("gender") String gender) {
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.where("username={0}", username);
		User user = userService.selectOne(wrapper);
		if (user != null) {
			user.setGender(gender);
			boolean flag = userService.updateAllColumnById(user);
			System.out.println(flag);
			if (flag) {
				HashMap<String, Object> map = new HashMap<>();
				map.put("message", "修改性别成功");
				map.put("gender", gender);
				return ResultVM.ok(map);
			} else {
				return ResultVM.error(1, "系统错误,修改性别失败");
			}
		} else {
			return ResultVM.error(1, "用户不存在,修改性别失败");
		}

	}

	/**
	 * 修改手机号码 author Eddie
	 */
	@ApiOperation(value = "修改手机号", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/changMobile")
	public ResultVM changMobile(@RequestBody JSONObject JSONObject) throws Exception {

		String mobile = (String) JSONObject.get("mobile");
		String code = (String) JSONObject.get("code");
		String username = (String) JSONObject.get("username");
		String bdsjCode = JedisClusterUtils.getString("bdsj" + mobile + code);
		if (bdsjCode != null) {
			String md5Hex = DigestUtils.md5Hex(ValidateCodeUtils.getRandNum());
			JedisClusterUtils.saveString(username + "mobile", md5Hex, 999);
			HashMap<String, Object> map = new HashMap<>();
			map.put("token", md5Hex);
			map.put("msg", "校验修改手机验证码成功");
			return ResultVM.ok(map);

		} else {
			return ResultVM.error(1, "验证码过期或者输入错误");
		}

	}

	/**
	 * 更换新手机 author Eddie
	 */
	@ApiOperation(value = "更换新手机", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/Mobile")
	public ResultVM isBangMobile(@RequestBody JSONObject JSONObject) {
		String username = (String) JSONObject.get("username");
		System.out.println(username);
		String newMobile = (String) JSONObject.get("newMobile");
		System.out.println(newMobile);
		String code = (String) JSONObject.get("code");
		System.out.println(code);
		String token = (String) JSONObject.get("token");
		System.out.println(token);
		String key = JedisClusterUtils.getString(username + "mobile");
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.where("username={0}", username);
		User selectOne = userService.selectOne(wrapper);
		if (token.equals(key)) {
			String NewMobileCode = JedisClusterUtils.getString("bdnsj" + code);

			System.out.println(NewMobileCode + "呵呵");
			if (NewMobileCode.equals(code)) {
				String regex = "^((13[0-9])|(14[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
				if (Pattern.matches(regex, newMobile)) {
					EntityWrapper<User> wrapperUser = new EntityWrapper<>();
					wrapperUser.where("username={0}", username);
					User user = userService.selectOne(wrapperUser);
					if (user != null) {

						user.setMobile(newMobile);
						boolean flag = userService.updateAllColumnById(user);
						if (flag) {
							JedisClusterUtils.delKey("bdnsj" + code);
							HashMap<String, Object> map = new HashMap<>();
							map.put("mobile", newMobile);
							map.put("msg", "修改手机号码成功");
							return ResultVM.ok(map);
						} else {
							return ResultVM.error(1, "系统错误,修改手机号码失败");
						}
					} else {
						return ResultVM.error(1, "用户不存在,修改手机号码失败");
					}

				} else {

					return ResultVM.error(1, "请输入正确的手机号");
				}
			} else {
				return ResultVM.error(1, "验证码过期或者输入错误");
			}

		} else {
			return ResultVM.error(1, "token过期,修改失败");
		}

	}

	/**
	 * 修改生日 author Eddie
	 * 
	 * @throws ParseException
	 */
	@ApiOperation(value = "修改生日", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/updateBirthday")
	public ResultVM updateBirthday(@RequestParam("username") String username, @RequestParam("birthday") String birthday)
			throws ParseException {
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.where("username={0}", username);
		User user = userService.selectOne(wrapper);
		if (user != null) {

			user.setBirthday(birthday);
			boolean flag = userService.updateAllColumnById(user);
			if (flag) {
				HashMap<String, Object> map = new HashMap<>();
				map.put("message", "修改生日成功");
				map.put("birthday", user.getBirthday());
				return ResultVM.ok(map);
			} else {
				return ResultVM.error(1, "系统错误,修改生日失败");
			}
		} else {
			return ResultVM.error(1, "用户不存在,修改生日失败");
		}

	}

	/**
	 * 设置支付密码 author Eddie @RequestParam("username") String
	 * username, @RequestParam("payPwd") String payPwd, @RequestParam("code") String
	 * code
	 */
	@ApiOperation(value = "设置支付密码", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/setPayPassWord")
	@ResponseBody
	public ResultVM setPayPassWord(@RequestBody JSONObject JSONObject) {
		String username = (String) JSONObject.get("username");
		String payPwd = (String) JSONObject.get("payPwd");
		String code = (String) JSONObject.get("code");
		String szzfmmCode = JedisClusterUtils.getString("szzfmm" + username + code);
		if (szzfmmCode != null) {
			EntityWrapper<User> wrapper = new EntityWrapper<>();
			wrapper.where("username={0}", username);
			User user = userService.selectOne(wrapper);
			String md5Hex = DigestUtils.md5Hex(payPwd + regx);
			user.setPaypwd(md5Hex);
			user.setIspaypwd("1");
			if (user != null) {

				boolean flag = userService.updateById(user);
				if (flag) {
					return ResultVM.ok("设置支付密码成功");
				} else {
					return ResultVM.error(1, "系统错误,设置支付密码失败");
				}
			} else {
				return ResultVM.error(1, "用户不存在,设置支付密码失败");
			}

		} else {
			return ResultVM.error(1, "您输入的设置支付密码验证码错误,或已过期");
		}

	}

	/**
	 * 校验支付密码 author Eddie
	 */
	@ApiOperation(value = "校验支付密码 ", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/matchPayPassWord")
	public ResultVM matchPayPassWord(@RequestBody User user) {
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.where("username={0}", user.getUsername());
		User one = userService.selectOne(wrapper);
		String paypwd = one.getPaypwd();
		if (paypwd.equals(DigestUtils.md5Hex(user.getPaypwd() + regx))) {
			String md5Hex = DigestUtils.md5Hex(ValidateCodeUtils.getRandNum());
			JedisClusterUtils.saveString(user.getUsername(), md5Hex, 999);
			HashMap<String, Object> map = new HashMap<>();
			map.put("token", md5Hex);
			map.put("msg", "支付密碼检验成功");
			return ResultVM.ok(map);
		} else {
			return ResultVM.error(1, "您输入的旧支付密码不正确");
		}

	}

	/**
	 * 通过旧密码--->修改支付密码 author Eddie
	 */
	@ApiOperation(value = "通过旧密码--->修改支付密码 ", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/changPayPassWord")
	public ResultVM changPayPassWord(@RequestBody JSONObject JSONObject) {
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		String username = (String) JSONObject.get("username");
		String paypwd = (String) JSONObject.get("paypwd");
		String paypwdMd5 = DigestUtils.md5Hex(paypwd + regx);
		String token = (String) JSONObject.get("token");
		wrapper.where("username={0}", username);
		User one = userService.selectOne(wrapper);
		String key = JedisClusterUtils.getString(username);
		if (token.equals(key)) {
			if (one != null) {
				
				if(!paypwdMd5.equals(one.getPaypwd())){
					one.setPaypwd(paypwdMd5);
					boolean updateAllColumnById = userService.updateAllColumnById(one);
					if (updateAllColumnById) {
						JedisClusterUtils.delKey(username);
						return ResultVM.ok("修改支付密码成功");
					} else {
						return ResultVM.error(1, "系统错误,修改支付密码失败");
					}
				}else {
					return ResultVM.error(1, "您的新支付密码不能与旧支付密码相同");
				}
				
			} else {
				return ResultVM.error(1, "该用户不存在,修改支付密码失败");
			}
		} else {
			return ResultVM.error(1, "token验证错误");
		}

	}

	/**
	 * 校验修改密码验证码
	 * 
	 * author Eddie
	 * 
	 * @throws Exception
	 */
	@ApiOperation(value = "校验修改密码验证码 ", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/matchPassWordCode")
	public ResultVM matchPassWordCode(@RequestBody JSONObject JSONObject) throws Exception {
		String username = (String) JSONObject.get("username");
		String code = (String) JSONObject.get("code");
		String changPassWordCode = JedisClusterUtils.getString("xgmm" + username + code);
		if (changPassWordCode.equals(code)) {
			String md5Hex = DigestUtils.md5Hex(ValidateCodeUtils.getRandNum());
			JedisClusterUtils.saveString(username + "xjmmtoken", md5Hex, 999);

			HashMap<String, Object> map = new HashMap<>();
			map.put("token", md5Hex);
			map.put("msg", "忘记密码检验成功");
			return ResultVM.ok(map);

		} else {
			return ResultVM.error(1, "验证码错误或者已经过期");
		}

	}

	@ApiOperation(value = "修改密码 ", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/setPassWord")
	public ResultVM setPassWord(@RequestBody JSONObject JSONObject) {
		String username = (String) JSONObject.get("username");
		String token = (String) JSONObject.get("token");
		String newPassWord = (String) JSONObject.get("newPassWord");
		String key = JedisClusterUtils.getString(username + "xjmmtoken");
		if (key.equals(token)) {
			if(!newPassWord.matches(regx1)) {
				return ResultVM.error(1, "密码格式不对，请输入6-12位数字或字母组合的密码");
			}
			String md5Hex = DigestUtils.md5Hex(newPassWord + regx);
			EntityWrapper<User> wrapper = new EntityWrapper<>();
			wrapper.where("username={0}", username);
			User user = userService.selectOne(wrapper);
			if(!md5Hex.equals(user.getPwd())) {
				String key1 = user.getUsername() + "_" + newPassWord;
				String convert = convert(key1);
				String convert2 = convert(convert + user.getCreatetime());
				user.setKey(convert2);
				
				
				user.setPwd(md5Hex);
				boolean updateById = userService.updateById(user);
				if (updateById) {
					return ResultVM.ok("修改密码成功");
				} else {
					return ResultVM.error(1, "修改密码失败");
				}

			}else {
				return ResultVM.error(1, "您的新密码不能与旧密码相同");
			}

			
		} else {
			return ResultVM.error(1, "token验证错误");
		}

	}

	/**
	 * 通過用戶名獲得頭像 author Eddie
	 */
	@ApiOperation(value = "通過用戶名獲得頭像 ", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/getAvatar")
	public ResultVM getAvatar(@RequestBody JSONObject JSONObject) {
		String username = (String) JSONObject.get("username");
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.where("username={0}", username);
		User one = userService.selectOne(wrapper);
		if (one.getAvatar().contains("http")) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("avatar", one.getAvatar());
			map.put("username", one.getUsername());
			map.put("nickname", one.getNickname());
			map.put("msg", "获取用户头像成功");

			return ResultVM.ok(map);
		} else {
			return ResultVM.error(1, "获取用户头像失败");
		}

	}

	/**
	 * 发送解绑邮箱验证码 author Eddie
	 */
	@ApiOperation(value = "发送解绑邮箱验证码 ", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/SendRemoveEmail")
	public ResultVM SendRemoveEmail(@RequestBody JSONObject JSONObject) {
		String email = (String) JSONObject.get("email");
		String username = (String) JSONObject.get("username");
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.where("username={0}", username).andNew("email={0}", email);
		User one = userService.selectOne(wrapper);
		if (one != null) {
			String randNum = ValidateCodeUtils.getRandNum();
			jedisClusterUtils.saveString(username + "jbyx", randNum, 999);

			String title = "SGC解绑邮箱";
			String content = "尊敬的客户,您解绑邮箱的验证码为:" + randNum;
			MailUtils.sendMail(email, title, content);

			return ResultVM.ok("发送解绑邮箱验证码成功");
		} else {
			return ResultVM.error(1, "该邮箱并未与您的账号绑定");
		}

	}

	@ApiOperation(value = "解绑邮箱 ", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/RemoveEmail")
	public ResultVM RemoveEmail(@RequestBody JSONObject JSONObject) {
		String email = (String) JSONObject.get("email");
		String username = (String) JSONObject.get("username");
		String emailCode = (String) JSONObject.get("emailCode");
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.where("username={0}", username).andNew("email={0}", email);
		User one = userService.selectOne(wrapper);
		if (one != null) {
			String string = JedisClusterUtils.getString(username + "jbyx");
			if (emailCode.equals(string)) {
				
				userService.updateById(one);
				return ResultVM.ok("解绑成功");
			} else {
				return ResultVM.error(1, "发送解绑邮箱验证码成功");
			}
		} else {
			return ResultVM.error(1, "解绑失败");
		}

	}

	/**
	 * 绑定新邮箱author Eddie
	 */
	@ApiOperation(value = "绑定新邮箱 ", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/NewEmail")
	public ResultVM NewEmail(@RequestBody JSONObject JSONObject) {
		
		String newemail = (String) JSONObject.get("newemail");
		String username = (String) JSONObject.get("username");
		String newEmailCode = (String) JSONObject.get("newEmailCode");
		System.out.println(newemail);
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.where("email={0}", newemail);
		User one = userService.selectOne(wrapper);
		if (one == null) {
			String string = JedisClusterUtils.getString(username + "bdyx");
			if (newEmailCode.equals(string)) {
				EntityWrapper<User> wrapper1 = new EntityWrapper<>();
				wrapper1.where("username={0}", username);
				User one1 = userService.selectOne(wrapper1);
				one1.setEmail(newemail);
				one1.setAuth("1");
				userService.updateById(one1);
				HashMap<String, Object> map = new HashMap<>();
				map.put("msg", "绑定邮箱成功");
				map.put("email", one1.getEmail());
				return ResultVM.ok(map);
			} else {
				return ResultVM.error(1, "验证码错误或过期");
			}

		} else {
			return ResultVM.error(1, "该邮箱已被占用绑定");
		}

	}

	/**
	 * 发送绑定新邮箱验证码 author Eddie
	 */
	@ApiOperation(value = "发送绑定新邮箱验证码 ", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/SendNewEmail")
	public ResultVM SendNewEmail(@RequestBody JSONObject JSONObject) {
		String newemail = (String) JSONObject.get("newemail");
		String username = (String) JSONObject.get("username");
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.where("email={0}", newemail);
		User one = userService.selectOne(wrapper);
		if (one == null) {
			String randNum = ValidateCodeUtils.getRandNum();
			jedisClusterUtils.saveString(username + "bdyx", randNum, 999);

			String title = "SGC绑定邮箱";
			String content = "尊敬的客户,您绑定邮箱的验证码为:" + randNum;
			MailUtils.sendMail(newemail, title, content);

			return ResultVM.ok("发送绑定邮箱验证码成功");
		} else {
			return ResultVM.error(1, "该邮箱已被占用绑定");
		}

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
