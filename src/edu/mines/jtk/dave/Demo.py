from edu.mines.jtk.demo import *
from edu.mines.jtk.dsp import *
from math import *

True = 1
False = 0

def makeReal1(x):
  return Real1(len(x),1.0,0.0,x)

def plotArray(x):
  plotReal1(Real1(len(x),1.0,0.0,x))

def plotReal1(rx):
  SpectrumPlot(rx)

def bob1():
  return SpectrumPlot.readData("data/bob1.txt")
def dylan1():
  return SpectrumPlot.readData("data/dylan1.txt")
def mike1():
  return SpectrumPlot.readData("data/mike1.txt")
def emily1():
  rx = SpectrumPlot.readData("data/emily1.txt")
  return rx
  #return Real1(rx.getSampling(),Rap.sub(rx.getF(),550))
def matt1():
  rx = SpectrumPlot.readMatt("data/matt1.txt")
  return rx
  #return Real1(rx.getSampling(),Rap.sub(rx.getF(),14000))

#############################################################################
# Simple functions for interesting signals

def shift(m,rx):
  return Real1(rx.getSampling().shift(m),rx.getValues())

def impulse(m,n):
  x = Rap.zero(n)
  x[m] = 1
  return x

def box(m,n):
  x = Rap.zero(n)
  for i in range(0,m):
    x[i] = 1
  return x

def sinc(f,n):
  m = n/2
  w = 2*pi*f
  e = 0.0001;
  a = Rap.ramp(-w*(m+e),w,n)
  s = Rap.mul(w/pi,Rap.div(Rap.sin(a),a))
  sx = Real1(n,1.0,-m,s)
  return sx

def problems(n):
  x = Rap.zero(n);  x[0] =  1;  x[1] =  1;  plotArray(x)
  x = Rap.zero(n);  x[0] =  1;  x[1] = -1;  plotArray(x)
  x = Rap.zero(n);  x[0] =  1;  x[2] =  1;  plotArray(x)
  x = Rap.zero(n);  x[0] =  1;  x[2] = -1;  plotArray(x)

#px = SpectrumPlot(emily1(),1)
#problems(101)
#impulse(2,101)
#sinc(0.25,101)
#box(6,101)
