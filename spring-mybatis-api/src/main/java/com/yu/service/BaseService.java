package com.yu.service;

import com.yu.builder.SqlSource;
import com.yu.config.MappingConfiguration;
import com.yu.domain.ThemeFolder;
import com.yu.domain.UrlSqlSource;
import com.yu.domain.UrlStore;

import java.util.UUID;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public abstract class BaseService implements Service {

    private MappingConfiguration mappingConfiguration;

    private final String root_Folder = "$RootFolder";

    public BaseService (MappingConfiguration mappingConfiguration){
        this.mappingConfiguration = mappingConfiguration;
    }

    /**
     * 注册文件夹
     * @param parentId 父文件夹ID
     * @param subThemeFolder
     */
    public void register(String parentId , ThemeFolder subThemeFolder) {
        if( parentId==null||parentId.isEmpty()){
            parentId = root_Folder;
        }
        ThemeFolder themeFolder = mappingConfiguration.getThemeFolder(parentId);
        if(themeFolder == null){
            throw new RuntimeException("找不到父目录");
        }
        String id = UUID.randomUUID().toString();
        themeFolder.addThemeFolder(id);
        subThemeFolder.setId(id);
        mappingConfiguration.addThemeFolder(id,subThemeFolder);
    }

    /**
     * 注册Store
     * @param folderId 文件夹ID
     * @param subUrlStore
     */
    public void register(String folderId , UrlStore subUrlStore){
        if( folderId==null||folderId.isEmpty()){
            folderId = root_Folder;
        }
        ThemeFolder themeFolder = mappingConfiguration.getThemeFolder(folderId);
        if(themeFolder == null){
            throw new RuntimeException("找不到文件夹目录");
        }
        String id = UUID.randomUUID().toString();
        themeFolder.addUrlStores(id);
        subUrlStore.setId(id);
        mappingConfiguration.put(id,subUrlStore);
        // URI 映射 UrlStore
        mappingConfiguration.addUri(subUrlStore.getUri(),id);
    }

    /**
     * 注册解析后的SQL
     * @param sqlSource
     */
    void register(String urlStoreId,SqlSource sqlSource){
        if(urlStoreId==null||urlStoreId.isEmpty()){
            throw new RuntimeException("urlSqlSourceId 指向不为空");
        }
        UrlStore urlStore = mappingConfiguration.get(urlStoreId);

        String id = UUID.randomUUID().toString();
        urlStore.setUrlSqlSourceId(id);
        UrlSqlSource urlSqlSource = new UrlSqlSource();
        urlSqlSource.setId(id);
        urlSqlSource.setSqlSource(sqlSource);
        mappingConfiguration.addSqlMapper(id,urlSqlSource);
    }

    UrlStore selectUri(String uri){
        String id = mappingConfiguration.getUri(uri);
        return mappingConfiguration.get(id);
    }

}
