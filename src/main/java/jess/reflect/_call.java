package jess.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import jess.Context;
import jess.RU;
import jess.ReteException;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;

class _call implements Userfunction {
  int m_name = RU.putAtom("call");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    String str = paramValueVector.get(2).stringValue();
    try {
      Class clazz = null;
      Object object1 = null;
      Value value = paramValueVector.get(1);
      if (value.type() == 2 || value.type() == 1)
        try {
          clazz = Class.forName(value.stringValue());
        } catch (ClassNotFoundException classNotFoundException) {}
      if (clazz == null) {
        object1 = value.externalAddressValue();
        clazz = object1.getClass();
      }
      int i = paramValueVector.size() - 3;
      Object[] arrayOfObject = new Object[i];
      Method[] arrayOfMethod = clazz.getMethods();
      Object object2 = null;
      for (byte b = 0; b < arrayOfMethod.length; b++) {
        try {
          Method method = arrayOfMethod[b];
          Class[] arrayOfClass = method.getParameterTypes();
          if (method.getName().equals(str) && i == arrayOfClass.length) {
            for (byte b1 = 0; b1 < i; b1++)
              arrayOfObject[b1] = ReflectFunctions.valueToObject(arrayOfClass[b1], paramValueVector.get(b1 + 3));
            object2 = method.invoke(object1, arrayOfObject);
            return ReflectFunctions.objectToValue(method.getReturnType(), object2);
          }
        } catch (IllegalArgumentException illegalArgumentException) {}
      }
      throw new NoSuchMethodException(str);
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new ReteException("call", "No method '" + str + "' found", "or invalid argument types");
    } catch (InvocationTargetException invocationTargetException) {
      throw new ReteException("call", "Called method threw an exception", invocationTargetException.getTargetException().toString());
    } catch (IllegalAccessException illegalAccessException) {
      throw new ReteException("call", "Method is not accessible", str);
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new ReteException("call", "Invalid argument to", str);
    }
  }
}
