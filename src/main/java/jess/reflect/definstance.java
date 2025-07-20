package jess.reflect;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import jess.Clearable;
import jess.Context;
import jess.Fact;
import jess.Funcall;
import jess.RU;
import jess.Resetable;
import jess.Rete;
import jess.ReteException;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;

class definstance implements Userfunction, PropertyChangeListener, Clearable, Resetable {
  Rete m_engine;

  defclass m_defclass;

  private int m_name = RU.putAtom("definstance");

  private Hashtable m_facts = new Hashtable(101);

  private Hashtable m_jessClasses = new Hashtable(101);

  definstance(Rete paramRete, defclass paramdefclass) {
    this.m_engine = paramRete;
    this.m_defclass = paramdefclass;
  }

  public int name() {
    return this.m_name;
  }

  Value undefine(Object paramObject) {
    this.m_facts.remove(paramObject);
    return (this.m_jessClasses.remove(paramObject) == null) ? Funcall.FALSE() : Funcall.TRUE();
  }

  public void clear() {
    this.m_facts.clear();
    this.m_jessClasses.clear();
  }

  public void reset() throws ReteException {
    Enumeration enumeration = this.m_facts.keys();
    while (enumeration.hasMoreElements()) {
      Object object = enumeration.nextElement();
      createFact(object, this.m_defclass.jessNameToJavaName((String)this.m_jessClasses.get(object)), null);
    }
  }

  public Value call(ValueVector paramValueVector, Context paramContext) throws ReteException {
    try {
      String str1 = paramValueVector.get(1).stringValue();
      if (this.m_defclass.jessNameToJavaName(str1) == null)
        throw new ReteException("defclass", "Unknown object class", str1);
      Object object = paramValueVector.get(2).externalAddressValue();
      if (this.m_facts.get(object) != null)
        return Funcall.FALSE();
      String str2 = this.m_defclass.jessNameToJavaName(str1);
      if (!object.getClass().isAssignableFrom(Class.forName(str2)))
        throw new ReteException("defclass", "Object is not instance of", str2);
      this.m_jessClasses.put(object, str1);
      createFact(object, str2, null);
      Class clazz = Class.forName("java.beans.PropertyChangeListener");
      Method method = object.getClass().getMethod("addPropertyChangeListener", new Class[] { clazz });
      method.invoke(object, new Object[] { this });
      return Funcall.TRUE();
    } catch (InvocationTargetException invocationTargetException) {
      throw new ReteException("definstance", "Cannot add PropertyChangeListener", invocationTargetException.getTargetException().toString());
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new ReteException("definstance", "Obj doesn't accept PropertyChangeListeners", "");
    } catch (ClassNotFoundException classNotFoundException) {
      throw new ReteException("definstance", "Class not found", "");
    } catch (IllegalAccessException illegalAccessException) {
      throw new ReteException("definstance", "Class or method is not accessible", "");
    }
  }

  private synchronized void createFact(Object paramObject, String paramString1, String paramString2) throws ReteException {
    boolean bool = false;
    try {
      Fact fact = (Fact)this.m_facts.get(paramObject);
      if (fact != null) {
        this.m_engine.retract(fact.factData());
      } else {
        bool = true;
        fact = new Fact((String)this.m_jessClasses.get(paramObject), 256, this.m_engine);
        fact.addValue("OBJECT", new Value(paramObject, 2048));
        this.m_facts.put(paramObject, fact);
      }
      ValueVector valueVector = fact.deft();
      Object[] arrayOfObject = new Object[0];
      for (byte b = 3; b < valueVector.size(); b += 2) {
        if (!valueVector.get(b + 0).stringValue().equals("OBJECT")) {
          PropertyDescriptor propertyDescriptor = (PropertyDescriptor)valueVector.get(b + 1).externalAddressValue();
          String str = propertyDescriptor.getName();
          Method method = propertyDescriptor.getReadMethod();
          Class clazz = method.getReturnType();
          if (paramString2 == null || str.equals(paramString2)) {
            Object object = method.invoke(paramObject, arrayOfObject);
            Value value1 = fact.findValue(str);
            Value value2 = ReflectFunctions.objectToValue(method.getReturnType(), object);
            if (!value1.equals(value2)) {
              fact.addValue(str, value2);
              bool = true;
            }
          }
        }
      }
      if (bool)
        this.m_engine.assert(fact.factData());
    } catch (InvocationTargetException invocationTargetException) {
      throw new ReteException("call", "Called method threw an exception", invocationTargetException.getTargetException().toString());
    } catch (IllegalAccessException illegalAccessException) {
      throw new ReteException("call", "Method is not accessible", illegalAccessException.toString());
    } catch (IllegalArgumentException illegalArgumentException) {
      throw new ReteException("call", "Invalid argument", illegalArgumentException.toString());
    }
  }

  public synchronized void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
    Object object = paramPropertyChangeEvent.getSource();
    try {
      String str = (String)this.m_jessClasses.get(object);
      if (str != null)
        createFact(object, this.m_defclass.jessNameToJavaName(str), paramPropertyChangeEvent.getPropertyName());
    } catch (ReteException reteException) {
      System.out.println("Async Error: " + reteException);
    }
  }
}
