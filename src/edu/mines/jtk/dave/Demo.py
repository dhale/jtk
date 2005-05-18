import math
from edu.mines.jtk.util.Array import *
from edu.mines.jtk.dave import *
from edu.mines.jtk.dsp import *

True = 1
False = 0

def makeReal1(x):
  return Real1(len(x),1.0,0.0,x)

def plotArray(x):
  plotReal1(Real1(len(x),1.0,0.0,x))

def plotArraydb(x):
  plotReal1db(Real1(len(x),1.0,0.0,x))

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
  #return Real1(rx.getSampling(),sub(rx.getF(),550))
def hunter1():
  return Real1Reader.readData("data/hunter1.txt",2)
def jen1():
  return Real1Reader.readData("data/jen1.txt",2)
def matt1(c):
  rx = Real1Reader.readData("data/matt1.txt",c)
  return rx
  #return Real1(rx.getSampling(),sub(rx.getF(),14000))
def mike1():
  return Real1Reader.readData("data/mike1.txt")

#############################################################################
# Simple functions for interesting signals

def subsample(x):
  n = len(x)
  m = n/2
  y = zerofloat(m)
  for i in range(0,m):
    y[i] = x[2*i]
  return y

def supersample(x):
  n = len(x)
  m = n*2
  y = zerofloat(m)
  for i in range(0,n):
    y[2*i] = x[i]
  return y

def sub(rx):
  s = rx.x1;
  x = rx.f;
  n = len(x)
  m = n/2
  y = zerofloat(m)
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
  y = zerofloat(m)
  for i in range(0,n):
    y[2*i] = x[i]
  s = Sampling(m,0.5*s.delta,s.first)
  ry = Real1(s,y)
  return ry

def shift(m,rx):
  return Real1(rx.getSampling().shift(m),rx.getValues())

def impulse(m,n):
  x = zerofloat(n)
  x[m] = 1
  return x

def box(m,n):
  x = zerofloat(n)
  for i in range(0,m):
    x[i] = 1
  return x

def sinc(f,n):
  m = n/2
  w = 2*pi*f
  e = 0.0001;
  a = rampfloat(-w*(m+e),w,n)
  s = mul(w/math.pi,div(sin(a),a))
  sx = Real1(n,1.0,-m,s)
  return sx

def problems(n):
  x = zerofloat(n);  x[0] =  1;  x[1] =  1;  plotArray(x)
  x = zerofloat(n);  x[0] =  1;  x[1] = -1;  plotArray(x)
  x = zerofloat(n);  x[0] =  1;  x[2] =  1;  plotArray(x)
  x = zerofloat(n);  x[0] =  1;  x[2] = -1;  plotArray(x)

#px = SpectrumPlot(emily1(),1)
#problems(101)
#impulse(2,101)
#sinc(0.25,101)
#box(6,101)

#############################################################################
# Test filter

HP = ButterworthFilter.Type.HIGH_PASS
LP = ButterworthFilter.Type.LOW_PASS

def filterReal1X(r,fl,al,fh,ah):
  n1 = r.x1.count
  d1 = r.x1.delta
  f1 = r.x1.first
  fl *= d1
  fh *= d1
  bf = ButterworthFilter(fl,al,fh,ah)
  x = r.f
  y = zerofloat(n1)
  bf.apply(x,y)
  return Real1(n1,d1,f1,y)

def filterReal1(r,f3db,npole,type):
  n1 = r.x1.count
  d1 = r.x1.delta
  f1 = r.x1.first
  f3db *= d1
  bf = ButterworthFilter(f3db,npole,type)
  x = r.f
  y = zerofloat(n1)
  bf.apply(x,y)
  return Real1(n1,d1,f1,y)

def makeImpulse(n,m=0):
  return Real1(n,1.0,-m,impulse(m,n))

x = makeImpulse(101)
y = filterReal1(x,0.25,3,LP)
plotReal1(y)

#ri = emily1(1)
#plotReal1db(ri)
#ro = filterReal1(ri,0.002,0.01,0.02,0.99)
#ro = filterReal1(ri,0.002,1)
#plotReal1db(ro)





























