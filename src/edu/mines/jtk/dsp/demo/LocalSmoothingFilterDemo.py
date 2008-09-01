import sys
from math import *
from java.lang import *
from java.util import *
from javax.swing import *

from edu.mines.jtk.awt import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.sgl import *
from edu.mines.jtk.sgl.test import *
from edu.mines.jtk.util import *

#############################################################################
# functions

def main(args):
  doTargetExample2()
  doTargetExample3()
  return

def doTargetExample2():
  n1,n2 = 315,315
  scale = 100
  small = 0.01
  niter = 100
  x = makeRandomImage2(n1,n2)
  plot2(x)
  t = makeTargetImage2(n1,n2)
  d = makeTensors2(t)
  lsf = LocalSmoothingFilter(scale,small,niter)
  y = Array.zerofloat(n1,n2)
  lsf.apply(d,x,y)
  plot2(y)

def doTargetExample3():
  n1,n2,n3 = 101,101,101
  scale = 100
  small = 0.01
  niter = 100
  x = makeRandomImage3(n1,n2,n3)
  #plot3(x)
  t = makeTargetImage3(n1,n2,n3)
  #plot3(t)
  d = makeTensors3(t)
  lsf = LocalSmoothingFilter(scale,small,niter)
  y = Array.zerofloat(n1,n2,n3)
  lsf.apply(d,x,y)
  plot3(y)

def makeRandomImage2(n1,n2):
  return smooth2(Array.sub(Array.randfloat(n1,n2),0.5))

def makeRandomImage3(n1,n2,n3):
  return smooth3(Array.sub(Array.randfloat(n1,n2,n3),0.5))

def makeTargetImage2(n1,n2):
  k = 0.3
  c1,c2 = n1/2,n2/2
  f = Array.zerofloat(n1,n2)
  for i2 in range(n2):
    d2 = i2-c2
    for i1 in range(n1):
      d1 = i1-c1
      f[i2][i1] = 10.0*sin(k*sqrt(d1*d1+d2*d2))
  return f

def makeTargetImage3(n1,n2,n3):
  k = 0.3
  c1,c2,c3 = n1/2,n2/2,n3/2
  f = Array.zerofloat(n1,n2,n3)
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
  y = Array.zerofloat(n1,n2)
  rgf = RecursiveGaussianFilter(1.0)
  rgf.apply00(x,y)
  return y

def smooth3(x):
  n1,n2,n3 = len(x[0][0]),len(x[0]),len(x)
  y = Array.zerofloat(n1,n2,n3)
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

def plot3(f,clip=0.0):
  world = World()
  ipg = ImagePanelGroup(f)
  if clip!=0.0:
    ipg.setClips(-clip,clip)
  else:
    ipg.setPercentiles(0.0,100.0)
  world.addChild(ipg)
  frame = TestFrame(world)
  frame.setVisible(True)

#############################################################################
# Do everything on Swing thread.

class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain())
