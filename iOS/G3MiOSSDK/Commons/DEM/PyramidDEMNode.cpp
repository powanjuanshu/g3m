//
//  PyramidDEMNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/20/16.
//
//

#include "PyramidDEMNode.hpp"

#include "PyramidDEMProvider.hpp"
#include "DEMGrid.hpp"
#include "ErrorHandling.hpp"


PyramidDEMNode::PyramidDEMNode(const PyramidDEMNode* parent,
                               const size_t  childID,
                               const Sector& sector,
                               const int     z,
                               const int     x,
                               const int     y) :
_parent(parent),
_childID(childID),
_sector(sector),
_z(z), _x(x), _y(y),
_grid(NULL),
_stickyGrid(false),
_children(NULL)
{

}

PyramidDEMNode::~PyramidDEMNode() {
  if (_grid != NULL) {
    _grid->_release();
  }

  if (_children != NULL) {
    for (size_t i = 0; i < _children->size(); i++) {
      PyramidDEMNode* child = _children->at(i);
      delete child;
    }
    delete _children;
  }
}

std::vector<PyramidDEMNode*>* PyramidDEMNode::getChildren(PyramidDEMProvider* pyramidDEMProvider) {
  if (_children == NULL) {
    _children = new std::vector<PyramidDEMNode*>();
    for (size_t i = 0; i < 4; i++) {
      PyramidDEMNode* child = pyramidDEMProvider->createNode(this, i);
      _children->push_back(child);
    }
  }
  return _children;
}


bool PyramidDEMNode::insertGrid(int z,
                                int x,
                                int y,
                                DEMGrid* grid,
                                const bool sticky,
                                PyramidDEMProvider* pyramidDEMProvider) {
  if (z < _z) {
    THROW_EXCEPTION("Logic error!");
  }
  else if (z == _z) {
    if ((x == _x) && (y == _y)) {
      _grid = grid;
      _stickyGrid = sticky;
      return true;
    }
    return false;
  }

  std::vector<PyramidDEMNode*>* children = getChildren(pyramidDEMProvider);
  for (size_t i = 0; i < children->size(); i++) {
    PyramidDEMNode* child = children->at(i);
    if (child->insertGrid(z, x, y,
                          grid,
                          sticky,
                          pyramidDEMProvider)) {
      return true;
    }
  }
  return false;
}
