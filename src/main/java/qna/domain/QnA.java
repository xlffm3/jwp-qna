package qna.domain;

import qna.CannotDeleteException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QnA {

    private final Question question;
    private final Answers answers;

    public QnA(Question question, Answers answers) {
        this.question = question;
        this.answers = answers;
    }

    public List<DeleteHistory> delete(User loginUser) throws CannotDeleteException {
        validateDeleteCondition(loginUser);
        return Stream.concat(Stream.of(question.delete()), answers.delete())
                .collect(Collectors.toList());
    }

    private void validateDeleteCondition(User loginUser) throws CannotDeleteException {
        question.isDeletable(loginUser);
        answers.isDeletable(loginUser);
    }
}
