package jess.view;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import jess.Node;
import jess.Rete;
import jess.ReteCompiler;
import jess.Successor;

class NetworkViewer extends Panel implements Observer {
  Rete m_engine;

  Vector m_rows = new Vector();

  Hashtable m_doneNodes = new Hashtable();

  Label m_lbl;

  long m_lastMD = 0L;

  Vector m_frames = new Vector();

  public NetworkViewer(Rete engine) {
    this.m_engine = engine;
    setLayout(new BorderLayout());
    add("South", this.m_lbl = new Label());
    ReteCompiler rc = this.m_engine.compiler();
    Vector roots = rc.roots();
    for (int i = 0; i < roots.size(); i++) buildNetwork(((Successor) roots.elementAt(i)).m_node, 1);
    message("Network complete");
  }

  public void update(Observable obs, Object obj) {
    if (obj.equals("RULE")) {
      this.m_doneNodes = new Hashtable();
      this.m_rows = new Vector();
      for (int j = 0; j < this.m_engine.compiler().roots().size(); j++)
        buildNetwork(((Successor) this.m_engine.compiler().roots().elementAt(j)).m_node, 1);
      message("Network complete");
      repaint();
    }
    for (int i = 0; i < this.m_frames.size(); i++)
      ((NodeView) this.m_frames.elementAt(i)).describeNode();
  }

  NodeView buildNetwork(Node n, int depth) {
    Vector vector;
    message("Building at depth " + depth);
    NodeView nv;
    if ((nv = (NodeView) this.m_doneNodes.get(n)) != null) return nv;
    if (this.m_rows.size() < depth) {
      vector = new Vector();
      this.m_rows.addElement(vector);
    } else {
      vector = this.m_rows.elementAt(depth - 1);
    }
    nv = new NodeView(n, depth - 1, vector.size(), this.m_frames);
    this.m_doneNodes.put(n, nv);
    vector.addElement(nv);
    for (int i = 0; i < n.succ().size(); i++) {
      Successor s = n.succ().elementAt(i);
      nv.addConnection(buildNetwork(s.m_node, depth + 1), s.m_callType);
    }
    return nv;
  }

  public void paint(Graphics g) {
    for (int i = 0; i < this.m_rows.size(); i++) {
      Vector row = this.m_rows.elementAt(i);
      for (int j = 0; j < row.size(); j++) {
        NodeView nv = row.elementAt(j);
        nv.paint(g);
      }
    }
  }

  public boolean mouseUp(Event e, int x, int y) {
    long t = System.currentTimeMillis();
    if (t - this.m_lastMD < 500L) {
      int rowidx = y / 20;
      int colidx = (x - rowidx % 2 * 10) / 20;
      message("No node in row " + rowidx + ", col " + colidx);
      if (this.m_rows.size() < rowidx + 1) return false;
      Vector row = this.m_rows.elementAt(rowidx);
      if (row.size() < colidx + 1) return false;
      NodeView nv = row.elementAt(colidx);
      nv.fullDisplay();
      message("OPEN!");
      return true;
    }
    this.m_lastMD = System.currentTimeMillis();
    return false;
  }

  public boolean mouseMove(Event e, int x, int y) {
    int rowidx = y / 20;
    int colidx = (x - rowidx % 2 * 10) / 20;
    message("No node in row " + rowidx + ", col " + colidx);
    if (this.m_rows.size() < rowidx + 1) return false;
    Vector row = this.m_rows.elementAt(rowidx);
    if (row.size() < colidx + 1) return false;
    NodeView nv = row.elementAt(colidx);
    nv.textDisplay(this);
    return true;
  }

  public void message(String s) {
    this.m_lbl.setText(s);
  }
}
