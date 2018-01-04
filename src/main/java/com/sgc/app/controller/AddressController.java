package com.sgc.app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.sgc.domain.Address;
import com.sgc.domain.vm.ResultVM;
import com.sgc.app.service.AddressService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Eddie
 */
@RestController("appAddress")
@RequestMapping("/app/address")
@Api(value = "app - Address")
public class AddressController {
	@Autowired
	private AddressService addressService;

	/**
	 * 
	 * 新增收货地址 
	 * author Eddie
	 * 已通
	 */
	@ApiOperation(value = "新增收货地址 ", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/addAddress")
	public ResultVM addAddress( @RequestBody Address address) {
		//根据用户名获得对应的地址数量
		EntityWrapper<Address> addressWrapper = new EntityWrapper<>();
		addressWrapper.where("username={0}", address.getUsername());
		Integer selectCount = addressService.selectCount(addressWrapper);
		//判断地址数量是否>10条
		if (selectCount < 10) {
			address.setId(new Date().getTime()/1000);
			//  判断新增地址是否为默认的地址
			if (address.getIsdefault() == 0||selectCount==0) {
				address.setIsdefault(0);
				return addressService.addAddress(address);
			} else {
				boolean insert = addressService.insert(address);
				if (insert) {
					return ResultVM.ok("新增收货地址成功");
					
				} else {
					return ResultVM.error(1, "新增收货地址失败");
				}

			}
			//大于10条则不允许新增
		} else {
			return ResultVM.error(1, "收货地址不能超过10条");
		}
	}

	/**
	 * 修改收货地址 
	 * author Eddie
	 * 已通
	 */
	@ApiOperation(value = "修改收货地址", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/updateAddress")
	public ResultVM updateAddress(@RequestBody Address address) {
		System.out.println(address);
		//判断修改地址是否为默认地址
		if (address.getIsdefault() == 0) {

			return addressService.updateAddress(address);
		} else {

			boolean insert = addressService.updateById(address);
			if (insert) {
				EntityWrapper<Address> wrapper = new EntityWrapper<>();
				wrapper.where("id!={0}", address.getId()).andNew("username={0}", address.getUsername());
				List<Address> selectList = addressService.selectList(wrapper);
				System.out.println(selectList);
				Address address2 = selectList.get(0);
				address2.setIsdefault(0);
				addressService.updateById(address2);
				return ResultVM.ok("修改收货地址成功");
			} else {
				return ResultVM.error(1, "修改收货地址失败");
			}

		}

	}

	/**
	 * 查询收货地址 
	 * author Eddie
	 * 已通
	 */
	@ApiOperation(value = "查询收货地址", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/selectAddressByUserName")
	public ResultVM selectAddressByUserName( @RequestBody Address address) {
		System.out.println("地址进来了");
		EntityWrapper<Address> wrapper = new EntityWrapper<>();
		wrapper.where("username={0}", address.getUsername()).andNew("isdefault={0}", 0);
		Address one = addressService.selectOne(wrapper);
		EntityWrapper<Address> wrapperTwo = new EntityWrapper<>();
		wrapperTwo.where("username={0}",  address.getUsername()).andNew("isdefault!={0}", 0);
		List<Address> selectList = addressService.selectList(wrapperTwo);
		ArrayList<Address> list = new ArrayList<>();
		list.add(one);// 将默认地址存在0索引中
		if (selectList != null && selectList.size() != 0) {

			for (Address address1 : selectList) {
				list.add(address1);
			}
		}
		HashMap<String, Object> result = new HashMap<>();
		result.put("AddressList", list);
		result.put("message", "查询收货地址成功");
		return ResultVM.ok(result);

	}

	/**
	 * 删除收货地址 
	 * author Eddie
	 * 已通
	 */
	@ApiOperation(value = "删除收货地址", httpMethod = "POST", response = ResultVM.class)
	@PostMapping("/deleteAddressById")
	public ResultVM deleteAddressById(@RequestBody Address address) {
		System.out.println("删除进来了");
		EntityWrapper<Address> wrapper = new EntityWrapper<>();
		wrapper.where("id={0}", address.getId());
		Address one = addressService.selectOne(wrapper);
		if (one.getIsdefault() == 0) {
			addressService.deleteById(one);

			EntityWrapper<Address> addressWrapper = new EntityWrapper<>();
			addressWrapper.where("username={0}", address.getUsername());
			List<Address> selectList = addressService.selectList(addressWrapper);
			Address address1 = selectList.get(0);
			address1.setIsdefault(0);
			addressService.updateById(address1);
			return ResultVM.ok("删除地址成功");

		} else {
			addressService.deleteById(one);
			return ResultVM.ok("删除地址成功");
		}

	}

}
