//
//  GLFeature.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 18/03/13.
//
//

#include "GLFeature.hpp"

BillboardGLFeature::BillboardGLFeature(int textureWidth, int textureHeight, int viewportWidth, int viewportHeight):
GLFeature(NO_GROUP){

  _texExtent = new GPUUniformValueVec2Float(textureWidth, textureHeight);
  _values.addUniformValue(TEXTURE_EXTENT, _texExtent);

  _viewportExtent = new GPUUniformValueVec2Float(viewportWidth, viewportHeight);
  _values.addUniformValue(VIEWPORT_EXTENT, _viewportExtent);
}

BillboardGLFeature::~BillboardGLFeature(){
  _texExtent->_release();
  _viewportExtent->_release();
}

GeometryGLFeature::GeometryGLFeature(IFloatBuffer* buffer, int arrayElementSize, int index, bool normalized, int stride,
                  bool depthTestEnabled,
                  bool cullFace, int culledFace,
                  bool  polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits,
                  float lineWidth,
                  bool needsPointSize, float pointSize):
GLFeature(NO_GROUP),
_depthTestEnabled(depthTestEnabled),
_cullFace(cullFace),
_culledFace(culledFace),
_polygonOffsetFill(polygonOffsetFill),
_polygonOffsetFactor(polygonOffsetFactor),
_polygonOffsetUnits(polygonOffsetUnits),
_lineWidth(lineWidth)
{
  
  _position = new GPUAttributeValueVec4Float(buffer, arrayElementSize, index, stride, normalized);
  _values.addAttributeValue(POSITION, _position);

//  _globalState = new GLGlobalState();
//  if (depthTestEnabled){
//    _globalState->enableDepthTest();
//  } else{
//    _globalState->disableDepthTest();
//  }
//
//  if (cullFace){
//    _globalState->enableCullFace(culledFace);
//  } else{
//    _globalState->disableCullFace();
//  }
//
//  if (polygonOffsetFill){
//    _globalState->enablePolygonOffsetFill(polygonOffsetFactor, polygonOffsetFill);
//  } else{
//    _globalState->disPolygonOffsetFill();
//  }
//
//  _globalState->setLineWidth(lineWidth);

  if (needsPointSize){
    _values.addNewUniformValue(POINT_SIZE, new GPUUniformValueFloat(pointSize));
  }
}

void GeometryGLFeature::applyOnGlobalGLState(GLGlobalState* state) const{
  if (_depthTestEnabled){
    state->enableDepthTest();
  } else{
    state->disableDepthTest();
  }

  if (_cullFace){
    state->enableCullFace(_culledFace);
  } else{
    state->disableCullFace();
  }

  if (_polygonOffsetFill){
    state->enablePolygonOffsetFill(_polygonOffsetFactor, _polygonOffsetUnits);
  } else{
    state->disPolygonOffsetFill();
  }

  state->setLineWidth(_lineWidth);
}


GeometryGLFeature::~GeometryGLFeature(){
  _position->_release();
}

TextureGLFeature::TextureGLFeature(const IGLTextureId* texID,
                                   IFloatBuffer* texCoords, int arrayElementSize, int index, bool normalized, int stride,
                                   bool blend, int sFactor, int dFactor,
                                   bool coordsTransformed, const Vector2D& translate, const Vector2D& scale):
GLColorGroupFeature(4, blend, sFactor, dFactor),
_texID(texID)
{
//  _globalState->bindTexture(texID);

  GPUAttributeValueVec4Float* value = new GPUAttributeValueVec4Float(texCoords, arrayElementSize, index, stride, normalized);
  _values.addNewAttributeValue(TEXTURE_COORDS, value);

  if (coordsTransformed){
    _values.addNewUniformValue(TRANSLATION_TEXTURE_COORDS,
                            new GPUUniformValueVec2Float((float)translate._x, (float)translate._y));
    _values.addNewUniformValue(SCALE_TEXTURE_COORDS,
                            new GPUUniformValueVec2Float((float)scale._x, (float)scale._y));
  }
}

void TextureGLFeature::applyOnGlobalGLState(GLGlobalState* state) const{
  blendingOnGlobalGLState(state);
  state->bindTexture(_texID);
}

ColorGLFeature::ColorGLFeature(IFloatBuffer* colors, int arrayElementSize, int index, bool normalized, int stride,
                               bool blend, int sFactor, int dFactor):
GLColorGroupFeature(3, blend, sFactor, dFactor)
{
  GPUAttributeValueVec4Float* value = new GPUAttributeValueVec4Float(colors, arrayElementSize, index, stride, normalized);
  _values.addNewAttributeValue(COLOR, value);
}

FlatColorGLFeature::FlatColorGLFeature(const Color& color,
                                       bool blend, int sFactor, int dFactor):
GLColorGroupFeature(2, blend, sFactor, dFactor)
{
  _values.addNewUniformValue(FLAT_COLOR, new GPUUniformValueVec4Float(color.getRed(),
                                                           color.getGreen(),
                                                           color.getBlue(),
                                                           color.getAlpha()));
}

