# Introduction #
Models trained with DKPro TC are not usable as independent components in pipelines, yet.
We thus can't take advantage of once created models.
In order to make trained TC models reusable, the necessary TC related components have to be deployed together with the model wrapped into a prediction-mode component.

I see essentially 3 stages of TC model reusability

# Stage 1 (TC reusable) #
Progress: 80%

Trained models can be used within a TC pipeline in a developing environment.
This is largely achieved.
Open points are:
  * User defined features have to be provided (loop-hole here: copy user-defined features into TC project that loads model - ugly code redundants, but works)
  * Implicit requirement that features that are part of the TC feature package are present in the classpath somewhere
  * No version awareness if feature XY is needed and it is found in the classpath/workspace it is taken (feature might have changed between TC versions ...,0.6, 0.7) this might lead to unintended side-effects / different results which are extremely hard to track from the user perspective

**REC** Let us assume we establish a best-practice as follows for TC experiments:

  * experiments consist of two Maven projects:
    1. **experiment code** - containing the experimental setup
    1. **runtime code** - containing any parts of the experimental setup that are required when using a generated model, e.g. custom feature extractors.
  * additionally, models are packaged in a Maven artifact
  * the model artifact has a dependency on the runtime artifact from above

So when somebody adds a TC model to a Maven project, the runtime code with the custom feature extractors is automatically added as well.

# Stage 2 (DKProCore / UIMA reusable) #
Progress: 0%

Models, including all machine learning adapter related preprocessing such as feature extraction and so on, are usable in a DKPro Core environment as modules.
This requires that everything TC-related is provided without the need to have the TC project in your workspace/developer environment
Probably main challenge here (assumed stage 1 is cleared):
  * dealing with absolute file paths
  * Knowing which parameter takes an external resources and which is just a constant
  * How to package stuff in an at least semi-automatical way

# Stage 3 (standalone application ) #
Progress: 0%

It is possible to create a standalone component that can be provided free of any developer environment to 3rd parties to release an actual product of something
e.g.
`java -jar myExperiment.jar 'param1' 'param2' `

This one might become trivial once stage 2 is cleared