from java.awt import *
from java.util import *
from javax.swing import *

from edu.mines.jtk.dsp import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.util import *

nrow = 2
ncol = 1
axesPlacement = EnumSet.of(
  Mosaic.AxesPlacement.LEFT,
  Mosaic.AxesPlacement.BOTTOM)
borderStyle = Mosaic.BorderStyle.FLAT
mosaic = Mosaic(nrow,ncol,axesPlacement,borderStyle)
mosaic.setPreferredSize(Dimension(950,400))
tileA = mosaic.getTile(0,0)
tileB = mosaic.getTile(1,0)
nx = 101
dx = 0.1
fx = 0.0
y = Array.sin(Array.rampfloat(fx,dx,nx))
x = Sampling(nx,dx,fx)
lv = LollipopView(x,y)
tileA.addTiledView(lv)
tileB.addTiledView(lv)
frame = JFrame()
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
frame.add(mosaic,BorderLayout.CENTER)
frame.pack()
frame.setVisible(1)
mosaic.paintToPng(600,3,"mosaic.png")
