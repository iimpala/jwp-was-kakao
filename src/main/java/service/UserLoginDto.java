package service;

import java.util.Arrays;

public class UserLoginDto {

    private final String userId;
    private final String password;

    public UserLoginDto(String userId, String password) {
        validate(userId, password);
        this.userId = userId;
        this.password = password;
    }

    private void validate(String... inputValues) {
        Arrays.stream(inputValues)
                .forEach(this::validateString);
    }

    private void validateString(String input) {
        if (input.isBlank() || input.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] input validation failed");
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }
}
