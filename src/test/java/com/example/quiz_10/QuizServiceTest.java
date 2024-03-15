package com.example.quiz_10;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.quiz_10.entity.Quiz;
import com.example.quiz_10.entity.QuizId;
import com.example.quiz_10.repository.QuizDao;
import com.example.quiz_10.service.ifs.QuizService;
import com.example.quiz_10.vo.BaseRes;
import com.example.quiz_10.vo.CreateOrUpdateReq;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class QuizServiceTest {

	@Autowired
	private QuizService quizService;
	
	@Autowired
	private QuizDao quizDao; 

	@BeforeEach
	private void addData() {
//		CreateOrUpdateReq req = new CreateOrUpdateReq();
//		req.setQuizList(new ArrayList<>(Arrays.asList(new Quiz(2, 1, "test", "test", LocalDate.now().plusDays(2),
//				LocalDate.now().plusDays(9), "q_test", "single", true, "A;B;C;D", false))));
//		quizService.create(req);
//		System.out.println("before each test");
	}

	@BeforeAll
	private static void testAdd() {
		System.out.println("before all test");
	}

	@AfterEach
	private void aftereach() {
		quizDao.deleteById(new QuizId(2, 1));
	}

	@AfterAll
	private static void afterAll() {
		System.out.println("after all test");
	}

	@Test
	public void createTest() {
		CreateOrUpdateReq req = new CreateOrUpdateReq();
		BaseRes res = quizService.create(req);
		Assert.isTrue(res.getCode() == 400, "create test fail!!");
		// ================ 測試 quizId
		quizIdTest(req, res);
		// ================ 測試 quId
		quIdTest(req, res);
		// ================= 測試 quizName
		req.setQuizList(new ArrayList<>(Arrays.asList(new Quiz(1, 1, "", "test", LocalDate.now().plusDays(2),
				LocalDate.now().plusDays(9), "q_test", "single", true, "A;B;C;D", false))));
		res = quizService.create(req);
		Assert.isTrue(res.getCode() == 400, "create test quizName fail!!");
		// ================= 測試 startDate
		req.setQuizList(new ArrayList<>(Arrays.asList(new Quiz(1, 1, "test", "test", null, LocalDate.now().plusDays(9),
				"q_test", "single", true, "A;B;C;D", false))));
		res = quizService.create(req);
		Assert.isTrue(res.getCode() == 400, "create test startDate fail!!");
		// ================= 測試 endDate
		req.setQuizList(new ArrayList<>(Arrays.asList(new Quiz(1, 1, "test", "test", LocalDate.now().plusDays(2), null,
				"q_test", "single", true, "A;B;C;D", false))));
		res = quizService.create(req);
		Assert.isTrue(res.getCode() == 400, "create test endDate fail!!");
		// ================= 測試 question 問題名稱)
		req.setQuizList(new ArrayList<>(Arrays.asList(new Quiz(1, 1, "test", "test", LocalDate.now().plusDays(2),
				LocalDate.now().plusDays(9), "", "single", true, "A;B;C;D", false))));
		res = quizService.create(req);
		Assert.isTrue(res.getCode() == 400, "create test question fail!!");
		// ================= 測試 type
		req.setQuizList(new ArrayList<>(Arrays.asList(new Quiz(1, 1, "test", "test", LocalDate.now().plusDays(2),
				LocalDate.now().plusDays(9), "q_name", "", true, "A;B;C;D", false))));
		res = quizService.create(req);
		Assert.isTrue(res.getCode() == 400, "create test type fail!!");
		// ================= 測試 startDate > endDate
		req.setQuizList(new ArrayList<>(Arrays.asList(new Quiz(1, 1, "test", "test", LocalDate.now().plusDays(9),
				LocalDate.now().plusDays(2), "q_name", "single", true, "A;B;C;D", false))));
		res = quizService.create(req);
		Assert.isTrue(res.getCode() == 400, "create test date range fail!!");
		// ================ 測試成功
		req.setQuizList(new ArrayList<>(Arrays.asList(new Quiz(1, 1, "test", "test", LocalDate.now().plusDays(2),
				LocalDate.now().plusDays(9), "q_test", "single", true, "A;B;C;D", false))));
		res = quizService.create(req);
		Assert.isTrue(res.getCode() == 200, "create test success fail!!");
		// ================ 測試已存在資料
		req.setQuizList(new ArrayList<>(Arrays.asList(new Quiz(1, 1, "test", "test", LocalDate.now().plusDays(2),
				LocalDate.now().plusDays(9), "q_test", "single", true, "A;B;C;D", false))));
		res = quizService.create(req);
		Assert.isTrue(res.getCode() == 400, "create test fail!!");
		// ================ 刪除測試資料
	}
	
	private Quiz newQuiz() {
		return new Quiz(2, 2, "test", "test", LocalDate.now().plusDays(2),
				LocalDate.now().plusDays(9), "q_test", "single", true, "A;B;C;D", false);
	}

	private void quizIdTest(CreateOrUpdateReq req, BaseRes res) {
		// =============== 測試 quizId
		Quiz quiz = newQuiz();
		quiz.setQuizId(0);
		req.setQuizList(new ArrayList<>(Arrays.asList(quiz)));
		res = quizService.create(req);
		Assert.isTrue(res.getCode() == 400, "create test quizId fail!!");
	}
	
	private void quIdTest(CreateOrUpdateReq req, BaseRes res) {
		Quiz quiz = newQuiz();
		quiz.setQuId(-1);
		req.setQuizList(new ArrayList<>(Arrays.asList(quiz)));
		res = quizService.create(req);
		Assert.isTrue(res.getCode() == 400, "create test quId fail!!");
	}

	@Test
	public void updateTest() {
		System.out.println("=================");
		System.out.println("=================");
	}
//	
//	@Test
//	public void updateTest2() {
//		System.out.println("=================");
//		System.out.println("=================");
//	}

}
