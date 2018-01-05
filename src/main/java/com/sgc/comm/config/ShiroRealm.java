package com.sgc.comm.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sgc.domain.User;
import com.sgc.app.service.UserService;


@Component
public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;
	/**
	 * @Author : Wvv
	 * @Description : 授权(验证权限时调用)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//		SysUser user = (SysUser) principalCollection.getPrimaryPrincipal();
//		String userId = user.getId();
//
//		// 用户权限列表
//		Set<String> permsSet = sysMenuService.getPermissions(userId);
//
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//		info.setStringPermissions(permsSet);
		return info;
	}

	/**
	 * @Author : Wvv
	 * @Description : 认证(登录时调用)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		String username = (String) authenticationToken.getPrincipal();
		String password = new String((char[]) authenticationToken.getCredentials());

		// 查询用户信息
		EntityWrapper entityWrapper = new EntityWrapper<>();
		entityWrapper.where("username={0}", username);
		User user = userService.selectOne(entityWrapper);
//		SysUser; user = sysUserService.findByUserName(username);

		// 账号不存在
		if (user == null) {
			throw new UnknownAccountException("用户名不正确");
		}

		// 密码错误
		if (!password.equals(user.getPwd())) {
			throw new IncorrectCredentialsException("用户名或密码不正确");
		}

		// 账号禁用
//		if ("0".equals(user.getStatus())) {
//			throw new LockedAccountException("用户已被禁用,请联系管理员");
//		}

		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
		return info;
	}
}
