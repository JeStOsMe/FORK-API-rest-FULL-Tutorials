package com.books.integrate.spring.react.controller;

import java.util.*;

import com.books.integrate.spring.react.model.Tutorial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.books.integrate.spring.react.repository.TutorialRepository;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class TutorialController {

	@Autowired
	TutorialRepository tutorialRepository;

	@GetMapping("/tutorials")
	public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {
		try {
			List<Tutorial> tutorials = new ArrayList<Tutorial>();

			if (title == null)
				tutorialRepository.findAll().forEach(tutorials::add);
			else
				tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

		if (tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


	@PostMapping("/tutorials")
	public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
		try {
			Tutorial _tutorial = tutorialRepository
					.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false, tutorial.getPrice()));
			return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@PutMapping("/tutorials/{id}")
	public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

		if (tutorialData.isPresent()) {
			Tutorial _tutorial = tutorialData.get();
			_tutorial.setTitle(tutorial.getTitle());
			_tutorial.setDescription(tutorial.getDescription());
			_tutorial.setPublished(tutorial.isPublished());
			return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

//HttpStatus
	@DeleteMapping("/tutorials/{id}")
	public ResponseEntity<String> deleteTutorial(@PathVariable("id") long id) {
		try {
			tutorialRepository.deleteById(id);
				return new ResponseEntity<>("Tutorials DELETE!! ",HttpStatus.NO_CONTENT);
			} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

	@DeleteMapping("/tutorials")
	public ResponseEntity<HttpStatus> deleteAllTutorials() {
		try {
			tutorialRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}

	}
        //Delete all records with the title entered in the form /tutorials/query?title=tutorial_title 
        @DeleteMapping("/tutorials/delete/query")
        public ResponseEntity<HttpStatus> deleteTutorialByTitle(@RequestParam("title")String title){
            List<Tutorial> aux = tutorialRepository.findByTitleContaining(title);
            try{
                for (Tutorial element: aux){
                    tutorialRepository.deleteById(element.getId());
                }
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (Exception ex){
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
        }
        /*Updates title, description, and published status, based on the title 
        submitted in tutorials/update/query?title=tutorial_title. You need to 
        add the tutorial ID in the body. */
        @PutMapping("/tutorials/update/query")
        public ResponseEntity<Tutorial> updateTutorialByTitle(@RequestParam("title") String title, @RequestBody Tutorial tutorial){
            Optional<Tutorial> tutorialData = tutorialRepository.findById(tutorial.getId());
            if(tutorialData.isPresent()){
                Tutorial _tutorial = tutorialData.get();
                _tutorial.setTitle(tutorial.getTitle());
                _tutorial.setDescription(tutorial.getDescription());
                _tutorial.setPublished(tutorial.isPublished());
                _tutorial.setPrice(tutorial.getPrice());
                return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
            } else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

	@GetMapping("/tutorials/published")
	public ResponseEntity<List<Tutorial>> findByPublished() {
		try {
			List<Tutorial> tutorials = tutorialRepository.findByPublished(true);

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
        //queries all tutorials with the price supplied in /tutorials/price/title_price. 
        @GetMapping(path= "/tutorials/price/{price}")
        public ResponseEntity<List<Tutorial>> findByPrice(@PathVariable("price") Double price) {
            try{
                List<Tutorial> tutorials = tutorialRepository.findByPrice(price);
                if (tutorials.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
                return new ResponseEntity<>(tutorials, HttpStatus.OK);
            } catch (Exception ex){
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

}
