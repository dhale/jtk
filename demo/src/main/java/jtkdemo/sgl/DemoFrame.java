/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package jtkdemo.sgl;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import edu.mines.jtk.awt.*;
import edu.mines.jtk.sgl.*;

/**
 * A frame for demoing the package {@link edu.mines.jtk.sgl}.
 * @author Dave Hale
 * @version 2006.06.28
 */
class DemoFrame extends JFrame {
  private static final long serialVersionUID = 1L;

  public DemoFrame(World world) {
    OrbitView view = (world!=null)?new OrbitView(world):new OrbitView();
    view.setAxesOrientation(AxesOrientation.XRIGHT_YOUT_ZDOWN);
    _canvas = new ViewCanvas(view);
    _canvas.setView(view);

    ModeManager mm = new ModeManager();
    mm.add(_canvas);
    OrbitViewMode ovm = new OrbitViewMode(mm);
    SelectDragMode sdm = new SelectDragMode(mm);

    JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic('F');
    Action exitAction = new AbstractAction("Exit") {
      private static final long serialVersionUID = 1L;
      public void actionPerformed(ActionEvent event) {
        System.exit(0);
      }
    };
    JMenuItem exitItem = fileMenu.add(exitAction);
    exitItem.setMnemonic('x');

    JMenu modeMenu = new JMenu("Mode");
    modeMenu.setMnemonic('M');
    JMenuItem ovmItem = new ModeMenuItem(ovm);
    modeMenu.add(ovmItem);
    JMenuItem sdmItem = new ModeMenuItem(sdm);
    modeMenu.add(sdmItem);

    JMenuBar menuBar = new JMenuBar();
    menuBar.add(fileMenu);
    menuBar.add(modeMenu);

    JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
    toolBar.setRollover(true);
    JToggleButton ovmButton = new ModeToggleButton(ovm);
    toolBar.add(ovmButton);
    JToggleButton sdmButton = new ModeToggleButton(sdm);
    toolBar.add(sdmButton);

    ovm.setActive(true);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(new Dimension(SIZE,SIZE));
    this.add(_canvas,BorderLayout.CENTER);
    this.add(toolBar,BorderLayout.WEST);
    this.setJMenuBar(menuBar);

    _view = view;
  }

  public OrbitView getOrbitView() {
    return _view;
  }

  public ViewCanvas getViewCanvas() {
    return _canvas;
  }

  public static void main(String[] args) {
    DemoFrame frame = new DemoFrame(null);
    frame.setVisible(true);
  }

  private static final int SIZE = 600;
  private OrbitView _view;
  private ViewCanvas _canvas;
}
