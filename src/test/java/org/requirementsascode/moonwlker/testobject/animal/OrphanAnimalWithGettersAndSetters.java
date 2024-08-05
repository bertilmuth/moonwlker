package org.requirementsascode.moonwlker.testobject.animal;

/**
 * A class that doesn't inherit from Animal, for testing puposes.
 * 
 * @author b_muth
 *
 */
public class OrphanAnimalWithGettersAndSetters{
	private String name;
	
	public OrphanAnimalWithGettersAndSetters() {
	}

	public OrphanAnimalWithGettersAndSetters(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String aName) {
		this.name = aName;
	}
}
