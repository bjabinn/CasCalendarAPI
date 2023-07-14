package com.ajedrezsevilla.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class EventCalendar {
    long id;
    String nombreTorneo;
    String sistemaDeJuego;
    String precio;
    String basesURL;
    String notas;

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime comienzo;

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime fin;

    String location;
    boolean isVisible;

    long calendarId;  //esto sirve para almacenar las provinciaId

    List<String> categorias;
}
