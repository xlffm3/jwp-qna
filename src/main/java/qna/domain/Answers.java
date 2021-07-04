package qna.domain;

import qna.CannotDeleteException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Answers {

    private final List<Answer> answers;

    public Answers(List<Answer> answers) {
        this.answers = answers;
    }

    public void isDeletable(User loginUser) throws CannotDeleteException {
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
}
