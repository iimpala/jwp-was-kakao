package service;

import db.DataBase;
import model.User;
import model.Users;
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
        service.save(userCreateDto);

        //when
        //then
        Assertions.assertThatThrownBy(() -> service.login(new UserLoginDto(userId, "wrongPassword")))
                .isInstanceOf(RuntimeException.class);
        Assertions.assertThatThrownBy(() -> service.login(new UserLoginDto("wrongUserId", "pw")))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void 전체_사용자를_조회한다() {
        //given
        UserService service = new UserService();
        User user1 = service.save(new UserCreateDto("cu1", "pw", "이동규1", "cu@gmail.com"));
        User user2 = service.save(new UserCreateDto("cu2", "pw", "이동규2", "cu@gmail.com"));
        User user3 = service.save(new UserCreateDto("cu3", "pw", "이동규3", "cu@gmail.com"));

        //when
        Users users = service.findAll();

        //then
        Assertions.assertThat(users.getUsers().size()).isEqualTo(3);
        Assertions.assertThat(users.getUsers()).containsExactlyInAnyOrder(user1, user2, user3);
    }
}
