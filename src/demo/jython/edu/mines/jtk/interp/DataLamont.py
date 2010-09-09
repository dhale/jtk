#############################################################################
# Data and plotting for Lamont data.

from java.awt import *
from java.lang import *
from javax.swing import *

from edu.mines.jtk.awt import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.sgl import *
from edu.mines.jtk.util.ArrayMath import *

def dataLamont():
  """
  Seismic horizon from Davis, J.C., 2002, Statistics and Data Analysis in
  Geology, 3rd Edition: Wiley, page 381. The name of the file from which 
  these data were copied is LAMONT.TXT. Davis refers to these data as 
  "drill-hole and seismic control points." Returned x,y,z are easting, 
  northing, and depth, all in kft (after scaling by 0.001 below).
  """
  xyz = [
  32000, 87015, 7888, 38600, 87030, 8020, 44000, 86400, 7943,
  48200, 87060, 8003, 30650, 82200, 7918, 35030, 80901, 7905,
  39476, 80970, 7900, 44198, 80910, 7998, 33530, 76230, 7879,
  38024, 76170, 7817, 42440, 76650, 8018, 46955, 76620, 7890,
  30350, 71700, 7903, 35195, 71745, 7815, 39650, 71760, 7966,
  44180, 71766, 7913, 48800, 70215, 7818, 29240, 81960, 7920,
  32270, 83760, 7923, 33770, 84720, 7918, 35240, 85590, 7927,
  36740, 86520, 7969, 39680, 88350, 8043, 41240, 89250, 8059,
  29840, 67560, 7932, 31280, 66600, 7883, 32720, 65610, 7842,
  34190, 64650, 7842, 35720, 63720, 7874, 37100, 62700, 7871,
  38600, 61800, 7861, 40100, 60840, 7875, 30200, 62280, 7678,
  31730, 63000, 7740, 33500, 63720, 7799, 36680, 65160, 7902,
  38300, 65790, 7943, 39830, 66540, 7962, 41420, 67200, 7967,
  43010, 67920, 7931, 44690, 68580, 7876, 46250, 69300, 7854,
  30200, 76500, 7849, 31640, 75420, 7880, 33080, 74280, 7869,
  34520, 73260, 7820, 37280, 71070, 7892, 38660, 70020, 7965,
  40040, 69000, 8008, 41450, 67890, 7986, 42800, 66900, 7922,
  44240, 65730, 7852, 45500, 64710, 7773, 46940, 63600, 7693,
  48200, 62520, 7632, 49610, 61410, 7567, 33800, 89220, 7896,
  34880, 87720, 7915, 35840, 86280, 7943, 37040, 84720, 7948,
  37940, 83160, 7928, 40784, 79260, 7923, 42740, 75120, 8004,
  43730, 74610, 8001, 44660, 73200, 7927, 45680, 71700, 7877,
  46760, 70200, 7857, 47600, 68790, 7824, 48500, 67380, 7768,
  49520, 65940, 7696, 42320, 89100, 8032, 43700, 88080, 7971,
  45080, 87000, 7946, 46520, 86010, 7973, 47900, 85050, 8003,
  49400, 84000, 8032]
  #30800, 82800, ., 38210, 87480, ., 48020, 70020, .,
  #49400, 70680, ., 35900, 72120, ., 38840, 81900, .,
  #39800, 80400, ., 41780, 76800, .,
  n = len(xyz)/3
  x = zerofloat(n)
  y = zerofloat(n)
  z = zerofloat(n)
  copy(n,0,3,xyz,0,1,x)
  copy(n,1,3,xyz,0,1,y)
  copy(n,2,3,xyz,0,1,z)
  x = mul(0.001,x)
  y = mul(0.001,y)
  z = mul(0.001,z)
  return x,y,z

def samplingsLamont(grid="fine"):
  fx,fy = 29.0,60.5
  dx,dy = 0.10,0.10; nx,ny = 210,291
  if grid=="coarse":
    dx,dy = 0.40,0.40; nx,ny = 53,73
  elif grid=="medium":
    dx,dy = 0.20,0.20; nx,ny = 105,146
  elif grid=="fine":
    dx,dy = 0.10,0.10; nx,ny = 210,291
  elif grid=="finer":
    dx,dy = 0.05,0.05; nx,ny = 420,581
  sx,sy = Sampling(nx,dx,fx),Sampling(ny,dy,fy)
  return sx,sy
 
#############################################################################
# plotting

def plot2Lamont(f,x1,x2,g,s1,s2,title=None,png=None):
  n1 = len(g[0])
  n2 = len(g)
  panel = panel2Lamont()
  panel.setHLimits(29.0,49.5)
  panel.setVLimits(60.5,89.0)
  panel.setHInterval(5.0)
  panel.setVInterval(5.0)
  panel.setHLabel("Easting (kft)")
  panel.setVLabel("Northing (kft)")
  panel.addColorBar("Depth (kft)")
  pv = panel.addPixels(s1,s2,g)
  pv.setClips(min(f),max(f))
  pv.setInterpolation(PixelsView.Interpolation.NEAREST)
  pv.setColorModel(ColorMap.JET)
  cv = panel.addContours(s1,s2,g)
  cv.setContours(Sampling(20,0.025,7.575)) # 7.575 - 8.050
  cv.setLineColor(Color.BLACK)
  pv = panel.addPoints(x1,x2)
  pv.setLineStyle(PointsView.Line.NONE)
  pv.setMarkStyle(PointsView.Mark.FILLED_CIRCLE)
  pv.setMarkSize(6)
  frame2Lamont(panel,title,png)

def plot3Lamont(f,x1,x2,g,s1,s2):
  n = len(f)
  f = mul(20.0,f)
  g = mul(20.0,g)
  xyz = zerofloat(3*n)
  copy(n,0,1,x2,0,3,xyz)
  copy(n,0,1,x1,1,3,xyz)
  copy(n,0,1, f,2,3,xyz)
  tg = TriangleGroup(True,s2,s1,g)
  pg = PointGroup(s1.delta*s1.count/100.0,xyz);
  pg.setStates(StateSet.forTwoSidedShinySurface(Color.RED))
  sf = SimpleFrame()
  sf.orbitView.setAzimuth(240)
  sf.orbitView.setElevation(40)
  sf.orbitView.setScale(1.2)
  sf.orbitView.setAxesOrientation(AxesOrientation.XRIGHT_YOUT_ZDOWN)
  sf.setWorldSphere(s2.first,s1.first,min(f),s2.last,s1.last,max(f))
  sf.world.addChild(tg)
  sf.world.addChild(pg)
  sf.setSize(1000,1000)

def panel2Lamont():
  panel = PlotPanel(1,1,
    PlotPanel.Orientation.X1RIGHT_X2UP,
    PlotPanel.AxesPlacement.LEFT_BOTTOM)
  return panel

def frame2Lamont(panel,title=None,png=None):
  frame = PlotFrame(panel)
  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  if title:
    panel.setTitle(title)
    frame.setFontSizeForSlide(1.0,1.0)
    frame.setSize(865,952)
  else:
    frame.setFontSizeForSlide(1.0,0.8)
    frame.setSize(865,831)
  frame.setVisible(True)
  if png and pngDir:
    frame.paintToPng(100,6,pngDir+"/"+png+".png")
  return frame
