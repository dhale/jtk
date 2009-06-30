#/****************************************************************************
#Copyright (c) 2008, Colorado School of Mines and others. All rights reserved.
#This program and accompanying materials are made available under the terms of
#the Common Public License - v1.0, which accompanies this distribution, and is 
#available at http://www.eclipse.org/legal/cpl-v10.html
#****************************************************************************/
# This Jython script is set up to demonstrate the application of SteerablePyramid
# on 2D and 3D images.
# Author: John Mathewson, Colorado School of Mines
# Version: 2008.10.07

import sys
from math import *
from java.lang import *
from java.util import *
from java.nio import *
from javax.swing import *
from java.io import *

from edu.mines.jtk.awt import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.io import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.util import *
from edu.mines.jtk.util.ArrayMath import *

True = 1
False = 0

#############################################################################
# parameters

# no. of samples in x1 and x2 directions for synthetic 2D timeslice image
nr1 = 500
nr2 = 500

# no. of samples in x1, x2, and x3 directions for synthetic 3D timeslice image
n3d1 = 101
n3d2 = 101
n3d3 = 101

fontSize = 24
width =  1000
height = 1000
widthColorBar = 200
pngDir = "./png/"
dataDir = "./data/"

spyr = SteerablePyramid(0.5,1.0)
#############################################################################
# functions
# Uncomment one of the following lines in main to run.
def main(args):
  #makePyramidandPlotBasisImages2D("win34nodecim_ts170.dat")
  #makePyramidandPlotBasisImages3D("win34_decim101.dat")
  #makePyramidSteerSumandCascadePlot2D("win34nodecim_ts170.dat")
  #SubtractPlanesthenHighlightChannels3D("win34_decim101a.dat")
  return


def makePyramidandPlotBasisImages2D(filename):
  x = readImage2D(filename)
  plot(x,0.0125,)
  pyr = spyr.makePyramid(x)
  nlev = len(pyr)
  ndir = len(pyr[0])
  for lev in range(nlev-1):
    for dir in range(ndir):
      plot(pyr[lev][dir],0.00125,)
  y = spyr.sumPyramid(1,pyr)
  plot(y,0.0125,)
  sub(x,y,y)
  plot(y,0.0125,)

def makePyramidandPlotBasisImages3D(filename):
  x = readImage3D(filename)
  sliceplot(x,0.0125,)
  pyr = spyr.makePyramid(x)
  nlev = len(pyr)
  ndir = len(pyr[0])
  for lev in range(nlev-1):
    for dir in range(ndir):
      sliceplot(pyr[lev][dir],0.00125,)
  y = spyr.sumPyramid(1,pyr)
  sliceplot(y,0.0125,)
  sub(x,y,y)
  sliceplot(y,0.0125,)

def makePyramidSteerSumandCascadePlot2D(filename):
  x = readImage2D(filename)
  plot(x,0.0125,)
  y = copy(x)
  for i in range(0,5,1):
    pyr = spyr.makePyramid(y)
    attr = spyr.estimateAttributes(2.0,pyr)
    spyr.steerScale(0,50.0,0.5,attr,pyr)
    y = spyr.sumPyramid(1,pyr)
    plot(y,0.0125,)

def SubtractPlanesthenHighlightChannels3D(filename):
  x = readImage3D(filename)
  sliceplot(x,0.0100,)
  y = copy(x)
  # Smooth locally-planar and plot.
  for i in range(0,3,1):
    pyr = spyr.makePyramid(y)
    attr = spyr.estimateAttributes(0,2.0,pyr)
    spyr.steerScale(0,0,50.0,0.5,attr,pyr)
    y = spyr.sumPyramid(1,pyr)
    sliceplot(y,0.0100,)
  # Subtract smoothed planes and plot.
  sub(x,y,y)
  sliceplot(y,0.0100,)
  # Smooth locally-linear, threshold and plot.
  pyr = spyr.makePyramid(y)
  attr = spyr.estimateAttributes(1,2.0,pyr)
  spyr.steerScale(1,99,50.0,0.3,attr,pyr)
  y = spyr.sumPyramid(0,pyr)
  sliceplot(y,0.0100,)

def readImage2D(infile):
  fileName = dataDir+infile
  ais = ArrayInputStream(fileName,ByteOrder.BIG_ENDIAN)
  x = zerofloat(nr1,nr2)
  ais.readFloats(x)
  ais.close()
  print "x min =",min(x)," max =",max(x)
  return x

def readImage3D(infile):
  fileName = dataDir+infile
  ais = ArrayInputStream(fileName,ByteOrder.BIG_ENDIAN)
  x = zerofloat(n3d1,n3d2,n3d3)
  ais.readFloats(x)
  ais.close()
  print "x min =",min(x)," max =",max(x)
  return x

#############################################################################
# plots functions

def plot(f,clip=0.0,png=None):
  n1 = len(f[0])
  n2 = len(f)
  p = panel()
  s1 = Sampling(n1,1.0,0.0)
  s2 = Sampling(n2,1.0,0.0)
  pv = p.addPixels(s1,s2,f)
  if clip!=0.0:
    pv.setClips(-clip,clip)
  else:
    pv.setPercentiles(0.0,100.0)
  #pv.setColorModel(ColorMap.JET)
  pv.setInterpolation(PixelsView.Interpolation.LINEAR)
  frame(p,png)

def sliceplot(x,clip=0.0,png=None):
  np3 = len(x)
  np2 = len(x[0])
  np1 = len(x[0][0])
  islice = zerofloat(np2, np1)
  for i2 in range(np2):
    for i1 in range(np1):
      islice[i1][i2] = x[(int)(np3/2.0)][i2][np1-1-i1]
  xslice = zerofloat(np3, np1)
  for i2 in range(np3):
    for i1 in range(np1):
      xslice[i1][i2] = x[i2][(int)(np2/2.0)][np1-1-i1]
  tslice = zerofloat(np2, np3)
  for i2 in range(np3):
    for i1 in range(np2):
      tslice[i2][i1] = x[i2][i1][(int)(np1/2.0)]
  pp = PlotPanel(2, 2)
  p1 = pp.addPixels(1, 0, islice)
  p2 = pp.addPixels(0, 0, tslice)
  p3 = pp.addPixels(1, 1, xslice)
  #p1.setPercentiles(1,99)
  #p2.setPercentiles(1,99)
  #p3.setPercentiles(1,99)
  p1.setClips(-clip,clip)
  p2.setClips(-clip,clip)
  p3.setClips(-clip,clip)
  frame(pp,png)

def panel():
  p = PlotPanel(PlotPanel.Orientation.X1DOWN_X2RIGHT)
  #p.addColorBar()
  #p.setColorBarWidthMinimum(widthColorBar)
  return p

def frame(panel,png=None):
  frame = PlotFrame(panel)
  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
  frame.setFontSize(fontSize)
  frame.setSize(width,height)
  frame.setVisible(True)
  if png and pngDir:
    frame.paintToPng(100,6,pngDir+"/"+png+".png")
  return frame

#############################################################################
# Do everything on Swing thread.

class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain())
