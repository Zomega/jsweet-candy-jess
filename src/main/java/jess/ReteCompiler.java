package jess;

import java.util.Vector;

public class ReteCompiler {
  private final int VARIABLE_TYPES = 8200;

  private Rete m_engine;

  private Vector m_roots = new Vector();

  private boolean m_dirty = false;

  public final Vector roots() {
    return this.m_roots;
  }

  public ReteCompiler(Rete paramRete) {
    this.m_engine = paramRete;
  }

  private Value eval(int[][] paramArrayOfint, Value paramValue, Defrule paramDefrule) throws ReteException {
    ValueVector valueVector = (ValueVector)paramValue.funcallValue().clone();
    for (byte b = 0; b < valueVector.size(); b++) {
      if ((valueVector.get(b).type() & 0x2008) != 0) {
        int[] arrayOfInt = new int[3];
        int i = valueVector.get(b).variableValue();
        arrayOfInt[0] = -1;
        for (byte b1 = 0; b1 < (paramArrayOfint[0]).length; b1++) {
          if (paramArrayOfint[0][b1] == i) {
            arrayOfInt[0] = paramArrayOfint[1][b1];
            arrayOfInt[1] = paramArrayOfint[2][b1];
            arrayOfInt[2] = paramArrayOfint[3][b1];
            break;
          }
        }
        if (arrayOfInt[0] == -1) {
          Binding binding = paramDefrule.findBinding(i);
          if (binding == null)
            throw new ReteException("ReteCompiler:eval()", "Unbound variable found in funcall:", valueVector.get(b).stringValue());
          if (binding.m_factIndex >= 0) {
            arrayOfInt[0] = binding.m_factIndex;
            arrayOfInt[1] = 2;
            arrayOfInt[2] = -1;
            valueVector.set(new Value(arrayOfInt, 4096), b);
          } else if (binding.m_factIndex == -2) {
            Funcall funcall = new Funcall("get-var", this.m_engine);
            funcall.add(valueVector.get(b));
            valueVector.set(new Value(funcall, 64), b);
          }
        } else {
          valueVector.set(new Value(arrayOfInt, 4096), b);
        }
      } else if (valueVector.get(b).type() == 64) {
        valueVector.set(eval(paramArrayOfint, valueVector.get(b), paramDefrule), b);
      }
    }
    return new Value(valueVector, 64);
  }

  private boolean checkForMultiPattern(Value paramValue, int paramInt) throws ReteException {
    Funcall funcall = paramValue.funcallValue();
    for (byte b = 0; b < funcall.size(); b++) {
      if (funcall.get(b).type() == 4096 && funcall.get(b).intArrayValue()[0] != paramInt)
        return true;
      if (funcall.get(b).type() == 64 && checkForMultiPattern(funcall.get(b), paramInt))
        return true;
    }
    return false;
  }

  void freeze() {
    if (this.m_dirty)
      for (byte b = 0; b < this.m_roots.size(); b++)
        ((Successor)this.m_roots.elementAt(b)).m_node.freeze();
    this.m_dirty = false;
  }

  public void addRule(Defrule paramDefrule) throws ReteException {
    if (this.m_engine.watchCompilations())
      this.m_engine.outStream().print(RU.getAtom(paramDefrule.m_name) + ": ");
    paramDefrule.freeze();
    doAddRule(paramDefrule);
  }

  private void doAddRule(Defrule paramDefrule) throws ReteException {
    int[][] arrayOfInt = makeVarTable(paramDefrule);
    Successor[] arrayOfSuccessor = new Successor[paramDefrule.m_patts.size()];
    Node[] arrayOfNode = new Node[paramDefrule.m_patts.size()];
    for (byte b1 = 0; b1 < paramDefrule.m_patts.size(); b1++) {
      Pattern pattern = paramDefrule.m_patts.elementAt(b1);
      boolean bool = (pattern.name() == RU.putAtom("test")) ? true : false;
      int i = 3 + pattern.m_tests.length;
      Successor successor1 = createSuccessor(this.m_roots, bool ? 30 : 2, pattern.name(), pattern.ordered(), 0, paramDefrule);
      arrayOfNode[b1] = successor1.m_node;
      for (byte b4 = 0; !bool && b4 < pattern.m_tests.length; b4++) {
        if (pattern.m_tests[b4] != null) {
          int j = 3 + b4;
          for (byte b = 0; b < (pattern.m_tests[b4]).length; b++) {
            Test1 test1 = pattern.m_tests[b4][b];
            if (test1.m_slotValue.type() == 8192)
              if (test1.m_subIdx == -1) {
                successor1 = createSuccessor(successor1.m_node.succ(), 7, j, 0, 0, paramDefrule);
              } else {
                successor1 = createSuccessor(successor1.m_node.succ(), 23, j, test1.m_subIdx, 0, paramDefrule);
              }
          }
        }
      }
      if (!bool)
        successor1 = createSuccessor(successor1.m_node.succ(), 6, i, 0, 0, paramDefrule);
      for (byte b5 = 0; !bool && b5 < pattern.m_tests.length; b5++) {
        if (pattern.m_slotLengths != null && pattern.m_slotLengths[b5] != -1)
          successor1 = createSuccessor(successor1.m_node.succ(), 22, 3 + b5, pattern.m_slotLengths[b5], 0, paramDefrule);
      }
      for (byte b6 = 0; !bool && b6 < pattern.m_tests.length; b6++) {
        if (pattern.m_tests[b6] != null) {
          int j = 3 + b6;
          for (byte b = 0; b < (pattern.m_tests[b6]).length; b++) {
            Test1 test1 = pattern.m_tests[b6][b];
            byte b7 = (test1.m_subIdx == -1) ? 1 : 20;
            if (test1.m_test == 1)
              b7 = (test1.m_subIdx == -1) ? 5 : 21;
            if (test1.m_slotValue.type() != 8 && test1.m_slotValue.type() != 8192)
              if (test1.m_slotValue.type() == 64) {
                Value value = eval(arrayOfInt, test1.m_slotValue, paramDefrule);
                if (!checkForMultiPattern(value, b1))
                  successor1 = createSuccessor(successor1.m_node.succ(), b7, value, j, test1.m_subIdx, paramDefrule);
              } else {
                successor1 = createSuccessor(successor1.m_node.succ(), b7, test1.m_slotValue, j, test1.m_subIdx, paramDefrule);
              }
          }
        }
      }
      arrayOfSuccessor[b1] = successor1;
    }
    for (byte b2 = 0; b2 < paramDefrule.m_patts.size(); b2++) {
      Pattern pattern = paramDefrule.m_patts.elementAt(b2);
      if (pattern.name() != RU.putAtom("test")) {
        int[] arrayOfInt1 = new int[256];
        byte b4 = 0;
        for (byte b5 = 0; b5 < pattern.m_tests.length; b5++) {
          if (pattern.m_tests[b5] != null)
            for (byte b = 0; b < (pattern.m_tests[b5]).length; b++) {
              if (((pattern.m_tests[b5][b]).m_slotValue.type() & 0x2008) != 0) {
                for (byte b6 = 0; b6 < b4; b6++) {
                  if ((pattern.m_tests[b5][b]).m_slotValue.variableValue() == arrayOfInt1[b6]) {}
                    // Byte code: goto -> 1163
                    // TODO: WTF, rexamine byte code
                  }
                }
                for (int i = b5 + 1; i < pattern.m_tests.length; i++) {
                  if (pattern.m_tests[i] != null)
                    for (byte b7 = 0; b7 < (pattern.m_tests[i]).length; b7++) {
                      if (((pattern.m_tests[i][b7]).m_slotValue.type() & 0x2008) != 0 && (pattern.m_tests[i][b7]).m_slotValue.variableValue() == (pattern.m_tests[b5][b]).m_slotValue.variableValue()) {
                        int j = b5 + 3;
                        int k = i + 3;
                        if ((pattern.m_tests[b5][b]).m_test == 0) {
                          if ((pattern.m_tests[i][b7]).m_test == 0) {
                            arrayOfSuccessor[b2] = createSuccessor((arrayOfSuccessor[b2]).m_node.succ(), 3, j, k, (pattern.m_tests[b5][b]).m_subIdx, (pattern.m_tests[i][b7]).m_subIdx, paramDefrule);
                          } else {
                            arrayOfSuccessor[b2] = createSuccessor((arrayOfSuccessor[b2]).m_node.succ(), 4, j, k, (pattern.m_tests[b5][b]).m_subIdx, (pattern.m_tests[i][b7]).m_subIdx, paramDefrule);
                          }
                        } else if ((pattern.m_tests[i][b7]).m_test == 0) {
                          arrayOfSuccessor[b2] = createSuccessor((arrayOfSuccessor[b2]).m_node.succ(), 4, j, k, (pattern.m_tests[b5][b]).m_subIdx, (pattern.m_tests[i][b7]).m_subIdx, paramDefrule);
                        }
                      }
                    }
                }
                arrayOfInt1[b4++] = (pattern.m_tests[b5][b]).m_slotValue.variableValue();
              }
            }
        }
      }
    }
    for (byte b3 = 1; b3 < paramDefrule.m_patts.size(); b3++) {
      NodeTest nodeTest;
      Pattern pattern = paramDefrule.m_patts.elementAt(b3);
      boolean bool1 = (pattern.name() == RU.putAtom("test")) ? true : false;
      if (bool1) {
        nodeTest = new NodeTest(this.m_engine);
      } else if (pattern.negated() != 0) {
        nodeTest = new NodeNot2(this.m_engine);
      } else {
        nodeTest = new Node2(this.m_engine);
      }
      for (byte b = 0; b < pattern.m_tests.length; b++) {
        if (pattern.m_tests[b] != null)
          for (byte b4 = 0; b4 < (pattern.m_tests[b]).length; b4++) {
            if (((pattern.m_tests[b][b4]).m_slotValue.type() & 0x2008) != 0) {
              byte b5;
              for (b5 = 0; arrayOfInt[0][b5] != (pattern.m_tests[b][b4]).m_slotValue.variableValue(); b5++);
              if (arrayOfInt[1][b5] > b3) {
                compilerError("AddRule", "Corrupted VarTable: table[1][n] > i");
              } else if (arrayOfInt[1][b5] == b3) {
                continue;
              }
              if ((pattern.m_tests[b][b4]).m_test == 0) {
                nodeTest.addTest(0, arrayOfInt[1][b5], arrayOfInt[2][b5], arrayOfInt[3][b5], 3 + b, (pattern.m_tests[b][b4]).m_subIdx);
              } else {
                nodeTest.addTest(1, arrayOfInt[1][b5], arrayOfInt[2][b5], arrayOfInt[3][b5], 3 + b, (pattern.m_tests[b][b4]).m_subIdx);
              }
              continue;
            }
            if ((pattern.m_tests[b][b4]).m_slotValue.type() == 64) {
              Value value = eval(arrayOfInt, (pattern.m_tests[b][b4]).m_slotValue, paramDefrule);
              if (bool1 || checkForMultiPattern(value, b3))
                if ((pattern.m_tests[b][b4]).m_test == 0) {
                  nodeTest.addTest(0, 3 + b, (pattern.m_tests[b][b4]).m_subIdx, value);
                } else {
                  nodeTest.addTest(1, 3 + b, (pattern.m_tests[b][b4]).m_subIdx, value);
                }
            }
            continue;
          }
      }
      boolean bool2 = true;
      Successor successor1 = null;
      Successor successor2 = null;
      if (pattern.negated() == 0) {
        byte b4;
        label212: for (b4 = 0; b4 < (arrayOfSuccessor[b3 - 1]).m_node.succ().size(); b4++) {
          Node node = ((Successor)(arrayOfSuccessor[b3 - 1]).m_node.succ().elementAt(b4)).m_node;
          if (node instanceof Node2 && !(node instanceof NodeNot2))
            for (byte b5 = 0; b5 < (arrayOfSuccessor[b3]).m_node.succ().size(); b5++) {
              if (node == ((Successor)(arrayOfSuccessor[b3]).m_node.succ().elementAt(b5)).m_node) {
                successor1 = (arrayOfSuccessor[b3 - 1]).m_node.succ().elementAt(b4);
                successor2 = (arrayOfSuccessor[b3]).m_node.succ().elementAt(b5);
                if (successor1.m_callType == 0 && successor2.m_callType == 1 && ((Node2)node).equals(nodeTest)) {
                  bool2 = false;
                  node.m_usecount++;
                  paramDefrule.addNode(successor1);
                  break label212;
                }
              }
            }
        }
      }
      if (bool2) {
        successor1 = new Successor(nodeTest, 0);
        (arrayOfSuccessor[b3 - 1]).m_node.succ().addElement(successor1);
        successor2 = new Successor(nodeTest, 1);
        (arrayOfSuccessor[b3]).m_node.succ().addElement(successor2);
        nodeTest.complete();
        if (this.m_engine.watchCompilations())
          this.m_engine.outStream().print("+2");
        paramDefrule.addNode(successor1);
      } else if (this.m_engine.watchCompilations()) {
        this.m_engine.outStream().print("=2");
      }
      arrayOfSuccessor[b3 - 1] = successor1;
      arrayOfSuccessor[b3] = successor2;
    }
    NodeTerm nodeTerm = new NodeTerm(paramDefrule, this.m_engine);
    Successor successor = new Successor(nodeTerm, 3);
    (arrayOfSuccessor[paramDefrule.m_patts.size() - 1]).m_node.succ().addElement(successor);
    paramDefrule.addNode(successor);
    if (this.m_engine.watchCompilations())
      this.m_engine.outStream().println("+t");
    this.m_dirty = true;
    this.m_engine.updateNodes(arrayOfNode);
  }

  private int[][] makeVarTable(Defrule paramDefrule) throws ReteException {
    int[] arrayOfInt = new int[256];
    byte b1 = 0;
    int[][] arrayOfInt1 = new int[4][256];
    for (byte b2 = 0; b2 < paramDefrule.m_patts.size(); b2++) {
      Pattern pattern = paramDefrule.m_patts.elementAt(b2);
      for (byte b = 0; b < pattern.m_tests.length; b++) {
        if (pattern.m_tests[b] != null)
          for (byte b4 = 0; b4 < (pattern.m_tests[b]).length; b4++) {
            if (((pattern.m_tests[b][b4]).m_slotValue.type() & 0x2008) != 0) {
              if (-1 == findIntInArray(arrayOfInt1[0], b1, (pattern.m_tests[b][b4]).m_slotValue.variableValue()) && (pattern.m_tests[b][b4]).m_test == 1)
                compilerError("makeVarTable", "Variable " + RU.getAtom((pattern.m_tests[b][b4]).m_slotValue.variableValue()) + " is used before definition, Rule " + RU.getAtom(paramDefrule.m_name));
              if ((pattern.m_tests[b][b4]).m_test != 1) {
                arrayOfInt1[0][b1] = (pattern.m_tests[b][b4]).m_slotValue.variableValue();
                arrayOfInt1[1][b1] = b2;
                arrayOfInt1[2][b1] = 3 + b;
                arrayOfInt1[3][b1++] = (pattern.m_tests[b][b4]).m_subIdx;
              }
            }
          }
      }
    }
    int[][] arrayOfInt2 = new int[4][b1];
    for (byte b3 = 0; b3 < 4; b3++)
      System.arraycopy(arrayOfInt1[b3], 0, arrayOfInt2[b3], 0, b1);
    return arrayOfInt2;
  }

  private int findIntInArray(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    byte b = -1;
    for (byte b1 = 0; b1 < paramInt1; b1++) {
      if (paramArrayOfint[b1] == paramInt2) {
        b = b1;
        break;
      }
    }
    return b;
  }

  Successor createSuccessor(Vector paramVector, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Defrule paramDefrule) throws ReteException {
    return createSuccessor(paramVector, paramInt1, paramInt2, paramInt3, paramInt4, -1, paramDefrule);
  }

  Successor createSuccessor(Vector paramVector, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Defrule paramDefrule) throws ReteException {
    for (byte b = 0; b < paramVector.size(); b++) {
      Successor successor1 = paramVector.elementAt(b);
      if (successor1.m_node instanceof Node1) {
        Node1 node11 = (Node1)successor1.m_node;
        if (node11.m_command == paramInt1 && node11.R1 == paramInt2 && node11.R2 == paramInt3 && node11.R3 == paramInt4 && node11.R4 == paramInt5) {
          if (this.m_engine.watchCompilations())
            this.m_engine.outStream().print("=1");
          paramDefrule.addNode(successor1);
          node11.m_usecount++;
          return successor1;
        }
      }
    }
    Node1 node1 = Node1.create(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, this.m_engine);
    Successor successor = new Successor(node1, 2);
    paramVector.addElement(successor);
    if (this.m_engine.watchCompilations())
      this.m_engine.outStream().print("+1");
    paramDefrule.addNode(successor);
    return successor;
  }

  private void cleanupBindings(Value paramValue) throws ReteException {
    Funcall funcall = paramValue.funcallValue();
    for (byte b = 0; b < funcall.size(); b++) {
      if (funcall.get(b).type() == 4096) {
        funcall.get(b).intArrayValue()[0] = 0;
      } else if (funcall.get(b).type() == 64) {
        cleanupBindings(funcall.get(b));
      }
    }
  }

  Successor createSuccessor(Vector paramVector, int paramInt1, Value paramValue, int paramInt2, Defrule paramDefrule) throws ReteException {
    return createSuccessor(paramVector, paramInt1, paramValue, paramInt2, -1, paramDefrule);
  }

  Successor createSuccessor(Vector paramVector, int paramInt1, Value paramValue, int paramInt2, int paramInt3, Defrule paramDefrule) throws ReteException {
    if (paramValue.type() == 64) {
      cleanupBindings(paramValue);
    } else if (paramValue.type() == 4096) {
      paramValue.intArrayValue()[0] = 0;
    }
    for (byte b = 0; b < paramVector.size(); b++) {
      Successor successor1 = paramVector.elementAt(b);
      if (successor1.m_node instanceof Node1) {
        Node1 node11 = (Node1)successor1.m_node;
        if (node11.m_command == paramInt1 && paramValue.equals(node11.m_value) && node11.R3 == paramInt2 && node11.R4 == paramInt3) {
          if (this.m_engine.watchCompilations())
            this.m_engine.outStream().print("=1");
          node11.m_usecount++;
          paramDefrule.addNode(successor1);
          return successor1;
        }
      }
    }
    Node1 node1 = Node1.create(paramInt1, paramValue, paramInt2, paramInt3, this.m_engine);
    Successor successor = new Successor(node1, 2);
    paramVector.addElement(successor);
    paramDefrule.addNode(successor);
    if (this.m_engine.watchCompilations())
      this.m_engine.outStream().print("+1");
    return successor;
  }

  private void compilerError(String paramString1, String paramString2) throws ReteException {
    throw new ReteException("ReteCompiler::" + paramString1, paramString2, "");
  }
}
