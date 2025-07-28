package jess.reflect;

import java.lang.reflect.Field;
import jess.Context;
import jess.RU;
import jess.ReteException;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;

class _field implements Userfunction {
  private int m_name;

  public int name() {
    return this.m_name;
  }

  _field(String paramString) {
    this.m_name = RU.putAtom(paramString);
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    String str = paramValueVector.get(2).stringValue();
    boolean bool = false;
    if (paramValueVector.get(0).stringValue().equals("set-member")) bool = true;
    Class clazz = null;
    Object object = null;
    Value value = paramValueVector.get(1);
    if (value.type() == 2 || value.type() == 1)
      try {
        clazz = Class.forName(value.stringValue());
      } catch (ClassNotFoundException classNotFoundException) {
        throw new ReteException(
            paramValueVector.get(0).stringValue(), "No such class", value.stringValue());
      }
    if (clazz == null) {
      object = value.externalAddressValue();
      clazz = object.getClass();
    }
    try {
      Field field = clazz.getField(paramValueVector.get(2).stringValue());
      Class clazz1 = field.getType();
      if (bool) {
        field.set(object, ReflectFunctions.valueToObject(clazz1, paramValueVector.get(3)));
        return paramValueVector.get(3);
      }
      Object object1 = field.get(object);
      return ReflectFunctions.objectToValue(clazz1, object1);
    } catch (NoSuchFieldException noSuchFieldException) {
      throw new ReteException(
          paramValueVector.get(0).stringValue(),
          "No such field " + paramValueVector.get(2).stringValue() + " in class ",
          clazz.getName());
    } catch (IllegalAccessException illegalAccessException) {
      throw new ReteException(
          paramValueVector.get(0).stringValue(),
          "Field is not accessible",
          paramValueVector.get(2).stringValue());
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new ReteException(
          paramValueVector.get(0).stringValue(),
          "Invalid argument",
          paramValueVector.get(1).toString());
    }
  }
}
