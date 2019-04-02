# SpaceXMVI

Sample project on which we build and test different architecture approaches. Most interesting part is being inside **`base`** package. There is also a base class for `ViewRobots`.


## There are three different branches:
 - mviPresenter renamed to mvi - standard MVI architecture with `Presenter`, not using `ViewModel`
 - mviWithActions renamed to mvvmi - new approach(something like MVVMI), with `ViewModel` as presentation layer, including some new features for our architecture as for example `Action`, `Intent` 
 
 **@Depreciated**
 - mviStandard - standard MVI architecture with `ViewModel` as presentation layer 

**UPDATE 26.03:** 
- mviPresenter - `Presenter` instance is being hold inside `retained fragment` which holds last `view state` in a bundle
