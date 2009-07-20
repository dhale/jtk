import sys
from math import *
from java.awt import *
from java.lang import *
from java.util import *
from javax.swing import *

from edu.mines.jtk.awt import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.sgl import *
from edu.mines.jtk.sgl.test import *
from edu.mines.jtk.util import *
from edu.mines.jtk.util.ArrayMath import *

#############################################################################
# functions

def main(args):
  doInterpExample1()
  #doSmoothExample1()
  #doTargetExample2()
  #doTargetExample3()
  return

def getNearestNeighbors(t,x,n1):
  """Returns a sampled distance map d and nearest neighbor interpolant y 
     for specified lists t and x that define a sampled function x(t). 
     n1 is the number of samples in the returned 1D arrays d and y."""
  nt = len(t)
  d = zerofloat(n1)
  y = zerofloat(n1)
  for i1 in range(n1):
    kt = binarySearch(t,i1)
    if kt<0:
      kt = -1-kt
    if kt>=nt:
      kt = nt-1
    di = abs(t[kt]-i1)
    if kt>0:
      dm = abs(t[kt-1]-i1)
      if dm<di:
        di = dm
        kt -= 1
    d[i1] = di
    y[i1] = x[kt];
  return d,y

def doInterpExample1():
  """An example of 1D interpolation using local smoothing of a sampled
     nearest-neighbor interpolant."""
  n1 = 315
  t = [ 60.0, 100.0, 170.0, 200.0, 250.0]
  x = [  1.0,   2.0,   2.7,   3.0,   2.0]
  d,y = getNearestNeighbors(t,x,n1)
  #d = clip(0.0,25.0,d)
  s = mul(d,d)
  #s = add(s,64.0) # for smoothing instead of interpolation
  SimplePlot.asPoints(d)
  SimplePlot.asPoints(s)
  sp = SimplePlot()
  pvx = sp.addPoints(t,x)
  pvx.setMarkStyle(PointsView.Mark.HOLLOW_CIRCLE)
  pvx.setLineStyle(PointsView.Line.NONE)
  pvy = sp.addPoints(y)
  pvy.setLineStyle(PointsView.Line.DASH)
  lsf = LocalSmoothingFilter()
  for c,color in [(0.1,Color.RED),(0.3,Color.GREEN),(0.5,Color.BLUE)]:
    z = zerofloat(n1)
    lsf.apply(c,s,y,z)
    pvz = sp.addPoints(z)
    pvz.setLineColor(color)

def doSmoothExample1():
  n1 = 315
  sigma = 10.0
  c = 0.5*sigma*sigma;
  s = abs(rampfloat(1.0,-2.0/(n1-1),n1))
  s = mul(s,s)
  x = zerofloat(n1)
  x[1*n1/8] = 1.0
  x[2*n1/8] = 1.0
  x[3*n1/8] = 1.0
  x[4*n1/8] = 1.0
  x[5*n1/8] = 1.0
  x[6*n1/8] = 1.0
  x[7*n1/8] = 1.0
  lsf = LocalSmoothingFilter()
  y = zerofloat(n1)
  lsf.apply(c,s,x,y)
  SimplePlot.asPoints(s);
  SimplePlot.asPoints(x);
  SimplePlot.asPoints(y);

def doTargetExample2():
  sigma = 10.0
  scale = 0.5*sigma*sigma
  small = 0.01
  niter = 100
  n1,n2 = 315,315
  x = makeRandomImage2(n1,n2)
  plot2(x)
  t = makeTargetImage2(n1,n2)
  d = makeTensors2(t)
  lsf = LocalSmoothingFilter(small,niter)
  y = copy(x)
  #y = zerofloat(n1,n2)
  lsf.apply(d,scale,x,y)
  plot2(y)

def doTargetExample3():
  sigma = 10.0
  scale = 0.5*sigma*sigma
  small = 0.01
  niter = 100
  n1,n2,n3 = 101,101,101
  x = makeRandomImage3(n1,n2,n3)
  #plot3(x)
  t = makeTargetImage3(n1,n2,n3)
  #plot3(t)
  d = makeTensors3(t)
  lsf = LocalSmoothingFilter(small,niter)
  y = copy(x)
  #y = zerofloat(n1,n2,n3)
  lsf.apply(d,scale,x,y)
  plot3(y)

def makeRandomImage2(n1,n2):
  r = sub(randfloat(n1,n2),0.5)
  r = smooth2(r)
  return r

def makeBlockyImage2(n1,n2):
  r = zerofloat(n1,n2)
  for i2 in range(0,n2/2):
    for i1 in range(n1):
      r[i2][i1] = 1.0
  for i2 in range(n2/2,n2):
    for i1 in range(0,n1/2):
      r[i2][i1] = 2.0
    for i1 in range(n1/2,n1):
      r[i2][i1] = 3.0
  return r

def makeRandomImage3(n1,n2,n3):
  r = sub(randfloat(n1,n2,n3),0.5)
  r = smooth3(r)
  return r

def makeTargetImage2(n1,n2):
  k = 0.3
  c1,c2 = n1/2,n2/2
  f = zerofloat(n1,n2)
  for i2 in range(n2):
    d2 = i2-c2
    for i1 in range(n1):
      d1 = i1-c1
      f[i2][i1] = 10.0*sin(k*sqrt(d1*d1+d2*d2))
  return f

def makeTargetImage3(n1,n2,n3):
  k = 0.3
  c1,c2,c3 = n1/2,n2/2,n3/2
  f = zerofloat(n1,n2,n3)
  for i3 in range(n3):
    d3 = i3-c3
    for i2 in range(n2):
      d2 = i2-c2
      for i1 in range(n1):
        d1 = i1-c1
        f[i3][i2][i1] = 10.0*sin(k*sqrt(d1*d1+d2*d2+d3*d3))
  return f

def smooth2(x):
  n1,n2 = len(x[0]),len(x)
  y = zerofloat(n1,n2)
  rgf = RecursiveGaussianFilter(1.0)
  rgf.apply00(x,y)
  return y

def smooth3(x):
  n1,n2,n3 = len(x[0][0]),len(x[0]),len(x)
  y = zerofloat(n1,n2,n3)
  rgf = RecursiveGaussianFilter(1.0)
  rgf.apply000(x,y)
  return y

def makeTensors2(x):
  n1,n2 = len(x[0]),len(x)
  lof = LocalOrientFilter(8)
  d = lof.applyForTensors(x)
  for i2 in range(n2):
    for i1 in range(n1):
      d.setEigenvalues(i1,i2,0.0,1.0)
  return d

def makeTensors3(x):
  n1,n2,n3 = len(x[0][0]),len(x[0]),len(x)
  lof = LocalOrientFilter(8)
  d = lof.applyForTensors(x)
  d.setEigenvalues(0.0,1.0,1.0)
  return d

#############################################################################
# plot

def plot2(f):
  sp = SimplePlot(SimplePlot.Origin.UPPER_LEFT)
  sp.setSize(750,750)
  pv = sp.addPixels(f)

def plot3(f):
  world = World()
  ipg = ImagePanelGroup(f)
  world.addChild(ipg)
  frame = TestFrame(world)
  frame.setVisible(True)

#############################################################################
# Do everything on Swing thread.

class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain())
