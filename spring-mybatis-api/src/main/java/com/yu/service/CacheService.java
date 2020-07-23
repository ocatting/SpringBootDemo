package com.yu.service;

import com.yu.config.MappingConfiguration;
import com.yu.domain.ThemeFolder;
import com.yu.domain.UrlStore;

import java.util.List;
import java.util.UUID;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class CacheService extends XmlParerService implements Service {

    private Service delegate;

    // 是否持久化
    private boolean isPersistence = true;

    public CacheService(MappingConfiguration mappingConfiguration){
        super(mappingConfiguration);
        this.delegate = new DiskService(mappingConfiguration);
    }

    public void setService(Service delegate){
        this.delegate = delegate;
    }

    public void register(String parentId ,UrlStore urlStore) {
        super.register(parentId,urlStore);
    }

    public void register(String folderId , ThemeFolder subThemeFolder) {
        super.register(folderId,subThemeFolder);
    }

    @Override
    public void register(UrlStore urlStore) {
        String id = UUID.randomUUID().toString();
        urlStore.setId(id);
        // id url:sql
        super.register(urlStore);
        if(isPersistence){
            delegate.register(urlStore);
        }
    }

    @Override
    public UrlStore selectUri(String uri) {
        return super.selectUri(uri);
    }
}
