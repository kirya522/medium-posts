package hard.tests;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpTests {
    @Test
    public void basicHttpClient() {
        try {
            URL url = new URL("http://example.com/api");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Test
    public void apacheHttpClient() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet("http://example.com/api");

            HttpResponse response = httpClient.execute(request);

            String responseBody = EntityUtils.toString(response.getEntity());

            System.out.println(responseBody);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * https://www.baeldung.com/rest-template
     * https://docs.spring.io/spring-android/docs/current/reference/html/rest-template.html
     * https://www.digitalocean.com/community/tutorials/spring-resttemplate-example
     */
    @Test
    public void restTemplateExample() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("param1", "value1");
        map.add("param2", "value2");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://example.com/api", HttpMethod.POST, request, String.class);

        System.out.println(response.getBody());
    }

    /**
     * https://www.baeldung.com/spring-resttemplate-logging
     * https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#actuator.metrics.supported.http-clients
     * https://medium.com/@TimvanBaarsen/spring-boot-why-you-should-always-use-the-resttemplatebuilder-to-create-a-resttemplate-instance-d5a44ebad9e9
     * https://www.youtube.com/watch?v=cOncBTpFQW8
     * https://stackoverflow.com/questions/54066803/unable-to-see-resttemplate-metrics-using-micrometer
     * https://stackoverflow.com/questions/74937191/spring-boot-3-micrometer-integration-with-resttemplate
     */
    @Test
    public void monitorHttpClient() {
        // request + response
    }

    /**
     * https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Keep-Alive
     * https://medium.com/@yannic.luyckx/resttemplate-and-connection-pool-617ebd924f68
     */
    @Test
    public void keepAliveHttpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(20);
        connectionManager.setValidateAfterInactivity(5000);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        RestTemplate restTemplate = new RestTemplate(factory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("param1", "value1");
        map.add("param2", "value2");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange("http://example.com/api", HttpMethod.POST, request, String.class);

        System.out.println(response.getBody());
    }
}
