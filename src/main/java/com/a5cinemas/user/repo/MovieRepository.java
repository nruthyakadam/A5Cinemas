package com.a5cinemas.user.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.a5cinemas.user.model.Movie;


@Repository
public interface MovieRepository extends JpaRepository < Movie, Long > {
    public Movie findByTitle(String title);
    public Movie findByCategory(String category);
    
    @Query(value = "select * from cinema.movie c where c.movie_title like %:keyword%", nativeQuery = true)
    List<Movie> findByKeyword(@Param("keyword") String keyword);
    
    @Query(value = "select * from cinema.movie c where c.category like %:keyword%", nativeQuery = true)
    List<Movie> findByCategoryKeyword(@Param("keyword") String keyword);
}