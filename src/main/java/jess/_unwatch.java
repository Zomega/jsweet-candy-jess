package jess;

class _unwatch implements Userfunction {
  public int name() {
    return RU.putAtom("unwatch");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    String str = paramValueVector.get(1).stringValue();
    if (str.equals("rules")) {
      paramContext.engine().watchRules(false);
    } else if (str.equals("facts")) {
      paramContext.engine().watchFacts(false);
    } else if (str.equals("compilations")) {
      paramContext.engine().watchCompilations(false);
    } else if (str.equals("activations")) {
      paramContext.engine().watchActivations(false);
    } else if (str.equals("all")) {
      paramContext.engine().watchFacts(false);
      paramContext.engine().watchRules(false);
      paramContext.engine().watchCompilations(false);
      paramContext.engine().watchActivations(false);
    } else {
      throw new ReteException("Funcall::Execute", "unwatch: can't unwatch", str);
    }
    return Funcall.s_true;
  }
}
