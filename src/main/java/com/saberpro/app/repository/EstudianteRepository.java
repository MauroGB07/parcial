package com.saberpro.app.repository;

import com.saberpro.app.models.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Estudiante findByIdentificacion(String identificacion);
    Optional<Estudiante> findByCorreo(String correo);
    long countByPuntajeGlobalIsNotNull();
    long countByPuntajeGlobalIsNull();

    @Query("SELECT e FROM Estudiante e WHERE e.puntajeGlobal IS NOT NULL AND e.puntajeGlobal >= 100")
    List<Estudiante> findEstudiantesConResultados();

    @Query("SELECT e FROM Estudiante e WHERE e.puntajeGlobal >= 241")
    List<Estudiante> findEstudiantesConBeneficios();

    List<Estudiante> findByPrograma(String programa);

    List<Estudiante> findByFacultad_Id(Long facultadId);

    List<Estudiante> findByIdentificacionContaining(String identificacion);
}
