package com.example.attendance.service;

import com.example.attendance.dto.AttendanceDto;
import com.example.attendance.entity.AttendanceEntity;
import com.example.attendance.entity.UserEntity;
import com.example.attendance.form.AttendanceForm;
import com.example.attendance.repository.AttendanceRepository;
import com.example.attendance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    /** 出勤打刻 */
    @Transactional
    public void punchIn(AttendanceForm form) {
        validatePunchIn(form);

        UserEntity employee = userRepository.findByName(form.getEmployeeName())
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setName(form.getEmployeeName());
                    return userRepository.save(newUser);
                });

        if (attendanceRepository.findByEmployeeAndWorkDate(employee, form.getWorkDate()).isPresent()) {
            throw new IllegalStateException("この日はすでに出勤済みです");
        }

        AttendanceEntity entity = new AttendanceEntity();
        entity.setEmployee(employee);
        entity.setEmployeeName(employee.getName());
        entity.setWorkDate(form.getWorkDate());
        entity.setCheckInTime(LocalDateTime.of(form.getWorkDate(), form.getCheckInTime()));

        attendanceRepository.save(entity);
    }

    /** 退勤打刻 */
    @Transactional
    public void punchOut(AttendanceForm form) {
        validatePunchOut(form);

        UserEntity employee = userRepository.findByName(form.getEmployeeName())
                .orElseThrow(() -> new NoSuchElementException("社員が存在しません"));

        AttendanceEntity entity = attendanceRepository.findByEmployeeAndWorkDate(employee, form.getWorkDate())
                .orElseThrow(() -> new NoSuchElementException("出勤記録がありません"));

        LocalDateTime checkOutDateTime = LocalDateTime.of(form.getWorkDate(), form.getCheckOutTime());

        // 時間整合性チェック
        if (!checkOutDateTime.isAfter(entity.getCheckInTime())) {
            throw new IllegalArgumentException("退勤時刻は出勤時刻より後である必要があります");
        }
        if (checkOutDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("退勤時刻は未来時刻を指定できません");
        }

        entity.setCheckOutTime(checkOutDateTime);
        attendanceRepository.save(entity);
    }

    /** 勤怠一覧取得（DTO 変換） */
    public List<AttendanceDto> findAllDtos() {
        return attendanceRepository.findAllByOrderByWorkDateDesc()
                .stream()
                .map(AttendanceDto::new)
                .toList();
    }

    /** 勤怠取得（ID） */
    public AttendanceEntity findById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("勤怠が見つかりません id=" + id));
    }

    /** 勤怠削除 */
    @Transactional
    public void deleteById(Long id) {
        AttendanceEntity entity = findById(id);
        attendanceRepository.delete(entity);
    }

    /** 勤怠更新（編集用部分更新） */
    @Transactional
    public void updateAttendancePartial(AttendanceEntity entity,
                                        LocalDateTime newCheckIn,
                                        LocalDateTime newCheckOut) {
        if (newCheckIn != null) entity.setCheckInTime(newCheckIn);
        if (newCheckOut != null) entity.setCheckOutTime(newCheckOut);
        attendanceRepository.save(entity);
    }

    /** 出勤バリデーション */
    private void validatePunchIn(AttendanceForm form) {
        if (form.getEmployeeName() == null || form.getEmployeeName().isBlank()) {
            throw new IllegalArgumentException("社員名を入力してください");
        }
        if (!form.getEmployeeName().contains("　")) {
            throw new IllegalArgumentException("社員名は姓と名の間に全角スペースを入れてください");
        }
        if (form.getCheckInTime() == null) {
            throw new IllegalArgumentException("出勤時刻を入力してください");
        }
        if (form.getWorkDate() == null) {
            throw new IllegalArgumentException("勤務日を入力してください");
        }
        if (form.getWorkDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("勤務日は未来日を指定できません");
        }
    }

    /** 退勤バリデーション */
    private void validatePunchOut(AttendanceForm form) {
        if (form.getEmployeeName() == null || form.getEmployeeName().isBlank()) {
            throw new IllegalArgumentException("社員名を入力してください");
        }
        if (!form.getEmployeeName().contains("　")) {
            throw new IllegalArgumentException("社員名は姓と名の間に全角スペースを入れてください");
        }
        if (form.getCheckOutTime() == null) {
            throw new IllegalArgumentException("退勤時刻を入力してください");
        }
        if (form.getWorkDate() == null) {
            throw new IllegalArgumentException("勤務日を入力してください");
        }
        if (form.getWorkDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("勤務日は未来日を指定できません");
        }
    }
}
