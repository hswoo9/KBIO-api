package egovframework.com.devjitsu.config;

import java.util.HashMap;
import java.util.Map;

public class LoginUsers {
    private final static Map uniqueUsers = new HashMap();

    public static int getSize(){
        return uniqueUsers.size();
    }

    public static UserSessionBinding getUser(String id){
        return (UserSessionBinding) uniqueUsers.get(id);
    }

    static void removeUser(String id){
        uniqueUsers.remove(id);
    }

    static void setUser(String id, UserSessionBinding user){
        uniqueUsers.put(id, user);
    }
}
