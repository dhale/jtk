from edu.mines.jtk.dave import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.util import *

lx = 101
x = Array.sin(Array.rampfloat(1,0.2,lx))
lh = 11
h = Array.fillfloat(1.0/(lh-1),lh)
ly = lx
y = Array.zerofloat(ly)
Conv.conv(lx,0,x,lh,0,h,ly,0,y)
SequencePlot(x,h,y)
