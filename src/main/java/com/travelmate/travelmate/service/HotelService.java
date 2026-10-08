package com.travelmate.travelmate.service;

import com.travelmate.travelmate.entity.Hotel;
import com.travelmate.travelmate.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    // 1. Get all hotels
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    // 2. Get hotel by ID
    public Hotel getHotelById(Long id) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        return optionalHotel.orElse(null);
    }

    // 3. Create hotel
    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    // 4. Update hotel
    public Hotel updateHotel(Long id, Hotel hotelDetails) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);
        if (optionalHotel.isPresent()) {
            Hotel existingHotel = optionalHotel.get();
            existingHotel.setName(hotelDetails.getName());
            existingHotel.setLocation(hotelDetails.getLocation());
            existingHotel.setDescription(hotelDetails.getDescription());
            existingHotel.setImageUrl(hotelDetails.getImageUrl());
            return hotelRepository.save(existingHotel);
        }
        return null;
    }

    // 5. Delete hotel
    public boolean deleteHotel(Long id) {
        if (hotelRepository.existsById(id)) {
            hotelRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
