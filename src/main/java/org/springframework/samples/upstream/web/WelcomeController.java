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
          model.put("title", "Upstream");
          model.put("group", "Developers");
          model.put("description","Like every spring the melted snow fills the riverbeds, opening the way back\r\n" + 
          		"home for the Salmon, after a life swimming in the oceans.\r\n" + 
          		"Each player controls a run of salmons going upstream to lay their eggs\r\n" + 
          		"where they were born. During their journey they will face hungry bears,\r\n" + 
          		"fierce birds of prey, as well as patient herons, which they must avoid in\r\n" + 
          		"order to survive.\r\n" + 
          		"The game ends when all the salmons are either deceased or on the spawn\r\n" + 
          		"area, at the very end of the river. Players will then score points, according\r\n" + 
          		"to the number and speed of their remaining Salmons: the player with most\r\n" + 
          		"points wins the game!\r" +
          		"We have designed the online version for those players who want to delight and spend a good time.");

	    return "welcome";
	  }
	 
}
