package de.daslaboratorium.machinelearning.classifier;

import java.util.Map;

public class RestartData {
    private Map TotalFeatureCount;
    private Map TotalCategoryCategory;
    private Map FeatureCountPerCategory;

    public Map getTotalFeatureCount() {
        return this.TotalFeatureCount;
    }

    public void setTotalFeatureCount(Map TotalFeatureCount) {
        this.TotalFeatureCount = TotalFeatureCount;
    }

    public Map getTotalCategoryCategory() {
        return this.TotalCategoryCategory;
    }

    public void setTotalCategoryCategory(Map TotalCategoryCategory) {
        this.TotalCategoryCategory = TotalCategoryCategory;
    }

    public Map getFeatureCountPerCategory() {
        return this.FeatureCountPerCategory;
    }

    public void setFeatureCountPerCategory(Map FeatureCountPerCategory) {
        this.FeatureCountPerCategory = FeatureCountPerCategory;
    }

}
