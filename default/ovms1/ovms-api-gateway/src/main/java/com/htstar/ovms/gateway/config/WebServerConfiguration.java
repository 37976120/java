package com.htstar.ovms.gateway.config;

/**
 * Description:
 * Author: hsl
 * Date: Created in 2020/11/10
 * Company: 航通星空
 * Modified By:
 */

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;

public class WebServerConfiguration {

    @Bean
    public TomcatServletWebServerFactory createEmbeddedServletContainerFactory() {
        TomcatServletWebServerFactory tomcatFactory = new TomcatServletWebServerFactory();
        //tomcatFactory.setPort(8082);
        tomcatFactory.addConnectorCustomizers();
        tomcatFactory.addConnectorCustomizers(new MyTomcatConnectorCustomizer());
        return tomcatFactory;
    }
}

class MyTomcatConnectorCustomizer implements TomcatConnectorCustomizer {
    public void customize(Connector connector) {
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        protocol.setAcceptCount(2000);
        protocol.setAcceptorThreadCount(2);

        protocol.setMaxThreads(100);
        protocol.setMinSpareThreads(100);
        protocol.setMaxConnections(2000);
        protocol.setConnectionTimeout(5000);
    }
}
