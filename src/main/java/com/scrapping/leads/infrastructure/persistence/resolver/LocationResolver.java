package com.scrapping.leads.infrastructure.persistence.resolver;

import com.scrapping.leads.infrastructure.persistence.entity.LocationJpaEntity;
import com.scrapping.leads.infrastructure.persistence.repository.LocationJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class LocationResolver {
    private final LocationJpaRepository repository;

    public LocationResolver(LocationJpaRepository repository) {
        this.repository = repository;
    }

    public LocationJpaEntity resolve(String city, String state, String country) {
        if (city == null && country == null) return null;
        return repository.findByCityAndCountry(city, country).orElseGet(() -> {
            LocationJpaEntity entity = new LocationJpaEntity();
            entity.setCity(city);
            entity.setState(state);
            entity.setCountry(country);
            return repository.save(entity);
        });
    }
}
