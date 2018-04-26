package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/questions")
public class ApiQuestionController {
    @Resource(name = "qnaService")
    private QnaService qnaService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @LoginUser User loginUser, @RequestBody QuestionDto questionDto) {
        Question question = qnaService.createQuestion(loginUser, questionDto.toQuestion());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/questions/" + question.getId()));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable long id) {
        return new ResponseEntity<>(qnaService.findQuestionById(id).toQuestionDto(), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> update(@LoginUser User loginUser, @PathVariable long id, @Valid @RequestBody QuestionDto questionDto) {
        qnaService.updateQuestion(loginUser, id, questionDto.toQuestion());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@LoginUser User loginUser, @PathVariable long id) {
        qnaService.deleteQuestion(loginUser, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
