package com.saberpro.app.controller;

import com.saberpro.app.models.Estudiante;
import com.saberpro.app.repository.EstudianteRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    private final EstudianteRepository estudianteRepository;

    public EstudianteController(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    private Estudiante getEstudianteFromSession(HttpSession session) {
        if (!"ESTUDIANTE".equals(session.getAttribute("rol"))) return null;
        String id = (String) session.getAttribute("identificacion");
        return estudianteRepository.findByIdentificacion(id);
    }

    @GetMapping("/portal")
    public String portal(HttpSession session, Model model) {
        Estudiante e = getEstudianteFromSession(session);
        if (e == null) return "redirect:/login";
        model.addAttribute("estudiante", e);
        // Beneficio: puntaje >= 241
        if (e.getPuntajeGlobal() != null && e.getPuntajeGlobal() >= 241) {
            model.addAttribute("tieneBeneficio", true);
        }
        return "estudiante-portal";
    }

    @GetMapping("/datos-personales")
    public String datosPersonales(HttpSession session, Model model) {
        Estudiante e = getEstudianteFromSession(session);
        if (e == null) return "redirect:/login";
        model.addAttribute("estudiante", e);
        return "estudiante-datos";
    }

    @GetMapping("/ultimo-resultado")
    public String ultimoResultado(HttpSession session, Model model) {
        Estudiante e = getEstudianteFromSession(session);
        if (e == null) return "redirect:/login";
        model.addAttribute("estudiante", e);
        return "estudiante-resultado";
    }

    @GetMapping("/todos-resultados")
    public String todosResultados(HttpSession session, Model model) {
        Estudiante e = getEstudianteFromSession(session);
        if (e == null) return "redirect:/login";
        model.addAttribute("estudiante", e);
        return "estudiante-todos-resultados";
    }

    @GetMapping("/beneficios")
    public String beneficios(HttpSession session, Model model) {
        Estudiante e = getEstudianteFromSession(session);
        if (e == null) return "redirect:/login";
        model.addAttribute("estudiante", e);
        boolean tieneBeneficio = e.getPuntajeGlobal() != null && e.getPuntajeGlobal() >= 241;
        model.addAttribute("tieneBeneficio", tieneBeneficio);
        return "estudiante-beneficios";
    }

    @GetMapping("/pago")
    public String pago(HttpSession session, Model model) {
        Estudiante e = getEstudianteFromSession(session);
        if (e == null) return "redirect:/login";
        model.addAttribute("estudiante", e);
        return "estudiante-pago";
    }

    @PostMapping("/pago/cargar")
    public String cargarPago(@RequestParam String referenciaPago, HttpSession session, Model model) {
        Estudiante e = getEstudianteFromSession(session);
        if (e == null) return "redirect:/login";
        e.setPagoCargado(true);
        estudianteRepository.save(e);
        model.addAttribute("estudiante", e);
        model.addAttribute("exito", "¡Comprobante de pago registrado correctamente! Referencia: " + referenciaPago);
        return "estudiante-pago";
    }

    @GetMapping("/resolucion-beneficios")
    public String resolucionBeneficios(HttpSession session, Model model) {
        if (getEstudianteFromSession(session) == null) return "redirect:/login";
        model.addAttribute("volver", "/estudiante/portal");
        return "resolucion-beneficios";
    }
}
