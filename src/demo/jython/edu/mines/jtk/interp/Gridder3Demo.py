import sys
from java.lang import *
from java.util import *
from javax.swing import *

from edu.mines.jtk.awt import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.interp import *
from edu.mines.jtk.sgl import *
from edu.mines.jtk.util.ArrayMath import *

def main(args):
  demoDiscreteSibsonGridder3()
 
def demoDiscreteSibsonGridder3():
  s1 = Sampling(51,0.02,0.0)
  s2 = Sampling(51,0.02,0.0)
  s3 = Sampling(51,0.02,0.0)
  n = 100
  x1 = randfloat(n)
  x2 = randfloat(n)
  x3 = randfloat(n)
  f = zerofloat(n)
  for i in range(n):
    f[i] = x1[i]+x2[i]+x3[i]
    #f[i] = sin(PI*x1[i])*sin(PI*x2[i])*sin(PI*x3[i])
  for sg in [DiscreteSibsonGridder3(f,x1,x2,x3),
             BlendedGridder3(f,x1,x2,x3)]:
    q = sg.grid(s1,s2,s3)
    sf = SimpleFrame()
    ipg = sf.addImagePanels(s1,s2,s3,q)
    ipg.setColorModel(ColorMap.JET)

#############################################################################
# Run everything on Swing thread.
class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain())
