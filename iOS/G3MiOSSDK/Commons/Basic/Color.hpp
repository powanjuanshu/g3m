//
//  Color.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 13/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_Color_hpp
#define G3MiOSSDK_Color_hpp

class Color {
private:
  const float _red;
  const float _green;
  const float _blue;
  const float _alpha;

  Color(const float red,
        const float green,
        const float blue,
        const float alpha) :
  _red(red),
  _green(green),
  _blue(blue),
  _alpha(alpha)
  {
    
  }
  
public:
  
  static Color fromRGBA(const float red,
                       const float green,
                       const float blue,
                       const float alpha) {
    return Color(red, green, blue, alpha);
  }
  
  static Color black() {
    return Color::fromRGBA(0, 0, 0, 1);
  }
  
  static Color white() {
    return Color::fromRGBA(1, 1, 1, 1);
  }
  
  
  float getRed() const {
    return _red;
  }
  
  float getGreen() const {
    return _green;
  }
  
  float getBlue() const {
    return _blue;
  }
  
  float getAlpha() const {
    return _alpha;
  }
  
};

#endif
