package Controller;

import DTO.Address.AddressDTO;
import DTO.Address.CoordinatesDTO;
import Service.GeocodingService;

public class GeocodingController {

    private GeocodingService geocodingService = new GeocodingService();

    public CoordinatesDTO geocode(String streeName, String number, String postalCode, String cityName) {
        return geocodingService.geocode(streeName, number, postalCode, cityName);
    }

    public AddressDTO reverseGeocode(double lat, double lng) {
        return geocodingService.reverseGeocode(lat, lng);
    }
}
