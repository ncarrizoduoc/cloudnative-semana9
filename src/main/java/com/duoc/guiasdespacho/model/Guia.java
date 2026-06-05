package com.duoc.guiasdespacho.model;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.duoc.guiasdespacho.config.CONSTANTS.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Guia implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Debe ingresar el contenido del despacho")
    @NotBlank(message = "El contenido del despacho no puede ser un texto vacio")
    @Size(min = 3, max = 200, message = "El contenido del despacho debe tener entre 3 y 200 caracteres")
    private String contenido;

    @NotNull(message = "Debe ingresar la direccion de despacho")
    @NotBlank(message = "La direccion de despacho no puede ser un texto vacio")
    @Size(min = 3, max = 200, message = "La direccion de despacho debe tener entre 3 y 200 caracteres")
    private String direccion;

    @NotNull(message = "Debe ingresar la fecha del despacho")
    @DateTimeFormat(pattern = DATEFORMAT)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "transportista_id", nullable = false)
    private Transportista transportista;
}
