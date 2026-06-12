package com.saberpro.app.controller;

import com.saberpro.app.models.Coordinador;
import com.saberpro.app.models.Director;
import com.saberpro.app.models.Docente;
import com.saberpro.app.models.Facultad;
import com.saberpro.app.repository.CoordinadorRepository;
import com.saberpro.app.repository.DirectorRepository;
import com.saberpro.app.repository.DocenteRepository;
import com.saberpro.app.repository.FacultadRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CoordinadorRepository coordinadorRepository;
    private final FacultadRepository facultadRepository;
    private final DirectorRepository directorRepository;
    private final DocenteRepository docenteRepository;

    public AdminController(CoordinadorRepository coordinadorRepository,
                            FacultadRepository facultadRepository,
                            DirectorRepository directorRepository,
                            DocenteRepository docenteRepository) {
        this.coordinadorRepository = coordinadorRepository;
        this.facultadRepository = facultadRepository;
        this.directorRepository = directorRepository;
        this.docenteRepository = docenteRepository;
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
        try {
            model.addAttribute("facultades", facultadRepository.findAll());
        } catch (Exception e) {
            model.addAttribute("facultades", new ArrayList<>());
        }
        try {
            model.addAttribute("directores", directorRepository.findAll());
        } catch (Exception e) {
            model.addAttribute("directores", new ArrayList<>());
        }
        try {
            model.addAttribute("docentes", docenteRepository.findAll());
        } catch (Exception e) {
            model.addAttribute("docentes", new ArrayList<>());
        }
        return "admin";
    }

    // ---- CRUD Coordinadores ----

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

    // ---- CRUD Facultades ----

    @PostMapping("/facultad/crear")
    public String crearFacultad(@ModelAttribute Facultad facultad, HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("rol"))) return "redirect:/login";
        try {
            facultadRepository.save(facultad);
            model.addAttribute("exito", "Facultad creada correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear facultad: " + e.getMessage());
        }
        return panelAdmin(session, model);
    }

    @PostMapping("/facultad/eliminar/{id}")
    public String eliminarFacultad(@PathVariable Long id, HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("rol"))) return "redirect:/login";
        try {
            facultadRepository.deleteById(id);
        } catch (Exception e) {
            model.addAttribute("error", "No se puede eliminar: la facultad tiene registros asociados.");
        }
        return panelAdmin(session, model);
    }

    // ---- CRUD Directores ----

    @PostMapping("/director/crear")
    public String crearDirector(@ModelAttribute Director director,
                                 @RequestParam(required = false) Long facultadId,
                                 HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("rol"))) return "redirect:/login";
        try {
            if (facultadId != null) {
                facultadRepository.findById(facultadId).ifPresent(director::setFacultad);
            }
            directorRepository.save(director);
            model.addAttribute("exito", "Director creado correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear director: " + e.getMessage());
        }
        return panelAdmin(session, model);
    }

    @PostMapping("/director/desactivar/{id}")
    public String desactivarDirector(@PathVariable Long id, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("rol"))) return "redirect:/login";
        directorRepository.findById(id).ifPresent(d -> {
            d.setActivo(false);
            directorRepository.save(d);
        });
        return "redirect:/admin";
    }

    @PostMapping("/director/activar/{id}")
    public String activarDirector(@PathVariable Long id, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("rol"))) return "redirect:/login";
        directorRepository.findById(id).ifPresent(d -> {
            d.setActivo(true);
            directorRepository.save(d);
        });
        return "redirect:/admin";
    }

    // ---- CRUD Docentes ----

    @PostMapping("/docente/crear")
    public String crearDocente(@ModelAttribute Docente docente,
                                @RequestParam(required = false) Long facultadId,
                                HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("rol"))) return "redirect:/login";
        try {
            if (facultadId != null) {
                facultadRepository.findById(facultadId).ifPresent(docente::setFacultad);
            }
            docenteRepository.save(docente);
            model.addAttribute("exito", "Docente creado correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear docente: " + e.getMessage());
        }
        return panelAdmin(session, model);
    }

    @PostMapping("/docente/desactivar/{id}")
    public String desactivarDocente(@PathVariable Long id, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("rol"))) return "redirect:/login";
        docenteRepository.findById(id).ifPresent(d -> {
            d.setActivo(false);
            docenteRepository.save(d);
        });
        return "redirect:/admin";
    }

    @PostMapping("/docente/activar/{id}")
    public String activarDocente(@PathVariable Long id, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("rol"))) return "redirect:/login";
        docenteRepository.findById(id).ifPresent(d -> {
            d.setActivo(true);
            docenteRepository.save(d);
        });
        return "redirect:/admin";
    }

    @GetMapping("/resolucion-beneficios")
    public String resolucionBeneficios(HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("rol"))) return "redirect:/login";
        model.addAttribute("volver", "/admin");
        return "resolucion-beneficios";
    }
}
