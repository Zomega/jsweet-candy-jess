package jess;

import java.util.Enumeration;

class listfunctions implements Userfunction {
  private int m_name = RU.putAtom("list-function$");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    ValueVector valueVector = new ValueVector(100);
    Enumeration enumeration = Funcall.listIntrinsics();
    while (enumeration.hasMoreElements()) valueVector.add(new Value(enumeration.nextElement(), 1));
    enumeration = paramContext.engine().listDeffunctions();
    while (enumeration.hasMoreElements()) valueVector.add(new Value(enumeration.nextElement(), 1));
    enumeration = paramContext.engine().listUserfunctions();
    while (enumeration.hasMoreElements()) {
      String str = enumeration.nextElement();
      valueVector.add(new Value(str, 1));
    }
    while (true) {
      byte b1 = 0;
      for (byte b2 = 0; b2 < valueVector.size() - 1; b2++) {
        Value value1 = valueVector.get(b2);
        Value value2 = valueVector.get(b2 + 1);
        if (value1.stringValue().compareTo(value2.stringValue()) > 0) {
          b1++;
          valueVector.set(value2, b2);
          valueVector.set(value1, b2 + 1);
        }
      }
      if (b1 <= 0) return new Value(valueVector, 512);
    }
  }
}
