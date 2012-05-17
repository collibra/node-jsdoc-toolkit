package com.collibra.dgc.core.service.exchanger.impl.importer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.collibra.dgc.core.service.exchanger.exceptions.SbvrXmiException;

/**
 * Preprocesses the XMI that is parsed as a cmof model. The hrefs in the type element cause errors.
 * @author dtrog
 * 
 */
public class SbvrXmiPreprocesor {

	private final InputStream xmiStream;

	public SbvrXmiPreprocesor(InputStream is) {
		xmiStream = is;
	}

	public InputStream preprocess() {
		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true); // never forget this!
			DocumentBuilder builder = domFactory.newDocumentBuilder();
			Document doc = builder.parse(xmiStream);

			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			XPathExpression expr = xpath.compile("//type");

			Object result = expr.evaluate(doc, XPathConstants.NODESET);
			NodeList nodes = (NodeList) result;
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				NamedNodeMap attributes = node.getAttributes();
				Node href = attributes.getNamedItem("href");
				String typeValue = href.getNodeValue().substring(href.getNodeValue().indexOf('#') + 1);

				Attr datatype = doc.createAttribute("name");
				datatype.setNodeValue(typeValue);
				datatype.setValue(href.getNodeValue());
				attributes.removeNamedItem("href");
				attributes.setNamedItem(datatype);

			}
			DOMSource domSource = new DOMSource(doc);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			java.io.StringWriter sw = new java.io.StringWriter();
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);

			byte[] byteArray = sw.toString().getBytes("ISO-8859-1"); // choose a charset
			return new ByteArrayInputStream(byteArray);
		} catch (Exception e) {
			throw new SbvrXmiException("Could not preprocess the xmi inputstream", e);
		}
	}
}
