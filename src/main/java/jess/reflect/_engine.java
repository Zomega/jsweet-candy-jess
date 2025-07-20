package jess.reflect;

import jess.Context;
import jess.RU;
import jess.Rete;
import jess.ReteException;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;

class _engine implements Userfunction {
  private int m_name = RU.putAtom("engine");

  private Value m_engine;

  public int name() {
    return this.m_name;
  }

  _engine(Rete paramRete) {
    try {
      this.m_engine = new Value(paramRete, 2048);
    } catch (ReteException reteException) {}
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return this.m_engine;
  }
}
