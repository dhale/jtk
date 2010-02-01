#############################################################################
# Data and plotting for synthetic data f(x,y) = sin(2*pi*x)*sin(2*pi*y).

from edu.mines.jtk.util.ArrayMath import *

from DataSquare import *

def dataSaddle():
  x = zerofloat(4); copy([0.1,0.9,0.1,0.9],x)
  y = zerofloat(4); copy([0.1,0.1,0.9,0.9],y)
  f = zerofloat(4); copy([0.0,1.0,1.0,0.0],f)
  return x,y,f

samplingsSaddle = samplingsSquare
plot2Saddle = plot2Square
plot3Saddle = plot3Square
