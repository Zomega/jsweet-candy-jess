package jess;

import java.util.Vector;

public class NodeTest extends Node {
  Vector m_tests = new Vector();

  Object[] m_localTests;

  NodeTest(Rete paramRete) {
    super(paramRete);
  }

  void complete() {
    this.m_localTests = new Object[this.m_tests.size()];
    for (byte b = 0; b < this.m_tests.size(); b++) this.m_localTests[b] = this.m_tests.elementAt(b);
  }

  void addTest(int paramInt1, int paramInt2, Value paramValue) {
    this.m_tests.addElement(new Test1(paramInt1, paramInt2, -1, paramValue));
  }

  void addTest(int paramInt1, int paramInt2, int paramInt3, Value paramValue) {
    this.m_tests.addElement(new Test1(paramInt1, paramInt2, paramInt3, paramValue));
  }

  void addTest(int paramInt1, int paramInt2, int paramInt3, int paramInt4) throws ReteException {
    throw new ReteException("NodeTest:addtest", "Can't add Test2s to this class", "");
  }

  void addTest(
      int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
      throws ReteException {
    throw new ReteException("NodeTest:addtest", "Can't add Test2s to this class", "");
  }

  boolean equals(NodeTest paramNodeTest) {
    if (this == paramNodeTest) return true;
    if (getClass() != paramNodeTest.getClass()
        || this instanceof NodeNot2
        || paramNodeTest instanceof NodeNot2
        || paramNodeTest.m_tests.size() != this.m_tests.size()) return false;
    for (byte b = 0; b < this.m_tests.size(); b++) {
      if (this.m_tests.elementAt(b) instanceof Test1) return false;
      Test2 test2 = this.m_tests.elementAt(b);
      for (byte b1 = 0; ; b1++) {
        if (b1 >= this.m_tests.size()) return false;
        if (test2.equals(paramNodeTest.m_tests.elementAt(b1))) break;
      }
    }
    return true;
  }

  boolean callNode(Token paramToken, int paramInt) throws ReteException {
    if (paramToken.m_tag == 3) {
      passAlong(paramToken);
      return true;
    }
    boolean bool = runTests(paramToken);
    if (bool)
      for (Successor successor : this.m_localSucc)
        successor.m_node.callNode(paramToken, successor.m_callType);
    return bool;
  }

  boolean runTests(Token paramToken) throws ReteException {
    int i = this.m_localTests.length;
    for (byte b = 0; b < i; b++) {
      Test1 test1;
      int j;
      int k;
      try {
        test1 = (Test1) this.m_localTests[b];
      } catch (ClassCastException classCastException) {
        throw new ReteException(
            "NodeTest:callNode", "Bad test type!", this.m_localTests[b].getClass().getName());
      }
      Value value = test1.m_slotValue;
      switch (test1.m_test) {
        case 0:
          j = 0;
          k = 0;
          try {
            j = this.m_cache.markFuncall();
            k = this.m_cache.markValue();
            if (Funcall.execute(
                    eval(value, paramToken), this.m_engine.globalContext(), this.m_cache)
                .equals(Funcall.FALSE())) return false;
          } finally {
            this.m_cache.restoreFuncall(j);
            this.m_cache.restoreValue(k);
          }
          break;
        case 1:
          j = 0;
          k = 0;
          try {
            j = this.m_cache.markFuncall();
            k = this.m_cache.markValue();
            if (!Funcall.execute(
                    eval(value, paramToken), this.m_engine.globalContext(), this.m_cache)
                .equals(Funcall.FALSE())) return false;
          } finally {
            this.m_cache.restoreFuncall(j);
            this.m_cache.restoreValue(k);
          }
          break;
        default:
          throw new ReteException(
              "NodeTest::runTests", "Test type not supported", String.valueOf(test1.m_test));
      }
    }
    return true;
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(256);
    stringBuffer.append("[NodeTest ntests=");
    stringBuffer.append(this.m_tests.size());
    stringBuffer.append(" ");
    for (byte b = 0; b < this.m_tests.size(); b++) {
      stringBuffer.append(this.m_tests.elementAt(b).toString());
      stringBuffer.append(" ");
    }
    stringBuffer.append(";usecount = ");
    stringBuffer.append(this.m_usecount);
    stringBuffer.append("]");
    return stringBuffer.toString();
  }
}
