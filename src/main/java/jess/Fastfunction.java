package jess;

abstract class Fastfunction implements Userfunction {
  public abstract Value call(ValueVector paramValueVector, Context paramContext, Value paramValue) throws ReteException;

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    return call(paramValueVector, paramContext, new Value(0, 4));
  }

  public abstract int name();
}
