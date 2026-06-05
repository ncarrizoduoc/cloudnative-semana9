package com.duoc.guiasdespacho.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Transportista implements Serializable{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @NotNull(message="Debe ingresar un nombre de transportista")
    @NotBlank(message="El nombre del transportista no puede ser un texto vacio")
    @Size(min = 3, max = 100, message = "El nombre del transportista debe tener entre 3 y 100 caracteres")
    private String nombre;

}
