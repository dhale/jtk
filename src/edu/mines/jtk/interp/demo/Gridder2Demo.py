import sys
from java.lang import *
from javax.swing import *

from edu.mines.jtk.dsp import *
from edu.mines.jtk.interp import *
from edu.mines.jtk.util.ArrayMath import *

from DataLamont import *
from DataNotreDame import *

def main(args):
  demoLamont()
  #demoNotreDame()

def demoLamont():
  plot2 = plot2Lamont
  plot3 = plot3Lamont
  x,y,f = dataLamont()
  sx,sy = samplingsLamont()
  gridders,gridderNames = makeGridders(f,x,y)
  for gridder in gridders:
    name = gridderNames[gridder]
    print name
    g = gridder.grid(sx,sy)
    plot2(f,x,y,g,sx,sy,title=name)
    #plot2(f,x,y,g,sx,sy)
    #plot3(f,x,y,g,sx,sy)

def demoNotreDame():
  plot2 = plot2NotreDame
  plot3 = plot3NotreDame
  x,y,f = dataNotreDame()
  sx,sy = samplingsNotreDame()
  gridders,gridderNames = makeGridders(f,x,y)
  for gridder in gridders:
    name = gridderNames[gridder]
    print name
    g = gridder.grid(sx,sy)
    plot2(f,x,y,g,sx,sy,title=name)
    #plot2(f,x,y,g,sx,sy)
    #plot3(f,x,y,g,sx,sy)

def makeGridders(f,x,y):
  gridders = []
  gridderNames = {}
  g = makeBiharmonicGridder(f,x,y)
  gridders.append(g); gridderNames[g] = "Biharmonic"
  g = makeWesselBercoviciGridder(f,x,y,tension=0.5)
  gridders.append(g); gridderNames[g] = "Wessel-Bercovici: t = 0.5"
  g = makeBlendedGridder(f,x,y,smooth=0.5)
  gridders.append(g); gridderNames[g] = "Blended: s = 0.5"
  g = makeBlendedGridder(f,x,y,smooth=1.0)
  gridders.append(g); gridderNames[g] = "Blended: s = 1.0"
  g = makeSibsonGridder(f,x,y,smooth=False)
  gridders.append(g); gridderNames[g] = "Sibson C0"
  g = makeSibsonGridder(f,x,y,smooth=True)
  gridders.append(g); gridderNames[g] = "Sibson C1"
  return gridders,gridderNames

def makeWesselBercoviciGridder(f,x,y,tension=0.5):
  xmin,xmax = min(x),max(x)
  ymin,ymax = min(y),max(y)
  fmin,fmax = min(f),max(f)
  xdif,ydif = xmax-xmin,ymax-ymin
  scale = 0.02*max(xdif,ydif)
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
