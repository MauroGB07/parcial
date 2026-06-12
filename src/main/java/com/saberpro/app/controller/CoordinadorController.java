package com.saberpro.app.controller;

import com.saberpro.app.models.Estudiante;
import com.saberpro.app.repository.EstudianteRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/coordinador")
public class CoordinadorController {

    private final EstudianteRepository estudianteRepository;

    public CoordinadorController(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    private boolean checkSession(HttpSession session) {
        return "COORDINADOR".equals(session.getAttribute("rol"));
    }

    @GetMapping
    public String panelCoordinador(HttpSession session, Model model) {
        if (!checkSession(session)) return "redirect:/login";
        List<Estudiante> todos = estudianteRepository.findAll();
        long conResultados = estudianteRepository.countByPuntajeGlobalIsNotNull();
        long sinResultados = estudianteRepository.countByPuntajeGlobalIsNull();
        model.addAttribute("totalEstudiantes", todos.size());
        model.addAttribute("conResultados", conResultados);
        model.addAttribute("sinResultados", sinResultados);
        model.addAttribute("nombreCoordinador", session.getAttribute("nombre"));
        model.addAttribute("area", session.getAttribute("area"));
        return "coordinador";
    }

    // ---- CRUD Alumnos ----

    @GetMapping("/alumnos")
    public String gestionAlumnos(HttpSession session, Model model) {
        if (!checkSession(session)) return "redirect:/login";
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("nuevoEstudiante", new Estudiante());
        return "gestion-alumnos";
    }

    @PostMapping("/alumnos/crear")
    public String crearAlumno(@ModelAttribute Estudiante estudiante,
                               HttpSession session, Model model) {
        if (!checkSession(session)) return "redirect:/login";
        if (estudiante.getContrasena() == null || estudiante.getContrasena().isBlank()) {
            estudiante.setContrasena(estudiante.getIdentificacion());
        }
        try {
            estudianteRepository.save(estudiante);
        } catch (Exception e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("estudiantes", estudianteRepository.findAll());
            model.addAttribute("nuevoEstudiante", estudiante);
            return "gestion-alumnos";
        }
        return "redirect:/coordinador/alumnos";
    }

    @GetMapping("/alumnos/editar/{id}")
    public String formularioEditar(@PathVariable Long id, HttpSession session, Model model) {
        if (!checkSession(session)) return "redirect:/login";
        estudianteRepository.findById(id).ifPresent(e -> model.addAttribute("estudiante", e));
        return "editar-alumno";
    }

    @PostMapping("/alumnos/actualizar")
    public String actualizarAlumno(@ModelAttribute Estudiante estudiante, HttpSession session) {
        if (!checkSession(session)) return "redirect:/login";
        Estudiante existente = estudianteRepository.findById(estudiante.getId()).orElse(null);
        if (existente != null) {
            existente.setTipoDocumento(estudiante.getTipoDocumento());
            existente.setPrimerNombre(estudiante.getPrimerNombre());
            existente.setSegundoNombre(estudiante.getSegundoNombre());
            existente.setPrimerApellido(estudiante.getPrimerApellido());
            existente.setSegundoApellido(estudiante.getSegundoApellido());
            existente.setCorreo(estudiante.getCorreo());
            existente.setTelefono(estudiante.getTelefono());
            existente.setNumRegistro(estudiante.getNumRegistro());
            existente.setSemestre(estudiante.getSemestre());
            existente.setPrograma(estudiante.getPrograma());
            existente.setTipoPrograma(estudiante.getTipoPrograma());
            estudianteRepository.save(existente);
        }
        return "redirect:/coordinador/alumnos";
    }

    @PostMapping("/alumnos/calificar/{id}")
    public String calificarAlumno(@PathVariable Long id,
                                   @ModelAttribute Estudiante datos,
                                   HttpSession session) {
        if (!checkSession(session)) return "redirect:/login";
        Estudiante e = estudianteRepository.findById(id).orElse(null);
        if (e != null) {
            e.setPuntajeGlobal(datos.getPuntajeGlobal());
            e.setNivelGlobal(datos.getNivelGlobal());
            e.setComunicacionEscrita(datos.getComunicacionEscrita());
            e.setComunicacionEscritaNivel(datos.getComunicacionEscritaNivel());
            e.setRazonamientoCuantitativo(datos.getRazonamientoCuantitativo());
            e.setRazonamientoCuantitativoNivel(datos.getRazonamientoCuantitativoNivel());
            e.setLecturaCritica(datos.getLecturaCritica());
            e.setLecturaCriticaNivel(datos.getLecturaCriticaNivel());
            e.setCompetenciasCiudadanas(datos.getCompetenciasCiudadanas());
            e.setCompetenciasCiudadanasNivel(datos.getCompetenciasCiudadanasNivel());
            e.setIngles(datos.getIngles());
            e.setInglesNivelMcer(datos.getInglesNivelMcer());
            e.setFormulacionProyectos(datos.getFormulacionProyectos());
            e.setFormulacionProyectosNivel(datos.getFormulacionProyectosNivel());
            e.setPensamientoCientifico(datos.getPensamientoCientifico());
            e.setPensamientoCientificoNivel(datos.getPensamientoCientificoNivel());
            e.setDisenoSoftware(datos.getDisenoSoftware());
            e.setDisenoSoftwareNivel(datos.getDisenoSoftwareNivel());
            e.setFechaExamen(datos.getFechaExamen());
            e.setAprobadoSaberPro(true);
            estudianteRepository.save(e);
        }
        return "redirect:/coordinador/alumnos";
    }

    @PostMapping("/alumnos/desactivar/{id}")
    public String desactivarAlumno(@PathVariable Long id, HttpSession session) {
        if (!checkSession(session)) return "redirect:/login";
        estudianteRepository.findById(id).ifPresent(e -> {
            e.setActivo(false);
            estudianteRepository.save(e);
        });
        return "redirect:/coordinador/alumnos";
    }

    @PostMapping("/alumnos/activar/{id}")
    public String activarAlumno(@PathVariable Long id, HttpSession session) {
        if (!checkSession(session)) return "redirect:/login";
        estudianteRepository.findById(id).ifPresent(e -> {
            e.setActivo(true);
            estudianteRepository.save(e);
        });
        return "redirect:/coordinador/alumnos";
    }

    // ---- Informes ----

    @GetMapping("/informe-general")
    public String informeGeneral(HttpSession session, Model model) {
        if (!checkSession(session)) return "redirect:/login";
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("nombreCoordinador", session.getAttribute("nombre"));
        model.addAttribute("area", session.getAttribute("area"));
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
        return "informe-detallado";
    }

    @GetMapping("/informe-beneficios")
    public String informeBeneficios(HttpSession session, Model model) {
        if (!checkSession(session)) return "redirect:/login";
        model.addAttribute("estudiantes", estudianteRepository.findEstudiantesConBeneficios());
        return "informe-beneficios";
    }
}
