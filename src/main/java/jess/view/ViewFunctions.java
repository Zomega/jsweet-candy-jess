package jess.view;

import jess.Rete;
import jess.Userpackage;

public class ViewFunctions implements Userpackage {
  public void add(Rete engine) {
    engine.addUserfunction(new View());
  }
}
