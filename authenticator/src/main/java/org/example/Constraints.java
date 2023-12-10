package org.example;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.AuthenticationExecutionModel.Requirement;

public class Constraints {
    public static final String PROVIDER_ID = "rest-api-authenticator";
    public static final String DISPLAY_TYPE = "Username Password REST Form";
    public static final String HELPER_TEXT = "Validates a username and password from login form against a REST API";
    public static final String URL = "url";
    public static final String LABEL = "REST API";
    public static final String HELP_TEXT = "Mobile Authenticator API URL.";
    public static final String DEFAULT_API = "http://localhost:9080/va/auth";
    public enum METHOD { POST};
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String ACCEPT = "Accept";

    public static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES =
            new AuthenticationExecutionModel.Requirement[]{Requirement.REQUIRED, Requirement.ALTERNATIVE, Requirement.DISABLED};
}
