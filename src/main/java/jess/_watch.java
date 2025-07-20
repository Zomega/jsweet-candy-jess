package jess;

class _watch implements Userfunction {
  public int name() {
    return RU.putAtom("watch");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    String str = paramValueVector.get(1).stringValue();
    if (str.equals("rules")) {
      paramContext.engine().watchRules(true);
    } else if (str.equals("facts")) {
      paramContext.engine().watchFacts(true);
    } else if (str.equals("compilations")) {
      paramContext.engine().watchCompilations(true);
    } else if (str.equals("activations")) {
      paramContext.engine().watchActivations(true);
    } else if (str.equals("all")) {
      paramContext.engine().watchFacts(true);
      paramContext.engine().watchRules(true);
      paramContext.engine().watchCompilations(true);
      paramContext.engine().watchActivations(true);
    } else {
      throw new ReteException("Funcall::Execute", "watch: can't watch", str);
    }
    return Funcall.s_true;
  }
}
