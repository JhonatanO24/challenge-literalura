package com.literalura.challenge.repository;

import com.literalura.challenge.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long>{
    Optional<Autor> findByNombreContainingIgnoreCase(String nombre);

    List<Autor> findByAnoNacimientoLessThanEqualAndAnoFallecimientoGreaterThanEqual(Integer anoNacimiento, Integer anoFallecimiento);

    @Query("SELECT a FROM Autor a WHERE a.anoNacimiento <= :ano AND (a.anoFallecimiento >= :ano OR a.anoFallecimiento IS NULL)")
    List<Autor> autoresVivosEnAno(@Param("ano") Integer ano);

    List<Autor> findByAnoNacimientoGreaterThan(Integer ano);
}
