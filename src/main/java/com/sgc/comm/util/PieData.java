package com.sgc.comm.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PieData implements Serializable{

	private static final long serialVersionUID = 8362205175983260549L;
	
	private Map<String,Object> title;//报表的名字
	
	private Map<String,Object> tooltip;//嵌入的显示
	
	private Map<String,Object> legend;//左边的显示
	
	private List<Map<String,Object>> series;//数据显示
	
}
