package jess;

public abstract class Node1 extends Node {
  static final int TEQ = 1;

  static final int TECT = 2;

  static final int TEV1 = 3;

  static final int TNEV1 = 4;

  static final int TNEQ = 5;

  static final int TELN = 6;

  static final int TMF = 7;

  static final int MTEQ = 20;

  static final int MTNEQ = 21;

  static final int MTELN = 22;

  static final int MTMF = 23;

  static final int NONE = 30;

  int R1;

  int R2;

  int R3;

  int R4;

  Value m_value;

  Node1(
      int paramInt1,
      int paramInt2,
      int paramInt3,
      int paramInt4,
      int paramInt5,
      Value paramValue,
      Rete paramRete) {
    super(paramRete);
    this.m_command = paramInt1;
    this.R1 = paramInt2;
    this.R2 = paramInt3;
    this.R3 = paramInt4;
    this.R4 = paramInt5;
    this.m_value = paramValue;
  }

  boolean callNode(Token paramToken, int paramInt) throws ReteException {
    if (paramToken.m_tag == 3) {
      passAlong(paramToken);
      return true;
    }
    return false;
  }

  void debugPrint(
      Token paramToken, int paramInt, ValueVector paramValueVector, boolean paramBoolean)
      throws ReteException {
    System.out.print("TEST " + toString() + ";ct=" + paramInt);
    System.out.println(
        ";id="
            + paramValueVector.get(2).factIDValue()
            + ";tag="
            + paramToken.m_tag
            + ";"
            + paramBoolean);
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append("[Node1 command=");
    switch (this.m_command) {
      case 1:
        stringBuffer.append("TEQ;data=");
        stringBuffer.append(this.m_value);
        stringBuffer.append(";type=");
        stringBuffer.append(this.R2);
        stringBuffer.append(";idx=");
        stringBuffer.append(this.R3);
        break;
      case 20:
        stringBuffer.append("MTEQ;data=");
        stringBuffer.append(this.m_value);
        stringBuffer.append(";type=");
        stringBuffer.append(this.R2);
        stringBuffer.append(";idx=");
        stringBuffer.append(this.R3);
        stringBuffer.append(";subidx=");
        stringBuffer.append(this.R4);
        break;
      case 7:
        stringBuffer.append("TMF");
        break;
      case 23:
        stringBuffer.append("MTMF");
        break;
      case 5:
        stringBuffer.append("TNEQ;data=");
        stringBuffer.append(this.m_value);
        stringBuffer.append(";type=");
        stringBuffer.append(this.R2);
        stringBuffer.append(";idx=");
        stringBuffer.append(this.R3);
        break;
      case 21:
        stringBuffer.append("MTNEQ;data=");
        stringBuffer.append(this.m_value);
        stringBuffer.append(";type=");
        stringBuffer.append(this.R2);
        stringBuffer.append(";idx=");
        stringBuffer.append(this.R3);
        stringBuffer.append(";subidx=");
        stringBuffer.append(this.R4);
        break;
      case 2:
        stringBuffer.append("TECT;class=");
        stringBuffer.append(RU.getAtom(this.R1));
        stringBuffer.append(";ordr=");
        stringBuffer.append(this.R2);
        break;
      case 6:
        stringBuffer.append("TELN;length=");
        stringBuffer.append(this.R1);
        break;
      case 22:
        stringBuffer.append("MTELN;idx=");
        stringBuffer.append(this.R1);
        stringBuffer.append(";length=");
        stringBuffer.append(this.R2);
        break;
      case 3:
        stringBuffer.append("TEV1;idx1=");
        stringBuffer.append(this.R1);
        stringBuffer.append(";idx2=");
        stringBuffer.append(this.R2);
        break;
      case 4:
        stringBuffer.append("TNEV1;idx1=");
        stringBuffer.append(this.R1);
        stringBuffer.append(";idx2=");
        stringBuffer.append(this.R2);
        break;
      case 30:
        stringBuffer.append("NONE (dummy node)");
        break;
    }
    stringBuffer.append(";usecount = ");
    stringBuffer.append(this.m_usecount);
    stringBuffer.append("]");
    return stringBuffer.toString();
  }

  static final Node1 create(
      int paramInt1,
      int paramInt2,
      int paramInt3,
      int paramInt4,
      int paramInt5,
      Value paramValue,
      Rete paramRete)
      throws ReteException {
    switch (paramInt1) {
      case 7:
        return new Node1TMF(paramInt2, paramInt3, paramInt4, paramInt5, paramValue, paramRete);
      case 23:
        return new Node1MTMF(paramInt2, paramInt3, paramInt4, paramInt5, paramValue, paramRete);
      case 1:
        return new Node1TEQ(paramInt2, paramInt3, paramInt4, paramInt5, paramValue, paramRete);
      case 20:
        return new Node1MTEQ(paramInt2, paramInt3, paramInt4, paramInt5, paramValue, paramRete);
      case 21:
        return new Node1MTNEQ(paramInt2, paramInt3, paramInt4, paramInt5, paramValue, paramRete);
      case 3:
        return new Node1TEV1(paramInt2, paramInt3, paramInt4, paramInt5, paramValue, paramRete);
      case 4:
        return new Node1TNEV1(paramInt2, paramInt3, paramInt4, paramInt5, paramValue, paramRete);
      case 5:
        return new Node1TNEQ(paramInt2, paramInt3, paramInt4, paramInt5, paramValue, paramRete);
      case 6:
        return new Node1TELN(paramInt2, paramInt3, paramInt4, paramInt5, paramValue, paramRete);
      case 22:
        return new Node1MTELN(paramInt2, paramInt3, paramInt4, paramInt5, paramValue, paramRete);
      case 2:
        return new Node1TECT(paramInt2, paramInt3, paramInt4, paramInt5, paramValue, paramRete);
      case 30:
        return new Node1NONE(paramInt2, paramInt3, paramInt4, paramInt5, paramValue, paramRete);
    }
    throw new ReteException("Node1::create", "invalid command code:", String.valueOf(paramInt1));
  }

  static final Node1 create(
      int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Rete paramRete)
      throws ReteException {
    return create(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, null, paramRete);
  }

  static final Node1 create(
      int paramInt1, Value paramValue, int paramInt2, int paramInt3, Rete paramRete)
      throws ReteException {
    return create(paramInt1, 0, 0, paramInt2, paramInt3, paramValue, paramRete);
  }
}
