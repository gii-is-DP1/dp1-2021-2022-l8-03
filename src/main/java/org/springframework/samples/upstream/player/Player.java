package org.springframework.samples.upstream.player;

import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.upstream.model.Person;
import org.springframework.samples.upstream.piece.Piece;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.score.Score;
import org.springframework.samples.upstream.user.User;

import lombok.Getter;
import lombok.Setter;

@Audited
@Entity
@Getter
@Setter
@Table(name = "players")
public class Player extends Person {

	@Column(name = "email")
	@NotEmpty
	@Email
	private String email;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
	@NotAudited
	private Collection<Piece> pieces;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
	@NotAudited
	private Collection<Score> scores;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
	@NotAudited
    private Collection<Round> rounds;
	
	@ManyToOne(optional = true)
	@NotAudited
    @JoinColumn(name = "round_id")
    private Round round;
	
	@Valid
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
	private User user;
	

	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("id", this.getId()).append("new", this.isNew()).append("lastName", this.getLastName())
				.append("firstName", this.getFirstName()).append("email", this.email).toString();
	}

}

