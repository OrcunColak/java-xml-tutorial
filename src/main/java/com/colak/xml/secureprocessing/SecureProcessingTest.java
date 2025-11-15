package com.colak.xml.secureprocessing;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * See <a href="https://medium.com/@dub-flow/how-secure-is-javas-secureprocessing-ec49544a59ad">...</a>
 */
@Slf4j
public class SecureProcessingTest {

    public static void main(String[] args) throws Exception {
        xmlExternalEntity();
    }

    private static void xmlExternalEntity() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // enables secure processing
        String feature = "http://javax.xml.XMLConstants/feature/secure-processing";
        dbf.setFeature(feature, true);

        String inputXml = """
                <?xml version="1.0"?>
                <!DOCTYPE book [<!ENTITY xxe SYSTEM 'file:///etc/passwd'>]>
                <book>
                    <title>&xxe;</title>
                </book>
                """;
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(inputXml)));

        String str = xmlToString(doc);
        log.info(str);
    }

    private static String xmlToString(Document doc) throws TransformerException {
        StringWriter sw = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();

        Transformer transformer = tf.newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(sw));

        return sw.toString();
    }
}
