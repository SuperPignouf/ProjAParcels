package GMapClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Classe utilitaire tiree de la page des exercices du cours Web Services (INFO-H-511)
 */
public class XMLHelper {

    /**
     * Execute an xpath query on a specific node.
     *
     * The type parameter specifies the expected type of the result of the
     * query. Typical types are:
     *
     *  XPathContants.NUMBER: A java Double that corresponds to a numeric
     *  literal in the XML document
     *  XPathConstants.STRING: A java String that corresponds to a text literal
     *  in the XML document
     *  XPathConstants.NODESET: A NodeList that contains a list of nodes
     *  in the XML document
     *  XPathConstants.NODE: A particular Node inside the XML document
     *
     * @param doc The node on which the xpath expression is to be executed
     * @param xpath_expr The XPath expression to be executed
     * @param type One of the XPathConstants that specified the expected output.
     * @return The result of the XPath query on the node, of the corresponding
     *         type
     */
    @SuppressWarnings("unchecked")
    public static <T> T xpath(Node doc, String xpath_expr, QName type) {
        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath xpath = xpathfactory.newXPath();
        XPathExpression expr;
        try {
            expr = xpath.compile(xpath_expr);
            return (T)expr.evaluate(doc, type);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    private static DocumentBuilder xmlBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        return factory.newDocumentBuilder();
    }

    /**
     * Build an XML Document from the data read on the given stream
     * @param data A stream from which an XML document can be read
     * @return A Document object equivalent to the XML document read on stream
     */
    public static Document xml(InputStream data) {
        try {
            return xmlBuilder().parse(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Build an XML Document from the data contained in the given String
     * @param data A String containing an XML document
     * @return A Document object equivalent to the XML document in the given String
     */
    public static Document xml(String data) {
        return xml(new ByteArrayInputStream(data.getBytes()));
    }

    /**
     * Create a new, empty XML document
     * @return A new, empty XML document
     */
    public static Document xml() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            Document d = factory.newDocumentBuilder().newDocument();
            d.setXmlStandalone(true);
            return d;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a String representation corresponding to the given XML Document
     * @param doc The document to be serialized as a String.
     * @param prettyprint Whether the document should be human-readable
     *                    (pretty-printed) or not.
     * @return A String corresponding to the given document.
     */
    public static String stringify(Document doc, boolean prettyprint) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            if (prettyprint) factory.setAttribute("indent-number", 2);

            Transformer transformer = factory.newTransformer();
            if (prettyprint) transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            StringWriter writer = new StringWriter();
            Result result = new StreamResult(writer);
            Source source = new DOMSource(doc);
            transformer.transform(source, result);
            writer.flush();
            writer.close();
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a String representation corresponding to the given XML Document
     * @param doc The document to be serialized as a String.
     * @return A String corresponding to the given document.
     */
    public static String stringify(Document doc) {
        return stringify(doc, false);
    }

    private static Document getDoc(Node n) {
        if (n instanceof Document)  return (Document)n;
        else return n.getOwnerDocument();
    }

    /**
     * Create a new element in the parent node with the given local name.
     * @param parent The node to which the new element will be appended.
     * @param name The local name of the new element to be created
     * @return The newly created element.
     */
    public static Element makeElement(Node parent, String name) {
        Document d = getDoc(parent);
        Element result = d.createElement(name);
        parent.appendChild(result);
        return result;
    }

    /**
     * Create a new element in the parent node with the given local name and namespace.
     * @param parent The node to which the new element will be appended.
     * @param namespace The namespace of the new element to be created
     * @param name The local name of the new element to be created
     * @return The newly created element.
     */
    public static Element makeElementNS(Node parent, String namespace, String name) {
        Document d = getDoc(parent);
        Element result = d.createElementNS(namespace, name);
        parent.appendChild(result);
        return result;
    }

}
