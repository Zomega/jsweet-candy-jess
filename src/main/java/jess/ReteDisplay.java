package jess;

import java.applet.Applet;
import java.io.InputStream;
import java.io.PrintStream;

public interface ReteDisplay {
  void assertFact(ValueVector paramValueVector);

  void retractFact(ValueVector paramValueVector);

  void addDeffacts(Deffacts paramDeffacts);

  void addDeftemplate(Deftemplate paramDeftemplate);

  void addDefrule(Defrule paramDefrule);

  void activateRule(Defrule paramDefrule);

  void deactivateRule(Defrule paramDefrule);

  void fireRule(Defrule paramDefrule);

  PrintStream stdout();

  InputStream stdin();

  PrintStream stderr();

  Applet applet();
}
