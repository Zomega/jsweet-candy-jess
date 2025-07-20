package jess;

class loadfn implements Userfunction {
  private int m_name = RU.putAtom("load-function");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    String str = paramValueVector.get(1).stringValue();
    try {
      Userfunction userfunction = (Userfunction)Class.forName(str).newInstance();
      paramContext.engine().addUserfunction(userfunction);
    } catch (ClassNotFoundException classNotFoundException) {
      throw new ReteException("load-function", "Class not found", str);
    } catch (IllegalAccessException illegalAccessException) {
      throw new ReteException("load-function", "Class is not accessible", str);
    } catch (InstantiationException instantiationException) {
      throw new ReteException("load-function", "Class cannot be instantiated", str);
    } catch (ClassCastException classCastException) {
      throw new ReteException("load-function", "Class must inherit from UserFunction", str);
    }
    return Funcall.TRUE();
  }
}
