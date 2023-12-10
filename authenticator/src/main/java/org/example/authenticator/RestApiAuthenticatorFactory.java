package org.example.authenticator;

import lombok.extern.slf4j.Slf4j;
import org.example.Constraints;
import org.keycloak.Config;
import org.keycloak.authentication.authenticators.browser.UsernamePasswordFormFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.authentication.Authenticator;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class RestApiAuthenticatorFactory extends UsernamePasswordFormFactory{

    @Override
    public Authenticator create(KeycloakSession session) {
        return new RestApiAuthenticator(session);
    }

    @Override
    public void init(Config.Scope config) {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        log.info("[{}], initialized..",this.getClass().getCanonicalName());
    }

    @Override
    public String getId() {
        return Constraints.PROVIDER_ID;
    }

    @Override
    public String getDisplayType() {
        return Constraints.DISPLAY_TYPE;
    }

    @Override
    public String getHelpText() {
        return Constraints.HELPER_TEXT;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return Arrays.asList(
                new ProviderConfigProperty(Constraints.URL, Constraints.LABEL, Constraints.HELP_TEXT, ProviderConfigProperty.STRING_TYPE, Constraints.DEFAULT_API)
        );
    }
    public boolean isConfigurable() {
        return true;
    }
    public org.keycloak.models.AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return Constraints.REQUIREMENT_CHOICES;
    }
}
