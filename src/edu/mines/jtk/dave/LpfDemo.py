from math import *
from java.awt import *
from edu.mines.jtk.dave import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.io import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.util import *

True = 1
False = 0

def plot(f,png=None,clip=0,cmin=0):
  fmin = Array.min(f)
  fmax = Array.max(f)
  print "fmin =",fmin,"  fmax =",fmax
  panel = PlotPanel(PlotPanel.Orientation.X1DOWN_X2RIGHT)
  panel.setVFormat("%4.0f");
  panel.addColorBar()
  panel.setColorBarFormat("%4.2f");
  pv = panel.addPixels(f)
  if clip==0:
    pv.setPercentiles(0.0,100.0)
  elif cmin==0:
    pv.setClips(-clip,clip)
  else:
    pv.setClips(cmin,clip)
  #pv.setInterpolation(PixelsView.Interpolation.NEAREST)
  frame = PlotFrame(panel)
  frame.setBackground(Color.WHITE)
  frame.setFontSize(18)
  #frame.setSize(800,850)
  frame.setSize(900,800)
  frame.setVisible(True)
  #if png!=None:
  #  frame.paintToPng(600,3,png)

def readData():
  bo = DataFile.ByteOrder.LITTLE_ENDIAN
  df = DataFile("/data/seis/vg/junks.dat","r",bo)
  n1 = 315
  n2 = 315
  f = Array.zerofloat(n1,n2)
  df.readFloats(f)
  return f

def filter(lag1,lag2,sigma,f):
  n2 = len(f)
  n1 = len(f[0])
  lpf = LocalPredictionFilter(sigma)
  g = Array.zerofloat(n1,n2)
  a = lpf.apply(lag1,lag2,f,g)
  return g,a

def filterQ1(sigma,f):
  lag1 = (    0,
           1, 1)
  lag2 = (    1,  
           0, 1)
  return filter(lag1,lag2,sigma,f)

def filterQ4(sigma,f):
  lag1 = ( 0,
           1, 1)
  lag2 = (-1,
          -1, 0)
  return filter(lag1,lag2,sigma,f)

def filter33(sigma,f):
  lag1 = (-1,-1,-1,
           0,    0,
           1, 1, 1)
  lag2 = (-1, 0, 1,
          -1,    1,
          -1, 0, 1)
  return filter(lag1,lag2,sigma,f)

def filter13(sigma,f):
  lag1 = ( 0,    0)
  lag2 = (-1,    1)
  return filter(lag1,lag2,sigma,f)

def filter15(sigma,f):
  lag1 = ( 0, 0,    0, 0)
  lag2 = (-2,-1,    1, 2)
  return filter(lag1,lag2,sigma,f)

def filter17(sigma,f):
  lag1 = ( 0, 0, 0,    0, 0, 0)
  lag2 = (-3,-2,-1,    1, 2, 3)
  return filter(lag1,lag2,sigma,f)

def filter19(sigma,f):
  lag1 = ( 0, 0, 0, 0,    0, 0, 0, 0)
  lag2 = (-4,-3,-2,-1,    1, 2, 3, 4)
  return filter(lag1,lag2,sigma,f)

def filter23(sigma,f):
  lag1 = ( 0,    0,
           1, 1, 1)
  lag2 = (-1,    1,
          -1, 0, 1)
  return filter(lag1,lag2,sigma,f)

def filterH23(sigma,f):
  lag1 = (       0,
           1, 1, 1)
  lag2 = (       1,
          -1, 0, 1)
  return filter(lag1,lag2,sigma,f)

def filter25(sigma,f):
  lag1 = ( 0, 0,    0, 0,
           1, 1, 1, 1, 1)
  lag2 = (-2,-1,    1, 2,
          -2,-1, 0, 1, 2)
  return filter(lag1,lag2,sigma,f)

def filter27(sigma,f):
  lag1 = ( 0, 0, 0,    0, 0, 0,
           1, 1, 1, 1, 1, 1, 1)
  lag2 = (-3,-2,-1,    1, 2, 3,
          -3,-2,-1, 0, 1, 2, 3)
  return filter(lag1,lag2,sigma,f)

def filter55(sigma,f):
  lag1 = (-2,-2,-2,-2,-2, 
          -1,-1,-1,-1,-1,  
           0, 0,    0, 0,  
           1, 1, 1, 1, 1,
           2, 2, 2, 2, 2)
  lag2 = (-2,-1, 0, 1, 2, 
          -2,-1, 0, 1, 2,
          -2,-1,    1, 2,
          -2,-1, 0, 1, 2,
          -2,-1, 0, 1, 2)
  return filter(lag1,lag2,sigma,f)

def test():
  f = readData()
  plot(f)
  g,a = filterQ1(4,f)
  plot(g)
  r = Array.sub(g,f)
  plot(r)
  for aa in a:
    plot(aa)

def makeFigsQ1():
  f = readData()
  plot(f,"image.png",10)
  g,a = filterQ1(4000,f)
  e = Array.sub(f,g)
  plot(g,"lpfQ1Gg.png",10)
  plot(e,"lpfQ1Ge.png",1)
  print "a01 =",a[0][100][100]
  print "a10 =",a[1][100][100]
  print "a11 =",a[2][100][100]
  g,a = filterQ1(4,f)
  e = Array.sub(f,g)
  plot(g,"lpfQ14g.png",10)
  plot(e,"lpfQ14e.png",1)
  plot(a[0],"lpfQ14a01.png")
  plot(a[1],"lpfQ14a10.png")
  plot(a[2],"lpfQ14a11.png")
#makeFigsQ1()

def makeFigs27():
  f = readData()
  plot(f,"image.png",10)
  g,a = filter27(8000,f)
  e = Array.sub(f,g)
  plot(g,"lpf27Gg.png",10)
  plot(e,"lpf27Ge.png",1)
  print "a[ 0,-3] =",a[0][100][100]
  print "a[ 0,-2] =",a[1][100][100]
  print "a[ 0,-1] =",a[2][100][100]
  print "a[ 0, 0] = X"
  print "a[ 0, 1] =",a[3][100][100]
  print "a[ 0, 2] =",a[4][100][100]
  print "a[ 0, 3] =",a[5][100][100]
  print "a[ 1,-3] =",a[6][100][100]
  print "a[ 1,-2] =",a[7][100][100]
  print "a[ 1,-1] =",a[8][100][100]
  print "a[ 1, 0] =",a[9][100][100]
  print "a[ 1, 1] =",a[10][100][100]
  print "a[ 1, 2] =",a[11][100][100]
  print "a[ 1, 3] =",a[12][100][100]
  g,a = filter27(8,f)
  e = Array.sub(f,g)
  plot(g,"lpf278g.png",10)
  plot(e,"lpf278e.png",1)
#makeFigs27()

def makeFigsFR():
  f = readData()
  plot(f,"image.png",10)
  n2 = len(f)
  n1 = len(f[0])
  for sigma in (8,8000):
    lcf = LocalCorrelationFilter(sigma)
    t0 = Array.zerofloat(n1,n2)
    lcf.apply(0,0,f,f,t0)
    r = Array.zerofloat(n1,n2)
    t = Array.zerofloat(n1,n2)
    for lag2 in range(-7,8,1):
      for lag1 in range(-7,8,1):
        lcf.apply(lag1,lag2,f,f,t)
        t = Array.div(t,t0)
        Array.copy(n1/15,n2/15,7,7,15,15,t,7+lag1,7+lag2,15,15,r)
    if sigma<1000:
      plot(r,"acorl.png",1.0,-0.7)
    else:
      plot(r,"acorg.png",1.0,-0.7)
#makeFigsFR()
