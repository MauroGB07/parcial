package com.saberpro.app.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "estudiantes")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String identificacion;
    private String tipoDocumento;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String nombre; // nombre completo calculado o importado
    private String correo;
    private String telefono;
    private String numRegistro;
    private String programa;
    private String tipoPrograma;
    private Integer semestre;
    private String contrasena;
    private Boolean activo = true;

    private LocalDate fechaExamen;

    // Puntaje global y nivel
    private Double puntajeGlobal;
    private String nivelGlobal;

    // Competencias genéricas
    private Double comunicacionEscrita;
    private String comunicacionEscritaNivel;
    private Double razonamientoCuantitativo;
    private String razonamientoCuantitativoNivel;
    private Double lecturaCritica;
    private String lecturaCriticaNivel;
    private Double competenciasCiudadanas;
    private String competenciasCiudadanasNivel;
    private Double ingles;
    private String inglesNivelMcer;

    // Competencias específicas
    private Double formulacionProyectos;
    private String formulacionProyectosNivel;
    private Double pensamientoCientifico;
    private String pensamientoCientificoNivel;
    private Double disenoSoftware;
    private String disenoSoftwareNivel;

    private Boolean aprobadoSaberPro = false;
    private Boolean pagoCargado = false;

    @ManyToOne
    @JoinColumn(name = "facultad_id")
    private Facultad facultad;

    public Estudiante() {}

    // Nombre completo helpers
    public String getNombreCompleto() {
        if (primerNombre != null && primerApellido != null) {
            String n = primerNombre.trim();
            if (segundoNombre != null && !segundoNombre.isBlank()) n += " " + segundoNombre.trim();
            n += " " + primerApellido.trim();
            if (segundoApellido != null && !segundoApellido.isBlank()) n += " " + segundoApellido.trim();
            return n;
        }
        return nombre != null ? nombre : "";
    }

    // ---- Getters y Setters ----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }
    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }
    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getNumRegistro() { return numRegistro; }
    public void setNumRegistro(String numRegistro) { this.numRegistro = numRegistro; }
    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }
    public String getTipoPrograma() { return tipoPrograma; }
    public void setTipoPrograma(String tipoPrograma) { this.tipoPrograma = tipoPrograma; }
    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public LocalDate getFechaExamen() { return fechaExamen; }
    public void setFechaExamen(LocalDate fechaExamen) { this.fechaExamen = fechaExamen; }
    public Double getPuntajeGlobal() { return puntajeGlobal; }
    public void setPuntajeGlobal(Double puntajeGlobal) { this.puntajeGlobal = puntajeGlobal; }
    public String getNivelGlobal() { return nivelGlobal; }
    public void setNivelGlobal(String nivelGlobal) { this.nivelGlobal = nivelGlobal; }
    public Double getComunicacionEscrita() { return comunicacionEscrita; }
    public void setComunicacionEscrita(Double v) { this.comunicacionEscrita = v; }
    public String getComunicacionEscritaNivel() { return comunicacionEscritaNivel; }
    public void setComunicacionEscritaNivel(String v) { this.comunicacionEscritaNivel = v; }
    public Double getRazonamientoCuantitativo() { return razonamientoCuantitativo; }
    public void setRazonamientoCuantitativo(Double v) { this.razonamientoCuantitativo = v; }
    public String getRazonamientoCuantitativoNivel() { return razonamientoCuantitativoNivel; }
    public void setRazonamientoCuantitativoNivel(String v) { this.razonamientoCuantitativoNivel = v; }
    public Double getLecturaCritica() { return lecturaCritica; }
    public void setLecturaCritica(Double v) { this.lecturaCritica = v; }
    public String getLecturaCriticaNivel() { return lecturaCriticaNivel; }
    public void setLecturaCriticaNivel(String v) { this.lecturaCriticaNivel = v; }
    public Double getCompetenciasCiudadanas() { return competenciasCiudadanas; }
    public void setCompetenciasCiudadanas(Double v) { this.competenciasCiudadanas = v; }
    public String getCompetenciasCiudadanasNivel() { return competenciasCiudadanasNivel; }
    public void setCompetenciasCiudadanasNivel(String v) { this.competenciasCiudadanasNivel = v; }
    public Double getIngles() { return ingles; }
    public void setIngles(Double v) { this.ingles = v; }
    public String getInglesNivelMcer() { return inglesNivelMcer; }
    public void setInglesNivelMcer(String v) { this.inglesNivelMcer = v; }
    public Double getFormulacionProyectos() { return formulacionProyectos; }
    public void setFormulacionProyectos(Double v) { this.formulacionProyectos = v; }
    public String getFormulacionProyectosNivel() { return formulacionProyectosNivel; }
    public void setFormulacionProyectosNivel(String v) { this.formulacionProyectosNivel = v; }
    public Double getPensamientoCientifico() { return pensamientoCientifico; }
    public void setPensamientoCientifico(Double v) { this.pensamientoCientifico = v; }
    public String getPensamientoCientificoNivel() { return pensamientoCientificoNivel; }
    public void setPensamientoCientificoNivel(String v) { this.pensamientoCientificoNivel = v; }
    public Double getDisenoSoftware() { return disenoSoftware; }
    public void setDisenoSoftware(Double v) { this.disenoSoftware = v; }
    public String getDisenoSoftwareNivel() { return disenoSoftwareNivel; }
    public void setDisenoSoftwareNivel(String v) { this.disenoSoftwareNivel = v; }
    public Boolean getAprobadoSaberPro() { return aprobadoSaberPro; }
    public void setAprobadoSaberPro(Boolean v) { this.aprobadoSaberPro = v; }
    public Facultad getFacultad() { return facultad; }
    public void setFacultad(Facultad facultad) { this.facultad = facultad; }
    public Boolean getPagoCargado() { return pagoCargado; }
    public void setPagoCargado(Boolean pagoCargado) { this.pagoCargado = pagoCargado; }
}
