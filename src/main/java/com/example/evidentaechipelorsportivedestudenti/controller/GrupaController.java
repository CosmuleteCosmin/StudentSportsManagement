/**
 * Controller pentru gestionarea grupelor competitionale si repartizarea echipelor in serii
 * @author Cosmulete Ion-Cosmin
 * @version 09 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.controller;

import com.example.evidentaechipelorsportivedestudenti.model.Grupa;
import com.example.evidentaechipelorsportivedestudenti.repository.GrupaRepository;
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
@RequestMapping("/grupe")
public class GrupaController {

    @Autowired private GrupaRepository grupaRepository;
    @Autowired private SportRepository sportRepository;

    @GetMapping("")
    public String afiseazaLista(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Grupa> lista;
        if (keyword != null && !keyword.isEmpty()) {
            lista = grupaRepository.cautaGrupeSQL(keyword);
        } else {
            lista = grupaRepository.gasesteToateGrupeleSQL();
        }
        model.addAttribute("grupe", lista);
        model.addAttribute("keyword", keyword);
        return "grupe/grupe";
    }

    @GetMapping("/adauga")
    public String showAddForm(Model model) {
        model.addAttribute("grupa", new Grupa());
        model.addAttribute("listaSporturi", sportRepository.gasesteToateSporturileSQL());
        return "grupe/adauga-grupa";
    }

    @PostMapping("/salveaza")
    public String save(@Valid @ModelAttribute("grupa") Grupa grupa, BindingResult bindingResult, Model model) {
        if (grupaRepository.countByNumeSQL(grupa.getNume()) > 0) {
            bindingResult.rejectValue("nume", "error.grupa", "O grupă cu acest nume există deja!");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("listaSporturi", sportRepository.gasesteToateSporturileSQL());
            return "grupe/adauga-grupa";
        }
        grupaRepository.insereazaGrupaSQL(grupa.getNume(), grupa.getSport().getSportID());
        return "redirect:/grupe";
    }

    @GetMapping("/sterge/{id}")
    public String sterge(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            grupaRepository.stergeGrupaSQL(id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mesajEroare", "Nu puteți șterge grupa deoarece conține meciuri active!");
        }
        return "redirect:/grupe";
    }

    @GetMapping("/editeaza/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Grupa grupa = grupaRepository.gasesteDupaIdSQL(id);
        model.addAttribute("grupa", grupa);
        model.addAttribute("listaSporturi", sportRepository.gasesteToateSporturileSQL());
        return "grupe/editeaza-grupa";
    }

    @PostMapping("/actualizeaza/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("grupa") Grupa grupa, BindingResult bindingResult, Model model) {
        if (grupaRepository.countByNumeUpdateSQL(grupa.getNume(), id) > 0) {
            bindingResult.rejectValue("nume", "error.grupa", "O grupă cu acest nume există deja!");
        }

        if (bindingResult.hasErrors()) {
            grupa.setGrupaID(id);
            model.addAttribute("listaSporturi", sportRepository.gasesteToateSporturileSQL());
            return "grupe/editeaza-grupa";
        }
        grupaRepository.actualizeazaGrupaSQL(id, grupa.getNume(), grupa.getSport().getSportID());
        return "redirect:/grupe";
    }
}