package jess;

public class NodeNot2 extends Node2 {
  private static Token s_nullToken;

  NodeNot2(Rete paramRete) {
    super(paramRete);
  }

  boolean callNode(Token paramToken, int paramInt) throws ReteException {
    Token token = new Token(paramToken);
    return super.callNode(token, paramInt);
  }

  void runTestsVaryRight(Token paramToken, TokenHolder paramTokenHolder) throws ReteException {
    doRunTestsVaryRight(paramToken, paramTokenHolder);
    if (paramToken.m_negcnt == 0) {
      Token token = new Token(paramToken, s_nullToken.fact(0));
      passAlong(token);
    }
  }

  private void doRunTestsVaryRight(Token paramToken, TokenHolder paramTokenHolder)
      throws ReteException {
    if (paramTokenHolder == null) return;
    doRunTestsVaryRight(paramToken, paramTokenHolder.m_left);
    doRunTestsVaryRight(paramToken, paramTokenHolder.m_right);
    for (Token token = paramTokenHolder.m_token; token != null; token = token.m_next) {
      Token token1 = new Token(paramToken, token.fact(0));
      if (runTests(paramToken, token, token1)) paramToken.m_negcnt++;
    }
  }

  void runTestsVaryLeft(Token paramToken, TokenHolder paramTokenHolder) throws ReteException {
    if (paramTokenHolder == null) return;
    runTestsVaryLeft(paramToken, paramTokenHolder.m_left);
    runTestsVaryLeft(paramToken, paramTokenHolder.m_right);
    for (Token token = paramTokenHolder.m_token; token != null; token = token.m_next) {
      Token token1 = new Token(token, paramToken.fact(0));
      if (runTests(token, paramToken, token1))
        if (paramToken.m_tag == 0 || paramToken.m_tag == 2) {
          Token token2 = new Token(token, s_nullToken.fact(0));
          token2.m_tag = 1;
          passAlong(token2);
          token.m_negcnt++;
        } else if (--token.m_negcnt == 0) {
          Token token2 = new Token(token, s_nullToken.fact(0));
          passAlong(token2);
        } else if (token.m_negcnt < 0) {
          throw new ReteException("NodeNot2::RunTestsVaryLeft", "Corrupted Negcnt (< 0)", "");
        }
    }
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(256);
    stringBuffer.append("[NodeNot2 ntests=");
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

  static {
    try {
      ValueVector valueVector = new ValueVector();
      valueVector.set(new Value("$NOT-CE$", 1), 0);
      valueVector.set(new Value(128, 1024), 1);
      valueVector.set(new Value(-1, 16), 2);
      s_nullToken = new Token(0, valueVector);
    } catch (ReteException reteException) {
    }
  }
}
