package service;

import db.DataBase;
import model.User;

public class UserService {

    public User save(UserDto userDto) {
        User user = new User(userDto.getUserId(),
                userDto.getPassword(),
                userDto.getName(),
                userDto.getEmail());

        DataBase.addUser(user);
        return user;
    }
}
