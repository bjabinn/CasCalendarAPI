package com.ajedrezsevilla.controllers;

import com.ajedrezsevilla.models.EventCalendar;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/calendar")
@EnableCaching
public class calendarController {

    @GetMapping()
    public ResponseEntity<EventCalendar> getAll(){
        List<EventCalendar> objetos = leerObjetosDesdeJSON();
        ResponseEntity response = new ResponseEntity<>(objetos, HttpStatus.OK);
        return response;

    }

    @PostMapping()
    public ResponseEntity<String> addEvent(@RequestBody EventCalendar nuevoEvento){
        List<EventCalendar> objetos = leerObjetosDesdeJSON();
        objetos.add(nuevoEvento);
        escribirObjetosEnJSON(objetos);
        ResponseEntity response = new ResponseEntity<>(nuevoEvento, HttpStatus.OK);
        return response;
    }

    private static List<EventCalendar> leerObjetosDesdeJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            File archivoJSON = new File("eventsCalendar.json");
            if (archivoJSON.exists()) {
                return objectMapper.readValue(archivoJSON, new TypeReference<>() {
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private  void escribirObjetosEnJSON(List<EventCalendar> objetos) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            objectMapper.writeValue(new File("eventsCalendar.json"), objetos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
