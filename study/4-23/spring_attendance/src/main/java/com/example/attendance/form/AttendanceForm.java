package com.example.attendance.form;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.*;
import org.springframework.format.annotation.DateTimeFormat; // ← ★ これを追加

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceForm {

	private Long id;

	@NotBlank(message = "社員名は必須です")
	@Pattern(regexp = "^[\\p{L}\\p{IsHiragana}\\p{IsKatakana}]+　[\\p{L}\\p{IsHiragana}\\p{IsKatakana}]+$", message = "社員名は苗字と名前の間に全角スペースを入れてください")
	private String employeeName;

	@NotNull(message = "勤務日は必須です")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate workDate;

	@DateTimeFormat(pattern = "HH:mm") 
	private LocalTime checkInTime;

	@DateTimeFormat(pattern = "HH:mm") 
	private LocalTime checkOutTime;

	/** 勤務日は未来日不可 */
	@AssertTrue(message = "勤務日は未来日を指定できません")
	public boolean isNotFutureDate() {
		if (workDate == null)
			return true;
		return !workDate.isAfter(LocalDate.now());
	}

	/** 出勤 < 退勤チェック */
	@AssertTrue(message = "退勤時刻は出勤時刻より後にしてください")
	public boolean isCheckOutAfterCheckIn() {
		if (checkInTime == null || checkOutTime == null)
			return true;
		return !checkOutTime.isBefore(checkInTime);
	}

	/** 未来時間禁止 */
	@AssertTrue(message = "未来の時刻は入力できません")
	public boolean isNotFutureTime() {
		if (workDate == null)
			return true;
		LocalDateTime now = LocalDateTime.now();
		if (checkInTime != null && LocalDateTime.of(workDate, checkInTime).isAfter(now))
			return false;
		if (checkOutTime != null && LocalDateTime.of(workDate, checkOutTime).isAfter(now))
			return false;
		return true;
	}

	// Entity → Form 変換
	public static AttendanceForm fromEntity(com.example.attendance.entity.Attendance attendance) {
		AttendanceForm form = new AttendanceForm();
		form.setId(attendance.getId());
		form.setEmployeeName(attendance.getEmployeeName());
		form.setWorkDate(attendance.getWorkDate());
		form.setCheckInTime(attendance.getCheckInTime() != null ? attendance.getCheckInTime().toLocalTime() : null);
		form.setCheckOutTime(attendance.getCheckOutTime() != null ? attendance.getCheckOutTime().toLocalTime() : null);
		return form;
	}
}
