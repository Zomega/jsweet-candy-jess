package jess;

class EvalCache {
  private ValueStack m_usedV = new ValueStack();

  private ValueStack m_newV = new ValueStack();

  private FuncallStack m_usedF = new FuncallStack();

  private FuncallStack m_newF = new FuncallStack();

  final synchronized Value getValue() throws ReteException {
    return this.m_usedV.push(this.m_newV.pop());
  }

  final int markValue() {
    return this.m_usedV.size();
  }

  final synchronized void restoreValue(int paramInt) throws ReteException {
    while (this.m_usedV.size() > paramInt) this.m_newV.push(this.m_usedV.pop());
  }

  final synchronized Funcall getFuncall() throws ReteException {
    return this.m_usedF.push(this.m_newF.pop());
  }

  final int markFuncall() {
    return this.m_usedF.size();
  }

  final synchronized void restoreFuncall(int paramInt) throws ReteException {
    while (this.m_usedF.size() > paramInt) this.m_newF.push(this.m_usedF.pop());
  }
}
