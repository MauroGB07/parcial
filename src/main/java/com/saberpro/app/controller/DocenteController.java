package com.saberpro.app.controller;

import com.saberpro.app.models.Docente;
import com.saberpro.app.models.Estudiante;
import com.saberpro.app.repository.DocenteRepository;
import com.saberpro.app.repository.EstudianteRepository;
import com.saberpro.app.repository.FacultadRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    private final DocenteRepository docenteRepository;
    private final EstudianteRepository estudianteRepository;
    private final FacultadRepository facultadRepository;

    public DocenteController(DocenteRepository docenteRepository,
                              EstudianteRepository estudianteRepository,
                              FacultadRepository facultadRepository) {
        this.docenteRepository = docenteRepository;
        this.estudianteRepository = estudianteRepository;
        this.facultadRepository = facultadRepository;
    }

    private boolean checkSession(HttpSession session) {
        return "DOCENTE".equals(session.getAttribute("rol"));
    }

    @GetMapping
    public String panelDocente(HttpSession session, Model model) {
        if (!checkSession(session)) return "redirect:/login";
        List<Estudiante> todos = estudianteRepository.findAll();
        long conResultados = estudianteRepository.countByPuntajeGlobalIsNotNull();
        long sinResultados = estudianteRepository.countByPuntajeGlobalIsNull();
        model.addAttribute("totalEstudiantes", todos.size());
        model.addAttribute("conResultados", conResultados);
        model.addAttribute("sinResultados", sinResultados);
        model.addAttribute("nombreDocente", session.getAttribute("nombre"));
        model.addAttribute("facultad", session.getAttribute("facultad"));
        return "docente";
    }

    // ---- Por Facultad / Por Cédula ----

    @GetMapping("/alumnos")
    public String alumnos(@RequestParam(required = false) Long facultadId,
                           @RequestParam(required = false) String cedula,
                           HttpSession session, Model model) {
        if (!checkSession(session)) return "redirect:/login";

        List<Estudiante> lista;
        if (cedula != null && !cedula.isBlank()) {
            lista = estudianteRepository.findByIdentificacionContaining(cedula.trim());
        } else if (facultadId != null) {
            lista = estudianteRepository.findByFacultad_Id(facultadId);
        } else {
            lista = estudianteRepository.findAll();
        }

        try {
            model.addAttribute("facultades", facultadRepository.findAll());
        } catch (Exception e) {
            model.addAttribute("facultades", new ArrayList<>());
        }
        model.addAttribute("estudiantes", lista);
        model.addAttribute("facultadSeleccionada", facultadId);
        model.addAttribute("cedulaBuscada", cedula);
        return "docente-alumnos";
    }

    // ---- Informes (reutiliza las plantillas del coordinador) ----

    @GetMapping("/informe-general")
    public String informeGeneral(HttpSession session, Model model) {
        if (!checkSession(session)) return "redirect:/login";
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("nombreCoordinador", session.getAttribute("nombre"));
        model.addAttribute("area", session.getAttribute("facultad"));
        model.addAttribute("rolLabel", "Docente");
        model.addAttribute("volver", "/docente");
        return "informe-general";
    }

    @GetMapping("/informe-detallado")
    public String informeDetallado(@RequestParam(required = false) String programa,
                                    HttpSession session, Model model) {
        if (!checkSession(session)) return "redirect:/login";
        List<Estudiante> lista;
        if (programa != null && !programa.isBlank()) {
            lista = estudianteRepository.findByPrograma(programa);
        } else {
            lista = estudianteRepository.findEstudiantesConResultados();
        }
        List<String> programas = estudianteRepository.findAll().stream()
                .map(Estudiante::getPrograma).filter(p -> p != null && !p.isBlank()).distinct().sorted().toList();
        model.addAttribute("estudiantes", lista);
        model.addAttribute("programas", programas);
        model.addAttribute("programaSeleccionado", programa);
        model.addAttribute("baseUrl", "/docente/informe-detallado");
        model.addAttribute("volver", "/docente");
        return "informe-detallado";
    }

    @GetMapping("/informe-beneficios")
    public String informeBeneficios(HttpSession session, Model model) {
        if (!checkSession(session)) return "redirect:/login";
        model.addAttribute("estudiantes", estudianteRepository.findEstudiantesConBeneficios());
        model.addAttribute("volver", "/docente");
        return "informe-beneficios";
    }

    @GetMapping("/resolucion-beneficios")
    public String resolucionBeneficios(HttpSession session, Model model) {
        if (!checkSession(session)) return "redirect:/login";
        model.addAttribute("volver", "/docente");
        return "resolucion-beneficios";
    }
}
