from edu.mines.jtk.mosaic import *
from edu.mines.jtk.util import *

n1 = 101
n2 = 101
f = Array.rampfloat(0.0,0.1,0.1,n1,n2)
f = Array.sin(f)

orientation = PlotFrame.Orientation.X1DOWN_X2RIGHT
pf = PlotFrame(1,2,orientation)
pv0 = pf.addPixels(0,0,f)
pv1 = pf.addPixels(0,1,f)
pv0.setColorModel(ByteIndexColorModel.linearGray(0.0,1.0))
pv1.setColorModel(ByteIndexColorModel.linearHue(0.0,0.67))
pf.addColorBar("amplitude")
pf.title = "A Test of PlotFrame"
pf.setX1Label("depth (km)")
pf.setX2Label(0,"offset (km)")
pf.setX2Label(1,"velocity (km/s)")
pf.setVisible(1)
pf.paintToPng(300,6,"junk.png")
