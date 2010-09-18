import sys
from math import *
from java.awt import *
from java.lang import *
from java.util import *
from javax.swing import *

from edu.mines.jtk.awt import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.interp import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.sgl import *
from edu.mines.jtk.util import *
from edu.mines.jtk.util.ArrayMath import *

#############################################################################
# demos

def main(args):
  demoCircleData2()
  #demoSphereData3()
  return

def demoCircleData2():
  n1,n2 = 401,401
  kr,kt = 40,80
  fnull = -999.0
  s1,s2 = Sampling(n1),Sampling(n2)
  f,x1,x2 = makeCircleData2(n1,n2,kr,kt)
  for au in [1.00,0.01]:
    sg = SimpleGridder2(f,x1,x2)
    sg.setNullValue(fnull)
    p = sg.grid(s1,s2)
    d = makeCircleTensors2(n1,n2,au)
    bg = BlendedGridder2(d,f,x1,x2)
    bg.setSmoothness(0.5)
    bg.setTimeMarkerX(True)
    t = bg.gridNearest(fnull,p)
    q = copy(p)
    bg.gridBlended(t,p,q)
    checkKnownSamples2(t,p,q)
    cmap,cbwm = jet,150
    hlabel,vlabel = "Sample","Sample"
    width,height = 883,766
    plot2(f,x1,x2,t,cmap=cmap,cbwm=cbwm,cbar="Distance (time)",
          title="Distance (time)",hlabel=hlabel,vlabel=vlabel,
          width=width,height=height)
    plot2(f,x1,x2,p,cmap=cmap,cbwm=cbwm,cbar="Interpolated value",
          title="Nearest neighbor",hlabel=hlabel,vlabel=vlabel,
          width=width,height=height)
    plot2(f,x1,x2,q,cmap=cmap,cbwm=cbwm,cbar="Interpolated value",
          title="Blended neighbor",hlabel=hlabel,vlabel=vlabel,
          width=width,height=height)

def demoSphereData3():
  n1,n2,n3 = 101,101,101
  kr,kt,kp = 10,20,20
  fnull = -999.0
  s1,s2,s3 = Sampling(n1),Sampling(n2),Sampling(n3)
  f,x1,x2,x3 = makeSphereData3(n1,n2,n3,kr,kt,kp)
  for tmx in [False,True]:
    sg = SimpleGridder3(f,x1,x2,x3)
    sg.setNullValue(fnull)
    p = sg.grid(s1,s2,s3)
    d = makeSphereTensors3(n1,n2,n3,0.01)
    bg = BlendedGridder3(d,f,x1,x2,x3)
    bg.setSmoothness(0.5)
    bg.setTimeMarkerX(tmx)
    t = bg.gridNearest(fnull,p)
    q = copy(p)
    bg.gridBlended(t,p,q)
    checkKnownSamples3(t,p,q)
    slices = (68,31,22)
    plot3(f,x1,x2,x3,t,slices=slices)
    plot3(f,x1,x2,x3,p,slices=slices)
    plot3(f,x1,x2,x3,q,slices=slices)

def checkKnownSamples2(t,p,q):
  print "checkKnownSamples2: begin"
  n1,n2 = len(t[0]),len(t)
  for i2 in range(n2):
    for i1 in range(n1):
      if t[i2][i1]==0.0:
        pi = p[i2][i1]
        qi = q[i2][i1]
        if abs(qi-pi)>0.00001:
          print "i1 =",i1,"i2 =",i2," p =",pi," q =",qi
  print "checkKnownSamples2: end"

def checkKnownSamples3(t,p,q):
  print "checkKnownSamples3: begin"
  n1,n2,n3 = len(t[0][0]),len(t[0]),len(t)
  for i3 in range(n3):
    for i2 in range(n2):
      for i1 in range(n1):
        if t[i3][i2][i1]==0.0:
          pi = p[i3][i2][i1]
          qi = q[i3][i2][i1]
          if abs(qi-pi)>0.00001:
            print "i1 =",i1,"i2 =",i2,"i3 =",i3," p =",pi," q =",qi
  print "checkKnownSamples3: end"

#############################################################################
# scattered data to be interpolated

def makeCircleData2(n1,n2,kr,kt):
  """
  Returns scattered data for samples uniformly spaced along circular arcs.
  kr is the radial spacing between impulses, in samples 
  kt is the angular (theta) spacing between impulses, in samples
  """
  f,x1,x2 = [],[],[]
  fi = 1.0
  nr = int(sqrt(n1*n1+n2*n2))
  mr = nr/kr
  jr = (nr-(mr-1)*kr)/2
  for ir in range(jr,nr,kr):
    r = ir
    nt = int(0.5*PI*r)
    mt = nt/kt
    jt = (nt-(mt-1)*kt)/2
    for it in range(jt,nt,kt):
      t = 0.5*PI*it/nt
      i1 = int(r*cos(t)+0.5)
      i2 = int(r*sin(t)+0.5)
      if i1<n1 and i2<n2:
        f.append(fi)
        x1.append(float(i1))
        x2.append(float(i2))
    if fi==0.0: fi = 1.0
    else: fi = 0.0
  """
  for i in range(8,min(n1,n2),8):
    f.append(fi)
    x1.append(float(i))
    x2.append(float(i))
    if fi==0.0: fi = 1.0
    else: fi = 0.0
  """
  return f,x1,x2

def makeSphereData3(n1,n2,n3,kr,kt,kp):
  """
  Returns an image of impulses uniformly spaced along spherical shells.
  kr is the radial spacing between impulses, in samples 
  kt is the polar angular (theta) spacing between impulses, in samples
  kp is the azimuthal angular (phi) spacing between impulses, in samples
  """
  f,x1,x2,x3 = [],[],[],[]
  fi = 1.0
  nr = int(sqrt(n1*n1+n2*n2+n3*n3))
  mr = nr/kr
  jr = (nr-(mr-1)*kr)/2
  for ir in range(jr,nr,kr):
    r = ir
    nt = int(0.5*PI*r)
    mt = nt/kt
    jt = (nt-(mt-1)*kt)/2
    for it in range(jt,nt,kt):
      t = 0.5*PI*it/nt
      np = int(0.5*PI*r*sin(t))
      mp = np/kp
      jp = (np-(mp-1)*kp)/2
      for ip in range(jp,np,kp):
        p = 0.5*PI*ip/np
        i1 = int(r*cos(t)+0.5)
        i2 = int(r*sin(t)*cos(p)+0.5)
        i3 = int(r*sin(t)*sin(p)+0.5)
        if i1<n1 and i2<n2 and i3<n3:
          f.append(fi)
          x1.append(float(i1))
          x2.append(float(i2))
          x3.append(float(i3))
    if fi==0.0: fi = 1.0
    else: fi = 0.0
  """
  for i in range(3,min(n1,n2,n3),3):
    f.append(fi)
    x1.append(float(i))
    x2.append(float(i))
    x3.append(float(i))
    if fi==0.0: fi = 1.0
    else: fi = 0.0
  f.append(fi)
  x1.append(4.0)
  x2.append(4.0)
  x3.append(4.0)
  """
  return f,x1,x2,x3

#############################################################################
# tensor fields

def makeCircleTensors2(n1,n2,au):
  """ 
  Returns tensors for smoothing along concentric circular arcs.
  """
  c1,c2 = 0.01,0.01
  u1 = zerofloat(n1,n2)
  u2 = zerofloat(n1,n2)
  au = fillfloat( au,n1,n2)
  av = fillfloat(1.0,n1,n2)
  for i2 in range(n2):
    x2 = i2-c2
    for i1 in range(n1):
      x1 = i1-c1
      xs = 1.0/sqrt(x1*x1+x2*x2)
      u1[i2][i1] = xs*x1
      u2[i2][i1] = xs*x2
  return EigenTensors2(u1,u2,au,av)

def makeSphereTensors3(n1,n2,n3,au):
  """ 
  Returns tensors for smoothing within concentric spherical surfaces.
  """
  c1,c2,c3 = 0.01,0.01,0.01
  u1 = zerofloat(n1,n2,n3)
  u2 = zerofloat(n1,n2,n3)
  w1 = zerofloat(n1,n2,n3)
  w2 = zerofloat(n1,n2,n3)
  au = fillfloat( au,n1,n2,n3)
  av = fillfloat(1.0,n1,n2,n3)
  aw = fillfloat(1.0,n1,n2,n3)
  for i3 in range(n3):
    x3 = i3-c3
    for i2 in range(n2):
      x2 = i2-c2
      for i1 in range(n1):
        x1 = i1-c1
        xu = 1.0/sqrt(x1*x1+x2*x2+x3*x3)
        xw = 1.0/sqrt(x1*x1+x2*x2)
        u1[i3][i2][i1] =  xu*x1
        u2[i3][i2][i1] =  xu*x2
        w1[i3][i2][i1] = -xw*x2
        w2[i3][i2][i1] =  xw*x1 
  return EigenTensors3(u1,u2,w1,w2,au,av,aw,True)

#############################################################################
# plots

gray = ColorMap.GRAY
jet = ColorMap.JET

def plot2(f,x1,x2,g,cmin=0,cmax=0,cmap=gray,cbar=None,cbwm=None,
          title=None,hlabel=None,vlabel=None,
          width=750,height=750):
  sp = SimplePlot(SimplePlot.Origin.LOWER_LEFT)
  sp.setFontSizeForSlide(1.0,1.0)
  sp.setSize(width,height)
  pv = sp.addPixels(g)
  pv.setColorModel(cmap)
  pv.setInterpolation(PixelsView.Interpolation.NEAREST)
  if cmin!=cmax:
    pv.setClips(cmin,cmax)
  if cbar:
    sp.addColorBar(cbar)
    if cbwm:
      sp.plotPanel.setColorBarWidthMinimum(cbwm)
  pv = sp.addPoints(x1,x2)
  pv.setLineStyle(PointsView.Line.NONE)
  pv.setMarkStyle(PointsView.Mark.HOLLOW_CIRCLE)
  pv.setMarkColor(Color.WHITE)
  pv.setMarkSize(4)
  n1,n2 = len(g[0]),len(g)
  sp.setLimits(0,0,n1-1,n2-1)
  if title:
    sp.setTitle(title)
  if hlabel:
    sp.setHLabel(hlabel)
  if vlabel:
    sp.setVLabel(vlabel)

def plot3(f,x1,x2,x3,g,cmin=0,cmax=0,slices=None):
  n1,n2,n3 = len(g[0][0]),len(g[0]),len(g)
  world = World()
  sf = SimpleFrame()
  ipg = sf.addImagePanels(copy(g))
  ipg.setColorModel(jet)
  if slices:
    ipg.setSlices(slices[0],slices[1],slices[2])
  if cmin!=cmax:
    ipg.setClips(cmin,cmax)
  sf.setSize(950,950)
  sf.orbitView.setScale(2.0)

#############################################################################
# do everything on Swing thread.

class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain())
