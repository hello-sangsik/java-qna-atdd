package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.domain.UserRepository;
import codesquad.dto.AnswerDto;
import codesquad.dto.QuestionDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import support.test.AcceptanceTest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ApiAnswerAcceptanceTest extends AcceptanceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void create_and_show() {
        QuestionDto question = createDefaultQuestion();
        AnswerDto answerDto = new AnswerDto(question.getId(), "test answer");
        ResponseEntity<String> response = basicAuthTemplate().postForEntity(String.format("/api/answer", question.getId()), answerDto, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
    }

    @Test
    public void show() {
        QuestionDto question = createDefaultQuestion();
        AnswerDto answerDto = new AnswerDto(question.getId(), "test answer");
        ResponseEntity<String> response = basicAuthTemplate().postForEntity(String.format("/api/answer", question.getId()), answerDto, String.class);
        String location = response.getHeaders().getLocation().getPath();

        AnswerDto dbAnswerDto = template().getForObject(location, AnswerDto.class);
        assertThat(answerDto.getContents(), is(dbAnswerDto.getContents()));
    }

    @Test
    public void update() {
        QuestionDto question = createDefaultQuestion();
        AnswerDto answerDto = new AnswerDto(question.getId(), "test answer");
        ResponseEntity<String> response = basicAuthTemplate().postForEntity(String.format("/api/answer", question.getId()), answerDto, String.class);
        String location = response.getHeaders().getLocation().getPath();

        String updatedContents = "edited answer";
        basicAuthTemplate().put(location, updatedContents);

        AnswerDto dbAnswerDto = template().getForObject(location, AnswerDto.class);
        assertThat(dbAnswerDto.getContents(), is(updatedContents));
    }

    @Test
    public void update_not_owner() {
        QuestionDto question = createDefaultQuestion();
        AnswerDto answerDto = new AnswerDto(question.getId(), "test answer");
        ResponseEntity<String> response = basicAuthTemplate().postForEntity(String.format("/api/answer", question.getId()), answerDto, String.class);
        String location = response.getHeaders().getLocation().getPath();

        String updatedContents = "edited answer";
        User user = userRepository.findByUserId("sanjigi").get();
        basicAuthTemplate(user).put(location, updatedContents);

        AnswerDto dbAnswerDto = template().getForObject(location, AnswerDto.class);
        assertThat(dbAnswerDto.getContents(), is(answerDto.getContents()));
    }

    @Test
    public void delete() {
        String location = createAnswerAndGetLocation();

        basicAuthTemplate().delete(location);

        AnswerDto dbAnswerDto = template().getForObject(location, AnswerDto.class);
        assertThat(ObjectUtils.isEmpty(dbAnswerDto), is(Boolean.TRUE));
    }

    @Test
    public void delete_not_owner() {
        String location = createAnswerAndGetLocation();

        User user = userRepository.findByUserId("sanjigi").get();
        basicAuthTemplate(user).delete(location);

        AnswerDto dbAnswerDto = template().getForObject(location, AnswerDto.class);
        assertThat(ObjectUtils.isEmpty(dbAnswerDto), is(Boolean.FALSE));
    }

    private String createAnswerAndGetLocation() {
        QuestionDto question = createDefaultQuestion();
        AnswerDto answerDto = new AnswerDto(question.getId(), "test answer");
        ResponseEntity<String> response = basicAuthTemplate().postForEntity(String.format("/api/answer", question.getId()), answerDto, String.class);
        return response.getHeaders().getLocation().getPath();
    }

    private QuestionDto createDefaultQuestion() {
        Question question = new Question("test title", "test contents", defaultUser());
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", question, String.class);
        String location = response.getHeaders().getLocation().getPath();
        return template().getForObject(location, QuestionDto.class);
    }
}
