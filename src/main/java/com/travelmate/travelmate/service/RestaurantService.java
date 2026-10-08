package com.travelmate.travelmate.service;

import com.travelmate.travelmate.entity.Restaurant;
import com.travelmate.travelmate.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // 1. Get all restaurants
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    // 2. Get restaurant by ID
    public Restaurant getRestaurantById(Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        return optionalRestaurant.orElse(null);
    }

    // 3. Create restaurant
    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    // 4. Update restaurant
    public Restaurant updateRestaurant(Long id, Restaurant restaurantDetails) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        if (optionalRestaurant.isPresent()) {
            Restaurant existingRestaurant = optionalRestaurant.get();
            existingRestaurant.setName(restaurantDetails.getName());
            existingRestaurant.setLocation(restaurantDetails.getLocation());
            existingRestaurant.setDescription(restaurantDetails.getDescription());
            existingRestaurant.setImageUrl(restaurantDetails.getImageUrl());
            return restaurantRepository.save(existingRestaurant);
        }
        return null;
    }

    // 5. Delete restaurant
    public boolean deleteRestaurant(Long id) {
        if (restaurantRepository.existsById(id)) {
            restaurantRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
