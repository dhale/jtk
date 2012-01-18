import sys
from java.lang import *
from java.util import *
from javax.swing import *

from edu.mines.jtk.dsp import *
from edu.mines.jtk.interp import *
from edu.mines.jtk.util.ArrayMath import *

from DataLamont import *
from DataNotreDame import *
from DataSaddle import *
from DataSinSin import *
from DataTeapot import *

def main(args):
  demoTeapot()
  #demoBlendedGridder("Saddle")
  #demoSplinesGridder("Saddle")
  #demoTensorGuided("NotreDame")
  #demoLamont()
  #demoNotreDame()
  #demoSaddle()
  #demoSinSin()
 
def demoTeapot():
  t,x,f = dataTeapot()
  st,sx,s = imageTeapot()
  plot2Teapot(f,t,x,s,st,sx,None,"Known value","tp2f")
  smooth = 1.00 # smoothness for blended gridder
  fnull = -1.0
  sg = SimpleGridder2(f,t,x)
  sg.setNullValue(fnull)
  p = sg.grid(st,sx)
  tensors = makeImageTensors(s)
  plot2Teapot(f,t,x,s,st,sx,None,"Known value","tp2f",et=tensors)
  bg = makeBlendedGridder(f,t,x,smooth=smooth)
  bg.setTensors(tensors)
  d = bg.gridNearest(fnull,p)
  plot2Teapot(f,t,x,s,st,sx,d,"Time (samples)","tp2t")
  plot2Teapot(f,t,x,s,st,sx,p,"Nearest value","tp2p")
  q = copy(p)
  bg.gridBlended(d,p,q)
  plot2Teapot(f,t,x,s,st,sx,q,"Blended value","tp2q")

def demoBlendedGridder(data="SinSin",n=100):
  setupFor(data,n)
  for grid in ["coarse","medium","fine","finer"]:
    sx,sy = samplings(grid)
    bg = makeBlendedGridder(f,x,y,smooth=0.7)
    bg.setTimeMax(100.0)
    #bg.setBlending(False)
    g = bg.grid(sx,sy)
    nx,ny = sx.count,sy.count
    #plot2(f,x,y,g,sx,sy,title="Blended: "+str(nx)+"x"+str(ny))
    plot3(f,x,y,g,sx,sy)

def demoSplinesGridder(data="SinSin",n=100):
  setupFor(data,n)
  for grid in ["coarse","medium","fine","finer"]:
    sx,sy = samplings(grid)
    sg = makeSplinesGridder(f,x,y,tension=0.0)
    g = sg.grid(sx,sy)
    r = sg.getResiduals()
    #sp = SimplePlot()
    #pv = sp.addPoints(r)
    #sp.setHLabel("conjugate-gradient iterations")
    #sp.setVLabel("normalized residuals")
    ni = sg.getIterationCount()
    nx,ny = sx.count,sy.count
    plot2(f,x,y,g,sx,sy,title=str(nx)+"x"+str(ny)+": "+str(ni)+" iterations")

def demoTensorGuided(data="SinSin"):
  setupFor(data)
  sx,sy = samplings("fine")
  nx,ny = sx.count,sy.count
  t = 0.0 # tension for splines gridder
  s = 0.5 # smoothness for blended gridder
  tensors = makeCircleTensors(nx,ny)
  sg = makeSplinesGridder(f,x,y,tension=t)
  bg = makeBlendedGridder(f,x,y,smooth=s)
  for setTensors in [False,True]:
    if setTensors:
      sg.setTensors(tensors)
      bg.setTensors(tensors)
    else:
      sg.setTensors(None)
      bg.setTensors(None)
    gs = sg.grid(sx,sy)
    gb = bg.grid(sx,sy)
    plot2(f,x,y,gs,sx,sy,title="Splines: t = "+str(t))
    plot2(f,x,y,gb,sx,sy,title="Blended: s = "+str(s))
    #plot3(f,x,y,gs,sx,sy)
    #plot3(f,x,y,gb,sx,sy)

def demoSaddle():
  setupFor("Saddle")
  sx,sy = samplings("fine")
  gridders,gridderNames = makeGridders(f,x,y,sx,sy)
  for gridder in gridders:
    name = gridderNames[gridder]
    print name
    g = gridder.grid(sx,sy)
    plot2(f,x,y,g,sx,sy,title=name)
    #plot2(f,x,y,g,sx,sy)
    #plot3(f,x,y,g,sx,sy)

def demoSinSin():
  setupFor("SinSin")
  sx,sy = samplings("fine")
  gridders,gridderNames = makeGridders(f,x,y,sx,sy)
  for gridder in gridders:
    name = gridderNames[gridder]
    print name
    g = gridder.grid(sx,sy)
    plot2(f,x,y,g,sx,sy,title=name)
    #plot2(f,x,y,g,sx,sy)
    #plot3(f,x,y,g,sx,sy)

def demoLamont():
  setupFor("Lamont")
  sx,sy = samplings()
  putDataOnGrid(f,x,y,sx,sy)
  gridders,gridderNames = makeGridders(f,x,y,sx,sy)
  for gridder in gridders:
    name = gridderNames[gridder]
    print name
    g = gridder.grid(sx,sy)
    plot2(f,x,y,g,sx,sy,title=name)
    #plot2(f,x,y,g,sx,sy)
    #plot3(f,x,y,g,sx,sy)

def demoNotreDame():
  setupFor("NotreDame")
  sx,sy = samplings("fine")
  plot2,plot3 = plot2NotreDame,plot3NotreDame
  putDataOnGrid(f,x,y,sx,sy)
  gridders,gridderNames = makeGridders(f,x,y,sx,sy)
  for gridder in gridders:
    name = gridderNames[gridder]
    print name
    g = gridder.grid(sx,sy)
    #plot2(f,x,y,g,sx,sy,title=name)
    #plot2(f,x,y,g,sx,sy)
    plot3(f,x,y,g,sx,sy)

def setupFor(data="Saddle",n=100):
  global x,y,f,samplings,plot2,plot3
  x,y,f = dataSaddle()
  samplings = samplingsSaddle
  plot2,plot3 = plot2Saddle,plot3Saddle
  if data=="SinSin":
    x,y,f = dataSinSin(n)
    samplings = samplingsSinSin
    plot2,plot3 = plot2SinSin,plot3SinSin
  elif data=="Lamont":
    x,y,f = dataLamont()
    samplings = samplingsLamont
    plot2,plot3 = plot2Lamont,plot3Lamont
  elif data=="NotreDame":
    x,y,f = dataNotreDame()
    samplings = samplingsNotreDame
    plot2,plot3 = plot2NotreDame,plot3NotreDame

def putDataOnGrid(f,x,y,sx,sy):
  """ facilitates comparison of different gridders """
  n = len(f)
  for i in range(n):
    ix = sx.indexOfNearest(x[i])
    iy = sy.indexOfNearest(y[i])
    x[i] = sx.getValue(ix)
    y[i] = sy.getValue(iy)

def makeGridders(f,x,y,sx,sy):
  gridders = []
  gridderNames = {}
  g = makeBiharmonicGridder(f,x,y)
  gridders.append(g); gridderNames[g] = "Bi-harmonic radial basis"
  g = makeWesselBercoviciGridder(f,x,y,sx,sy,tension=0)
  gridders.append(g); gridderNames[g] = "Wessel-Bercovici: t = 0"
  g = makeSplinesGridder(f,x,y,tension=0)
  gridders.append(g); gridderNames[g] = "Splines: t = 0"
  g = makeBlendedGridder(f,x,y,smooth=0.7)
  gridders.append(g); gridderNames[g] = "Blended: s = 0.7"
  g = makeBlendedGridder(f,x,y,smooth=0.5)
  gridders.append(g); gridderNames[g] = "Blended: s = 0.5"
  g = makeSibsonGridder(f,x,y,smooth=False)
  gridders.append(g); gridderNames[g] = "Sibson C0"
  """
  t = 0.7
  g = makeSplinesGridder(f,x,y,tension=t)
  gridders.append(g); gridderNames[g] = "Splines: t = "+str(t)
  g = makeWesselBercoviciGridder(f,x,y,sx,sy,tension=t)
  gridders.append(g); gridderNames[g] = "Wessel-Bercovici: t = "+str(t)
  g = makeBlendedGridder(f,x,y,smooth=0.75)
  gridders.append(g); gridderNames[g] = "Blended: s = 0.75"
  """
  """
  g = makeSplinesGridder(f,x,y,tension=0)
  gridders.append(g); gridderNames[g] = "Splines: t = 0"
  g = makeSplinesGridder(f,x,y,tension=0.5)
  gridders.append(g); gridderNames[g] = "Splines: t = 0.5"
  g = makeBiharmonicGridder(f,x,y)
  gridders.append(g); gridderNames[g] = "Biharmonic"
  g = makeWesselBercoviciGridder(f,x,y,sx,sy,tension=0.5)
  gridders.append(g); gridderNames[g] = "Wessel-Bercovici: t = 0.5"
  g = makeBlendedGridder(f,x,y,smooth=0.5)
  gridders.append(g); gridderNames[g] = "Blended: s = 0.5"
  g = makeBlendedGridder(f,x,y,smooth=1.0)
  gridders.append(g); gridderNames[g] = "Blended: s = 1.0"
  g = makeSibsonGridder(f,x,y,smooth=False)
  gridders.append(g); gridderNames[g] = "Sibson C0"
  g = makeSibsonGridder(f,x,y,smooth=True)
  gridders.append(g); gridderNames[g] = "Sibson C1"
  """
  return gridders,gridderNames

def makeSplinesGridder(f,x,y,tension=0.5):
  sg = SplinesGridder2(f,x,y)
  sg.setTension(tension)
  return sg

def makeWesselBercoviciGridder(f,x,y,sx,sy,tension=0.5):
  scale = 0.02*(sx.last-sx.first+sy.last-sy.first)
  print "WB: tension t =",tension," p =",sqrt(tension/(1-tension))/scale
  basis = RadialInterpolator2.WesselBercovici(tension,scale)
  rg = RadialGridder2(basis,f,x,y)
  rg.setPolyTrend(1)
  return rg

def makeBiharmonicGridder(f,x,y):
  basis = RadialInterpolator2.Biharmonic()
  rg = RadialGridder2(basis,f,x,y)
  rg.setPolyTrend(1)
  return rg

def makeBlendedGridder(f,x,y,smooth=0.5,tmax=FLT_MAX,tmx=False):
  bg = BlendedGridder2(f,x,y)
  bg.setSmoothness(smooth)
  #bg.setBlendingKernel(
  #  LocalDiffusionKernel(LocalDiffusionKernel.Stencil.D21))
  if tmax<FLT_MAX:
    bg.setTimeMax(tmax)
  if tmx:
    bg.setTimeMarkerX(tmx)
  return bg

def makeSibsonGridder(f,x,y,smooth=False):
  sg = SibsonGridder2(f,x,y)
  sg.setSmooth(smooth)
  return sg

def makeCircleTensors(n1,n2):
  """ 
  Returns tensors for guiding along concentric circular arcs.
  """
  c1,c2 = 0.01,0.01
  u1 = zerofloat(n1,n2)
  u2 = zerofloat(n1,n2)
  au = fillfloat(0.01,n1,n2)
  av = fillfloat(1.00,n1,n2)
  for i2 in range(n2):
    x2 = i2-c2
    for i1 in range(n1):
      x1 = i1-c1
      xs = 1.0/sqrt(x1*x1+x2*x2)
      u1[i2][i1] = xs*x1
      u2[i2][i1] = xs*x2
  return EigenTensors2(u1,u2,au,av)

def makeImageTensors(s):
  """ 
  Returns tensors for guiding along features in specified image.
  """
  sigma = 3
  n1,n2 = len(s[0]),len(s)
  lof = LocalOrientFilter(sigma)
  t = lof.applyForTensors(s) # structure tensors
  c = coherence(sigma,t,s) # structure-oriented coherence c
  c = clip(0.0,0.99,c) # c clipped to range [0,1)
  t.scale(sub(1.0,c)) # scale structure tensors by 1-c
  t.invertStructure(1.0,1.0) # invert and normalize
  return t

def coherence(sigma,t,s):
  lsf = LocalSemblanceFilter(sigma,4*sigma)
  return lsf.semblance(LocalSemblanceFilter.Direction2.V,t,s)

def xcoherence(sigma,x):
  n1,n2 = len(x[0]),len(x)
  lof1 = LocalOrientFilter(sigma)
  lof2 = LocalOrientFilter(sigma*4)
  u11 = zerofloat(n1,n2)
  u21 = zerofloat(n1,n2)
  su1 = zerofloat(n1,n2)
  sv1 = zerofloat(n1,n2)
  u12 = zerofloat(n1,n2)
  u22 = zerofloat(n1,n2)
  su2 = zerofloat(n1,n2)
  sv2 = zerofloat(n1,n2)
  lof1.apply(x,None,u11,u21,None,None,su1,sv1,None)
  lof2.apply(x,None,u12,u22,None,None,su2,sv2,None)
  c = u11
  for i2 in range(n2):
    for i1 in range(n1):
      u11i = u11[i2][i1]
      u21i = u21[i2][i1]
      su1i = su1[i2][i1]
      sv1i = sv1[i2][i1]
      u12i = u12[i2][i1]
      u22i = u22[i2][i1]
      su2i = su2[i2][i1]
      sv2i = sv2[i2][i1]
      s111 = (su1i-sv1i)*u11i*u11i+sv1i
      s121 = (su1i-sv1i)*u11i*u21i     
      s221 = (su1i-sv1i)*u21i*u21i+sv1i
      s112 = (su2i-sv2i)*u12i*u12i+sv2i
      s122 = (su2i-sv2i)*u12i*u22i     
      s222 = (su2i-sv2i)*u22i*u22i+sv2i
      s113 = s111*s112+s121*s122
      s223 = s121*s122+s221*s222
      t1 = s111+s221
      t2 = s112+s222
      t3 = s113+s223
      t12 = t1*t2
      if t12>0.0:
        c[i2][i1] = t3/t12
      else:
        c[i2][i1] = 0.0
  return c

#############################################################################
# Run everything on Swing thread.
class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain())
