/****************************************************************************
Copyright 2009, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package jtkdemo.mosaic;

import javax.swing.*;

import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.mosaic.GridView.Horizontal;
import edu.mines.jtk.mosaic.GridView.Style;
import edu.mines.jtk.mosaic.GridView.Vertical;
import edu.mines.jtk.util.ArrayMath;

import static edu.mines.jtk.util.ArrayMath.*;

import java.awt.Color;

/**
 * Demos {@link edu.mines.jtk.mosaic.PointsView}
 * @author Eric Addison
 * @version 2016.1.22
 */
public class GridViewDemo {

  public static void main(String[] args) {
    
	  // DEMO of using user selected locations for gridview
	  
	  PlotPanel plot = new PlotPanel();
	  
	  // make some simple data
	  float[] x = ArrayMath.rampfloat(0, 1, 30);
	  PointsView pv = plot.addPoints(x,x);
	  pv.setLineColor(Color.RED);
	  
	  // set up lines for USER gridview
	  int n = 5;
	  float[] vLines = new float[2*n+1];
	  float[] hLines = new float[2*n+1];
	  
	  for(int i=0; i<n; i++){
		  vLines[n-i-1] = n - (i)*(i) + 10;
		  vLines[n+i-1] = n + (i)*(i) + 10;
		  hLines[n-i-1] = n - (i)*(i) + 10;
		  hLines[n+i-1] = n + (i)*(i) + 10;
	  }
	  
	  // add the gridview, set the type and line locations
	  GridView gv = plot.addGrid();
	  gv.setHorizontal(Horizontal.USER);
	  gv.setVertical(Vertical.USER);
	  gv.setVLineLocations(vLines);
	  gv.setHLineLocations(hLines);
	  gv.setStyle(Style.DASH);
	  
	  // mke a plot frame
	  PlotFrame frame = new PlotFrame(plot);
	  frame.setVisible(true);
  }
}
