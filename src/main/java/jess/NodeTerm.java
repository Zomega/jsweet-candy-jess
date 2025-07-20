package jess;

import java.util.Vector;

public class NodeTerm extends Node {
  private Defrule m_rule;

  private Vector m_activations = new Vector();

  public Defrule rule() {
    return this.m_rule;
  }

  public NodeTerm(Defrule paramDefrule, Rete paramRete) {
    super(paramRete);
    this.m_rule = paramDefrule;
  }

  public void standDown(Activation paramActivation) throws ReteException {
    this.m_activations.removeElement(paramActivation);
    this.m_engine.standDown(paramActivation);
  }

  public void ruleFired(Activation paramActivation) {
    this.m_activations.removeElement(paramActivation);
    this.m_engine.ruleFired(paramActivation);
  }

  private void doAddCall(Token paramToken) throws ReteException {
    if (paramToken.m_tag == 2)
      for (byte b = 0; b < this.m_activations.size(); b++) {
        Activation activation1 = this.m_activations.elementAt(b);
        if (activation1.m_token.dataEquals(paramToken))
          return;
      }
    Activation activation = new Activation(paramToken, this.m_rule, this);
    this.m_activations.addElement(activation);
    this.m_engine.addActivation(activation);
  }

  public boolean callNode(Token paramToken, int paramInt) throws ReteException {
    int i;
    byte b;
    switch (paramToken.m_tag) {
      case 3:
        for (i = 0; i < this.m_activations.size(); i++)
          standDown(this.m_activations.elementAt(i));
        break;
      case 0:
      case 2:
        doAddCall(paramToken);
        break;
      case 1:
        i = this.m_activations.size();
        for (b = 0; b < i; b++) {
          Activation activation = this.m_activations.elementAt(b);
          if (paramToken.dataEquals(activation.m_token)) {
            standDown(activation);
            return true;
          }
        }
        break;
    }
    return true;
  }

  private void debugPrint(Token paramToken, int paramInt) {
    System.out.println("TEST " + toString() + ";tag=" + paramToken.m_tag + "callType=" + paramInt);
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append("[NodeTerm rule=");
    stringBuffer.append(RU.getAtom(this.m_rule.m_name));
    stringBuffer.append(";Activations:\n");
    for (byte b = 0; b < this.m_activations.size(); b++) {
      stringBuffer.append(this.m_activations.elementAt(b));
      stringBuffer.append("\n");
    }
    stringBuffer.append(";usecount = ");
    stringBuffer.append(this.m_usecount);
    stringBuffer.append("]");
    return stringBuffer.toString();
  }
}
