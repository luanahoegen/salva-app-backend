package com.salva.api.controller;

import com.salva.api.model.Event;
import com.salva.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin("*")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@RequestBody Event event) {
        try {
            System.out.println("-> Recebendo POST para eventos: " + event.getName());
            String result = eventService.save(event);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Isso VAI mostrar o erro no terminal do PC agora!
            return new ResponseEntity<>("Erro no servidor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllEvents() {
        try {
            List<Event> events = eventService.findAll();
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
