package org.requirementsascode.moonwlker.testobject.person;

public class Employee extends Person{
  private final String employeeNumber;

  public Employee(String firstName, String lastName, String employeeNumber) {
    super(firstName, lastName);
    this.employeeNumber = employeeNumber;
  }

  public String employeeNumber() {
    return employeeNumber;
  }
}
