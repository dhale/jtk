# Copyright 2015, Colorado School of Mines and others.
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
import sys
from java.awt import *
from java.lang import *
from javax.swing import *
from edu.mines.jtk.awt import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.util import *
from edu.mines.jtk.util.ArrayMath import *
 
# 2014 Average Rainfall in inches
# Data provided by Weather Underground (www.wunderground.com)
#        Houston  Denver Seattle
data = [  0.04,   0.02,   0.12,  # Jan
          0.10,   0.01,   0.21,  # Feb
          0.08,   0.02,   0.25,  # Mar
          0.07,   0.05,   0.12,  # Apr
          0.42,   0.13,   0.08,  # May
          0.09,   0.06,   0.01,  # Jun
          0.20,   0.16,   0.00,  # Jul
          0.05,   0.07,   0.00,  # Aug
          0.16,   0.07,   0.03,  # Sep
          0.11,   0.05,   0.18,  # Oct
          0.12,   0.02,   0.14,  # Nov
          0.24,   0.01,   0.15   # Dec
       ]

KHOU = 0
KBKF = 1
KBFI = 2
s2 = Sampling(12,1.0,1.0)

BLUE_500  = Color(0x2196F3,False)
AMBER_500 = Color(0xFFC107,False)
PINK_500  = Color(0xE91E63,False)
 
# For testing below.
def makePlotPanel(f):
  pp = PlotPanel()
  bv = pp.addBars(s2,f)
  bv.setFillColor(0,BLUE_500)
  bv.setFillColor(1,AMBER_500)
#  bv.setFillColor(2,PINK_500)
  bv.setLineColor(Color.BLACK)
  bv.setBarWidth(0.98)
  bv.setAlignment(BarsView.Alignment.ALIGN_CENTER)
  bv.setOrientation(BarsView.Orientation.X1DOWN_X2RIGHT)
  bv.setStackBars(True)
  return pp

def getData():
  f = [[0 for i in xrange(12)] for i in xrange(2)]
  for i in range(12):
    f[KHOU][i] = data[KHOU+3*i]
    f[KBKF][i] = data[KBKF+3*i]
#    f[KBFI][i] = data[KBFI+3*i]
  return f

def getData2():
  f = [[0 for i in xrange(10)] for i in xrange(3)]
  for i in range(10):
    f[0][i] = 10
    f[1][i] = i
    f[2][i] = 10-i

  return f
 
def main(args):
  # One plot panel.
  f = getData()
  pp = makePlotPanel(f)
  pp.setVLabel("Average Rainfall (inches)")
  pp.setHLabel("Month")
  pp.setTitle("2014 Average Rainfall (in)")
  pf = PlotFrame(pp)
  pf.pack();
  pf.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE)
  pf.setVisible(True)
 
#############################################################################
class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain())
