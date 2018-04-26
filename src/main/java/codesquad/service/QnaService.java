package codesquad.service;

import codesquad.UnAuthorizedException;
import codesquad.domain.Answer;
import codesquad.domain.AnswerRepository;
import codesquad.domain.Question;
import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service("qnaService")
public class QnaService {
    private static final Logger log = LoggerFactory.getLogger(QnaService.class);

    @Resource(name = "questionRepository")
    private QuestionRepository questionRepository;

    @Resource(name = "answerRepository")
    private AnswerRepository answerRepository;

    @Resource(name = "deleteHistoryService")
    private DeleteHistoryService deleteHistoryService;

    public Question createQuestion(User loginUser, Question question) {
        question.writeBy(loginUser);
        log.debug("question : {}", question);
        return questionRepository.save(question);
    }

    public Question findQuestionById(long id, boolean deleted) {
        return questionRepository.findByIdAndDeleted(id, deleted).orElseThrow(EntityNotFoundException::new);
    }

    public Question findQuestionById(long id) {
        return findQuestionById(id, Boolean.FALSE);
    }

    public Question findQuestionByIdForUpdate(User loginUser, long id) {
        Question question = findQuestionById(id);
        if (!question.isOwner(loginUser)) {
            throw new UnAuthorizedException();
        }
        return question;
    }

    @Transactional
    public Question updateQuestion(User loginUser, long id, Question updatedQuestion) {
        Question question = findQuestionById(id);
        question.update(loginUser, updatedQuestion);
        return question;
    }

    @Transactional
    public void deleteQuestion(User loginUser, long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(EntityNotFoundException::new);
        question.delete(loginUser);
    }

    public Iterable<Question> findAll() {
        return questionRepository.findAll();
    }

    public List<Question> findQuestionByDeleted(boolean deleted) {
        return questionRepository.findByDeleted(deleted);
    }

    public Answer findAnswerById(long id, boolean deleted) {
        return answerRepository.findByIdAndDeleted(id, deleted).orElseThrow(EntityNotFoundException::new);
    }

    public Answer findAnswerById(long id) {
        return findAnswerById(id, Boolean.FALSE);
    }

    @Transactional
    public Answer addAnswer(User loginUser, long questionId, String contents) {
        Answer answer = new Answer(loginUser, contents);
        Question question = findQuestionById(questionId);
        question.addAnswer(answer);
        return answer;
    }

    @Transactional
    public Answer updateAnswer(User loginUser, long id, String contents) {
        Answer answer = answerRepository.findByIdAndDeleted(id, Boolean.FALSE).orElseThrow(EntityNotFoundException::new);
        answer.update(loginUser, contents);
        return answer;
    }

    @Transactional
    public void deleteAnswer(User loginUser, long id) {
        Answer answer = findAnswerById(id);
        answer.delete(loginUser);
    }
}
