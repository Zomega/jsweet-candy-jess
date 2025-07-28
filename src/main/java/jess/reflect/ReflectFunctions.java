package jess.reflect;

import java.lang.reflect.Array;
import jess.Funcall;
import jess.Rete;
import jess.ReteException;
import jess.Userpackage;
import jess.Value;
import jess.ValueVector;

public class ReflectFunctions implements Userpackage {
  public void add(Rete paramRete) {
    paramRete.addUserfunction(new _new());
    paramRete.addUserfunction(new _engine(paramRete));
    paramRete.addUserfunction(new _call());
    paramRete.addUserfunction(new _field("set-member"));
    paramRete.addUserfunction(new _field("get-member"));
    paramRete.addUserfunction(new _set());
    paramRete.addUserfunction(new _get());
    defclass defclass = new defclass();
    paramRete.addUserfunction(defclass);
    paramRete.addClearable(defclass);
    definstance definstance = new definstance(paramRete, defclass);
    paramRete.addUserfunction(definstance);
    paramRete.addClearable(definstance);
    paramRete.addResetable(definstance);
    paramRete.addUserfunction(new undefinstance(definstance));
  }

  static Object valueToObject(Class paramClass, Value paramValue)
      throws IllegalArgumentException, ReteException {
    String str;
    int i;
    double d;
    switch (paramValue.type()) {
      case 2048:
        if (paramClass.isInstance(paramValue.externalAddressValue()))
          return paramValue.externalAddressValue();
        throw new IllegalArgumentException();
      case 1:
      case 2:
        str = paramValue.stringValue();
        if (paramClass == String.class) return str;
        if (paramClass == char.class) {
          if (str.length() == 1) return new Character(str.charAt(0));
          throw new IllegalArgumentException();
        }
        if (paramClass == boolean.class) {
          if (str.equals("TRUE")) return Boolean.TRUE;
          if (str.equals("FALSE")) return Boolean.FALSE;
          throw new IllegalArgumentException();
        }
        if (!paramClass.isPrimitive() && str.equals("NIL")) return null;
        throw new IllegalArgumentException();
      case 4:
        i = paramValue.intValue();
        if (paramClass == long.class || paramClass == Long.class) return new Long(i);
        if (paramClass == int.class || paramClass == Integer.class) return new Integer(i);
        if (paramClass == short.class || paramClass == Short.class) return new Short((short) i);
        if (paramClass == char.class || paramClass == Character.class)
          return new Character((char) i);
        if (paramClass == byte.class || paramClass == Byte.class) return new Byte((byte) i);
        throw new IllegalArgumentException();
      case 32:
        d = paramValue.floatValue();
        if (paramClass == double.class || paramClass == Double.class) return new Double(d);
        if (paramClass == float.class || paramClass == Float.class) return new Float((float) d);
        throw new IllegalArgumentException();
      case 512:
        if (paramClass.isArray()) {
          Class clazz = paramClass.getComponentType();
          ValueVector valueVector = paramValue.listValue();
          Object object = Array.newInstance(clazz, valueVector.size());
          for (byte b = 0; b < valueVector.size(); b++)
            Array.set(object, b, valueToObject(clazz, valueVector.get(b)));
          return object;
        }
        throw new IllegalArgumentException();
    }
    throw new IllegalArgumentException();
  }

  static Value objectToValue(Class paramClass, Object paramObject) throws ReteException {
    if (paramObject == null) return Funcall.NIL();
    if (paramClass == Void.class) return Funcall.NIL();
    if (paramClass == String.class) return new Value(paramObject.toString(), 2);
    if (paramClass.isArray()) {
      int i = Array.getLength(paramObject);
      ValueVector valueVector = new ValueVector(i);
      for (byte b = 0; b < i; b++)
        valueVector.add(objectToValue(paramClass.getComponentType(), Array.get(paramObject, b)));
      return new Value(valueVector, 512);
    }
    return (paramClass == boolean.class)
        ? (((Boolean) paramObject).booleanValue() ? Funcall.TRUE() : Funcall.FALSE())
        : ((paramClass == byte.class
                || paramClass == short.class
                || paramClass == int.class
                || paramClass == long.class)
            ? new Value(((Number) paramObject).intValue(), 4)
            : ((paramClass == double.class || paramClass == float.class)
                ? new Value(((Number) paramObject).doubleValue(), 32)
                : ((paramClass == char.class)
                    ? new Value(paramObject.toString(), 1)
                    : new Value(paramObject, 2048))));
  }
}
