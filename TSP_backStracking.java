package test;

import java.util.Random;

/*
1.邻接矩阵的第一行第一列及x、bestx的第一个元素都是做辅助作用的，为了让所有的编号能从1开始计数，因为这个算法设计非常精巧，与编号的关系很大
2.预设是以1为起点，2是第一轮排列的第一个选择，所以参数都是传入2，所以这个程序的==限制==很多如果旅行商换一个点开始，那么还要重新调整序号和邻接矩阵，将出发的那个点作为序号为1的点
 */

public class TSP_backStracking {
    private static int N = 20;  //城市个数
    private static final int B = 65535;  //用一个很大的数表示点不连通
    private static int[][] a = new int[N + 1][N + 1];
    private static int[] x = new int[N + 1];  //记录当前解，并初始化为单位排列
    private static int[] bestx = new int[N + 1];  //记录当前最优解
    private static int cc;  //动态记录当前费用
    private static int bestc = B;  //记录当前最优解对应的费用，如果有更好的就更新它
    private static int judgeCount = 0;

    public static void main(String[] args) {
        //初始化可行解x
        initialArray();

        //初始化图的邻接矩阵
        //  1.通过随机生成的邻接矩阵测试程序
        initialMatrix();
        //  2.通过给定的邻接矩阵求解
//        a = new int[][]{
//                {0, 0, 0, 0, 0, 0},
//                {0, 0, 3, 1, 5, 8},
//                {0, 3, 0, 6, 7, 9},
//                {0, 1, 5, 0, 4, 2},
//                {0, 5, 7, 4, 0, 3},
//                {0, 8, 9, 2, 3, 0},};

        //打印邻接矩阵
        printMatrix();

        //调用核心回溯算法
        System.out.println("==========================================================================================================");
        long start = System.currentTimeMillis();
        backStracking(2);
        long end = System.currentTimeMillis();
        System.out.println("总共耗时 = " + (end - start) + "毫秒");
        System.out.println("判断次数 = " + judgeCount);  //统计递归的次数，也即进行if判断的次数
    }

    //核心的回溯求解TSP方法
    public static void backStracking(int i) {
        judgeCount++;
        if (i == N) {  //从头到尾i都只是用来计数并最终用来判断程序是否已经遍历了所有点的
            if (a[x[N - 1]][x[N]] != B && a[x[N]][x[1]] != B && ((cc + a[x[N - 1]][x[N]] + a[x[N]][x[1]] < bestc) || bestc == 0)) {
                bestc = cc + a[x[N - 1]][x[N]] + a[x[N]][x[1]];
                printBest();  //将当前的临时x赋给最优解bestx并打印
            }
        } else {
            for (int j = i; j <= N; j++) {
                if (a[x[i - 1]][x[j]] != B && (cc + a[x[i - 1]][x[j]] < bestc || bestc == B)) {
                    //只要if里的条件a[x[i - 1]][x[j]]满足，那么就立即把当前解x中的也调整成x[i-1]==>x[j]的顺序，且下面cc加的a[x[i - 1]][x[i]]其实也变成a[x[i - 1]][x[j]]了
                    exchange(x, i, j);  //得到第一个可行解，并回溯之后，才会出现i != j的情况，交换才真正起了作用
                    cc += a[x[i - 1]][x[i]];  //经过交换之后这里的x[i]其实变成了x[j]，也与当前解x中的顺序保持了一致
                    backStracking(i + 1);
                    cc -= a[x[i - 1]][x[i]];
                    exchange(x, i, j);
                }
            }
        }
    }

    //初始化解数组x的方法
    public static void initialArray() {
        for (int i = 1; i <= N; i++) {
            x[i] = i;
        }
    }

    //生成随机邻接矩阵的方法
    public static void initialMatrix() {
        Random random = new Random();
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                if (i != j) {
                    a[i][j] = random.nextInt(50);
                } else {
                    a[i][j] = 0;
                }
            }
        }
    }

    //打印邻接矩阵的方法
    public static void printMatrix() {
        for (int i = 0; i <= N; i++) {
            for (int j = 0; j <= N; j++) {
                System.out.print(a[i][j] + "\t");
            }
            System.out.println();
        }
    }

    //打印当前最优解的方法
    public static void printBest() {
        for (int j = 1; j <= N; j++) {  //遍历当前解数组x，打印当前最优解
            bestx[j] = x[j];
            System.out.print(bestx[j] + "\t");
        }
        System.out.println("bestc = " + bestc);  //打印最小代价
    }

    //交换值的方法
    public static void exchange(int[] x, int i, int j) {
        int temp = x[i];
        x[i] = x[j];
        x[j] = temp;
    }
}
