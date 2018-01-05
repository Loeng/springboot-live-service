package com.sgc.app.controller;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sgc.comm.util.GUIDHelper;
import com.sgc.domain.Dynamic;
import com.sgc.domain.User;
import com.sgc.domain.vm.DynamicVM;
import com.sgc.domain.vm.ResultVM;
import com.sgc.app.service.DynamicService;
import com.sgc.app.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import main.java.com.UpYun;

@RestController("appDynamic")
@RequestMapping("/app/dynamic")
@Api(value = "app - Dynamic")
public class DynamicController {
    @Autowired
    private DynamicService dynamicService;
    @Autowired
    private UserService userService;
    UpYun upyun = new UpYun(UpyunController.BUCKET_NAME, UpyunController.OPERATOR_NAME, UpyunController.OPERATOR_PWD);


    /**
     * 已通
     * 发布动态,,
     *
     * @param jsonObject
     * @param pictureFile ,MultipartRequest pictureFile
     * @return ResultVM
     */
    @ApiOperation(httpMethod = "POST", response = ResultVM.class, value = "发布动态")
    @PostMapping("/sendDynamic")
    public ResultVM sendDynamic(@RequestBody JSONObject jsonObject) {
        String userid = (String) jsonObject.get("userid");
        System.out.println(userid);
        String content = (String) jsonObject.get("content");
        System.out.println(content);
        long parseLong = Long.parseLong(userid);
        Dynamic dynamic = new Dynamic();
        dynamic.setUserid(parseLong);

        dynamic.setContent(content);
		/*String imageUrl = "";
		if (null != pictureFile) {
			System.out.println(pictureFile);
			Iterator<String> fileNames = pictureFile.getFileNames();
			while (fileNames.hasNext()) {
				MultipartFile multipartFile = pictureFile.getFile(fileNames.next());
				String oriName = multipartFile.getOriginalFilename();
				System.out.println(oriName);
				String newName = GUIDHelper.genRandomGUID() + ".jpg";
				System.out.println(newName);
				try {
					String filePath = UpyunController.DIR_ROOT + newName;
					File file = new File(System.getProperty("user.dir") + filePath);
					InputStream inputStream = multipartFile.getInputStream();
					BufferedInputStream in = new BufferedInputStream(inputStream);
					BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
					byte[] buffer = new byte[1024*8];
					int len = -1;
					while ((len = in.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}
					in.close();
					out.close();
					upyun.setTimeout(120);
					upyun.setContentMD5(UpYun.md5(file));
					upyun.setFileSecret(UpyunController.IPCPWD);
					// 上传文件，并自动创建父级目录（最多10级）
					System.out.println("上传又拍云之前");
					boolean result = upyun.writeFile(filePath, file, true);
					System.out.println("上传又拍云之后");
					assertTrue(result);
					String width = upyun.getPicWidth();
					String height = upyun.getPicHeight();
					String frames = upyun.getPicFrames();
					String type = upyun.getPicType();
					assertTrue(width != null && !"".equals(width));
					assertTrue(height != null && !"".equals(height));
					assertTrue(frames != null && !"".equals(frames));
					assertTrue(type != null && !"".equals(type));
					System.out.println("删除文件之前");
					FileUtils.deleteQuietly(file);
					System.out.println("删除文件之后");
					imageUrl+=newName+",";
				} catch (Exception e) {
					e.printStackTrace();
					return ResultVM.error();
				}
			}
			dynamic.setUrl(imageUrl);
		}*/


        dynamic.setId(GUIDHelper.genRandomGUID());
        dynamic.setTime(new Date().getTime() / 1000);
        return (dynamicService.insert(dynamic)) ? ResultVM.ok("发布动态成\n" +
                "\t功") : ResultVM.error("发布动态失败");
    }

    /**
     * 分页查询动态
     * author Eddie
     */
    @ApiOperation(value = "分页查询动态", httpMethod = "GET", response = ResultVM.class)
    @GetMapping("/getDynamic/{page}")
    public ResultVM getDynamic(@PathVariable("page") Integer page) {
        System.out.println(page);
        ArrayList<DynamicVM> list = new ArrayList<DynamicVM>();
        try {
            EntityWrapper<Dynamic> entityWrapper = new EntityWrapper<Dynamic>();
            entityWrapper.orderBy("time", false);
            Page<Dynamic> selectPage = dynamicService.selectPage(new Page<>(page, 20), entityWrapper);

            List<Dynamic> records = selectPage.getRecords();
            for (Dynamic dynamic : records) {
                User user = userService.selectById(dynamic.getUserid());
                DynamicVM dynamicVM = new DynamicVM();
                dynamicVM.setNickname(user.getNickname());
                dynamicVM.setId(dynamic.getId());
                dynamicVM.setAvatar(user.getAvatar());
                dynamicVM.setTime(dynamic.getTime());
                dynamicVM.setContent(dynamic.getContent());
                dynamicVM.setUrl(dynamic.getUrl());
                System.out.println(dynamicVM);
                list.add(dynamicVM);
            }

            return ResultVM.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVM.error(1, "分页查询动态失败");
        }

    }


}









