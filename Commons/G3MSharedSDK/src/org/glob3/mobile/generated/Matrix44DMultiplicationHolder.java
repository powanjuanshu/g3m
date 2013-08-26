package org.glob3.mobile.generated; 
//class Matrix44DMultiplicationHolder: public Matrix44DProvider{
//  const Matrix44D** _lastMatrixes;
//  const Matrix44DProvider** _providers;
//  int _nMatrix;
//  mutable Matrix44D* _modelview;
//public:
//  Matrix44DMultiplicationHolder(const Matrix44DProvider* providers[], int nMatrix):
//  _providers(new const Matrix44DProvider*[nMatrix]),
//  _nMatrix(nMatrix),
//  _modelview(NULL)
//  {
///#ifdef C_CODE
//    _lastMatrixes = new const Matrix44D*[nMatrix];
///#endif
///#ifdef JAVA_CODE
//    _lastMatrixes = new Matrix44D[nMatrix];
///#endif
//    for (int i = 0; i < _nMatrix; i++) {
//      _providers[i] = providers[i];
//      _lastMatrixes[i] = _providers[i]->getMatrix();
//      _providers[i]->_retain();
//      if (_lastMatrixes[i] == NULL){
//        ILogger::instance()->logError("Modelview multiplication failure");
//      }
//    }
//  }
//
//  ~Matrix44DMultiplicationHolder(){
///#ifdef C_CODE
//    delete[] _lastMatrixes;
//    delete[] _providers;
///#endif
//    if (_modelview != NULL){
//      _modelview->_release();
//    }
//    for (int i = 0; i < _nMatrix; i++) {
//      _providers[i]->_release();
//    }
//  }
//
//  const Matrix44D* getMatrix() const {
//
//    if (_modelview != NULL){
//      for (int i = 0; i < _nMatrix; i++) {
//        const Matrix44D* m = _providers[i]->getMatrix();
//        if (m == NULL){
//          ILogger::instance()->logError("Modelview multiplication failure");
//        }
//
//        if (_lastMatrixes[i] != m){
//
//          //If one matrix differs we have to raplace all matrixes on Holders and recalculate modelview
//          _modelview->_release();//NEW MODELVIEW NEEDED
//          _modelview = NULL;
//
//          for (int j = 0; j < _nMatrix; j++) {
//            _lastMatrixes[j] = _providers[j]->getMatrix();
//          }
//          break;
//        }
//      }
//    }
//
//
//    if (_modelview == NULL){
//      _modelview = new Matrix44D(*_lastMatrixes[0]);
//      for (int i = 1; i < _nMatrix; i++){
//        const Matrix44D* m2 = _lastMatrixes[i];
//        Matrix44D* m3 = _modelview->createMultiplication(*m2);
//        _modelview->_release();
//        _modelview = m3;
//      }
//    }
//    return _modelview;
//  }
//
//
//};

public class Matrix44DMultiplicationHolder extends Matrix44DProvider
{
  private final Matrix44D[] _matrix;
  private final Matrix44DProvider[] _providers;
  private int _nMatrix;
  private Matrix44D _modelview;

  private void pullMatrixes()
  {
    for (int j = 0; j < _nMatrix; j++)
    {
      final Matrix44D newMatrix = _providers[j].getMatrix();
  
      if (newMatrix != _matrix[j])
      {
        if (_matrix[j] != null)
        {
          _matrix[j]._release();
        }
  
        _matrix[j] = newMatrix;
        _matrix[j]._retain();
      }
    }
  }

  public Matrix44DMultiplicationHolder(Matrix44DProvider[] providers, int nMatrix)
  {
     _providers = new const Matrix44DProvider[nMatrix];
     _nMatrix = nMatrix;
     _modelview = null;
    _matrix = new Matrix44D[nMatrix];
    for (int i = 0; i < _nMatrix; i++)
    {
      _matrix[i] = null;
      _providers[i] = providers[i];
      _providers[i]._retain();
    }
  
    pullMatrixes();
  }

  public void dispose()
  {
  
    for (int j = 0; j < _nMatrix; j++)
    {
      if (_matrix[j] != null)
      {
        _matrix[j]._release();
      }
      _providers[j]._release();
    }
  
  
    if (_modelview != null)
    {
      _modelview._release();
    }
  }

  public final Matrix44D getMatrix()
  {
  
    if (_modelview != null)
    {
      for (int i = 0; i < _nMatrix; i++)
      {
        final Matrix44D m = _providers[i].getMatrix();
        if (m == null)
        {
          ILogger.instance().logError("Modelview multiplication failure");
        }
  
        if (_matrix[i] != m)
        {
          //If one matrix differs we have to raplace all matrixes on Holders and recalculate modelview
          _modelview._release(); //NEW MODELVIEW NEEDED
          _modelview = null;
  
          pullMatrixes();
          break;
        }
      }
    }
  
    if (_modelview == null)
    {
      _modelview = new Matrix44D(_matrix[0]);
      for (int i = 1; i < _nMatrix; i++)
      {
        final Matrix44D m2 = _matrix[i];
        Matrix44D m3 = _modelview.createMultiplication(m2);
        _modelview._release();
        _modelview = m3;
      }
    }
    return _modelview;
  }

}