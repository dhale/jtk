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

# Two methods for designing recursive Gaussian filters.
Method1 = RecursiveGaussianFilter.Method.DERICHE
Method2 = RecursiveGaussianFilter.Method.VAN_VLIET

# Make exact Gaussian 0th, 1st, and 2nd derivatives.
def makeg0(sigma,nt,dt=1.0):
  a = 1.0/(sqrt(2.0*pi)*sigma)
  b = -0.5/(sigma*sigma)
  ft = -0.5*(nt-1)*dt
  g = Array.zerofloat(nt)
  for it in range(nt):
    t = ft+it*dt
    g[it] = a*exp(b*t*t)
  return g
def makeg1(sigma,nt,dt=1.0):
  a = 1.0/(sqrt(2.0*pi)*sigma*sigma*sigma)
  b = -0.5/(sigma*sigma)
  ft = -0.5*(nt-1)*dt
  g = Array.zerofloat(nt)
  for it in range(nt):
    t = ft+it*dt
    g[it] = -a*t*exp(b*t*t)
  return g
def makeg2(sigma,nt,dt=1.0):
  a = 1.0/(sqrt(2.0*pi)*sigma*sigma*sigma)
  b = -0.5/(sigma*sigma)
  c = 1.0/(sigma*sigma)
  ft = -0.5*(nt-1)*dt
  g = Array.zerofloat(nt)
  for it in range(nt):
    t = ft+it*dt
    g[it] = -a*(1.0-c*t*t)*exp(b*t*t)
  return g

# Make exact Gaussian frequency responses for 0th, 1st, and 2nd derivatives.
# If the frequency sampling interval is not specified, then the frequency
# response is computed for only frequencys less than pi.
# If positive, then amplitude response is computed for only positive
# frequencies; otherwise, the frequency response is computed for both
# positive and negative frequencies. In the latter case, for the 1st
# derivative, the returned frequency response is the imaginary part.
def makeG0(sigma,nw,dw=0.0,positive=True):
  if dw==0.0:
    dw = pi/(nw-1)
    if not positive:
      dw *= 2.0
  if positive:
    fw = 0.0
  else:
    fw = -(nw-1)*dw/2.0
  b = -0.5*sigma*sigma
  g = Array.zerofloat(nw)
  for iw in range(nw):
    w = fw+iw*dw
    g[iw] = exp(b*w*w)
  if positive:
    g = Array.abs(g)
  return g
def makeG1(sigma,nw,dw=0.0,positive=True):
  if dw==0.0:
    dw = pi/(nw-1)
    if not positive:
      dw *= 2.0
  if positive:
    fw = 0.0
  else:
    fw = -(nw-1)*dw/2.0
  b = -0.5*sigma*sigma
  g = Array.zerofloat(nw)
  for iw in range(nw):
    w = fw+iw*dw
    g[iw] = w*exp(b*w*w)
  if positive:
    g = Array.abs(g)
  return g
def makeG2(sigma,nw,dw=0.0,positive=True):
  if dw==0.0:
    dw = pi/(nw-1)
    if not positive:
      dw *= 2.0
  if positive:
    a = 1.0
    fw = 0.0
  else:
    a = -1.0
    fw = -(nw-1)*dw/2.0
  b = -0.5*sigma*sigma
  g = Array.zerofloat(nw)
  for iw in range(nw):
    w = fw+iw*dw
    g[iw] = a*w*w*exp(b*w*w)
  if positive:
    g = Array.abs(g)
  return g

# Amplitude response |H(w)| for a specified impulse response h[n].
def amplitudeResponse(h):
  n = len(h)
  nfft = FftReal.nfftSmall(100*n)
  fft = FftReal(nfft)
  hfft = Array.zerofloat(nfft+2)
  Array.copy(n,h,hfft)
  fft.realToComplex(1,hfft,hfft)
  habs = Array.cabs(hfft)
  return habs;

# Returns maximum and RMS norms of specified sequence.
def norms(e):
  n = len(e)
  emax = 0.0
  esum = 0.0
  for i in range(n):
    ei = abs(e[i])
    esum += ei*ei;
    if ei>emax:
      emax = ei
  erms = sqrt(esum)
  return emax,erms

# Tests (1) Deriche and (2) van Vliet methods for specified derivative.
# Returns a finely sample exact Gaussian derivative g, two impulse 
# responses h1 and h2, and their amplitude responses ag, ah1, and ah2.
def testMethods(sigma,deriv,norm=False):
  n = 1+2*int(4*sigma+0.5)
  f = Array.zerofloat(n)
  f[(n-1)/2] = 1.0
  h1 = Array.zerofloat(n)
  h2 = Array.zerofloat(n)
  rgf1 = RecursiveGaussianFilter(sigma,Method1)
  rgf2 = RecursiveGaussianFilter(sigma,Method2)
  if deriv==0:
    g = makeg0(sigma,n)
    rgf1.apply0(f,h1)
    rgf2.apply0(f,h2)
  elif deriv==1:
    g = makeg1(sigma,n)
    rgf1.apply1(f,h1)
    rgf2.apply1(f,h2)
  else:
    g = makeg2(sigma,n)
    rgf1.apply2(f,h1)
    rgf2.apply2(f,h2)
  ah1 = amplitudeResponse(h1)
  ah2 = amplitudeResponse(h2)
  e1 = Array.sub(h1,g)
  e2 = Array.sub(h2,g)
  e1max,e1rms = norms(e1)
  e2max,e2rms = norms(e2)
  print "sigma =",sigma," deriv =",deriv
  print "  method1: e1max =",e1max," e1rms  =",e1rms
  print "  method2: e2max =",e2max," e2rms  =",e2rms
  n = 1+(n-1)*20
  d = 0.05
  if deriv==0:
    g = makeg0(sigma,n,d)
    ag = makeG0(sigma,n)
  elif deriv==1:
    g = makeg1(sigma,n,d)
    ag = makeG1(sigma,n)
  else:
    g = makeg2(sigma,n,d)
    ag = makeG2(sigma,n)
  if norm:
    scale = 1.0/Array.max(Array.abs(g))
    g = Array.mul(scale,g)
    h1 = Array.mul(scale,h1)
    h2 = Array.mul(scale,h2)
  return g,h1,h2,ag,ah1,ah2

# Plots an exact Gaussian derivative and two specified impulse responses.
# The impulse responses are sampled symmetrically about the origin with
# unit sampling interval. The exact Gaussian derivative is assumed to
# be sampled more finely.
def plotImpulseResponses(g,h1,h2,png=None):
  panel = PlotPanel()
  panel.setVFormat("%4.3f");
  nh = len(h1); dh = 1.0;                     fh = -(nh-1)*dh/2.0
  ng = len(g);  dg = float(nh-1)/float(ng-1); fg = -(ng-1)*dg/2.0
  sh = Sampling(nh,dh,fh)
  sg = Sampling(ng,dg,fg)
  vmin,vmax = bounds(g,h1,h2)
  if nh<16:
    vmin -= 0.04*(vmax-vmin)
    vmax += 0.04*(vmax-vmin)
  elif (nh<128):
    vmin -= 0.02*(vmax-vmin)
    vmax += 0.02*(vmax-vmin)
  else:
    vmin -= 0.01*(vmax-vmin)
    vmax += 0.01*(vmax-vmin)
  panel.setVLimits(vmin,vmax)
  #panel.setVLimits(-0.03,1.09)
  panel.setHLabel("samples")
  pvh1 = panel.addPoints(sh,h1)
  pvh1.setStyle("rO")
  if (nh<16):
    pvh1.setLineWidth(3)
    pvh1.setMarkSize(20)
  elif (nh<32):
    pvh1.setLineWidth(1.5)
    pvh1.setMarkSize(15)
  elif (nh<128):
    pvh1.setLineWidth(2)
    pvh1.setMarkSize(10)
  else:
    pvh1.setLineWidth(1)
    pvh1.setMarkSize(5)
  pvh2 = panel.addPoints(sh,h2)
  pvh2.setStyle("bo")
  if (nh<16):
    pvh2.setLineWidth(3)
    pvh2.setMarkSize(20)
  elif (nh<32):
    pvh2.setLineWidth(1.5)
    pvh2.setMarkSize(15)
  elif (nh<128):
    pvh2.setLineWidth(2)
    pvh2.setMarkSize(10)
  else:
    pvh2.setLineWidth(1)
    pvh2.setMarkSize(5)
  pvg = panel.addPoints(sg,g)
  pvg.setLineWidth(3)
  frame = PlotFrame(panel)
  frame.setBackground(Color.WHITE)
  frame.setFontSize(18)
  frame.setSize(800,450)
  frame.setVisible(True)
  if png:
    frame.paintToPng(600,3,png)
  return frame

def bounds(a,b,c):
  amin =  Float.MAX_VALUE
  amax = -Float.MAX_VALUE
  if a:
    amin = min(amin,Array.min(a))
    amax = max(amax,Array.max(a))
  if b:
    amin = min(amin,Array.min(b))
    amax = max(amax,Array.max(b))
  if c:
    amin = min(amin,Array.min(c))
    amax = max(amax,Array.max(c))
  return amin,amax

# Plots three specified amplitude responses that are sampled for positive 
# frequencies in [0,0.5] cycles/sample. Windows the plots to show only 
# frequencies in [0,0.5/sigma] cycles/sample.
def plotAmplitudeResponses(sigma,g,h1,h2,png=None):
  panel = PlotPanel()
  panel.setVFormat("%4.2f");
  sg = Sampling(len(g),0.5/(len(g)-1),0.0)
  sh = Sampling(len(h1),0.5/(len(h1)-1),0.0)
  panel.setHLimits(0,0.5/sigma)
  vmin,vmax = bounds(g,h1,h2)
  vmin -= 0.02*(vmax-vmin)
  vmax += 0.02*(vmax-vmin)
  panel.setVLimits(vmin,vmax)
  #panel.setVLimits(-0.02,1.02)
  panel.setHLabel("cycles/sample")
  pvh1 = panel.addPoints(sh,h1)
  pvh1.setStyle("r-")
  pvh1.setLineWidth(3)
  pvh2 = panel.addPoints(sh,h2)
  pvh2.setStyle("b-")
  pvh2.setLineWidth(3)
  pvg = panel.addPoints(sg,g)
  pvg.setLineWidth(3)
  pvg.setStyle("k-.")
  frame = PlotFrame(panel)
  frame.setBackground(Color.WHITE)
  frame.setFontSize(18)
  frame.setSize(800,450)
  frame.setVisible(True)
  if png:
    frame.paintToPng(600,3,png)
  return frame

# Figures of exact Gaussian derivatives and their Fourier transforms 
def figgd():
  sigma = 1.0
  nt = 401
  dt = 2*4.0*sigma/(nt-1)
  ft = -(nt-1)*dt/2.0
  st = Sampling(nt,dt,ft)
  g0 = makeg0(sigma,nt,dt)
  g1 = makeg1(sigma,nt,dt)
  g2 = makeg2(sigma,nt,dt)
  panel = PlotPanel(3,1)
  panel.setVFormat(0,"%4.2f");
  panel.setVFormat(1,"%4.2f");
  panel.setVFormat(2,"%4.2f");
  panel.setVLimits(0,-0.005,0.405)
  panel.setVLimits(1,-0.26,0.26)
  panel.setVLimits(2,-0.42,0.20)
  gg0 = panel.addGrid(0,0,"H0V0-.")
  gg1 = panel.addGrid(1,0,"H0V0-.")
  gg2 = panel.addGrid(2,0,"H0V0-.")
  pvg0 = panel.addPoints(0,0,st,g0)
  pvg1 = panel.addPoints(1,0,st,g1)
  pvg2 = panel.addPoints(2,0,st,g2)
  pvg0.setLineWidth(3)
  pvg1.setLineWidth(3)
  pvg2.setLineWidth(3)
  frame = PlotFrame(panel)
  frame.setBackground(Color.WHITE)
  frame.setFontSize(18)
  frame.setSize(600,750)
  frame.setVisible(True)
  frame.paintToPng(600,3,"rgfgdt.png")
  nw = 401
  dw = 2*4.0/sigma/(nw-1)
  fw = -(nw-1)*dw/2.0
  sw = Sampling(nw,dw,fw)
  g0 = makeG0(sigma,nw,dw,False)
  g1 = makeG1(sigma,nw,dw,False)
  g2 = makeG2(sigma,nw,dw,False)
  panel = PlotPanel(3,1)
  panel.setVFormat(0,"%4.2f");
  panel.setVFormat(1,"%4.2f");
  panel.setVFormat(2,"%4.2f");
  panel.setVLimits(0,-0.02,1.02)
  panel.setVLimits(1,-0.65,0.65)
  panel.setVLimits(2,-0.75,0.02)
  gg0 = panel.addGrid(0,0,"H0V0-.")
  gg1 = panel.addGrid(1,0,"H0V0-.")
  gg2 = panel.addGrid(2,0,"H0V0-.")
  pvg0 = panel.addPoints(0,0,sw,g0)
  pvg1 = panel.addPoints(1,0,sw,g1)
  pvg2 = panel.addPoints(2,0,sw,g2)
  pvg0.setLineWidth(3)
  pvg1.setLineWidth(3)
  pvg2.setLineWidth(3)
  pvg1.setLineStyle(PointsView.Line.DOT)
  frame = PlotFrame(panel)
  frame.setBackground(Color.WHITE)
  frame.setFontSize(18)
  frame.setSize(600,750)
  frame.setVisible(True)
  frame.paintToPng(600,3,"rgfgdw.png")
#figgd()

def figDV0():
  base = "rgfdv0"
  #for sigma in (1,2,8,28,30,32,34,36,64):
  for sigma in (1,8,64):
    g,h1,h2,ag,ah1,ah2 = testMethods(sigma,0)
    #plotImpulseResponses(g,h1,h2)
    plotImpulseResponses(g,h1,h2,base+str(sigma)+".png")
    #plotAmplitudeResponses(sigma,ag,ah1,ah2)
    plotAmplitudeResponses(sigma,ag,ah1,ah2,base+"a"+str(sigma)+".png")
#figDV0()

def figDV12():
  base = "rgfdv"
  for deriv in (1,2):
    for sigma in (1,2,4):
      based = base+str(deriv)
      g,h1,h2,ag,ah1,ah2 = testMethods(sigma,deriv)
      #plotImpulseResponses(g,h1,h2)
      plotImpulseResponses(g,h1,h2,based+str(sigma)+".png")
      #plotAmplitudeResponses(sigma,ag,ah1,ah2)
      plotAmplitudeResponses(sigma,ag,ah1,ah2,based+"a"+str(sigma)+".png")
#figDV12()

# Figures comparing series and parallel implementations
def rgfPolesZerosGain(sigma):
  q = sigma/2.0
  c1 = Cdouble(1.0,0.0)
  d1 = Cdouble(1.12075,1.27788)
  d3 = Cdouble(1.76952,0.46611)
  d1 = Cdouble.polar(pow(d1.abs(),1.0/q),d1.arg()/q)
  d3 = Cdouble.polar(pow(d3.abs(),1.0/q),d3.arg()/q)
  p1 = c1.over(d1)
  p2 = p1.conj()
  p3 = c1.over(d3)
  p4 = p3.conj()
  g = c1.minus(p1).norm()*c1.minus(p3).norm()
  return [p1,p2,p3,p4],[],g
def rgfSeries(sigma,x):
  poles,zeros,gain = rgfPolesZerosGain(sigma)
  rf = RecursiveCascadeFilter(poles,zeros,gain)
  n = len(x)
  yf = Array.zerofloat(n)
  yr = Array.zerofloat(n)
  yb = Array.zerofloat(n)
  rf.applyForward(x,yf)
  rf.applyReverse(x,yr)
  rf.applyReverse(yf,yb)
  return yf,yr,yb
def rgfParallel(sigma,x):
  poles,zeros,gain = rgfPolesZerosGain(sigma)
  rf = RecursiveParallelFilter(poles,zeros,gain)
  n = len(x)
  yf = Array.zerofloat(n)
  yr = Array.zerofloat(n)
  yb = Array.zerofloat(n)
  rf.applyFrf(x,yf)
  rf.applyFrr(x,yr)
  rf.applyForwardReverse(x,yb)
  return yf,yr,yb
def rgfPlotThree(x1,x2,x3,png=None):
  sx = Sampling(len(x1),1.0,0.0)
  panel = PlotPanel(3,1)
  panel.setVFormat(0,"%4.2f");
  panel.setVFormat(1,"%4.2f");
  panel.setVFormat(2,"%4.2f");
  pvs0 = panel.addSequence(0,0,sx,x1)
  pvs1 = panel.addSequence(1,0,sx,x2)
  pvs2 = panel.addSequence(2,0,sx,x3)
  frame = PlotFrame(panel)
  frame.setBackground(Color.WHITE)
  frame.setFontSize(18)
  frame.setSize(600,600)
  frame.setVisible(True)
  if png:
    frame.paintToPng(600,3,png)
def figsp():
  sigma = 6.0
  n = 101
  x = Array.zerofloat(n)
  x[3] = x[(n-1)/2] = x[n-4] = 1.0
  ysf,ysr,ysb = rgfSeries(sigma,x)
  ypf,ypr,ypb = rgfParallel(sigma,x)
  rgfPlotThree(x,ysf,ysb,"rgfs.png")
  rgfPlotThree(x,ypf,ypb,"rgfp.png")
figsp()

#############################################################################
# Old stuff.

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
  e = makeg0(sigma,n1)
  frame = plot1(g,e)
  rgf.apply1(f,g)
  e = makeg1(sigma,n1)
  frame = plot1(g,e)
  rgf.apply2(f,g)
  e = makeg2(sigma,n1)
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

#test1()
#test2()
