package jess;

import java.util.Vector;

class GlobalContext extends Context {
  private Vector m_globalBindings = new Vector();

  public GlobalContext(Rete paramRete) {
    super(paramRete);
  }

  Binding findGlobalBinding(int paramInt) {
    for (byte b = 0; b < this.m_globalBindings.size(); b++) {
      Binding binding = this.m_globalBindings.elementAt(b);
      if (binding.m_name == paramInt) return binding;
    }
    return null;
  }

  public final Binding addGlobalBinding(int paramInt, Value paramValue) {
    Binding binding;
    if ((binding = findGlobalBinding(paramInt)) != null) {
      binding.m_val = paramValue;
      return binding;
    }
    binding = new Binding(paramInt, paramValue);
    this.m_globalBindings.addElement(binding);
    return binding;
  }

  public String toString() {
    return "[GlobalContext]";
  }
}
