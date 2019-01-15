package a01; // comment to execute

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * CS6375: Machine Learning 
 * Assignment 01: Inducing Decision Trees
 * 
 * @author Rahul Nalawade
 * 
 * 2018-Feb-01
 */

// TreeNode is used to store Data while creating Decision Trees.
class TreeNode {
	int label = -1;
	boolean isLeaf = false;
	int targetAttribute = -1;

	TreeNode parent;
	TreeNode left;
	TreeNode right;

	int leftIndices[];
	int rightIndices[];
}

/**
 * DecisionTrees: responsible for creating Decision Trees, 
 * pruning and printing them.
 */
public class DecisionTrees {
	private static int count = 0;
	private static double[][] accDT = new double[4][2];
	private static int L = 30, K = 50;
	
	public static void main(String[] args) {
		if (args.length != 6) {
			System.out.println("There must be 6 command line arguments: ");
			System.out.println(
					"$java DecisionTrees L(int) K(int) trainingDataFile validationDataFile testDataFile printTree?(yes/no)");
			return;
		}

		L = Integer.parseInt(args[0]);
		K = Integer.parseInt(args[1]);

		int[] featuresAndLength = findFeaturesAndLength(args[2]);
		// featuresAndLength[0] is F+1 = No of features/ columns,
		// and featuresAndLength[1] is R+1 = No of Rows/ Data-points

		int[][] values = new int[featuresAndLength[1]][featuresAndLength[0]];

		int h;
		String[] featureNames = new String[featuresAndLength[0]];

		// root[0] & prunedTree[0] are for Decision Tree with Variance Impurity, for
		// Original and Pruned Tree respectively
		// root[1] & prunedTree[1] are for Decision Tree with Information Gain, for
		// Original and Pruned Tree respectively
		TreeNode root[] = new TreeNode[2];
		TreeNode prunedTree[] = new TreeNode[2];
		
		// for two different heuristics
		for (h = 1; h >= 0; h--) {

			// isDone[] for each feature
			int[] isDone = new int[featuresAndLength[0]];
			int[] indexList = new int[values.length];
			loadActualValues(args[2], values, featureNames, isDone, indexList, featuresAndLength[0]);

			root[h] = constructDecisionTree(null, values, isDone, featuresAndLength[0] - 1, indexList, null, h);
			
			prunedTree[h] = postPruneAlgorithm(args[3], L, K, root[h], values, featuresAndLength[0] - 1, h);
			
			accDT[2][h] = calculateAccuracyOverTestData(args[4], root[h]);
			accDT[3][h] = calculateAccuracyOverTestData(args[4], prunedTree[h]);
			
			if (args[5].equalsIgnoreCase("yes")) {
				printVerbose(h, root, prunedTree, featureNames);
			}
		}
		printResult();
	}
	
	/**
	 * Prints the output with accuracies on both the heuristics, for each -
	 * 1. The Original Tree
	 * 2. The Pruned Tree
	 * 3. Testing data for Decision Tree
	 * 4. Testing data for Pruned Tree
	 */
	private static void printResult() {
		String lk = Integer.toString(L) + " " + Integer.toString(K);
		
		System.out.format("%80s", "+------------------------------------------------------------------------------+\n");
		System.out.format("%-32s%-7s%-40s%-1s", "| Accuracy of Decision Tree (%) ", " L K =", lk, "|\n");
		System.out.format("%80s", "|------------------------------------------------------------------------------|\n");
		System.out.format("%-35s%-22s%-22s%-1s", "| Heuristics ", "| Information Gain ", "| Variance Impurity ", "|\n");
		System.out.format("%80s", "|----------------------------------|---------------------|---------------------|\n");
		System.out.format("%-35s%-22s%-22s%-1s", "| The Original Tree",              "| " + accDT[0][1], "| " + accDT[0][0], "|\n");
		System.out.format("%-35s%-22s%-22s%-1s", "| The Pruned Tree",                "| " + accDT[1][1], "| " + accDT[1][0], "|\n");
		System.out.format("%-35s%-22s%-22s%-1s", "| Testing data for Decision Tree", "| " + accDT[2][1], "| " + accDT[2][0], "|\n");
		System.out.format("%-35s%-22s%-22s%-1s", "| Testing data for Pruned Tree ",  "| " + accDT[3][1], "| " + accDT[3][0], "|\n");
		System.out.format("%80s", "+------------------------------------------------------------------------------+\n");
	}
	
	/**
	 * Print the result with trees when verbose is asked.
	 * @param h the heuristic index
	 * @param root the root array of the Tree 
	 * @param prunedTree the root array of Pruned Tree
	 * @param featureNames array of String with feature names
	 */
	private static void printVerbose(int h, TreeNode[] root, TreeNode[] prunedTree, String[] featureNames) {
		// System.out.println("Before Pruning: ");
		System.out.format("%50s", "+------------------------------------------------+\n");
		if (h == 0) {
			System.out.format("%-7s%-41s%2s", "| ", " Before Pruning (Variance Impurity) ", " |\n");
		} else {
			System.out.format("%-7s%-41s%2s", "| ", " Before Pruning (Information Gain) ", " |\n");
		}
		System.out.format("%50s", "+------------------------------------------------+\n");
		printTree(root[h], 0, featureNames);
		System.out.println();
		
		System.out.format("%50s", "+------------------------------------------------+\n");
		if (h == 0) {
			System.out.format("%-7s%-41s%2s", "| ", " After Pruning (Variance Impurity) ", " |\n");
		} else {
			System.out.format("%-7s%-41s%2s", "| ", " After Pruning (Information Gain) ", " |\n");
		}
		System.out.format("%50s", "+------------------------------------------------+\n");
		printTree(prunedTree[h], 0, featureNames);
		System.out.println();
	}

	// -----------------------------------------------------------------------------------------------
	
	/**
	 * Finds number of Features and length of the data file.
	 * @param csvFile the input data file in .CSV format
	 * @return a pair of integers: {F+1 (no of columns/ feature length), R+1 (no of rows/ data-points)}
	 */
	private static int[] findFeaturesAndLength(String csvFile) {
		BufferedReader bufferedReader = null;
		String line = "";
		String cvsSplitBy = ",";
		@SuppressWarnings("unused")
		int features = 0;
		int count = 0;
		int[] featuresAndLength = new int[2];
		
		try {

			bufferedReader = new BufferedReader(new FileReader(csvFile));
			while ((line = bufferedReader.readLine()) != null) {
				if (count == 0) {
					String[] stringSplit = line.split(cvsSplitBy);
					featuresAndLength[0] = stringSplit.length;
				}
				count++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		featuresAndLength[1] = count;
		return featuresAndLength;
	}

	/**
	 * Parse the Training data from CSV file provided and populate them into
	 * local data structures which can be used for tree construction.
	 * 
	 * @param pathValue the location of the file
	 * @param values 
	 * @param featureNames
	 * @param isDone
	 * @param indexList
	 * @param features
	 */
	private static void loadActualValues(String pathValue, int[][] values, String[] featureNames, int[] isDone,
			int[] indexList, int features) {
		
		String csvFile = pathValue;
		BufferedReader bufferedReader = null;
		String line = "";
		String cvsSplitBy = ",";
		for (int k = 0; k < features; k++) {
			isDone[k] = 0;
		}
		int k = 0;
		for (k = 0; k < values.length; k++) {
			indexList[k] = k;
		}
		try {

			bufferedReader = new BufferedReader(new FileReader(csvFile));
			int i = 0;
			while ((line = bufferedReader.readLine()) != null) {
				String[] lineParameters = line.split(cvsSplitBy);
				int j = 0;
				if (i == 0) {
					for (String lineParameter : lineParameters) {
						featureNames[j++] = lineParameter;
					}
				} else {
					for (String lineParameter : lineParameters) {
						values[i][j++] = Integer.parseInt(lineParameter);
					}
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Construct Decision Tree based on the Training data and returns the TreeNode
	 * to the root.
	 */
	public static TreeNode constructDecisionTree(TreeNode root, int[][] values, int[] isDone, int features,
			int[] indexList, TreeNode parent, int h) {
		if (root == null) {
			root = new TreeNode();
			if (indexList == null || indexList.length == 0) {
				root.label = findMaxValue(root, values, features);
				root.isLeaf = true;
				return root;
			}
			if (isAllArePositive(indexList, values, features)) {
				root.label = 1;
				root.isLeaf = true;
				return root;
			}
			if (isAllAreNegative(indexList, values, features)) {
				root.label = 0;
				root.isLeaf = true;
				return root;
			}
			if (features == 1 || allAttributesProcessed(isDone)) {
				root.label = findMaxValue(root, values, features);
				root.isLeaf = true;
				return root;
			}
		}
		root = findAttributeAndConstructNode(root, values, isDone, features, indexList, h);
		root.parent = parent;
		if (root.targetAttribute != -1)
			isDone[root.targetAttribute] = 1;
		int leftIsDone[] = new int[isDone.length];
		int rightIsDone[] = new int[isDone.length];
		for (int j = 0; j < isDone.length; j++) {
			leftIsDone[j] = isDone[j];
			rightIsDone[j] = isDone[j];

		}

		root.left = constructDecisionTree(root.left, values, leftIsDone, features, root.leftIndices, root, h);
		root.right = constructDecisionTree(root.right, values, rightIsDone, features, root.rightIndices, root, h);
		return root;
	}

	/**
	 * Post Pruning algorithm on the constructed Tree.
	 */
	public static TreeNode postPruneAlgorithm(String pathValue, int L, int K, TreeNode root, int[][] values,
			int features, int h) {
		
		TreeNode postPrunedTree = new TreeNode();
		int i = 0;
		postPrunedTree = root;

		double maxAccuracy = measureAccuracyOverValidationDataSet(pathValue, root);
		accDT[0][h] = maxAccuracy;
		
		for (i = 0; i < L; i++) {
			TreeNode newRoot = createCopy(root);
			Random randomNumbers = new Random();
			int M = 1 + randomNumbers.nextInt(K);
			for (int j = 1; j <= M; j++) {
				count = 0;
				int noOfNonLeafNodes = findNumberOfNonLeafNodes(newRoot);
				if (noOfNonLeafNodes == 0)
					break;
				TreeNode TreeNodeArray[] = new TreeNode[noOfNonLeafNodes];
				buildArray(newRoot, TreeNodeArray);
				int P = randomNumbers.nextInt(noOfNonLeafNodes);
				TreeNodeArray[P] = createLeafNodeWithMajorityValue(TreeNodeArray[P], values, features);

			}
			double accuracy = measureAccuracyOverValidationDataSet(pathValue, newRoot);

			if (accuracy > maxAccuracy) {
				postPrunedTree = newRoot;
				maxAccuracy = accuracy;
			}
		}
		accDT[1][h] = maxAccuracy;
		
		return postPrunedTree;
	}

	/**
	 * Calculates accuracy over Test Data.
	 */
	private static double calculateAccuracyOverTestData(String pathValue, TreeNode root) {
		double accuracy = 0;
		int[][] testingData = loadTestingData(pathValue);

		for (int i = 0; i < testingData.length; i++) {
			accuracy += isCorrectlyClassified(testingData[i], root);
		}
		return accuracy / testingData.length;

	}

	/**
	 * Prints the tree in the specified format if we pass 'yes' at the command line.
	 */
	private static void printTree(TreeNode root, int printLines, String[] featureNames) {
		int printLinesForThisLoop = printLines;
		if (root.isLeaf) {
			System.out.println(" " + root.label);
			return;
		}
		for (int i = 0; i < printLinesForThisLoop; i++) {
			System.out.print("| ");
		}
		if (root.left != null && root.left.isLeaf && root.targetAttribute != -1)
			System.out.print(featureNames[root.targetAttribute] + "= 0 :");
		else if (root.targetAttribute != -1)
			System.out.println(featureNames[root.targetAttribute] + "= 0 :");

		printLines++;
		printTree(root.left, printLines, featureNames);
		for (int i = 0; i < printLinesForThisLoop; i++) {
			System.out.print("| ");
		}
		if (root.right != null && root.right.isLeaf && root.targetAttribute != -1)
			System.out.print(featureNames[root.targetAttribute] + "= 1 :");
		else if (root.targetAttribute != -1)
			System.out.println(featureNames[root.targetAttribute] + "= 1 :");
		printTree(root.right, printLines, featureNames);
	}

	/**
	 * USELESS NOW, USING ABOVE. Prints the Tree rooted at TreeNode tree
	 */
	public static void printTree(TreeNode tree) {
		if (tree != null) {
			System.out.println("tree.targetAttribute " + tree.targetAttribute);
			System.out.println("tree.label " + tree.label);
			System.out.println("tree.isLeaf " + tree.isLeaf);
			if (tree.leftIndices != null) {
				System.out.println("tree.leftIndices ");
				for (int i : tree.leftIndices) {
					System.out.print(i + " ");
				}
			}
			if (tree.rightIndices != null) {
				System.out.println("\ntree.rightIndices ");
				for (int i : tree.rightIndices) {
					System.out.print(i + " ");
				}
			}
			System.out.println();
			printTree(tree.left);
			printTree(tree.right);
		}
	}

	/*------------------------------------- THE END -------------------------------------------------*/

	/**
	 * Returns max value of TargetAttribute among the examples available at the
	 * given splitting attribute.
	 */
	public static int findMaxValue(TreeNode root, int[][] values, int features) {
		int noOfOnes = 0;
		int noOfZeroes = 0;
		if (root.parent == null) {
			int i = 0;
			for (i = 0; i < values.length; i++) {
				if (values[i][features] == 1) {
					noOfOnes++;
				} else {
					noOfZeroes++;
				}
			}
		} else {
			for (int i : root.parent.leftIndices) {
				if (values[i][features] == 1) {
					noOfOnes++;
				} else {
					noOfZeroes++;
				}
			}

			for (int i : root.parent.rightIndices) {
				if (values[i][features] == 1) {
					noOfOnes++;
				} else {
					noOfZeroes++;
				}
			}
		}
		return noOfZeroes > noOfOnes ? 0 : 1;

	}

	/**
	 * Checks if all the examples have TargetAttribute as 1: returns true if 1,
	 * false otherwise.
	 */
	public static boolean isAllArePositive(int[] indexList, int[][] values, int features) {
		boolean isOne = true;
		for (int i : indexList) {
			if (values[i][features] == 0)
				isOne = false;
		}
		return isOne;
	}

	/**
	 * Checks if all the examples have TargetAttribute as 0: returns true if 0,
	 * false otherwise.
	 */
	public static boolean isAllAreNegative(int[] indexList, int[][] values, int features) {
		boolean isZero = true;
		for (int i : indexList) {
			if (values[i][features] == 1)
				isZero = false;
		}
		return isZero;
	}

	/**
	 * Checks if all the Attributes are processed?: true if yes, false otherwise.
	 */
	public static boolean allAttributesProcessed(int[] isDone) {
		boolean allDone = true;
		for (int i : isDone) {
			if (i == 0)
				allDone = false;
		}
		return allDone;
	}

	/**
	 * DO NOT TOUCH THIS! 
	 * Using Information Gain and Variance Impurity heuristics,
	 * this will find and split attributes at every level.
	 */
	private static TreeNode findAttributeAndConstructNode(TreeNode root, int[][] values, int[] isDone, int features,
			int[] indexList, int h) {

		int i = 0;
		@SuppressWarnings("unused")
		int j = 0;
		int k = 0;
		double maxheuristic = 0;
		int maxLeftIndex[] = null;
		int maxRightIndex[] = null;
		int maxIndex = -1;

		for (; i < features; i++) {

			if (isDone[i] == 0) {
				double negatives = 0;
				double positives = 0;
				double left = 0;
				double right = 0;
				double leftEntropy = 0, leftVarImpurity = 0, rightVarImpurity = 0;
				double rightEntropy = 0;
				int[] leftIndex = new int[values.length];
				int[] rightIndex = new int[values.length];
				double entropy = 0, varianceImpurity = 0;
				double rightPositives = 0;
				double heuristic = 0;
				double rightNegatives = 0, leftPositives = 0, leftNegatives = 0;

				for (k = 0; k < indexList.length; k++) {
					if (values[indexList[k]][features] == 1) {
						positives++;
					} else {
						negatives++;
					}
					if (values[indexList[k]][i] == 1) {
						rightIndex[(int) right++] = indexList[k];
						if (values[indexList[k]][features] == 1) {
							rightPositives++;
						} else {
							rightNegatives++;
						}

					} else {
						leftIndex[(int) left++] = indexList[k];
						if (values[indexList[k]][features] == 1) {
							leftPositives++;
						} else {
							leftNegatives++;
						}

					}

				}
				if (h == 1) {

					entropy = (-1 * logValue(positives / indexList.length) * ((positives / indexList.length)))
							+ (-1 * logValue(negatives / indexList.length) * (negatives / indexList.length));

					leftEntropy = (-1 * logValue(leftPositives / (leftPositives + leftNegatives))
							* (leftPositives / (leftPositives + leftNegatives)))
							+ (-1 * logValue(leftNegatives / (leftPositives + leftNegatives))
									* (leftNegatives / (leftPositives + leftNegatives)));

					rightEntropy = (-1 * logValue(rightPositives / (rightPositives + rightNegatives))
							* (rightPositives / (rightPositives + rightNegatives)))
							+ (-1 * logValue(rightNegatives / (rightPositives + rightNegatives))
									* (rightNegatives / (rightPositives + rightNegatives)));

					if (Double.compare(Double.NaN, entropy) == 0) {
						entropy = 0;
					}
					if (Double.compare(Double.NaN, leftEntropy) == 0) {
						leftEntropy = 0;
					}
					if (Double.compare(Double.NaN, rightEntropy) == 0) {
						rightEntropy = 0;
					}

					heuristic = entropy
							- ((left / (left + right) * leftEntropy) + (right / (left + right) * rightEntropy));
				} else if (h == 0) {

					varianceImpurity = (positives / indexList.length) * (negatives / indexList.length);

					leftVarImpurity = (leftPositives / (leftPositives + leftNegatives))
							* (leftNegatives / (leftPositives + leftNegatives));

					rightVarImpurity = (rightPositives / (rightPositives + rightNegatives))
							* (rightNegatives / (rightPositives + rightNegatives));

					if (Double.compare(Double.NaN, varianceImpurity) == 0) {
						varianceImpurity = 0;
					}
					if (Double.compare(Double.NaN, leftVarImpurity) == 0) {
						leftVarImpurity = 0;
					}
					if (Double.compare(Double.NaN, rightVarImpurity) == 0) {
						rightVarImpurity = 0;
					}

					heuristic = varianceImpurity
							- ((left / (left + right) * leftVarImpurity) + (right / (left + right) * rightVarImpurity));
				}

				if (heuristic >= maxheuristic) {
					maxheuristic = heuristic;
					maxIndex = i;

					int leftTempArray[] = new int[(int) left];
					for (int index = 0; index < left; index++) {
						leftTempArray[index] = leftIndex[index];
					}

					int rightTempArray[] = new int[(int) right];
					for (int index = 0; index < right; index++) {
						rightTempArray[index] = rightIndex[index];
					}

					maxLeftIndex = leftTempArray;
					maxRightIndex = rightTempArray;

				}
			}
		}
		root.targetAttribute = maxIndex;
		root.leftIndices = maxLeftIndex;
		root.rightIndices = maxRightIndex;
		return root;
	}

	/**
	 * Measures accuracy of the Constructed Tree over Validation Tree.
	 */
	private static double measureAccuracyOverValidationDataSet(String pathValue, TreeNode newRoot) {
		int[][] validationDataSet = constructValidationDataSet(pathValue);
		double count = 0;
		for (int i = 1; i < validationDataSet.length; i++) {
			count += isCorrectlyClassified(validationDataSet[i], newRoot);
		}
		return count / validationDataSet.length;
	}

	/**
	 * Creates a copy for the given tree and returns it.
	 */
	public static TreeNode createCopy(TreeNode root) {
		if (root == null)
			return root;

		TreeNode temp = new TreeNode();
		temp.label = root.label;
		temp.isLeaf = root.isLeaf;
		temp.leftIndices = root.leftIndices;
		temp.rightIndices = root.rightIndices;
		temp.targetAttribute = root.targetAttribute;
		temp.parent = root.parent;
		temp.left = createCopy(root.left); // cloning left child
		temp.right = createCopy(root.right); // cloning right child
		return temp;
	}

	/**
	 * Counts the number of non leaf TreeNodes and returns it.
	 */
	private static int findNumberOfNonLeafNodes(TreeNode root) {
		if (root == null || root.isLeaf)
			return 0;
		else
			return (1 + findNumberOfNonLeafNodes(root.left) + findNumberOfNonLeafNodes(root.right));
	}

	/**
	 * Builds the {index, TreeNodes at the index} map which is used in PostPruning
	 * Algorithm.
	 */
	private static void buildArray(TreeNode root, TreeNode[] TreeNodeArray) {
		if (root == null || root.isLeaf) {
			return;
		}
		TreeNodeArray[count++] = root;
		if (root.left != null) {
			buildArray(root.left, TreeNodeArray);
		}
		if (root.right != null) {
			buildArray(root.right, TreeNodeArray);
		}
	}

	/**
	 * Create and return the leaf TreeNode with label as majority value of the
	 * TargetAttribute among the examples at that TreeNode.
	 */
	private static TreeNode createLeafNodeWithMajorityValue(TreeNode TreeNode, int[][] values, int features) {
		TreeNode.isLeaf = true;
		TreeNode.label = findMaxValueAtGivenNode(TreeNode, values, features);
		TreeNode.left = null;
		TreeNode.right = null;
		return TreeNode;
	}

	/**
	 * Loads the test data into local data structures.
	 */
	private static int[][] loadTestingData(String pathValue) {
		int[] featuresAndLength = findFeaturesAndLength(pathValue);
		String csvFile = pathValue;
		int[][] validationDataSet = new int[featuresAndLength[1]][featuresAndLength[0]];
		BufferedReader bufferReader = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			bufferReader = new BufferedReader(new FileReader(csvFile));
			int i = 0;
			int count = 0;

			while ((line = bufferReader.readLine()) != null) {
				String[] lineParameters = line.split(cvsSplitBy);
				int j = 0;

				if (count == 0) {
					count++;
					continue;
				} else {
					for (String lineParameter : lineParameters) {
						validationDataSet[i][j++] = Integer.parseInt(lineParameter);
					}
				}

				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferReader != null) {
				try {
					bufferReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return validationDataSet;
	}

	/**
	 * Verifies if the given Example is correctly classified as per the constructed
	 * Tree.
	 */
	private static int isCorrectlyClassified(int[] setValues, TreeNode newRoot) {
		int index = newRoot.targetAttribute;
		int correctlyClassified = 0;
		TreeNode testingNode = newRoot;
		while (testingNode.label == -1) {
			if (setValues[index] == 1) {
				testingNode = testingNode.right;
			} else {
				testingNode = testingNode.left;
			}
			if (testingNode.label == 1 || testingNode.label == 0) {
				if (setValues[setValues.length - 1] == testingNode.label) {
					correctlyClassified = 1;
					break;
				} else {
					break;
				}
			}
			index = testingNode.targetAttribute;
		}
		return correctlyClassified;
	}

	/**
	 * finds log(base 2) (X) wrt log(base 10)(X)
	 */
	private static double logValue(double fraction) {
		return Math.log10(fraction) / Math.log10(2);
	}

	/**
	 * This method will construct and return the validation set from the file path
	 * specified.
	 */
	private static int[][] constructValidationDataSet(String pathValue) {
		int[] featuresAndLength = findFeaturesAndLength(pathValue);
		String csvFile = pathValue;
		int[][] validationDataSet = new int[featuresAndLength[1]][featuresAndLength[0]];
		BufferedReader bufferedReader = null;
		String line = "";
		String cvsSplitBy = ",";
		try {
			bufferedReader = new BufferedReader(new FileReader(csvFile));
			int i = 0;
			int count = 0;
			while ((line = bufferedReader.readLine()) != null) {
				String[] lineParameters = line.split(cvsSplitBy);
				int j = 0;
				if (count == 0) {
					count++;
					continue;
				} else {
					for (String lineParameter : lineParameters) {
						validationDataSet[i][j++] = Integer.parseInt(lineParameter);
					}
				}
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return validationDataSet;
	}

	/**
	 * Returns the maxValue of the TargetAttribute among the the examples present at
	 * the TreeNode of Decision Tree.
	 * 
	 * @param root the root of the Decision Tree
	 * @param values the training file
	 * @param features column no of a feature in values[][]
	 * @return
	 */
	private static int findMaxValueAtGivenNode(TreeNode root, int[][] values, int features) {
		int noOfOnes = 0;
		int noOfZeroes = 0;
		
		if (root.leftIndices != null) {
			
			for (int i : root.leftIndices) {
				if (values[i][features] == 1) {
					noOfOnes++;
				} else {
					noOfZeroes++;
				}
			}
		}

		if (root.rightIndices != null) {
			for (int i : root.rightIndices) {
				if (values[i][features] == 1) {
					noOfOnes++;
				} else {
					noOfZeroes++;
				}
			}
		}
		return noOfZeroes > noOfOnes ? 0 : 1;
	}
}
