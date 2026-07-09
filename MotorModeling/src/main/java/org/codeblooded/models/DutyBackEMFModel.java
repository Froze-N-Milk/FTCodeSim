package org.codeblooded.models;

import org.codeblooded.fit.MotorModel;

import java.io.IOException;

import static org.codeblooded.fit.ModelFitter.fitAndReportToCSV;

public class DutyBackEMFModel {
    public static void main(String[] args) throws IOException {
        String[] paths = (args.length > 0) ? args : new String[]{"MotorModeling/+1to.0001.csv"};
//        String[] paths = (args.length > 0) ? args : new String[]{"MotorModeling/+1to.0001.csv", "MotorModeling/+1to-.0001.csv", "MotorModeling/+1to-.2.csv"};

        int windowSize = 41;
        int polyDegree = 2;

        fitAndReportToCSV(paths, windowSize, polyDegree, "fit_forward.csv", MotorModel.fromString("a=Au-Bv*abs(d)-Cv-Dsgn(v)"));

    }
}
