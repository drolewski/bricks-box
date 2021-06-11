package pl.rolex.bricksbox.service.user;

import pl.rolex.bricksbox.model.User;

public final class EmptyUserSingleton {
    private static User user;

    public static User getInstance() {
        if (user == null) {
            user = new User();
        }
        return user;
    }
}
