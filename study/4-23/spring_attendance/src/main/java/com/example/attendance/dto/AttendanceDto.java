package com.example.attendance.dto;

import com.example.attendance.entity.AttendanceEntity;
import lombok.Data;

import java.time.Duration;
import java.time.format.DateTimeFormatter;

@Data
public class AttendanceDto {

    private Long id;
    private String employeeName;
    private String workDateText;
    private String checkInTimeText;
    private String checkOutTimeText;
    private String workTime;
    private String breakInfo;

    /** Entity から DTO 生成 */
    public AttendanceDto(AttendanceEntity entity) {
        this.id = entity.getId();
        this.employeeName = entity.getEmployeeName();

        this.workDateText = entity.getWorkDate() != null
                ? entity.getWorkDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                : "-";

        this.checkInTimeText = entity.getCheckInTime() != null
                ? entity.getCheckInTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                : "-";

        this.checkOutTimeText = entity.getCheckOutTime() != null
                ? entity.getCheckOutTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                : "-";

        if (entity.getCheckInTime() != null && entity.getCheckOutTime() != null) {
            Duration duration = Duration.between(entity.getCheckInTime(), entity.getCheckOutTime());
            long hours = duration.toHours();
            long minutes = duration.toMinutesPart();

            boolean applied = false;
            if (hours >= 8) {
                hours -= 1; // 休憩1時間を引く
                applied = true;
            }

            this.workTime = String.format("%d時間%d分", hours, minutes);
            this.breakInfo = applied ? "(休憩1時間含む)" : "";
        } else {
            this.workTime = "-";
            this.breakInfo = "";
        }
    }
}
