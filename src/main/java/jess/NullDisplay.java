package jess;

import java.applet.Applet;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Observable;

public class NullDisplay extends Observable implements ReteDisplay {
  public void assertFact(ValueVector paramValueVector) {
    setChanged();
    notifyObservers("FACT");
  }

  public void retractFact(ValueVector paramValueVector) {
    setChanged();
    notifyObservers("FACT");
  }

  public void addDeffacts(Deffacts paramDeffacts) {}

  public void addDeftemplate(Deftemplate paramDeftemplate) {}

  public void addDefrule(Defrule paramDefrule) {
    setChanged();
    notifyObservers("RULE");
  }

  public void activateRule(Defrule paramDefrule) {}

  public void deactivateRule(Defrule paramDefrule) {}

  public void fireRule(Defrule paramDefrule) {
    setChanged();
    notifyObservers("RULE");
    System.gc();
  }

  public PrintStream stdout() {
    return System.out;
  }

  public InputStream stdin() {
    return System.in;
  }

  public PrintStream stderr() {
    return System.err;
  }

  public Applet applet() {
    return null;
  }
}
