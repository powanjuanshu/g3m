package org.glob3.mobile.generated; 
//
//  FlatPlanet.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 10/07/13.
//
//

//
//  FlatPlanet.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 10/07/13.
//
//




public class FlatPlanet extends Planet
{
  private final Vector2D _size ;
  private final Vector3D _radii ;

  private MutableVector3D _origin = new MutableVector3D();
  private MutableVector3D _initialPoint = new MutableVector3D();
  private double _dragHeight;
  private MutableVector3D _lastFinalPoint = new MutableVector3D();
  private boolean _validSingleDrag;
  private MutableVector3D _lastDirection = new MutableVector3D();

  private MutableVector3D _centerRay = new MutableVector3D();
  private MutableVector3D _initialPoint0 = new MutableVector3D();
  private double _dragHeight0;
  private MutableVector3D _initialPoint1 = new MutableVector3D();
  private double _dragHeight1;
  private double _distanceBetweenInitialPoints;
  private MutableVector3D _centerPoint = new MutableVector3D();

  private double _correctionT2;
  private MutableVector3D _correctedCenterPoint = new MutableVector3D();

  private double _lastDoubleDragAngle;


  public FlatPlanet(Vector2D size)
  {
     _size = new Vector2D(size);
     _radii = new Vector3D(size._x, size._y, 0);
  }

  public void dispose()
  {

  }

  public final Vector3D getRadii()
  {
    return _radii;
  }

  public final Vector3D centricSurfaceNormal(Vector3D position)
  {
    return new Vector3D(0, 0, 1);
  }

  public final Vector3D geodeticSurfaceNormal(Vector3D position)
  {
    return new Vector3D(0, 0, 1);
  }

  public final Vector3D geodeticSurfaceNormal(MutableVector3D position)
  {
    return new Vector3D(0, 0, 1);
  }


  public final Vector3D geodeticSurfaceNormal(Angle latitude, Angle longitude)
  {
    return new Vector3D(0, 0, 1);
  }

  public final Vector3D geodeticSurfaceNormal(Geodetic3D geodetic)
  {
    return new Vector3D(0, 0, 1);
  }

  public final Vector3D geodeticSurfaceNormal(Geodetic2D geodetic)
  {
    return new Vector3D(0, 0, 1);
  }

  public final java.util.ArrayList<Double> intersectionsDistances(double originX, double originY, double originZ, double directionX, double directionY, double directionZ)
  {
    java.util.ArrayList<Double> intersections = new java.util.ArrayList<Double>();
  
    // compute intersection with plane
    if (directionZ == 0)
       return intersections;
    final double t = -originZ / directionZ;
    final double x = originX + t * directionX;
    final double halfWidth = 0.5 * _size._x;
    if (x < -halfWidth || x > halfWidth)
       return intersections;
    final double y = originY + t * directionY;
    final double halfHeight = 0.5 * _size._y;
    if (y < -halfHeight || y > halfHeight)
       return intersections;
    intersections.add(t);
    return intersections;
  }

  public final Vector3D toCartesian(Angle latitude, Angle longitude, double height)
  {
    return toCartesian(new Geodetic3D(latitude, longitude, height));
  }

  public final Vector3D toCartesian(Geodetic3D geodetic)
  {
    final double x = geodetic._longitude._degrees * _size._x / 360.0;
    final double y = geodetic._latitude._degrees * _size._y / 180.0;
    return new Vector3D(x, y, geodetic._height);
  }


  public final Vector3D toCartesian(Geodetic2D geodetic)
  {
    return toCartesian(geodetic._latitude, geodetic._longitude, 0.0);
  }

  public final Vector3D toCartesian(Geodetic2D geodetic, double height)
  {
    return toCartesian(geodetic._latitude, geodetic._longitude, height);
  }

  public final Geodetic2D toGeodetic2D(Vector3D position)
  {
    final double longitude = position._x * 360.0 / _size._x;
    final double latitude = position._y * 180.0 / _size._y;
    return (new Geodetic2D(Angle.fromDegrees(latitude), Angle.fromDegrees(longitude)));
  }

  public final Geodetic3D toGeodetic3D(Vector3D position)
  {
    return new Geodetic3D(toGeodetic2D(position), position._z);
  }

  public final Vector3D scaleToGeodeticSurface(Vector3D position)
  {
    return new Vector3D(position._x, position._y, 0);
  }

  public final Vector3D scaleToGeocentricSurface(Vector3D position)
  {
    return scaleToGeodeticSurface(position);
  }

  public final Geodetic2D getMidPoint (Geodetic2D P0, Geodetic2D P1)
  {
    return new Geodetic2D(P0._latitude.add(P1._latitude).times(0.5), P0._longitude.add(P1._longitude).times(0.5));
  }



  // compute distance from two points
  public final double computePreciseLatLonDistance(Geodetic2D g1, Geodetic2D g2)
  {
    return toCartesian(g1).sub(toCartesian(g2)).length();
  }


  // compute distance from two points
  public final double computeFastLatLonDistance(Geodetic2D g1, Geodetic2D g2)
  {
    return computePreciseLatLonDistance(g1, g2);
  }

  public final MutableMatrix44D createGeodeticTransformMatrix(Geodetic3D position)
  {
    return MutableMatrix44D.createTranslationMatrix(toCartesian(position));
  }

  public final boolean isFlat()
  {
     return true;
  }

  //void beginSingleDrag(const Vector3D& origin, const Vector3D& initialRay) const;
  public final void beginSingleDrag(Vector3D origin, Vector3D touchedPosition)
  {
    _origin = origin.asMutableVector3D();
    //_initialPoint = Plane::intersectionXYPlaneWithRay(origin, initialRay).asMutableVector3D();
    _initialPoint = touchedPosition.asMutableVector3D();
    _dragHeight = toGeodetic3D(touchedPosition)._height;
  
    //printf("INiTIAL POINT EN %f, %f, %f\n ", _initialPoint.x(), _initialPoint.y(), _initialPoint.z());
  
    _lastFinalPoint = _initialPoint;
    _validSingleDrag = false;
  }

  public final MutableMatrix44D singleDrag(Vector3D finalRay)
  {
    // test if initialPoint is valid
    if (_initialPoint.isNan())
       return MutableMatrix44D.invalid();
  
    // compute final point
    final Vector3D origin = _origin.asVector3D();
    MutableVector3D finalPoint = Plane.intersectionXYPlaneWithRay(origin, finalRay, _dragHeight).asMutableVector3D();
    if (finalPoint.isNan())
       return MutableMatrix44D.invalid();
  
    // save params for possible inertial animations
    _validSingleDrag = true;
    _lastDirection = _lastFinalPoint.sub(finalPoint);
    _lastFinalPoint = finalPoint;
  
    // return rotation matrix
    return MutableMatrix44D.createTranslationMatrix(_initialPoint.sub(finalPoint).asVector3D());
  }

  public final Effect createEffectFromLastSingleDrag()
  {
    if (!_validSingleDrag)
       return null;
    return new SingleTranslationEffect(_lastDirection.asVector3D());
  }

  public final void beginDoubleDrag(Vector3D origin, Vector3D centerRay, Vector3D centerPosition, Vector3D touchedPosition0, Vector3D touchedPosition1)
  {
    _origin = origin.asMutableVector3D();
    _centerRay = centerRay.asMutableVector3D();
    //_initialPoint0 = Plane::intersectionXYPlaneWithRay(origin, initialRay0).asMutableVector3D();
    //_initialPoint1 = Plane::intersectionXYPlaneWithRay(origin, initialRay1).asMutableVector3D();
    _initialPoint0 = touchedPosition0.asMutableVector3D();
    _dragHeight0 = toGeodetic3D(touchedPosition0)._height;
    _initialPoint1 = touchedPosition1.asMutableVector3D();
    _dragHeight1 = toGeodetic3D(touchedPosition1)._height;
    _distanceBetweenInitialPoints = _initialPoint0.sub(_initialPoint1).length();
    //_centerPoint = Plane::intersectionXYPlaneWithRay(origin, centerRay).asMutableVector3D();
    _centerPoint = centerPosition.asMutableVector3D();
    _lastDoubleDragAngle = 0;
  
    // middle point in 3D
    _initialPoint = _initialPoint0.add(_initialPoint1).times(0.5);
  }


  /*
  MutableMatrix44D FlatPlanet::doubleDrag(const Vector3D& finalRay0,
                                          const Vector3D& finalRay1,
                                          bool allowRotation) const
  {
    // test if initialPoints are valid
    if (_initialPoint0.isNan() || _initialPoint1.isNan())
      return MutableMatrix44D::invalid();
    
    // init params
    const IMathUtils* mu = IMathUtils::instance();
    MutableVector3D positionCamera = _origin;
    
    // compute distance to translate camera
    double d0 = _distanceBetweenInitialPoints;
    const Vector3D r1 = finalRay0;
    const Vector3D r2 = finalRay1;
    double zA = _dragHeight0;
    double zB = _dragHeight1;
    double zc = _origin.z();
    double uz = _centerRay.z();
    double Rx = r2._x / r2._z - r1._x / r1._z;
    double Ry = r2._y / r2._z - r1._y / r1._z;
    double Kx = zc*Rx + zA*r1._x/r1._z - zB*r2._x/r2._z;
    double Ky = zc*Ry + zA*r1._y/r1._z - zB*r2._y/r2._z;
    double a = uz*uz * (Rx*Rx + Ry*Ry);
    double b = 2 * uz * (Rx*Kx + Ry*Ky);
    double c = Kx*Kx + Ky*Ky + (zA-zB)*(zA-zB) - d0*d0;
    double root = b*b - 4*a*c;
    if (root<0) return MutableMatrix44D::invalid();
    double squareRoot = mu->sqrt(root);
    double t2 = (-b - squareRoot) / (2*a);
    
    // the first time, t2 must be corrected
    if (_firstDoubleDragMovement) {
      _firstDoubleDragMovement = false;
      _correctionT2 = t2;
      t2 = 0;
    } else {
      t2 -= _correctionT2;
    }
    
    // start to compound matrix
    MutableMatrix44D matrix = MutableMatrix44D::identity();
    positionCamera = _origin;
    MutableVector3D viewDirection = _centerRay;
    MutableVector3D ray0 = finalRay0.asMutableVector3D();
    MutableVector3D ray1 = finalRay1.asMutableVector3D();
    
    MutableVector3D delta = viewDirection.times(t2);
    MutableMatrix44D translation = MutableMatrix44D::createTranslationMatrix(delta.asVector3D());
    positionCamera = positionCamera.transformedBy(translation, 1.0);
    matrix = translation.multiply(matrix);
    
    // compute final point
    MutableVector3D finalDragPoint = Plane::intersectionXYPlaneWithRay(positionCamera.asVector3D(), finalRay0, _dragHeight0).asMutableVector3D();
    if (finalDragPoint.isNan()) return MutableMatrix44D::invalid();
    MutableMatrix44D translationDrag = MutableMatrix44D::createTranslationMatrix(_initialPoint0.sub(finalDragPoint).asVector3D());
    matrix = translationDrag.multiply(matrix);
    
    return matrix;
  }*/
  
  public final MutableMatrix44D doubleDrag_old(Vector3D finalRay0, Vector3D finalRay1, boolean allowRotation)
  {
    // test if initialPoints are valid
    if (_initialPoint0.isNan() || _initialPoint1.isNan())
      return MutableMatrix44D.invalid();
  
    // init params
    final IMathUtils mu = IMathUtils.instance();
    MutableVector3D positionCamera = _origin;
  
    // following math in http://serdis.dis.ulpgc.es/~atrujill/glob3m/IGO/DoubleDrag.pdf
    /*
     // compute distance to translate camera
     double d0 = _distanceBetweenInitialPoints;
     const Vector3D r1 = finalRay0;
     const Vector3D r2 = finalRay1;
     double k = ((r1._x/r1._z - r2._x/r2._z) * (r1._x/r1._z - r2._x/r2._z) +
     (r1._y/r1._z - r2._y/r2._z) * (r1._y/r1._z - r2._y/r2._z));
     double zc = _origin.z();
     double uz = _centerRay.z();
     double t2 = (d0 / mu->sqrt(k) - zc) / uz;
     */
  
    // compute distance to translate camera
    double d0 = _distanceBetweenInitialPoints;
    final Vector3D r1 = finalRay0;
    final Vector3D r2 = finalRay1;
    double zA = _dragHeight0;
    double zB = _dragHeight1;
    double zc = _origin.z();
    double uz = _centerRay.z();
    double Rx = r2._x / r2._z - r1._x / r1._z;
    double Ry = r2._y / r2._z - r1._y / r1._z;
    double Kx = zc *Rx + zA *r1._x/r1._z - zB *r2._x/r2._z;
    double Ky = zc *Ry + zA *r1._y/r1._z - zB *r2._y/r2._z;
    double a = uz *uz * (Rx *Rx + Ry *Ry);
    double b = 2 * uz * (Rx *Kx + Ry *Ky);
    double c = Kx *Kx + Ky *Ky + (zA-zB)*(zA-zB) - d0 *d0;
    double root = b *b - 4 *a *c;
    if (root<0)
       return MutableMatrix44D.invalid();
    double squareRoot = mu.sqrt(root);
    double t2 = (-b - squareRoot) / (2 *a);
  
    // start to compound matrix
    MutableMatrix44D matrix = MutableMatrix44D.identity();
    positionCamera = _origin;
    MutableVector3D viewDirection = _centerRay;
    MutableVector3D ray0 = finalRay0.asMutableVector3D();
    MutableVector3D ray1 = finalRay1.asMutableVector3D();
  
    // drag from initialPoint to centerPoint and move the camera forward
    {
      MutableVector3D delta = _initialPoint.sub((_centerPoint));
      delta = delta.add(viewDirection.times(t2));
      MutableMatrix44D translation = MutableMatrix44D.createTranslationMatrix(delta.asVector3D());
      positionCamera = positionCamera.transformedBy(translation, 1.0);
  //    matrix.copyValue(translation.multiply(matrix));
      matrix.copyValueOfMultiplication(translation, matrix);
    }
  
    // compute middle point in 3D
    Vector3D P0 = Plane.intersectionXYPlaneWithRay(positionCamera.asVector3D(), ray0.asVector3D(), _dragHeight0);
    Vector3D P1 = Plane.intersectionXYPlaneWithRay(positionCamera.asVector3D(), ray1.asVector3D(), _dragHeight1);
    Vector3D finalPoint = P0.add(P1).times(0.5);
  
    // compute the corrected center point
    if (t2 == 0)
    {
      MutableVector3D delta = _initialPoint.sub((_centerPoint));
      _correctedCenterPoint = finalPoint.asMutableVector3D().sub(delta);
    }
    Vector3D correctedCenterPoint = _correctedCenterPoint.asVector3D();
  
    // drag globe from centerPoint to finalPoint
    {
      MutableMatrix44D translation = MutableMatrix44D.createTranslationMatrix(correctedCenterPoint.sub(finalPoint));
      positionCamera = positionCamera.transformedBy(translation, 1.0);
  //    matrix.copyValue(translation.multiply(matrix));
      matrix.copyValueOfMultiplication(translation, matrix);
    }
  
    // camera rotation
    if (allowRotation)
    {
      Vector3D normal = geodeticSurfaceNormal(correctedCenterPoint);
      Vector3D v0 = _initialPoint0.asVector3D().sub(correctedCenterPoint).projectionInPlane(normal);
      Vector3D p0 = Plane.intersectionXYPlaneWithRay(positionCamera.asVector3D(), ray0.asVector3D(), _dragHeight0);
      Vector3D v1 = p0.sub(correctedCenterPoint).projectionInPlane(normal);
      double angle = v0.angleBetween(v1)._degrees;
      double sign = v1.cross(v0).dot(normal);
      if (sign<0)
         angle = -angle;
      MutableMatrix44D rotation = MutableMatrix44D.createGeneralRotationMatrix(Angle.fromDegrees(angle), normal, correctedCenterPoint);
      matrix.copyValueOfMultiplication(rotation, matrix); // = rotation.multiply(matrix);
      /*
  =======
      MutableMatrix44D rotation = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(angle), normal, centerPoint2);
  //    matrix.copyValue(rotation.multiply(matrix));
      matrix.copyValueOfMultiplication(rotation, matrix);
  >>>>>>> origin/purgatory
       */
  
    }
  
    return matrix;
  }

  /* ********************
   TODO ESTA FUNCION ES PARA ORDENARLA, DOCUMENTARLA EN CHULETA PARA WEB, EXPLICARLA A JM
   Y QUE LA PASE AL PAPER GESTOS DESPUÉS DEL VERANO.
   Y TODAVIA FALTARA PROBARLO EN PLANETAS ESFERICOS
   AQUI DEBE SER PRACTICAMENTE IGUAL EXCEPTO LA MATRIZ DEL PASO 2 PARA PONER EL ORIGEN DE COORDENADAS
   SOBRE EL DRAGGED INITIAL POINT, QUE AHORA SOLO ES TRASLACION, PERO LUEGO SERA UNA MATRIZ
   DE CAMBIO DE SISTEMA
   *****/
  /*MutableMatrix44D FlatPlanet::doubleDrag(const Vector3D& finalRay0,
                                          const Vector3D& finalRay1,
                                          bool allowRotation) const
  {
    // test if initialPoints are valid
    if (_initialPoint0.isNan() || _initialPoint1.isNan())
      return MutableMatrix44D::invalid();
    
    // init params
    const IMathUtils* mu = IMathUtils::instance();
    const Vector3D origin = _origin.asVector3D();
    MutableVector3D positionCamera = _origin;
    
    // compute final points
    Vector3D finalPoint0 = Plane::intersectionXYPlaneWithRay(origin, finalRay0, _dragHeight0);
    if (finalPoint0.isNan()) return MutableMatrix44D::invalid();
  
    // drag initial point 0 to final point 0
    MutableMatrix44D matrix = MutableMatrix44D::createTranslationMatrix(_initialPoint0.sub(finalPoint0));
    
    // transform points to set axis origin in initialPoint0
    // (en el mundo plano es solo una traslacion)
    // (en el esférico será un cambio de sistema de referencia: traslacion + rotacion, usando el sistema local normal en ese punto)
    {
      Vector3D draggedCameraPos = positionCamera.transformedBy(matrix, 1.0).asVector3D();
      Vector3D finalPoint1 = Plane::intersectionXYPlaneWithRay(draggedCameraPos, finalRay1.transformedBy(matrix,0), _dragHeight1);
      MutableMatrix44D traslation = MutableMatrix44D::createTranslationMatrix(_initialPoint0.times(-1).asVector3D());
      Vector3D transformedInitialPoint1 = _initialPoint1.transformedBy(traslation, 1.0).asVector3D();
      Vector3D transformedFinalPoint1 = finalPoint1.transformedBy(traslation, 1.0);
      Vector3D transformedCameraPos = draggedCameraPos.transformedBy(traslation, 1.0);
      Vector3D v0 = transformedFinalPoint1.sub(transformedCameraPos);
      Vector3D v1 = transformedCameraPos.times(-1);
      Vector3D planeNormal = v0.cross(v1).normalized();
      double a = planeNormal._x;
      double b = planeNormal._y;
      double c = planeNormal._z;
      double xb = transformedInitialPoint1._x;
      double yb = transformedInitialPoint1._y;
      double zb = transformedInitialPoint1._z;
      double A = a*xb + b*yb;
      double B = b*xb - a*yb;
      double C = c*zb;
      double ap = A*A + B*B;
      double bp = 2*B*C;
      double cp = C*C - A*A;
      double root = bp*bp - 4*ap*cp;
      if (root<0) return MutableMatrix44D::invalid();
      double squareRoot = mu->sqrt(root);
      double sinTita1 = (-bp + squareRoot) / (2*ap);
      double sinTita2 = (-bp - squareRoot) / (2*ap);
      double cosTita1 = - (C + B*sinTita1) / A;
      double cosTita2 = - (C + B*sinTita2) / A;
      double angulo1 = atan2(sinTita1, cosTita1) / 3.14159 * 180;
      double angulo2 = atan2(sinTita2, cosTita2) / 3.14159 * 180;
      double dif1 = mu->abs(angulo1-_lastDoubleDragAngle);
      if (dif1 > 180) dif1 = 360 - dif1;
      double dif2 = mu->abs(angulo2-_lastDoubleDragAngle);
      if (dif2 > 180) dif2 = 360 - dif2;
      _lastDoubleDragAngle = (dif1 < dif2)? angulo1 : angulo2;
      Vector3D normal0 = geodeticSurfaceNormal(_initialPoint0);
      MutableMatrix44D rotation = MutableMatrix44D::createGeneralRotationMatrix(Angle::fromDegrees(-_lastDoubleDragAngle),
                                                                                normal0, _initialPoint0.asVector3D());
      matrix = rotation.multiply(matrix);
    }
    
    // zoom camera (see chuleta en pdf)
    // ahora mismo lo que se hace es buscar cuánto acercar para que el angulo de las dos parejas de vectores
    // sea el mismo
    {
      Vector3D P0   = positionCamera.transformedBy(matrix, 1.0).asVector3D();
      Vector3D B    = _initialPoint1.asVector3D();
      Vector3D B0   = B.sub(P0);
      Vector3D Ra   = finalRay0.transformedBy(matrix, 0.0).normalized();
      Vector3D Rb   = finalRay1.transformedBy(matrix, 0.0).normalized();
      double b      = -2 * (B0.dot(Ra));
      double c      = B0.squaredLength();
      double k      = Ra.dot(B0);
      double RaRb2  = Ra.dot(Rb) * Ra.dot(Rb);
      double at     = RaRb2 - 1;
      double bt     = b*RaRb2 + 2*k;
      double ct     = c*RaRb2 - k*k;
      double root   = bt*bt - 4*at*ct;
      if (root<0) return MutableMatrix44D::invalid();
      double squareRoot = mu->sqrt(root);
      double t = (-bt + squareRoot) / (2*at);
      MutableMatrix44D zoom = MutableMatrix44D::createTranslationMatrix(Ra.times(t));
      matrix = zoom.multiply(matrix);
    }
    
    return matrix;
  }
  */
  
  //JM VERSION: JUST TO CLARIFY A FEW THINGS
  public final MutableMatrix44D doubleDrag(Vector3D finalRay0, Vector3D finalRay1, boolean allowRotation)
  {
    // test if initialPoints are valid
    if (_initialPoint0.isNan() || _initialPoint1.isNan())
      return MutableMatrix44D.invalid();
  
    // init params
    final IMathUtils mu = IMathUtils.instance();
    final Vector3D origin = _origin.asVector3D();
    MutableVector3D positionCamera = _origin;
  
    // compute final points
    Vector3D finalPoint0 = Plane.intersectionXYPlaneWithRay(origin, finalRay0, _dragHeight0); //A1
    if (finalPoint0.isNan())
       return MutableMatrix44D.invalid();
  
    // drag initial point 0 to final point 0
    MutableMatrix44D matrix = MutableMatrix44D.createTranslationMatrix(_initialPoint0.sub(finalPoint0));
  
    // transform points to set axis origin in initialPoint0
    // (en el mundo plano es solo una traslacion)
    // (en el esférico será un cambio de sistema de referencia: traslacion + rotacion, usando el sistema local normal en ese punto)
    {
  
      Vector3D draggedCameraPos = positionCamera.transformedBy(matrix, 1.0).asVector3D();
      Vector3D finalPoint1 = Plane.intersectionXYPlaneWithRay(draggedCameraPos, finalRay1.transformedBy(matrix, 0), _dragHeight1); //B1
  
      //Taking whole system to origin
      MutableMatrix44D traslation = MutableMatrix44D.createTranslationMatrix(_initialPoint0.times(-1).asVector3D());
      Vector3D transformedInitialPoint1 = _initialPoint1.transformedBy(traslation, 1.0).asVector3D();
      Vector3D transformedFinalPoint1 = finalPoint1.transformedBy(traslation, 1.0);
      Vector3D transformedCameraPos = draggedCameraPos.transformedBy(traslation, 1.0);
      Vector3D v0 = transformedFinalPoint1.sub(transformedCameraPos);
  
      //Angles to rotate transformedInitialPoint1 to adjust the plane that contains origin, TFP1 and TCP
      Vector3D planeNormal = transformedCameraPos.cross(v0).normalized();
      Plane plane = new Plane(planeNormal, v0);
      Vector2D angles = plane.rotationAngleAroundZAxisToFixPointInRadians(transformedInitialPoint1);
  
      //Selecting best angle to rotate (smallest)
      double angulo1 = angles._x;
      double angulo2 = angles._y;
      double dif1 = Angle.distanceBetweenAnglesInRadians(angulo1, _lastDoubleDragAngle);
      double dif2 = Angle.distanceBetweenAnglesInRadians(angulo2, _lastDoubleDragAngle);
      _lastDoubleDragAngle = (dif1 < dif2)? angulo1 : angulo2;
  
      //Creating rotating matrix
      Vector3D normal0 = geodeticSurfaceNormal(_initialPoint0);
      MutableMatrix44D rotation = MutableMatrix44D.createGeneralRotationMatrix(Angle.fromRadians(-_lastDoubleDragAngle), normal0, _initialPoint0.asVector3D());
      matrix.copyValueOfMultiplication(rotation, matrix); // = rotation.multiply(matrix);
  
    }
  
    // zoom camera (see chuleta en pdf)
    // ahora mismo lo que se hace es buscar cuánto acercar para que el angulo de las dos parejas de vectores
    // sea el mismo
    {
      Vector3D P0 = positionCamera.transformedBy(matrix, 1.0).asVector3D();
      Vector3D B = _initialPoint1.asVector3D();
      Vector3D B0 = B.sub(P0);
      Vector3D Ra = finalRay0.transformedBy(matrix, 0.0).normalized();
      Vector3D Rb = finalRay1.transformedBy(matrix, 0.0).normalized();
      double b = -2 * (B0.dot(Ra));
      double c = B0.squaredLength();
      double k = Ra.dot(B0);
      double RaRb2 = Ra.dot(Rb) * Ra.dot(Rb);
      double at = RaRb2 - 1;
      double bt = b *RaRb2 + 2 *k;
      double ct = c *RaRb2 - k *k;
  
      Vector2D sol = mu.solveSecondDegreeEquation(at, bt, ct);
      if (sol.isNan())
      {
        return MutableMatrix44D.invalid();
      }
      double t = sol._x;
  
      MutableMatrix44D zoom = MutableMatrix44D.createTranslationMatrix(Ra.times(t));
      matrix.copyValueOfMultiplication(zoom, matrix); // = zoom.multiply(matrix);
    }
  
    /*
    //Much more visual reasoning (does not work so well though) :S [Check with AGUSTIN]
    {
      Vector3D A0 = _initialPoint0.transformedBy(matrix, 1.0).asVector3D();
      Vector3D P0 = positionCamera.transformedBy(matrix, 1.0).asVector3D();
      Vector3D B0 = _initialPoint1.transformedBy(matrix, 1.0).asVector3D();
    
      Vector3D finalPoint1 = Plane::intersectionXYPlaneWithRay(positionCamera.asVector3D(), finalRay1, _dragHeight1);
      Vector3D B1 = finalPoint1.transformedBy(matrix, 1.0);
    
      Vector3D P0_A0 = P0.sub(A0);
      Vector3D P0_B0 = P0.sub(B0);
      Vector3D A0_B0 = A0.sub(B0);
      Vector3D A0_B1 = A0.sub(B1);
      Vector3D B1_A0 = B1.sub(A0);
  
      Angle alpha = finalRay0.angleBetween(finalRay1); // P0_A0.angleBetween(P0_B0);
      Angle tita = (P0_B0.times(-1)).angleBetween(B1_A0);
    
      double distP1_A0 = mu->sin(tita._radians) / mu->sin(alpha._radians) * A0_B1.length();
    
      double distToMove = P0_A0.length() - distP1_A0;
    
      MutableMatrix44D zoom = MutableMatrix44D::createTranslationMatrix(P0_A0.normalized().times(distToMove));
      matrix = zoom.multiply(matrix);
    }
     */
  
    return matrix;
  }

  public final Effect createDoubleTapEffect(Vector3D origin, Vector3D centerRay, Vector3D touchedPosition)
  {
    //const Vector3D initialPoint = Plane::intersectionXYPlaneWithRay(origin, tapRay);
    if (touchedPosition.isNan())
       return null;
    //const Vector3D centerPoint = Plane::intersectionXYPlaneWithRay(origin, centerRay);
    double dragHeight = toGeodetic3D(touchedPosition)._height;
    final Vector3D centerPoint = Plane.intersectionXYPlaneWithRay(origin, centerRay, dragHeight);
  
    /*
     =======
                                            const Vector3D& tapRay) const {
    const Vector3D initialPoint = Plane::intersectionXYPlaneWithRay(origin, tapRay);
    if (initialPoint.isNan()) return NULL;
    const Vector3D centerPoint = Plane::intersectionXYPlaneWithRay(origin, centerRay);
  >>>>>>> origin/purgatory
     */
  
    // create effect
    double distanceToGround = toGeodetic3D(origin)._height - dragHeight;
  
    //printf("\n-- double tap to height %.2f, desde mi altura=%.2f\n", dragHeight, toGeodetic3D(origin)._height);
  
    return new DoubleTapTranslationEffect(TimeInterval.fromSeconds(0.75), touchedPosition.sub(centerPoint), distanceToGround *0.6);
  }

  public final double distanceToHorizon(Vector3D position)
  {
    double xCorner = 0.5 * _size._x;
    if (position._x > 0)
       xCorner *= -1;
    double yCorner = 0.5 * _size._y;
    if (position._y > 0)
       yCorner *= -1;
    final Vector3D fartherCorner = new Vector3D(xCorner, yCorner, 0.0);
    return position.sub(fartherCorner).length();
  }

  public final MutableMatrix44D drag(Geodetic3D origin, Geodetic3D destination)
  {
    final Vector3D P0 = toCartesian(origin);
    final Vector3D P1 = toCartesian(destination);
    return MutableMatrix44D.createTranslationMatrix(P1.sub(P0));
  }

  public final Vector3D getNorth()
  {
    return Vector3D.upY();
  }

  public final void applyCameraConstrainers(Camera previousCamera, Camera nextCamera)
  {
  //  Vector3D pos = nextCamera->getCartesianPosition();
  //  Vector3D origin = _origin.asVector3D();
  //  double maxDist = _size.length() * 1.5;
  //
  //  if (pos.distanceTo(origin) > maxDist) {
  //    //    printf("TOO FAR %f\n", pos.distanceTo(origin) / maxDist);
  //    nextCamera->copyFrom(*previousCamera);
  //  }
  }

  public final Geodetic3D getDefaultCameraPosition(Sector rendereSector)
  {
    final Vector3D asw = toCartesian(rendereSector.getSW());
    final Vector3D ane = toCartesian(rendereSector.getNE());
    final double height = asw.sub(ane).length() * 1.9;

    return new Geodetic3D(rendereSector._center, height);
  }

}