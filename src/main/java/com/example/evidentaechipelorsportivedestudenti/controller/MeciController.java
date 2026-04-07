/**
 * Controller pentru programarea meciurilor si filtrarea dupa data/locatie
 * @author Cosmulete Ion-Cosmin
 * @version 10 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.controller;

import com.example.evidentaechipelorsportivedestudenti.model.Echipa;
import com.example.evidentaechipelorsportivedestudenti.model.Meci;
import com.example.evidentaechipelorsportivedestudenti.repository.EchipaRepository;
import com.example.evidentaechipelorsportivedestudenti.repository.GrupaRepository;
import com.example.evidentaechipelorsportivedestudenti.repository.MeciRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/meciuri")
public class MeciController {

    @Autowired private MeciRepository meciRepository;
    @Autowired private EchipaRepository echipaRepository;
    @Autowired private GrupaRepository grupaRepository;

    @GetMapping("")
    public String afiseazaLista(Model model,
                                @RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(value = "echipa1Id", required = false) Integer echipa1Id,
                                @RequestParam(value = "echipa2Id", required = false) Integer echipa2Id,
                                @RequestParam(value = "locatie", required = false) String locatie,
                                @RequestParam(value = "data", required = false) String dataStr) {

        java.sql.Date data = null;
        if (dataStr != null && !dataStr.isEmpty()) {
            try {
                data = java.sql.Date.valueOf(dataStr);
            } catch (Exception e) {
            }
        }

        List<Meci> lista = meciRepository.filtreazaSiCautaMeciuriSQL(echipa1Id, echipa2Id, locatie, data, keyword);
        model.addAttribute("meciuri", lista);
        model.addAttribute("keyword", keyword);
        model.addAttribute("echipa1Id", echipa1Id);
        model.addAttribute("echipa2Id", echipa2Id);
        model.addAttribute("locatieFiltru", locatie);
        model.addAttribute("dataFiltru", data);
        model.addAttribute("listaEchipe", echipaRepository.gasesteToateEchipeleSQL());

        return "meciuri/meciuri";
    }

    @GetMapping("/adauga")
    public String showAddForm(Model model) {
        model.addAttribute("meci", new Meci());
        model.addAttribute("listaEchipe", echipaRepository.gasesteToateEchipeleSQL());
        model.addAttribute("listaGrupe", grupaRepository.gasesteToateGrupeleSQL());
        return "meciuri/adauga-meci";
    }

    @PostMapping("/salveaza")
    public String save(@Valid @ModelAttribute("meci") Meci meci, BindingResult bindingResult, Model model) {

        if (meciRepository.countDuplicateMatchSQL(meci.getEchipa1().getEchipaID(), meci.getEchipa2().getEchipaID(), meci.getData()) > 0) {
            bindingResult.rejectValue("data", "error.meci", "Aceste echipe au deja meci programat la această dată!");
        }
        if (meci.getEchipa1().getEchipaID().equals(meci.getEchipa2().getEchipaID())) {
            bindingResult.rejectValue("echipa2", "error.meci", "O echipă nu poate juca împotriva ei!");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("listaEchipe", echipaRepository.gasesteToateEchipeleSQL());
            model.addAttribute("listaGrupe", grupaRepository.gasesteToateGrupeleSQL());
            return "meciuri/adauga-meci";
        }
        calculeazaSiSeteazaCastigator(meci);

        meciRepository.save(meci);
        return "redirect:/meciuri";
    }

    @GetMapping("/sterge/{id}")
    public String sterge(@PathVariable("id") Integer id) {
        meciRepository.stergeMeciSQL(id);
        return "redirect:/meciuri";
    }

    @GetMapping("/editeaza/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Meci meci = meciRepository.findById(id).orElse(null);

        if (meci == null) {
            return "redirect:/meciuri";
        }

        model.addAttribute("meci", meci);
        model.addAttribute("listaEchipe", echipaRepository.gasesteToateEchipeleSQL());
        model.addAttribute("listaGrupe", grupaRepository.gasesteToateGrupeleSQL());

        List<Echipa> participante = new ArrayList<>();
        if (meci.getEchipa1() != null) participante.add(meci.getEchipa1());
        if (meci.getEchipa2() != null) participante.add(meci.getEchipa2());
        model.addAttribute("echipeParticipante", participante);

        return "meciuri/editeaza-meci";
    }

    @PostMapping("/actualizeaza/{id}")
    public String update(@PathVariable("id") Integer id,
                         @Valid @ModelAttribute("meci") Meci meciDinFormular,
                         BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            meciDinFormular.setMeciID(id);
            model.addAttribute("listaEchipe", echipaRepository.gasesteToateEchipeleSQL());
            model.addAttribute("listaGrupe", grupaRepository.gasesteToateGrupeleSQL());

            // Reconstituim listele pentru dropdown-uri
            List<Echipa> participante = new ArrayList<>();
            if (meciDinFormular.getEchipa1() != null) participante.add(meciDinFormular.getEchipa1());
            if (meciDinFormular.getEchipa2() != null) participante.add(meciDinFormular.getEchipa2());
            model.addAttribute("echipeParticipante", participante);

            return "meciuri/editeaza-meci";
        }

        // Verificăm duplicat doar dacă ambele echipe sunt selectate
        if (meciDinFormular.getEchipa1() != null && meciDinFormular.getEchipa2() != null) {

            if (meciRepository.countDuplicateMatchUpdateSQL(
                    meciDinFormular.getEchipa1().getEchipaID(),
                    meciDinFormular.getEchipa2().getEchipaID(),
                    meciDinFormular.getData(), id) > 0) {
                bindingResult.rejectValue("data", "error.meci", "Aceste echipe au deja meci programat la această dată!");
            }

            // Verificare echipa joaca cu ea insasi
            if (meciDinFormular.getEchipa1().getEchipaID().equals(meciDinFormular.getEchipa2().getEchipaID())) {
                bindingResult.rejectValue("echipa2", "error.meci", "O echipă nu poate juca împotriva ei!");
            }
        }

        if (bindingResult.hasErrors()) {
            meciDinFormular.setMeciID(id);
            model.addAttribute("listaEchipe", echipaRepository.gasesteToateEchipeleSQL());
            model.addAttribute("listaGrupe", grupaRepository.gasesteToateGrupeleSQL());
            List<Echipa> participante = new ArrayList<>();
            if (meciDinFormular.getEchipa1() != null) participante.add(meciDinFormular.getEchipa1());
            if (meciDinFormular.getEchipa2() != null) participante.add(meciDinFormular.getEchipa2());
            model.addAttribute("echipeParticipante", participante);

            return "meciuri/editeaza-meci";
        }

        Meci meciExistent = meciRepository.findById(id).orElse(null);

        if (meciExistent != null) {
            meciExistent.setEchipa1(meciDinFormular.getEchipa1());
            meciExistent.setEchipa2(meciDinFormular.getEchipa2());
            meciExistent.setGrupa(meciDinFormular.getGrupa());
            meciExistent.setData(meciDinFormular.getData());
            meciExistent.setLocatie(meciDinFormular.getLocatie());

            // Actualizare scoruri
            meciExistent.setScorEchipa1(meciDinFormular.getScorEchipa1());
            meciExistent.setScorEchipa2(meciDinFormular.getScorEchipa2());

            calculeazaSiSeteazaCastigator(meciExistent);

            meciRepository.save(meciExistent);
        }

        return "redirect:/meciuri";
    }

    private void calculeazaSiSeteazaCastigator(Meci meci) {
        if (meci.getScorEchipa1() != null && meci.getScorEchipa2() != null) {
            if (meci.getScorEchipa1() > meci.getScorEchipa2()) {
                meci.setCastigator(meci.getEchipa1());
            } else if (meci.getScorEchipa2() > meci.getScorEchipa1()) {
                meci.setCastigator(meci.getEchipa2());
            } else {
                meci.setCastigator(null); // Egalitate
            }
        } else {
            meci.setCastigator(null); // Scoruri necompletate
        }
    }
}