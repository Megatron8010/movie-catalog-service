package com.example.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.entity.CatalogItem;
import com.example.entity.Movie;
import com.example.entity.Rating;
import com.example.entity.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	@Autowired
    private RestTemplate restTemplate;

	@RequestMapping("/{userid}")
	public List<CatalogItem> getCatalog(@PathVariable("userid") String userid) {
	//	RestTemplate restTemplate = new RestTemplate();
	//	Movie movie = restTemplate.getForObject("localhost:8081/movies/foo", Movie.class); //parsing getting the json from the url and making it an object of movie.class
	/*	List<Rating> ratings = Arrays.asList(
				new Rating("1234", 3),
                new Rating("5678", 4)
				);    */
		
		UserRating ratings = restTemplate.getForObject("http://localhost:8082/ratingsdata/user/"+ userid, UserRating.class);
		return ratings.getRatings().stream()
				.map(rating -> {
                    Movie movie = restTemplate.getForObject("http://localhost:8081/movies/" + rating.getMovieId(), Movie.class);
                    return new CatalogItem(movie.getName(), "Description", rating.getRating());
                })
                .collect(Collectors.toList());	
    }
}
