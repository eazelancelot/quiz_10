package com.example.quiz_10.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.quiz_10.entity.DateTest;

@Repository
@Transactional
public interface DateTestDao extends JpaRepository<DateTest, Integer> {
	
	public List<DateTest> findByStartDateAfter(LocalDate startDate);
	
	public List<DateTest> findByStartDateGreaterThan(LocalDate startDate);
	
	public List<DateTest> findByStartDateGreaterThanEqual(LocalDate startDate);
	
	public List<DateTest> findByStrContaining(String str);
	
	public List<DateTest> findAllByIdIn(List<Integer> quizIds);
	
	public List<DateTest> findAllByIdInAndPublishedFalseOrIdInAndStartDateAfter(List<Integer> quizIds,
			List<Integer> quizIds2, LocalDate now);
	
	public List<DateTest> findAllByIdInOrPublishedFalseAndStartDateAfter(List<Integer> quizIds,
			LocalDate now);
	
	public List<DateTest> findAllByPublishedFalseAndStartDateAfter(LocalDate now);
	
	public void deleteAllByIdInAndPublishedFalseOrStartDateAfter(List<Integer> quizIds,
			LocalDate now);

}
