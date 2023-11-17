package org.prgms.springbootboardjpayu.service;

import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.prgms.springbootboardjpayu.dto.request.CreateUserRequest;
import org.prgms.springbootboardjpayu.dto.response.UserResponse;
import org.prgms.springbootboardjpayu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저 생성에 성공한다.")
    @Test
    void createUser() {
        // given
        String name = "의진";
        Integer age = 23;
        String hobby = "낮잠 자기";
        CreateUserRequest request = new CreateUserRequest(name, age, hobby);

        // when
        UserResponse savedUser = userService.createUser(request);

        // then
        assertThat(savedUser.createdAt()).isNotNull();
        assertThat(savedUser).extracting("name", "age", "hobby")
                .containsExactly(name, age, hobby);
    }

    @DisplayName("빈 이름을 입력해 유저 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void createUserWithBlankName(String name) {
        // given
        CreateUserRequest request = new CreateUserRequest(name, 23, "낮잠 자기");

        // then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(ConstraintViolationException.class);

    }

    @DisplayName("이름을 2 ~ 30자 범위를 초과 입력해 유저 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {"a", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    void createUserWithOutOfRangeName(String name) {
        // given
        CreateUserRequest request = new CreateUserRequest(name, 23, "낮잠 자기");

        // then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(ConstraintViolationException.class);

    }

    @DisplayName("나이가 0 ~ 200 범위를 초과 입력해 유저 생성에 실패한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 201})
    void createUserWithOutOfRangeAge(int age) {
        // given
        CreateUserRequest request = new CreateUserRequest("의진", age, "낮잠 자기");

        // then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(ConstraintViolationException.class);

    }

}