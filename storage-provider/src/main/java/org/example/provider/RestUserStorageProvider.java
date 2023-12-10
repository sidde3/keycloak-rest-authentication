package org.example.provider;

import lombok.extern.slf4j.Slf4j;
import org.example.Constraints;
import org.example.model.RequestModel;
import org.example.services.UserAuthenticationService;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialInputValidator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.storage.StorageId;
import org.keycloak.storage.UserStorageProvider;
import org.keycloak.storage.user.UserLookupProvider;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RestUserStorageProvider implements UserStorageProvider, UserLookupProvider,CredentialInputValidator{
    private ComponentModel model;
    private KeycloakSession session;
    private String validateUrl;
    private String authUrl;
    protected Map<String, UserModel> loadedUsers = new HashMap<>();
    public RestUserStorageProvider(KeycloakSession session, ComponentModel model){
        this.session = session;
        this.model = model;
        this.validateUrl = model.getConfig().getFirst(Constraints.BASE_URL);
        this.authUrl = model.getConfig().getFirst(Constraints.AUTH_URL);
    }

    @Override
    public void close() {

    }

    @Override
    public boolean supportsCredentialType(String credentialType) {
        return credentialType.equals(PasswordCredentialModel.TYPE);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realmModel, UserModel userModel, String s) {
        log.info("Validating existance [{}]",userModel.getUsername());
        return UserAuthenticationService.getInstance().isExists(session, validateUrl+"/"+userModel.getUsername(),userModel.getUsername());
    }

    @Override
    public boolean isValid(RealmModel realmModel, UserModel userModel, CredentialInput credentialInput) {
        log.info("Validating authentication [{}]",userModel.getUsername());
        return UserAuthenticationService.getInstance().isValid(session,new RequestModel(userModel.getUsername(),credentialInput.getChallengeResponse()),authUrl);
    }

    @Override
    public UserModel getUserById(RealmModel realm, String id) {
        StorageId storageId = new StorageId(id);
        String username = storageId.getExternalId();
        return getUserByUsername(realm, username);
    }

    @Override
    public UserModel getUserByUsername(RealmModel realm, String username) {
        UserModel adapter = loadedUsers.get(username);
        if (adapter == null) {
            adapter = UserAuthenticationService.getInstance().createAdapter(session,realm,model,username);
            loadedUsers.put(username,adapter);
        }
        return adapter;
    }

    @Override
    public UserModel getUserByEmail(RealmModel realm, String email) {
        return null;
    }
}
