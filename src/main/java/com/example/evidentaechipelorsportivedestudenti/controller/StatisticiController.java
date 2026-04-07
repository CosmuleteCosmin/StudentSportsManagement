/**
 * Controller dedicat generarii de rapoarte si statistici complexe
 * @author Cosmulete Ion-Cosmin
 * @version 10 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.controller;

import com.example.evidentaechipelorsportivedestudenti.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StatisticiController {

    @Autowired private FacultateRepository facultateRepository;
    @Autowired private SportRepository sportRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private EchipaRepository echipaRepository;
    @Autowired private MeciRepository meciRepository;

    @GetMapping("/statistici")
    public String afiseazaStatistici(Model model,
                                     @RequestParam(value = "minStudenti", required = false, defaultValue = "0") Integer minStudenti) {
        model.addAttribute("facultatiFaraEchipe", facultateRepository.gasesteFacultatiFaraEchipeSQL());
        model.addAttribute("sporturiFaraEchipe", sportRepository.gasesteSporturiFaraEchipeSQL());
        model.addAttribute("studentiFaraEchipa", studentRepository.gasesteStudentiFaraEchipaSQL());
        model.addAttribute("echipeActive", echipaRepository.gasesteEchipeCuMeciuriSQL());

        model.addAttribute("totalStudenti", studentRepository.count());
        model.addAttribute("totalEchipe", echipaRepository.count());
        model.addAttribute("totalMeciuri", meciRepository.count());
        model.addAttribute("totalSporturi", sportRepository.count());

        model.addAttribute("meciuriViitoare", meciRepository.gasesteMeciuriViitoareSQL());

        model.addAttribute("facultatiFiltrate", facultateRepository.filtreazaFacultatiDupaNrStudentiSQL(minStudenti));
        model.addAttribute("valoareFiltru", minStudenti);

        model.addAttribute("clasament", meciRepository.getClasamentGeneral());
        model.addAttribute("jucatoriPerSport", sportRepository.getJucatoriPerSport());

        return "statistici";
    }
}