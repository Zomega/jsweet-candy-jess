package jess.reflect;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import jess.Rete;
import jess.ReteException;

public class KeyListener extends JessListener implements KeyListener {
  public KeyListener(String paramString, Rete paramRete) throws ReteException {
    super(paramString, paramRete);
  }

  public void keyPressed(KeyEvent paramKeyEvent) {
    receiveEvent(paramKeyEvent);
  }

  public void keyReleased(KeyEvent paramKeyEvent) {
    receiveEvent(paramKeyEvent);
  }

  public void keyTyped(KeyEvent paramKeyEvent) {
    receiveEvent(paramKeyEvent);
  }
}
