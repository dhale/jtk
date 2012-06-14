#############################################################################
# Demonstrates dynamic warping for 2D and 3D images.
# The Teapot Dome image was provided by the
# Rocky Mountain Oilfield Test Center, US DOE.

import os,sys
from java.awt import *
from java.lang import *
from java.util import *
from javax.swing import *

from edu.mines.jtk.awt import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.io import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.sgl import *
from edu.mines.jtk.util import *
from edu.mines.jtk.util.ArrayMath import *

#############################################################################

# Parameters used to generate images and estimate shifts
smax = 12 # shifts (in samples) are in range [0,smax]
nrms = 1.0 # rms noise/signal ratio
strainMax1 = 0.250 # not less than     smax*pi/n1
strainMax2 = 0.200 # not less than 0.5*smax*pi/n2
strainMax3 = 0.200 # not less than 0.5*smax*pi/n3
usmooth1 = 1.0 # smoothing of shifts in 2nd dimension
usmooth2 = 1.0 # smoothing of shifts in 2nd dimension
esmooth = 2 # number of smoothings of alignment errors

# Sampling for Teapot Dome images (with fake 3rd dimension)
n1,n2,n3 = 251,357,100 # numbers of samples
d1,d2,d3 = 0.004,0.025,0.025 # sampling intervals
f1,f2,f3 = 0.500,0.000,0.000 # first samples
s1,s2,s3 = Sampling(n1,d1,f1),Sampling(n2,d2,f2),Sampling(n3,d3,f3)
label1,label2,label3 = "Time (s)","Inline (km)","Crossline (km)"

def main(args):
  goTeapot2()
  #goTeapot3() # requires a few minutes on a dual-core laptop

def goTeapot2():
  f,g,s = makeTeapot2Images(smax,nrms)
  mlag = 4+smax
  dw = DynamicWarping(-mlag,mlag)
  dw.setStrainMax(strainMax1,strainMax2)
  dw.setErrorSmoothing(esmooth)
  dw.setShiftSmoothing(usmooth1,usmooth2)
  u = dw.findShifts(f,g)
  h = dw.applyShifts(u,g)
  s = mul(s,d1*1000)
  u = mul(u,d1*1000)
  print "s: min =",min(s),"max =",max(s)
  print "u: min =",min(u),"max =",max(u)
  usmin,usmax = min(s),max(s)
  fgmin,fgmax = -4,4
  show2(s,usmin,usmax,cbar="Shift (ms)",title="s")
  show2(u,usmin,usmax,cbar="Shift (ms)",title="u")
  show2(g,fgmin,fgmax,title="g")
  show2(f,fgmin,fgmax,title="f")
  show2(h,fgmin,fgmax,title="h")

def goTeapot3():
  sw = Stopwatch(); sw.start()
  print "making images"
  f,g,s = makeTeapot3Images(smax,nrms)
  print "time =",sw.time()," finding shifts"; sw.restart()
  mlag = 4+smax
  dw = DynamicWarping(-mlag,mlag)
  dw.setStrainMax(strainMax1,strainMax2)
  dw.setErrorSmoothing(esmooth)
  dw.setShiftSmoothing(usmooth1,usmooth2)
  u = dw.findShifts(f,g)
  print "time =",sw.time()," applying shifts"; sw.restart()
  h = dw.applyShifts(u,g)
  print "time =",sw.time()," showing results"; sw.restart()
  s = mul(s,d1*1000)
  u = mul(u,d1*1000)
  print "s: min =",min(s),"max =",max(s)
  print "u: min =",min(u),"max =",max(u)
  usmin,usmax = min(s),max(s)
  fgmin,fgmax = -4,4
  show3(s,usmin,usmax)
  show3(u,usmin,usmax)
  show3(g,fgmin,fgmax)
  show3(f,fgmin,fgmax)
  show3(h,fgmin,fgmax)

#############################################################################
# Teapot dome data

def makeTeapot2Images(smax,nrms):
  f = readTeapotImage()
  w = WarpFunction2.constantPlusSinusoid(0.5*smax,0.0,0.5*smax,0.0,n1,n2)
  #w = WarpFunction2.constant(smax,0.0,n1,n2)
  g = w.warp1(f)
  f = addNoise2(nrms,f,101)
  g = addNoise2(nrms,g,102)
  s = w.u1x()
  return f,g,s

def makeTeapot3Images(smax,nrms):
  f2 = readTeapotImage()
  f = zerofloat(n1,n2,n3)
  for i3 in range(n3):
    copy(f2,f[i3])
  w = WarpFunction3.constantPlusSinusoid(
    0.5*smax,0.0,0.0,0.5*smax,0.0,0.0,n1,n2,n3)
  #w = WarpFunction2.constant(smax,0.0,0.0,n1,n2,n3)
  g = w.warp1(f)
  f = addNoise3(nrms,f,101)
  g = addNoise3(nrms,g,102)
  s = w.u1x()
  return f,g,s

def readTeapotImage():
  n1,n2 = 251,357
  x = zerofloat(n1,n2)
  ais = ArrayInputStream(getDataDir()+"tp73.dat")
  ais.readFloats(x)
  ais.close()
  return x

#############################################################################
# utilities

def getDataDir():
  scriptDir = sys.path[0]
  baseIndex = scriptDir.find("jtk"+os.sep+"src")
  if baseIndex<0:
    baseIndex = scriptDir.find("idh"+os.sep+"bench")
  if baseIndex<0:
    return None
  dataDir = scriptDir[:baseIndex]+"jtk"+os.sep+"data"+os.sep
  return dataDir

def addNoise2(nrms,f,seed=0):
  n1,n2 = len(f[0]),len(f)
  if seed!=0:
    r = Random(seed)
  else:
    r = Random()
  rgf = RecursiveGaussianFilter(1.5)
  g = sub(randfloat(r,n1,n2),0.5)
  rgf.apply2X(g,g)
  rgf.applyX0(g,g)
  frms = sqrt(sum(mul(f,f))/n1/n2)
  grms = sqrt(sum(mul(g,g))/n1/n2)
  g = mul(g,nrms*frms/grms)
  return add(f,g)

def addNoise3(nrms,f,seed=0):
  n1,n2,n3 = len(f[0][0]),len(f[0]),len(f)
  if seed!=0:
    r = Random(seed)
  else:
    r = Random()
  rgf = RecursiveGaussianFilter(1.5)
  g = sub(randfloat(r,n1,n2,n3),0.5)
  rgf.apply2XX(g,g)
  rgf.applyX0X(g,g)
  rgf.applyXX0(g,g)
  frms = sqrt(sum(mul(f,f))/n1/n2/n3)
  grms = sqrt(sum(mul(g,g))/n1/n2/n3)
  g = mul(g,nrms*frms/grms)
  return add(f,g)
 
#############################################################################
# plotting

def show2(f,cmin=0.0,cmax=0.0,cbar=None,title=None):
  plot = SimplePlot(SimplePlot.Origin.UPPER_LEFT)
  pv = plot.addPixels(s1,s2,f)
  if cmin<cmax:
    pv.setClips(cmin,cmax)
  if not cbar:
    cbar = "Amplitude"
  plot.addColorBar(cbar)
  if title:
    plot.setTitle(title)
  plot.plotPanel.setColorBarWidthMinimum(90)
  plot.setVLabel(label1)
  plot.setHLabel(label2)
  plot.setSize(900,800)

def show3(f,cmin=0.0,cmax=0.0):
  frame = SimpleFrame()
  ip = frame.addImagePanels(f)
  ip.setSlices(7*n1/8,n2/3,n3/3)
  if cmin<cmax:
    ip.setClips(cmin,cmax)
  frame.getOrbitView().setScale(2.5)
  frame.setSize(900,900)

#############################################################################
# Do everything on Swing thread.

class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain())
