/**
 * Controller pentru gestionarea studentilor (Adaugare, Editare, Stergere, Cautare)
 * @author Cosmulete Ion-Cosmin
 * @version 10 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.controller;

import com.example.evidentaechipelorsportivedestudenti.model.Student;
import com.example.evidentaechipelorsportivedestudenti.repository.FacultateRepository;
import com.example.evidentaechipelorsportivedestudenti.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
@RequestMapping("/studenti")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultateRepository facultateRepository;

    @GetMapping("")
    public String afiseazaLista(Model model,
                                   @RequestParam(value = "keyword", required = false) String keyword,
                                   @RequestParam(value = "facultateId", required = false) Integer facultateId) {

        List<Student> lista = studentRepository.filtreazaSiCautaStudentiSQL(facultateId, keyword);

        model.addAttribute("studenti", lista);
        model.addAttribute("keyword", keyword);
        model.addAttribute("facultateId", facultateId);

        model.addAttribute("listaFacultati", facultateRepository.gasesteToateFacultatileSQL());

        return "studenti/studenti";
    }

    @GetMapping("/adauga")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("listaFacultati", facultateRepository.gasesteToateFacultatileSQL());
        return "studenti/adauga-student";
    }

    @PostMapping("/salveaza")
    public String save(@Valid @ModelAttribute("student") Student student,
                       BindingResult bindingResult,
                       Model model) {


        // Verificam Email-ul
        if (studentRepository.countByEmailSQL(student.getEmail()) > 0) {
            bindingResult.rejectValue("email", "error.student", "Acest email este deja folosit!");
        }

        // Verificam Matricolul
        if (studentRepository.countByMatricolSQL(student.getNumarMatricol()) > 0) {
            bindingResult.rejectValue("numarMatricol", "error.student", "Acest număr matricol există deja!");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("listaFacultati", facultateRepository.gasesteToateFacultatileSQL());
            return "studenti/adauga-student";
        }

        studentRepository.insereazaStudentSQL(
                student.getNume(), student.getPrenume(), student.getEmail(),
                student.getNumarMatricol(), student.getFacultate().getFacultateID());
        return "redirect:/studenti";
    }

    @GetMapping("/sterge/{id}")
    public String sterge(@PathVariable("id") Integer id) {
        studentRepository.stergeStudentSQL(id);
        return "redirect:/studenti";
    }

    @GetMapping("/editeaza/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Student std = studentRepository.gasesteDupaIdSQL(id);
        model.addAttribute("student", std);
        model.addAttribute("listaFacultati", facultateRepository.gasesteToateFacultatileSQL());
        return "studenti/editeaza-student";
    }

    @PostMapping("/actualizeaza/{id}")
    public String update(@PathVariable("id") Integer id,
                         @Valid @ModelAttribute("student") Student student,
                         BindingResult bindingResult,
                         Model model) {

        if (studentRepository.countByEmailUpdateSQL(student.getEmail(), id) > 0) {
            bindingResult.rejectValue("email", "error.student", "Acest email este deja folosit de alt student!");
        }
        if (studentRepository.countByMatricolUpdateSQL(student.getNumarMatricol(), id) > 0) {
            bindingResult.rejectValue("numarMatricol", "error.student", "Acest număr matricol există deja!");
        }

        if (bindingResult.hasErrors()) {
            student.setStudentID(id);
            model.addAttribute("listaFacultati", facultateRepository.gasesteToateFacultatileSQL());
            return "studenti/editeaza-student";
        }

        studentRepository.actualizeazaStudentSQL(
                id, student.getNume(), student.getPrenume(), student.getEmail(),
                student.getNumarMatricol(), student.getFacultate().getFacultateID());
        return "redirect:/studenti";
    }
}