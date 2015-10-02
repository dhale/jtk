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
