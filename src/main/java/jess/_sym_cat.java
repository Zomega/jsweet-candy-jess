package jess;

class _sym_cat extends Fastfunction {
  public int name() {
    return RU.putAtom("sym-cat");
  }

  public Value call(ValueVector paramValueVector, Context paramContext, Value paramValue) throws ReteException {
    StringBuffer stringBuffer = new StringBuffer("");
    for (byte b = 1; b < paramValueVector.size(); b++) {
      Value value = paramValueVector.get(b);
      if (value.type() == 2) {
        stringBuffer.append(value.stringValue());
      } else {
        stringBuffer.append(value.toString());
      }
    }
    return paramValue.resetValue(stringBuffer.toString(), 1);
  }
}
