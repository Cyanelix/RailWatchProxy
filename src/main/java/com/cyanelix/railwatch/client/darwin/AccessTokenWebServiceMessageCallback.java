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
    private final AccessToken accessToken;

    public AccessTokenWebServiceMessageCallback(String soapAction, Marshaller marshaller, String token) {
        super(soapAction);
        this.marshaller = marshaller;
        this.accessToken = new AccessToken();
        this.accessToken.setTokenValue(token);
    }

    @Override
    public void doWithMessage(WebServiceMessage message) throws IOException {
        super.doWithMessage(message);

        SoapMessage soapMessage = (SoapMessage) message;
        SoapHeader soapHeader = soapMessage.getSoapHeader();

        com.thalesgroup.rtti._2013_11_28.token.types.ObjectFactory typesObjectFactory = new com.thalesgroup.rtti._2013_11_28.token.types.ObjectFactory();
        JAXBElement<AccessToken> createAccessToken = typesObjectFactory.createAccessToken(accessToken);

        marshaller.marshal(createAccessToken, soapHeader.getResult());

        soapHeader.addHeaderElement(new QName("http://thalesgroup.com/RTTI/2013-11-28/Token/types", "AccessToken"))
                .setText(accessToken.getTokenValue());
    }

}
