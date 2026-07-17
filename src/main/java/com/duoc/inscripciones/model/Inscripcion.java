package com.duoc.inscripciones.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "inscripcion")
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    private List<Curso> cursos;
    
    private Integer total;

    @Override
    public String toString(){
        String string = "";
        string += "ID de inscripcion: " + id + "\n";
        string += "Estudiante:\n";
        string += "\tID: "+ estudiante.getId() + "\n"
                + "\tNombre: " + estudiante.getNombre() + "\n"
                + "\tCarrera: " + estudiante.getCarrera() + "\n";
        string += "Cursos:\n";
        for (Curso curso:cursos){
            string += "\tID: " + curso.getId() + "\n";
            string += "\tNombre: " + curso.getNombre() + "\n";
            string += "\tInstructor: " + curso.getInstructor() + "\n";
            string += "\tDuracion: " + curso.getDuracion() + "\n";
            string += "\tCosto: $" + curso.getCosto() + "\n";
            string += "\n"; // Salto de linea al final de cada curso
        }
        string += "Total: $" + total;

        return string;
    }

}
