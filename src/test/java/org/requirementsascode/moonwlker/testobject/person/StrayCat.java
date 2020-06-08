package org.requirementsascode.moonwlker.testobject.person;

import java.math.BigDecimal;

import org.requirementsascode.moonwlker.testobject.animal.Cat;

public class StrayCat extends Cat {
  public StrayCat(BigDecimal price, String name, String nickname) {
    super(price, name, nickname);
  }
}