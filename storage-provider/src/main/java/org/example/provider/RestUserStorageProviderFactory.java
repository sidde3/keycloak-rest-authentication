package org.example.provider;

import lombok.extern.slf4j.Slf4j;
import org.example.Constraints;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.storage.UserStorageProviderFactory;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class RestUserStorageProviderFactory implements UserStorageProviderFactory<RestUserStorageProvider> {

    @Override
    public RestUserStorageProvider create(KeycloakSession keycloakSession, ComponentModel componentModel) {
        return new RestUserStorageProvider(keycloakSession, componentModel);
    }

    @Override
    public String getId() {
        return Constraints.PROVIDER_ID;
    }

    @Override
    public String getHelpText() {
        return Constraints.HELPER_TEXT;
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        log.info("[{}] initialized...",this.getClass().getCanonicalName());
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return Arrays.asList(
                new ProviderConfigProperty(Constraints.BASE_URL, Constraints.BASE_API_LABEL, Constraints.BASE_HELPER_TEXT, ProviderConfigProperty.STRING_TYPE, Constraints.BASE_API),
                new ProviderConfigProperty(Constraints.AUTH_URL, Constraints.AUTH_API_LABEL, Constraints.AUTH_HELPER_TEXT, ProviderConfigProperty.STRING_TYPE, Constraints.AUTH_API));
    }

}
