package com.example.quiz_10.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_10.service.ifs.QuizService;
import com.example.quiz_10.vo.AnswerReq;
import com.example.quiz_10.vo.BaseRes;
import com.example.quiz_10.vo.CreateOrUpdateReq;
import com.example.quiz_10.vo.DeleteQuizReq;
import com.example.quiz_10.vo.SearchReq;
import com.example.quiz_10.vo.SearchRes;
import com.example.quiz_10.vo.StatisticsRes;

@RestController
@CrossOrigin
public class QuizServiceController {
	
	@Autowired
	private QuizService quizService;
	
	@PostMapping(value = "quiz/create")
	public BaseRes create(@RequestBody CreateOrUpdateReq req) {
		return quizService.create(req);
	}
	
	@GetMapping(value = "quiz/search")
	public SearchRes search(@RequestBody SearchReq req) {
		return quizService.search(req.getQuizName(), req.getStartDate(), req.getEndDate(), req.isBackend());
	}
	
	@PostMapping(value = "quiz/delete_quiz")
	public BaseRes deleteQuiz(@RequestBody DeleteQuizReq req) {
		return quizService.deleteQuiz(req.getQuizIds());
	}
	
	@PostMapping(value = "quiz/update")
	public BaseRes update(@RequestBody CreateOrUpdateReq req) {
		return quizService.update(req);
	}
	
	@PostMapping(value = "quiz/answer")
	public BaseRes answer(@RequestBody AnswerReq req) {
		return quizService.answer(req);
	}
	
	@GetMapping(value = "quiz/statistics")
	public StatisticsRes statistics(@RequestParam(value = "quiz_id") int quizId) {
		return quizService.statistics(quizId);
	}
	
	@PostMapping(value = "quiz/test")
	public BaseRes objMapper(@RequestParam(value = "str") String str) throws Exception {
		return quizService.objMapper(str);
	}

}
