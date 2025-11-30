package com.example.attendance.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AttendanceForm {

    private Long id;

    @NotBlank(message = "社員名を入力してください")
    @Pattern(regexp = "^[^\\s]+　[^\\s]+$", message = "社員名は「姓＋全角スペース＋名」で入力してください")
    private String employeeName;

    @NotNull(message = "勤務日を選択してください")
    private LocalDate workDate;

    private LocalTime checkInTime;

    private LocalTime checkOutTime;
}
