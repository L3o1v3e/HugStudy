package com.example.attendance.controller;

import com.example.attendance.dto.AttendanceDto;
import com.example.attendance.entity.AttendanceEntity;
import com.example.attendance.form.AttendanceForm;
import com.example.attendance.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    /** 勤怠一覧表示 */
    @GetMapping("/list")
    public String showAttendanceList(Model model) {
        List<AttendanceDto> attendanceList = attendanceService.findAllDtos();
        model.addAttribute("attendanceList", attendanceList);
        return "attendance/list";
    }

    /** 打刻フォーム表示 */
    @GetMapping("/punch")
    public String punchForm(Model model) {
        AttendanceForm form = new AttendanceForm();
        form.setWorkDate(LocalDate.now());
        model.addAttribute("attendanceForm", form);
        return "attendance/punch";
    }

    /** 打刻処理（出勤/退勤） */
    @PostMapping("/punch")
    public String punchSubmit(@Valid @ModelAttribute("attendanceForm") AttendanceForm form,
                              BindingResult result,
                              @RequestParam String action,
                              Model model) {

        if (result.hasErrors()) {
            return "attendance/punch";
        }

        try {
            if ("checkIn".equals(action)) {
                attendanceService.punchIn(form);
            } else if ("checkOut".equals(action)) {
                attendanceService.punchOut(form);
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            result.reject("serviceError", e.getMessage());
            return "attendance/punch";
        }

        return "redirect:/attendance/list";
    }

    /** 編集画面表示 */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        AttendanceEntity entity = attendanceService.findById(id);

        AttendanceForm form = new AttendanceForm();
        form.setId(entity.getId());
        form.setEmployeeName(entity.getEmployeeName());
        form.setWorkDate(entity.getWorkDate());
        form.setCheckInTime(entity.getCheckInTime() != null ? entity.getCheckInTime().toLocalTime() : null);
        form.setCheckOutTime(entity.getCheckOutTime() != null ? entity.getCheckOutTime().toLocalTime() : null);

        model.addAttribute("attendanceForm", form);
        return "attendance/edit";
    }

    /** 編集更新処理 */
    @PostMapping("/edit")
    public String editSubmit(@Valid @ModelAttribute("attendanceForm") AttendanceForm form,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            return "attendance/edit";
        }

        AttendanceEntity entity = attendanceService.findById(form.getId());

        LocalDateTime updatedCheckIn = form.getCheckInTime() != null
                ? LocalDateTime.of(entity.getWorkDate(), form.getCheckInTime())
                : entity.getCheckInTime();

        LocalDateTime updatedCheckOut = form.getCheckOutTime() != null
                ? LocalDateTime.of(entity.getWorkDate(), form.getCheckOutTime())
                : entity.getCheckOutTime();

        // 出勤・退勤の時間整合性チェック
        if (updatedCheckIn != null && updatedCheckOut != null && !updatedCheckOut.isAfter(updatedCheckIn)) {
            result.reject("invalidTime", "退勤時刻は出勤時刻より後である必要があります");
            return "attendance/edit";
        }

        attendanceService.updateAttendancePartial(entity, updatedCheckIn, updatedCheckOut);

        return "redirect:/attendance/list";
    }

    /** 削除確認画面表示 */
    @GetMapping("/delete/{id}")
    public String showDeleteConfirm(@PathVariable Long id, Model model) {
        AttendanceEntity entity = attendanceService.findById(id);

        // DTO に変換
        AttendanceDto attendance = new AttendanceDto(entity);

        model.addAttribute("attendance", attendance);
        return "attendance/delete_confirm"; 
    }

    /** 削除実行 */
    @PostMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            attendanceService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "削除しました");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "削除に失敗しました");
        }
        return "redirect:/attendance/list";
    }
}
