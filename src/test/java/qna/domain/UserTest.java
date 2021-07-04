package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import qna.config.JpaConfig;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JpaConfig.class))
public class UserTest {
    public static final User JAVAJIGI = new User("javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

    @Autowired
    private UserRepository userRepository;

    @DisplayName("User를 저장 및 조회한다.")
    @Test
    void saveAndFind() {
        LocalDateTime before = LocalDateTime.now();

        User savedUser = userRepository.save(JAVAJIGI);
        LocalDateTime createdAt = savedUser.getCreatedAt();
        String email = savedUser.getEmail();

        assertThat(createdAt).isAfter(before);
        assertThat(email).isEqualTo(JAVAJIGI.getEmail());
    }

    @DisplayName("Dirty Checking을 통해 User를 수정한다.")
    @Test
    void update() {
        User user = new User("jipark", "pass", "jinhong", "abc@naver.com");
        userRepository.save(user);

        user.setName("park");

        String name = userRepository.findByUserId(user.getUserId()) //이 때 쓰기 지연 저장소의 쿼리들이 날아간다
                .orElseThrow(IllegalArgumentException::new)
                .getName();

        assertThat(name).isEqualTo("park");

    }
}
