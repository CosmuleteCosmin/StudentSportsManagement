/**
 * Controller principal care gestioneaza pagina de start, meniul si navigarea in aplicatie
 * @author Cosmulete Ion-Cosmin
 * @version 05 Ianuarie 2026
 */

package com.example.evidentaechipelorsportivedestudenti.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String afiseazaMeniuPrincipal() {
        return "index";
    }
}