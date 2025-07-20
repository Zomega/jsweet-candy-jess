package jess.reflect;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Hashtable;
import jess.Clearable;
import jess.Context;
import jess.Deftemplate;
import jess.Funcall;
import jess.RU;
import jess.ReteException;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;

class defclass implements Userfunction, Clearable {
  private int m_name = RU.putAtom("defclass");

  private Hashtable m_javaClasses = new Hashtable(101);

  public int name() {
    return this.m_name;
  }

  public void clear() {
    this.m_javaClasses.clear();
  }

  String jessNameToJavaName(String paramString) {
    return (String)this.m_javaClasses.get(paramString);
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    String str1 = paramValueVector.get(1).stringValue();
    String str2 = paramValueVector.get(2).stringValue();
    try {
      Class clazz = Class.forName(str2);
      this.m_javaClasses.put(str1, str2);
      Deftemplate deftemplate = new Deftemplate(str1, 256);
      deftemplate.docstring("$JAVA-OBJECT$ " + str2);
      PropertyDescriptor[] arrayOfPropertyDescriptor = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
      for (byte b = 0; b < arrayOfPropertyDescriptor.length; b++) {
        Method method = arrayOfPropertyDescriptor[b].getReadMethod();
        if (method != null) {
          String str = arrayOfPropertyDescriptor[b].getName();
          Class clazz1 = method.getReturnType();
          if (clazz1.isArray()) {
            deftemplate.addMultiSlot(str, new Value(arrayOfPropertyDescriptor[b], 2048));
          } else {
            deftemplate.addSlot(str, new Value(arrayOfPropertyDescriptor[b], 2048));
          }
        }
      }
      deftemplate.addSlot("OBJECT", Funcall.NIL());
      paramContext.engine().addDeftemplate(deftemplate);
      return new Value(str2, 1);
    } catch (ClassNotFoundException classNotFoundException) {
      throw new ReteException("defclass", "Class not found:", str2);
    } catch (IntrospectionException introspectionException) {
      throw new ReteException("defclass", "Introspection error:", introspectionException.toString());
    }
  }
}
