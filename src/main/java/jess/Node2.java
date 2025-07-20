package jess;

public class Node2 extends NodeTest {
  TokenTree m_left = new TokenTree();

  TokenTree m_right = new TokenTree();

  Node2(Rete paramRete) {
    super(paramRete);
  }

  void addTest(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.m_tests.addElement(new Test2(paramInt1, paramInt2, paramInt3, -1, paramInt4, -1));
  }

  void addTest(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
    Test2 test2;
    this.m_tests.addElement(test2 = new Test2(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6));
  }

  boolean callNode(Token paramToken, int paramInt) throws ReteException {
    if (paramToken.m_tag == 3) {
      this.m_left = new TokenTree();
      this.m_right = new TokenTree();
      passAlong(paramToken);
      return false;
    }
    switch (paramInt) {
      case 0:
        switch (paramToken.m_tag) {
          case 0:
          case 2:
            this.m_left.add(paramToken);
            runTestsVaryRight(paramToken, this.m_right.m_root);
            return true;
          case 1:
            this.m_left.remove(paramToken);
            runTestsVaryRight(paramToken, this.m_right.m_root);
            return true;
        }
        throw new ReteException("Node2::callNode", "Bad tag in token", String.valueOf(paramToken.m_tag));
      case 1:
        switch (paramToken.m_tag) {
          case 0:
          case 2:
            this.m_right.add(paramToken);
            runTestsVaryLeft(paramToken, this.m_left.m_root);
            return true;
          case 1:
            this.m_right.remove(paramToken);
            runTestsVaryLeft(paramToken, this.m_left.m_root);
            return true;
        }
        throw new ReteException("Node2::callNode", "Bad tag in token", String.valueOf(paramToken.m_tag));
    }
    throw new ReteException("Node2::callNode", "Bad callType", String.valueOf(paramInt));
  }

  void debugPrint(Token paramToken, int paramInt) throws ReteException {
    System.out.println("TEST " + toString() + ";calltype=" + paramInt + ";tag=" + paramToken.m_tag + ";class=" + paramToken.fact(0).get(0).stringValue());
  }

  void runTestsVaryRight(Token paramToken, TokenHolder paramTokenHolder) throws ReteException {
    if (paramTokenHolder == null)
      return;
    TokenHolder tokenHolder1;
    if ((tokenHolder1 = paramTokenHolder.m_left) != null)
      runTestsVaryRight(paramToken, tokenHolder1);
    TokenHolder tokenHolder2;
    if ((tokenHolder2 = paramTokenHolder.m_right) != null)
      runTestsVaryRight(paramToken, tokenHolder2);
    for (Token token = paramTokenHolder.m_token; token != null; token = token.m_next) {
      Token token1 = new Token(paramToken, token.fact(0));
      if (runTests(paramToken, token, token1))
        passAlong(token1);
    }
  }

  void runTestsVaryLeft(Token paramToken, TokenHolder paramTokenHolder) throws ReteException {
    if (paramTokenHolder == null)
      return;
    if (paramTokenHolder.m_left != null)
      runTestsVaryLeft(paramToken, paramTokenHolder.m_left);
    if (paramTokenHolder.m_right != null)
      runTestsVaryLeft(paramToken, paramTokenHolder.m_right);
    for (Token token = paramTokenHolder.m_token; token != null; token = token.m_next) {
      Token token1 = new Token(token, paramToken.fact(0));
      if (runTests(token, paramToken, token1)) {
        token1.m_tag = paramToken.m_tag;
        passAlong(token1);
      }
    }
  }

  boolean runTests(Token paramToken1, Token paramToken2, Token paramToken3) throws ReteException {
    int i = this.m_localTests.length;
    ValueVector valueVector = paramToken2.fact(0);
    EvalCache evalCache = this.m_cache;
    for (byte b = 0; b < i; b++) {
      Object object = this.m_localTests[b];
      if (object instanceof Test2) {
        Test2 test2 = (Test2)object;
        ValueVector valueVector1 = paramToken1.fact(test2.m_tokenIdx);
        if (valueVector1 != null) {
          Value value1;
          Value value2;
          int j = test2.m_leftSubIdx;
          int k = test2.m_rightSubIdx;
          if (j != -1) {
            value1 = valueVector1.get(test2.m_leftIdx).listValue().get(j);
          } else {
            value1 = valueVector1.get(test2.m_leftIdx);
          }
          if (k != -1) {
            value2 = valueVector.get(test2.m_rightIdx).listValue().get(k);
          } else {
            value2 = valueVector.get(test2.m_rightIdx);
          }
          boolean bool = value1.equals(value2);
          int m = test2.m_test;
          if (m == 0) {
            if (!bool)
              return false;
          } else if (m == 1) {
            if (bool)
              return false;
          } else {
            throw new ReteException("Node2::runTests", "Test2 type not supported", String.valueOf(test2.m_test));
          }
        }
      } else {
        int j;
        int k;
        Test1 test1 = (Test1)object;
        Value value = test1.m_slotValue;
        switch (test1.m_test) {
          case 0:
            j = 0;
            k = 0;
            try {
              j = evalCache.markFuncall();
              k = evalCache.markValue();
              if (Funcall.execute(eval(value, paramToken3), this.m_engine.globalContext(), this.m_cache).equals(Funcall.FALSE()))
                return false;
            } finally {
              evalCache.restoreFuncall(j);
              evalCache.restoreValue(k);
            }
            break;
          case 1:
            j = 0;
            k = 0;
            try {
              j = evalCache.markFuncall();
              k = evalCache.markValue();
              if (!Funcall.execute(eval(value, paramToken3), this.m_engine.globalContext(), this.m_cache).equals(Funcall.FALSE()))
                return false;
            } finally {
              evalCache.restoreFuncall(j);
              evalCache.restoreValue(k);
            }
            break;
          default:
            throw new ReteException("Node2::runTests", "Test1 type not supported", String.valueOf(test1.m_test));
        }
      }
    }
    return true;
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(256);
    stringBuffer.append("[Node2 ntests=");
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

  public String displayMemory() {
    StringBuffer stringBuffer = new StringBuffer(256);
    stringBuffer.append("\n\nLeft Memory:\n\n");
    int i = showMemory(this.m_left.m_root, stringBuffer);
    stringBuffer.append("\n");
    stringBuffer.append(i);
    stringBuffer.append(" entries");
    stringBuffer.append("\n\nRight Memory:\n\n");
    i = showMemory(this.m_right.m_root, stringBuffer);
    stringBuffer.append("\n");
    stringBuffer.append(i);
    stringBuffer.append(" entries");
    return stringBuffer.toString();
  }

  private int showMemory(TokenHolder paramTokenHolder, StringBuffer paramStringBuffer) {
    if (paramTokenHolder == null)
      return 0;
    int i = 0;
    i += showMemory(paramTokenHolder.m_left, paramStringBuffer);
    if (paramTokenHolder.m_token != null)
      for (Token token = paramTokenHolder.m_token; token != null; token = token.m_next) {
        i++;
        paramStringBuffer.append(token);
        paramStringBuffer.append('\n');
      }
    i += showMemory(paramTokenHolder.m_right, paramStringBuffer);
    return i;
  }
}
