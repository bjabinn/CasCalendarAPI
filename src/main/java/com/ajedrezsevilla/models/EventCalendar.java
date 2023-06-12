package com.ajedrezsevilla.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

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
}
