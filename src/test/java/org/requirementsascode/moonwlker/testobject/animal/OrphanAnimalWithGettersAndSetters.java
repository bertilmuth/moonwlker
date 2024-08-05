package org.requirementsascode.moonwlker.testobject.animal;

/**
 * A class that doesn't inherit from Animal, for testing puposes.
 * 
 * @author b_muth
 *
 */
public class OrphanAnimalWithGettersAndSetters{
	private final String name;

	public OrphanAnimalWithGettersAndSetters(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
