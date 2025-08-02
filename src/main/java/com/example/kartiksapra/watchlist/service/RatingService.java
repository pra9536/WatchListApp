package com.example.kartiksapra.watchlist.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class RatingService {
   
//	String apiUrl = "https://www.omdbapi.com/?i=tt3896198&apikey=69329663";
	
	String key = "69329663";
	String base = "https://www.omdbapi.com/";
	public String getMovieRating(String title) {
		try {
			// try to fetch the rating by calling omdb api
			 RestTemplate template = new RestTemplate();
			 // apiUrl + title
			 String apiUrl = base + "?t=" + title + "&apikey=" + key;
			 
			 ResponseEntity<ObjectNode> response = template.getForEntity(apiUrl, ObjectNode.class);
			 
			 ObjectNode jsonObject = response.getBody();
			 
			 if(jsonObject != null && "True".equalsIgnoreCase(jsonObject.path("Response").asText())) {
			 System.out.println(jsonObject.path("imdbRating").asText());
			 String rating = jsonObject.path("imdbRating").asText();
			 return (rating != null && !rating.isEmpty() && !rating.equalsIgnoreCase("N/A")) ? rating : null;
			 
			 }
			 else {
//				 System.out.println("Movie not found: " + jsonObject.path("Error").asText());
				 return null;
			 }
		} catch(Exception e) {
			// to user entered rating will be taken
			System.out.println("Either movie name not available or api is down" + e.getMessage());
			return null;
		}
	}
}
