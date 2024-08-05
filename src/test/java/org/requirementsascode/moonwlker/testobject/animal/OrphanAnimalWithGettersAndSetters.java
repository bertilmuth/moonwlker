package org.requirementsascode.moonwlker.testobject.animal;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A class that doesn't inherit from Animal, for testing puposes.
 * 
 * @author b_muth
 *
 */
public class OrphanAnimalWithGettersAndSetters{
    @JsonIgnore
	private String aName;
	
	public OrphanAnimalWithGettersAndSetters() {
	}

	public OrphanAnimalWithGettersAndSetters(String name) {
		this.aName = name;
	}

	public String getName() {
		return aName;
	}
	
	public void setName(String aName) {
		this.aName = aName;
	}
}
