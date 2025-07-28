package jess;

class Token {
  int m_tag;

  int m_negcnt;

  int m_sortcode;

  private ValueVector m_fact;

  private int m_size;

  private Token m_parent;

  Token m_next;

  final ValueVector fact(int paramInt) {
    Token token = this;
    int i = this.m_size - paramInt;
    while (--i > 0) token = token.m_parent;
    return token.m_fact;
  }

  final int size() {
    return this.m_size;
  }

  Token(int paramInt, ValueVector paramValueVector) throws ReteException {
    this.m_size++;
    this.m_fact = paramValueVector;
    this.m_tag = paramInt;
    this.m_sortcode = paramValueVector.get(2).factIDValue();
  }

  Token() {}

  Token resetToken(int paramInt, ValueVector paramValueVector) throws ReteException {
    this.m_parent = null;
    this.m_size = 1;
    this.m_fact = paramValueVector;
    this.m_tag = paramInt;
    this.m_negcnt = 0;
    this.m_sortcode = paramValueVector.get(2).factIDValue();
    return this;
  }

  Token(Token paramToken, ValueVector paramValueVector) throws ReteException {
    this.m_fact = paramValueVector;
    this.m_parent = paramToken;
    this.m_tag = paramToken.m_tag;
    paramToken.m_size++;
    this.m_sortcode = (paramToken.m_sortcode << 3) + paramValueVector.get(2).factIDValue();
  }

  Token(Token paramToken) throws ReteException {
    this.m_fact = paramToken.m_fact;
    this.m_parent = paramToken.m_parent;
    this.m_tag = paramToken.m_tag;
    this.m_negcnt = 0;
    this.m_size = paramToken.m_size;
    this.m_sortcode = paramToken.m_sortcode;
    this.m_next = null;
  }

  public final boolean dataEquals(Token paramToken) {
    return (this.m_sortcode != paramToken.m_sortcode)
        ? false
        : (!this.m_fact.equals(paramToken.m_fact)
            ? false
            : ((this.m_parent == null || this.m_parent == paramToken.m_parent)
                ? true
                : this.m_parent.dataEquals(paramToken.m_parent)));
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append("[Token: size=");
    stringBuffer.append(this.m_size);
    stringBuffer.append(";sortcode=");
    stringBuffer.append(this.m_sortcode);
    stringBuffer.append(";tag=");
    stringBuffer.append((this.m_tag == 0) ? "ADD" : ((this.m_tag == 2) ? "UPDATE" : "REMOVE"));
    stringBuffer.append(";negcnt=");
    stringBuffer.append(this.m_negcnt);
    stringBuffer.append(";facts=");
    for (byte b = 0; b < this.m_size; b++) {
      stringBuffer.append(fact(b));
      stringBuffer.append(";");
    }
    stringBuffer.append("]");
    return stringBuffer.toString();
  }
}
