package jess.view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import jess.Context;
import jess.Funcall;
import jess.RU;
import jess.ReteException;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;

class View implements Userfunction {
  private int m_name = RU.putAtom("view");

  public int name() {
    return this.m_name;
  }

  public Value call(ValueVector vv, Context context) throws ReteException {
    Frame f = new Frame("Network View");
    f.setLayout(new BorderLayout());
    NetworkViewer nv = new NetworkViewer(context.engine());
    f.add("Center", nv);
    Button b = new Button("Quit");
    b.addActionListener(
        new ActionListener(this, f) {
          private final Frame val$f;

          private final View this$0;

          public void actionPerformed(ActionEvent ae) {
            this.val$f.hide();
            this.val$f.dispose();
          }
        });
    f.add("South", b);
    f.resize(500, 500);
    f.validate();
    f.show();
    if (context.engine().display() instanceof Observable)
      ((Observable) context.engine().display()).addObserver(nv);
    return Funcall.TRUE();
  }
}
