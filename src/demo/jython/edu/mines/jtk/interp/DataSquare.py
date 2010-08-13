#############################################################################
# Samplings and plotting for synthetic data [0,1]x[0,1] -> [0,1].

from java.awt import *
from java.lang import *
from java.util import *
from javax.swing import *

from edu.mines.jtk.awt import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.sgl import *
from edu.mines.jtk.util.ArrayMath import *

def samplingsSquare(grid="fine"):
  nxy = 51
  if grid=="coarse": nxy = 51
  elif grid=="medium": nxy = 101
  elif grid=="fine": nxy = 201
  elif grid=="finer": nxy = 401
  elif grid=="finest": nxy = 801
  nx,ny = nxy,nxy
  dx,dy = 1.0/(nx-1),1.0/(ny-1)
  fx,fy = 0.0,0.0
  sx,sy = Sampling(nx,dx,fx),Sampling(ny,dy,fy)
  return sx,sy

def plot2Square(f,x1,x2,g,s1,s2,title=None,png=None,contours=True,points=True):
  n1 = len(g[0])
  n2 = len(g)
  panel = panel2Square()
  panel.setHLimits(-0.01,1.01)
  panel.setVLimits(-0.01,1.01)
  panel.setHInterval(0.2)
  panel.setVInterval(0.2)
  panel.setHLabel("x")
  panel.setVLabel("y")
  panel.addColorBar()
  pv = panel.addPixels(s1,s2,g)
  pv.setClips(0.0,1.0)
  pv.setInterpolation(PixelsView.Interpolation.NEAREST)
  pv.setColorModel(ColorMap.JET)
  if contours:
    cv = panel.addContours(s1,s2,g)
    cv.setContours(Sampling(8,0.1,0.1)) # 0.1-0.9
    cv.setLineColor(Color.WHITE)
  if points:
    pv = panel.addPoints(x1,x2)
    pv.setLineStyle(PointsView.Line.NONE)
    pv.setMarkStyle(PointsView.Mark.FILLED_CIRCLE)
    pv.setMarkSize(4)
    pv.setMarkColor(Color.WHITE)
  frame2Square(panel,title,png)

def plot3Square(f,x1,x2,g,s1,s2):
  g = transpose(g)
  n = len(f)
  f = mul(0.5,f)
  g = mul(0.5,g)
  xyz = zerofloat(3*n)
  copy(n,0,1,x1,0,3,xyz)
  copy(n,0,1,x2,1,3,xyz)
  copy(n,0,1, f,2,3,xyz)
  tg = TriangleGroup(True,s1,s2,g)
  pg = PointGroup(0.01,xyz);
  pg.setStates(StateSet.forTwoSidedShinySurface(Color.RED))
  sf = SimpleFrame()
  sf.orbitView.setScale(1.2)
  sf.orbitView.setAxesOrientation(AxesOrientation.XOUT_YRIGHT_ZUP)
  sf.setWorldSphere(s1.first,s2.first,min(f),s1.last,s2.last,max(f))
  sf.world.addChild(tg)
  sf.world.addChild(pg)
  sf.setSize(1000,1000)

def panel2Square():
  panel = PlotPanel(1,1,
    PlotPanel.Orientation.X1RIGHT_X2UP,
    PlotPanel.AxesPlacement.LEFT_BOTTOM)
  return panel

def frame2Square(panel,title=None,png=None):
  frame = PlotFrame(panel)
  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  if title:
    panel.setTitle(title)
    frame.setFontSizeForSlide(1.0,1.0)
    frame.setSize(1020,960)
  else:
    frame.setFontSizeForSlide(1.0,0.8)
    frame.setSize(1020,900)
  frame.setVisible(True)
  if png and pngDir:
    frame.paintToPng(100,6,pngDir+"/"+png+".png")
  return frame
