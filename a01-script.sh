#! /bin/bash

#1. Compilation
javac DecisionTrees.java

#2. Execution

# Non-verbal Output for Data Set 01, for 10 {L,K} pairs:
java DecisionTrees 30  50  DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv no >  log/DS1.log
echo '' >> log/DS1.log
java DecisionTrees 70  100 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv no >> log/DS1.log
echo '' >> log/DS1.log
java DecisionTrees 90  150 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv no >> log/DS1.log
echo '' >> log/DS1.log
java DecisionTrees 100 200 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv no >> log/DS1.log
echo '' >> log/DS1.log
java DecisionTrees 150 250 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv no >> log/DS1.log
echo '' >> log/DS1.log
java DecisionTrees 200 300 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv no >> log/DS1.log
echo '' >> log/DS1.log
java DecisionTrees 300 400 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv no >> log/DS1.log
echo '' >> log/DS1.log
java DecisionTrees 400 500 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv no >> log/DS1.log
echo '' >> log/DS1.log
java DecisionTrees 500 550 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv no >> log/DS1.log
echo '' >> log/DS1.log
java DecisionTrees 600 600 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv no >> log/DS1.log

# Verbal output for Data Set 01, for 10 {L,K} pairs:
java DecisionTrees 30  50  DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv yes > log-verbose/DS1-01.log
java DecisionTrees 70  100 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv yes > log-verbose/DS1-02.log
java DecisionTrees 90  150 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv yes > log-verbose/DS1-03.log
java DecisionTrees 100 200 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv yes > log-verbose/DS1-04.log
java DecisionTrees 150 250 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv yes > log-verbose/DS1-05.log
java DecisionTrees 200 300 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv yes > log-verbose/DS1-06.log
java DecisionTrees 300 400 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv yes > log-verbose/DS1-07.log
java DecisionTrees 400 500 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv yes > log-verbose/DS1-08.log
java DecisionTrees 500 550 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv yes > log-verbose/DS1-09.log
java DecisionTrees 600 600 DataSet-1/training_set.csv DataSet-1/validation_set.csv DataSet-1/test_set.csv yes > log-verbose/DS1-10.log
#-------------------------------------------------------------------------------------------------------------------------------------

# Non-verbal Output for Data Set 02, for 10 {L,K} pairs:
java DecisionTrees 30  50  DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv no >  log/DS2.log
echo '' >> log/DS2.log
java DecisionTrees 70  100 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv no >> log/DS2.log
echo '' >> log/DS2.log
java DecisionTrees 90  150 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv no >> log/DS2.log
echo '' >> log/DS2.log
java DecisionTrees 100 200 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv no >> log/DS2.log
echo '' >> log/DS2.log
java DecisionTrees 150 250 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv no >> log/DS2.log
echo '' >> log/DS2.log
java DecisionTrees 200 300 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv no >> log/DS2.log
echo '' >> log/DS2.log
java DecisionTrees 300 400 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv no >> log/DS2.log
echo '' >> log/DS2.log
java DecisionTrees 400 500 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv no >> log/DS2.log
echo '' >> log/DS2.log
java DecisionTrees 500 550 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv no >> log/DS2.log
echo '' >> log/DS2.log
java DecisionTrees 600 600 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv no >> log/DS2.log

# Verbal output for Data Set 02, for 10 {L,K} pairs:
java DecisionTrees 30  50  DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv yes > log-verbose/DS2-01.log
java DecisionTrees 70  100 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv yes > log-verbose/DS2-02.log
java DecisionTrees 90  150 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv yes > log-verbose/DS2-03.log
java DecisionTrees 100 200 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv yes > log-verbose/DS2-04.log
java DecisionTrees 150 250 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv yes > log-verbose/DS2-05.log
java DecisionTrees 200 300 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv yes > log-verbose/DS2-06.log
java DecisionTrees 300 400 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv yes > log-verbose/DS2-07.log
java DecisionTrees 400 500 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv yes > log-verbose/DS2-08.log
java DecisionTrees 500 550 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv yes > log-verbose/DS2-09.log
java DecisionTrees 600 600 DataSet-2/training_set.csv DataSet-2/validation_set.csv DataSet-2/test_set.csv yes > log-verbose/DS2-10.log
#-------------------------------------------------------------------------------------------------------------------------------------

exit 0