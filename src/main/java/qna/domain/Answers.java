package qna.domain;

import org.hibernate.annotations.Where;
import qna.CannotDeleteException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Embeddable
public class Answers {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade = CascadeType.PERSIST)
    @Where(clause = "deleted = false")
    public List<Answer> answers = new ArrayList<>();

    public Answers() {
    }

    public void validateDeletableCondition(User loginUser) throws CannotDeleteException {
        long answerCountsByOtherUser = answers.stream()
                .filter(answer -> !answer.isOwner(loginUser))
                .count();
        if (answerCountsByOtherUser != 0) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    public Stream<DeleteHistory> delete() {
        return answers.stream()
                .map(Answer::delete);
    }

    public void add(Answer answer) {
        answers.add(answer);
    }

    public List<Answer> list() {
        return answers;
    }
}
