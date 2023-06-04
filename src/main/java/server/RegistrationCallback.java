package server;

import java.util.ArrayList;
import java.util.Set;

public interface RegistrationCallback {
    void onRegistration(String username);
    void getNewMessages(Message message);
    void getUser(String username, boolean status);
    void getList(Set<String> onlineNames);
}
