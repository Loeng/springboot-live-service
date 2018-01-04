package com.sgc.app.service;

import com.baomidou.mybatisplus.service.IService;
import com.sgc.domain.Entity.Account;
import com.sgc.domain.vm.ResultVM;

public interface AccountService extends IService<Account> {

	ResultVM registerAccount(Account account);
}
