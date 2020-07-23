package com.yu.builder;

import com.yu.builder.sql.*;
import com.yu.exception.ScriptBuilderException;
import com.yu.parsing.SNode;
import com.yu.util.StrUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class XMLScriptBuilder {

    private final SNode context;
    private boolean isDynamic;
    private final Map<String, XMLScriptBuilder.NodeHandler> nodeHandlerMap = new HashMap<>();

    public XMLScriptBuilder(SNode context) {
        this.context = context;
        initNodeHandlerMap();
    }

    private void initNodeHandlerMap() {
//        nodeHandlerMap.put("trim", new XMLScriptBuilder.TrimHandler());
        nodeHandlerMap.put("where", new XMLScriptBuilder.WhereHandler());
//        nodeHandlerMap.put("set", new XMLScriptBuilder.SetHandler());
//        nodeHandlerMap.put("foreach", new XMLScriptBuilder.ForEachHandler());
        nodeHandlerMap.put("if", new XMLScriptBuilder.IfHandler());
//        nodeHandlerMap.put("choose", new XMLScriptBuilder.ChooseHandler());
        nodeHandlerMap.put("when", new XMLScriptBuilder.IfHandler());
//        nodeHandlerMap.put("otherwise", new XMLScriptBuilder.OtherwiseHandler());
        nodeHandlerMap.put("bind", new XMLScriptBuilder.BindHandler());
    }

    public SqlSource parseScriptNode() {
        MixedSqlNode rootSqlNode = parseDynamicTags(context);
        SqlSource sqlSource = null;
        if (isDynamic) {
            sqlSource = new DynamicSqlSource(rootSqlNode);
        }else {
            sqlSource = new RawSqlSource(rootSqlNode);
        }
        return sqlSource;
    }

    protected MixedSqlNode parseDynamicTags(SNode node) {
        List<SqlNode> contents = new ArrayList<>();
        NodeList children = node.getNode().getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            SNode child = node.newSNode(children.item(i));
            if (child.getNode().getNodeType() == Node.CDATA_SECTION_NODE || child.getNode().getNodeType() == Node.TEXT_NODE) {
                String data = StrUtil.replaceAllSymbol(child.getStringBody(""));
                if(data!=null && data.length()>0){
                    contents.add(new StaticTextSqlNode(data));
                }
            } else if (child.getNode().getNodeType() == Node.ELEMENT_NODE) { // issue #628
                String nodeName = child.getNode().getNodeName();
                XMLScriptBuilder.NodeHandler handler = nodeHandlerMap.get(nodeName);
                if (handler == null) {
                    throw new ScriptBuilderException("Unknown element <" + nodeName + "> in SQL statement.");
                }
                handler.handleNode(child, contents);
                isDynamic = true;
            }
        }
        return new MixedSqlNode(contents);
    }

    private interface NodeHandler {
        void handleNode(SNode nodeToHandle, List<SqlNode> targetContents);
    }

    private class BindHandler implements XMLScriptBuilder.NodeHandler {
        public BindHandler() {
            // Prevent Synthetic Access
        }

        @Override
        public void handleNode(SNode nodeToHandle, List<SqlNode> targetContents) {
            final String name = nodeToHandle.getStringAttribute("name");
            final String expression = nodeToHandle.getStringAttribute("value");
            final VarDeclSqlNode node = new VarDeclSqlNode(name, expression);
            targetContents.add(node);
        }
    }

    private class WhereHandler implements XMLScriptBuilder.NodeHandler {
        public WhereHandler() {
            // Prevent Synthetic Access
        }

        @Override
        public void handleNode(SNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = parseDynamicTags(nodeToHandle);
            WhereSqlNode where = new WhereSqlNode(mixedSqlNode);
            targetContents.add(where);
        }
    }

    private class IfHandler implements XMLScriptBuilder.NodeHandler {
        public IfHandler() {
            // Prevent Synthetic Access
        }

        @Override
        public void handleNode(SNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = parseDynamicTags(nodeToHandle);
            String test = nodeToHandle.getStringAttribute("test");
            IfSqlNode ifSqlNode = new IfSqlNode(mixedSqlNode, test);
            targetContents.add(ifSqlNode);
        }
    }

}
