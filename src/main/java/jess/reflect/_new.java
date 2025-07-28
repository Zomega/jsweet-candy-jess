package jess.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import jess.Context;
import jess.RU;
import jess.ReteException;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;

class _new implements Userfunction {
  private int m_name = RU.putAtom("new");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    String str = paramValueVector.get(1).stringValue();
    try {
      int i = paramValueVector.size() - 2;
      Object[] arrayOfObject = new Object[i];
      Class clazz = Class.forName(str);
      Constructor[] arrayOfConstructor = (Constructor[]) clazz.getConstructors();
      Object object = null;
      for (byte b = 0; b < arrayOfConstructor.length; b++) {
        try {
          Constructor constructor = arrayOfConstructor[b];
          Class[] arrayOfClass = constructor.getParameterTypes();
          if (i == arrayOfClass.length) {
            for (byte b1 = 0; b1 < i; b1++)
              arrayOfObject[b1] =
                  ReflectFunctions.valueToObject(arrayOfClass[b1], paramValueVector.get(b1 + 2));
            object = constructor.newInstance(arrayOfObject);
            return new Value(object, 2048);
          }
        } catch (IllegalArgumentException illegalArgumentException) {
        }
      }
      throw new NoSuchMethodException(str);
    } catch (InvocationTargetException invocationTargetException) {
      throw new ReteException(
          "new",
          "Constructor threw an exception",
          invocationTargetException.getTargetException().toString());
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new ReteException("new", "Constructor not found", str);
    } catch (ClassNotFoundException classNotFoundException) {
      throw new ReteException("new", "Class not found", str);
    } catch (IllegalAccessException illegalAccessException) {
      throw new ReteException("new", "Class or constructor is not accessible", str);
    } catch (InstantiationException instantiationException) {
      throw new ReteException("new", "Class cannot be instantiated", str);
    }
  }
}
