#/****************************************************************************
# Copyright 2010, Colorado School of Mines and others.
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
#****************************************************************************/
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
