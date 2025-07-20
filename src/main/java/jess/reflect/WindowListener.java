package jess.reflect;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import jess.Funcall;
import jess.Rete;
import jess.ReteException;

public class WindowListener extends JessListener implements WindowListener {
  private Funcall m_fc;

  private Rete m_engine;

  public WindowListener(String paramString, Rete paramRete) throws ReteException {
    super(paramString, paramRete);
  }

  public void windowActivated(WindowEvent paramWindowEvent) {
    receiveEvent(paramWindowEvent);
  }

  public void windowDeactivated(WindowEvent paramWindowEvent) {
    receiveEvent(paramWindowEvent);
  }

  public void windowDeiconified(WindowEvent paramWindowEvent) {
    receiveEvent(paramWindowEvent);
  }

  public void windowIconified(WindowEvent paramWindowEvent) {
    receiveEvent(paramWindowEvent);
  }

  public void windowOpened(WindowEvent paramWindowEvent) {
    receiveEvent(paramWindowEvent);
  }

  public void windowClosed(WindowEvent paramWindowEvent) {
    receiveEvent(paramWindowEvent);
  }

  public void windowClosing(WindowEvent paramWindowEvent) {
    receiveEvent(paramWindowEvent);
  }
}
