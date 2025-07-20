package jess.reflect;

import jess.Context;
import jess.RU;
import jess.ReteException;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;

class undefinstance implements Userfunction {
  private definstance m_di;

  private int m_name = RU.putAtom("undefinstance");

  undefinstance(definstance paramdefinstance) {
    this.m_di = paramdefinstance;
  }

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    Value value = paramValueVector.get(1);
    return this.m_di.undefine(value.externalAddressValue());
  }
}
