package jess.reflect;

import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import jess.Rete;
import jess.ReteException;

public class TextListener extends JessListener implements TextListener {
  public TextListener(String paramString, Rete paramRete) throws ReteException {
    super(paramString, paramRete);
  }

  public void textValueChanged(TextEvent paramTextEvent) {
    receiveEvent(paramTextEvent);
  }
}
