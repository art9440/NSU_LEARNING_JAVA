package org.chat.common.messagesOBJ;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;

public class XMLUtil {
    public static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();


    public static Document newDocument() throws ParserConfigurationException {
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.newDocument();
    }

    public static byte[] toBytes(Document doc) throws TransformerException {
        Transformer t = TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        t.transform(new DOMSource(doc), new StreamResult(baos));
        return baos.toByteArray();
    }

    public static Element addTextElement(Document doc, Element parent, String tagName, String text) {
        Element el = doc.createElement(tagName);
        el.setTextContent(text);
        parent.appendChild(el);
        return el;
    }



}
