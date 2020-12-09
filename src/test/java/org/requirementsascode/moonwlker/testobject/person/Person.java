package org.requirementsascode.moonwlker.testobject.person;

import java.time.Month;

import com.fasterxml.jackson.annotation.JsonCreator;

public abstract class Person {
  private final String firstName;
  private final String lastName;
  private final Month month;

  public Person(String firstName, String lastName,Month month) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.month = month;
  }

  public String firstName() {
    return firstName;
  }

  public String lastName() {
    return lastName;
  }

  public Month month()
  {
    return month;
  }
}