package jess;

import java.util.Vector;

public class Defglobal {
  Vector m_bindings = new Vector();

  void addGlobal(String paramString, Value paramValue) {
    this.m_bindings.addElement(new Binding(RU.putAtom(paramString), paramValue));
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append("[Defglobal: ");
    stringBuffer.append(this.m_bindings.size());
    stringBuffer.append(" variables");
    stringBuffer.append("]");
    return stringBuffer.toString();
  }
}
