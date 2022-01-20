package org.springframework.samples.upstream.salmonBoard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class SalmonBoardServiceTest {
	
	
	@Autowired
	protected SalmonBoardService salmonBoardService;
	
    private Validator createValidator() {
    	LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
    	localValidatorFactoryBean.afterPropertiesSet();
    	return localValidatorFactoryBean;
    }
	
	@Test
	void shouldFindById() {
		Optional<SalmonBoard> salmonBoard=this.salmonBoardService.findById(1);
		assertThat(salmonBoard.get().getHeight()).isEqualTo(800);
	}
	
	@Test
	void shouldNotFindById() {
		Optional<SalmonBoard> salmonBoard=this.salmonBoardService.findById(99);
		assertThat(salmonBoard).isEqualTo(Optional.empty());
	}
	
	@Test
	void shouldFindByRoundId() {
		SalmonBoard salmonBoard=this.salmonBoardService.findByRoundId(1);
		assertThat(salmonBoard.getHeight()).isEqualTo(800);
	}
	
	@Test
	void shouldNotFindByRoundId() {
		SalmonBoard salmonBoard=this.salmonBoardService.findByRoundId(99);
		assertThat(salmonBoard).isEqualTo(null);
	}
	
	@Test
	void shouldDeleteSalmonBoard() {
		SalmonBoard salmonBoard=new SalmonBoard();
		this.salmonBoardService.saveBoard(salmonBoard);
		assertThat(this.salmonBoardService.findById(2).get()).isNotEqualTo(Optional.empty());
		this.salmonBoardService.delete(salmonBoard);
		assertThat(this.salmonBoardService.findById(2)).isEqualTo(Optional.empty());
	}
	
	@Test
	void shouldSaveSalmonBoard() {
		List<SalmonBoard> salmonBoardsStart=this.salmonBoardService.findAll();
		SalmonBoard salmonBoard=new SalmonBoard();
		this.salmonBoardService.saveBoard(salmonBoard);
		List<SalmonBoard> salmonBoardsEnd= this.salmonBoardService.findAll();
		assertThat(salmonBoardsEnd.size()).isEqualTo(salmonBoardsStart.size()+1);
	}
	
	@Test
	void shouldNotSaveSalmonBoard() {
			SalmonBoard salmonBoard=new SalmonBoard();
			salmonBoard.setWidth(-100);
			
			Validator validator = createValidator();
			Set<ConstraintViolation<SalmonBoard>> constraintViolations = validator.validate(salmonBoard);
			assertThat(constraintViolations.size()).isEqualTo(1);
			ConstraintViolation<SalmonBoard> violation = constraintViolations.iterator().next();
			assertThat(violation.getPropertyPath().toString()).isEqualTo("width");
			assertThat(violation.getMessage()).isEqualTo("tiene que ser mayor que 0");

	}
	
}
