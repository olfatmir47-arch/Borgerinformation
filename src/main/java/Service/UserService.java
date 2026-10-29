package Service;

import DAOs.AddressDAO;
import DAOs.CityDAO;
import DAOs.UserDAOImpl;
import DTO.Address.CoordinatesDTO;
import DTO.Security.JwtUserDTO;
import DTO.User.CreateUserDTO;
import DTO.User.UserDTO;
import app.exceptions.ApiException;
import entities.Address;
import entities.City;
import entities.Role;
import entities.User;

import java.util.List;

public class UserService {

    private UserDAOImpl userDAO;
    private CityDAO cityDAO;
    private AddressDAO addressDAO;
    private GeocodingService geocodingService;

    public UserService(UserDAOImpl userDAO, CityDAO cityDAO, AddressDAO addressDAO, GeocodingService geocodingService){
        this.userDAO = userDAO;
        this.cityDAO = cityDAO;
        this.addressDAO = addressDAO;
        this.geocodingService = geocodingService;
    }

    public User createUser(CreateUserDTO dto) {

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

        User existing = userDAO.findByEmail(dto.getEmail());
        if (existing != null) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDateOfBirth(dto.getBirthDate());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setAddress(address);
        user.setRole(Role.USER);


        User result = userDAO.createUser(user);

        return result;
    }

    public User findUserById(int id){
        return userDAO.findById(id);
    }

    public List<UserDTO> getAllUsers(){
        List<User>users = userDAO.findAllUsers();
        return users.stream().map(UserDTO::new).toList();
    }

    public User updateUser(int id, CreateUserDTO dto){
        User user = userDAO.findById(id);
        if (user == null) {
            throw new ApiException(404, "User not found");
        }
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setDateOfBirth(dto.getBirthDate());

        Address address = user.getAddress();

        if(address == null) {
            address = new Address();
        }
        address.setStreetName(dto.getAddress().getStreetName());
        address.setNumber(dto.getAddress().getNumber());
        address.setFloor(dto.getAddress().getFloor());
        user.setAddress(address);

        City city = address.getCity();
        if(city == null) {
            city = new City();
        }

        city.setCityName(dto.getAddress().getCityName());
        city.setPostalCode(dto.getAddress().getPostalCode());
        address.setCity(city);

        return userDAO.update(user);
    }

    public void delete(int id){
        User user = userDAO.findById(id);
        if(user == null) {
            throw new ApiException(404, "User not found");
        }
        userDAO.delete(id);
    }

}

