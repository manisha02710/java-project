package com.rbac.controller;

import com.rbac.annotation.RequiresPermission;
import com.rbac.model.Student;
import com.rbac.model.Mark;
import com.rbac.model.Attendance;
import com.rbac.service.FacultyService;
import com.rbac.service.AuthorizationService;
import com.rbac.model.SessionUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for faculty actions.
 * // OOP: Encapsulation – thin controller delegates to FacultyService.
 */
@Controller
public class FacultyController {

    private final FacultyService facultyService;
    private final AuthorizationService authorizationService;

    public FacultyController(FacultyService facultyService, AuthorizationService authorizationService) {
        this.facultyService = facultyService;
        this.authorizationService = authorizationService;
    }

    private SessionUser getSessionUser(HttpSession session) {
        return (SessionUser) session.getAttribute("sessionUser");
    }

    @RequiresPermission("STUDENT_READ")
    @GetMapping("/faculty/students")
    public String viewStudents(Model model, HttpSession session) {
        SessionUser su = getSessionUser(session);
        List<Student> students = facultyService.listAllStudents();
        model.addAttribute("students", students);
        return "faculty/students";
    }

    @RequiresPermission("MARKS_READ")
    @GetMapping("/faculty/student/marks")
    public String viewStudentMarks(@RequestParam Long studentId, Model model, HttpSession session) {
        SessionUser su = getSessionUser(session);
        List<Mark> marks = facultyService.getMarksForStudent(studentId);
        model.addAttribute("marks", marks);
        model.addAttribute("studentId", studentId);
        return "faculty/studentMarks";
    }

    @RequiresPermission("MARKS_ADD")
    @PostMapping("/faculty/student/marks/add")
    public String addMark(@RequestParam Long studentId,
                          @RequestParam String subject,
                          @RequestParam Integer marks,
                          RedirectAttributes redirectAttrs,
                          HttpSession session) {
        SessionUser su = getSessionUser(session);
        facultyService.addMark(studentId, subject, marks, su.getUserId());
        redirectAttrs.addFlashAttribute("success", "Mark added.");
        return "redirect:/faculty/student/marks?studentId=" + studentId;
    }

    @RequiresPermission("ATTENDANCE_VIEW")
    @GetMapping("/faculty/student/attendance")
    public String viewAttendance(@RequestParam Long studentId, Model model, HttpSession session) {
        List<Attendance> attendance = facultyService.getAttendanceForStudent(studentId);
        model.addAttribute("attendance", attendance);
        model.addAttribute("studentId", studentId);
        return "faculty/studentAttendance";
    }

    @RequiresPermission("ATTENDANCE_MANAGE")
    @PostMapping("/faculty/student/attendance/save")
    public String saveAttendance(@RequestParam Long studentId,
                                 @RequestParam String subject,
                                 @RequestParam Integer totalClasses,
                                 @RequestParam Integer attendedClasses,
                                 RedirectAttributes redirectAttrs,
                                 HttpSession session) {
        SessionUser su = getSessionUser(session);
        facultyService.saveAttendance(studentId, subject, totalClasses, attendedClasses, su.getUserId());
        redirectAttrs.addFlashAttribute("success", "Attendance saved.");
        return "redirect:/faculty/student/attendance?studentId=" + studentId;
    }
}
