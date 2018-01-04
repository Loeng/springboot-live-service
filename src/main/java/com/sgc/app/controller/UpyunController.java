package com.sgc.app.controller;

import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sgc.api.huanxin.impl.EasemobIMUsers;
import com.sgc.comm.util.GUIDHelper;
import com.sgc.domain.User;
import com.sgc.domain.vm.ResultVM;
import com.sgc.app.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.client.model.Nickname;
import main.java.com.UpYun;

/**
 * @author 邱绎玮
 */
@RestController("appUpyun")
@RequestMapping("/app/upyun")
@Api(value = "app - Upyun")
public class UpyunController {
	@Autowired
	private UserService userService;
	 public static final String BUCKET_NAME = "kjz-avator-upyun";
	 public static final String OPERATOR_NAME = "kjzavator";
	 public static final String OPERATOR_PWD = "upyunimage2018";
	 public static final String DIR_ROOT = "/";
	 public static final String DOMAIN_NAME = "kjz-avator-upyun.test.upcdn.net";// 测试域名
	 public static final String IPCPWD = "";// 后缀密码
	UpYun upyun = new UpYun(BUCKET_NAME, OPERATOR_NAME, OPERATOR_PWD);
	private EasemobIMUsers easemobIMUsers = new EasemobIMUsers();
	
	
	/**
	 *修改用户信息
	 */
	@ApiOperation(value = "修改用户信息", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/setUserInfo")
	public ResultVM setUserInfo(User user , MultipartFile pictureFile) {
	if(user.getNickname().trim()==null||user.getNickname().replaceAll(" ","")==""){
		return  ResultVM.error(1, "昵称不能为空");
	}
		Map<String, Object> map = new HashMap<String, Object>();
		if (pictureFile != null) {
			String oriName = pictureFile.getOriginalFilename();
			System.out.println(oriName);
			String md5Hex = DigestUtils.md5Hex(user.getUsername() + user.getUsername());
			String newName = GUIDHelper.genRandomGUID() + ".jpg";
			System.out.println(newName);
			try {
				String filePath = DIR_ROOT + newName;
				File file = new File(System.getProperty("user.dir") + filePath);
				InputStream inputStream = pictureFile.getInputStream();
				BufferedInputStream in = new BufferedInputStream(inputStream);
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
				byte[] buffer = new byte[1024];
				int len = -1;
				while ((len = in.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
				in.close();
				out.close();

				upyun.setTimeout(2000);
				upyun.setContentMD5(UpYun.md5(file));
				upyun.setFileSecret(IPCPWD);

				// 上传文件，并自动创建父级目录（最多10级）
				boolean result = upyun.writeFile(filePath, file, true);
				assertTrue(result);
				String width = upyun.getPicWidth();
				String height = upyun.getPicHeight();
				String frames = upyun.getPicFrames();
				String type = upyun.getPicType();
				assertTrue(width != null && !"".equals(width));
				assertTrue(height != null && !"".equals(height));
				assertTrue(frames != null && !"".equals(frames));
				assertTrue(type != null && !"".equals(type));
				EntityWrapper<User> entityWrapper = new EntityWrapper<User>();
				entityWrapper.where("username={0}", user.getUsername());
				User one = userService.selectOne(entityWrapper);
				one.setAvatar("http://kjz-avator-upyun.test.upcdn.net" + filePath);
				one.setBirthday(user.getBirthday());
				one.setGender(user.getGender());
				one.setNickname(user.getNickname());
				Nickname nickname = new Nickname();
				nickname.setNickname(user.getNickname());
				Object modifyIMUserNickNameWithAdminToken = easemobIMUsers.modifyIMUserNickNameWithAdminToken(user.getUsername(), nickname);
				boolean updateById = userService.updateById(one);
				if (updateById) {
					map.put("user",one);
					map.put("msg", "修改用户信息成功");
					FileUtils.deleteQuietly(file);
					return ResultVM.ok(map);

				} else {
					return ResultVM.error(1, "修改用户信息失败");
				}

			} catch (Exception e) {
				e.printStackTrace();
				return ResultVM.error(1, "系统错误");
			}
		}
		return  ResultVM.error(1, "上传图片不能为空");
	}

	@GetMapping("/goPic")
	public String goPic() {
		System.out.println("j");
		return "picUpload";
	}
}
