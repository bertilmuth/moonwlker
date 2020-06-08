package org.requirementsascode.moonwlker.testobject.animal;

import java.math.BigDecimal;

public class Cat extends Animal {
  private final String name;
  private final String nickname;
  
  public Cat(BigDecimal price, String name, String nickname) {
    super(price);
    this.name = name;
    this.nickname = nickname;
  }

  public String name() {
    return name;
  }

  public String nickname() {
    return nickname;
  }
}