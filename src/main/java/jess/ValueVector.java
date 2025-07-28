package jess;

public class ValueVector implements Cloneable {
  Value[] m_v;

  int m_ptr = 0;

  public ValueVector() {
    this(10);
  }

  public ValueVector(int paramInt) {
    this.m_v = new Value[paramInt];
  }

  public final int size() {
    return this.m_ptr;
  }

  public Object clone() {
    ValueVector valueVector = new ValueVector(this.m_ptr);
    valueVector.m_ptr = this.m_ptr;
    System.arraycopy(this.m_v, 0, valueVector.m_v, 0, this.m_ptr);
    return valueVector;
  }

  public final Value get(int paramInt) {
    return this.m_v[paramInt];
  }

  public final void setLength(int paramInt) {
    if (paramInt > this.m_v.length) {
      Value[] arrayOfValue = new Value[paramInt];
      System.arraycopy(this.m_v, 0, arrayOfValue, 0, this.m_v.length);
      this.m_v = arrayOfValue;
    }
    this.m_ptr = paramInt;
  }

  public final void set(Value paramValue, int paramInt) {
    this.m_v[paramInt] = paramValue;
  }

  public final void add(Value paramValue) {
    if (this.m_ptr >= this.m_v.length) {
      Value[] arrayOfValue = new Value[this.m_v.length * 2];
      System.arraycopy(this.m_v, 0, arrayOfValue, 0, this.m_v.length);
      this.m_v = arrayOfValue;
    }
    this.m_v[this.m_ptr++] = paramValue;
  }

  public boolean equals(Object paramObject) {
    if (this == paramObject) return true;
    if (!(paramObject instanceof ValueVector)) return false;
    ValueVector valueVector = (ValueVector) paramObject;
    if (this.m_ptr != valueVector.m_ptr) return false;
    for (int i = this.m_ptr - 1; i > -1; i--) {
      if (!this.m_v[i].equals(valueVector.m_v[i])) return false;
    }
    return true;
  }

  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    for (byte b = 0; b < this.m_ptr; b++) {
      if (b > 0) stringBuffer.append(" ");
      stringBuffer.append(this.m_v[b]);
    }
    return stringBuffer.toString();
  }
}
