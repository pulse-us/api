package gov.ca.emsa.pulse.broker.saml;

import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.xml.XMLObjectBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.saml.SAMLBootstrap;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.metadata.MetadataGenerator;
import org.springframework.security.saml.metadata.MetadataGeneratorFilter;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.security.saml.processor.SAMLProcessorImpl;

import java.util.Iterator;
import java.util.Map;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Condition;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.OneTimeUse;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.schema.XSString;

public class SamlGenerator {

	@Autowired private SAMLBootstrap samlBootstrap;
	@Autowired private MetadataGenerator metadataGenerator;
	@Autowired private SAMLCredential samlCredential;
	@Autowired private SAMLProcessorImpl samlProcessorImpl;
	@Autowired private MetadataGeneratorFilter metadataGeneratorFilter;
	@Autowired private SAMLEntryPoint samlEntryPoint;
	@Autowired private MetadataManager metadataManager;
	@Autowired private XMLObjectBuilder xmlObjectBuilder;
	@Autowired private SAMLObjectBuilder samlObjectBuilder;

	private static XMLObjectBuilderFactory builderFactory;

	public static XMLObjectBuilderFactory getSAMLBuilder() throws ConfigurationException{

		if(builderFactory == null){
			DefaultBootstrap.bootstrap();
			builderFactory = Configuration.getBuilderFactory();
		}

		return builderFactory;
	}

	/**
	 * Builds a SAML Attribute of type String
	 * @param name
	 * @param value
	 * @param builderFactory
	 * @return
	 * @throws ConfigurationException
	 */
	public static Attribute buildStringAttribute(String name, String value, XMLObjectBuilderFactory builderFactory) throws ConfigurationException
	{
		SAMLObjectBuilder attrBuilder = (SAMLObjectBuilder) getSAMLBuilder().getBuilder(Attribute.DEFAULT_ELEMENT_NAME);
		Attribute attrFirstName = (Attribute) attrBuilder.buildObject();
		attrFirstName.setName(name);

		// Set custom Attributes
		XMLObjectBuilder stringBuilder = getSAMLBuilder().getBuilder(XSString.TYPE_NAME);
		XSString attrValueFirstName = (XSString) stringBuilder.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME, XSString.TYPE_NAME);
		attrValueFirstName.setValue(value);

		attrFirstName.getAttributeValues().add(attrValueFirstName);
		return attrFirstName;
	}

	/**
	 * Helper method which includes some basic SAML fields which are part of almost every SAML Assertion.
	 *
	 * @param input
	 * @return
	 */
	public static Assertion buildDefaultAssertion(SAMLInputContainer input)
	{
		try
		{
			// Create the NameIdentifier
			SAMLObjectBuilder nameIdBuilder = (SAMLObjectBuilder) SamlGenerator.getSAMLBuilder().getBuilder(NameID.DEFAULT_ELEMENT_NAME);
			NameID nameId = (NameID) nameIdBuilder.buildObject();
			nameId.setValue(input.getStrNameID());
			nameId.setNameQualifier(input.getStrNameQualifier());
			nameId.setFormat(NameID.UNSPECIFIED);

			// Create the SubjectConfirmation

			SAMLObjectBuilder confirmationMethodBuilder = (SAMLObjectBuilder)  SamlGenerator.getSAMLBuilder().getBuilder(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
			SubjectConfirmationData confirmationMethod = (SubjectConfirmationData) confirmationMethodBuilder.buildObject();
			DateTime now = new DateTime();
			confirmationMethod.setNotBefore(now);
			confirmationMethod.setNotOnOrAfter(now.plusMinutes(2));

			SAMLObjectBuilder subjectConfirmationBuilder = (SAMLObjectBuilder) SamlGenerator.getSAMLBuilder().getBuilder(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
			SubjectConfirmation subjectConfirmation = (SubjectConfirmation) subjectConfirmationBuilder.buildObject();
			subjectConfirmation.setSubjectConfirmationData(confirmationMethod);

			// Create the Subject
			SAMLObjectBuilder subjectBuilder = (SAMLObjectBuilder) SamlGenerator.getSAMLBuilder().getBuilder(Subject.DEFAULT_ELEMENT_NAME);
			Subject subject = (Subject) subjectBuilder.buildObject();

			subject.setNameID(nameId);
			subject.getSubjectConfirmations().add(subjectConfirmation);

			// Create Authentication Statement
			SAMLObjectBuilder authStatementBuilder = (SAMLObjectBuilder) SamlGenerator.getSAMLBuilder().getBuilder(AuthnStatement.DEFAULT_ELEMENT_NAME);
			AuthnStatement authnStatement = (AuthnStatement) authStatementBuilder.buildObject();
			//authnStatement.setSubject(subject);
			authnStatement.setAuthenticationMethod(input.setStrAuthMethod(strAuthMethod));
			DateTime now2 = new DateTime();
			authnStatement.setAuthnInstant(now2);
			authnStatement.setSessionIndex(input.getSessionId());
			authnStatement.setSessionNotOnOrAfter(now2.plus(input.getMaxSessionTimeoutInMinutes()));

			SAMLObjectBuilder authContextBuilder = (SAMLObjectBuilder) SamlGenerator.getSAMLBuilder().getBuilder(AuthnContext.DEFAULT_ELEMENT_NAME);
			AuthnContext authnContext = (AuthnContext) authContextBuilder.buildObject();

			SAMLObjectBuilder authContextClassRefBuilder = (SAMLObjectBuilder) SamlGenerator.getSAMLBuilder().getBuilder(AuthnContextClassRef.DEFAULT_ELEMENT_NAME);
			AuthnContextClassRef authnContextClassRef = (AuthnContextClassRef) authContextClassRefBuilder.buildObject();
			authnContextClassRef.setAuthnContextClassRef("urn:oasis:names:tc:SAML:2.0:ac:classes:Password"); // TODO not sure exactly about this

			authnContext.setAuthnContextClassRef(authnContextClassRef);
			authnStatement.setAuthnContext(authnContext);

			// Builder Attributes
			SAMLObjectBuilder attrStatementBuilder = (SAMLObjectBuilder) SamlGenerator.getSAMLBuilder().getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
			AttributeStatement attrStatement = (AttributeStatement) attrStatementBuilder.buildObject();

			Map attributes = input.getAttributes();
			if(attributes != null){
				Iterator keySet = attributes.keySet().iterator();
				while (keySet.hasNext())
				{
					String key = keySet.next().toString();
					String val = attributes.get(key).toString();
					Attribute attrFirstName = buildStringAttribute(key, val, getSAMLBuilder());
					attrStatement.getAttributes().add(attrFirstName);
				}
			}

			// Create the do-not-cache condition
			SAMLObjectBuilder doNotCacheConditionBuilder = (SAMLObjectBuilder) SamlGenerator.getSAMLBuilder().getBuilder(OneTimeUse.DEFAULT_ELEMENT_NAME);
			Condition condition = (Condition) doNotCacheConditionBuilder.buildObject();

			SAMLObjectBuilder conditionsBuilder = (SAMLObjectBuilder) SamlGenerator.getSAMLBuilder().getBuilder(Conditions.DEFAULT_ELEMENT_NAME);
			Conditions conditions = (Conditions) conditionsBuilder.buildObject();
			conditions.getConditions().add(condition);

			// Create Issuer
			SAMLObjectBuilder issuerBuilder = (SAMLObjectBuilder) SamlGenerator.getSAMLBuilder().getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
			Issuer issuer = (Issuer) issuerBuilder.buildObject();
			issuer.setValue(input.getStrIssuer());

			// Create the assertion
			SAMLObjectBuilder assertionBuilder = (SAMLObjectBuilder) SamlGenerator.getSAMLBuilder().getBuilder(Assertion.DEFAULT_ELEMENT_NAME);
			Assertion assertion = (Assertion) assertionBuilder.buildObject();
			assertion.setIssuer(issuer);
			assertion.setIssueInstant(now);
			assertion.setVersion(SAMLVersion.VERSION_20);

			assertion.getAuthnStatements().add(authnStatement);
			assertion.getAttributeStatements().add(attrStatement);
			assertion.setConditions(conditions);

			return assertion;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static class SAMLInputContainer
	{

		private String strIssuer;
		private String strNameID;
		private String strNameQualifier;
		private String sessionId;
		private String strAuthMethod;
		private int maxSessionTimeoutInMinutes = 15; // default is 15 minutes

		private Map attributes;

		/**
		 * Returns the strIssuer.
		 *
		 * @return the strIssuer
		 */
		public String getStrIssuer()
		{
			return strIssuer;
		}

		/**
		 * Sets the strIssuer.
		 *
		 * @param strIssuer
		 *            the strIssuer to set
		 */
		public void setStrIssuer(String strIssuer)
		{
			this.strIssuer = strIssuer;
		}

		/**
		 * Returns the strNameID.
		 *
		 * @return the strNameID
		 */
		public String getStrNameID()
		{
			return strNameID;
		}

		/**
		 * Sets the strNameID.
		 *
		 * @param strNameID
		 *            the strNameID to set
		 */
		public void setStrNameID(String strNameID)
		{
			this.strNameID = strNameID;
		}

		/**
		 * Returns the strNameQualifier.
		 *
		 * @return the strNameQualifier
		 */
		public String getStrNameQualifier()
		{
			return strNameQualifier;
		}

		/**
		 * Sets the strNameQualifier.
		 *
		 * @param strNameQualifier
		 *            the strNameQualifier to set
		 */
		public void setStrNameQualifier(String strNameQualifier)
		{
			this.strNameQualifier = strNameQualifier;
		}

		/**
		 * Sets the attributes.
		 *
		 * @param attributes
		 *            the attributes to set
		 */
		public void setAttributes(Map attributes)
		{
			this.attributes = attributes;
		}

		/**
		 * Returns the attributes.
		 *
		 * @return the attributes
		 */
		public Map getAttributes()
		{
			return attributes;
		}

		/**
		 * Sets the sessionId.
		 * @param sessionId the sessionId to set
		 */
		public void setSessionId(String sessionId)
		{
			this.sessionId = sessionId;
		}

		/**
		 * Returns the sessionId.
		 * @return the sessionId
		 */
		public String getSessionId()
		{
			return sessionId;
		}

		/**
		 * Sets the maxSessionTimeoutInMinutes.
		 * @param maxSessionTimeoutInMinutes the maxSessionTimeoutInMinutes to set
		 */
		public void setMaxSessionTimeoutInMinutes(int maxSessionTimeoutInMinutes)
		{
			this.maxSessionTimeoutInMinutes = maxSessionTimeoutInMinutes;
		}

		/**
		 * Returns the maxSessionTimeoutInMinutes.
		 * @return the maxSessionTimeoutInMinutes
		 */
		public int getMaxSessionTimeoutInMinutes()
		{
			return maxSessionTimeoutInMinutes;
		}

		public String getStrAuthMethod() {
			return strAuthMethod;
		}

		public void setStrAuthMethod(String strAuthMethod) {
			this.strAuthMethod = strAuthMethod;
		}

	}

}
