package org.requirementsascode.moonwlker.testobject.animal;

import java.math.BigDecimal;

public class Cat extends Animal {
  private final String name;
  private final String nickname;
  private final Lives lives;

  public Cat(BigDecimal price, String name, String nickname, Lives lives) {
    super(price);
    this.name = name;
    this.nickname = nickname;
    this.lives = lives;
  }

  public String name() {
    return name;
  }

  public String nickname() {
    return nickname;
  }

  public Lives lives() {
    return lives;
  }
}