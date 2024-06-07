package ispyb.common.util;

import javax.security.auth.callback.*;
import java.io.IOException;
import java.util.Arrays;

public class UsernamePasswordHandler implements CallbackHandler {
    private final String userName;
    private final String password;

    public UsernamePasswordHandler(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        getCallback(callbacks, NameCallback.class).setName(userName);
        getCallback(callbacks, PasswordCallback.class).setPassword(password.toCharArray());
    }

    private <T> T getCallback(Callback[] callbacks, Class<T> clazz) {
        return Arrays.stream(callbacks).filter(callback -> callback.getClass().isAssignableFrom(clazz)).map(callback -> (T) callback).findAny().get();
    }
}
