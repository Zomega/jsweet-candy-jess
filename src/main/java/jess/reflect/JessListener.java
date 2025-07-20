package jess.reflect;

import java.awt.AWTEvent;
import jess.Funcall;
import jess.Rete;
import jess.ReteException;
import jess.Value;

public class JessListener {
  private Funcall m_fc;

  private Rete m_engine;

  JessListener(String paramString, Rete paramRete) throws ReteException {
    this.m_engine = paramRete;
    this.m_fc = new Funcall(paramString, paramRete);
    this.m_fc.setLength(2);
  }

  final void receiveEvent(AWTEvent paramAWTEvent) {
    try {
      this.m_fc.set(new Value(paramAWTEvent, 2048), 1);
      Funcall.simpleExecute(this.m_fc, this.m_engine.globalContext());
    } catch (ReteException reteException) {
      this.m_engine.errStream().println(reteException);
    }
  }
}
