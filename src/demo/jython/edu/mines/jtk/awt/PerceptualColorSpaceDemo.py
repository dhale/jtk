import os,sys
from java.awt.image import *
from java.awt import *
from java.lang import *
from javax.swing import *
import math

from jarray import *

from edu.mines.jtk.awt import ColorMap
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.dsp import LocalSmoothingFilter
from edu.mines.jtk.dsp import Sampling
from edu.mines.jtk.util.ArrayMath import *
from edu.mines.jtk.sgl import *

##############################################################################
# Perceptual Color Map Demo Using CIE L*a*b* Color Space
#
# Humans are terrible at differentiating colors. We can't help it - 
# it's biology! The human eye has four types of receptors: the rods which are
# sensitive only to black, white and shades of gray, and cones of which there
# are three types, each responding to a different range of colors. In fact,
# those ranges have some degree of overlap, and not every wavelength range is
# adequately covered.
#
# Because of this, there exists two main sets of colors that are always 
# competing for dominance and can not be perceived together: the Red-Green 
# pair, and the Yellow-Blue pair. These are known as "color opponents".
#
# Conventional color models such as RGB and CMYK do not adequately reflect 
# this physiological bias.
#
# The CIE L*a*b* (or CIELAB) color space addresses this by describing the 
# colors visible to the human eye. It is a three-dimensional color space 
# where L* represents the lightness of a color, a* represents a color's 
# position between the red and green color opponents, and b* represents a 
# color's position between blue and yellow.
# 
# When we convert color maps and observe the lightness (L*) we immediately see
# we immediately see distinct inflection points which are observed to be bands
# or contours in the original color map. This can create biases when applied
# to scientific visualization by unnecessarily leading our eyes or creating
# false topography.
#
# There are two ways this demo addresses this. The first method smooths the
# lightness graph thereby reducing the inflection points, which essentially
# "smooths" the sharp bands of color when transitioning hues. 
# The second method assigns a new monotonically increasing lightness graph, 
# which attempts to approximate that each value change is represented by a 
# change in perception.
#
# Author: Chris Engelsma
# Version: 2015.09.27
##############################################################################
def main(args):
  pp1 = test1()
  pp2 = test2()
#  pp3 = test3()
  pf = PlotFrame(pp1,pp2,PlotFrame.Split.HORIZONTAL)
  pf.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE)
  pf.setVisible(True)
  return

def test1():
  rgb,Lab = getRgbAndLab()
  L = getLightnessFromLab(Lab)
  return plot(L,icm)

def test2():
  rgb,Lab = getRgbAndLab()
  Lab = smoothLightness(Lab)
  L = getLightnessFromLab(Lab)
  icm2 = getNewColorModel(Lab)
  return plot(L,icm2)

def test3():
  rgb,Lab = getRgbAndLab()
  Lab = setMonotonicallyIncreasingLightness(Lab)
  L = getLightnessFromLab(Lab)
  icm2 = getNewColorModel(Lab)
  return plot(L,icm2)

def plot(L,icm):
  pp = PlotPanel(2,1)
  pv = pp.addPixels(0,0,f)
  pv.setColorModel(icm)
  pv.setOrientation(PixelsView.Orientation.X1DOWN_X2RIGHT)
  pv.setInterpolation(PixelsView.Interpolation.LINEAR)
  pov = pp.addPoints(1,0,L)
  pov.setMarkStyle(PointsView.Mark.FILLED_CIRCLE)
  pov.setMarkSize(2)
  pov.setLineStyle(PointsView.Line.NONE)
  pp.setHLabel(0,"Color value")
  pp.setVLabel(1,"Lightness (L*)")
  pp.setVLimits(1,0,100)
  return pp

def getNewColorModel(Lab):
  col = zeros(len(x),Color)
  for i in range(len(x)):
    j = 3*i
    rgb = ColorMap.cieLabToRgb(Lab[j+0],Lab[j+1],Lab[j+2])
    col[i] = Color(rgb[0],rgb[1],rgb[2]);
  cm = ColorMap(0,1,col)
  return cm.getColorModel()
  
def getRgbAndLab():
  cm = ColorMap(icm)
  Lab = zerofloat(n*3)
  rgb = zerofloat(n*3)
  color = zerofloat(3)
  for i in range(len(x)):
    cieLab = cm.getCieLabFloats(f[i])
    color = cm.getRgbFloats(f[i])
    rgb[3*i+0] = color[0]
    rgb[3*i+1] = color[1]
    rgb[3*i+2] = color[2]
    Lab[3*i+0] = cieLab[0]
    Lab[3*i+1] = cieLab[1]
    Lab[3*i+2] = cieLab[2]
  return rgb,Lab

def getLightnessFromLab(Lab):
  L = zerofloat(len(Lab)/3)
  for i in range(len(L)):
    L[i] = Lab[3*i]
  return L

def setUniformLightness(Lab,v):
  for i in range(len(Lab)/3):
    Lab[3*i] = v
  return Lab

def setMonotonicallyIncreasingLightness(Lab):
  for i in range(len(Lab)/3):
    Lab[3*i] = i * (50.0/256.0) + 25 
  return Lab

def smoothLightness(Lab):
  w = 10;
  n = len(Lab)/3
  for k in range(5):
    for i in range(n):
      lw = max(0,i-w)
      rw = min(n,i+w)

      val = 0.0
      for j in range(lw,rw):
        val += Lab[3*j]
      val /= rw-lw
      Lab[3*i] = val

  return Lab

n = 256; d1 = .0039; f1 = 0.0;
x = rampfloat(f1,d1,n)
f = zerofloat(1,n)
for i in range(n):
  f[i][0] = x[i]
s1 = Sampling(n,d1,f1)
icm = ColorMap.HUE

##############################################################################

class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain())
