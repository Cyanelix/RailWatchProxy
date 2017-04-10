package com.cyanelix.railwatch.darwin.client;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.oxm.Marshaller;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;

public class AccessTokenWebServiceMessageCallbackTest {
    @Test
    public void doWithMessage_shouldAddToken() throws IOException {
        // Given...
        Marshaller marshaller = mock(Marshaller.class);

        SoapHeaderElement soapHeaderElement = mock(SoapHeaderElement.class);
        SoapHeader soapHeader = mock(SoapHeader.class);
        given(soapHeader.addHeaderElement(any(QName.class))).willReturn(soapHeaderElement);

        SoapMessage soapMessage = mock(SoapMessage.class);
        given(soapMessage.getSoapHeader()).willReturn(soapHeader);

        String token = "token";
        AccessTokenWebServiceMessageCallback callback = new AccessTokenWebServiceMessageCallback("action", marshaller,
                token);

        // When...
        callback.doWithMessage(soapMessage);

        // Then...
        ArgumentCaptor<String> headerValueCaptor = ArgumentCaptor.forClass(String.class);
        verify(soapHeaderElement).setText(headerValueCaptor.capture());

        assertThat(headerValueCaptor.getValue(), is(token));
    }

}
