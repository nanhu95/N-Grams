package ngrams;

/**
 * Code taken from http://introcs.cs.princeton.edu/java/22library/Matrix.java
 * additionally functionality added by Nan Hu (includes normalizing vectors and scalar multiplication)
 * 
 */

public class Matrix {

    // return a random m-by-n matrix with values between 0 and 1
    public static double[][] random(int m, int n) {
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = Math.random();
        return C;
    }

    // return n-by-n identity matrix I
    public static double[][] identity(int n) {
        double[][] I = new double[n][n];
        for (int i = 0; i < n; i++)
            I[i][i] = 1;
        return I;
    }

    // return x^T y
    public static double dot(double[] x, double[] y) {
        if (x.length != y.length) throw new RuntimeException("Illegal vector dimensions.");
        double sum = 0.0;
        for (int i = 0; i < x.length; i++)
            sum += x[i] * y[i];
        return sum;
    }

    // return C = A^T
    public static double[][] transpose(double[][] A) {
        int m = A.length;
        int n = A[0].length;
        double[][] C = new double[n][m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[j][i] = A[i][j];
        return C;
    }

    // return C = A + B
    public static double[][] add(double[][] A, double[][] B) {
        int m = A.length;
        int n = A[0].length;
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] + B[i][j];
        return C;
    }

    // return C = A - B
    public static double[][] subtract(double[][] A, double[][] B) {
        int m = A.length;
        int n = A[0].length;
        double[][] C = new double[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        return C;
    }

    // return C = A * B
    public static double[][] multiply(double[][] A, double[][] B) {
        int mA = A.length;
        int nA = A[0].length;
        int mB = B.length;
        int nB = B[0].length;
        if (nA != mB) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] C = new double[mA][nB];
        for (int i = 0; i < mA; i++)
            for (int j = 0; j < nB; j++)
                for (int k = 0; k < nA; k++)
                    C[i][j] += A[i][k] * B[k][j];
        return C;
    }

    // matrix-vector multiplication (y = A * x)
    public static double[] multiply(double[][] A, double[] x) {
        int m = A.length;
        int n = A[0].length;
        if (x.length != n) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[m];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                y[i] += A[i][j] * x[j];
        return y;
    }


    // vector-matrix multiplication (y = x^T A)
    public static double[] multiply(double[] x, double[][] A) {
        int m = A.length;
        int n = A[0].length;
        if (x.length != m) throw new RuntimeException("Illegal matrix dimensions.");
        double[] y = new double[n];
        for (int j = 0; j < n; j++)
            for (int i = 0; i < m; i++)
                y[j] += A[i][j] * x[i];
        return y;
    }
    
    /**
     * method for scalar multiplication of vectors
     * @param alpha		a scalar
     * @param x			the vector
     * @return mult		the new vector
     * @author nanhu
     */
    public static double[] scalarMultiply(double[] x, double alpha) {
    	int length = x.length;
    	double[] mult = new double[length];
    	for (int i=0; i<length; i++) {
    		mult[i]=alpha*x[i];
    	}
    	return mult;
    }
    
    /**
     * method for scalar multiplication of matrices
     * @param alpha		a scalar
     * @param A			the matrix
     * @return mult		the new matrix
     * @author nanhu
     */
    public static double[][] scalarMultiply(double[][] A, double alpha) {
    	int rows = A.length;
    	int columns = A[0].length;
    	double[][] mult = new double[rows][columns];
    	for (int i=0; i<rows; i++) {
    		for (int j=0; j<columns; j++) {
    			mult[i][j]=alpha*A[i][j];
    		}
    	}
    	return mult;
    }
    
    /**
     * method for normalizing vectors
     * note we normalize in the sense that all elements sum to 1,
     * not in the sense that magnitude is 1
     * @param x				the vector
     * @return normalized	the normalized vector
     */
    public static double[] normalizeVector(double[] x) {
    	int length = x.length;
    	double[] normalized = new double[length];
    	// find the sum of all elements
    	double sum = 0.0;
    	for (int i=0; i<length; i++) {
    		sum+=x[i];
    	}
    	// divide each element by the norm
    	for (int j=0; j<length; j++) {
    		normalized[j]=x[j]/sum;
    	}
    	return normalized;
    }
    // removed testing code from original file; not necessary
}

