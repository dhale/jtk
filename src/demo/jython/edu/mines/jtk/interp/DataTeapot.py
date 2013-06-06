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

pngDir = None # for no png images
#pngDir = "." # for png images

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
  #t = add(0.5,mul(0.004,t))
  #x = add(0.0,mul(0.025,x))
  return t,x,f

def dataTeapotShallow():
  """
  Values set interactively by Dave Hale. Omit deeper samples.
  """
  txf = [
    30, 69,0.50,  99, 72,0.50,
    63, 71,0.90, 128, 72,0.90,
    29,172,0.35,  97,173,0.35,
    63,173,0.75, 127,174,0.75,
    33,272,0.20, 103,270,0.20,
    70,271,0.60, 134,268,0.60]
  n = len(txf)/3
  t = zerofloat(n)
  x = zerofloat(n)
  f = zerofloat(n)
  copy(n,0,3,txf,0,1,t)
  copy(n,1,3,txf,0,1,x)
  copy(n,2,3,txf,0,1,f)
  #t = add(0.5,mul(0.004,t))
  #x = add(0.0,mul(0.025,x))
  return t,x,f

def imageTeapot():
  #ft,fx = 0.500,0.000
  #dt,dx = 0.004,0.025
  ft,fx = 0.0,0.0
  dt,dx = 1.0,1.0
  nt,nx = 251,357
  image = zerofloat(nt,nx)
  st,sx = Sampling(nt,dt,ft),Sampling(nx,dx,fx)
  ais = ArrayInputStream("/Users/dhale/Home/git/dave/jtk/data/tp73.dat")
  ais.readFloats(image)
  ais.close()
  return st,sx,image
 
#############################################################################
# plotting

def plot2Teapot(f,x1,x2,s,s1,s2,g=None,label=None,png=None,et=None):
  n1 = len(s[0])
  n2 = len(s)
  panel = panel2Teapot()
  #panel.setHInterval(2.0)
  #panel.setVInterval(0.2)
  #panel.setHLabel("Distance (km)")
  #panel.setVLabel("Time (s)")
  #panel.setHInterval(100.0)
  #panel.setVInterval(100.0)
  #panel.setHLabel("Pixel")
  #panel.setVLabel("Pixel")
  if label:
    panel.addColorBar(label)
  else:
    panel.addColorBar()
  panel.setColorBarWidthMinimum(180)
  pv = panel.addPixels(s1,s2,s)
  pv.setInterpolation(PixelsView.Interpolation.LINEAR)
  pv.setColorModel(ColorMap.GRAY)
  pv.setClips(-4.5,4.5)
  if g:
    alpha = 0.5
  else:
    g = zerofloat(s1.count,s2.count)
    alpha = 0.0
  pv = panel.addPixels(s1,s2,g)
  pv.setInterpolation(PixelsView.Interpolation.LINEAR)
  pv.setColorModel(ColorMap.getJet(alpha))
  if label and label[0]=="T":
    pv.setClips(0.0,1000.0)
  else:
    pv.setClips(0.0,1.0)
  cmap = pv.getColorMap()
  if et:
    tv = TensorsView(s1,s2,et)
    tv.setOrientation(TensorsView.Orientation.X1DOWN_X2RIGHT)
    tv.setLineColor(Color.YELLOW)
    tv.setLineWidth(3.0)
    tv.setScale(2.0)
    panel.getTile(0,0).addTiledView(tv)
  else:
    fs,x1s,x2s = makePointSets(cmap,f,x1,x2)
    for i in range(len(fs)):
      color = cmap.getColor(fs[i][0])
      color = Color(color.red,color.green,color.blue)
      pv = panel.addPoints(x1s[i],x2s[i])
      pv.setLineStyle(PointsView.Line.NONE)
      pv.setMarkStyle(PointsView.Mark.FILLED_CIRCLE)
      pv.setMarkSize(10)
      pv.setMarkColor(color)
  frame2Teapot(panel,png)

def makePointSets(cmap,f,x1,x2):
  sets = {}
  for i in range(len(f)):
    if f[i] in sets:
      points = sets[f[i]]
      points[0].append(f[i])
      points[1].append(x1[i])
      points[2].append(x2[i])
    else:
      points = [[f[i]],[x1[i]],[x2[i]]] # lists of f, x1, x2
      sets[f[i]] = points
  ns = len(sets)
  fs = zerofloat(1,ns)
  x1s = zerofloat(1,ns)
  x2s = zerofloat(1,ns)
  il = 0
  for points in sets:
    fl = sets[points][0]
    x1l = sets[points][1]
    x2l = sets[points][2]
    nl = len(fl)
    fs[il] = zerofloat(nl)
    x1s[il] = zerofloat(nl)
    x2s[il] = zerofloat(nl)
    copy(fl,fs[il])
    copy(x1l,x1s[il])
    copy(x2l,x2s[il])
    il += 1
  return fs,x1s,x2s

def panel2Teapot():
  panel = PlotPanel(1,1,
    PlotPanel.Orientation.X1DOWN_X2RIGHT,
    PlotPanel.AxesPlacement.NONE)
  return panel

def frame2Teapot(panel,png=None):
  frame = PlotFrame(panel)
  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  #frame.setFontSizeForPrint(8,240)
  #frame.setSize(1240,774)
  frame.setFontSizeForSlide(1.0,0.8)
  frame.setSize(1290,777)
  frame.setVisible(True)
  if png and pngDir:
    frame.paintToPng(400,3.2,pngDir+"/"+png+".png")
  return frame
