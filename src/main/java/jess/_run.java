package jess;

class _run implements Userfunction {
  public int name() {
    return RU.putAtom("run");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    if (paramValueVector.size() == 1) {
      paramContext.engine().run();
    } else {
      paramContext.engine().run(paramValueVector.get(1).intValue());
    }
    return Funcall.s_true;
  }
}
