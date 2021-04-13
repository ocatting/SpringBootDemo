package com.design.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用工厂方法，首先静态实例化对象，然后在需要的时候取出
 * 优化 Demo 中的代码。
 * 思路是:parser 中的代码违反了开闭原则，需将代码单独抽取出来。作为简单方法。
 * @author mou ren
 */
public class OptimizeDemo {

    public static class RuleConfigFactory {

//        private static final Map<String,IRuleConfigParser> mapping = new ConcurrentHashMap<>(4);
//
//        static {
//            mapping.put("json",new JsonRuleConfigParser());
//            mapping.put("xml",new XmlRuleConfigParser());
//            mapping.put("yaml",new YamlRuleConfigParser());
//            mapping.put("properties",new PropertiesRuleConfigParser());
//        }
//
//        public static final IRuleConfigParser createParser(String fileType){
//            return mapping.get(fileType);
//        }


    }

    public class RuleConfigSource {
//        public RuleConfig load(String ruleConfigFilePath) {
//
//            String fileExtension = getFileExtension(ruleConfigFilePath);
//
//            IRuleConfigParser parser = RuleConfigFactory.createParser(fileExtension);
//
//            String configText = "";
//            //从ruleConfigFilePath文件中读取配置文本到configText中
//            RuleConfig ruleConfig = parser.parse(configText);
//            return ruleConfig;
//        }

        /**
         * 解析文件后缀 如:rule.json
         * @param filepath
         * @return
         */
        private String getFileExtension(String filepath){

            return "json";
        }
    }




}
