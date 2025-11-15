package com.colak.xml.xsd;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

// See https://nfrankel.medium.com/xml-schema-validation-1-1-in-java-d9d463bd27ad
@Slf4j
@UtilityClass
class SAXXMLValidatorTest {

    static void main() {
        // XML file to validate
        URL xmlResource = SAXXMLValidatorTest.class.getClassLoader().getResource("xsd/example.xml");
        URL xsdResource = SAXXMLValidatorTest.class.getClassLoader().getResource("xsd/example.xsd");
        assert xmlResource != null;
        assert xsdResource != null;

        // Call the method to validate XML against XSD schema
        try {
            validateXML(xmlResource.getFile(), xsdResource.getFile());
            log.info("Validation successful.");
        } catch (SAXException | IOException | ParserConfigurationException exception) {
            log.error("Validation failed: ", exception);
        }
    }

    public static void validateXML(String xmlFilePath, String xsdFilePath) throws SAXException, IOException, ParserConfigurationException {
        // Set the XSD 1.1 version
        // SchemaFactory schemaFactory = SchemaFactory.newInstance(Constants.W3C_XML_SCHEMA11_NS_URI); //1
        // Create a SchemaFactory and specify XML schema language
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // Load and compile the XML schema
        Schema schema = factory.newSchema(new File(xsdFilePath));

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        saxParserFactory.setSchema(schema);

        DefaultHandler handler = new DefaultHandler();

        XMLReader reader = saxParserFactory.newSAXParser().getXMLReader();                      //3
        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);
        reader.setEntityResolver(handler);
    }
}

