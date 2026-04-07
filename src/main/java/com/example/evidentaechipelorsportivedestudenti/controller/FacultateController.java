/**
 * Controller pentru administrarea facultatilor din universitate
 * @author Cosmulete Ion-Cosmin
 * @version 10 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.controller;

import com.example.evidentaechipelorsportivedestudenti.model.Facultate;
import com.example.evidentaechipelorsportivedestudenti.repository.FacultateRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/facultati")
public class FacultateController {

    @Autowired
    private FacultateRepository facultateRepository;

    @GetMapping("")
    public String afiseazaLista(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Facultate> lista;
        if (keyword != null && !keyword.isEmpty()) {
            lista = facultateRepository.cautaFacultatiSQL(keyword);
        } else {
            lista = facultateRepository.gasesteToateFacultatileSQL();
        }
        model.addAttribute("facultati", lista);
        model.addAttribute("keyword", keyword);
        return "facultati/facultati";
    }

    @GetMapping("/adauga")
    public String showAddForm(Model model) {
        model.addAttribute("facultate", new Facultate());
        return "facultati/adauga-facultate";
    }

    @PostMapping("/salveaza")
    public String save(@Valid @ModelAttribute("facultate") Facultate facultate, BindingResult bindingResult) {
        if (facultateRepository.countByDenumireSQL(facultate.getDenumire()) > 0) {
            bindingResult.rejectValue("denumire", "error.facultate", "Există deja o facultate cu acest nume!");
        }

        if (bindingResult.hasErrors()) {
            return "facultati/adauga-facultate";
        }
        facultateRepository.insereazaFacultateSQL(facultate.getDenumire());
        return "redirect:/facultati";
    }

    @GetMapping("/sterge/{id}")
    public String sterge(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            facultateRepository.stergeFacultateSQL(id);
            redirectAttributes.addFlashAttribute("mesajSucces", "Facultatea a fost ștearsă cu succes!");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Dacă intervine o eroare de cheie străină (FK), trimitem un mesaj de alertă
            redirectAttributes.addFlashAttribute("mesajEroare",
                    "Nu se poate șterge facultatea deoarece există studenți sau echipe înscrise la aceasta!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mesajEroare", "A apărut o eroare neașteptată!");
        }
        return "redirect:/facultati";
    }

    @GetMapping("/editeaza/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Facultate facultate = facultateRepository.gasesteDupaIdSQL(id);
        model.addAttribute("facultate", facultate);
        return "facultati/editeaza-facultate";
    }

    @PostMapping("/actualizeaza/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("facultate") Facultate facultate, BindingResult bindingResult) {
        if (facultateRepository.countByDenumireUpdateSQL(facultate.getDenumire(), id) > 0) {
            bindingResult.rejectValue("denumire", "error.facultate", "Există deja o facultate cu acest nume!");
        }

        if (bindingResult.hasErrors()) {
            facultate.setFacultateID(id);
            return "facultati/editeaza-facultate";
        }
        facultateRepository.actualizeazaFacultateSQL(id, facultate.getDenumire());
        return "redirect:/facultati";
    }
}