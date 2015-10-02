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
package edu.mines.jtk.sgl;

/**
 * Orientation of the three axes X, Y, and Z.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.07.24
 */
public enum AxesOrientation {
  XRIGHT_YUP_ZOUT,
  XRIGHT_YOUT_ZDOWN,
  XRIGHT_YIN_ZDOWN,
  XOUT_YRIGHT_ZUP,
  XDOWN_YRIGHT_ZOUT,
  XUP_YLEFT_ZOUT,
  XUP_YRIGHT_ZOUT
}
