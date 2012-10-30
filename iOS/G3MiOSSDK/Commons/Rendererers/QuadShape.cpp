//
//  QuadShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

#include "QuadShape.hpp"
#include "IGLTextureId.hpp"
#include "IImage.hpp"
#include "IFactory.hpp"
#include "TexturesHandler.hpp"
#include "FloatBufferBuilderFromCartesian3D.hpp"
#include "IntBufferBuilder.hpp"
#include "FloatBufferBuilderFromCartesian2D.hpp"
#include "IndexedMesh.hpp"
#include "GLConstants.hpp"
#include "TexturedMesh.hpp"
#include "GL.hpp"
#include "Planet.hpp"


const IGLTextureId* QuadShape::getTextureId(const RenderContext* rc) {
  if (_textureImage == NULL) {
    return NULL;
  }

  const IGLTextureId* texId = rc->getTexturesHandler()->getGLTextureId(_textureImage,
                                                                       GLFormat::rgba(),
                                                                       _textureFilename,
                                                                       false);

  if (_autoDeleteTextureImage) {
    rc->getFactory()->deleteImage(_textureImage);
    _textureImage = NULL;
  }

  if (texId == NULL) {
    rc->getLogger()->logError("Can't load file %s", _textureFilename.c_str());
  }

  return texId;
}

Mesh* QuadShape::createMesh(const RenderContext* rc) {

  const float halfWidth  = (float) _width / 2.0f;
  const float halfHeight = (float) _height / 2.0f;
  FloatBufferBuilderFromCartesian3D vertices(CenterStrategy::noCenter(), Vector3D::zero());
  vertices.add(-halfWidth, +halfHeight, 0);
  vertices.add(-halfWidth, -halfHeight, 0);
  vertices.add(+halfWidth, +halfHeight, 0);
  vertices.add(+halfWidth, -halfHeight, 0);

  IntBufferBuilder indices;
  indices.add(0);
  indices.add(1);
  indices.add(2);
  indices.add(3);


//  const Vector3D center = rc->getPlanet()->toCartesian( _position );
  const Vector3D center = Vector3D::zero();

  IndexedMesh *im = new IndexedMesh(GLPrimitive::triangleStrip(),
                                    true,
                                    center,
                                    vertices.create(),
                                    indices.create(),
                                    1);

  const IGLTextureId* texId = getTextureId(rc);
  if (texId == NULL) {
    return im;
  }

  FloatBufferBuilderFromCartesian2D texCoords;
  texCoords.add(0, 0);
  texCoords.add(0, 1);
  texCoords.add(1, 0);
  texCoords.add(1, 1);

  TextureMapping* texMap = new SimpleTextureMapping(texId,
                                                    texCoords.create(),
                                                    true);

  return new TexturedMesh(im, true, texMap, true, true);
}

Mesh* QuadShape::getMesh(const RenderContext* rc) {
  if (_mesh == NULL) {
    _mesh = createMesh(rc);
  }
  return _mesh;
}

QuadShape::~QuadShape() {
  delete _mesh;
}



void QuadShape::render(const RenderContext* rc) {
  int __diego_at_work;
  Mesh* mesh = getMesh(rc);
  if (mesh != NULL) {
    GL* gl = rc->getGL();

    gl->pushMatrix();

    const Planet* planet = rc->getPlanet();


    const Vector3D cartesianPosition = planet->toCartesian( _position );
    MutableMatrix44D translationMatrix = MutableMatrix44D::createTranslationMatrix(cartesianPosition);
    gl->multMatrixf(translationMatrix);


    MutableMatrix44D rotationMatrix = MutableMatrix44D::createRotationMatrixFromNormal( planet->geodeticSurfaceNormal(_position) );
    gl->multMatrixf(rotationMatrix);


    mesh->render(rc);

    gl->popMatrix();
  }
}
