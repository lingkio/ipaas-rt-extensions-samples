package io.arcadia.extension.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class PersonApp {
    private String id;
    private String applicationId;
    private String desiredProgram;
    private String firstName;
    private String lastName;

    public PersonApp() {
    }

    public PersonApp(String applicationId, String desiredProgram, String firstName, String lastName) {
        this.applicationId = applicationId;
        this.desiredProgram = desiredProgram;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getDesiredProgram() {
        return desiredProgram;
    }

    public void setDesiredProgram(String desiredProgram) {
        this.desiredProgram = desiredProgram;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public static PersonApp fromJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(json, Map.class);

        PersonApp personApp = new PersonApp();
        personApp.setApplicationId(checkAndGet(map, "applicationId"));
        personApp.setDesiredProgram(checkAndGet(map, "desiredProgram"));
        personApp.setFirstName(checkAndGet(map, "firstName"));
        personApp.setLastName(checkAndGet(map, "lastName"));

        personApp.setId(checkAndGet(map, "id"));

        return personApp;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert", e);
        }
    }

    @Override
    public String toString() {
        return new StringBuilder().append("MockObject{").append("applicationId='").append(applicationId).append('\'')
                                  .append(", desiredProgram='").append(desiredProgram).append('\'')
                                  .append(", firstName='").append(firstName).append('\'').append(", lastName='")
                                  .append(lastName).append('\'').append('}').toString();
    }

    private static String checkAndGet(Map map, String key) {
        return map.get(key) != null ? map.get(key).toString() : "";
    }
}
