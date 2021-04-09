package com.kosteklvp.priceUpdater;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Team {

	@Id
	private long id;

	private String fullName;

	private String shortName;

}
