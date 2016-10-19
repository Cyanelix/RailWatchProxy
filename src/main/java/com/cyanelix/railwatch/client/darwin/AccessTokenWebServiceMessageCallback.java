package com.cyanelix.railwatch.client.darwin;

import java.io.IOException;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.springframework.oxm.Marshaller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.thalesgroup.rtti._2013_11_28.token.types.AccessToken;

public class AccessTokenWebServiceMessageCallback extends SoapActionCallback {
	private final Marshaller marshaller;

	public AccessTokenWebServiceMessageCallback(String soapAction, Marshaller marshaller) {
		super(soapAction);
		this.marshaller = marshaller;
	}

	@Override
	public void doWithMessage(WebServiceMessage message) throws IOException {
		super.doWithMessage(message);

		AccessToken accessToken = new AccessToken();
		accessToken.setTokenValue("886912ae-4e59-4326-9745-8c9e43642e1f");

		SoapMessage soapMessage = (SoapMessage) message;
		SoapHeader soapHeader = soapMessage.getSoapHeader();
		
		com.thalesgroup.rtti._2013_11_28.token.types.ObjectFactory typesObjectFactory = new com.thalesgroup.rtti._2013_11_28.token.types.ObjectFactory();
		JAXBElement<AccessToken> createAccessToken = typesObjectFactory.createAccessToken(accessToken);
		
		marshaller.marshal(createAccessToken, soapHeader.getResult());

		soapHeader.addHeaderElement(new QName("http://thalesgroup.com/RTTI/2013-11-28/Token/types", "AccessToken"))
			.setText("886912ae-4e59-4326-9745-8c9e43642e1f");
	}

}
