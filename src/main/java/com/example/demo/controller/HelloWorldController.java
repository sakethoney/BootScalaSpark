package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.work.MovieRatings;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class HelloWorldController {

    @RequestMapping(value="/invoke", method=GET)
    @ResponseBody
    public String callScalaMovieRatingCode() {
        MovieRatings.CalculateAverageRating();
        return "Hello World!";
    }
}
