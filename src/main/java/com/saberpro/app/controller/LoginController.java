package com.saberpro.app.controller;

import com.saberpro.app.models.Admin;
import com.saberpro.app.models.Coordinador;
import com.saberpro.app.models.Estudiante;
import com.saberpro.app.repository.AdminRepository;
import com.saberpro.app.repository.CoordinadorRepository;
import com.saberpro.app.repository.EstudianteRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    private final AdminRepository adminRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final EstudianteRepository estudianteRepository;

    public LoginController(AdminRepository adminRepository,
                           CoordinadorRepository coordinadorRepository,
                           EstudianteRepository estudianteRepository) {
        this.adminRepository = adminRepository;
        this.coordinadorRepository = coordinadorRepository;
        this.estudianteRepository = estudianteRepository;
    }

    @GetMapping({"/", "/login"})
    public String login(HttpSession session) {
        if (session.getAttribute("rol") != null) {
            String rol = (String) session.getAttribute("rol");
            return switch (rol) {
                case "ADMIN" -> "redirect:/admin";
                case "COORDINADOR" -> "redirect:/coordinador";
                case "ESTUDIANTE" -> "redirect:/estudiante/portal";
                default -> "login";
            };
        }
        return "login";
    }

    @PostMapping("/validar")
    public String procesarLogin(@RequestParam String usuario,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {

        // Buscar Admin
        Optional<Admin> adminOpt = adminRepository.findByCorreo(usuario);
        if (adminOpt.isPresent() && adminOpt.get().getContrasena().equals(password)) {
            session.setAttribute("rol", "ADMIN");
            session.setAttribute("nombre", adminOpt.get().getNombre());
            session.setAttribute("correo", adminOpt.get().getCorreo());
            return "redirect:/admin";
        }

        // Buscar Coordinador
        Optional<Coordinador> coordOpt = coordinadorRepository.findByCorreo(usuario);
        if (coordOpt.isPresent() && coordOpt.get().getContrasena().equals(password)) {
            Coordinador coord = coordOpt.get();
            if (!Boolean.TRUE.equals(coord.getActivo())) {
                model.addAttribute("error", "Tu cuenta está desactivada.");
                return "login";
            }
            session.setAttribute("rol", "COORDINADOR");
            session.setAttribute("nombre", coord.getNombreCompleto());
            session.setAttribute("correo", coord.getCorreo());
            session.setAttribute("area", coord.getAreaAsignada());
            session.setAttribute("coordinadorId", coord.getId());
            return "redirect:/coordinador";
        }

        // Buscar Estudiante por correo o identificación
        Estudiante est = null;
        Optional<Estudiante> estOpt = estudianteRepository.findByCorreo(usuario);
        if (estOpt.isPresent()) {
            est = estOpt.get();
        } else {
            est = estudianteRepository.findByIdentificacion(usuario);
        }
        if (est != null && est.getIdentificacion().equals(password)) {
            if (!Boolean.TRUE.equals(est.getActivo())) {
                model.addAttribute("error", "Tu cuenta está desactivada.");
                return "login";
            }
            session.setAttribute("rol", "ESTUDIANTE");
            session.setAttribute("identificacion", est.getIdentificacion());
            session.setAttribute("nombre", est.getNombreCompleto());
            return "redirect:/estudiante/portal";
        }

        model.addAttribute("error", "Usuario o contraseña incorrectos");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
