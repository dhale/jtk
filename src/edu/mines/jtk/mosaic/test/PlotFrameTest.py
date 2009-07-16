import sys
from java.awt import *
from java.lang import *
from javax.swing import *
from edu.mines.jtk.awt import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.util import *
from edu.mines.jtk.util.ArrayMath import *

True = 1
False = 0

n1 = 101;  d1 = 0.1;  f1 = 0.0
n2 = 101;  d2 = 0.1;  f2 = 0.0
f = sin(rampfloat(0.0,d1,d2,n1,n2))
s1 = Sampling(n1,d1,f1)
s2 = Sampling(n2,d2,f2)

ax = 0.5*d2*(n2-1)
x1 = rampfloat(f1,d1,n1)
x2 = add(ax,mul(ax,sin(x1)))

# For testing below.
def makePlotPanel(orientation):
  pp = PlotPanel(1,2,orientation)

  pxv0 = pp.addPixels(0,0,s1,s2,f)
  pxv1 = pp.addPixels(0,1,s1,s2,f)
  pxv0.setColorModel(ColorMap.GRAY)
  pxv1.setColorModel(ColorMap.JET)

  gv0 = pp.addGrid(0,0)
  gv1 = pp.addGrid(0,1)
  gv0.setVertical(GridView.Vertical.ZERO)
  gv0.setColor(Color.YELLOW)
  gv1.setParameters("HVw-.")

  ptv0 = pp.addPoints(0,0,s1,x2)
  ptv1 = pp.addPoints(0,1,x1,x2)
  ptv0.setStyle("r--.")
  ptv1.setStyle("k-o")
  ptv0.setLineWidth(3)

  pp.addColorBar("amplitude")
  pp.title = "A Test of PlotFrame"
  pp.setHLabel(0,"offset (km)")
  pp.setHLabel(1,"velocity (km/s)")
  pp.setVLabel("depth (km)")

  return pp

def main(args):
  # One plot panel.
  #pp = makePlotPanel(PlotPanel.Orientation.X1DOWN_X2RIGHT)
  #pf = PlotFrame(pp)

  # Two plot panels.
  pp1 = makePlotPanel(PlotPanel.Orientation.X1DOWN_X2RIGHT)
  pp2 = makePlotPanel(PlotPanel.Orientation.X1RIGHT_X2UP)
  pf = PlotFrame(pp1,pp2,PlotFrame.Split.VERTICAL)
  pf.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE)
  pf.setBackground(Color.CYAN)
  #pf.setFontSize(24)
  #pf.setFontSizeForSlide(1,1)
  pf.setFontSizeForPrint(8,504) # 504 pt for a 2-column figure
  pf.pack();
  pf.setVisible(True)
  #pf.paintToPng(720,3.3333,"junk.png")

#############################################################################
class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain()) 
