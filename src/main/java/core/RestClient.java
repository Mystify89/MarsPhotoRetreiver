package core;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Makes rest requests.
 */
public class RestClient {

    private final CloseableHttpClient httpClient;

    private static final int DEFAULT_TIMEOUT = 60 * 1000; //60 seconds

    public RestClient()   {
        int timeout = DEFAULT_TIMEOUT;
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout).build();
        this.httpClient = HttpClients.custom().setDefaultRequestConfig(config).build();
    }

    /**
     * Send an HTTP GET request to a service and return the response object if there is one.
     *
     * @param uri the resource URI
     * @param headers headers to set explicitly, or null
     * @return the response body if there is one
     * @throws IOException if there is a failure.
     */
    public Optional<String> httpGet(URI uri, Map<String, String> headers)
           throws IOException {
        HttpGet httpGet = new HttpGet(uri);
        if (headers != null) {
            headers.forEach(httpGet::setHeader);
        }
        return httpRequest(httpGet);
    }

    private Optional<String> httpRequest(HttpEntityEnclosingRequestBase request, Map<String, String> headers)
            throws IOException {
        if (headers != null) {
            headers.forEach(request::setHeader);
        }

        return httpRequest(request);
    }

    private Optional<String> httpRequest(HttpUriRequest request) throws IOException{
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            Optional<HttpEntity> responseEntity = Optional.ofNullable(response.getEntity());
            int code = response.getStatusLine().getStatusCode();
            //Return results for 2xx response code
            if (code >= 200 && code < 300) {
                if (!responseEntity.isPresent()) {
                    return Optional.empty();
                }
                return Optional.of(EntityUtils.toString(responseEntity.get()));
            }

            throw new IOException("Unexpected response status code: " + code);
        }
    }
}
