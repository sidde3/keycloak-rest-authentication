package org.example.services;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DirectoryService {
    private static Map<String,String> usersMap = new HashMap<>();
    private static DirectoryService ds = null;

    static {
        usersMap.put("sidde","secret");
        usersMap.put("atiwari","secret");
    }

    private DirectoryService(){
    }

    public static DirectoryService getInstance(){
        if(ds == null){
            ds = new DirectoryService();
        }
        return ds;
    }

    public boolean isValid(String username, String password){
        if(password.equals(usersMap.get(username))){
            log.info("Authentication Success: [{}]",username);
            return true;
        }
        return false;
    }

    public boolean isUserExists(String username){
        return usersMap.containsKey(username);
    }
}
