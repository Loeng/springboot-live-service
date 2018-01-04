package com.sgc.app.service;

import com.baomidou.mybatisplus.service.IService;
import com.sgc.domain.Address;
import com.sgc.domain.vm.ResultVM;

public interface AddressService extends IService<Address> {

	ResultVM addAddress(Address address);

	ResultVM updateAddress(Address address);


}
