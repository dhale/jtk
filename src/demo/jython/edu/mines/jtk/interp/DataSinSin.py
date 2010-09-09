#############################################################################
# Data and plotting for synthetic data f(x,y) = (sin(2*pi*x)*sin(2*pi*y))^2.

from java.lang import *
from java.util import *

#from edu.mines.jtk.dsp import *
from edu.mines.jtk.util.ArrayMath import *

from DataSquare import *

def dataSinSin(n):
  r = Random(31415)
  x = zerofloat(n)
  y = zerofloat(n)
  f = zerofloat(n)
  sx,sy = samplingsSquare("coarse")
  nx,ny = sx.count,sy.count
  mark = zeroint(nx,ny)
  for i in range(n):
    marked = False
    while not marked:
      xi = r.nextFloat()
      yi = r.nextFloat()
      ix = sx.indexOfNearest(xi)
      iy = sy.indexOfNearest(yi)
      if not mark[iy][ix]:
        xi = sx.getValue(ix)
        yi = sy.getValue(iy)
        ss = sin(2*PI*xi)*sin(2*PI*yi)
        f[i] = ss*ss
        x[i] = xi
        y[i] = yi
        mark[iy][ix] = 1
        marked = True
  return x,y,f

samplingsSinSin = samplingsSquare
plot2SinSin = plot2Square
plot3SinSin = plot3Square
