package com.example.quiz_10.vo;

import java.util.List;

import com.example.quiz_10.entity.Answer;

public class AnswerReq {

	private List<Answer> answerList;

	public AnswerReq() {
		super();
	}

	public AnswerReq(List<Answer> answerList) {
		super();
		this.answerList = answerList;
	}

	public List<Answer> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<Answer> answerList) {
		this.answerList = answerList;
	}

}
