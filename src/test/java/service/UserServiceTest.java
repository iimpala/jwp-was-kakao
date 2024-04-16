package service;

import db.DataBase;
import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserServiceTest {

    @Test
    void 사용자_정보를_DB에_저장한다() {
        //given
        UserService service = new UserService();

        String userId = "cu";
        String password = "password";
        String name = "이동규";
        String email = "brainbackdoor@gmail.com";
        UserCreateDto userCreateDto = new UserCreateDto(userId, password, name, email);

        //when
        User user = service.save(userCreateDto);

        //then
        Assertions.assertThat(DataBase.findUserById(userId)).isEqualTo(user);
    }

    @Test
    void DB에_사용자가_있으면_로그인_성공() {
        //given
        UserService service = new UserService();
        String userId = "cu";
        String password = "password";
        String name = "이동규";
        String email = "brainbackdoor@gmail.com";
        UserCreateDto userCreateDto = new UserCreateDto(userId, password, name, email);
        User savedUser = service.save(userCreateDto);

        //when
        User loginUser = service.login(new UserLoginDto(userId, password));

        //then
        Assertions.assertThat(loginUser).isEqualTo(savedUser);
    }

    @Test
    void 로그인에_실패하면_예외를_던진다() {
        //given
        UserService service = new UserService();
        String userId = "cu";
        String password = "password";
        String name = "이동규";
        String email = "brainbackdoor@gmail.com";
        UserCreateDto userCreateDto = new UserCreateDto(userId, password, name, email);
        User savedUser = service.save(userCreateDto);

        //when
        //then
        Assertions.assertThatThrownBy(() -> service.login(new UserLoginDto(userId, "wrongPassword")))
                .isInstanceOf(RuntimeException.class);
        Assertions.assertThatThrownBy(() -> service.login(new UserLoginDto("wrongUserId", "pw")))
                .isInstanceOf(RuntimeException.class);
    }
}
