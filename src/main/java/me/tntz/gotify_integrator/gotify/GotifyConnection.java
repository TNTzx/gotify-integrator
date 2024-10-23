package me.tntz.gotify_integrator.gotify;

import me.tntz.gotify_integrator.Gotify_integrator;
import me.tntz.gotify_integrator.tools.Config;
import me.tntz.gotify_integrator.tools.ConfigManager;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class GotifyConnection {

    //here should set HttpConnectionManagerParams but not important for you
    private static final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    private static URI getUri() throws IOException, URISyntaxException {
        Config config = ConfigManager.getConfig();
        URI oldURI = new URI(config.serverURL);

        return new URIBuilder()
                .setScheme(oldURI.getScheme())
                .setUserInfo(oldURI.getUserInfo())
                .setHost(oldURI.getHost())
                .setPort(config.port)
                .setPath(oldURI.getPath() + "/message")
                .setFragment(oldURI.getFragment())
                .addParameter("token", config.appToken)
                .build();
    }


    private static boolean isEnabled() throws IOException {
        return ConfigManager.getConfig().enabled;
    }

    @SuppressWarnings("unused")
    public static void sendMessage(String title, String message) throws IOException, URISyntaxException {
        sendMessage(title, message, GotifyPriorityLevel.NORMAL);
    }
    public static void sendMessage(String title, String message, GotifyPriorityLevel priorityLevel) throws IOException, URISyntaxException {
        if (!isEnabled()) return;
        if (!GotifyPriorityLevelManager.isQualified(priorityLevel)) return;

        HttpPost req = new HttpPost(getUri());

        HttpEntity payload = MultipartEntityBuilder.create()
                .addPart("title", new StringBody(title, ContentType.TEXT_PLAIN))
                .addPart("message", new StringBody(message, ContentType.TEXT_PLAIN))
                .addPart("priority", new StringBody(Integer.toString(priorityLevel.getValue()), ContentType.TEXT_PLAIN))
                .build();
        req.setEntity(payload);

        HttpClientResponseHandler<String> responseHandler = new BasicHttpClientResponseHandler();
        String response = httpClient.execute(req, responseHandler);
        Gotify_integrator.LOGGER.info("Sent message of priority {}: {} | {} || {}", priorityLevel.name(), title, message, response);
    }
}
