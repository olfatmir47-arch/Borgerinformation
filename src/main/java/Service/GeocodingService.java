package Service;

import DTO.Address.AddressDTO;
import DTO.Address.CoordinatesDTO;
import app.config.APIReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GeocodingService {

   private String apiKey = System.getenv("APIKey");
   private APIReader apiReader = new APIReader();
   private ObjectMapper mapper = new ObjectMapper();

   public CoordinatesDTO geocode(String streetName, String number, String postalCode, String cityName){
       try{

           String query = streetName + " " + number + ", " + postalCode + " " + cityName;
           String url = "https://geocode.search.hereapi.com/v1/geocode?q=" + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&apikey=" + apiKey;
           String json = apiReader.readAPI(url);

           JsonNode root = mapper.readTree(json);
           JsonNode item = root.get("items").get(0);
           JsonNode pos = item.get("position");

           double lat = pos.get("lat").asDouble();
           double lng = pos.get("lng").asDouble();

           return new CoordinatesDTO(lat, lng);

       }  catch (JsonProcessingException e) {
           throw new RuntimeException(e.getMessage());
       }
   }

   public AddressDTO reverseGeocode(double lat, double lng){
       try {
           String url = "https://revgeocode.search.hereapi.com/v1/revgeocode?at=" + lat + "," + lng + "&apikey=" + apiKey;
           String json = apiReader.readAPI(url);


           JsonNode root = mapper.readTree(json);
           JsonNode item = root.get("items").get(0);
           JsonNode address = item.get("address");

           String street = address.get("street").asText("");
           String houseNumber = address.get("houseNumber").asText("");
           String postalCode = address.get("postalCode").asText("");
           String city = address.get("city").asText("");

           return new AddressDTO(street, houseNumber, postalCode, city);

       } catch (JsonProcessingException e) {
           throw new RuntimeException(e.getMessage());
       }
   }
}
