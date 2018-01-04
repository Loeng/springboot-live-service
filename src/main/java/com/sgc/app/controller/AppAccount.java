package com.sgc.app.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sgc.app.service.AccountService;
import com.sgc.domain.Entity.Account;
import com.sgc.domain.vm.RegRequestData;
import com.sgc.domain.vm.ResultVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("appAccount")
@RequestMapping("/account")
public class AppAccount {
    @Autowired
    private AccountService accountService;

    @PostMapping("/reg")
    public ResultVM regAppUser(@RequestBody RegRequestData req){
        String uid = req.getUid();
        String pass = req.getPass();
        //参数判断
        if (uid==null || uid.isEmpty()){
            return ResultVM.error(1,"账号为空");
        }
        if(pass==null || pass.isEmpty()){
            return ResultVM.error(1,"密码为空");
        }

        //账号重复判断
        EntityWrapper<Account> entityWrapper = new EntityWrapper<Account>();
        entityWrapper.where("uid={0}",uid);

        int count = accountService.selectCount(entityWrapper);
        if(count>0){
            return ResultVM.error(1,"账号已经存在!");
        }

        Account account =  new Account();
        account.setUid(uid);
        account.setPwd(pass);
        ResultVM ret = accountService.registerAccount(account);
        return  ret;
    }
}
