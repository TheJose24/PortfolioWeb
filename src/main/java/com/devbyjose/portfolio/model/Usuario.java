package com.devbyjose.portfolio.model;

import lombok.Data;

@Data
public class Usuario {
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String mensaje;

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

}
