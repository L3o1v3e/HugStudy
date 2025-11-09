package com.example.attendance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.attendance.entity.Attendance;
import com.example.attendance.entity.User;
import com.example.attendance.form.AttendanceForm;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.UserRepository;

@Service
public class AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private UserRepository userRepository;

	/** 勤怠一覧取得 */
	public List<Attendance> findAll() {
		return attendanceRepository.findAll();
	}

	/** 社員取得（存在しなければ自動登録） */
	@Transactional
	public User getOrCreateUser(String name) {
		return userRepository.findByName(name)
				.orElseGet(() -> {
					User newUser = new User();
					newUser.setName(name);
					return userRepository.save(newUser);
				});
	}

	/** 出勤打刻処理 */
	@Transactional
	public void punchIn(AttendanceForm form) {
		User user = getOrCreateUser(form.getEmployeeName());
		LocalDate workDate = form.getWorkDate();

		Attendance attendance = attendanceRepository
				.findByEmployeeAndWorkDate(user, workDate)
				.orElseGet(() -> {
					Attendance a = new Attendance();
					a.setEmployee(user); // ✅ employee_idをセット
					a.setEmployeeName(user.getName());
					a.setWorkDate(workDate);
					return a;
				});

		attendance.setCheckInTime(LocalDateTime.now());
		attendanceRepository.saveAndFlush(attendance);
	}

	/** 退勤打刻処理（休憩1時間差し引き対応） */
	@Transactional
	public void punchOut(AttendanceForm form) {
		User user = getOrCreateUser(form.getEmployeeName());
		LocalDate workDate = form.getWorkDate();

		Attendance attendance = attendanceRepository
				.findByEmployeeAndWorkDate(user, workDate)
				.orElseThrow(() -> new IllegalArgumentException("出勤記録が存在しません。先に出勤を打刻してください。"));

		attendance.setCheckOutTime(LocalDateTime.now());
		attendance.calculateWorkDurationWithConditionalBreak(1);
		attendanceRepository.saveAndFlush(attendance);

		// 勤務時間を再計算（休憩1時間を差し引く）
		attendance.calculateWorkDurationWithConditionalBreak(1);

		attendanceRepository.saveAndFlush(attendance);
	}

	/** 勤怠データ取得（編集用） */
	public Attendance findById(Long id) {
		return attendanceRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("指定された勤怠データが存在しません。"));
	}

	/** 勤怠更新処理（編集時も再計算・休憩1時間差し引き） */
	@Transactional
	public void updateAttendance(AttendanceForm form) {
		Attendance attendance = attendanceRepository.findById(form.getId())
				.orElseThrow(() -> new IllegalArgumentException("指定された勤怠データが存在しません。"));

		// 編集画面からworkDateが来ていない場合、既存の値を使う
		LocalDate workDate = form.getWorkDate() != null
				? form.getWorkDate()
				: attendance.getWorkDate();

		if (form.getCheckInTime() != null) {
			attendance.setCheckInTime(LocalDateTime.of(workDate, form.getCheckInTime()));
		}
		if (form.getCheckOutTime() != null) {
			attendance.setCheckOutTime(LocalDateTime.of(workDate, form.getCheckOutTime()));
		}

		// 勤務時間を再計算（休憩1時間を差し引く）
		attendance.calculateWorkDurationWithConditionalBreak(1);

		attendanceRepository.save(attendance);
	}

	/** 勤怠削除処理 */
	@Transactional
	public void deleteById(Long id) {
		attendanceRepository.deleteById(id);
	}
}
