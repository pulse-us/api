package gov.ca.emsa.pulse.broker.manager;

import gov.ca.emsa.pulse.broker.BrokerApplicationTestConfig;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={BrokerApplicationTestConfig.class})
public class DocumentParseTest extends TestCase {
	@Autowired private ResourceLoader resourceLoader;

	@Test
	public void parseCcdaDocumentTemplateId() {
		Resource xmlResource = resourceLoader.getResource("classpath:" + "ccdas" + File.separator + "VCN CCDA.xml");
		try {
			File xmlFile = xmlResource.getFile();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);
			
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			NodeList templateIdNodes = (NodeList) xpath.evaluate("ClinicalDocument/templateId", document,
			    XPathConstants.NODESET);
						
			assertEquals(2, templateIdNodes.getLength());
			
			Node templateIdNode = templateIdNodes.item(0);
			NamedNodeMap templateIdAttrs = templateIdNode.getAttributes();
			assertEquals(1, templateIdAttrs.getLength());
			
			Node rootAttribute = templateIdAttrs.getNamedItem("root");
			String templateId = rootAttribute.getNodeValue();
			assertEquals("2.16.840.1.113883.10.20.22.1.1", templateId);
			
		} catch(ParserConfigurationException pce) {
			pce.printStackTrace();
			fail();
		} catch(IOException ex) {
			ex.printStackTrace();
			fail();
		} catch(SAXException sax) {
			sax.printStackTrace();
			fail();
		} catch(XPathExpressionException xex) {
			xex.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void parseCcdaDocumentEffectiveTime() {
		Resource xmlResource = resourceLoader.getResource("classpath:" + "ccdas" + File.separator + "VCN CCDA.xml");
		try {
			File xmlFile = xmlResource.getFile();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);
			
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			Node effectiveDateNode = (Node) xpath.evaluate("ClinicalDocument/effectiveTime", document,
			    XPathConstants.NODE);
									
			NamedNodeMap effectiveDateAttrs = effectiveDateNode.getAttributes();
			assertEquals(1, effectiveDateAttrs.getLength());
			
			Node valueAttr = effectiveDateAttrs.getNamedItem("value");
			String value = valueAttr.getNodeValue();
			assertEquals("20150226004009", value);
			
		} catch(ParserConfigurationException pce) {
			pce.printStackTrace();
			fail();
		} catch(IOException ex) {
			ex.printStackTrace();
			fail();
		} catch(SAXException sax) {
			sax.printStackTrace();
			fail();
		} catch(XPathExpressionException xex) {
			xex.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void parseCcdaDocumentPatientData() {
		Resource xmlResource = resourceLoader.getResource("classpath:" + "ccdas" + File.separator + "VCN CCDA.xml");
		try {
			File xmlFile = xmlResource.getFile();
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);
			
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			Node familyNameNode = (Node) xpath.evaluate("ClinicalDocument/recordTarget/patientRole/patient/name/family", document,
			    XPathConstants.NODE);
			Node givenNameNode = (Node) xpath.evaluate("ClinicalDocument/recordTarget/patientRole/patient/name/given", document,
				    XPathConstants.NODE);				
			
			String familyName = familyNameNode.getTextContent();
			assertEquals("Jones", familyName);

			String givenName = givenNameNode.getTextContent();
			assertEquals("Isabella", givenName);

		} catch(ParserConfigurationException pce) {
			pce.printStackTrace();
			fail();
		} catch(IOException ex) {
			ex.printStackTrace();
			fail();
		} catch(SAXException sax) {
			sax.printStackTrace();
			fail();
		} catch(XPathExpressionException xex) {
			xex.printStackTrace();
			fail();
		}
	}
}
