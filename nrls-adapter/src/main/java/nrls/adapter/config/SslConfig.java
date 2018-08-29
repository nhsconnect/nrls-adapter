package nrls.adapter.config;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SslConfig {

    // Certificates & SSL
    @Value("${server.ssl.key-store}")
    private String keystorePath;
    @Value("${server.ssl.key-store-password}")
    private String keystorePassword;

    @Autowired
    private RestTemplateBuilder builder;
    
    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
        
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            keyStore.load(new FileInputStream(new File(keystorePath)), keystorePassword.toCharArray());

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                    new SSLContextBuilder()
                            .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                            .loadKeyMaterial(keyStore, keystorePassword.toCharArray())
                            .build());

            HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();

            ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

            return new RestTemplate(requestFactory);
        } catch (Exception certEx) {
            certEx.printStackTrace();
        }

        return new RestTemplate();
    }

}
