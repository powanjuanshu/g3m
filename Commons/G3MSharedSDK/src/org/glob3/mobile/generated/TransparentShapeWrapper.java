package org.glob3.mobile.generated; 
///#include "GPUProgramState.hpp"

public class TransparentShapeWrapper extends OrderedRenderable
{
  private Shape _shape;
  private final double _squaredDistanceFromEye;
  private GLState _parentGLState;
  private final boolean _renderNotReadyShapes;
  public TransparentShapeWrapper(Shape shape, double squaredDistanceFromEye, GLState parentGLState, boolean renderNotReadyShapes)
  {
     _shape = shape;
     _squaredDistanceFromEye = squaredDistanceFromEye;
     _parentGLState = parentGLState;
     _renderNotReadyShapes = renderNotReadyShapes;
  }

  public final double squaredDistanceFromEye()
  {
    return _squaredDistanceFromEye;
  }

  public final void render(G3MRenderContext rc)
  {
    _shape.render(rc, _parentGLState, _renderNotReadyShapes);
  }
}
//void ShapesRenderer::createGLState(){
////  _glState.getGLGlobalState()->enableDepthTest();
//  
////  GPUProgramState& progState = *_glState.getGPUProgramState();
////  progState.setUniformValue(EnableTexture, false);
////  progState.setUniformValue(POINT_SIZE, (float)1.0);
////  progState.setUniformValue(SCALE_TEXTURE_COORDS, Vector2D(1.0,1.0));
////  progState.setUniformValue(TRANSLATION_TEXTURE_COORDS, Vector2D(0.0,0.0));
////  
////  progState.setUniformValue(ColorPerVertexIntensity, (float)0.0);
////  progState.setUniformValue(EnableFlatColor, false);
////  progState.setUniformValue(FLAT_COLOR, (float)0.0, (float)0.0, (float)0.0, (float)0.0);
////  progState.setUniformValue(FlatColorIntensity, (float)0.0);
////  
////  progState.setAttributeEnabled(TEXTURE_COORDS, false);
////  progState.setAttributeEnabled(COLOR, false);
//}
