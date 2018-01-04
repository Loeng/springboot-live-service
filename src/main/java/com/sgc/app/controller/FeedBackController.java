package com.sgc.app.controller;

import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.sgc.comm.util.GUIDHelper;
import com.sgc.domain.FeedBack;
import com.sgc.domain.vm.ResultVM;
import com.sgc.app.service.FeedBackService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import main.java.com.UpYun;

@RestController("appFeedBack")
@RequestMapping("/app/feedBack")
@Api(value = "app - FeedBack")
public class FeedBackController {
	@Autowired
	private FeedBackService feedBackService;
	UpYun upyun = new UpYun(UpyunController.BUCKET_NAME, UpyunController.OPERATOR_NAME, UpyunController.OPERATOR_PWD);
	

		/**
		 * 已通
		 * 传用户名,反馈意见,mapfile,
		 * @param shopFeedback
		 * @return
		 */
		@ApiOperation(httpMethod="POST",response=ResultVM.class,value="意见反馈")
		@PostMapping("/addFeedBack")
		public ResultVM saveShopfeedbac(FeedBack feedBack,MultipartRequest pictureFile) {
			String imageUrl = "";
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
				feedBack.setImageUrl(imageUrl);
			}
			feedBack.setCreatetime(new Date());
			return (feedBackService.insert(feedBack)) ? ResultVM.ok("反馈成功") : ResultVM.error("反馈失败");
		};
	}