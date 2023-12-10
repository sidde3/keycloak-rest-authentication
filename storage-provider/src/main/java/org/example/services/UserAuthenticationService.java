package org.example.services;

import jakarta.ws.rs.core.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.LegacyUserCredentialManager;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.SubjectCredentialManager;
import org.keycloak.models.UserModel;
import org.keycloak.storage.adapter.AbstractUserAdapter;
import org.keycloak.utils.MediaType;
import org.example.model.RequestModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserAuthenticationService {
    private static UserAuthenticationService service;
    protected Map<String, UserModel> users = new HashMap<>();

    private UserAuthenticationService() {
    }

    public static UserAuthenticationService getInstance() {
        if (service == null) {
            service = new UserAuthenticationService();
        }
        return service;
    }

    public UserModel createAdapter(KeycloakSession session, RealmModel realm, ComponentModel model, String username) {
        return new AbstractUserAdapter(session, realm, model) {
            @Override
            public String getUsername() {
                return username;
            }

            @Override
            public SubjectCredentialManager credentialManager() {
                return new LegacyUserCredentialManager(session, realm, this);
            }
        };
    }

    public boolean isExists(KeycloakSession session, String url, String username) {
        try {
            SimpleHttp.Response response = SimpleHttp
                    .doGet(url, session)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
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

        public boolean isValid (KeycloakSession session, RequestModel model, String url){
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
