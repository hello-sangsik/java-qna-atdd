package codesquad.dto;

import codesquad.domain.Answer;
import codesquad.domain.User;

/**
 * @author sangsik.kim
 */
public class AnswerDto {
    private long id;
    private String contents;
    private User writer;
    private long questionId;

    public AnswerDto() {
    }

    public AnswerDto(long questionId, String contents) {
        this.questionId = questionId;
        this.contents = contents;
    }

    public AnswerDto(Answer answer) {
        this.id = answer.getId();
        this.contents = answer.getContents();
        this.writer = answer.getWriter();
        this.questionId = answer.getQuestion().getId();
    }

    public long getId() {
        return id;
    }

    public AnswerDto setId(long id) {
        this.id = id;
        return this;
    }

    public String getContents() {
        return contents;
    }

    public AnswerDto setContents(String contents) {
        this.contents = contents;
        return this;
    }

    public User getWriter() {
        return writer;
    }

    public AnswerDto setWriter(User writer) {
        this.writer = writer;
        return this;
    }

    public long getQuestionId() {
        return questionId;
    }

    public AnswerDto setQuestionId(long questionId) {
        this.questionId = questionId;
        return this;
    }
}
