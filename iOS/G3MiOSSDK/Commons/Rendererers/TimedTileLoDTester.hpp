//
//  TimedTileLoDTester.hpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 4/12/15.
//
//

#ifndef TimedTileLoDTester_hpp
#define TimedTileLoDTester_hpp

#include "TileLoDTester.hpp"
#include "Tile.hpp"
#include "ITimer.hpp"
#include "IFactory.hpp"
#include "Context.hpp"

class TimedTileLoDTester: public TileLoDTester{
protected:
  
  class TimedTileLoDTesterData: public TileLoDTesterData{
  public:
    double _lastMeetsRenderCriteriaTimeInMS;
  };
  
  bool _meetsRenderCriteria(int testerLevel,
                            Tile* tile, const G3MRenderContext& rc) const{
    
    TimedTileLoDTesterData* data = (TimedTileLoDTesterData*) tile->getDataForLoDTester(testerLevel);
    if (data == NULL){
      data = new TimedTileLoDTesterData();
      data->_lastMeetsRenderCriteriaTimeInMS = rc.getFrameStartTimer()->nowInMilliseconds();
      tile->setDataForLoDTester(testerLevel, data);
      return false;
    }
    
    double lastTime = data->_lastMeetsRenderCriteriaTimeInMS;
    data->_lastMeetsRenderCriteriaTimeInMS = rc.getFrameStartTimer()->nowInMilliseconds(); //Update

    if ((rc.getFrameStartTimer()->nowInMilliseconds() - lastTime) > _maxElapsedTimeInMS){
      return true;
    } else{
      return false;
    }
    
  }
  
  bool _isVisible(int testerLevel,
                  Tile* tile, const G3MRenderContext& rc) const{
    return true;
  }
  
  double _maxElapsedTimeInMS;
  
public:
  
  TimedTileLoDTester(double maxElapsedTimeInMS,
                     TileLoDTester* nextTesterRightLoD,
                     TileLoDTester* nextTesterWrongLoD,
                     TileLoDTester* nextTesterVisible,
                     TileLoDTester* nextTesterNotVisible):
  _maxElapsedTimeInMS(maxElapsedTimeInMS),
  TileLoDTester(nextTesterRightLoD,
                nextTesterWrongLoD,
                nextTesterVisible,
                nextTesterNotVisible){}
  
  
  ~TimedTileLoDTester(){
  }
  
};

#endif /* TimedTileLoDTester_hpp */