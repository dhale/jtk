//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2004, Colorado School of Mines and others.
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Common Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/cpl-v10.html
// Contributors:
//   Dave Hale, Colorado School of Mines
//////////////////////////////////////////////////////////////////////////////
package edu.mines.jves.opengl;

import java.awt.Canvas;
import java.awt.Graphics;

public class GlAwtCanvas extends Canvas {

  public GlAwtCanvas(GlPainter painter) {
    _painter = painter;
  }

  public void paint(Graphics g) {
    super.paint(g);
    if (_context==null)
      _context = new GlContext(this);
    _context.lock();
    try {
      _painter.glPaint();
      _context.swapBuffers();
    } finally {
      _context.unlock();
    }
  }

  public GlContext getContext() {
    return _context;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private GlContext _context;
  private GlPainter _painter;
}

