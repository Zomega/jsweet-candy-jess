package jess.reflect;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jess.Rete;
import jess.ReteException;

public class ActionListener extends JessListener implements ActionListener {
  public ActionListener(String paramString, Rete paramRete) throws ReteException {
    super(paramString, paramRete);
  }

  public void actionPerformed(ActionEvent paramActionEvent) {
    receiveEvent(paramActionEvent);
  }
}
