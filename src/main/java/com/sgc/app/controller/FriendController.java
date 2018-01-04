package com.sgc.app.controller;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sgc.api.huanxin.impl.EasemobIMUsers;
import com.sgc.comm.util.GUIDHelper;
import com.sgc.domain.Friend;
import com.sgc.domain.RemarksFriend;
import com.sgc.domain.User;
import com.sgc.domain.vm.ResultVM;
import com.sgc.app.service.BlackListService;
import com.sgc.app.service.FriendService;
import com.sgc.app.service.RemarksFriendService;
import com.sgc.app.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author 邱绎玮
 */
@RestController("appFriend")
@RequestMapping("/app/friend")
@Api(value = "app - Friend")
public class FriendController {
	@Autowired
	private FriendService friendService;
//	@Autowired
//	private FriendApplyService friendApplyService;
	@Autowired
	private UserService userService;
	private EasemobIMUsers easemobIMUsers = new EasemobIMUsers();
	@Autowired
	private RemarksFriendService remarksFriendService;
	@Autowired
	private BlackListService blackListService;

	/**
	 * 搜索用户 username 用户名 
	 * author Eddie
	 */
	@ApiOperation(value = "搜索用户", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/searchFriend")
	public ResultVM searchFriend(@RequestParam("username") String username) {
		EntityWrapper<User> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("username={0}", username);
		User user = userService.selectOne(entityWrapper);
		if (user != null) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("avatar", user.getAvatar());
			map.put("nickname", user.getNickname());
			map.put("province", user.getProvince());
			map.put("city", user.getCity());
			map.put("gender", user.getGender());
			return ResultVM.ok(map);
		} else {
			return ResultVM.error(1, "用户不存在");
		}

	}

	/**
	 * 发送添加好友申请 username 申请人 friendname 被申请人 content 内容 type//0:好友 1:等待验证 2:拒绝通过
	 * 3:拉黑
	 */
	@ApiOperation(value = "发送添加好友申请", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/sendFriend")
	public ResultVM sendFriend(Friend friend) {
		friend.setId(GUIDHelper.genRandomGUID());
		friend.setCreattime(new Date());
		friend.setType("1");
		boolean insert = friendService.insert(friend);
		if (insert) {
			return ResultVM.ok("发送成功");
		} else {
			return ResultVM.error(1, "发送添加好友申请失败");
		}

	}

	/**
	 * 添加好友申请
	 * username 申请人 
	 * friendname 被申请人
	 * 
	 */
	@ApiOperation(value = "添加好友", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/addFriend")
	public ResultVM addFriend(Friend friend) {
		EntityWrapper<Friend> entityWrapper = new EntityWrapper<>();
		entityWrapper.where("username={0}", friend.getUsername()).andNew("friendname={0}", friend.getFriendname());
		Friend searchfriend = friendService.selectOne(entityWrapper);
		if (searchfriend != null) {
			Object result = easemobIMUsers.addFriendSingle(friend.getUsername(), friend.getFriendname());
			if (result != null) {
				searchfriend.setType("0"); // 0:好友 1:等待验证 2:拒绝通过
				boolean updateById = friendService.updateById(searchfriend);
				if (updateById) {
					EntityWrapper<User> entity = new EntityWrapper<>();
					entity.where("username={0}", friend.getUsername());
					User selectOne = userService.selectOne(entity);
					HashMap<String, Object> map = new HashMap<>();
//					map.put("content", searchfriend.getContent());// 添加好友内容
//					map.put("isScreen", 1);// 是否对他屏蔽我的动态 0是 1否
//					map.put("nickname", selectOne.getNickname());// 昵称

					return ResultVM.ok(map);
				} else {
					return ResultVM.error(1, "添加好友失败");
				}
			} else {
				return ResultVM.error(1, "环信添加好友失败");
			}

		} else {
			return ResultVM.error(1, "添加好友记录不存在");
		}

	}

	/**
	 * 通过验证 ---->设置是否屏蔽动态 设置备注 author Eddie
	 */
	@ApiOperation(value = "设置是否屏蔽动态  设置备注", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/setRemarksFriend")
	public ResultVM setRemarksFriend(RemarksFriend remarksFriend) {

		remarksFriend.setId(GUIDHelper.genRandomGUID());

		boolean insert = remarksFriendService.insert(remarksFriend);
		if (insert) {
			return ResultVM.ok("设置成功");

		} else {

			return ResultVM.error(1, "设置失败");
		}

	}

	/**
	 * 
	 * 加入黑名单 username 用户名 friendname 被添加入黑名单数组 单人拉黑或者多人拉黑
	 * 
	 */
	@ApiOperation(value = "添加黑名单", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/addBlackList")
	public ResultVM addBlackList(
			@RequestParam("usernmae") String usernmae,
			@RequestParam("blacknames") String[] blacknames) {
			
				return null;

	}

}
