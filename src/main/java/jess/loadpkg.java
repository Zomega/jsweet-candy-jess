package jess;

class loadpkg implements Userfunction {
  private int m_name = RU.putAtom("load-package");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    String str = paramValueVector.get(1).stringValue();
    try {
      Userpackage userpackage = (Userpackage) Class.forName(str).newInstance();
      paramContext.engine().addUserpackage(userpackage);
    } catch (ClassNotFoundException classNotFoundException) {
      throw new ReteException("load-package", "Class not found", str);
    } catch (IllegalAccessException illegalAccessException) {
      throw new ReteException("load-package", "Class is not accessible", str);
    } catch (InstantiationException instantiationException) {
      throw new ReteException("load-package", "Class cannot be instantiated", str);
    } catch (ClassCastException classCastException) {
      throw new ReteException("load-package", "Class must inherit from UserPackage", str);
    }
    return Funcall.TRUE();
  }
}
