from math import *
from java.awt import *
from edu.mines.jtk.dave import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.io import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.util import *

True = 1
False = 0

def plot(f,png=None):
  fmin = Array.min(f)
  fmax = Array.max(f)
  print "fmin =",fmin,"  fmax =",fmax
  panel = PlotPanel(PlotPanel.Orientation.X1DOWN_X2RIGHT)
  panel.setVFormat("%4.0f");
  #cb = panel.addColorBar()
  #cb.setFormat("%3.1f");
  pv = panel.addPixels(f)
  #pv.setPercentiles(1.0,99.0)
  #pv.setInterpolation(PixelsView.Interpolation.NEAREST)
  frame = PlotFrame(panel)
  frame.setBackground(Color.WHITE)
  frame.setFontSize(18)
  frame.setSize(800,850)
  frame.setVisible(True)
  if png!=None:
    frame.paintToPng(600,3,png)

def makeChirp(n1,n2):
  c1 = (n1-1)/2.0
  c2 = (n2-1)/2.0
  a = 0.0
  b = 0.3*pi/(sqrt(2.0)*max(c1,c2))
  f = Array.zerofloat(n1,n2)
  for i2 in range(n2):
    for i1 in range(n1):
      x1 = i1-c1
      x2 = i2-c2
      r = sqrt(x1*x1+x2*x2)
      f[i2][i1] = cos((a+0.5*b*r)*r)
  return f

def makeWaves(n1,n2):
  a1 = (n1-1)*1.0/4.0
  a2 = (n2-1)*1.0/4.0
  b1 = (n1-1)*3.0/4.0
  b2 = (n2-1)*3.0/4.0
  k = 0.25*pi
  f = Array.zerofloat(n1,n2)
  for i2 in range(n2):
    for i1 in range(n1):
      x1 = i1-a1
      x2 = i2-a2
      ra = sqrt(x1*x1+x2*x2)
      x1 = i1-b1
      x2 = i2-b2
      rb = sqrt(x1*x1+x2*x2)
      f[i2][i1] = cos(k*ra)+cos(k*rb)
  return f

def readData():
  bo = DataFile.ByteOrder.LITTLE_ENDIAN
  df = DataFile("/data/seis/vg/junks.dat","r",bo)
  n1 = 315
  n2 = 315
  f = Array.zerofloat(n1,n2)
  df.readFloats(f)
  return f

def filter(sigma,f):
  n2 = len(f)
  n1 = len(f[0])
  lcf = LocalCorrelationFilter(sigma)
  r0 = Array.zerofloat(n1,n2)
  lcf.apply(0,0,f,f,r0)
  r = Array.zerofloat(n1,n2)
  c = Array.zerofloat(n1,n2)
  for lag2 in range(-7,8,1):
    for lag1 in range(-7,8,1):
      lcf.apply(lag1,lag2,f,f,r)
      if lag1==5 and lag2==5:
        plot(r,"seislag55.png")
      r = Array.div(r,r0)
      Array.copy(n1/15,n2/15,7,7,15,15,r,7+lag1,7+lag2,15,15,c)
  return c

#f = makeChirp(105,105)
#plot(f,"chirp.png")
#c = filter(8,f)
#plot(c,"chirpcf8.png")

f = readData()
plot(f,"seis.png")
c = filter(8,f)
plot(c,"seiscf8.png")
