package Service;

import DAOs.*;
import DTO.Address.CoordinatesDTO;
import DTO.Event.CreateEventDTO;
import entities.*;

import java.util.List;

public class EventService {
    private EventDAOImpl eventDAO;
    private CityDAO cityDAO;
    private AddressDAO addressDAO;
    private GeocodingService geocodingService;
    private CategoryDAO categoryDAO;

    public EventService(EventDAOImpl eventDAO, CityDAO cityDAO, AddressDAO addressDAO, GeocodingService geocodingService, CategoryDAO categoryDAO) {
        this.eventDAO = eventDAO;
        this.cityDAO = cityDAO;
        this.addressDAO = addressDAO;
        this.geocodingService = geocodingService;
        this.categoryDAO = categoryDAO;

    }

    public Event createEvent(CreateEventDTO dto) {

        dto.validate();

        City city = cityDAO.findByPostalCode(dto.getAddress().getPostalCode());
        if (city == null) {
            City newCity = new City();

            newCity.setPostalCode(dto.getAddress().getPostalCode());
            newCity.setCityName(dto.getAddress().getCityName());
            cityDAO.createCity(newCity);
            city = newCity;
        }

        Address address = addressDAO.findAddress(
                dto.getAddress().getStreetName(),
                dto.getAddress().getNumber(),
                dto.getAddress().getFloor(),
                city
        );

        CoordinatesDTO coords = null;
        if (address == null) {
            coords = geocodingService.geocode(
                    dto.getAddress().getStreetName(),
                    dto.getAddress().getNumber(),
                    city.getPostalCode(),
                    city.getCityName()
            );

            address = new Address();
            address.setStreetName(dto.getAddress().getStreetName());
            address.setNumber(dto.getAddress().getNumber());
            address.setFloor(dto.getAddress().getFloor());
            address.setLat(coords.getLat());
            address.setLng(coords.getLng());
            address.setCity(city);

            addressDAO.createAddress(address);

        }
        Category category = categoryDAO.findById(dto.getCategoryId());

        Event event = new Event();
        event.setNameOfOccurrence(dto.getName());
        event.setDescription(dto.getDescription());
        event.setCategory(category);
        event.setStartTime(dto.getStartTime());
        event.setEndTime(dto.getEndTime());
        event.setAddress(address);

        Event result = eventDAO.create(event);

        return result;
    }

        public Event findUserById ( int id){
            return eventDAO.findById(id);
        }

        public List<Event> findAll () {
            return eventDAO.findAll();
        }

        public Event updateUser (Event event){
            return eventDAO.update(event);
        }

        public Event delete ( int id){
            eventDAO.delete(id);
            return null;
        }

    public List<Event> getAllEventsByCategory(int categoryId) {
        return eventDAO.findByCategory(categoryId);
    }

}
