import java.io.File;
import java.util.Date;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public class JaxbWorker {
    public static void main(String[] args) throws JAXBException, SAXException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(new File("./src/main/resources/Employee.xsd")));
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        jaxbUnmarshaller.setSchema(schema);

        //конвертируем XML в объект
        Employee employee = (Employee)jaxbUnmarshaller.unmarshal(new File("./src/main/resources/IncomingXML.xml"));

        //создаем фабрику объектов
        ObjectFactory objectFactory = new ObjectFactory();

        //создаем новый адрес с помощью фабрики
        Employee.Address newAddress = objectFactory.createEmployeeAddress();

        //заполняем адрес
        newAddress.setAddressLine1("г.Ижевск");
        newAddress.setAddressLine2("ул.Космонавтов, 13");
        newAddress.setCountry(employee.getAddress().country);
        newAddress.setState(employee.getAddress().state);
        newAddress.setZip((short) 1);

        //меняем адрес объекта
        employee.setAddress(newAddress);

        Marshaller jaxbMarshaller= jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        //конвертируем объект в XML
        jaxbMarshaller.marshal(employee, new File("./src/main/resources/OutputXML-" + new Date().getTime() +".xml"));

    }
}
