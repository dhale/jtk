from math import *
from edu.mines.jtk.awt import *
from edu.mines.jtk.dave import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.io import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.util import *

True = 1
False = 0

def plot(f):
  fmin = Array.min(f)
  fmax = Array.max(f)
  print "fmin =",fmin,"  fmax =",fmax
  panel = PlotPanel(PlotPanel.Orientation.X1DOWN_X2RIGHT)
  pv = panel.addPixels(f)
  pv.setPercentiles(1.0,99.0)
  #pv.setColorModel(ColorMap.JET);
  #pv.setInterpolation(PixelsView.Interpolation.NEAREST)
  panel.addColorBar()
  frame = PlotFrame(panel)
  frame.setFontSize(24)
  frame.setSize(1100,900)
  frame.setVisible(True)

def makeChirp(n1,n2):
  c1 = (n1-1)/2.0
  c2 = (n2-1)/2.0
  a = 0.0
  b = 0.2*pi/(sqrt(2.0)*max(c1,c2))
  f = Array.zerofloat(n1,n2)
  for i2 in range(n2):
    for i1 in range(n1):
      x1 = i1-c1
      x2 = i2-c2
      r = sqrt(x1*x1+x2*x2)
      f[i2][i1] = cos((a+0.5*b*r)*r)
  return f

def makeCosine(n1,n2):
  c1 = (n1-1)/2.0
  c2 = (n2-1)/2.0
  a = 0.25*pi
  f = Array.zerofloat(n1,n2)
  for i2 in range(n2):
    for i1 in range(n1):
      x1 = i1-c1
      x2 = i2-c2
      r = x1+0.0001*x2
      f[i2][i1] = cos(a*r)
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

def dip(g):
  n1 = g[0][0].length
  n2 = g[0].length
  d = Array.zerofloat(n1,n2)
  for i2 in range(n2):
    for i1 in range(n1):
      d[i2][i1] = atan2(g[2][i2][i1],g[1][i2][i1])
  return d

def orientA(sigma,f):
  n2 = len(f)
  n1 = len(f[0])
  g = Array.zerofloat(n1,n2,3)
  lof = LocalOrientFilter(sigma)
  lof.applyA(f,g)
  for i in range(3):
    plot(g[i])

def orientG(sigma,f):
  n2 = len(f)
  n1 = len(f[0])
  g = Array.zerofloat(n1,n2,3)
  lof = LocalOrientFilter(sigma)
  lof.applyG(f,g)
  for i in range(3):
    plot(g[i])

#f = makeChirp(315,315)
#f = makeCosine(105,105)
#f = makeWaves(315,315)
f = readData()
plot(f)
orientG(8,f)

rgf = RecursiveGaussianFilter(1)
rgf.apply00(f,f)
plot(f)
orientA(8,f)
