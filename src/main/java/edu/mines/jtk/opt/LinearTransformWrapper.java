/****************************************************************************
Copyright 2003, Landmark Graphics and others.
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
package edu.mines.jtk.opt;

/** Wrap a LinearTransform as a non-linear Transform,
    by ignoring reference model.
    @author W.S. Harlan
*/
public class LinearTransformWrapper implements Transform {
  private LinearTransform _linearTransform = null;

  /** Constructor.
      @param linearTransform Wrap this as a general Transform
  */
  public LinearTransformWrapper(LinearTransform linearTransform) {
    _linearTransform = linearTransform;
  }

  // Transform
  public void forwardNonlinear(Vect data, VectConst model) {
    _linearTransform.forward(data, model);
  }

  // Transform
  public void forwardLinearized(Vect data,
                                VectConst model,
                                VectConst modelReference) {
    _linearTransform.forward(data, model);
  }

  // Transform
  public void addTranspose(VectConst data,
                           Vect model,
                           VectConst modelReference) {
    _linearTransform.addTranspose(data, model);
  }

  // Transform
  public void inverseHessian(Vect model, VectConst modelReference) {
    _linearTransform.inverseHessian(model);
  }

  // Transform
  public void adjustRobustErrors(Vect dataError) {
    _linearTransform.adjustRobustErrors(dataError);
  }
}
