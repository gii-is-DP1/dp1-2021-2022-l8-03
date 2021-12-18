package org.springframework.samples.upstream.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.samples.upstream.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
	
	  @GetMapping({"/","/welcome"})
	  public String welcome(Map<String, Object> model) {
		  
		  List<Person> persons = new ArrayList<Person>();
		  Person manuel = new Person();
		  manuel.setFirstName("Manuel");
		  manuel.setLastName("Dominguez");
		  
		  Person manuel2 = new Person();
          manuel2.setFirstName("Manuel");
          manuel2.setLastName("López");
          
          Person carlos= new Person();
          carlos.setFirstName("Carlos");
          carlos.setLastName("Delgado");
          
          Person celia= new Person();
          celia.setFirstName("Celia");
          celia.setLastName("Hermoso");
          
          Person sonia= new Person();
          sonia.setFirstName("Sonia");
          sonia.setLastName("Laure");
          
          Collections.addAll(persons, sonia, celia, carlos, manuel2, manuel);
          
          model.put("persons", persons);
          model.put("title", "My project");
          model.put("group", "Teachers");

	    return "welcome";
	  }
	 
}
