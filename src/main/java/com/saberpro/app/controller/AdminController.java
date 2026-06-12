package com.saberpro.app.controller;

import com.saberpro.app.models.Coordinador;
import com.saberpro.app.repository.CoordinadorRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CoordinadorRepository coordinadorRepository;

    public AdminController(CoordinadorRepository coordinadorRepository) {
        this.coordinadorRepository = coordinadorRepository;
    }

    @GetMapping
    public String panelAdmin(HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("rol"))) return "redirect:/login";
        model.addAttribute("nombreAdmin", session.getAttribute("nombre"));
        try {
            model.addAttribute("coordinadores", coordinadorRepository.findAll());
        } catch (Exception e) {
            model.addAttribute("coordinadores", new ArrayList<>());
        }
        return "admin";
    }

    @PostMapping("/coordinador/crear")
    public String crearCoordinador(@ModelAttribute Coordinador coordinador,
                                   HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("rol"))) return "redirect:/login";
        try {
            coordinadorRepository.save(coordinador);
            model.addAttribute("exito", "Coordinador creado correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear coordinador: " + e.getMessage());
        }
        return panelAdmin(session, model);
    }

    @PostMapping("/coordinador/desactivar/{id}")
    public String desactivarCoordinador(@PathVariable Long id, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("rol"))) return "redirect:/login";
        coordinadorRepository.findById(id).ifPresent(c -> {
            c.setActivo(false);
            coordinadorRepository.save(c);
        });
        return "redirect:/admin";
    }

    @PostMapping("/coordinador/activar/{id}")
    public String activarCoordinador(@PathVariable Long id, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("rol"))) return "redirect:/login";
        coordinadorRepository.findById(id).ifPresent(c -> {
            c.setActivo(true);
            coordinadorRepository.save(c);
        });
        return "redirect:/admin";
    }
}
