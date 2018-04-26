package codesquad.domain;

import codesquad.UnAuthorizedException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author sangsik.kim
 */
public class AnswerTest {
    private User DEFAULT_USER;
    private User ANOTHER_USER;
    private Question DEFAULT_QUESTION;
    private Answer DEFAULT_ANSWER;

    @Before
    public void setup() {
        DEFAULT_USER = new User(0, "sangsik", "test", "김상식", "sangsik@test.com");
        ANOTHER_USER = new User(1, "sion", "test", "송시온", "sion@test.com");
        DEFAULT_QUESTION = new Question(0, "테스트 제목", "테스트 내용", DEFAULT_USER);
        DEFAULT_ANSWER = new Answer(0, DEFAULT_USER, "테스트 답글");
    }


    @Test
    public void update_owner() throws Exception {
        DEFAULT_QUESTION.addAnswer(DEFAULT_ANSWER);
        DEFAULT_ANSWER.update(DEFAULT_USER, "답글 변경");

        assertThat(DEFAULT_ANSWER.getContents(), is("답글 변경"));
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_not_owner() {
        DEFAULT_QUESTION.addAnswer(DEFAULT_ANSWER);
        DEFAULT_ANSWER.update(ANOTHER_USER, "답글 변경");

        assertThat(DEFAULT_ANSWER.getContents(), is("답글 변경"));
    }

    @Test
    public void delete_success() {
        DEFAULT_ANSWER.delete(DEFAULT_USER);
        assertThat(DEFAULT_ANSWER.isDeleted(), is(Boolean.TRUE));
    }

    @Test(expected = UnAuthorizedException.class)
    public void delete_not_owner() {
        DEFAULT_ANSWER.delete(ANOTHER_USER);
    }
}
