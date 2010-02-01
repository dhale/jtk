#############################################################################
# Data and plotting for Teapot Dome data. The 2D seismic image provided 
# here was extracted from a 3D seismic image provided by the Rocky Mountain 
# Oilfield Testing Center, a facility of the U.S. Department of Energy.

from java.awt import *
from java.lang import *
from javax.swing import *

from edu.mines.jtk.awt import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.io import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.sgl import *
from edu.mines.jtk.util.ArrayMath import *

def dataTeapot():
  """
  Values set interactively by Dave Hale.
  """
  txf = [
    30, 69,0.50,  99, 72,0.50, 153, 69,0.50, 198, 68,0.50, 
    63, 71,0.90, 128, 72,0.90, 176, 69,0.90,
    29,172,0.35,  97,173,0.35, 150,173,0.35, 192,176,0.35,
    63,173,0.75, 127,174,0.75, 172,174,0.75,
    33,272,0.20, 103,270,0.20, 160,267,0.20, 199,267,0.20,
    70,271,0.60, 134,268,0.60, 179,267,0.60]
  n = len(txf)/3
  t = zerofloat(n)
  x = zerofloat(n)
  f = zerofloat(n)
  copy(n,0,3,txf,0,1,t)
  copy(n,1,3,txf,0,1,x)
  copy(n,2,3,txf,0,1,f)
  t = add(0.5,mul(0.004,t))
  x = add(0.0,mul(0.025,x))
  return t,x,f

def imageTeapot():
  ft,fx = 0.500,0.000
  dt,dx = 0.004,0.025
  nt,nx = 251,357
  image = zerofloat(nt,nx)
  st,sx = Sampling(nt,dt,ft),Sampling(nx,dx,fx)
  ais = ArrayInputStream("data/tp73.dat")
  ais.readFloats(image)
  ais.close()
  return st,sx,image
 
#############################################################################
# plotting

def plot2Teapot(f,x1,x2,s,s1,s2,g=None,title=None,png=None):
  n1 = len(s[0])
  n2 = len(s)
  panel = panel2Teapot()
  panel.setHInterval(1.0)
  panel.setVInterval(0.2)
  panel.setHLabel("Distance (km)")
  panel.setVLabel("Time (s)")
  panel.addColorBar()
  panel.setColorBarWidthMinimum(110)
  pv = panel.addPixels(s1,s2,s)
  pv.setInterpolation(PixelsView.Interpolation.LINEAR)
  pv.setColorModel(ColorMap.GRAY)
  pv.setClips(-4.5,4.5)
  if g:
    pv = panel.addPixels(s1,s2,g)
    pv.setInterpolation(PixelsView.Interpolation.LINEAR)
    pv.setColorModel(ColorMap.getJet(0.5))
    pv.setClips(0.0,1.0)
  pv = panel.addPoints(x1,x2)
  pv.setLineStyle(PointsView.Line.NONE)
  pv.setMarkStyle(PointsView.Mark.FILLED_CIRCLE)
  pv.setMarkSize(6)
  pv.setMarkColor(Color.WHITE)
  frame2Teapot(panel,title,png)

def panel2Teapot():
  panel = PlotPanel(1,1,
    PlotPanel.Orientation.X1DOWN_X2RIGHT,
    PlotPanel.AxesPlacement.LEFT_TOP)
  return panel

def frame2Teapot(panel,title=None,png=None):
  frame = PlotFrame(panel)
  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  if title:
    panel.setTitle(title)
    frame.setFontSizeForSlide(1.0,1.0)
    frame.setSize(1240,940)
  else:
    frame.setFontSizeForSlide(1.0,0.8)
    frame.setSize(1240,840)
  frame.setVisible(True)
  if png and pngDir:
    frame.paintToPng(100,6,pngDir+"/"+png+".png")
  return frame
