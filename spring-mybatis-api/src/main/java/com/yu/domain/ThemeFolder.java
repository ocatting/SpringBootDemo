package com.yu.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 文件列表
 * @Author Yan XinYu
 **/
@Data
public class ThemeFolder {

    // 文件夹ID
    private String id;
    // 文件夹名称
    private String name;
    // 备注
    private String remark;
    // 接口列表
    private List<String> urlStores;
    // 文件夹列表
    private List<String> themeFolders;

    /**
     * 增加子文件夹
     * @param id
     */
    public void addThemeFolder(String id){
        if(themeFolders == null){
            themeFolders = new ArrayList<>();
        }
        themeFolders.add(id);
    }

    /**
     * 增加子SQLSchema 项目
     * @param id
     */
    public void addUrlStores(String id){
        if(urlStores == null){
            urlStores = new ArrayList<>();
        }
        urlStores.add(id);
    }

}
