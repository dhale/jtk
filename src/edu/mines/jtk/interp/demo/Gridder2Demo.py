import sys
from java.lang import *
from java.util import *
from javax.swing import *

from edu.mines.jtk.dsp import *
from edu.mines.jtk.interp import *
from edu.mines.jtk.la import *
from edu.mines.jtk.util.ArrayMath import *

from DataLamont import *
from DataNotreDame import *
from DataSinSin import *

def main(args):
  #demoSplinesGridder("Lamont")
  #demoSinSin()
  demoLamont()
  #demoNotreDame()

def demoSplinesGridder(data="SinSin"):
  if data=="SinSin":
    x,y,f = dataSinSin(100)
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
  for grid in ["coarse","medium","fine","finer"]:
    sx,sy = samplings(grid)
    sg = makeSplinesGridder(f,x,y,tension=0.0)
    g = sg.grid(sx,sy)
    r = sg.getMaxUpdates()
    r = add(r,1)
    sp = SimplePlot()
    pv = sp.addPoints(r)
    sp.setHLabel("conjugate-gradient iteration number");
    sp.setVLabel("maximum change in gridded value");
    n = len(r)
    nx,ny = sx.count,sy.count
    plot2(f,x,y,g,sx,sy,title=str(nx)+"x"+str(ny)+": "+str(n)+" iterations")

def demoSinSin():
  x,y,f = dataSinSin(100)
  sx,sy = samplingsSinSin("fine")
  plot2 = plot2SinSin
  plot3 = plot3SinSin
  gridders,gridderNames = makeGridders(f,x,y,sx,sy)
  for gridder in gridders:
    name = gridderNames[gridder]
    print name
    g = gridder.grid(sx,sy)
    plot2(f,x,y,g,sx,sy,title=name)
    #plot2(f,x,y,g,sx,sy)
    #plot3(f,x,y,g,sx,sy)

def demoLamont():
  plot2 = plot2Lamont
  plot3 = plot3Lamont
  x,y,f = dataLamont()
  sx,sy = samplingsLamont()
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
  x,y,f = dataNotreDame()
  sx,sy = samplingsNotreDame("fine")
  plot2,plot3 = plot2NotreDame,plot3NotreDame
  putDataOnGrid(f,x,y,sx,sy) # for comparison with more general interpolators
  gridders,gridderNames = makeGridders(f,x,y,sx,sy)
  for gridder in gridders:
    name = gridderNames[gridder]
    print name
    g = gridder.grid(sx,sy)
    plot2(f,x,y,g,sx,sy,title=name)
    #plot2(f,x,y,g,sx,sy)
    #plot3(f,x,y,g,sx,sy)

def putDataOnGrid(f,x,y,sx,sy):
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
  gridders.append(g); gridderNames[g] = "Biharmonic"
  g = makeSplinesGridder(f,x,y,tension=0)
  gridders.append(g); gridderNames[g] = "Splines: t = 0"
  g = makeBlendedGridder(f,x,y,smooth=0.75)
  gridders.append(g); gridderNames[g] = "Blended: s = 0.75"
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
  return RadialGridder2(basis,f,x,y)

def makeBiharmonicGridder(f,x,y):
  basis = RadialInterpolator2.Biharmonic()
  return RadialGridder2(basis,f,x,y)

def makeBlendedGridder(f,x,y,smooth=0.5,tmax=FLT_MAX):
  bg = BlendedGridder2(f,x,y)
  bg.setSmoothness(smooth)
  if tmax<FLT_MAX:
    bg.setTimeMax(tmax)
  return bg

def makeSibsonGridder(f,x,y,smooth=False):
  sg = SibsonGridder2(f,x,y)
  sg.setSmooth(smooth)
  return sg

#############################################################################
# Run everything on Swing thread.
class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain())
