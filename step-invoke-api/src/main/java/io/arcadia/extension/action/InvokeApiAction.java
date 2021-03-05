package io.arcadia.extension.action;

import io.arcadia.extension.model.MockObject;
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

public class InvokeApiAction implements Step {
    private static Logger logger = LoggerFactory.getLogger(InvokeApiAction.class);

    private String apiURL;

    public String getApiURL() {
        return apiURL;
    }

    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
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

                MockObject mockObject = MockObject.fromJson(body);
                StringEntity input = new StringEntity(mockObject.toJson());
                post.setHeader("content-type", "application/json");
                post.setEntity(input);
                HttpResponse response = client.execute(post);
                logger.info("HTTP Response: " + response.getStatusLine());
                if (500 != response.getStatusLine().getStatusCode()) {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line = rd.readLine();
                    logger.info("Response from POST request: " + line);
                    mockObject = MockObject.fromJson(line);
                    id = mockObject.getId();
                    status = "SUCCESS";

                    appId = mockObject.getApplicationId();
                    program = mockObject.getDesiredProgram();
                    first = mockObject.getFirstName();
                    last = mockObject.getLastName();

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
