package com.sgc.comm.filter;

import lombok.Data;
import lombok.Value;

import java.util.List;

//辅助过滤器类
@Data
public class Filter {
    private List groupBaseInfoFilter;
    private List memberInfoFilter;
    private List appDefinedDataFilter_Group;
    private List SelfInfoFilter;

}
