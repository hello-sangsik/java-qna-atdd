package codesquad.web;

import codesquad.domain.Answer;
import codesquad.domain.User;
import codesquad.dto.AnswerDto;
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
@RequestMapping("/api/answer")
public class ApiAnswerController {
    @Resource(name = "qnaService")
    private QnaService qnaService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @LoginUser User loginUser, @RequestBody AnswerDto answerDto) {
        Answer answer = qnaService.addAnswer(loginUser, answerDto.getQuestionId(), answerDto.getContents());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/answer/" + answer.getId()));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public AnswerDto show(@PathVariable long id) {
        return qnaService.findAnswerById(id).toAnswerDto();
    }


    @PutMapping("/{id}")
    public void update(@LoginUser User loginUser, @PathVariable long id, @RequestBody String contents) {
        qnaService.updateAnswer(loginUser, id, contents);
    }

    @DeleteMapping("{id}")
    public void delete(@LoginUser User loginUser, @PathVariable long id) {
        qnaService.deleteAnswer(loginUser, id);
    }
}
