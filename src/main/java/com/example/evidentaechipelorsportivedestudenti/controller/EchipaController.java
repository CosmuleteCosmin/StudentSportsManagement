/**
 * Controller pentru gestionarea echipelor si filtrarea acestora dupa Sport/Facultate
 * @author Cosmulete Ion-Cosmin
 * @version 10 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.controller;

import com.example.evidentaechipelorsportivedestudenti.model.Echipa;
import com.example.evidentaechipelorsportivedestudenti.repository.EchipaRepository;
import com.example.evidentaechipelorsportivedestudenti.repository.FacultateRepository;
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
@RequestMapping("/echipe")
public class EchipaController {

    @Autowired private EchipaRepository echipaRepository;
    @Autowired private FacultateRepository facultateRepository;
    @Autowired private SportRepository sportRepository;

    @GetMapping("")
    public String afiseazaLista(Model model,
                                 @RequestParam(value = "keyword", required = false) String keyword,
                                 @RequestParam(value = "sportId", required = false) Integer sportId,
                                 @RequestParam(value = "facultateId", required = false) Integer facultateId) {

        List<Echipa> lista = echipaRepository.filtreazaSiCautaEchipeSQL(sportId, facultateId, keyword);
        model.addAttribute("echipe", lista);

        model.addAttribute("keyword", keyword);
        model.addAttribute("sportId", sportId);
        model.addAttribute("facultateId", facultateId);

        model.addAttribute("listaSporturi", sportRepository.gasesteToateSporturileSQL());
        model.addAttribute("listaFacultati", facultateRepository.gasesteToateFacultatileSQL());

        return "echipe/echipe";
    }

    @GetMapping("/adauga")
    public String showAddForm(Model model) {
        model.addAttribute("echipa", new Echipa());
        model.addAttribute("listaFacultati", facultateRepository.gasesteToateFacultatileSQL());
        model.addAttribute("listaSporturi", sportRepository.gasesteToateSporturileSQL());
        return "echipe/adauga-echipa";
    }

    @PostMapping("/salveaza")
    public String save(@Valid @ModelAttribute("echipa") Echipa echipa, BindingResult bindingResult, Model model) {
        if (echipaRepository.countByFacultateAndSportSQL(echipa.getFacultate().getFacultateID(), echipa.getSport().getSportID()) > 0) {
            bindingResult.rejectValue("sport", "error.echipa", "Această facultate are deja o echipă la acest sport!");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("listaFacultati", facultateRepository.gasesteToateFacultatileSQL());
            model.addAttribute("listaSporturi", sportRepository.gasesteToateSporturileSQL());
            return "echipe/adauga-echipa";
        }
        echipaRepository.insereazaEchipaSQL(echipa.getSport().getSportID(), echipa.getFacultate().getFacultateID());
        return "redirect:/echipe";
    }

    @GetMapping("/sterge/{id}")
    public String sterge(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            echipaRepository.stergeEchipaSQL(id);
            redirectAttributes.addFlashAttribute("mesajSucces", "Echipa a fost ștearsă!");
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("mesajEroare", "Nu puteți șterge echipa deoarece are membri înscriși sau meciuri programate!");
        }
        return "redirect:/echipe";
    }

    @GetMapping("/editeaza/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Echipa echipa = echipaRepository.gasesteDupaIdSQL(id);
        model.addAttribute("echipa", echipa);
        model.addAttribute("listaFacultati", facultateRepository.gasesteToateFacultatileSQL());
        model.addAttribute("listaSporturi", sportRepository.gasesteToateSporturileSQL());
        return "echipe/editeaza-echipa";
    }

    @PostMapping("/actualizeaza/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("echipa") Echipa echipa, BindingResult bindingResult, Model model) {
        if (echipaRepository.countByFacultateAndSportUpdateSQL(echipa.getFacultate().getFacultateID(), echipa.getSport().getSportID(), id) > 0) {
            bindingResult.rejectValue("sport", "error.echipa", "Această facultate are deja o echipă la acest sport!");
        }

        if (bindingResult.hasErrors()) {
            echipa.setEchipaID(id);
            model.addAttribute("listaFacultati", facultateRepository.gasesteToateFacultatileSQL());
            model.addAttribute("listaSporturi", sportRepository.gasesteToateSporturileSQL());
            return "echipe/editeaza-echipa";
        }
        echipaRepository.actualizeazaEchipaSQL(id, echipa.getSport().getSportID(), echipa.getFacultate().getFacultateID());
        return "redirect:/echipe";
    }
}