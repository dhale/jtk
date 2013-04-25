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
from edu.mines.jtk.util import *
from edu.mines.jtk.util.ArrayMath import *

#############################################################################
# demos

def main(args):
  #demoCircleChirp2()
  demoCircleImpulses2()
  #demoCircleRandom2()
  demoSphereImpulses3()
  #demoSphereRandom3()
  return

lowpass,simple = "lowpass","simple"
chirp,impulses,random ="chirp","impulses","random"
def demoCircleChirp2(smooth=lowpass):
  demoCircle2(chirp,smooth)
def demoCircleImpulses2(smooth=lowpass):
  demoCircle2(impulses,smooth)
def demoCircleRandom2(smooth=lowpass):
  demoCircle2(random,smooth)
def demoSphereImpulses3(smooth=lowpass):
  demoSphere3(impulses,smooth)
def demoSphereRandom3(smooth=lowpass):
  demoSphere3(random,smooth)

def demoCircle2(input,smooth):
  cmap,cbar,cbwm = gray,"Amplitude",150
  hlabel,vlabel = "Sample","Sample"
  width,height = 883,766
  if input==chirp:
    n1,n2 = 401,401
    kmax = 0.4
    f = makeCircleChirp2(n1,n2,kmax)
    cmin,cmax = -1.0,1.0
  elif input==impulses:
    n1,n2 = 201,201
    kr,kt = 20,40
    f = makeCircleImpulses2(n1,n2,kr,kt)
    cmin,cmax = -0.25,0.25
  elif input==random:
    n1,n2 = 201,201
    f = makeRandomImage2(n1,n2)
    cmin,cmax = -0.5,0.5
  d = makeCircleTensors2(n1,n2)
  lsf = LocalSmoothingFilter()
  if smooth==lowpass:
    lsf.applySmoothL(0.40,f,f)
  elif smooth==simple:
    lsf.applySmoothS(f,f)
  plot2(f,cmin=cmin,cmax=cmax,cmap=cmap,cbar=cbar,cbwm=cbwm,
        title="Input",hlabel=hlabel,vlabel=vlabel,
        width=width,height=height)
  if input==impulses:
    cmin *= 0.10; cmax *= 0.10
  elif input==random:
    cmin *= 0.25; cmax *= 0.25
  g = copy(f)
  for stencil in stencils2:
    ldk = LocalDiffusionKernel(stencil)
    lsf = LocalSmoothingFilter(0.01,1000,ldk)
    lsf.setPreconditioner(True)
    lsf.apply(d,200.0,f,g)
    plot2(g,cmin=cmin,cmax=cmax,cmap=cmap,cbar=cbar,cbwm=cbwm,
          title=stencilName[stencil],hlabel=hlabel,vlabel=vlabel,
          width=width,height=height)

def demoSphere3(input,smooth):
  cmap = gray
  if input==impulses:
    n1,n2,n3 = 101,101,101
    kr,kt,kp = 20,40,40
    f = makeSphereImpulses3(n1,n2,n3,kr,kt,kp)
    cmin,cmax = -0.1,0.1
    slices = (68,31,22)
  elif input==random:
    n1,n2,n3 = 101,101,101
    f = makeRandomImage3(n1,n2,n3)
    cmin,cmax = -0.5,0.5
    slices = (n1-5,4,4)
  d = makeSphereTensors3(n1,n2,n3)
  lsf = LocalSmoothingFilter()
  if smooth==lowpass:
    lsf.applySmoothL(0.40,f,f)
  elif smooth==simple:
    lsf.applySmoothS(f,f)
  plot3(f,cmin=cmin,cmax=cmax,slices=slices)
  if input==impulses:
    cmin *= 0.01; cmax *= 0.01
  elif input==random:
    cmin *= 0.1; cmax *= 0.1
  g = copy(f)
  for stencil in stencils3:
    sw = Stopwatch()
    ldk = LocalDiffusionKernel(stencil)
    lsf = LocalSmoothingFilter(0.01,1000,ldk)
    lsf.setPreconditioner(True)
    sw.start()
    lsf.apply(d,200.0,f,g)
    sw.stop()
    print "time =",sw.time()
    plot3(g,cmin=cmin,cmax=cmax,slices=slices)

#############################################################################
# constants

stencils2 = [
  LocalDiffusionKernel.Stencil.D22,
  LocalDiffusionKernel.Stencil.D24,
  LocalDiffusionKernel.Stencil.D33,
  LocalDiffusionKernel.Stencil.D71,
  LocalDiffusionKernel.Stencil.D91]

stencils3 = [
  LocalDiffusionKernel.Stencil.D22,
  LocalDiffusionKernel.Stencil.D33,
  LocalDiffusionKernel.Stencil.D71]

stencilName = {
  LocalDiffusionKernel.Stencil.D22:"Derivative stencil: 2x2",
  LocalDiffusionKernel.Stencil.D24:"Derivative stencil: 2x4",
  LocalDiffusionKernel.Stencil.D33:"Derivative stencil: 3x3",
  LocalDiffusionKernel.Stencil.D71:"Derivative stencil: 7x1",
  LocalDiffusionKernel.Stencil.D91:"Derivative stencil: 9x1"}

#############################################################################
# input images

def makeRandomImage2(n1,n2):
  rand = Random(314159)
  r = sub(randfloat(rand,n1,n2),0.5)
  return r

def makeRandomImage3(n1,n2,n3):
  rand = Random(314159)
  r = sub(randfloat(rand,n1,n2,n3),0.5)
  return r

def makeCircleChirp2(n1,n2,kmax):
  """
  Returns an image of circular features with linearly increasing wavenumber.
  kmax is the highest wavenumber along any axis, in cycles per sample
  """
  n = max(n1,n2)
  s = kmax*PI*n
  x1 = rampfloat(0.0,1.0/n,n1)
  x2 = rampfloat(0.0,1.0/n,n2)
  c = zerofloat(n1,n2)
  for i2 in range(n2):
    for i1 in range(n1):
      rr = x1[i1]*x1[i1]+x2[i2]*x2[i2]
      c[i2][i1] = cos(s*rr)
  return c

def makeCircleImpulses2(n1,n2,kr,kt):
  """
  Returns an image of impulses uniformly spaced along circular arcs.
  kr is the radial spacing between impulses, in samples 
  kt is the angular (theta) spacing between impulses, in samples
  """
  f = zerofloat(n1,n2)
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
        f[i2][i1] = 1.0
  return f

def makeSphereImpulses3(n1,n2,n3,kr,kt,kp):
  """
  Returns an image of impulses uniformly spaced along spherical shells.
  kr is the radial spacing between impulses, in samples 
  kt is the polar angular (theta) spacing between impulses, in samples
  kp is the azimuthal angular (phi) spacing between impulses, in samples
  """
  f = zerofloat(n1,n2,n3)
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
          f[i3][i2][i1] = 1.0
  return f

#############################################################################
# tensors

def makeCircleTensors2(n1,n2):
  """ 
  Returns tensors for smoothing along concentric circular arcs.
  """
  c1,c2 = 0.01,0.01
  u1 = zerofloat(n1,n2)
  u2 = zerofloat(n1,n2)
  au = fillfloat(0,n1,n2)
  av = fillfloat(1,n1,n2)
  for i2 in range(n2):
    x2 = i2-c2
    for i1 in range(n1):
      x1 = i1-c1
      xs = 1.0/sqrt(x1*x1+x2*x2)
      u1[i2][i1] = xs*x1
      u2[i2][i1] = xs*x2
  return EigenTensors2(u1,u2,au,av)

def makeSphereTensors3(n1,n2,n3):
  """ 
  Returns tensors for smoothing within concentric spherical surfaces.
  """
  c1,c2,c3 = 0.01,0.01,0.01
  u1 = zerofloat(n1,n2,n3)
  u2 = zerofloat(n1,n2,n3)
  w1 = zerofloat(n1,n2,n3)
  w2 = zerofloat(n1,n2,n3)
  au = fillfloat(0,n1,n2,n3)
  av = fillfloat(1,n1,n2,n3)
  aw = fillfloat(1,n1,n2,n3)
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

def makeLinearTensors2(x):
  """
  Returns tensors computed for the specified image.
  Eigenvalues au (corresponding to eigenvectors u) are set to zero. 
  Eigenvalues av (corresponding to eigenvectors v) are set to one. 
  Therefore, these tensors are good for smoothing in locally linear
  trajectories along which the image changes least.
  """
  n1,n2 = len(x[0]),len(x)
  lof = LocalOrientFilter(8)
  d = lof.applyForTensors(x)
  d.setEigenvalues(0,1)
  return d

def makePlanarTensors3(x):
  """
  Returns tensors computed for the specified image.
  Eigenvalues au (corresponding to eigenvectors u) are set to zero. 
  Eigenvalues av and aw (corresponding to eigenvectors v and w) are set to 
  one. Therefore, these tensors are good for smoothing in locally planar
  patches within which the image changes least.
  """
  n1,n2,n3 = len(x[0][0]),len(x[0]),len(x)
  lof = LocalOrientFilter(8)
  d = lof.applyForTensors(x)
  d.setEigenvalues(0.0,1.0,1.0)
  return d


#############################################################################
# plots

gray = ColorMap.GRAY
jet = ColorMap.JET

def plot2(f,cmin=0,cmax=0,cmap=gray,cbar=None,cbwm=None,
          title=None,hlabel=None,vlabel=None,
          width=750,height=750):
  sp = SimplePlot(SimplePlot.Origin.LOWER_LEFT)
  sp.setFontSizeForSlide(1.0,1.0)
  sp.setSize(width,height)
  pv = sp.addPixels(f)
  if len(f)<250:
    pv.setInterpolation(PixelsView.Interpolation.NEAREST)
  if cmin!=cmax:
    pv.setClips(cmin,cmax)
  if cbar:
    sp.addColorBar(cbar)
    if cbwm:
      sp.plotPanel.setColorBarWidthMinimum(cbwm)
  if title:
    sp.setTitle(title)
  if hlabel:
    sp.setHLabel(hlabel)
  if vlabel:
    sp.setVLabel(vlabel)

def plot3(f,cmin=0,cmax=0,slices=None):
  n1,n2,n3 = len(f[0][0]),len(f[0]),len(f)
  world = World()
  sf = SimpleFrame()
  ipg = sf.addImagePanels(copy(f))
  if slices:
    ipg.setSlices(slices[0],slices[1],slices[2])
  if cmin!=cmax:
    ipg.setClips(cmin,cmax)
  sf.setSize(950,950)
  sf.orbitView.setScale(2.2)

#############################################################################
# do everything on Swing thread.

class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain())

#############################################################################
# old stuff

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
  for i in range(n1):
    if d[i]==0.0:
      s[i-1] = 0.0
      s[i+1] = 0.0
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
