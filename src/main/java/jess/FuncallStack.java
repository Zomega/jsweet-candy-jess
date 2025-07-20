package jess;

class FuncallStack {
  private Funcall[] m_v = new Funcall[30];

  private int m_ptr = 0;

  Funcall push(Funcall paramFuncall) {
    if (this.m_ptr >= this.m_v.length) {
      Funcall[] arrayOfFuncall = new Funcall[this.m_v.length * 2];
      System.arraycopy(this.m_v, 0, arrayOfFuncall, 0, this.m_v.length);
      this.m_v = arrayOfFuncall;
    }
    this.m_v[this.m_ptr++] = paramFuncall;
    return paramFuncall;
  }

  Funcall pop() throws ReteException {
    return (this.m_ptr > 0) ? this.m_v[--this.m_ptr] : new Funcall(10);
  }

  int size() {
    return this.m_ptr;
  }
}
