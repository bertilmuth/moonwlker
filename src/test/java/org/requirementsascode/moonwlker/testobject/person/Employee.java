package org.requirementsascode.moonwlker.testobject.person;

import java.time.Month;

public class Employee extends Person{
  private final String employeeNumber;

  public Employee(String firstName, String lastName, Month month, String employeeNumber) {
    super(firstName, lastName, month);
    this.employeeNumber = employeeNumber;
  }

  public String employeeNumber() {
    return employeeNumber;
  }
}