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
  cb = panel.addColorBar()
  #cb.setFormat("%3.1f");
  pv = panel.addPixels(f)
  pv.setPercentiles(1.0,99.0)
  #pv.setClips(-10,10)
  #pv.setInterpolation(PixelsView.Interpolation.NEAREST)
  frame = PlotFrame(panel)
  frame.setBackground(Color.WHITE)
  frame.setFontSize(18)
  #frame.setSize(800,850)
  frame.setSize(900,850)
  frame.setVisible(True)
  if png!=None:
    frame.paintToPng(600,3,png)

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
  lpf = LocalPredictionFilter(sigma)
  g = Array.zerofloat(n1,n2)
  lag1 = (    1, 2,
           0, 1, 2,
           0, 1, 2)
  lag2 = (    0, 0,  
          -1,-1,-1,
          -2,-2,-2)
  lag1 = (-2,-1, 0, 1, 2, 
          -2,-1, 0, 1, 2,
          -2,-1,    1, 2,
          -2,-1, 0, 1, 2,
          -2,-1, 0, 1, 2)
  lag2 = (-2,-2,-2,-2,-2, 
          -1,-1,-1,-1,-1,  
           0, 0,    0, 0,  
           1, 1, 1, 1, 1,
           2, 2, 2, 2, 2)
  lag1 = ( 0, 0, 0,    0, 0, 0,
           1, 1, 1, 1, 1, 1, 1)
  lag2 = (-3,-2,-1,    1, 2, 3,
          -3,-2,-1, 0, 1, 2, 3)
  lag1 = ( 0, 0, 0, 0,    0, 0, 0, 0)
  lag2 = (-4,-3,-2,-1,    1, 2, 3, 4)
  lag1 = ( 0,
           1, 1)
  lag2 = (-1,
          -1, 0)
  lag1 = (    0,
           1, 1)
  lag2 = (    1,  
           0, 1)
  lag1 = ( 0, 0, 0,    0, 0, 0,
           1, 1, 1, 1, 1, 1, 1)
  lag2 = (-3,-2,-1,    1, 2, 3,
          -3,-2,-1, 0, 1, 2, 3)
  lag1 = ( 0, 0, 0, 0,    0, 0, 0, 0)
  lag2 = (-4,-3,-2,-1,    1, 2, 3, 4)
  lag1 = ( 0, 0, 0,    0, 0, 0,
           1, 1, 1, 1, 1, 1, 1)
  lag2 = (-3,-2,-1,    1, 2, 3,
          -3,-2,-1, 0, 1, 2, 3)
  lpf.apply(lag1,lag2,f,g)
  #n = len(a)
  #for i in range(n):
  #  plot(a[i])
  return g

f = readData()
plot(f)
g = filter(20,f)
plot(g)
r = Array.sub(g,f)
plot(r)
