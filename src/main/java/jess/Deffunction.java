package jess;

import java.util.Vector;

public class Deffunction extends Context implements Userfunction {
  int m_name;

  String m_docstring;

  private int m_nargs;

  public final int name() {
    return this.m_name;
  }

  public final String docstring() {
    return this.m_docstring;
  }

  Deffunction(String paramString, Rete paramRete) {
    super(paramRete);
    this.m_name = RU.putAtom(paramString);
  }

  void addArgument(String paramString) {
    addBinding(RU.putAtom(paramString), this.m_nargs + 1, 0, -1);
    this.m_nargs++;
  }

  void addValue(Value paramValue) {
    this.m_actions.addElement(paramValue);
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    Value value = null;
    if (paramValueVector.size() < this.m_nargs + 1)
      throw new ReteException("Deffunction::Fire", "Too few arguments to Deffunction", RU.getAtom(this.m_name));
    push();
    clearReturnValue();
    int i = this.m_bindings.size();
    for (byte b1 = 0; b1 < i; b1++) {
      Binding binding = this.m_bindings.elementAt(b1);
      if (binding.m_slotIndex != -2)
        binding.m_val = paramValueVector.get(binding.m_factIndex);
    }
    i = this.m_actions.size();
    for (byte b2 = 0; b2 < i; b2++) {
      if (this.m_actions.elementAt(b2) instanceof ValueVector) {
        Funcall funcall1 = this.m_actions.elementAt(b2);
        Funcall funcall2 = expandAction(funcall1);
        value = Funcall.execute(funcall2, this);
      } else {
        Value value1 = this.m_actions.elementAt(b2);
        value = expandValue(value1);
      }
      if (returning()) {
        value = getReturnValue();
        clearReturnValue();
        break;
      }
    }
    pop();
    return value;
  }

  private void debugPrint(Vector paramVector) throws ReteException {
    this.m_engine.outStream().print("FIRE " + toString());
    for (byte b = 0; b < paramVector.size(); b++) {
      ValueVector valueVector = paramVector.elementAt(b);
      this.m_engine.outStream().print(" f-" + valueVector.get(2).factIDValue());
      if (b < paramVector.size() - 1)
        this.m_engine.outStream().print(",");
    }
    this.m_engine.outStream().println();
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append("[Deffunction: ");
    stringBuffer.append(RU.getAtom(this.m_name));
    stringBuffer.append(" ");
    if (this.m_docstring != null)
      stringBuffer.append("\"" + this.m_docstring + "\"; ");
    stringBuffer.append("]");
    return stringBuffer.toString();
  }
}
