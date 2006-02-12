from math import *
from java.awt import *
from edu.mines.jtk.dave import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.io import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.util import *

True = 1
False = 0

def plot1(f,g):
  fmin = Array.min(f)
  fmax = Array.max(f)
  print "fmin =",fmin,"  fmax =",fmax
  panel = PlotPanel()
  pvf = panel.addPoints(f)
  pvg = panel.addPoints(g)
  pvg.setLineColor(Color.RED)
  frame = PlotFrame(panel)
  frame.setBackground(Color.WHITE)
  frame.setFontSize(18)
  frame.setSize(800,600)
  frame.setVisible(True)
  return frame

def exact0(sigma,n1):
  g = Array.zerofloat(n1)
  k = n1/2
  s = 1.0/(sqrt(2.0*pi)*sigma)
  for i in range(n1):
    x = float(i-k)
    g[i] = s*exp(-0.5*x*x/(sigma*sigma))
  return g

def exact1(sigma,n1):
  g = Array.zerofloat(n1)
  k = n1/2
  s = 1.0/(sqrt(2.0*pi)*sigma*sigma*sigma)
  for i in range(n1):
    x = float(i-k)
    g[i] = -s*x*exp(-0.5*x*x/(sigma*sigma))
  return g

def exact2(sigma,n1):
  g = Array.zerofloat(n1)
  k = n1/2
  s = 1.0/(sqrt(2.0*pi)*sigma*sigma*sigma)
  for i in range(n1):
    x = float(i-k)
    g[i] = -s*(1.0-(x*x)/(sigma*sigma))*exp(-0.5*x*x/(sigma*sigma))
  return g

def plot2(f):
  fmin = Array.min(f)
  fmax = Array.max(f)
  #f = Array.mul(1.0/fmax,f)
  print "fmin =",fmin,"  fmax =",fmax
  panel = PlotPanel(PlotPanel.Orientation.X1DOWN_X2RIGHT)
  panel.addColorBar()
  pv = panel.addPixels(f)
  pv.setColorMap(PixelsView.ColorMap.JET);
  #pv.setInterpolation(PixelsView.Interpolation.NEAREST)
  frame = PlotFrame(panel)
  frame.setBackground(Color.WHITE)
  frame.setFontSize(18)
  frame.setSize(800,600)
  frame.setVisible(True)
  return frame

def other2(rgf,f,g):
  rgf.apply10(f,g)
  plot2(g)
  rgf.apply01(f,g)
  plot2(g)
  rgf.apply20(f,g)
  plot2(g)
  rgf.apply11(f,g)
  plot2(g)
  rgf.apply02(f,g)
  plot2(g)

def test1():
  sigma = 10;
  n1 = 1+20*sigma
  f = Array.zerofloat(n1)
  #f[3] = f[n1-4] = f[n1/2] = 1.0
  f[n1/2] = 1.0
  g = Array.zerofloat(n1)
  rgf = RecursiveGaussianFilter(sigma)
  rgf.apply0(f,g)
  e = exact0(sigma,n1)
  frame = plot1(g,e)
  rgf.apply1(f,g)
  e = exact1(sigma,n1)
  frame = plot1(g,e)
  rgf.apply2(f,g)
  e = exact2(sigma,n1)
  frame = plot1(g,e)

def test2():
  sigma = 10
  n1 = 1+20*sigma
  n2 = n1
  f = Array.zerofloat(n1,n2)
  f[3][3] = f[3][n1-4] = f[n2-4][3] = f[n2-4][n1-4] = f[n2/2][n1/2] = 1.0

  g = Array.zerofloat(n1,n2)
  rgf = RecursiveGaussianFilter(sigma)

  rgf.apply00(f,g)
  frame = plot2(g)
  #frame.paintToPng(600,3,"rgf.png")
  other2(rgf,f,g)

test1()
test2()
