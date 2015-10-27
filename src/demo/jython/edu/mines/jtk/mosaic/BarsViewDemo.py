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
# Author: Chris Engelsma
# Version: 2015.10.27

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

# Yearly US Budget Surplus and GDP  (Millions)
#         Surplus       GDP
gdp = [ -484602.0,	17700000.0,
        -679544.0,	17080000.0,
        -1086963.0,	16330000.0,
        -1299593.0,	15790000.0,
        -1294373.0,	15230000.0,
        -1412688.0,	14570000.0,
        -458553.0,	14550000.0,
        -160701.0,	14690000.0,
        -248181.0,	14070000.0,
        -318346.0,	13380000.0,
        -412727.0,	12560000.0,
        -377585.0,	11820000.0,
        -157758.0,	11100000.0,
        128236.0,	10700000.0,
        236241.0,	10470000.0,
        125610.0,	9930000.0,
        69270.0,	9330000.0,
        -21884.0,	8790000.0,
        -107431.0,	8290000.0,
        -163952.0,	7800000.0,
        -203186.0,	7480000.0,
        -255051.0,	7030000.0,
        -290321.0,	6700000.0,
        -269238.0,	6280000.0,
        -221036.0,	6020000.0,
        -152639.0,	5760000.0,
        -155178.0,	5410000.0,
        -149730.0,	5020000.0,
        -221227.0,	4670000.0,
        -212308.0,	4450000.0,
        -185367.0,	4150000.0,
        -207802.0,	3800000.0,
        -127977.0,	3410000.0,
        -78968.0,	3280000.0,
        -73830.0,	2990000.0,
        -40726.0,	2730000.0,
        -59185.0,	2480000.0,
        -53659.0,	2170000.0,
        -73732.0,	1940000.0,
        -53242.0,	1770000.0,
        -6135.0,	1600000.0,
        -14908.0,	1480000.0,
        -23373.0,	1330000.0,
        -23033.0,	1190000.0,
        -2842.0,	1090000.0,
        3242.0,	1040000.0,
        -25161.0,	970000.0,
        -8643.0,	880000.0,
        -3698.0,	830000.0,
        -1411.0,	770000.0,
        -5915.0,	700000.0,
        -4756.0,	650000.0,
        -7146.0,	610000.0,
        -3335.0,	580000.0,
        301.0,	540000.0,
        -12849.0,	530000.0,
        -2769.0,	500000.0,
        3412.0,	480000.0,
        3947.0,	460000.0,
        -2993.0,	440000.0,
        -1154.0,	400000.0,
        -6493.0,	390000.0,
        -1519.0,	380000.0,
        6102.0,	360000.0,
        -3119.0,	320000.0,
        580.0,	270000.0,
        11796.0,	280000.0,
        4018.0,	260000.0,
        -15936.0,	230000.0,
        -47553.0,	230000.0,
        -47557.0,	220000.0,
        -54554.0,	200000.0,
        -20503.0,	170000.0,
        -4941.0,	130000.0,
        -2920.0,	100000.0,
        -2846.0,	90000.0,
        -89.0,	90000.0,
        -2193.0,	90000.0,
        -4304.0,	80000.0,
        -2803.0,	70000.0,
        -3586.0,	70000.0,
        -2602.0,	60000.0,
        -2735.0,	60000.0,
        -462.0,	80000.0,
        738.0,	90000.0
      ]


KHOU = 0
KBKF = 1
KBFI = 2
n2 = 12
d2 = 1.0
f2 = 1.0

n3 = 85
d3 = 1.0
f3 = 1930

s3 = Sampling(n3,d3,f3)
s2 = Sampling(n2,d2,f2)

BLUE_500  = Color(0x2196F3,False)
AMBER_500 = Color(0xFFC107,False)
PINK_500  = Color(0xE91E63,False)
 
# For testing below.
def makePlotPanel1(f):
  pp = PlotPanel()
  bv = pp.addBars(s2,f)
  bv.setFillColor(0,BLUE_500)
  bv.setFillColor(1,AMBER_500)
  bv.setFillColor(2,PINK_500)
  bv.setLineColor(Color.BLACK)
  bv.setBarWidth(0.98)
  bv.setAlignment(BarsView.Alignment.ALIGN_CENTER)
  bv.setOrientation(BarsView.Orientation.X1RIGHT_X2UP)
  bv.setStackBars(True)
  return pp

def makePlotPanel2(f):
  pp = PlotPanel()
  gv = pp.addGrid()
  gv.setVertical(GridView.Vertical.ZERO)
  gv.setColor(Color.GRAY)
  bv = pp.addBars(s3,f)
  c0 = Color(133,125,125)
  c1 = Color(250,8,8)
  bv.setColorMap(ColorMap(0,5,prettyColors(c0,c1)))
  bv.setLineColor(Color.WHITE)
  bv.setBarWidth(1.00)
  bv.setAlignment(BarsView.Alignment.ALIGN_CENTER)
  bv.setOrientation(BarsView.Orientation.X1DOWN_X2RIGHT)
  return pp

def makePlotPanel3(f):
  pp = PlotPanel()
  gv = pp.addGrid()
  gv.setVertical(GridView.Vertical.ZERO)
  gv.setColor(Color.GRAY)
  bv = pp.addBars(f)
  bv.setFillColor(BLUE_500)
  bv.setBarWidth(1.00)
  bv.setAlignment(BarsView.Alignment.ALIGN_CENTER)
  bv.setOrientation(BarsView.Orientation.X1DOWN_X2RIGHT)
  return pp

def prettyColors(c0,c1):
  r0 = c0.red/255.0
  g0 = c0.green/255.0
  b0 = c0.blue/255.0

  r1 = c1.red/255.0
  g1 = c1.green/255.0
  b1 = c1.blue/255.0

  color = []
  dr = (r1-r0) / 256.0
  dg = (g1-g0) / 256.0
  db = (b1-b0) / 256.0
  for i in range(256):
    color.append(Color(r0+i*dr,g0+i*dg,b0+i*db))
  return ColorMap.makeIndexColorModel(color)

def compileWeather():
  f = [[0 for i in xrange(12)] for i in xrange(3)]
  for i in range(12):
    f[KHOU][i] = data[KHOU+3*i]
    f[KBKF][i] = data[KBKF+3*i]
    f[KBFI][i] = data[KBFI+3*i]
  return f

def compileDeficit():
  l = len(gdp)/2
  f = [0 for i in xrange(l)]
  for i in range(l):
    f[n3-i-1] = -1*(gdp[2*i]/gdp[2*i+1])*100
  return f

def fakeData():
  f = [0 for i in xrange(10)]
  for i in range(10):
    f[i] = i-5
  return f
 
def test1():
  f = compileWeather()
  pp = makePlotPanel1(f)
  pp.setVLabel("Average Rainfall (inches)")
  pp.setHLabel("Month")
  pp.setTitle("2014 Average Rainfall (in)")
  pf = PlotFrame(pp)
  pf.pack();
  pf.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE)
  pf.setVisible(True)

def test2():
  f = compileDeficit()
  pp = makePlotPanel2(f)
  pp.setTitle("US Deficit as Percent of GDP (1930-2014)")
  pp.setHLabel("Deficit (% of GDP)")
  pp.setVLabel("Fiscal Year")
  pf = PlotFrame(pp)
  pf.pack();
  pf.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE)
  pf.setVisible(True)

def test3():
  f = fakeData()
  pp = makePlotPanel3(f)
  pf = PlotFrame(pp)
  pf.pack();
  pf.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE)
  pf.setVisible(True)
 
def main(args):
  test1()
#  test2()
#  test3()

#############################################################################
class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain())
