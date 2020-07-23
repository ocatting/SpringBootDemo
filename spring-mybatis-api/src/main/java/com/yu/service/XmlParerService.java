package com.yu.service;

import com.yu.builder.SqlSource;
import com.yu.builder.XMLScriptBuilder;
import com.yu.config.MappingConfiguration;
import com.yu.domain.UrlStore;
import com.yu.parsing.SNode;
import com.yu.parsing.ScriptParser;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class XmlParerService extends BaseService {

    public XmlParerService(MappingConfiguration mappingConfiguration) {
        super(mappingConfiguration);
    }

    @Override
    public void register(UrlStore urlStore) {
        // 读取文档结构
        ScriptParser scriptParserDemo = new ScriptParser(urlStore.getSchemaSQL());
        SNode sNode = scriptParserDemo.evalNode("/script");
        // 动态解析文档SQL
        XMLScriptBuilder builder = new XMLScriptBuilder(sNode);
        SqlSource sqlSource = builder.parseScriptNode();
        super.register(urlStore.getId(),sqlSource);
    }

}
