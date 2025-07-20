package jess;

public final class Value {
  private final int STRING_TYPES = 57355;

  private final int NUM_TYPES = 36;

  private final int FACT_TYPES = 52;

  private int m_type;

  private int m_intval;

  private double m_floatval;

  private Object m_objectval;

  final int ITYPES = 58399;

  final int VTYPES = 960;

  public Value(int paramInt1, int paramInt2) throws ReteException {
    resetValue(paramInt1, paramInt2);
  }

  public Value(Value paramValue) {
    resetValue(paramValue);
  }

  public Value(String paramString, int paramInt) throws ReteException {
    resetValue(paramString, paramInt);
  }

  public Value(ValueVector paramValueVector, int paramInt) throws ReteException {
    resetValue(paramValueVector, paramInt);
  }

  public Value(double paramDouble, int paramInt) throws ReteException {
    resetValue(paramDouble, paramInt);
  }

  public Value(Object paramObject, int paramInt) throws ReteException {
    resetValue(paramObject, paramInt);
  }

  public Value(int[] paramArrayOfint, int paramInt) throws ReteException {
    resetValue(paramArrayOfint, paramInt);
  }

  public int[] intArrayValue() throws ReteException {
    if (this.m_type == 4096)
      return (int[])this.m_objectval;
    throw new ReteException("Value::intArrayValue", "Not an int[]: " + toString(), "type = " + this.m_type);
  }

  public final Object externalAddressValue() throws ReteException {
    switch (this.m_type) {
      case 2048:
        return this.m_objectval;
      case 1:
      case 2:
        return stringValue().intern();
    }
    throw new ReteException("Value::externalAddressValue", "Not an external address: " + toString(), "type = " + this.m_type);
  }

  public final Funcall funcallValue() throws ReteException {
    if (this.m_type == 64)
      return (Funcall)this.m_objectval;
    throw new ReteException("Value::funcallValue", "Not a Funcall: " + toString(), "type = " + this.m_type);
  }

  public final ValueVector factValue() throws ReteException {
    if (this.m_type == 128 || this.m_type == 256)
      return (ValueVector)this.m_objectval;
    throw new ReteException("Value::factValue", "Not a Fact: " + toString(), "type = " + this.m_type);
  }

  public final ValueVector listValue() throws ReteException {
    if (this.m_type == 512)
      return (ValueVector)this.m_objectval;
    throw new ReteException("Value::listValue", "Not a List: " + toString(), "type = " + this.m_type);
  }

  public final double numericValue() throws ReteException {
    if (this.m_type == 32)
      return this.m_floatval;
    if (this.m_type == 4)
      return this.m_intval;
    throw new ReteException("Value::numericValue", "Not a number: " + toString(), "type = " + this.m_type);
  }

  public final int descriptorValue() throws ReteException {
    if (this.m_type == 1024)
      return this.m_intval;
    throw new ReteException("Value::descriptorValue", "Not a descriptor: " + toString(), "type = " + this.m_type);
  }

  public final int intValue() throws ReteException {
    if (this.m_type == 4)
      return this.m_intval;
    if (this.m_type == 32)
      return (int)this.m_floatval;
    throw new ReteException("Value::intValue", "Not a number: " + toString(), "type = " + this.m_type);
  }

  public final double floatValue() throws ReteException {
    return numericValue();
  }

  public final String stringValue() throws ReteException {
    if ((this.m_type & 0xE00B) != 0)
      return RU.getAtom(this.m_intval);
    throw new ReteException("Value::stringValue", "Not a string: " + toString(), "type = " + this.m_type);
  }

  public final int atomValue() throws ReteException {
    if ((this.m_type & 0xE00B) != 0)
      return this.m_intval;
    throw new ReteException("Value::atomValue", "Not an atom: " + toString(), "type = " + this.m_type);
  }

  public final int variableValue() throws ReteException {
    if (this.m_type == 8 || this.m_type == 8192)
      return this.m_intval;
    throw new ReteException("Value::variableValue", "Not a Variable: " + toString(), "type = " + this.m_type);
  }

  public final int factIDValue() throws ReteException {
    if ((this.m_type & 0x34) != 0)
      return this.m_intval;
    throw new ReteException("Value::factIDValue", "Not a Fact-ID: " + toString(), "type = " + this.m_type);
  }

  private String escape(String paramString) {
    if (paramString.indexOf('"') == -1)
      return paramString;
    StringBuffer stringBuffer = new StringBuffer();
    for (byte b = 0; b < paramString.length(); b++) {
      char c = paramString.charAt(b);
      if (c == '"' || c == '\\')
        stringBuffer.append('\\');
      stringBuffer.append(c);
    }
    return stringBuffer.toString();
  }

  public final String toString() {
    int[] arrayOfInt;
    switch (this.m_type) {
      case 4:
        return "" + this.m_intval;
      case 32:
        return "" + this.m_floatval;
      case 2:
        return "\"" + escape(RU.getAtom(this.m_intval)) + "\"";
      case 1:
      case 16384:
      case 32768:
        return RU.getAtom(this.m_intval);
      case 8:
        return "?" + RU.getAtom(this.m_intval);
      case 8192:
        return "$?" + RU.getAtom(this.m_intval);
      case 16:
        return "<Fact-" + this.m_intval + ">";
      case 1024:
        return (this.m_intval == 128) ? "<ordered>" : "<unordered>";
      case 64:
      case 128:
      case 256:
      case 512:
        return this.m_objectval.toString();
      case 4096:
        arrayOfInt = (int[])this.m_objectval;
        return "?" + arrayOfInt[0] + "," + arrayOfInt[1] + "," + arrayOfInt[2];
      case 2048:
        return "<External-Address:" + this.m_objectval.getClass().getName() + ">";
      case 0:
        return Funcall.NIL().toString();
    }
    return "<UNKNOWN>";
  }

  public final int sortCode() {
    switch (this.m_type) {
      case 1:
      case 2:
      case 4:
      case 8:
      case 16:
      case 1024:
      case 8192:
      case 16384:
      case 32768:
        return this.m_intval;
      case 32:
        return (int)this.m_floatval;
      case 64:
      case 128:
      case 256:
        return (((ValueVector)this.m_objectval).get(0)).m_intval;
      case 512:
        return ((ValueVector)this.m_objectval).get(0).sortCode();
    }
    return 0;
  }

  public final int type() {
    return this.m_type;
  }

  public final boolean equals(Object paramObject) {
    return (paramObject instanceof Value) ? equals((Value)paramObject) : false;
  }

  public final boolean equals(Value paramValue) {
    if (this == paramValue)
      return true;
    int i = this.m_type;
    return (paramValue.m_type != i) ? false : (((i & 0xE41F) != 0) ? ((this.m_intval == paramValue.m_intval)) : ((i == 32) ? ((this.m_floatval == paramValue.m_floatval)) : this.m_objectval.equals(paramValue.m_objectval)));
  }

  public final boolean equalsStar(Value paramValue) throws ReteException {
    return (this == paramValue) ? true : (((this.m_type & 0x24) != 0 && (paramValue.m_type & 0x24) != 0) ? ((numericValue() == paramValue.numericValue())) : equals(paramValue));
  }

  final Value resetValue(int paramInt1, int paramInt2) throws ReteException {
    this.m_type = paramInt2;
    switch (this.m_type) {
      case 0:
      case 1:
      case 2:
      case 4:
      case 8:
      case 16:
      case 1024:
      case 8192:
      case 16384:
      case 32768:
        this.m_intval = paramInt1;
        return this;
    }
    throw new ReteException("Value::resetValue", "Not an integral type", "type = " + this.m_type);
  }

  final Value resetValue(Value paramValue) {
    this.m_type = paramValue.m_type;
    this.m_intval = paramValue.m_intval;
    this.m_floatval = paramValue.m_floatval;
    this.m_objectval = paramValue.m_objectval;
    return this;
  }

  final Value resetValue(String paramString, int paramInt) throws ReteException {
    if ((paramInt & 0xE00B) == 0)
      throw new ReteException("Value::Value", "not a string type", "type = " + this.m_type);
    this.m_type = paramInt;
    this.m_intval = RU.putAtom(paramString);
    return this;
  }

  final Value resetValue(ValueVector paramValueVector, int paramInt) throws ReteException {
    if (paramInt != 64 && paramInt != 128 && paramInt != 256 && paramInt != 512)
      throw new ReteException("Value::Value", "not a vector type", "type = " + this.m_type);
    this.m_type = paramInt;
    this.m_objectval = paramValueVector;
    return this;
  }

  final Value resetValue(double paramDouble, int paramInt) throws ReteException {
    if (paramInt != 32 && paramInt != 4 && paramInt != 16)
      throw new ReteException("Value::Value", "not a float type:", "type = " + this.m_type);
    this.m_type = paramInt;
    if (paramInt == 32) {
      this.m_floatval = paramDouble;
    } else {
      this.m_intval = (int)paramDouble;
    }
    return this;
  }

  final Value resetValue(Object paramObject, int paramInt) throws ReteException {
    if (paramInt != 2048)
      throw new ReteException("Value::Value", "Not an External Address type", "type = " + this.m_type);
    this.m_type = paramInt;
    this.m_objectval = paramObject;
    return this;
  }

  final Value resetValue(int[] paramArrayOfint, int paramInt) throws ReteException {
    if (paramInt != 4096)
      throw new ReteException("Value::Value", "Not an int[] type", "type = " + this.m_type);
    this.m_type = paramInt;
    this.m_objectval = paramArrayOfint;
    return this;
  }
}
