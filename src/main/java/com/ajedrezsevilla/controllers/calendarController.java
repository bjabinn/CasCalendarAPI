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
import java.util.OptionalLong;

@RestController
@Log4j2
@RequestMapping("/calendar")
@EnableCaching
@CrossOrigin(origins = "*")
public class calendarController {

    @GetMapping()

    public ResponseEntity<EventCalendar> getAll(){
        List<EventCalendar> objetos = leerObjetosDesdeJSON();
        ResponseEntity response = new ResponseEntity<>(objetos, HttpStatus.OK);
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventCalendar> getById(@PathVariable("id") long id){
        List<EventCalendar> objetos = leerObjetosDesdeJSON();
        EventCalendar resultado = objetos.stream()
                .filter(objeto -> objeto.getId() == id).findFirst().orElse(new EventCalendar());
        ResponseEntity response = new ResponseEntity<>(resultado, HttpStatus.OK);
        return response;
    }

    @PostMapping()
    public ResponseEntity<String> addEvent(@RequestBody EventCalendar nuevoEvento){
        List<EventCalendar> objetos = leerObjetosDesdeJSON();
        Long id = objetos.stream().mapToLong(EventCalendar::getId).max().orElse(0);
        ResponseEntity response;
        if (id != 0){
            nuevoEvento.setId(id+1);
            objetos.add(nuevoEvento);
            escribirObjetosEnJSON(objetos);
            response = new ResponseEntity<>(nuevoEvento, HttpStatus.OK);
        }else{
            response = new ResponseEntity("Error creando evento", HttpStatus.OK);
        }


        return response;
    }

    @PostMapping("/group")
    public ResponseEntity<String> addEvents(@RequestBody List<EventCalendar> eventos){
        List<EventCalendar> listaEventos = leerObjetosDesdeJSON();
        Long idMax = listaEventos.stream().mapToLong(EventCalendar::getId).max().orElse(0);

        for (int i=0; i < eventos.size(); i++){
            EventCalendar evento = eventos.get(i);
            idMax++;
            evento.setId(idMax);
            listaEventos.add(evento);
        }
        escribirObjetosEnJSON(listaEventos);
        ResponseEntity response = new ResponseEntity<>(eventos, HttpStatus.OK);
        return response;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateEvent(@RequestBody EventCalendar evento, @PathVariable("id") long id){
        List<EventCalendar> objetos = leerObjetosDesdeJSON();
        EventCalendar resultado = objetos.stream()
                .filter(objeto -> objeto.getId() == id).findFirst().orElse(null);
        if (resultado != null){
            //resultado = objectMapper.readValue(objectMapper.writeValueAsString(evento), EventCalendar.class);
            resultado.setNombreTorneo(evento.getNombreTorneo());
            resultado.setSistemaDeJuego(evento.getSistemaDeJuego());
            resultado.setPrecio(evento.getPrecio());
            resultado.setBasesURL(evento.getBasesURL());
            resultado.setNotas(evento.getNotas());
            resultado.setComienzo(evento.getComienzo());
            resultado.setFin(evento.getFin());
            resultado.setLocation(evento.getLocation());
            resultado.setVisible(evento.isVisible());
        }
        escribirObjetosEnJSON(objetos);
        ResponseEntity response = new ResponseEntity<>(resultado, HttpStatus.OK);
        return response;
    }

    @DeleteMapping()
    public ResponseEntity<EventCalendar> emptyJson(@RequestBody String cadena){
        ResponseEntity response = null;
        if (cadena.equals("emptyJson")){
            List<EventCalendar> listaObjetosVacia = new ArrayList<EventCalendar>();
            escribirObjetosEnJSON(listaObjetosVacia);
            response = new ResponseEntity<>("TODOS LOS OBJETOS BORRADOS", HttpStatus.OK);
        }else{
            response = new ResponseEntity<>("METODO DE SEGURIDAD ERRONEO", HttpStatus.OK);
        }
        return response;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<EventCalendar> deleteById(@PathVariable("id") long id){
        List<EventCalendar> objetos = leerObjetosDesdeJSON();
        EventCalendar resultado = objetos.stream()
                .filter(objeto -> objeto.getId() == id).findFirst().orElse(null);
        ResponseEntity response = null;
        if (resultado != null) {
            objetos.removeIf(objeto -> objeto.getId() == id);
            escribirObjetosEnJSON(objetos);
            response = new ResponseEntity<>(resultado, HttpStatus.OK);
        }else{
            response = new ResponseEntity<>("Id not found", HttpStatus.NOT_FOUND);
        }
        return response;

    }






    //--------------------
    //---------METODOS PRIVADOS
    //---------------------
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
