package com.xiaohei.java.lib.paras.xml;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParas extends Paras {
    SAXParser saxParser;
    static SAXParas saxParas;
    SAXParasHandler sHandler;

    public static SAXParas getSaxParas() {
        if (saxParas != null)
            return saxParas;
        throw new RuntimeException("not init");
    }

    public static SAXParas init() {
        if (saxParas == null)
            synchronized (SAXParas.class.getName()) {
                if (saxParas == null)
                    saxParas = new SAXParas();
            }
        return saxParas;
    }

    public SAXParas() {
        // TODO Auto-generated constructor stub
        try {
            saxParser = SAXParserFactory.newInstance().newSAXParser();
            sHandler = new Handler();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }

    public List<NodeTree> paras(File file, SAXParasHandler sParasHandler) {
        if (sParasHandler == null)
            sParasHandler = sHandler;
        try {
            saxParser.parse(file, sParasHandler);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
        return sParasHandler.getNodeTrees();
    }

    @Override
    List<NodeTree> paras(File file) {
        return paras(file, null);
    }

    public List<NodeTree> paras(String uri, SAXParasHandler sParasHandler) {
        if (sParasHandler == null)
            sParasHandler = sHandler;
        try {
            saxParser.parse(uri, sParasHandler);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return sParasHandler.getNodeTrees();
    }

    @Override
    List<NodeTree> paras(String uri) {
        return paras(uri, null);
    }

    public List<NodeTree> paras(InputStream is, SAXParasHandler sParasHandler) {
        if (sParasHandler == null)
            sParasHandler = sHandler;
        try {
            saxParser.parse(is, sParasHandler);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return sParasHandler.getNodeTrees();
    }

    @Override
    List<NodeTree> paras(InputStream is) {
        return paras(is, null);
    }

    public List<NodeTree> paras(InputSource is, SAXParasHandler sParasHandler) {
        if (sParasHandler == null)
            sParasHandler = sHandler;
        try {
            saxParser.parse(is, sParasHandler);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return sParasHandler.getNodeTrees();
    }

    public List<NodeTree> paras(InputSource is) {
        return paras(is, null);
    }

    @Override
    String nodeTree2string(NodeTree nodeTree) {
        try {
            TransformerHandler handler = getHandler();
            StringWriter writer = new StringWriter();
            Result result = new StreamResult(writer);
            handler.setResult(result);
            String uri = "";
            String localName = "";
            handler.startDocument();
            start_element(uri, localName, nodeTree, handler);
            handler.endDocument();
            return writer.toString();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }


    private void start_element(String uri, String localName, NodeTree nodeTree,
                               TransformerHandler handler) throws Exception {
        if (nodeTree == null)
            return;
        Map<String, String> attributes = nodeTree.getAttributes();
        if (attributes == null || attributes.size() == 0)
            handler.startElement(uri, localName, nodeTree.getName(), null);
        else {
            AttributesImpl attrs = new AttributesImpl();
            Set<String> set = attributes.keySet();
            Iterator<String> i = set.iterator();
            while (i.hasNext()) {
                String key = i.next();
                Object o = attributes.get(key);
                String type = null;
                if (o instanceof Integer)
                    type = "int";
                else if (o instanceof Byte)
                    type = "byte";
                else if (o instanceof Long)
                    type = "long";
                else if (o instanceof Float)
                    type = "float";
                else if (o instanceof Double)
                    type = "double";
                else if (o instanceof Boolean)
                    type = "boolean";
                else if (o instanceof String)
                    type = "string";
                if (type != null)
                    attrs.addAttribute(uri, localName, key, type, String
                            .valueOf(o));
            }
            handler.startElement(uri, localName, nodeTree.getName(), attrs);
        }
        String value = nodeTree.getValue();
        if (value != null) {
            char[] ch = ch = value.toCharArray();
            handler.characters(ch, 0, ch.length);
        }
        List<NodeTree> nodeTrees = nodeTree.getChildNodeTrees();
        if (nodeTrees != null && nodeTrees.size() > 0) {
            for (NodeTree node : nodeTrees) {
                start_element(uri, localName, node, handler);
            }
        }
        handler.endElement(uri, localName, nodeTree.getName());
    }


    private TransformerHandler getHandler() throws Exception {
        SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory
                .newInstance();
        TransformerHandler handler = factory.newTransformerHandler();
        Transformer transformer = handler.getTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        return handler;
    }

    class Handler extends SAXParasHandler {
        List<NodeTree> nodeTrees;
        NodeTree nodeTree;
        StringBuilder builder;

        @Override
        public void startDocument() throws SAXException {
            // TODO Auto-generated method stub
            nodeTrees = new ArrayList<NodeTree>();
            builder = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            // TODO Auto-generated method stub
            nodeTree = new NodeTree();
            nodeTree.setName(qName);
            if (attributes != null) {
                Map<String, String> map = new TreeMap<String, String>();
                for (int i = 0; i < attributes.getLength(); i++) {
                    String key = attributes.getLocalName(i);
                    String value = attributes.getValue(i);
                    map.put(key, value);
                }
                nodeTree.setAttributes(map);
            }
            NodeTree n = getNodeTree(nodeTrees);
            if (n == null)
                nodeTrees.add(nodeTree);
            else {
                List<NodeTree> nList = n.getChildNodeTrees();
                if (nList == null) {
                    nList = new ArrayList<NodeTree>();
                    nList.add(nodeTree);
                    n.setChildNodeTrees(nList);
                } else
                    nList.add(nodeTree);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length)
                throws SAXException {
            // TODO Auto-generated method stub
            builder.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            // TODO Auto-generated method stub
            if (nodeTree != null) {
                nodeTree.setValue(builder.toString().trim());
                nodeTree.setIs_end(true);
                nodeTree = null;
            } else {
                NodeTree n = getNodeTree(nodeTrees);
                if (n != null) {
                    if (qName.equals(n.getName()))
                        n.setIs_end(true);
                }
            }
            builder.setLength(0);
        }

        @Override
        public void endDocument() throws SAXException {
            // TODO Auto-generated method stub
            super.endDocument();
        }

        @Override
        public List<NodeTree> getNodeTrees() {
            // TODO Auto-generated method stub
            return nodeTrees;
        }

        NodeTree getNodeTree(List<NodeTree> nodeTrees) {
            if (nodeTrees.size() == 0)
                return null;
            for (int i = 0; i < nodeTrees.size(); i++) {
                NodeTree nodeTree = nodeTrees.get(i);
                if (!nodeTree.isIs_end()) {
                    if (nodeTree.hasChildNodeTrees()) {
                        NodeTree n = getNodeTree(nodeTree.getChildNodeTrees());
                        if (n != null)
                            return n;
                    }
                    return nodeTree;
                }
            }
            return null;
        }
    }

    public static abstract class SAXParasHandler extends DefaultHandler {
        public abstract List<NodeTree> getNodeTrees();
    }
}
