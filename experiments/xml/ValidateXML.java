import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

public class ValidateXML {
    public static void main(String[] args) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File("drives.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File("drives.xml")));
            System.out.println("XML is valid according to XSD.");
        } catch (Exception e) {
            System.out.println("XML validation failed: " + e.getMessage());
        }
    }
}
