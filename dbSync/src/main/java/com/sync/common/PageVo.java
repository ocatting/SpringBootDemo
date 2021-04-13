package com.sync.common;

import lombok.Data;

import java.util.List;

/**
 * @Description: 分页
 * @Author: Yan XinYu
 * @Date: 2021-03-17 22:30
 */
@Data
public class PageVo<T> implements java.io.Serializable {

    private int draw;
    private long recordsTotal;
    private long recordsFiltered;
    private List<T> data;
    private String error;

    public PageVo(long recordsTotal, long recordsFiltered, List<T> data) {
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
    }

    public static <T> PageVo<T> create(List<T> data, long total, long filtered){
        return new PageVo<>(total,filtered,data);
    }
}
