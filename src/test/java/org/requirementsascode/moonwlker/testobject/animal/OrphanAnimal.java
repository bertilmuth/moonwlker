package org.requirementsascode.moonwlker.testobject.animal;

/**
 * A class that doesn't inherit from Animal, for testing puposes.
 * 
 * @author b_muth
 *
 */
public class OrphanAnimal{
	private final String name;

	public OrphanAnimal(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
