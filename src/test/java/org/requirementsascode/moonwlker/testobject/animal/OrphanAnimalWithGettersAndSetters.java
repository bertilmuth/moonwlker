package org.requirementsascode.moonwlker.testobject.animal;

/**
 * A class that doesn't inherit from Animal, for testing puposes.
 * 
 * @author b_muth
 *
 */
public class OrphanAnimalWithGettersAndSetters{
	private String aName;

	public OrphanAnimalWithGettersAndSetters(String aName) {
		this.aName = aName;
	}

	public String getName() {
		return aName;
	}
	
	public void setName(String aName) {
		this.aName = aName;
	}
}
