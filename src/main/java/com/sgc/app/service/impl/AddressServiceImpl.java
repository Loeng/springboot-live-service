package com.sgc.app.controller.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sgc.domain.Address;
import com.sgc.domain.mapper.AddressMapper;
import com.sgc.domain.vm.ResultVM;
import com.sgc.app.controller.service.AddressService;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {
	@Autowired
	private AddressMapper addressMapper;

	/**
	 * 添加新收货地址,为默认收货地址
	 */
	@Override
	public ResultVM addAddress(Address address) {
		
		
	
			EntityWrapper<Address> wrapper = new EntityWrapper<>();
			wrapper.where("username={0}", address.getUsername());
			List<Address> selectList = addressMapper.selectList(wrapper);
			for (Address address2 : selectList) {
				address2.setIsdefault(1);
				addressMapper.updateById(address2);
				
			}
			Integer insert = addressMapper.insert(address);
			if (insert == 1) {
				return ResultVM.ok("新增收货地址成功");
			} else {
				return ResultVM.error(1, "新增收货地址失败");
			}

		
	
	}

	/**
	 * 修改收货地址,为默认收货地址
	 */
	@Override
	public ResultVM updateAddress(Address address) {
		System.out.println(address.getMobile());
		EntityWrapper<Address> wrapper = new EntityWrapper<>();
		wrapper.where("username={0}", address.getUsername());
		List<Address> selectList = addressMapper.selectList(wrapper);
		for (Address address2 : selectList) {
			if (address2.getId() != address.getId()) {
				address2.setIsdefault(1);
				addressMapper.updateById(address2);                        
			}

		}
		Integer updateById = addressMapper.updateAllColumnById(address);
		if (updateById == 1) {
			return ResultVM.ok("修改收货地址成功");
		} else {
			return ResultVM.error(1, "修改收货地址失败");
		}
	}

}
