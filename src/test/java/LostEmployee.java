import org.requirementsascode.moonwlker.testobject.person.Employee;

import java.time.Month;

public class LostEmployee extends Employee {
  public LostEmployee(String firstName, String lastName, Month month, String employeeNumber) {
    super(firstName, lastName, month, employeeNumber);
  }
}