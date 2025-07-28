package jess;

import java.util.Vector;

public class Deffacts {
  int m_name;

  Vector m_facts;

  String m_docstring;

  public final int name() {
    return this.m_name;
  }

  public final String docstring() {
    return this.m_docstring;
  }

  Deffacts(String paramString) {
    this.m_name = RU.putAtom(paramString);
    this.m_facts = new Vector();
  }

  void addFact(Fact paramFact) throws ReteException {
    addFact(paramFact.factData());
  }

  void addFact(ValueVector paramValueVector) {
    this.m_facts.addElement(paramValueVector);
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append("[Deffacts: ");
    stringBuffer.append(RU.getAtom(this.m_name));
    stringBuffer.append(" ");
    if (this.m_docstring != null) stringBuffer.append("\"" + this.m_docstring + "\"; ");
    stringBuffer.append(this.m_facts.size());
    stringBuffer.append(" facts");
    stringBuffer.append("]");
    return stringBuffer.toString();
  }
}
