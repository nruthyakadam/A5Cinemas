package com.a5cinemas.user.service;

import java.util.List;

import org.springframework.ui.Model;

import com.a5cinemas.user.dto.MovieCreationDto;
import com.a5cinemas.user.model.Movie;

public interface MovieService {

	public Movie findByTitle(String title);
	
    public Movie findByCategory(String category);

    Movie save(MovieCreationDto registration);

	public List<Movie> findAll();

	void fetchUpcomingAndCurrentMovies(Model model, String keyword, List<Movie> foundMovies, List<Movie> movies);
	
}
