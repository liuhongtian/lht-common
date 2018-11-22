package net.lht.common.misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

//import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * JAXB实现的XML处理类<br>
 * 使用了EclipseLink中的MOXy JAXB implementation作为JAXB的实现，详细说明见如下网址：<br>
 * <a href=
 * "http://theopentutorials.com/tutorials/java/jaxb/jaxb-marshalling-and-unmarshalling-cdata-block-using-eclipselink-moxy/"
 * >http://theopentutorials.com/tutorials/java/jaxb/jaxb-marshalling-and-
 * unmarshalling-cdata-block-using-eclipselink-moxy/</a>
 * 
 * @author liuhongtian
 * 
 */
public class XmlUtil {

	//private static final Logger log = Logger.getLogger(XmlUtil.class);

	private JAXBContext jaxbCtx;
	private Marshaller marshaller;
	private Unmarshaller unmarshaller;

	/**
	 * 
	 * object initer, used by constructor
	 * 
	 * @param ctxPath
	 *            POJO package name
	 * @param xsdName
	 *            XML schema file (must in class path)
	 * @throws JAXBException
	 * @throws SAXException
	 */
	private void init(String ctxPath, String xsdName) throws JAXBException, SAXException {

		this.jaxbCtx = JAXBContext.newInstance(ctxPath);
		this.marshaller = jaxbCtx.createMarshaller();
		// use UTF-8 encoding
		this.marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		this.unmarshaller = jaxbCtx.createUnmarshaller();
		ValidationEventCollector vec = new ValidationEventCollector();
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		URL url = XmlUtil.class.getClassLoader().getResource(xsdName);
		Schema schema = sf.newSchema(url);
		this.marshaller.setSchema(schema);
		this.marshaller.setEventHandler(vec);
		this.unmarshaller.setSchema(schema);
		this.unmarshaller.setEventHandler(vec);
	}

	/**
	 * 
	 * custom constructor
	 * 
	 * @param ctxPath
	 *            POJO package name
	 * @param xsdName
	 *            XML schema file (must in class path)
	 * @throws JAXBException
	 * @throws SAXException
	 */
	public XmlUtil(String ctxPath, String xsdName) throws JAXBException, SAXException {

		init(ctxPath, xsdName);
	}

	/**
	 * default constructor
	 * 
	 * @throws JAXBException
	 * @throws SAXException
	 */
	public XmlUtil() {
	}

	/**
	 * marshal POJO to stream
	 * 
	 * @param pojo
	 *            POJO object
	 * @param out
	 *            stream to output
	 * @throws JAXBException
	 */
	private void marshal(Object pojo, OutputStream out) throws JAXBException {
		if (marshaller != null) {
			marshaller.marshal(pojo, out);
		} else {
			//log.fatal("Marshaller is null.");
		}
	}

	/**
	 * unmarshal stream input to POJO
	 * 
	 * @param in
	 *            stream to input
	 * @return POJO
	 * @throws JAXBException
	 */
	private Object unmarshal(InputStream in) throws JAXBException {
		if (unmarshaller != null) {
			return unmarshaller.unmarshal(in);
		} else {
			//log.fatal("Unmarshaller is null.");
			return null;
		}
	}

	/**
	 * parse POJO as defined type to XML string
	 * 
	 * @param pojo
	 *            POJO to be parsed
	 * @param clazz
	 *            defined type
	 * @return XML string, null if any exception
	 */
	public String parsePojo(Object pojo, Class<?> clazz) {

		// return null if pojo is null.
		if (pojo == null) {
			//log.warn("pojo is null.");
			return null;
		}

		// return null if clazz is null.
		if (clazz == null) {
			//log.fatal("defined type clazz is null.");
			return null;
		}

		// test class cast, return null when false.
		if (!clazz.isInstance(pojo)) {
			//log.fatal("Can not parse POJO object as defined type: " + clazz.getName() + ".");
			return null;
		}

		ByteArrayOutputStream stream = null;
		String str = null;
		try {
			stream = new ByteArrayOutputStream();
			marshal(pojo, stream);
			byte[] buff = stream.toByteArray();
			str = new String(buff, "UTF-8"); // 使用UTF-8编码
		} catch (JAXBException e) {
			//log.fatal("Can not parse POJO (" + pojo.getClass().getName() + ") to XML string.", e);
			e.printStackTrace();
			// System.out.println("============return=d");

		} catch (UnsupportedEncodingException e) {
			//log.fatal("Can not parse POJO to XML string use UTF-8 encoding.", e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					//log.warn(e.getMessage(), e);
				}
			}
		}

		return str;
	}

	/**
	 * parse XML string to POJO object as defined type
	 * 
	 * @param xmlStr
	 *            XML string to be parsed
	 * @param clazz
	 *            defined type
	 * @return POJO raw object, null if any exception
	 */
	public <T> T parseXmlStr(String xmlStr, Class<T> clazz) {

		// return null if xmlStr is null.
		if (xmlStr == null) {
			//log.warn("xmlStr is null.");
			return null;
		}

		// return null if clazz is null.
		if (clazz == null) {
			//log.fatal("defined type clazz is null.");
			return null;
		}

		ByteArrayInputStream stream = null;
		T obj = null;
		try {
			stream = new ByteArrayInputStream(xmlStr.trim().getBytes("UTF-8"));
			Object tmp = unmarshal(stream);

			// test class cast, return null when false.
			if (!clazz.isInstance(tmp)) {
				//log.fatal("Can not parse XML string to POJO object as defined type: " + clazz.getName() + ".");
				return null;
			}

			obj = clazz.cast(tmp);

		} catch (UnsupportedEncodingException e) {
			//log.fatal("Can not parse XML string to POJO object as defined type use UTF-8 encoding.", e);
		} catch (JAXBException e) {
			//log.fatal("Can not parse XML string to POJO object as defined type: " + clazz.getName() + ".", e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					//log.warn(e.getMessage(), e);
				}
			}
		}
		return obj;
	}

}
