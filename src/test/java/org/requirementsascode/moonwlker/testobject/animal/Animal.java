package org.requirementsascode.moonwlker.testobject.animal;

import java.math.BigDecimal;

public class Animal {
  private final BigDecimal price;

  public Animal(BigDecimal price) {
    this.price = price;
  }

  public BigDecimal price() {
    return price;
  }
}