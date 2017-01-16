# Copyright 2017, Colorado School of Mines and others.
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
#
# Author: Chris Engelsma
# Version: 2017.01.16

import sys
from java.lang import *
from javax.swing import *
from edu.mines.jtk.sgl import *
from java.beans import PropertyChangeListener
import time
from threading import Thread

LIGHT_X = -0.1
LIGHT_Y = -0.1
LIGHT_Z =  1.0
LIGHT_W =  0.0

lightPosition = [LIGHT_X,LIGHT_Y,LIGHT_Z,LIGHT_W]

def makeSineWave():
  nx, ny = 100, 100
  dx = 10.0 / (float(nx))
  dy = 10.0 / (float(ny))
  xyz = []
  for ix in range(nx):
    x0 = ix * dx
    x1 = (ix + 1) * dx
    for iy in range(ny):
      y0 = iy * dy
      y1 = (iy + 1) * dy
      xyz.append(x0), xyz.append(y0), xyz.append(sin(x0, y0))
      xyz.append(x0), xyz.append(y1), xyz.append(sin(x0, y1))
      xyz.append(x1), xyz.append(y0), xyz.append(sin(x1, y0))
      xyz.append(x1), xyz.append(y0), xyz.append(sin(x1, y0))
      xyz.append(x0), xyz.append(y1), xyz.append(sin(x0, y1))
      xyz.append(x1), xyz.append(y1), xyz.append(sin(x1, y1))
  return xyz

def addBulge(xyz):
  t = []
  for i in range(len(xyz)/3):
    x = xyz[3*i  ]
    y = xyz[3*i+1]
    z = xyz[3*i+2]
    z -= exp(x,y)
    t.append(x)
    t.append(y)
    t.append(z)
  return t

def sin(x,y):
  return float(5.0+0.25*Math.sin(x+y));

def exp(x,y):
  x -= 5.0
  y -= 5.0
  x *= 0.4
  y *= 0.8
  return float(2.0*Math.exp(-x*x-y*y));


def fakeData():
  xyz = makeSineWave()
  xyz = addBulge(xyz)
  return xyz


class drawScene(PropertyChangeListener):
  def __init__(self):
    xyz = fakeData()
    self.sf = SimpleFrame()
    self.ls = sf.getOrbitView().getLightSource()
    self.ls.setPosition(lightPosition)
    tg = sf.addTriangles(xyz)

    timer = Timer(self)
    timer.addPropertyChangeListener(self)
    timer.execute()

  def propertyChange(self, e):
    if e.propertyName == 'light':
      self.ls.setPosition(lightPosition)


def main(argv):
  scene = drawScene()


##############################################################################
class Timer(SwingWorker):
  def __init__(self,gui):
    self.gui = gui
    SwingWorker.__init__(self)

  def doInBackground(self):
    now = time.time()
    light = 0
    while True:
      try:
        Thread.sleep(1000)

        LIGHT_X = sin(0,time.time()-now)
        lightPosition = [LIGHT_X, LIGHT_Y, LIGHT_Z, LIGHT_W]
      except InterruptedException, e:
        pass


class RunMain(Runnable):
  def run(self):
    main(sys.argv)

SwingUtilities.invokeLater(RunMain())

