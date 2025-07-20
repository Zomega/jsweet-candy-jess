package jess;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.Vector;

public class Context {
  Vector m_actions = new Vector();

  Vector m_bindings = new Vector();

  Rete m_engine;

  private Stack m_states;

  boolean m_return = false;

  Value m_retval;

  Context(Rete paramRete) {
    this.m_engine = paramRete;
    this.m_states = new Stack();
  }

  final boolean returning() {
    return this.m_return;
  }

  final Value setReturnValue(Value paramValue) {
    this.m_return = true;
    this.m_retval = paramValue;
    return paramValue;
  }

  final Value getReturnValue() {
    return this.m_retval;
  }

  final void clearReturnValue() {
    this.m_return = false;
    this.m_retval = null;
  }

  public final Rete engine() {
    return this.m_engine;
  }

  void push() {
    ContextState contextState = new ContextState();
    contextState.m_return = this.m_return;
    contextState.m_retval = this.m_retval;
    contextState.m_bindings = new Vector();
    for (byte b = 0; b < this.m_bindings.size(); b++)
      contextState.m_bindings.addElement(((Binding)this.m_bindings.elementAt(b)).clone());
    this.m_states.push(contextState);
  }

  void pop() {
    try {
      ContextState contextState = this.m_states.pop();
      this.m_return = contextState.m_return;
      this.m_retval = contextState.m_retval;
      this.m_bindings = contextState.m_bindings;
    } catch (EmptyStackException emptyStackException) {}
  }

  Binding findBinding(int paramInt) {
    for (byte b = 0; b < this.m_bindings.size(); b++) {
      Binding binding = this.m_bindings.elementAt(b);
      if (binding.m_name == paramInt)
        return binding;
    }
    return this.m_engine.globalContext().findGlobalBinding(paramInt);
  }

  final void addAction(ValueVector paramValueVector) {
    this.m_actions.addElement(paramValueVector);
  }

  final Binding addBinding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    Binding binding = new Binding(paramInt1, paramInt2, paramInt3, paramInt4);
    this.m_bindings.addElement(binding);
    return binding;
  }

  final Binding setVariable(int paramInt, Value paramValue) {
    Binding binding = findBinding(paramInt);
    if (binding == null)
      binding = addBinding(paramInt, -2, -2, -1);
    binding.m_val = paramValue;
    return binding;
  }

  Funcall expandAction(Funcall paramFuncall) throws ReteException {
    boolean bool = false;
    Funcall funcall = (Funcall)paramFuncall.clone();
    String str = funcall.get(0).stringValue();
    for (byte b = 1; b < funcall.size(); b++) {
      Value value = funcall.get(b);
      if ((str.equals("bind") || str.equals("foreach")) && b == 1) {
        Binding binding = findBinding(value.variableValue());
        if (binding == null)
          binding = setVariable(value.variableValue(), null);
        bool = true;
      } else {
        if ((str.equals("if") && b > 1) || str.equals("while") || str.equals("and") || str.equals("or") || (str.equals("foreach") && b > 2))
          break;
        funcall.set(expandValue(value), b);
      }
    }
    return funcall;
  }

  public Value expandValue(Value paramValue) throws ReteException {
    Binding binding;
    Funcall funcall;
    ValueVector valueVector;
    switch (paramValue.type()) {
      case 8:
      case 8192:
        binding = findBinding(paramValue.variableValue());
        if (binding == null) {
          binding = setVariable(paramValue.variableValue(), null);
          break;
        }
        return (binding.m_val != null) ? binding.m_val : Funcall.NIL();
      case 64:
        funcall = paramValue.funcallValue();
        return new Value(expandAction(funcall), 64);
      case 128:
      case 256:
        valueVector = paramValue.factValue();
        return new Value(expandFact(valueVector), paramValue.type());
      case 512:
        valueVector = paramValue.listValue();
        valueVector = expandList(valueVector);
        valueVector = flattenList(valueVector);
        return new Value(valueVector, paramValue.type());
    }
    return paramValue;
  }

  public ValueVector expandFact(ValueVector paramValueVector) throws ReteException {
    ValueVector valueVector = (ValueVector)paramValueVector.clone();
    for (byte b = 3; b < valueVector.size(); b++) {
      Binding binding;
      Funcall funcall;
      ValueVector valueVector1;
      Value value;
      switch (valueVector.get(b).type()) {
        case 8:
        case 8192:
          binding = findBinding(valueVector.get(b).variableValue());
          valueVector.set(binding.m_val, b);
          break;
        case 64:
          funcall = expandAction(valueVector.get(b).funcallValue());
          value = Funcall.execute(funcall, this, null, null);
          valueVector.set(value, b);
          break;
        case 512:
          valueVector1 = valueVector.get(b).listValue();
          valueVector1 = expandList(valueVector1);
          valueVector1 = flattenList(valueVector1);
          valueVector.set(new Value(valueVector1, 512), b);
          break;
      }
    }
    return valueVector;
  }

  ValueVector expandList(ValueVector paramValueVector) throws ReteException {
    ValueVector valueVector = (ValueVector)paramValueVector.clone();
    for (byte b = 0; b < valueVector.size(); b++) {
      Binding binding;
      Funcall funcall;
      ValueVector valueVector1;
      Value value;
      switch (valueVector.get(b).type()) {
        case 8:
        case 8192:
          binding = findBinding(valueVector.get(b).variableValue());
          valueVector.set(binding.m_val, b);
          break;
        case 64:
          funcall = expandAction(valueVector.get(b).funcallValue());
          value = Funcall.execute(funcall, this, null, null);
          valueVector.set(value, b);
          break;
        case 512:
          valueVector1 = expandList(valueVector.get(b).listValue());
          valueVector.set(new Value(valueVector1, 512), b);
          break;
      }
    }
    return valueVector;
  }

  static final ValueVector flattenList(ValueVector paramValueVector) throws ReteException {
    ValueVector valueVector = new ValueVector();
    doFlattenList(valueVector, paramValueVector);
    return valueVector;
  }

  private static void doFlattenList(ValueVector paramValueVector1, ValueVector paramValueVector2) throws ReteException {
    for (byte b = 0; b < paramValueVector2.size(); b++) {
      Value value = paramValueVector2.get(b);
      if (value.type() != 512) {
        paramValueVector1.add(value);
      } else {
        doFlattenList(paramValueVector1, value.listValue());
      }
    }
  }
}
