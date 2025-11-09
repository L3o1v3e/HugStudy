package com.example.attendance.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {

	/** 主キー */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 社員情報（外部キー） */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id", nullable = false)
	private User employee;

	/** 社員名（表示用キャッシュ） */
	@Column(nullable = false)
	private String employeeName;

	/** 勤務日 */
	@Column(nullable = false)
	private LocalDate workDate;

	/** 出勤時刻 */
	private LocalDateTime checkInTime;

	/** 退勤時刻 */
	private LocalDateTime checkOutTime;

	/** 勤務時間 */
	private String workDuration;

	/**
	 *  勤務時間を計算してセットする。
	 *  出勤・退勤の両方がある場合に実行
	 *  8時間以上の勤務なら自動で休憩1時間を差し引く
	 *  「退勤<出勤」の不正時刻は検出
	 *
	 * @param breakHours 差し引く休憩時間（時間単位）
	 */
	public void calculateWorkDurationWithConditionalBreak(long breakHours) {
		// 出勤・退勤の両方がセットされていない場合
		if (checkInTime == null || checkOutTime == null) {
			this.workDuration = "-";
			return;
		}

		// 不正な退勤時刻
		if (checkOutTime.isBefore(checkInTime)) {
			this.workDuration = "不正（退勤 < 出勤）";
			return;
		}

		// 勤務時間を算出
		Duration duration = Duration.between(checkInTime, checkOutTime);
		boolean breakApplied = false;

		//  8時間以上の勤務で休憩を自動差引
		if (duration.toHours() >= 8) {
			duration = duration.minusHours(breakHours);
			breakApplied = true;
		}

		// 勤務時間をフォーマット
		long hours = duration.toHours();
		long minutes = duration.toMinutesPart();

		// 表示用文字列をセット
		if (breakApplied) {
			this.workDuration = String.format("%d時間%d分（※休憩%d時間差引済）", hours, minutes, breakHours);
		} else {
			this.workDuration = String.format("%d時間%d分", hours, minutes);
		}
	}

	/**
	 * 出勤時刻をセットした際に勤務時間を自動再計算
	 */
	public void setCheckInTime(LocalDateTime checkInTime) {
		this.checkInTime = checkInTime;
		calculateWorkDurationWithConditionalBreak(1); // 休憩1時間差引
	}

	/**
	 * 退勤時刻をセットした際に勤務時間を自動再計算
	 */
	public void setCheckOutTime(LocalDateTime checkOutTime) {
		this.checkOutTime = checkOutTime;
		calculateWorkDurationWithConditionalBreak(1); // 休憩1時間差引
	}

	/**
	 * 出勤時刻の表示用テキスト（HH:mm形式）
	 */
	public String getCheckInTimeText() {
		return checkInTime != null
				? checkInTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
				: "-";
	}

	/**
	 * 退勤時刻の表示用テキスト（HH:mm形式）
	 */
	public String getCheckOutTimeText() {
		return checkOutTime != null
				? checkOutTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
				: "-";
	}

	/**
	 * 勤務日の表示用テキスト（yyyy/MM/dd形式）
	 */
	public String getWorkDateText() {
		return workDate != null
				? workDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
				: "-";
	}

	/**
	 * 勤務時間の表示用ゲッター
	 */
	public String getWorkDurationText() {
		return workDuration != null ? workDuration : "-";
	}
}
