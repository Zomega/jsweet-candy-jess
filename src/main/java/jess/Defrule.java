package jess;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;

public class Defrule extends Context {
  int m_name;

  Vector m_patts;

  String m_docstring;

  private int m_id;

  int m_salience;

  private boolean m_frozen = false;

  private Funcall[] m_localActions;

  private Vector m_nodes = new Vector();

  public final int name() {
    return this.m_name;
  }

  public final String docstring() {
    return this.m_docstring;
  }

  Defrule(String paramString, Rete paramRete) {
    super(paramRete);
    this.m_name = RU.putAtom(paramString);
    this.m_id = paramRete.nextRuleId();
    this.m_patts = new Vector();
  }

  public int id() {
    return this.m_id;
  }

  void addPattern(Pattern paramPattern) throws ReteException {
    this.m_patts.addElement(paramPattern);
    paramPattern.compact();
    for (byte b = 0; b < paramPattern.m_tests.length; b++) {
      if (paramPattern.m_tests[b] != null)
        for (byte b1 = 0; b1 < (paramPattern.m_tests[b]).length; b1++) {
          if (((paramPattern.m_tests[b][b1]).m_slotValue.type() == 8 || (paramPattern.m_tests[b][b1]).m_slotValue.type() == 8192) && findBinding((paramPattern.m_tests[b][b1]).m_slotValue.variableValue()) == null)
            if ((paramPattern.m_tests[b][b1]).m_test == 0) {
              addBinding((paramPattern.m_tests[b][b1]).m_slotValue.variableValue(), this.m_patts.size() - 1, 3 + b, (paramPattern.m_tests[b][b1]).m_subIdx);
            } else {
              throw new ReteException("Defrule::AddPattern", "First reference to variable negated", (paramPattern.m_tests[b][b1]).m_slotValue.toString());
            }
        }
    }
  }

  void freeze() {
    if (this.m_frozen)
      return;
    this.m_frozen = true;
    this.m_localActions = new Funcall[this.m_actions.size()];
    for (byte b = 0; b < this.m_localActions.length; b++)
      this.m_localActions[b] = this.m_actions.elementAt(b);
  }

  void ready(Token paramToken) throws ReteException {
    int i = this.m_bindings.size();
    for (byte b = 0; b < i; b++) {
      Binding binding = this.m_bindings.elementAt(b);
      if (binding.m_slotIndex != -2) {
        ValueVector valueVector = paramToken.fact(binding.m_factIndex);
        try {
          if (binding.m_slotIndex == -1) {
            binding.m_val = valueVector.get(2);
          } else if (binding.m_subIndex == -1) {
            binding.m_val = valueVector.get(binding.m_slotIndex);
          } else {
            ValueVector valueVector1 = valueVector.get(binding.m_slotIndex).listValue();
            binding.m_val = valueVector1.get(binding.m_subIndex);
          }
        } catch (Throwable throwable) {}
      }
    }
  }

  void fire(Token paramToken) throws ReteException {
    if (this.m_engine.watchRules())
      debugPrint(paramToken);
    push();
    clearReturnValue();
    ready(paramToken);
    int i = this.m_localActions.length;
    for (byte b = 0; b < i; b++) {
      Funcall funcall1 = this.m_localActions[b];
      Funcall funcall2 = expandAction(funcall1);
      Funcall.execute(funcall2, this);
      if (this.m_return) {
        clearReturnValue();
        pop();
        return;
      }
    }
    pop();
  }

  void debugPrint(Token paramToken) throws ReteException {
    PrintStream printStream = this.m_engine.outStream();
    printStream.print("FIRE " + toString());
    for (byte b = 0; b < paramToken.size(); b++) {
      ValueVector valueVector = paramToken.fact(b);
      if (valueVector.get(2).factIDValue() != -1)
        printStream.print(" f-" + valueVector.get(2).factIDValue());
      if (b < paramToken.size() - 1)
        printStream.print(",");
    }
    printStream.println();
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append("[Defrule: ");
    stringBuffer.append(RU.getAtom(this.m_name));
    stringBuffer.append(" ");
    if (this.m_docstring != null)
      stringBuffer.append("\"" + this.m_docstring + "\"; ");
    stringBuffer.append(this.m_patts.size());
    stringBuffer.append(" patterns; salience: ");
    stringBuffer.append(this.m_salience);
    stringBuffer.append("]");
    return stringBuffer.toString();
  }

  public String listNodes() {
    StringBuffer stringBuffer = new StringBuffer(100);
    for (byte b = 0; b < this.m_nodes.size(); b++) {
      stringBuffer.append(((Successor)this.m_nodes.elementAt(b)).m_node);
      stringBuffer.append("\n");
    }
    return stringBuffer.toString();
  }

  void addNode(Successor paramSuccessor) {
    this.m_nodes.addElement(paramSuccessor);
  }

  void remove(Vector paramVector) {
    Enumeration enumeration = this.m_nodes.elements();
    while (enumeration.hasMoreElements()) {
      Successor successor = enumeration.nextElement();
      if (--successor.m_node.m_usecount <= 0) {
        paramVector.removeElement(successor);
        Enumeration enumeration1 = this.m_nodes.elements();
        while (enumeration1.hasMoreElements()) {
          Node node = ((Successor)enumeration1.nextElement()).m_node;
          node.removeSuccessor(successor);
        }
      }
    }
    enumeration = this.m_nodes.elements();
    while (enumeration.hasMoreElements()) {
      Successor successor = enumeration.nextElement();
      successor.m_node.freeze();
    }
    this.m_nodes.removeAllElements();
  }

  public String ppRule() throws ReteException {
    StringBuffer stringBuffer = new StringBuffer(256);
    stringBuffer.append("(defrule ");
    stringBuffer.append(RU.getAtom(this.m_name));
    stringBuffer.append("\n");
    if (this.m_docstring != null) {
      stringBuffer.append("   \"");
      stringBuffer.append(this.m_docstring);
      stringBuffer.append("\"\n");
    }
    if (this.m_salience != 0) {
      stringBuffer.append("(declare (salience ");
      stringBuffer.append(this.m_salience);
      stringBuffer.append("))\n");
    }
    for (byte b1 = 0; b1 < this.m_patts.size(); b1++) {
      Pattern pattern = this.m_patts.elementAt(b1);
      if (b1 != 0 || pattern.name() != RU.putAtom("initial-fact"))
        if (pattern.negated() != 0) {
          stringBuffer.append("   (not ");
          stringBuffer.append(pattern.ppPattern(this.m_engine));
          stringBuffer.append(")\n");
        } else {
          stringBuffer.append("   ");
          String str;
          if ((str = isBoundPattern(b1)) != null) {
            stringBuffer.append("?");
            stringBuffer.append(str);
            stringBuffer.append(" <- ");
          }
          stringBuffer.append(pattern.ppPattern(this.m_engine));
          stringBuffer.append("\n");
        }
    }
    stringBuffer.append("  =>\n");
    for (byte b2 = 0; b2 < this.m_actions.size(); b2++) {
      Funcall funcall = this.m_actions.elementAt(b2);
      stringBuffer.append("   ");
      stringBuffer.append(funcall.ppFuncall(this.m_engine));
      stringBuffer.append("\n");
    }
    stringBuffer.append(")");
    return stringBuffer.toString();
  }

  private String isBoundPattern(int paramInt) {
    int i = this.m_bindings.size();
    for (byte b = 0; b < i; b++) {
      Binding binding = this.m_bindings.elementAt(b);
      if (binding.m_slotIndex == -1 && binding.m_factIndex == paramInt)
        return RU.getAtom(binding.m_name);
    }
    return null;
  }
}
