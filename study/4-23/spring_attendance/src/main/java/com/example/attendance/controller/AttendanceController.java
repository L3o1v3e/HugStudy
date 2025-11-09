package com.example.attendance.controller;

import com.example.attendance.entity.Attendance;
import com.example.attendance.form.AttendanceForm;
import com.example.attendance.service.AttendanceService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private AttendanceService attendanceService;

	/** 勤怠打刻画面 */
	@GetMapping("/punch")
	public String showPunchForm(Model model) {
		AttendanceForm form = new AttendanceForm();
		form.setWorkDate(LocalDate.now());
		form.setCheckInTime(LocalTime.now());
		model.addAttribute("attendanceForm", form);
		return "attendance/punch";
	}

	/** 出勤・退勤処理 */
	@PostMapping("/punch")
	public String punchSubmit(
			@Valid @ModelAttribute("attendanceForm") AttendanceForm form,
			BindingResult bindingResult,
			@RequestParam("action") String action,
			Model model) {

		if (bindingResult.hasErrors()) {
			return "attendance/punch";
		}

		try {
			if ("checkIn".equals(action)) {
				attendanceService.punchIn(form);
			} else if ("checkOut".equals(action)) {
				attendanceService.punchOut(form);
			} else {
				model.addAttribute("error", "不正な操作です。");
				return "attendance/punch";
			}
		} catch (Exception e) {
			model.addAttribute("error", "打刻処理中にエラーが発生しました: " + e.getMessage());
			return "attendance/punch";
		}

		return "redirect:/attendance/list";
	}

	/** 勤怠一覧画面 */
	@GetMapping("/list")
	public String showAttendanceList(Model model) {
		List<Attendance> attendanceList = attendanceService.findAll();
		model.addAttribute("attendanceList", attendanceList);
		return "attendance/list";
	}

	// 削除確認画面
	@GetMapping("/delete/{id}")
	public String confirmDelete(@PathVariable Long id, Model model) {
		Attendance attendance = attendanceService.findById(id);
		model.addAttribute("attendance", attendance);
		return "attendance/delete_confirm";
	}

	// 削除実行
	@PostMapping("/delete")
	public String deleteAttendance(@RequestParam Long id) {
		attendanceService.deleteById(id);
		return "redirect:/attendance/list";
	}

	// 編集画面の表示
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		Attendance attendance = attendanceService.findById(id);

		AttendanceForm form = new AttendanceForm();
		form.setId(attendance.getId());
		form.setEmployeeName(attendance.getEmployeeName());
		form.setWorkDate(attendance.getWorkDate());

		if (attendance.getCheckInTime() != null) {
			form.setCheckInTime(attendance.getCheckInTime().toLocalTime());
		}
		if (attendance.getCheckOutTime() != null) {
			form.setCheckOutTime(attendance.getCheckOutTime().toLocalTime());
		}

		model.addAttribute("attendanceForm", form);
		return "attendance/edit"; // ← resources/templates/attendance/edit.html に遷移
	}

	@PostMapping("/edit")
	public String updateAttendance(
			@Valid @ModelAttribute("attendanceForm") AttendanceForm form,
			BindingResult bindingResult,
			Model model) {

		System.out.println("✅ updateAttendance 呼び出し");
		System.out.println("✅ フォーム内容: " + form);

		if (bindingResult.hasErrors()) {
			System.out.println("⚠️ バリデーションエラー: " + bindingResult.getAllErrors());
			return "attendance/edit";
		}

		try {
			attendanceService.updateAttendance(form);
			System.out.println("✅ 勤怠更新完了: " + form.getId());
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "更新処理中にエラーが発生しました: " + e.getMessage());
			return "attendance/edit";
		}

		return "redirect:/attendance/list";
	}
}
