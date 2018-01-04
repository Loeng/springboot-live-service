package com.sgc.comm.util;

import java.util.List;

public class UserGXEcharts {
	/*{
        "name": "flare",
        "children": [
          {
            "name": "flex",
            "children": [
              {"name": "FlareVis", "value": 4116}
            ]
          },
          {
            "name": "scale",
            "children": [
              {"name": "IScaleMap", "value": 2105},
              {"name": "LinearScale", "value": 1316},
              {"name": "LogScale", "value": 3151},
              {"name": "OrdinalScale", "value": 3770},
              {"name": "QuantileScale", "value": 2435},
              {"name": "QuantitativeScale", "value": 4839},
              {"name": "RootScale", "value": 1756},
              {"name": "Scale", "value": 4268},
              {"name": "ScaleType", "value": 1821},
              {"name": "TimeScale", "value": 5833}
            ]
          },
          {
            "name": "display",
            "children": [
              {"name": "DirtySprite", "value": 8833}
            ]
          }
        ]
      }*/
	private String name;//页面展示的字段
	private String value;
	private boolean collapsed = false;
	private List<UserGXEcharts> children;//下一个节点
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<UserGXEcharts> getChildren() {
		return children;
	}
	public void setChildren(List<UserGXEcharts> children) {
		this.children = children;
	}
	public String toString() {
		return "UserGXEcharts [name=" + name + ", value=" + value + ", children=" + children + "]";
	}
	public boolean isCollapsed() {
		return collapsed;
	}
	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}
	
}
