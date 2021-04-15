package web.webServices;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
