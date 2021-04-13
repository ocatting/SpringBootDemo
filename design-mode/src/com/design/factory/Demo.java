package com.design.factory;

/**
 * 优化例子:
 * 比如：我需要根据文件后缀解析文件。
 * 该实现使用的是简单工厂
 * (json,xml,yaml,properties)
 * @author mou ren
 */
public class Demo {

    public class RuleConfigSource {

//        public RuleConfig load(String ruleConfigFilePath) {
//
//            String fileExtension = getFileExtension(ruleConfigFilePath);
//
//            IRuleConfigParser parser = null;
//            if ("json".equalsIgnoreCase(ruleConfigFileExtension))
//            { parser = new JsonRuleConfigParser();
//            } else if ("xml".equalsIgnoreCase(ruleConfigFileExtension)) {
//                parser = new XmlRuleConfigParser();
//            } else if ("yaml".equalsIgnoreCase(ruleConfigFileExtension)) {
//                parser = new YamlRuleConfigParser();
//            } else if ("properties".equalsIgnoreCase(ruleConfigFileExtension)) {
//                parser = new PropertiesRuleConfigParser();
//            } else {
//                throw new InvalidRuleConfigException( "Rule config file format is not supported: " + ruleConfigFilePath);
//            }
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
//        private String getFileExtension(String filepath){
//
//            return "json";
//        }
    }

}
