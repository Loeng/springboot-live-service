package com.sgc.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.app.service.AccountService;
import com.sgc.app.service.TimRest;
import com.sgc.domain.Entity.Account;
import com.sgc.domain.mapper.AccountMapper;
import com.sgc.domain.vm.ResultVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Service
@Slf4j
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	TimRest timRest;
	@Value("{yzb.sdkappid}")
	private String sdkappid;

	@Value("{yzb.identifier}")
	private String identifier;

	@Value("{yzb.im_path}")
	private String IM_PATH;

	@Value("yzb.private_key_path")
	private String privateKeyPath;


	private String regx = "5b77e34ff78f3b53";
	/**
	 * 注册用户
	 */
	@Transactional
	public ResultVM registerAccount(Account account) {
		String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
		System.out.println("Path:"+path);
        boolean ret = this.insert(account);
        if(ret) {
        	timRest.init(sdkappid,identifier);
			String signature = null;
			if(timRest.is_64bit()){
				if(File.pathSeparator == "/"){
					signature = IM_PATH+"/signature/linux-signature64";
				}else {
					signature = IM_PATH+"\\signature\\windows-signature64.exe";
				}
			}else{
				if(File.pathSeparator == "/"){
					signature = IM_PATH+"/signature/linux-signature32";
				}else {
					signature = IM_PATH+"\\signature\\windows-signature32.exe";
				}
			}

			String userSig = timRest.generate_user_sig(identifier,"36000",privateKeyPath,signature);
			List res = timRest.account_import(identifier,account.getUid(),"");
			System.out.println("userSig:"+userSig);
			System.out.println(res);
            return ResultVM.error(0, "");
        }
        return  ResultVM.error(1,"register a account error,try again...");
	}
	
}
