#############################################################################
# Data and plotting for NotreDame data.

from java.awt import *
from java.lang import *
from javax.swing import *

from edu.mines.jtk.awt import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.sgl import *
from edu.mines.jtk.util.ArrayMath import *

def dataNotreDame():
  """
  Elevation data from Davis, J.C., 2002, Statistics and Data Analysis in
  Geology, 3rd Edition: Wiley, page 374. The name of the file from which 
  these data were copied is NOTREDAM.TXT. These data were collected over
  a small area by students in a surveying class.
  These data are used in Figure 3 of Wessel, P., 2009, A general-purpose 
  Green's function-based interpolator: Computers and Geosciences 35, 
  1247-1254.
  This function returns three float arrays x, y, z containing Eastings,
  Northings and elevations. Units are unknown. Davis writes that one 
  map unit = 50 ft, but this implies extremely steep terrain, with 
  slopes exceeding 45 degrees.
  """
  xyzNotreDame = [
  0.3, 6.1, 870.0, 1.4, 6.2, 793.0, 2.4, 6.1, 755.0, 3.6, 6.2, 690.0,
  5.7, 6.2, 800.0, 1.6, 5.2, 800.0, 2.9, 5.1, 730.0, 3.4, 5.3, 728.0,
  3.4, 5.7, 710.0, 4.8, 5.6, 780.0, 5.3, 5.0, 804.0, 6.2, 5.2, 855.0,
  0.2, 4.3, 830.0, 0.9, 4.2, 813.0, 2.3, 4.8, 762.0, 2.5, 4.5, 765.0,
  3.0, 4.5, 740.0, 3.5, 4.5, 765.0, 4.1, 4.6, 760.0, 4.9, 4.2, 790.0,
  6.3, 4.3, 820.0, 0.9, 3.2, 855.0, 1.7, 3.8, 812.0, 2.4, 3.8, 773.0,
  3.7, 3.5, 812.0, 4.5, 3.2, 827.0, 5.2, 3.2, 805.0, 6.3, 3.4, 840.0,
  0.3, 2.4, 890.0, 2.0, 2.7, 820.0, 3.8, 2.3, 873.0, 6.3, 2.2, 875.0,
  0.6, 1.7, 873.0, 1.5, 1.8, 865.0, 2.1, 1.8, 841.0, 2.1, 1.1, 862.0,
  3.1, 1.1, 908.0, 4.5, 1.8, 855.0, 5.5, 1.7, 850.0, 5.7, 1.0, 882.0,
  6.2, 1.0, 910.0, 0.4, 0.5, 940.0, 1.4, 0.6, 915.0, 1.4, 0.1, 890.0,
  2.1, 0.7, 880.0, 2.3, 0.3, 870.0, 3.1, 0.0, 880.0, 4.1, 0.8, 960.0,
  5.4, 0.4, 890.0, 6.0, 0.1, 860.0, 5.7, 3.0, 830.0, 3.6, 6.0, 705.0]
  nNotreDame = len(xyzNotreDame)/3
  xNotreDame = zerofloat(nNotreDame)
  yNotreDame = zerofloat(nNotreDame)
  zNotreDame = zerofloat(nNotreDame)
  copy(nNotreDame,0,3,xyzNotreDame,0,1,xNotreDame)
  copy(nNotreDame,1,3,xyzNotreDame,0,1,yNotreDame)
  copy(nNotreDame,2,3,xyzNotreDame,0,1,zNotreDame)
  xNotreDame = mul(50.0,xNotreDame) # Davis says one map unit = 50 ft,
  yNotreDame = mul(50.0,yNotreDame) # but this makes the terrain steep!
  return xNotreDame,yNotreDame,zNotreDame

def samplingsNotreDame(grid="fine"):
  fx,fy = -5.0,-5.0
  dx,dy = 2.00,2.00; nx,ny = 165,165
  if grid=="coarser": dx,dy = 8.00,8.00; nx,ny = 42,42
  elif grid=="coarse": dx,dy = 4.00,4.00; nx,ny = 83,83
  elif grid=="medium": dx,dy = 2.00,2.00; nx,ny = 165,165
  elif grid=="fine": dx,dy = 1.00,1.00; nx,ny = 329,329
  elif grid=="finer": dx,dy = 0.50,0.50; nx,ny = 657,657
  elif grid=="finest": dx,dy = 0.25,0.25; nx,ny = 1313,1313
  sx,sy = Sampling(nx,dx,fx),Sampling(ny,dy,fy)
  return sx,sy
 
#############################################################################
# plotting

def plot2NotreDame(f,x1,x2,g,s1,s2,title=None,png=None,
                   contours=True,points=True):
  n1 = len(g[0])
  n2 = len(g)
  panel = panel2NotreDame()
  panel.setHLimits(-5.0,323.0)
  panel.setVLimits(-5.0,323.0)
  panel.setHInterval(100.0)
  panel.setVInterval(100.0)
  panel.setHLabel("Easting (ft)")
  panel.setVLabel("Northing (ft)")
  panel.addColorBar("Elevation (ft)")
  pv = panel.addPixels(s1,s2,g)
  pv.setClips(min(f),max(f))
  pv.setInterpolation(PixelsView.Interpolation.NEAREST)
  pv.setColorModel(ColorMap.JET)
  if contours:
    cv = panel.addContours(s1,s2,g)
    cv.setContours(Sampling(10,25.0,700.0)) # 700 - 925
    cv.setLineColor(Color.BLACK)
  if points:
    pv = panel.addPoints(x1,x2)
    pv.setLineStyle(PointsView.Line.NONE)
    pv.setMarkStyle(PointsView.Mark.FILLED_CIRCLE)
    pv.setMarkSize(6)
  frame2NotreDame(panel,title,png)

def plot3NotreDame(f,x1,x2,g,s1,s2):
  g = transpose(g)
  n = len(f)
  #f = mul(0.5,f)
  #g = mul(0.5,g)
  xyz = zerofloat(3*n)
  copy(n,0,1,x1,0,3,xyz)
  copy(n,0,1,x2,1,3,xyz)
  copy(n,0,1, f,2,3,xyz)
  tg = TriangleGroup(True,s1,s2,g)
  pg = PointGroup(s1.delta*s1.count/100.0,xyz);
  pg.setStates(StateSet.forTwoSidedShinySurface(Color.RED))
  sf = SimpleFrame()
  sf.orbitView.setScale(1.2)
  sf.orbitView.setAxesOrientation(AxesOrientation.XOUT_YRIGHT_ZUP)
  sf.setWorldSphere(s1.first,s2.first,min(f),s1.last,s2.last,max(f))
  sf.world.addChild(tg)
  sf.world.addChild(pg)
  sf.setSize(1000,1000)

def panel2NotreDame():
  panel = PlotPanel(1,1,
    PlotPanel.Orientation.X1RIGHT_X2UP,
    PlotPanel.AxesPlacement.LEFT_BOTTOM)
  return panel

def frame2NotreDame(panel,title=None,png=None):
  frame = PlotFrame(panel)
  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  if title:
    panel.setTitle(title)
    frame.setFontSizeForSlide(1.0,1.0)
    frame.setSize(1100,960)
  else:
    frame.setFontSizeForSlide(1.0,0.8)
    frame.setSize(1170,897)
  frame.setVisible(True)
  if png and pngDir:
    frame.paintToPng(100,6,pngDir+"/"+png+".png")
  return frame
