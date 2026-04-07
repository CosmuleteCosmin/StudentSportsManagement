/**
 * Controller pentru inscrierea studentilor in echipe si gestionarea rolurilor (Capitan, Titular, etc.)
 * @author Cosmulete Ion-Cosmin
 * @version 10 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.controller;

import com.example.evidentaechipelorsportivedestudenti.model.MembruEchipa;
import com.example.evidentaechipelorsportivedestudenti.repository.*;
import jakarta.validation.Valid; // Import necesar
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; // Import necesar
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/membrii")
public class MembruController {

    @Autowired private MembruEchipaRepository membruEchipaRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private EchipaRepository echipaRepository;
    @Autowired private FacultateRepository facultateRepository;
    @Autowired private SportRepository sportRepository;

    @GetMapping("")
    public String afiseazaMembrii(Model model,
                                  @RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "facultateId", required = false) Integer facultateId,
                                  @RequestParam(value = "sportId", required = false) Integer sportId,
                                  @RequestParam(value = "rol", required = false) String rol) {

        List<MembruEchipa> lista = membruEchipaRepository.filtreazaSiCautaMembriSQL(facultateId, sportId, rol, keyword);
        model.addAttribute("membrii", lista);

        model.addAttribute("keyword", keyword);
        model.addAttribute("facultateId", facultateId);
        model.addAttribute("sportId", sportId); // Trimitem ID-ul sportului selectat
        model.addAttribute("rolSelectat", rol);

        model.addAttribute("listaFacultati", facultateRepository.gasesteToateFacultatileSQL());

        model.addAttribute("listaSporturi", sportRepository.gasesteToateSporturileSQL());

        return "membrii/membrii";
    }

    @GetMapping("/adauga")
    public String showAddForm(Model model) {
        model.addAttribute("membru", new MembruEchipa());
        model.addAttribute("listaStudenti", studentRepository.gasesteTotiStudentiiSQL());
        model.addAttribute("listaEchipe", echipaRepository.gasesteToateEchipeleSQL());
        return "membrii/adauga-membru";
    }

    @PostMapping("/salveaza")
    public String save(@Valid @ModelAttribute("membru") MembruEchipa membru,
                       BindingResult bindingResult,
                       Model model) {

        // Verificam daca studentul este deja in echipa
        if (membru.getStudent() != null && membru.getEchipa() != null) {
            int exista = membruEchipaRepository.countDuplicateMemberSQL(
                    membru.getStudent().getStudentID(),
                    membru.getEchipa().getEchipaID()
            );
            if (exista > 0) {
                bindingResult.rejectValue("student", "error.membru", "Acest student este deja membru al echipei!");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("listaStudenti", studentRepository.gasesteTotiStudentiiSQL());
            model.addAttribute("listaEchipe", echipaRepository.gasesteToateEchipeleSQL());
            return "membrii/adauga-membru";
        }

        membruEchipaRepository.insereazaMembruSQL(
                membru.getStudent().getStudentID(),
                membru.getEchipa().getEchipaID(),
                membru.getRol());
        return "redirect:/membrii";
    }

    @GetMapping("/sterge/{sid}/{eid}")
    public String sterge(@PathVariable("sid") Integer sid, @PathVariable("eid") Integer eid) {
        membruEchipaRepository.stergeMembruSQL(sid, eid);
        return "redirect:/membrii";
    }

    @GetMapping("/editeaza/{sid}/{eid}")
    public String showEditForm(@PathVariable("sid") Integer sid, @PathVariable("eid") Integer eid, Model model) {
        MembruEchipa membru = membruEchipaRepository.gasesteDupaIdCompusSQL(sid, eid);
        model.addAttribute("membru", membru);
        return "membrii/editeaza-membru";
    }

    @PostMapping("/actualizeaza/{sid}/{eid}")
    public String update(@PathVariable("sid") Integer sid,
                         @PathVariable("eid") Integer eid,
                         @Valid @ModelAttribute("membru") MembruEchipa membru,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            MembruEchipa dateOriginale = membruEchipaRepository.gasesteDupaIdCompusSQL(sid, eid);
            membru.setStudent(dateOriginale.getStudent());
            membru.setEchipa(dateOriginale.getEchipa());
            return "membrii/editeaza-membru";
        }

        membruEchipaRepository.actualizeazaRolMembruSQL(sid, eid, membru.getRol());
        return "redirect:/membrii";
    }
}