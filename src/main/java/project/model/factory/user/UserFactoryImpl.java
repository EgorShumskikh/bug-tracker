package project.model.factory.user;

import project.model.user.User;

public class UserFactoryImpl implements UserFactory{

    private static UserFactoryImpl factory = new UserFactoryImpl();

    public static UserFactoryImpl getInstance() {
        return factory;
    }

    @Override
    public User createUser() {
        User user = new User();

        return user;
    }
}