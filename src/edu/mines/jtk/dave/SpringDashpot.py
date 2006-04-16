# Jython program plots sampled data from GPGN 210 spring-dashpot experiment.
# Note: Jython is Python for Java; Jython programs can use Java packages.

# Java packages used below.
from edu.mines.jtk.dave import *
from edu.mines.jtk.mosaic import *

# More useful packages. We do not use these below, but we might.
#from math import *
#from edu.mines.jtk.util.Array import *
#from edu.mines.jtk.dsp import *

# These constants are built in to recent versions of Jython.
# We define them here in case we are using an old version.
True = 1
False = 0

# Returns a plot frame showing the two sampled sequences.
# Real1Reader is a utility class in the Java package edu.mines.jtk.dave.
# It can read a real-valued function of one sampled variable from an ASCII 
# text file.
def plotSpringDashpot():
  rss = Real1Reader.readData("data/SpringSpring.txt")
  rsd = Real1Reader.readData("data/SpringDashpot.txt")
  panel = PlotPanel(2,1)
  panel.setHLabel("time (s)")
  panel.setVLabel(0,"volts")
  panel.setVLabel(1,"volts")
  panel.addSequence(0,0,rss.sampling,rss.values)
  panel.addSequence(1,0,rsd.sampling,rsd.values)
  frame = PlotFrame(panel)
  frame.setSize(950,600)
  frame.setVisible(True)
  return frame

# Makes the plot frame. Interactively zoom and scroll by clicking and
# dragging the mouse within any axis or plot.
fsd = plotSpringDashpot()

# Save plot frame as 300 dots/inch x 3 inch wide PNG image.
# Can import this image into various word-processing programs.
fsd.paintToPng(300,3,"SpringDashpot.png")
