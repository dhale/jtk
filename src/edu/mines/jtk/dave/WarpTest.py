from math import *
from java.awt import *
from java.lang import *
from edu.mines.jtk.dave import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.io import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.util import *

True = 1
False = 0

n1 = 315
n2 = 315

def readData():
  bo = DataFile.ByteOrder.LITTLE_ENDIAN
  df = DataFile("/data/seis/vg/junks.dat","r",bo)
  f = Array.zerofloat(n1,n2)
  df.readFloats(f)
  return f

def plot(x,png=None):
  panel = PlotPanel(PlotPanel.Orientation.X1DOWN_X2RIGHT)
  panel.setVFormat("%4.0f");
  panel.addColorBar()
  panel.setColorBarFormat("%4.2f");
  pv = panel.addPixels(x)
  pv.setColorMap(PixelsView.ColorMap.GRAY);
  frame = PlotFrame(panel)
  frame.setBackground(Color.WHITE)
  frame.setFontSize(18)
  frame.setSize(700,600)
  frame.setVisible(True)
  if png!=None:
    frame.paintToPng(600,3,png)
  return frame

def plotu(u,png=None,clip=0):
  panel = PlotPanel(PlotPanel.Orientation.X1DOWN_X2RIGHT)
  panel.setVFormat("%4.0f");
  panel.addColorBar()
  panel.setColorBarFormat("%4.2f");
  panel.setHLimits(-0.5,n2-0.5)
  panel.setVLimits(-0.5,n1-0.5)
  if len(u[0])<n1:
    s1 = Sampling(len(u[0]),3.0,1.0)
  else:
    s1 = Sampling(n1,1.0,0.0)
  if len(u)<n2:
    s2 = Sampling(len(u),3.0,1.0)
  else:
    s2 = Sampling(n2,1.0,0.0)
  pv = panel.addPixels(s1,s2,u)
  pv.setColorMap(PixelsView.ColorMap.JET);
  if clip!=0:
    pv.setClips(-clip,clip)
  frame = PlotFrame(panel)
  frame.setBackground(Color.WHITE)
  frame.setFontSize(18)
  frame.setSize(700,600)
  frame.setVisible(True)
  if png!=None:
    frame.paintToPng(600,3,png)
  return frame

def plotWithGrid(x,g=None,png=None):
  panel = PlotPanel(PlotPanel.Orientation.X1DOWN_X2RIGHT)
  panel.setVFormat("%4.0f");
  panel.addColorBar()
  panel.setColorBarFormat("%4.2f");
  pv = panel.addPixels(x)
  pv.setColorMap(PixelsView.ColorMap.GRAY);
  if g:
    vg = panel.getTile(0,0).addTiledView(warpView(g))
  else:
    vg = panel.getTile(0,0).addTiledView(gridView(len(x[0]),len(x)))
  frame = PlotFrame(panel)
  frame.setBackground(Color.WHITE)
  frame.setFontSize(18)
  frame.setSize(700,600)
  frame.setVisible(True)
  if png!=None:
    frame.paintToPng(600,3,png)
  return frame

def gridView(n1,n2):
  k1 = 15
  k2 = 15
  j1 = (k1-1)/2
  j2 = (k2-1)/2
  m1 = 1+(n1-j1-1)/k1
  m2 = 1+(n2-j2-1)/k2
  ns = 2+(n1-1)/k1+(n2-1)/k2
  s1 = []
  s2 = []
  for i2 in range(j2,n2,k2):
    u1 = Array.rampfloat(j1,k1,m1)
    u2 = Array.fillfloat(i2,m1)
    s1.append(u1)
    s2.append(u2)
  for i1 in range(j1,n1,k1):
    u1 = Array.fillfloat(i1,m1)
    u2 = Array.rampfloat(j2,k2,m2)
    s1.append(u1)
    s2.append(u2)
  pv = PointsView(s1,s2)
  pv.setOrientation(PointsView.Orientation.X1DOWN_X2RIGHT);
  return pv

def warpView(y):
  y1,y2 = y[0],y[1]
  n1 = len(y1[0])
  n2 = len(y1)
  k1 = 15
  k2 = 15
  j1 = (k1-1)/2
  j2 = (k2-1)/2
  m1 = 1+(n1-j1-1)/k1
  m2 = 1+(n2-j2-1)/k2
  ns = 2+(n1-1)/k1+(n2-1)/k2
  s1 = []
  s2 = []
  for i2 in range(j2,n2,k2):
    u1 = Array.zerofloat(m1)
    u2 = Array.zerofloat(m1)
    iu = 0
    for i1 in range(j1,n1,k1):
      u1[iu] = y1[i2][i1]
      u2[iu] = y2[i2][i1]
      iu = iu+1
    s1.append(u1)
    s2.append(u2)
  for i1 in range(j1,n1,k1):
    u1 = Array.zerofloat(m2)
    u2 = Array.zerofloat(m2)
    iu = 0
    for i2 in range(j2,n2,k2):
      u1[iu] = y1[i2][i1]
      u2[iu] = y2[i2][i1]
      iu = iu+1
    s1.append(u1)
    s2.append(u2)
  pv = PointsView(s1,s2)
  pv.setOrientation(PointsView.Orientation.X1DOWN_X2RIGHT);
  return pv

def lcc(sigma,f,g):
  n2 = len(f)
  n1 = len(f[0])
  lcf = LocalCorrelationFilter(sigma)
  ff = Array.zerofloat(n1,n2)
  gg = Array.zerofloat(n1,n2)
  lcf.apply(0,0,f,f,ff)
  lcf.apply(0,0,g,g,gg)
  s = Array.div(1.0,Array.sqrt(Array.mul(ff,gg)));
  r = Array.zerofloat(n1,n2)
  c = Array.zerofloat(n1,n2)
  for lag2 in range(-7,8,1):
    for lag1 in range(-7,8,1):
      lcf.apply(lag1,lag2,f,g,r)
      r = Array.mul(r,s)
      Array.copy(n1/15,n2/15,7,7,15,15,r,7+lag1,7+lag2,15,15,c)
  return c

a1 = 0.20
a2 = 0.10
b1 = (n1-1)/2
b2 = (n2-1)/2
s1 = 50
s2 = 50

x = WarpTest.warpGauss(a1,a2,b1,b2,s1,s2,n1,n2)
x1,x2 = x[0],x[1]
#plot(x1)
#plot(x2)

y = WarpTest.unwarpGauss(a1,a2,b1,b2,s1,s2,n1,n2)
y1,y2 = y[0],y[1]
#plot(y1)
#plot(y2)

z1 = Array.rampfloat(0.0,1.0,0.0,n1,n2)
z2 = Array.rampfloat(0.0,0.0,1.0,n1,n2)
e1 = Array.sub(y1,z1)
e2 = Array.sub(y2,z2)
plotu(e1,"e1.png",6)
plotu(e2,"e2.png",3)

p = readData()
q = WarpTest.warp(x1,x2,p)
r = WarpTest.warp(y1,y2,q)
#plotWithGrid(p,None,"image1.png")
#plotWithGrid(q,y,"image2.png")
#c = lcc(8,p,q)
#plot(c,"lcc.png")

u = WarpTest.findWarpL(8,7,7,p,q)
u1 = u[0]
u2 = u[1]
plotu(u1,"i1.png",6)
plotu(u2,"i2.png",3)

u = WarpTest.findWarpU(8,7,7,p,q)
u1 = u[0]
u2 = u[1]
plotu(u1,"u1.png",6)
plotu(u2,"u2.png",3)
