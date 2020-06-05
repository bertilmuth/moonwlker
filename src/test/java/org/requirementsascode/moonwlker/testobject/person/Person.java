package org.requirementsascode.moonwlker.testobject.person;

public class Person {
  private String firstName;
  private String lastName;

  protected Person() {
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}