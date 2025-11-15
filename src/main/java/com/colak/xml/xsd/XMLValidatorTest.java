package com.colak.xml.xsd;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Slf4j
@UtilityClass
class XMLValidatorTest {

    static void main() {
        // XML file to validate
        URL xmlResource = XMLValidatorTest.class.getClassLoader().getResource("xsd/example.xml");
        URL xsdResource = XMLValidatorTest.class.getClassLoader().getResource("xsd/example.xsd");
        assert xmlResource != null;
        assert xsdResource != null;

        // Call the method to validate XML against XSD schema
        try {
            validateXML(xmlResource.getFile(), xsdResource.getFile());
            log.info("Validation successful.");
        } catch (SAXException | IOException exception) {
            log.error("Validation failed: ", exception);
        }
    }

    public static void validateXML(String xmlFilePath, String xsdFilePath) throws SAXException, IOException {
        // Create a SchemaFactory and specify XML schema language
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // Load and compile the XML schema
        Schema schema = factory.newSchema(new File(xsdFilePath));

        // Create a Validator instance, which can be used to validate an instance document against a schema
        Validator validator = schema.newValidator();

        // Validate the XML document against the schema
        validator.validate(new StreamSource(new File(xmlFilePath)));
    }
}

