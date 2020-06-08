package org.requirementsascode.moonwlker.testobject.animal;

import java.math.BigDecimal;

/**
 * Inspired by:
 * https://stackoverflow.com/questions/57834971/java-jackson-polymorphic-match-on-interface-field-results-in-duplicate-field-n
 *
 */
public class Dog extends Animal {
  private final String name;
  private final String command;

  public Dog(BigDecimal price, String name, String command) {
    super(price);
    this.name = name;
    this.command = command;
  }
  
  public String name() {
    return name;
  }

  public String command() {
    return command;
  }
}