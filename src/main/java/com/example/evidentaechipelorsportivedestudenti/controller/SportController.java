/**
 * Controller pentru gestionarea tipurilor de sporturi (Individual/Echipa)
 * @author Cosmulete Ion-Cosmin
 * @version 10 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.controller;

import com.example.evidentaechipelorsportivedestudenti.model.Sport;
import com.example.evidentaechipelorsportivedestudenti.repository.SportRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/sporturi")
public class SportController {

    @Autowired
    private SportRepository sportRepository;

    @GetMapping("")
    public String afiseazaLista(Model model,
                                   @RequestParam(value = "keyword", required = false) String keyword,
                                   @RequestParam(value = "tip", required = false) String tip) {

        List<Sport> lista = sportRepository.filtreazaSiCautaSporturiSQL(tip, keyword);

        model.addAttribute("sporturi", lista);
        model.addAttribute("keyword", keyword);
        model.addAttribute("tipSelectat", tip);

        return "sporturi/sporturi";
    }

    @GetMapping("/adauga")
    public String showAddForm(Model model) {
        model.addAttribute("sport", new Sport());
        return "sporturi/adauga-sport";
    }

    @PostMapping("/salveaza")
    public String save(@Valid @ModelAttribute("sport") Sport sport, BindingResult bindingResult) {
        if (sportRepository.countByNumeSQL(sport.getNume()) > 0) {
            bindingResult.rejectValue("nume", "error.sport", "Acest sport există deja!");
        }

        if (bindingResult.hasErrors()) {
            return "sporturi/adauga-sport";
        }
        sportRepository.insereazaSportSQL(sport.getNume(), sport.getTip());
        return "redirect:/sporturi";
    }

    @GetMapping("/sterge/{id}")
    public String sterge(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            sportRepository.stergeSportSQL(id);
            redirectAttributes.addFlashAttribute("mesajSucces", "Sportul a fost șters!");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mesajEroare", "Nu puteți șterge sportul deoarece există echipe care îl practică!");
        }
        return "redirect:/sporturi";
    }

    @GetMapping("/editeaza/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Sport sport = sportRepository.gasesteDupaIdSQL(id);
        model.addAttribute("sport", sport);
        return "sporturi/editeaza-sport";
    }

    @PostMapping("/actualizeaza/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("sport") Sport sport, BindingResult bindingResult) {

        // Verificam duplicate
        if (sportRepository.countByNumeUpdateSQL(sport.getNume(), id) > 0) {
            bindingResult.rejectValue("nume", "error.sport", "Acest sport există deja!");
        }

        if (bindingResult.hasErrors()) {
            sport.setSportID(id);
            return "sporturi/editeaza-sport";
        }

        sportRepository.actualizeazaSportSQL(id, sport.getNume(), sport.getTip());
        return "redirect:/sporturi";
    }
}