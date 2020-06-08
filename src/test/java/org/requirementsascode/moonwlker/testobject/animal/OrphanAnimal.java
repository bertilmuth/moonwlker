package org.requirementsascode.moonwlker.testobject.animal;

/**
 * A class that doesn't inherit from Animal, for testing puposes.
 * 
 * @author b_muth
 *
 */
public class OrphanAnimal {
  private String name;

  private OrphanAnimal() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
