package jess;

public class Activation {
  Token m_token;

  Defrule m_rule;

  private NodeTerm m_nt;

  Activation(Token paramToken, Defrule paramDefrule, NodeTerm paramNodeTerm) {
    this.m_token = paramToken;
    this.m_rule = paramDefrule;
    this.m_nt = paramNodeTerm;
  }

  boolean fire() throws ReteException {
    this.m_rule.fire(this.m_token);
    this.m_nt.ruleFired(this);
    return true;
  }

  public String toString() {
    try {
      StringBuffer stringBuffer = new StringBuffer(100);
      stringBuffer.append("[Activation: ");
      stringBuffer.append(Rete.factList(this.m_token));
      stringBuffer.append("]");
      return stringBuffer.toString();
    } catch (ReteException reteException) {
      return reteException.toString();
    }
  }
}
