# SpaceXMVI

Sample project on which we build and test different architecture approaches. Most interesting part is being inside **`base`** package. There is also a base class for `ViewRobots`.


## There are three different branches:
 - mviStandard - standard MVI architecture with `ViewModel` as presentation layer 
 - mviPresenter - standard MVI architecture with `Presenter`, not using `ViewModel`
 - mviWithActions - new approach(something like MVVMI), with `ViewModel` as presentation layer, including some new features for our architecture as for example `Action`, `Intent` 

**NOTE:** branch "mviWithActions" is the most ahead and "master" is not being used
