package com.yu.builder;

import com.yu.builder.sql.StaticSqlSource;
import com.yu.parsing.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class SqlSourceBuilder {

    public SqlSource parse(String originalSql) {
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler();
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        String sql = parser.parse(originalSql);
        return new StaticSqlSource(sql, handler.getParameterMappings());
    }

    private static class ParameterMappingTokenHandler implements TokenHandler {

        private List<ParameterMapping> parameterMappings = new ArrayList<>();

        @Override
        public String handleToken(String content) {
            if(parameterMappings == null){
                parameterMappings = new ArrayList<>();
            }
            // 解决类型问题
            Map<String, String> propertiesMap = new ParameterExpression(content);
            String property = propertiesMap.get("property");
            ParameterMapping.ParameterMappingBuilder builder = ParameterMapping.builder().property(property);
            // 循环赋值这个属性的所有必要参数信息
            for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                String name = entry.getKey();
                String value = entry.getValue();
                if ("javaType".equals(name)) {
                    builder.javaTypeName(value);
                    builder.javaType(resolveClass(value));
                }
            }
            parameterMappings.add(builder.build());
            return "?";
        }

        /**
         * 映射类型
         * @param value
         * @param <T>
         * @return
         */
        private <T> Class<? extends T> resolveClass(String value){
            return TypeAliasRegistry.resolveAlias(value);
        }

        /**
         * 获取参数的映射关系
         * @return
         */
        public List getParameterMappings(){
            return parameterMappings;
        }

    }
}
