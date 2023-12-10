package org.example.authenticator;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.Constraints;
import org.example.model.AuthenticationRequestModel;
import org.example.model.RestUserModel;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.authenticators.browser.UsernamePasswordForm;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.services.managers.AuthenticationManager;
import org.keycloak.utils.MediaType;
import java.io.IOException;
import java.util.UUID;


@Slf4j
public class RestApiAuthenticator extends UsernamePasswordForm {
    private KeycloakSession session;
    public RestApiAuthenticator(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        log.info("action is executing..");
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        if (formData.containsKey("cancel")) {
            context.cancelLogin();
            return;
        }
        String username = formData.getFirst(AuthenticationManager.FORM_USERNAME);
        String password = formData.getFirst(CredentialRepresentation.PASSWORD);
        String url = context.getAuthenticatorConfig().getConfig().get(Constraints.URL);
        boolean isAuthScuccess = validateCredential(new AuthenticationRequestModel(username,password),url);

        if(!isAuthScuccess){
            log.error("Invalid credential");
            Response res = context.form().setError("Invalid credential",new Object[0]).createLoginUsernamePassword();
            context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, res);
            return;
        }
        //Validate the remote user with local storage
        UserModel user = session.users().getUserByUsername(context.getRealm(), username);
        if(user == null){
            //log.error("User does not exists in keycloak repo");
            //Response res = context.form().setError("Invalid user",new Object[0]).createLoginUsernamePassword();
            //context.failureChallenge(AuthenticationFlowError.INVALID_USER, res);
            //return;

            //Comment the user creation section if user is to be valided with a user-storage provider e.g. Ldap
            log.error("User does not exists in keycloak repo, will try to create");
            user = session.users().addUser(context.getRealm(), username);
            user.setEnabled(true);
        }

        context.setUser(user);
        context.success();
    }

    private boolean validateCredential(AuthenticationRequestModel model, String url){
        log.info("Authenticating [{}]", model.getUsername());
        try {
            SimpleHttp.Response response = SimpleHttp
                    .doPost(url, session)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .json(model)
                    .acceptJson()
                    .asResponse();
            log.info("Status [{}]", response.getStatus());
            if (response.getStatus() == 200) {
                return Boolean.valueOf(response.asString());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
        return false;
    }
}
