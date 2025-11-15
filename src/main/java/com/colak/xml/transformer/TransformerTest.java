package com.colak.xml.transformer;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

@Slf4j
@UtilityClass
class TransformerTest {

    public static void main() throws Exception {
        String xmlString = """
                <?xml version="1.0" encoding="UTF-8"?>
                <root>
                  <foo/>
                </root>
                """;

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

        Document document = convertStringToDocument(xmlString);

        DOMSource source = new DOMSource(document);
        StringWriter writer = new StringWriter();
        StreamResult outputTarget = new StreamResult(writer);
        transformer.transform(source, outputTarget);

        String string = writer.toString();

        // On Windows output is :
        // <?xml version = '1.0' encoding = 'UTF-8'?>\r\n<root>\n <foo/>\n</root>
        log.info(string);
    }

    private static Document convertStringToDocument(String xmlString)
            throws ParserConfigurationException, IOException, SAXException {
        // Create a DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        // Optional: Set factory properties for security and validation
        factory.setNamespaceAware(true);
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);

        // Create a DocumentBuilder
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Convert XML string to InputSource
        InputSource inputSource = new InputSource(new StringReader(xmlString));

        // Parse the InputSource to get a Document
        return builder.parse(inputSource);
    }
}
