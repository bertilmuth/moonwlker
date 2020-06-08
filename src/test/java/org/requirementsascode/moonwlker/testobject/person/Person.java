package org.requirementsascode.moonwlker.testobject.person;

public abstract class Person {
  private final String firstName;
  private final String lastName;

  public Person(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String firstName() {
    return firstName;
  }

  public String lastName() {
    return lastName;
  }
}