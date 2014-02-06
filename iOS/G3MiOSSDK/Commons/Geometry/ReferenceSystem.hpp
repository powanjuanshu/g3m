//
//  ReferenceSystem.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 29/01/14.
//
//

#ifndef __G3MiOSSDK__ReferenceSystem__
#define __G3MiOSSDK__ReferenceSystem__

#include "Vector3D.hpp"

class Mesh;
class Color;
class TaitBryanAngles;
class Angle;

class ReferenceSystem{
public:
#ifdef C_CODE
  const Vector3D _x;
  const Vector3D _y;
  const Vector3D _z;
  const Vector3D _origin;
#endif
#ifdef JAVA_CODE
  final Vector3D _x;
	final Vector3D _y;
	final Vector3D _z;
	final Vector3D _origin;
#endif

  static ReferenceSystem global();

  ReferenceSystem(const Vector3D& x, const Vector3D& y, const Vector3D& z, const Vector3D& origin);

  //For camera
  ReferenceSystem(const Vector3D& viewDirection, const Vector3D& up, const Vector3D& origin);

  static bool areOrtogonal(const Vector3D& x, const Vector3D& y, const Vector3D& z);

  Mesh* createMesh(double size, const Color& xColor, const Color& yColor, const Color& zColor) const;

  ReferenceSystem applyTaitBryanAngles(const TaitBryanAngles& angles) const;

  ReferenceSystem applyTaitBryanAngles(const Angle& heading, const Angle& pitch, const Angle& roll) const;

  ReferenceSystem changeOrigin(const Vector3D& newOrigin) const;

  TaitBryanAngles getTaitBryanAngles(const ReferenceSystem& global) const;

  bool isEqualsTo(const ReferenceSystem& that) const;
  
};

#endif
