package com.example.quiz_10.constants;

public enum RtnCode {

	SUCCESS(200, "Success!!"), //
	PARAM_ERROR(400, "Param error!!"), //
	QUIZ_EXISTS(400, "Quiz exists!!"), //
	QUIZ_NOT_FOUND(400, "Quiz not found!!"), //
	DUPLICATED_QUESTION_ID(400, "Duplicated question id!!"), //
	TIME_FORMAT_ERROR(400, "Time format error!!"), //
	QUIZ_ID_ERROR(400, "Quiz id error!!"), //
	QUESTION_NO_ANSWER(400, "Question no answer!!"), //
	DUPLICATED_QUIZ_ANSWER(400, "Duplicated quiz answer!!"), //
	QUIZ_ID_DOES_NOT_MATCH(400, "Quiz id does not match!!"), //
	DELETE_QUIZ_ERROR(400, "Delete quiz error!!"), //
	SAVE_QUIZ_ERROR(400, "Save quiz error!!");

	private int code;

	private String message;

	private RtnCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public static final int NOT_FOUND_CODE = 404;

	// 1. final: ���~���N�X�T�w���|�ܡA�O�`��
	//        Java ���`�ƪ��ܼƩR�W�����j�g�A�Ω��u�걵2�Ӥ��P����r��
	// 2. static: �����ϥ� enum ���W�٩I�s���ܼ�(enum �L�k new)
	public static final int ERROR_CODE = 400;

}
