package jess.reflect;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import jess.Rete;
import jess.ReteException;

public class FocusListener extends JessListener implements FocusListener {
  public FocusListener(String paramString, Rete paramRete) throws ReteException {
    super(paramString, paramRete);
  }

  public void focusGained(FocusEvent paramFocusEvent) {
    receiveEvent(paramFocusEvent);
  }

  public void focusLost(FocusEvent paramFocusEvent) {
    receiveEvent(paramFocusEvent);
  }
}
