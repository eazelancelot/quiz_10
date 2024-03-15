package com.example.quiz_10;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.quiz_10.entity.Answer;
import com.example.quiz_10.entity.DateTest;
import com.example.quiz_10.entity.Quiz;
import com.example.quiz_10.repository.AnswerDao;
import com.example.quiz_10.repository.DateTestDao;
import com.example.quiz_10.repository.QuizDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
class Quiz10ApplicationTests {

	@Autowired
	private DateTestDao dateTestDao;

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private AnswerDao answerDao;

	@Test
	void contextLoads() {
//		dateTestDao.save(new DateTest(1, LocalDate.now(), LocalDate.now().plusDays(1L)));
//		dateTestDao.save(new DateTest(2, LocalDate.now().plusDays(2L), LocalDate.now().plusDays(4L)));
//		dateTestDao.save(new DateTest(3, LocalDate.now().plusDays(3L), LocalDate.now().plusDays(5L)));
		List<DateTest> list = new ArrayList<>();
		list.add(new DateTest(1, LocalDate.now(), LocalDate.now().plusDays(1L)));
		list.add(new DateTest(2, LocalDate.now().plusDays(2L), LocalDate.now().plusDays(4L)));
		list.add(new DateTest(3, LocalDate.now().plusDays(3L), LocalDate.now().plusDays(5L)));
		dateTestDao.saveAll(list);
	}

	@Test
	public void test1() {
		List<DateTest> res1 = dateTestDao.findByStartDateAfter(LocalDate.now());
		List<DateTest> res2 = dateTestDao.findByStartDateGreaterThan(LocalDate.now());
		List<DateTest> res3 = dateTestDao.findByStartDateGreaterThanEqual(LocalDate.now());
		System.out.println("============================");
	}

	@Test
	public void test2() {
		List<DateTest> res1 = dateTestDao.findByStrContaining("");
		System.out.println("============================");
	}

	@Test
	public void test3() {
		List<DateTest> res1 = dateTestDao.findAllByIdIn(Arrays.asList(1, 3, 4, 5));
		System.out.println(res1.size());
		List<DateTest> res2 = dateTestDao.findAllByIdInAndPublishedFalseOrIdInAndStartDateAfter(
				Arrays.asList(2, 3, 4, 5), Arrays.asList(2, 3, 4, 5), LocalDate.now());
		System.out.println(res2.size());
		List<DateTest> res3 = dateTestDao.findAllByIdInOrPublishedFalseAndStartDateAfter(Arrays.asList(4, 5),
				LocalDate.now());
		System.out.println(res3.size());
	}

	@Test
	public void test4() {
		List<Quiz> res = quizDao.findByQuizIdAndQuIdInAndPublishedFalseOrQuizIdAndQuIdInAndStartDateAfter(1,
				Arrays.asList(1, 2), 1, Arrays.asList(1, 2), LocalDate.now());
		System.out.println(res.size());
	}

	@Test
	public void test5() {
		List<Integer> int1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
		int i = 0;
		for (int item : int1) {
			if (item % 2 == 0) {
				int1.remove(item);
			} else {
				i++;
			}
		}
		List<Integer> int2 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
		for (int j = 0; j < int2.size(); j++) {
			if (int2.get(j) % 2 == 0) {
				int2.remove(j);
			}
		}
	}

	@Test
	public void test6() throws JsonProcessingException {
		List<DateTest> list = new ArrayList<>(
				Arrays.asList(new DateTest(1, LocalDate.now(), LocalDate.now().plusDays(1L))));
		
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		
		String str = mapper.writeValueAsString(list);
		System.out.println(str);
		
		List<Object> res = mapper.readValue(str, List.class);
		
		Object obj = res.get(0);
		
		List<DateTest> res1 = mapper.readValue(str, new TypeReference<List<DateTest>>() {
		});
		
		DateTest obj1 = res1.get(0);
		System.out.println("==========================");
	}

	@Test
	public void test7() {
		Answer res = answerDao.save(new Answer("A01", "0912345678", "AA@123", 25, 1, 1, "A"));
		System.out.println(res.getId());
		System.out.println("==========================");
	}

	@Test
	public void test8() {
		List<Integer> list = new ArrayList<>();
		List<Integer> res = quizDao.findQuIdsByQuizIdAndNecessaryTrue(1);
		System.out.println(res.size());
	}
	
	@Test
	public void mapTest1() {
		// 選項(答案): 次數
		List<String> list = new ArrayList<>(Arrays.asList("A", "A", "B", "C", "A", "D"));
		Map<String, Integer> answerMap = new HashMap<>();
		for(String item : list) {
			if(answerMap.containsKey(item)) {
				int count = answerMap.get(item) + 1;
				answerMap.put(item, count);
			} else {
				answerMap.put(item, 1);
			}			
		}
		System.out.println(answerMap.toString());
	}
	
	@Test
	public void mapTest2() {
//		List<String> list = new ArrayList<>(Arrays.asList("A", "A", "B", "C", "A", "D"));
//		Map<String, Integer> map = new HashMap<>();
//		map.put("A", 1);
//		map.put("A", 5);
//		map.put("B", 3);
//		System.out.println(map.toString());
//		System.out.println("===================");
//		Map<String, Integer> map2 = list.stream().collect(Collectors.toMap(
//				ite, 1));
//		System.out.println(map2.toString());
	}
	
	@Test
	public void test9() {
		List<String> list = new ArrayList<>(Arrays.asList("A", "A", "D"));
		List<String> newList = new ArrayList<>();
		int a = Collections.frequency(list, "A");
		System.out.println(a);
	}

}
