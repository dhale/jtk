import sys
from java.awt import *
from java.lang import *
from javax.swing import *

from edu.mines.jtk.awt import *
from edu.mines.jtk.dsp import *
from edu.mines.jtk.mosaic import *
from edu.mines.jtk.sgl import *
from edu.mines.jtk.util.ArrayMath import *

def main(args):
  #demo1()
  #demo2()
  demo3()

def demo1():
  #bpf = BandPassFilter(0.0,0.3,0.1,0.01)
  bpf = BandPassFilter(0.0,0.45,0.1,0.01)
  bpf.setExtrapolation(BandPassFilter.Extrapolation.ZERO_SLOPE)
  h = bpf.getCoefficients1()
  nh = len(h)
  print "filter length =",nh
  plotH1(h)
  plotA1(h)
  kh = (nh-1)/2
  h = zerofloat(nh); h[kh] = 1.0
  bpf.apply(h,h)
  plotH1(h)
  plotA1(h)

def demo2():
  bpf = BandPassFilter(0.0,0.45,0.1,0.01)
  bpf.setExtrapolation(BandPassFilter.Extrapolation.ZERO_SLOPE)
  h = bpf.getCoefficients2()
  nh = len(h)
  print "filter length =",nh
  plotA2(h)
  kh = (nh-1)/2
  #h = fillfloat(1.0,nh,nh)
  h = zerofloat(nh,nh); h[kh][kh] = 1.0
  bpf.apply(h,h)
  plotH2(h)
  plotA2(h)

def demo3():
  bpf = BandPassFilter(0.0,0.45,0.1,0.01)
  bpf.setExtrapolation(BandPassFilter.Extrapolation.ZERO_SLOPE)
  h = bpf.getCoefficients3()
  nh = len(h)
  print "filter length =",nh
  plotH3(h)
  plotA3(h)
  kh = (nh-1)/2
  #h = fillfloat(1.0,nh,nh,nh)
  h = zerofloat(nh,nh,nh); h[kh][kh][kh] = 1.0
  bpf.apply(h,h)
  plotH3(h)
  plotA3(h)

def plotH1(h):
  n1 = len(h)
  s1 = Sampling(n1,1,-(n1-1)/2)
  sp = SimplePlot()
  sp.setFontSizeForSlide(1.0,1.0)
  sp.setHLabel("n")
  sp.setVLabel("h[n]")
  sp.setSize(948,763)
  sv = sp.addSequence(s1,h)

def plotH2(h):
  n1,n2 = len(h[0]),len(h)
  s1,s2 = Sampling(n1,1,-(n1-1)/2),Sampling(n2,1,-(n2-1)/2)
  sp = SimplePlot()
  sp.setFontSizeForSlide(1.0,1.0)
  sp.addColorBar("Amplitude")
  sp.setHLabel("x1 (samples)")
  sp.setVLabel("x2 (sample)")
  sp.setSize(948,763)
  pv = sp.addPixels(s1,s2,h)
  pv.setColorModel(ColorMap.JET)
  pv.setInterpolation(PixelsView.Interpolation.NEAREST);

def plotH3(h):
  n1,n2,n3 = len(h[0][0]),len(h[0]),len(h)
  s1 = Sampling(n1,1,-(n1-1)/2)
  s2 = Sampling(n2,1,-(n2-1)/2)
  s3 = Sampling(n3,1,-(n3-1)/2)
  sf = SimpleFrame()
  ip = sf.addImagePanels(s1,s2,s3,h)
  ip.setColorModel(ColorMap.JET)

def plotA1(h):
  fft = Fft(h)
  fft.setCenter(True)
  fft.setPadding(100)
  ak = cabs(fft.applyForward(h))
  print "amplitude max =",max(ak)
  sk = fft.getFrequencySampling1()
  sp = SimplePlot()
  sp.setFontSizeForSlide(1.0,1.0)
  sp.setHLabel("k")
  sp.setVLabel("H(k)")
  sp.setSize(948,763)
  sv = sp.addPoints(sk,ak)

def plotA2(h):
  fft = Fft(h)
  fft.setCenter(True)
  fft.setPadding(100)
  ak = cabs(fft.applyForward(h))
  print "amplitude max =",max(ak)
  sk1 = fft.getFrequencySampling1()
  sk2 = fft.getFrequencySampling2()
  sp = SimplePlot()
  sp.setFontSizeForSlide(1.0,1.0)
  sp.addColorBar("Amplitude")
  sp.setHLabel("k1 (cycles/sample)")
  sp.setVLabel("k2 (cycles/sample)")
  sp.setSize(948,763)
  pv = sp.addPixels(sk1,sk2,ak)
  pv.setColorModel(ColorMap.JET)
  #SimplePlot.asPoints(sk1,ak[len(ak)/2])

def plotA3(h):
  fft = Fft(h)
  fft.setCenter(True)
  fft.setPadding(100)
  ak = cabs(fft.applyForward(h))
  print "amplitude max =",max(ak)
  sk1 = fft.getFrequencySampling1()
  sk2 = fft.getFrequencySampling2()
  sk3 = fft.getFrequencySampling2()
  sf = SimpleFrame()
  ip = sf.addImagePanels(sk1,sk2,sk3,ak)
  ip.setColorModel(ColorMap.JET)


#############################################################################
# Run everything on Swing thread.
class RunMain(Runnable):
  def run(self):
    main(sys.argv)
SwingUtilities.invokeLater(RunMain())
