package service;

import db.DataBase;
import model.User;

public class UserService {

    public User save(UserCreateDto userCreateDto) {
        User user = new User(userCreateDto.getUserId(),
                userCreateDto.getPassword(),
                userCreateDto.getName(),
                userCreateDto.getEmail());

        DataBase.addUser(user);
        return user;
    }

    public User login(UserLoginDto userLoginDto) {
        User user = DataBase.findUserById(userLoginDto.getUserId());
        if (user.getPassword().equals(userLoginDto.getPassword())) {
            return user;
        }

        throw new IllegalArgumentException("Login Failed");
    }
}

