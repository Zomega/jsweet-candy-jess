package jess.reflect;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import jess.Context;
import jess.RU;
import jess.ReteException;
import jess.Value;
import jess.ValueVector;

class _set extends _call {
  _set() {
    this.m_name = RU.putAtom("set");
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    try {
      String str = paramValueVector.get(2).stringValue();
      PropertyDescriptor[] arrayOfPropertyDescriptor =
          Introspector.getBeanInfo(paramValueVector.get(1).externalAddressValue().getClass())
              .getPropertyDescriptors();
      for (byte b = 0; b < arrayOfPropertyDescriptor.length; b++) {
        Method method = null;
        if (arrayOfPropertyDescriptor[b].getName().equals(str)
            && (method = arrayOfPropertyDescriptor[b].getWriteMethod()) != null) {
          paramValueVector.set(new Value(method.getName(), 2), 2);
          return super.call(paramValueVector, paramContext);
        }
      }
      throw new ReteException("set", "No such property:", str);
    } catch (IntrospectionException introspectionException) {
      throw new ReteException("get", "Introspection Error:", introspectionException.toString());
    }
  }
}
