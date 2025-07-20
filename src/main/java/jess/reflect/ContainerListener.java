package jess.reflect;

import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import jess.Rete;
import jess.ReteException;

public class ContainerListener extends JessListener implements ContainerListener {
  public ContainerListener(String paramString, Rete paramRete) throws ReteException {
    super(paramString, paramRete);
  }

  public void componentAdded(ContainerEvent paramContainerEvent) {
    receiveEvent(paramContainerEvent);
  }

  public void componentRemoved(ContainerEvent paramContainerEvent) {
    receiveEvent(paramContainerEvent);
  }
}
