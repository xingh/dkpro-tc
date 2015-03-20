If you have a dataset with potential information leakage, but you want to take advantage of the results-deviation-reducing capability of k-fold cross validation, you should customize your DKPro TC experiment in the following ways:

First, you need to determine which instances must occur in the same training dataset or the same test dataset.
You must define a comparator to determine whether two instances need to occur together.  For our opinion mining dataset, we define a folder-based comparator, which returns '0' if two instances have the same parent folder (which makes them occur together in a fold)
```
Comparator<String> myComparator = new Comparator<String>(){

	@Override
	public int compare(String filename1, String filename2)
	{
		File file1 = new File(filename1);
		File file2 = new File(filename2);
		String folder1 = file1.getParentFile().getName();
		String folder2 = file2.getParentFile().getName();
        				
		if(folder1.equals(folder2)){
			return 0;
		}
		return 1;
	}
};
```
When you run your DKPro TC experiment, change the BatchTaskCrossValidation line, such as for the TwentyNewsgroups demo:
```
BatchTaskCrossValidation batch = new BatchTaskCrossValidation("TwentyNewsgroupsCV",
                getPreprocessing(), NUM_FOLDS);
```
to:
```
BatchTaskCrossValidation batch = new BatchTaskCrossValidationWithFoldControl("TwentyNewsgroupsCV",
                getPreprocessing(), NUM_FOLDS, myComparator);
```
Then, DKPro TC will create as evenly-sized folds as possible, using the comparator you provided instead of randomly assigning instances to a fold.

NOTE: There is currently no weighted fold averaging in TC CV.  This means that it is the user's resonsibility to only use BatchTaskCrossValidationWithFoldControl with datasets that produce nearly equal-sized folds.  Otherwise, the results of a fold of 9 evaluation instances will be weighted equally against a fold of 300 evaluation instances, and will unfairly bias the final, averaged results.