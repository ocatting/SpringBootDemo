package com.yu.test;

import com.yu.exception.ScriptBuilderException;
import lombok.SneakyThrows;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.*;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.io.StringReader;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class ScriptParserDemo {

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
     * <p>Maps to Java {@link org.w3c.dom.Node}.</p>
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

    public ScriptParserDemo(String xml){
        commonConstructor();
        this.document = createDocument(xml);
    }

    public ScriptParserDemo(InputStream inputStream){
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
        ScriptParserDemo scriptParserDemo = new ScriptParserDemo(resource.getInputStream());
        System.out.println(scriptParserDemo.evalNode("/script"));
//        XPathParser parser = new XPathParser(resource.getInputStream());
//        System.out.println(parser.evalNode("/script"));
    }
}
