package org.springframework.samples.upstream.user;

import org.springframework.data.repository.CrudRepository;



public interface AuthoritiesRepository extends  CrudRepository<Authorities, String>{
	
}
