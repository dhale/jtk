from edu.mines.jtk.dave import *
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

def plotReal1db(rx):
  SpectrumPlot(rx,True)

#############################################################################
# Data from GP404 students

def bob1():
  return Real1Reader.readData("data/bob1.txt")
def dylan1():
  return Real1Reader.readData("data/dylan1.txt")
def emily1(c):
  rx = Real1Reader.readData("data/emily1.txt",2+c)
  return rx
  #return Real1(rx.getSampling(),Rap.sub(rx.getF(),550))
def hunter1():
  return Real1Reader.readData("data/hunter1.txt",2)
def jen1():
  return Real1Reader.readData("data/jen1.txt",2)
def matt1(c):
  rx = Real1Reader.readData("data/matt1.txt",c)
  return rx
  #return Real1(rx.getSampling(),Rap.sub(rx.getF(),14000))
def mike1():
  return Real1Reader.readData("data/mike1.txt")

#############################################################################
# Simple functions for interesting signals

def subsample(x):
  n = len(x)
  m = n/2
  y = Rap.zero(m)
  for i in range(0,m):
    y[i] = x[2*i]
  return y

def supersample(x):
  n = len(x)
  m = n*2
  y = Rap.zero(m)
  for i in range(0,n):
    y[2*i] = x[i]
  return y

def sub(rx):
  s = rx.x1;
  x = rx.f;
  n = len(x)
  m = n/2
  y = Rap.zero(m)
  for i in range(0,m):
    y[i] = x[2*i]
  s = Sampling(m,2*s.delta,s.first)
  ry = Real1(s,y)
  return ry

def super(rx):
  s = rx.x1;
  x = rx.f;
  n = len(x)
  m = 2*n
  y = Rap.zero(m)
  for i in range(0,n):
    y[2*i] = x[i]
  s = Sampling(m,0.5*s.delta,s.first)
  ry = Real1(s,y)
  return ry

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

#############################################################################
# Test filter

def testFilter(fl,al,fh,ah,n):
  x = Rap.zero(n)
  y = Rap.zero(n)
  bf = ButterworthFilter(fl,al,fh,ah)
  x[0] = 1
  bf.apply(x,y)
  plotArray(y)

testFilter(0.2,0.1,0.3,0.9,101)
