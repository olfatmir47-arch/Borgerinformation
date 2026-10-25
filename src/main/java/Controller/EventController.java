package Controller;

import DTO.Event.CreateEventDTO;
import DTO.Event.EventDTO;
import Service.EventService;
import entities.Event;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventController {
private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    public void createEvent(Context ctx) {
        CreateEventDTO dto = ctx.bodyAsClass(CreateEventDTO.class);
        Event event = eventService.createEvent(dto);
        EventDTO response = new EventDTO();
        response.setName(dto.getName());
        ctx.json(response);
    }

    public void getAllEventsByCategory(Context ctx){
        int categoryId = Integer.parseInt(ctx.pathParam("id"));
        List<Event> events = eventService.getAllEventsByCategory(categoryId);

        List<EventDTO> response = events.stream().map(EventDTO::new).toList();
       ctx.json(response);
    }

    public void GetAllEvents(Context ctx) {
        List<Event> events = eventService.findAll();

        List<EventDTO> response = events.stream().map(EventDTO::new).toList();
        ctx.json(response);
    }
}
