package nrls.adapter.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SslConfig {

    // Certificates & SSL
    @Value("${server.ssl.key-store}")
    private String keystorePath;
    @Value("${server.ssl.key-store-password}")
    private String keystorePassword;
    
    @Value("${server.ssl.trust-store}")
    private String truststorePath;
    @Value("${server.ssl.trust-store-password}")
    private String truststorePassword;
    
    @Bean
    public RestTemplate getRestTemplate() {
        
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            keyStore.load(new FileInputStream(new File(keystorePath)), keystorePassword.toCharArray());

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                    new SSLContextBuilder()
                            .loadTrustMaterial(ResourceUtils.getFile(truststorePath), truststorePassword.toCharArray())
                            .loadKeyMaterial(keyStore, keystorePassword.toCharArray())
                            .build());

            HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();

            ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            setupResponseErrorHandler(restTemplate);
            
            return restTemplate;
        } catch (Exception certEx) {
            certEx.printStackTrace();
        }
        
        RestTemplate restTemplate = new RestTemplate();
        setupResponseErrorHandler(restTemplate);
        return restTemplate;
    }
    
    public RestTemplate setupResponseErrorHandler(RestTemplate restTemplate) {
    	restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false; // or whatever you consider an error
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // do nothing, or something
            }
        });
        
        return restTemplate;
    }

}
