package com.example.kartiksapra.watchlist.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.kartiksapra.watchlist.entity.Movie;
import com.example.kartiksapra.watchlist.repository.MovieRepo;
import com.example.kartiksapra.watchlist.service.DatabaseService;

import jakarta.validation.Valid;

@Controller
public class MovieController {

    private final MovieRepo movieRepo;

	@Autowired
	private DatabaseService databaseService;
  
	@Autowired
    public MovieController(MovieRepo movieRepo) {
        this.movieRepo = movieRepo;
    }
	
	@GetMapping("/watchlistItemForm")
	public ModelAndView showWatchListForm(@RequestParam(required = false) Integer id) {
          
//		System.out.println(id);
		String viewName = "watchlistItemForm";
		
		Map<String, Object> model = new HashMap<>();
		
		if(id == null) {
			model.put("watchlistItem", new Movie());
		}
		else {
			model.put("watchlistItem", databaseService.getMovieById(id));
		}
//		Movie dummyMovie = new Movie();
//		dummyMovie.setTitle("dummy");
//		dummyMovie.setRating(0);
//		dummyMovie.setPriority("HIGH");
//		dummyMovie.setComment("john doe");
//		model.put("watchlistItem", new Movie());
//		model.put("watchlistItem", dummyMovie);
		
		return new ModelAndView(viewName, model);
		
	}
	
	@PostMapping("/watchlistItemForm")
	public ModelAndView submitWatchListForm(@Valid @ModelAttribute("watchlistItem")	Movie movie, BindingResult bindingResult) {
		

		if(bindingResult.hasErrors()) {
			// if errors are there, redisplay the form and let user enter again
			
			return new ModelAndView("watchlistItemForm");
		}
		
		Integer id = movie.getId();
		if(id == null) {
			// create new movie
			databaseService.create(movie);
		} else {
			// update
			databaseService.update(movie, id);
		}
		
		RedirectView rd = new RedirectView();
		rd.setUrl("/watchlist");
		
		return new ModelAndView(rd);
		}
	
	@GetMapping("/watchlist")
	public ModelAndView getWatchlist() {
		
		String viewName = "watchlist";
		Map<String, Object> model = new HashMap<>();
		
		List<Movie> movieList = databaseService.getAllMovies();
		model.put("watchlistrows", movieList);
		model.put("noofmovies", movieList.size());
		
		return new ModelAndView(viewName, model);
	}
	
	
}
