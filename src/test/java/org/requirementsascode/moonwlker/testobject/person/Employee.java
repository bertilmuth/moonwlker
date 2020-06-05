package org.requirementsascode.moonwlker.testobject.person;

public class Employee extends Person{
  private String employeeNumber;
  
  protected Employee() {
  }

  public String getEmployeeNumber() {
    return employeeNumber;
  }

  public void setEmployeeNumber(String employeeNumber) {
    this.employeeNumber = employeeNumber;
  }
}
