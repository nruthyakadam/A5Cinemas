package com.a5cinemas.user.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.a5cinemas.user.dto.MovieCreationDto;
import com.a5cinemas.user.model.CinemaUserDetails;
import com.a5cinemas.user.model.Movie;
import com.a5cinemas.user.model.User;
import com.a5cinemas.user.repo.MovieRepository;

@Service
@Transactional
public class MovieServiceImpl implements MovieService {

	@Autowired
	private MovieRepository movieRepository;


	@Override
	public Movie findByTitle(String title) {
		Movie movie = movieRepository.findByTitle(title);
		return movie;
	}

	@Override
	public Movie findByCategory(String category) {
		Movie movie = movieRepository.findByCategory(category);
		return movie;
	}

	@Override
	public Movie save(MovieCreationDto movieCreationDto) {
		Movie movie = new Movie();
		movie.setCast(movieCreationDto.getCast());
		movie.setCategory(movieCreationDto.getGenre());
		movie.setDescription(movieCreationDto.getDescription());
		movie.setDirector(movieCreationDto.getDirector());
		movie.setLanguage(movieCreationDto.getLanguage());
		movie.setProducer(movieCreationDto.getProducer());
		movie.setReview(movieCreationDto.getReview());
		movie.setRuntime(movieCreationDto.getRuntime());
		movie.setTitle(movieCreationDto.getTitle());
		movie.setRating(movieCreationDto.getRating());
		movie.setPoster(movieCreationDto.getPoster());
		movie.setTrailer(movieCreationDto.getTrailer());
		Movie savedMovie = movieRepository.save(movie);
		return savedMovie;
	}
	

	@Override
	public List<Movie> findAll() {
		return movieRepository.findAll();
	}


}