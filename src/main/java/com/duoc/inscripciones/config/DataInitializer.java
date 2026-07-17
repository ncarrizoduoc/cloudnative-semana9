package com.duoc.inscripciones.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.duoc.inscripciones.model.Curso;
import com.duoc.inscripciones.model.Estudiante;
import com.duoc.inscripciones.repository.CursoRepository;
import com.duoc.inscripciones.repository.EstudianteRepository;

@Component
public class DataInitializer implements ApplicationRunner{
    @Autowired
    private CursoRepository cursoRepo;

    @Autowired
    private EstudianteRepository estudianteRepo;

    @Override
    public void run(ApplicationArguments args) throws Exception{
        if (cursoRepo.findAll().isEmpty()){
            //Crear cursos en la base de datos
            Curso curso1 = Curso.builder()
                .id(null)
                .nombre("Introduccion a la programacion")
                .instructor("Profesor Pedro")
                .duracion(5)
                .costo(250000)
                .build();

            Curso curso2 = Curso.builder()
                .id(null)
                .nombre("Etica")
                .instructor("Profesor Juan")
                .duracion(3)
                .costo(120000)
                .build();
            
            Curso curso3 = Curso.builder()
                .id(null)
                .nombre("Biologia Celular")
                .instructor("Profesor Diego")
                .duracion(10)
                .costo(475000)
                .build();
            
            Curso curso4 = Curso.builder()
                .id(null)
                .nombre("CURSO FALLIDO")
                .instructor("Profesor Lorenzo")
                .duracion(1)
                .costo(10000000)
                .build();
            
            cursoRepo.saveAll(List.of(curso1, curso2, curso3, curso4));
        }

        if(estudianteRepo.findAll().isEmpty()){
            Estudiante est1 = Estudiante.builder()
                .id(null)
                .nombre("Jane Doe")
                .carrera("Ingenieria")
                .build();
            
            Estudiante est2 = Estudiante.builder()
                .id(null)
                .nombre("Juan Perez")
                .carrera("Medicina")
                .build();

            Estudiante est3 = Estudiante.builder()
                .id(null)
                .nombre("Maria Jimenez")
                .carrera("Derecho")
                .build();
            
            estudianteRepo.saveAll(List.of(est1, est2, est3));
        }
        
    }


}
