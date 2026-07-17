package com.duoc.inscripciones.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.duoc.inscripciones.dto.InscripcionRequest;
import com.duoc.inscripciones.exception.ResourceNotFoundException;
import com.duoc.inscripciones.model.Curso;
import com.duoc.inscripciones.model.Estudiante;
import com.duoc.inscripciones.model.Inscripcion;
import com.duoc.inscripciones.repository.CursoRepository;
import com.duoc.inscripciones.repository.EstudianteRepository;

@Component
public class InscripcionRequestMapper {

    @Autowired
    private EstudianteRepository estudianteRepo;

    @Autowired
    private CursoRepository cursoRepo;

    public Inscripcion toInscripcion(InscripcionRequest request){
        //Guardar estudiante en inscripcion
        Long estudianteId = request.getEstudianteId();
        Estudiante estudiante = estudianteRepo.findById(estudianteId)
            .orElseThrow(() -> new ResourceNotFoundException("No existe el estudiante con ID: " + estudianteId));

        // Eliminar cursos duplicados en el request
        List<Long> cursosId = Arrays.stream(request.getCursos()).distinct().toList();

        //Guardar cursos
        List<Curso> cursos = new ArrayList<>();
        Integer total = 0;
        for(Long cursoId : cursosId){
            Curso curso = cursoRepo.findById(cursoId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el curso con ID: " + cursoId));
            cursos.add(curso);
            total += curso.getCosto(); // Se suma el valor del curso al total de la inscripcion
        }

        // Se crea el objeto Inscripcion
        Inscripcion inscripcion = Inscripcion.builder()
            .id(request.getId())
            .estudiante(estudiante)
            .cursos(cursos)
            .total(total)
            .build();

        return inscripcion;

    }

}
