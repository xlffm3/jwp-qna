package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import qna.config.JpaConfig;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JpaConfig.class))
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User javajigi;
    private User sanjigi;

    @BeforeEach
    void addUser() {
        javajigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
        sanjigi = new User("sanjigi", "password", "name", "sanjigi@slipp.net");
        userRepository.save(UserTest.JAVAJIGI);
        userRepository.save(UserTest.SANJIGI);
    }

    @AfterEach
    void removeUser() {
        userRepository.delete(javajigi);
        userRepository.delete(sanjigi);
        entityManager.flush();
    }

    @DisplayName("객체 그래프를 탐색한다.")
    @Test
    void findAnswers() {
        Question question = new Question("title1", "content1").writeBy(UserTest.JAVAJIGI);
        Answer answer1 = new Answer(UserTest.SANJIGI, question, "answer1");
        Answer answer2 = new Answer(UserTest.SANJIGI, question, "answer2");
        question.addAnswer(answer1);
        question.addAnswer(answer2);

        questionRepository.save(question);
        List<Answer> answers = questionRepository.findById(question.getId())
                .orElseThrow(IllegalArgumentException::new)
                .getAnswers();

        assertThat(answers).containsExactly(answer1, answer2);
    }
}
