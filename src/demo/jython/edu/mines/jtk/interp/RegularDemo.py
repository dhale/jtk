"""
Demonstrates interpolation of values sampled on regular (but non-uniform)
grids, using bilinear, bicubic, trilinear and tricubic methods.
@author: Dave Hale, Colorado School of Mines
"""

import sys
from java.awt import *
from java.lang import *
from java.util import *
from javax.swing import *

from edu.mines.jtk.awt import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.interp import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.sgl import *
from edu.mines.jtk.util.ArrayMath import *

def main(args):
  demo2()
  demo3()

def demo2():
  s1,s2,f = makeImage2()
  x1 = randomSamples(10,s1)
  x2 = randomSamples(10,s2)
  n1 = len(x1)
  n2 = len(x2)
  g = zerofloat(n1,n2)
  x1s = zerofloat(n1*n2)
  x2s = zerofloat(n1*n2)
  for i2 in range(n2):
    for i1 in range(n1):
      g[i2][i1] = function2(x1[i1],x2[i2])
      x1s[i1+i2*n1] = x1[i1]
      x2s[i1+i2*n1] = x2[i2]
  mm = BicubicInterpolator2.Method.MONOTONIC
  ms = BicubicInterpolator2.Method.SPLINE
  bli = BilinearInterpolator2(x1,x2,g)
  bcm = BicubicInterpolator2(mm,mm,x1,x2,g)
  bcs = BicubicInterpolator2(ms,ms,x1,x2,g)
  hli = bli.interpolate(s1,s2)
  hcm = bcm.interpolate(s1,s2)
  hcs = bcs.interpolate(s1,s2)
  plot2(s1,s2,f,"Original")
  plot2(s1,s2,hli,"Bilinear",x1s,x2s)
  plot2(s1,s2,hcm,"Bicubic (monotonic)",x1s,x2s)
  plot2(s1,s2,hcs,"Bicubic (spline)",x1s,x2s)

def demo3():
  s1,s2,s3,f = makeImage3()
  x1 = randomSamples(8,s1)
  x2 = randomSamples(8,s2)
  x3 = randomSamples(8,s3)
  n1 = len(x1)
  n2 = len(x2)
  n3 = len(x3)
  g = zerofloat(n1,n2,n3)
  xs = zerofloat(3*n1*n2*n3)
  ix = 0
  for i3 in range(n3):
    for i2 in range(n2):
      for i1 in range(n1):
        g[i3][i2][i1] = function3(x1[i1],x2[i2],x3[i3])
        xs[ix+0] = x3[i3]
        xs[ix+1] = x2[i2]
        xs[ix+2] = x1[i1]
        ix += 3
  mm = TricubicInterpolator3.Method.MONOTONIC
  ms = TricubicInterpolator3.Method.SPLINE
  tli = TrilinearInterpolator3(x1,x2,x3,g)
  tcm = TricubicInterpolator3(mm,mm,mm,x1,x2,x3,g)
  tcs = TricubicInterpolator3(ms,ms,ms,x1,x2,x3,g)
  hli = tli.interpolate(s1,s2,s3)
  hcm = tcm.interpolate(s1,s2,s3)
  hcs = tcs.interpolate(s1,s2,s3)
  plot3(s1,s2,s3,f,"Original")
  plot3(s1,s2,s3,hli,"Trilinear",xs)
  plot3(s1,s2,s3,hcm,"Tricubic (monotonic)",xs)
  plot3(s1,s2,s3,hcs,"Tricubic (spline)",xs)

def plot2(s1,s2,f,title=None,x1s=None,x2s=None):
  sp = SimplePlot()
  sp.setSize(600,650)
  if title:
    sp.setTitle(title)
  sp.setLimits(s1.first,s2.first,s1.last,s2.last)
  pv = sp.addPixels(s1,s2,f)
  pv.setColorModel(ColorMap.JET)
  if x1s and x2s:
    pv = sp.addPoints(x1s,x2s)
    pv.setLineStyle(PointsView.Line.NONE)
    pv.setMarkStyle(PointsView.Mark.FILLED_CIRCLE)
    pv.setMarkSize(4)
    pv.setMarkColor(Color.WHITE)

def plot3(s1,s2,s3,f,title=None,xs=None):
  sf = SimpleFrame()
  if title:
    sf.setTitle(title)
  ipg = sf.addImagePanels(s1,s2,s3,f)
  ipg.setSlices(s1.count/4,s2.count/4,s3.count/4)
  ipg.setColorModel(ColorMap.JET)
  if xs:
    pg = PointGroup(xs)
    ss = StateSet()
    cs = ColorState()
    cs.setColor(Color.BLACK)
    ss.add(cs)
    ps = PointState()
    ps.setSize(4.0)
    ss.add(ps)
    pg.setStates(ss)
    sf.world.addChild(pg)
  sf.orbitView.setScale(2.0)
  sf.setSize(600,650)

def makeImage2():
  n1,n2 = 201,202
  d1,d2 = 1.0/(n1-1),1.0/(n2-1)
  f1,f2 = 0.0,0.0
  s1,s2 = Sampling(n1,d1,f1),Sampling(n2,d2,f2)
  f1 = sin(rampfloat(0,2*PI/(n1-1),0,n1,n2))
  f2 = sin(rampfloat(0,0,2*PI/(n2-1),n1,n2))
  f = mul(f1,f2)
  return s1,s2,f
def function2(x1,x2):
  return sin(2*PI*x1)*sin(2*PI*x2)

def makeImage3():
  n1,n2,n3 = 201,202,203
  d1,d2,d3 = 1.0/(n1-1),1.0/(n2-1),1.0/(n3-1)
  f1,f2,f3 = 0.0,0.0,0.0
  s1,s2,s3 = Sampling(n1,d1,f1),Sampling(n2,d2,f2),Sampling(n3,d3,f3)
  f1 = sin(rampfloat(0,2*PI/(n1-1),0,0,n1,n2,n3))
  f2 = sin(rampfloat(0,0,2*PI/(n2-1),0,n1,n2,n3))
  f3 = sin(rampfloat(0,0,0,2*PI/(n3-1),n1,n2,n3))
  f = mul(mul(f1,f2),f3)
  return s1,s2,s3,f
def function3(x1,x2,x3):
  return sin(2*PI*x1)*sin(2*PI*x2)*sin(2*PI*x3)

def randomSamples(nr,s):
  r = randfloat(nr)
  rmin = min(r)
  rmax = max(r)
  smin = s.first
  smax = s.last
  r = add(smin,mul((smax-smin)/(rmax-rmin),sub(r,rmin)))
  quickSort(r)
  return r

#############################################################################
# Run everything on Swing thread.
class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain())
