package io.arcadia.extension.action;

import io.arcadia.extension.model.PersonApp;
import io.syndesis.extension.api.Step;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.util.ObjectHelper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

public class InvokeNifiApiAction implements Step {
    private static Logger logger = LoggerFactory.getLogger(InvokeNifiApiAction.class);

    private String apiURL;
    private String xLingkHeader;

    public String getApiURL() {
        return apiURL;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }

    public String getxLingkHeader() {
        return xLingkHeader;
    }

    public void setxLingkHeader(String xLingkHeader) {
        this.xLingkHeader = xLingkHeader;
    }

    @Override
    public Optional<ProcessorDefinition<?>> configure(CamelContext camelContext, ProcessorDefinition<?> route, Map<String, Object> map) {

        ObjectHelper.notNull(route, "route");
        route.setBody(exchange -> {
            String body = exchange.getIn().getBody(String.class);
            String status = "FAILURE";
            String id = "";
            String appId = "";
            String program = "";
            String first = "";
            String last = "";
            String synced = "false";

            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(getApiURL());

                PersonApp personApp = PersonApp.fromJson(body);
                StringEntity input = new StringEntity(personApp.toJson());
                post.setHeader("content-type", "application/json");
                post.setHeader("X-Lingk-Context", getxLingkHeader());
                post.setEntity(input);
                HttpResponse response = client.execute(post);
                logger.info("HTTP Response: " + response.getStatusLine());
                if (500 != response.getStatusLine().getStatusCode()) {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line = rd.readLine();
                    logger.info("Response from POST request: " + line);
                    personApp = PersonApp.fromJson(line);
                    id = personApp.getId();
                    status = "SUCCESS";

                    appId = personApp.getApplicationId();
                    program = personApp.getDesiredProgram();
                    first = personApp.getFirstName();
                    last = personApp.getLastName();

                    synced = "true";
                }
            } catch (IOException e) {
                logger.error("Failed to POST data", e);
            }
            return String.format(
                "{\"status\":\"%s\", \"id\":\"%s\", \"applicationId\": \"%s\", \"desiredProgram\": \"%s\", \"firstName\": \"%s\", \"lastName\": \"%s\", \"synced\": \"%s\"}",
                status, id, appId, program, first, last, synced);
        });
        return Optional.empty();
    }
}
