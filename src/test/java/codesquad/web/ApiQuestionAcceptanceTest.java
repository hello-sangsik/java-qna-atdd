package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import support.test.AcceptanceTest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {

    private Question QUESTION;
    private ResponseEntity<String> RESPONSE;
    private String LOCATION;

    @Before
    public void setup() {
        createDefaultQuestion();
    }

    private void createDefaultQuestion() {
        QUESTION = new Question("test title", "test contents", defaultUser());
        RESPONSE = basicAuthTemplate().postForEntity("/api/questions", QUESTION, String.class);
        LOCATION = RESPONSE.getHeaders().getLocation().getPath();
    }

    @Test
    public void create_and_show() {
        assertThat(RESPONSE.getStatusCode(), is(HttpStatus.CREATED));

        QuestionDto dbQuestion = template().getForObject(LOCATION, QuestionDto.class);
        assertThat(dbQuestion.getTitle(), is(QUESTION.getTitle()));
        assertThat(dbQuestion.getContents(), is(QUESTION.getContents()));
    }

    @Test
    public void update() {
        QuestionDto editedQuestion = new QuestionDto("edited title", "edited contents");
        basicAuthTemplate().put(LOCATION, editedQuestion);

        QuestionDto dbQuestion = template().getForObject(LOCATION, QuestionDto.class);
        assertThat(dbQuestion.getTitle(), is(editedQuestion.getTitle()));
        assertThat(dbQuestion.getContents(), is(editedQuestion.getContents()));
    }

    @Test
    public void update_not_owner() {
        User anotherUser = new User(3, "sangsik", "password123", "김상식", "sangsik@test.com");
        QuestionDto editedQuestion = new QuestionDto("edited title", "edited contents");
        basicAuthTemplate(anotherUser).put(LOCATION, editedQuestion);

        QuestionDto dbQuestion = template().getForObject(LOCATION, QuestionDto.class);
        assertThat(dbQuestion.getTitle(), is(QUESTION.getTitle()));
        assertThat(dbQuestion.getContents(), is(QUESTION.getContents()));
    }

    @Test
    public void delete() {
        basicAuthTemplate().delete(LOCATION);

        QuestionDto dbQuestion = template().getForObject(LOCATION, QuestionDto.class);
        assertThat(ObjectUtils.isEmpty(dbQuestion), is(Boolean.TRUE));
    }

    @Test
    public void delete_not_owner() {
        User anotherUser = new User(3, "sangsik", "password123", "김상식", "sangsik@test.com");
        basicAuthTemplate(anotherUser).delete(LOCATION);

        QuestionDto dbQuestion = template().getForObject(LOCATION, QuestionDto.class);
        assertThat(dbQuestion.getTitle(), is(QUESTION.getTitle()));
        assertThat(dbQuestion.getContents(), is(QUESTION.getContents()));
    }
}
