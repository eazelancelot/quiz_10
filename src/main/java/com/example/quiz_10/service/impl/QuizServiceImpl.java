package com.example.quiz_10.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.quiz_10.constants.RtnCode;
import com.example.quiz_10.entity.Answer;
import com.example.quiz_10.entity.Quiz;
import com.example.quiz_10.repository.AnswerDao;
import com.example.quiz_10.repository.QuizDao;
import com.example.quiz_10.service.ifs.QuizService;
import com.example.quiz_10.vo.AnswerReq;
import com.example.quiz_10.vo.BaseRes;
import com.example.quiz_10.vo.CreateOrUpdateReq;
import com.example.quiz_10.vo.SearchRes;
import com.example.quiz_10.vo.StatisticsRes;

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private AnswerDao answerDao;

	@Override
	public BaseRes create(CreateOrUpdateReq req) {
		return checkParams(req, true);
	}

	@Override
	public SearchRes search(String quizName, LocalDate startDate, LocalDate endDate, boolean isBackend) {
		if (!StringUtils.hasText(quizName)) {
			quizName = ""; // containing �a���ѼƭȬ��Ŧr��A��ܷ|��������
		}
		if (startDate == null) {
			startDate = LocalDate.of(1970, 1, 1);// �N�}�l�ɶ��]�w���ܦ����e���ɶ�
		}
		if (endDate == null) {
			endDate = LocalDate.of(2099, 12, 31);// �N�����ɶ��]�w�b�ܴN���᪺�ɶ�
		}
		if (isBackend) { // isBackend == true
			return new SearchRes(RtnCode.SUCCESS.getCode(), RtnCode.SUCCESS.getMessage(),
					quizDao.findByQuizNameContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(quizName,
							startDate, endDate));
		} else {
			return new SearchRes(RtnCode.SUCCESS.getCode(), RtnCode.SUCCESS.getMessage(),
					quizDao.findByQuizNameContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishedTrue(
							quizName, startDate, endDate));
		}

	}

	@Override
	public BaseRes deleteQuiz(List<Integer> quizIds) {
		if (CollectionUtils.isEmpty(quizIds)) { // �P�ɧP�_ quizIds �O�_�� null �H�ΪŶ��X
			return new BaseRes(RtnCode.PARAM_ERROR.getCode(), RtnCode.PARAM_ERROR.getMessage());
		}
		quizDao.deleteAllByQuizIdInAndPublishedFalseOrQuizIdInAndStartDateAfter(quizIds, quizIds, LocalDate.now());
		return new BaseRes(RtnCode.SUCCESS.getCode(), RtnCode.SUCCESS.getMessage());
	}

	@Override
	public BaseRes deleteQuestions(int quizId, List<Integer> quIds) {
		if (quizId <= 0 || CollectionUtils.isEmpty(quIds)) {
			return new BaseRes(RtnCode.PARAM_ERROR.getCode(), RtnCode.PARAM_ERROR.getMessage());
		}
		// �ھ� (quizId and ���o��) or (quizId and �|���}�l) ��ݨ�
		List<Quiz> res = quizDao.findByQuizIdAndPublishedFalseOrQuizIdAndStartDateAfterOrderByQuId(//
				quizId, quizId, LocalDate.now());
		if (res.isEmpty()) {
			return new BaseRes(RtnCode.QUIZ_NOT_FOUND.getCode(), RtnCode.QUIZ_NOT_FOUND.getMessage());
		}
//		int j = 0;
//		for(int item : quIds) { // quIds = 1, 4
//			// 1: j = 0, item = 1, item - 1 - j = 1-1-0 = 0�F
//			// 2: j = 1, item = 4, item - 1 - j = 4 - 1 - 1 = 2
//			res.remove(item - 1 -j); 
//			j++;
//		}
//		for(int i = 0; i < res.size(); i++) {
//			res.get(i).setQuId(i + 1);
//		}
		List<Quiz> retainList = new ArrayList<>();
		for (Quiz item : res) {
			if (!quIds.contains(item.getQuId())) { // �O�d���b�R���M�椤��
				retainList.add(item);
			}
		}
		for (int i = 0; i < retainList.size(); i++) {
			retainList.get(i).setQuId(i + 1);
		}
		// �R����i�ݨ�
		quizDao.deleteByQuizId(quizId);
		// �N�O�d�����D�s�^DB
		if (!retainList.isEmpty()) {
			quizDao.saveAll(retainList);
		}
		return new BaseRes(RtnCode.SUCCESS.getCode(), RtnCode.SUCCESS.getMessage());
	}

	@Override
	public BaseRes update(CreateOrUpdateReq req) {
		return checkParams(req, false);
	}

	private BaseRes checkParams(CreateOrUpdateReq req, boolean isCreate) {
		if (CollectionUtils.isEmpty(req.getQuizList())) {
			return new BaseRes(RtnCode.PARAM_ERROR.getCode(), RtnCode.PARAM_ERROR.getMessage());
		}
		// �ˬd���񶵥�
		for (Quiz item : req.getQuizList()) {
			if (item.getQuizId() <= 0 || item.getQuId() <= 0 || !StringUtils.hasText(item.getQuizName())
					|| item.getStartDate() == null || item.getEndDate() == null
					|| !StringUtils.hasText(item.getQuestion()) || !StringUtils.hasText(item.getType())) {
				return new BaseRes(RtnCode.PARAM_ERROR.getCode(), RtnCode.PARAM_ERROR.getMessage());
			}
		}
		// �`�� req ���Ҧ��� quizId
		// ��h�W�O�@�� req ���Ҧ��� quizId �|�ۦP(�@�i�ݨ��h�Ӱ��D)�A���]�O���i��䤤�@����ƪ� quizId �O����
		// ���O�ҩҦ���ƪ����T�ʡA�N���h�`�� req ���Ҧ��� quizId
//				List<Integer> quizIds = new ArrayList<>(); // List ���\���ƪ��Ȧs�b
//				for(Quiz item : req.getQuizList()){
//					if (!quizIds.contains(item.getQuizId())) {
//						quizIds.add(item.getQuizId());
//					}			
//				}
		// �H�U�� set ���g�k�P�W���� List ���g�k���G�@�Ҥ@��
		Set<Integer> quizIds = new HashSet<>(); // set ���|�s�b�ۦP���ȡA�N�O set ���w�s�b�ۦP���ȡA�N���|�s�W
		Set<Integer> quIds = new HashSet<>(); // �ˬd���D�s���O�_�����ơA���`���ӬO��������
		for (Quiz item : req.getQuizList()) {
			quizIds.add(item.getQuizId());
			quIds.add(item.getQuId());
		}
		if (quizIds.size() != 1) {
			return new BaseRes(RtnCode.QUIZ_ID_DOES_NOT_MATCH.getCode(), RtnCode.QUIZ_ID_DOES_NOT_MATCH.getMessage());
		}
		if (quIds.size() != req.getQuizList().size()) {
			return new BaseRes(RtnCode.DUPLICATED_QUESTION_ID.getCode(), RtnCode.DUPLICATED_QUESTION_ID.getMessage());
		}
		// �ˬd�}�l�ɶ�����j�󵲧��ɶ�
		for (Quiz item : req.getQuizList()) {
			if (item.getStartDate().isAfter(item.getEndDate())) {
				return new BaseRes(RtnCode.TIME_FORMAT_ERROR.getCode(), RtnCode.TIME_FORMAT_ERROR.getMessage());
			}
		}
		if (isCreate) { // isCreate == true�A����쥻 create ������k
			// �ˬd�ݨ��O�_�w�s�b
			if (quizDao.existsByQuizId(req.getQuizList().get(0).getQuizId())) {
				return new BaseRes(RtnCode.QUIZ_EXISTS.getCode(), RtnCode.QUIZ_EXISTS.getMessage());
			}
		} else { // isCreate == false�A����쥻 update ������k
			// �T�{�ǹL�Ӫ� quizId �O�_�u���i�H�R��(�i�H�R��������O: �|���o���άO�|���}�l)
			if (!quizDao.existsByQuizIdAndPublishedFalseOrQuizIdAndStartDateAfter(req.getQuizList().get(0).getQuizId(),
					req.getQuizList().get(0).getQuizId(), LocalDate.now())) {
				return new BaseRes(RtnCode.QUIZ_NOT_FOUND.getCode(), RtnCode.QUIZ_NOT_FOUND.getMessage());
			}
			// �R����i�ݨ�
			quizDao.deleteByQuizId(req.getQuizList().get(0).getQuizId());
		}
		// �ھڬO�_�n�o���A�A�� published ���� set ��ǰe�L�Ӫ� quizList ��
		for (Quiz item : req.getQuizList()) {
			item.setPublished(req.isPublished());
		}
		// �s�^DB
		quizDao.saveAll(req.getQuizList());
		return new BaseRes(RtnCode.SUCCESS.getCode(), RtnCode.SUCCESS.getMessage());
	}

	@Override
	public BaseRes answer(AnswerReq req) {
		if (CollectionUtils.isEmpty(req.getAnswerList())) {
			return new BaseRes(RtnCode.PARAM_ERROR.getCode(), RtnCode.PARAM_ERROR.getMessage());
		}
		for (Answer item : req.getAnswerList()) {
			if (!StringUtils.hasText(item.getName()) || !StringUtils.hasText(item.getPhone())
					|| !StringUtils.hasText(item.getEmail()) || item.getQuizId() <= 0 || item.getQuId() <= 0
					|| item.getAge() < 0) {
				return new BaseRes(RtnCode.PARAM_ERROR.getCode(), RtnCode.PARAM_ERROR.getMessage());
			}
		}
		// �ˬd��ƦC���A�Ҧ��� quizId ���@�ˡA�H�� quId ��������
		Set<Integer> quizIds = new HashSet<>(); // set ���|�s�b�ۦP���ȡA�N�O set ���w�s�b�ۦP���ȡA�N���|�s�W
		Set<Integer> quIds = new HashSet<>(); // �ˬd���D�s���O�_�����ơA���`���ӬO��������
		for (Answer item : req.getAnswerList()) {
			quizIds.add(item.getQuizId());
			quIds.add(item.getQuId());
		}
		if (quizIds.size() != 1) {
			return new BaseRes(RtnCode.QUIZ_EXISTS.getCode(), RtnCode.QUIZ_EXISTS.getMessage());
		}
		if (quIds.size() != req.getAnswerList().size()) {
			return new BaseRes(RtnCode.DUPLICATED_QUESTION_ID.getCode(), RtnCode.DUPLICATED_QUESTION_ID.getMessage());
		}
		// �ˬd������D�O�_���^��
		List<Integer> res = quizDao.findQuIdsByQuizIdAndNecessaryTrue(req.getAnswerList().get(0).getQuizId());
		for (Answer item : req.getAnswerList()) {
			if (res.contains(item.getQuId()) && !StringUtils.hasText(item.getAnswer())) {
				return new BaseRes(RtnCode.QUESTION_NO_ANSWER.getCode(), RtnCode.QUESTION_NO_ANSWER.getMessage());
			}
		}
		// �H�U�o�q�P�W�@�q���浲�G�ۦP
//		for(int item : res) {
//			Answer ans = req.getAnswerList().get(item - 1);
//			if (!StringUtils.hasText(ans.getAnswer())) {
//				return new BaseRes(RtnCode.QUESTION_NO_ANSWER.getCode(), RtnCode.QUESTION_NO_ANSWER.getMessage());
//			}
//		}
		// �T�{�P�@�� email ���୫�ƶ�g�P�@�i�ݨ�
		if (answerDao.existsByQuizIdAndEmail(req.getAnswerList().get(0).getQuizId(),
				req.getAnswerList().get(0).getEmail())) {
			return new BaseRes(RtnCode.DUPLICATED_QUIZ_ANSWER.getCode(), RtnCode.DUPLICATED_QUIZ_ANSWER.getMessage());
		}
		answerDao.saveAll(req.getAnswerList());
		return new BaseRes(RtnCode.SUCCESS.getCode(), RtnCode.SUCCESS.getMessage());
	}

	@Override
	public StatisticsRes statistics(int quizId) {
		if (quizId <= 0) {
			return new StatisticsRes(RtnCode.PARAM_ERROR.getCode(), RtnCode.PARAM_ERROR.getMessage());
		}
		// �����ݨ����o���D�� type �O�D²���D
		List<Quiz> quizs = quizDao.findByQuizId(quizId);
		// qus �O�D²���D���D�ؽs�������X
		List<Integer> qus = new ArrayList<>();
		// �Y�O²���D�Aoptions �O�Ū�
		for (Quiz item : quizs) {
			if (StringUtils.hasText(item.getOptions())) {
				qus.add(item.getQuId());
			}
		}
		List<Answer> answers = answerDao.findByQuizIdOrderByQuId(quizId);
		// quIdAnswerMap: ���D�s���P���ת� mapping
		Map<Integer, String> quIdAnswerMap = new HashMap<>();
		// ��D²���D���C�D���צU�ۦꦨ�r��A�Y�@�ӿﶵ(����)�|���@�Ӧr��
		for (Answer item : answers) {
			// �Y�O�]�t�b qus �� List �����A�N��ܬO����D(��B�h��)
			if (qus.contains(item.getQuId())) {
				// �Y key �w�s�b
				if (quIdAnswerMap.containsKey(item.getQuId())) {
					// 1. �z�L key ���o������ value
					String str = quIdAnswerMap.get(item.getQuId());
					// 2. ��즳���ȩM�o�����o���Ȧ걵�ܦ��s����
					str += item.getAnswer();
					// 3. �N�s���ȩ�^��쥻�� key ���U
					quIdAnswerMap.put(item.getQuId(), str);
				} else { // key ���s�b�A�����s�W key �M value
					quIdAnswerMap.put(item.getQuId(), item.getAnswer());
				}
			}
		}
		// �p��C�D�C�ӿﶵ������
		// Map ���� Map<String, Integer>,�����O�W���� answerCountMap
		Map<Integer, Map<String, Integer>> quizIdAndAnsCountMap = new HashMap<>();
		// �ϥ� foreach �M�� map �����C�Ӷ���
		// �M������H�n�q map �ন entrySet�A�n�B�O�i�H�������o map ���� key �M value
		for (Entry<Integer, String> item : quIdAnswerMap.entrySet()) {
			// answerCountMap: �ﶵ(����)�P���ƪ� mapping
			Map<String, Integer> answerCountMap = new HashMap<>();
			// ���o�C�Ӱ��D���ﶵ
			String[] optionList = quizs.get(item.getKey() - 1).getOptions().split(";");
			// ����D���ﶵ�P���ư� mapping
			for (String option : optionList) {
				String newStr = item.getValue();
				int length1 = newStr.length();
				newStr = newStr.replace(option, "");
				int length2 = newStr.length();
				// �n�� option ����]�O option �O�ﶵ�����e�A�Ӥ��O�ﶵ���s��
				int count = (length1 - length2) / option.length();
				answerCountMap.put(option, count);
			}
			quizIdAndAnsCountMap.put(item.getKey(), answerCountMap);
		}
		return new StatisticsRes(RtnCode.SUCCESS.getCode(), RtnCode.SUCCESS.getMessage(), quizIdAndAnsCountMap);
	}

}
