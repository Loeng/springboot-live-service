package com.sgc.app.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.app.service.AccountService;
import com.sgc.app.service.TimRest;
import com.sgc.domain.Entity.Account;
import com.sgc.domain.mapper.AccountMapper;
import com.sgc.domain.vm.ResultVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Path;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	TimRest timRest;
	@Value("{yzb.sdkappid}")
	private String sdkappid;

	@Value("{yzb.identifier}")
	private String identifier;

	private String regx = "5b77e34ff78f3b53";
	/**
	 * 注册用户
	 */
	@Transactional
	public ResultVM registerAccount(Account account) {
        boolean ret = this.insert(account);
        if(ret) {
        	timRest.init(sdkappid,identifier);
        	if(timRest.is_64bit()){
				if(File.pathSeparator == "/"){
					String signature = IM_PATH."/signature/linux-signature64";
				}else {
					String signature = IM_PATH."\\signature\\windows-signature64.exe";
				}
			}else{
				if(File.pathSeparator == "/"){
					String signature = IM_PATH."/signature/linux-signature32";
				}else {
					String signature = IM_PATH."\\signature\\windows-signature32.exe";
				}
			}
            return ResultVM.error(0, "");
        }
        return  ResultVM.error(1,"register a account error,try again...");
	}

	protected boolean importAccount(){

	}
	
}
