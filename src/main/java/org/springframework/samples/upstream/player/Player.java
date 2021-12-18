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
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.core.style.ToStringCreator;
import org.springframework.samples.upstream.model.Person;
import org.springframework.samples.upstream.piece.Piece;
import org.springframework.samples.upstream.round.Round;
import org.springframework.samples.upstream.score.Score;
import org.springframework.samples.upstream.user.User;

/**
 * Simple JavaBean domain object representing an player.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
@Entity
@Table(name = "players")
public class Player extends Person {

	@Column(name = "email")
	@NotEmpty
	@Email
	private String email;
	


	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
	private Collection<Piece> pieces;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
	private Collection<Score> scores;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private Collection<Round> rounds;
	
	@ManyToOne(optional = true)
    @JoinColumn(name = "round_id")
    private Round round;

	//
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
	private User user;
	//

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Round getRound() {
		return round;
	}
	
	public void setRound(Round round) {
		this.round = round;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	@Override
	public String toString() {
		return new ToStringCreator(this)

				.append("id", this.getId()).append("new", this.isNew()).append("lastName", this.getLastName())
				.append("firstName", this.getFirstName()).append("email", this.email).toString();
	}

}

