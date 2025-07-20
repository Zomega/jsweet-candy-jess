package jess;

class ValueStack {
  private Value[] m_v = new Value[30];

  private int m_ptr = 0;

  Value push(Value paramValue) {
    if (this.m_ptr >= this.m_v.length) {
      Value[] arrayOfValue = new Value[this.m_v.length * 2];
      System.arraycopy(this.m_v, 0, arrayOfValue, 0, this.m_v.length);
      this.m_v = arrayOfValue;
    }
    this.m_v[this.m_ptr++] = paramValue;
    return paramValue;
  }

  Value pop() throws ReteException {
    return (this.m_ptr > 0) ? this.m_v[--this.m_ptr] : new Value(0, 4);
  }

  int size() {
    return this.m_ptr;
  }
}
