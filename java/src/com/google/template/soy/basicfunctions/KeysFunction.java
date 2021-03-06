/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.template.soy.basicfunctions;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Singleton;
import com.google.template.soy.data.SoyMap;
import com.google.template.soy.data.SoyValue;
import com.google.template.soy.data.SoyValueHelper;
import com.google.template.soy.jssrc.restricted.JsExpr;
import com.google.template.soy.jssrc.restricted.SoyJsSrcFunction;
import com.google.template.soy.shared.restricted.SoyJavaFunction;
import com.google.template.soy.shared.restricted.SoyPureFunction;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

/**
 * Soy function that gets the keys in a map.
 *
 * <p> The keys are returned as a list with no guarantees on the order (may be different on each run
 * or for each backend).
 *
 * <p> This enables iteration over the keys in a map, e.g.
 *     {foreach $key in keys($myMap)} ... {/foreach}
 *
 */
@Singleton
@SoyPureFunction
class KeysFunction implements SoyJavaFunction, SoyJsSrcFunction {


  /** The SoyValueHelper instance to use internally. */
  private final SoyValueHelper valueHelper;


  @Inject
  KeysFunction(SoyValueHelper valueHelper) {
    this.valueHelper = valueHelper;
  }


  @Override public String getName() {
    return "keys";
  }


  @Override public Set<Integer> getValidArgsSizes() {
    return ImmutableSet.of(1);
  }


  @Override public SoyValue computeForJava(List<SoyValue> args) {
    SoyValue arg = args.get(0);

    if (! (arg instanceof SoyMap)) {
      throw new IllegalArgumentException("Argument to keys() function is not SoyMap.");
    }
    return valueHelper.newEasyListFromJavaIterable(((SoyMap) arg).getItemKeys());
  }


  @Override public JsExpr computeForJsSrc(List<JsExpr> args) {
    JsExpr arg = args.get(0);

    return new JsExpr("soy.$$getMapKeys(" + arg.getText() + ")", Integer.MAX_VALUE);
  }

}
