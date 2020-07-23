package com.yu.test;

import com.yu.builder.BoundSql;
import com.yu.builder.SqlSource;
import com.yu.builder.XMLScriptBuilder;
import com.yu.parsing.SNode;
import com.yu.parsing.ScriptParser;
import lombok.SneakyThrows;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class XMLScriptBuilderTest {

    @SneakyThrows
    public static void main(String[] args) {
        // 读取文件流
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource("classpath:xmltest");

        // 读取文档结构
        ScriptParser scriptParserDemo = new ScriptParser(resource.getInputStream());
        SNode sNode = scriptParserDemo.evalNode("/script");

        // 动态解析文档SQL
        XMLScriptBuilder builder = new XMLScriptBuilder(sNode);
        SqlSource sqlSource = builder.parseScriptNode();
        System.out.println("aa");

        // 动态生成 SQL 语句
        Map<String,String> root = new HashMap<>();
        root.put("name","a");
        root.put("person","人");
        BoundSql boundSql = sqlSource.getBoundSql(root);
        System.out.println(boundSql.getSql());
    }
}
