package org.prgms.springbootboardjpayu.service;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.prgms.springbootboardjpayu.dto.request.CreateUserRequest;
import org.prgms.springbootboardjpayu.dto.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    private UserService userService;

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
    @EmptySource
    void createUserWithBlankName(String blankName) {
        // given
        CreateUserRequest request = new CreateUserRequest(blankName, 23, "낮잠 자기");

        // when then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @DisplayName("이름을 2 ~ 30자 범위를 초과 입력해 유저 생성에 실패한다.")
    @ParameterizedTest(name = "{0}는 이름 범위를 벗어난다.")
    @ValueSource(strings = {"a"})
    @MethodSource("provideLongName")
    void createUserWithOutOfRangeName(String outOfRangeName) {
        // given
        CreateUserRequest request = new CreateUserRequest(outOfRangeName, 23, "낮잠 자기");

        // when then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(ConstraintViolationException.class);

    }

    @DisplayName("나이가 0 ~ 200 범위를 초과 입력해 유저 생성에 실패한다.")
    @ParameterizedTest(name = "{index}. {0}은 나이의 범위를 초과한다.")
    @ValueSource(ints = {-1, 201})
    void createUserWithOutOfRangeAge(int outOfRangeAge) {
        // given
        CreateUserRequest request = new CreateUserRequest("의진", outOfRangeAge, "낮잠 자기");

        // when then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(ConstraintViolationException.class);

    }

    private static List<String> provideLongName() {
        String string = "a";
        return List.of(string.repeat(31));
    }
}