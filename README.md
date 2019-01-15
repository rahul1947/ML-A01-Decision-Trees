## ML-A01: Decision Trees
Implementation of Decision Tree using the two heuristics for Assignment 01 of the course CS6375: Machine Learning.

- [Rahul Nalawade](https://github.com/rahul1947) 

#### Problem: 
[Assignment 01](https://github.com/rahul1947/ML-A01-Decision-Trees/blob/master/Assignment%201.pdf)

#### Solution: 
[DecisionTrees.java](https://github.com/rahul1947/ML-A01-Decision-Trees/blob/master/DecisionTrees.java), 
[Report](https://github.com/rahul1947/ML-A01-Decision-Trees/blob/master/rsn170330_A01.pdf)

### How to Run:

1. Compile: 
```
$javac DecisionTrees.java
```

2. Execute: 
```
$java DecisionTrees L K training_set.csv validation_set.csv test_set.csv V
```

**L:** positive integer (used in post pruning algorithm)

**K:** positive integer (used in post pruning algorithm)

**V:** 'yes' or 'no' to print the tree on the command line

NOTE: 
- 10 {L, K} pair-values that are chosen:
{30 , 50}, {70 , 100}, {90 , 150}, {100, 200}, {150, 250}, {200, 300}, {300, 400}, {400, 500}, {500, 550}, {600, 600}.
for both DataSet-1 and DataSet-2

- Refer [a01-script.sh](https://github.com/rahul1947/ML-A01-Decision-Trees/blob/master/a01-script.sh) for more execution guidelines. The results are stored in [log/](https://github.com/rahul1947/ML-A01-Decision-Trees/tree/master/log) and [log-verbose/](https://github.com/rahul1947/ML-A01-Decision-Trees/tree/master/log-verbose) directories. 

