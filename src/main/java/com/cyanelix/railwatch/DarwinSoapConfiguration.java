package com.cyanelix.railwatch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.cyanelix.railwatch.darwin.client.DarwinClient;

@Configuration
public class DarwinSoapConfiguration {
    @Value("${darwin.access.token}")
    private String accessToken;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPaths("com.thalesgroup.rtti._2007_10_10.ldb.commontypes",
                "com.thalesgroup.rtti._2012_01_13.ldb.types", "com.thalesgroup.rtti._2013_11_28.token.types",
                "com.thalesgroup.rtti._2015_11_27.ldb.types", "com.thalesgroup.rtti._2016_02_16.ldb",
                "com.thalesgroup.rtti._2016_02_16.ldb.types");
        marshaller.setCheckForXmlRootElement(false);
        return marshaller;
    }

    @Bean
    public DarwinClient darwinClient(Jaxb2Marshaller marshaller) {
        DarwinClient client = new DarwinClient(accessToken);
        client.setDefaultUri("https://lite.realtime.nationalrail.co.uk/OpenLDBWS/ldb9.asmx");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
