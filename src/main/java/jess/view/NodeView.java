package jess.view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import jess.Node;
import jess.Node2;
import jess.NodeTerm;
import jess.ReteException;

class NodeView implements ActionListener {
  static final int WIDTH = 20;

  static final int HEIGHT = 20;

  static final int SIZE = 10;

  static final int MARGIN = 5;

  static final int HALF = 5;

  int top;

  int left;

  Node m_node;

  Color c;

  Vector conn = new Vector();

  TextArea m_view;

  Vector m_frames;

  Frame m_f;

  Color[] ctColor = new Color[] { Color.blue, Color.orange, Color.green, Color.red };

  public NodeView(Node node, int row, int col, Vector v) {
    this.m_node = node;
    this.m_frames = v;
    this.top = row * 20 + 5;
    this.left = col * 20 + 5 + row % 2 * 10;
    if (this.m_node instanceof jess.Node1) {
      this.c = Color.red;
    } else if (this.m_node instanceof jess.NodeNot2) {
      this.c = Color.yellow;
    } else if (this.m_node instanceof Node2) {
      this.c = Color.green;
    } else if (this.m_node instanceof jess.NodeTest) {
      this.c = Color.blue;
    } else if (this.m_node instanceof NodeTerm) {
      this.c = Color.cyan;
    } else {
      this.c = Color.black;
    }
  }

  public void paint(Graphics g) {
    g.setColor(this.c);
    g.fillRect(this.left, this.top, 10, 10);
    for (int i = 0; i < this.conn.size(); i++) {
      NVSucc nvs = this.conn.elementAt(i);
      g.setColor(this.ctColor[nvs.callType]);
      g.drawLine(this.left + 5, this.top + 10, nvs.nv.left + 5, nvs.nv.top);
    }
  }

  public void textDisplay(NetworkViewer vwr) {
    vwr.message(this.m_node.toString());
  }

  public void actionPerformed(ActionEvent ae) {
    this.m_frames.removeElement(this);
    if (this.m_f != null) {
      this.m_f.dispose();
      this.m_f = null;
    }
  }

  public void fullDisplay() {
    if (this.m_f == null) {
      this.m_f = new Frame("Node View");
      this.m_f.setLayout(new BorderLayout());
      Button b = new Button("Quit");
      b.addActionListener(this);
      TextArea ta = new TextArea(40, 20);
      this.m_view = ta;
      ta.setEditable(false);
      this.m_f.add("South", b);
      this.m_f.add("Center", ta);
      describeNode();
      this.m_f.setSize(300, 300);
      this.m_f.validate();
      this.m_f.show();
      this.m_frames.addElement(this);
    }
  }

  void describeNode() {
    StringBuffer sb = new StringBuffer(this.m_node.toString());
    if (this.m_node instanceof Node2) {
      sb.append(((Node2)this.m_node).displayMemory());
    } else if (this.m_node instanceof NodeTerm) {
      try {
        sb.append("\n\n");
        sb.append(((NodeTerm)this.m_node).rule().ppRule());
        sb.append("\n\n");
        sb.append(((NodeTerm)this.m_node).rule().listNodes());
      } catch (ReteException re) {}
    }
    this.m_view.setText(sb.toString());
  }

  public void addConnection(NodeView nv, int callType) {
    this.conn.addElement(new NVSucc(nv, callType));
  }
}
