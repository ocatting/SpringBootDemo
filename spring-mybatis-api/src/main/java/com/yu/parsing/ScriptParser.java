package com.yu.parsing;

import com.yu.exception.ScriptBuilderException;
import lombok.SneakyThrows;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: Script 表达式解析
 * @Author Yan XinYu
 **/
public class ScriptParser {

    private final Document document;
    private XPath xpath;

    /**
     * <p>The XPath 1.0 number data type.</p>
     *
     * <p>Maps to Java {@link Double}.</p>
     */
    public static final QName NUMBER = new QName("http://www.w3.org/1999/XSL/Transform", "NUMBER");

    /**
     * <p>The XPath 1.0 string data type.</p>
     *
     * <p>Maps to Java {@link String}.</p>
     */
    public static final QName STRING = new QName("http://www.w3.org/1999/XSL/Transform", "STRING");

    /**
     * <p>The XPath 1.0 boolean data type.</p>
     *
     * <p>Maps to Java {@link Boolean}.</p>
     */
    public static final QName BOOLEAN = new QName("http://www.w3.org/1999/XSL/Transform", "BOOLEAN");

    /**
     * <p>The XPath 1.0 NodeSet data type.</p>
     *
     * <p>Maps to Java {@link org.w3c.dom.NodeList}.</p>
     */
    public static final QName NODESET = new QName("http://www.w3.org/1999/XSL/Transform", "NODESET");

    /**
     * <p>The XPath 1.0 NodeSet data type.
     *
     * <p>Maps to Java {@link Node}.</p>
     */
    public static final QName NODE = new QName("http://www.w3.org/1999/XSL/Transform", "NODE");

    /**
     * <p>The URI for the DOM object model, "http://java.sun.com/jaxp/xpath/dom".</p>
     */
    public static final String DOM_OBJECT_MODEL = "http://java.sun.com/jaxp/xpath/dom";

    /**
     * XPathFactory factory = XPathFactory.newInstance();
     *     XPath xpath this.xpath = factory.newXPath();
     * @param xml
     * @return
     */

    public ScriptParser(String xml){
        commonConstructor();
        this.document = createDocument(xml);
    }

    public ScriptParser(InputStream inputStream){
        commonConstructor();
        this.document = createDocument(inputStream);
    }

    private void commonConstructor() {
        XPathFactory factory = XPathFactory.newInstance();
        this.xpath = factory.newXPath();
    }

    public final Document createDocument(String xml){
        return createDocument(new InputSource(new StringReader(xml)));
    }

    public final Document createDocument(InputStream inputStream ){
        return createDocument(new InputSource(inputStream));
    }

    public String evalString(String expression) {
        return evalString(document, expression);
    }

    public String evalString(Object root, String expression) {
        String result = (String) evaluate(expression, root, XPathConstants.STRING);
        result = PropertyParser.parse(result, null);
        return result;
    }

    public Boolean evalBoolean(String expression) {
        return evalBoolean(document, expression);
    }

    public Boolean evalBoolean(Object root, String expression) {
        return (Boolean) evaluate(expression, root, XPathConstants.BOOLEAN);
    }

    public Short evalShort(String expression) {
        return evalShort(document, expression);
    }

    public Short evalShort(Object root, String expression) {
        return Short.valueOf(evalString(root, expression));
    }

    public Integer evalInteger(String expression) {
        return evalInteger(document, expression);
    }

    public Integer evalInteger(Object root, String expression) {
        return Integer.valueOf(evalString(root, expression));
    }

    public Long evalLong(String expression) {
        return evalLong(document, expression);
    }

    public Long evalLong(Object root, String expression) {
        return Long.valueOf(evalString(root, expression));
    }

    public Float evalFloat(String expression) {
        return evalFloat(document, expression);
    }

    public Float evalFloat(Object root, String expression) {
        return Float.valueOf(evalString(root, expression));
    }

    public Double evalDouble(String expression) {
        return evalDouble(document, expression);
    }

    public Double evalDouble(Object root, String expression) {
        return (Double) evaluate(expression, root, XPathConstants.NUMBER);
    }

    public List<SNode> evalNodes(String expression) {
        return evalNodes(document, expression);
    }

    public List<SNode> evalNodes(Object root, String expression) {
        List<SNode> SNodes = new ArrayList<>();
        NodeList nodes = (NodeList) evaluate(expression, root, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            SNodes.add(new SNode(this, nodes.item(i)));
        }
        return SNodes;
    }

    public SNode evalNode(String expression) {
        return evalNode(document, expression);
    }

    public SNode evalNode(Object document, String expression) {
        Node node = (Node) evaluate(expression,document,NODE);
        if (node == null) {
            return null;
        }
        return new SNode(this,node);
    }

    private Object evaluate(String expression, Object document, QName returnType) {
        try{
            return xpath.evaluate(expression, document, returnType);
        } catch (Exception e) {
            throw new ScriptBuilderException("Error evaluating XPath.  Cause: " + e, e);
        }
    }

    /**
     * 创建解析文档
     * @return
     */
    public final Document createDocument(InputSource inputSource){
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(false);
            factory.setCoalescing(false);
            factory.setExpandEntityReferences(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(null);
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(SAXParseException exception) throws SAXException {
                }
            });
            return builder.parse(inputSource);
        } catch (Exception e) {
            throw new ScriptBuilderException("Error creating document instance.  Cause: " + e, e);
        }
    }

    /**
     * 测试xml文件解析
     * @param args
     */
    @SneakyThrows
    public static void main(String[] args) {
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader.getResource("classpath:xmltest");
        String xml = "";
        ScriptParser scriptParserDemo = new ScriptParser(resource.getInputStream());
        System.out.println(scriptParserDemo.evalNode("/script"));
//        XPathParser parser = new XPathParser(resource.getInputStream());
//        System.out.println(parser.evalNode("/script"));
    }
}
