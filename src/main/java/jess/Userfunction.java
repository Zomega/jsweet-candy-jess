package jess;

public interface Userfunction {
  int name();

  Value call(ValueVector paramValueVector, Context paramContext) throws ReteException;
}
