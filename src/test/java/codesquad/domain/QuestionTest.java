package codesquad.domain;

import codesquad.UnAuthorizedException;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author sangsik.kim
 */
public class QuestionTest {
    private User DEFAULT_USER;
    private User ANOTHER_USER;
    private Question DEFAULT_QUESTION;

    @Before
    public void setup() {
        DEFAULT_USER = new User(0, "sangsik", "test", "김상식", "sangsik@test.com");
        ANOTHER_USER = new User(1, "sion", "test", "송시온", "sion@test.com");
        DEFAULT_QUESTION = new Question(0, "테스트 제목", "테스트 내용", DEFAULT_USER);
    }

    @Test
    public void update_owner() throws Exception {
        Question editedQuestion = new Question("제목 수정", "내용 수정");
        DEFAULT_QUESTION.update(DEFAULT_USER, editedQuestion);

        assertThat(DEFAULT_QUESTION.getTitle(), is(editedQuestion.getTitle()));
        assertThat(DEFAULT_QUESTION.getContents(), is(editedQuestion.getContents()));
    }

    @Test(expected = UnAuthorizedException.class)
    public void update_not_owner() {
        Question editedQuestion = new Question("제목 수정", "내용 수정");
        DEFAULT_QUESTION.update(ANOTHER_USER, editedQuestion);
    }

    @Test
    public void delete_success() {
        DEFAULT_QUESTION.delete(DEFAULT_USER);
        assertThat(DEFAULT_QUESTION.isDeleted(), is(Boolean.TRUE));
    }

    @Test(expected = UnAuthorizedException.class)
    public void delete_not_owner() {
        DEFAULT_QUESTION.delete(ANOTHER_USER);
    }
}
