import java.io.*;
import java.util.StringTokenizer;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution5 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution5.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output5.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution5

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution5
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution5   // 0.5초 수행
       timeout 1 java Solution5     // 1초 수행
 */

/*
    Solution5 알고리즘의 시간 복잡도
    첫 번째루프인 counts[][n-1] 초기화 부분: Theta(n)
    두 번째 루프인 counts[][] 계산 부분: Theta(nH)
    마지막 루프인 counts[][0] 계산 부분 : Theta(H)
    따라서 이 알고리즘은 n과 H에 비례하는 시간 복잡도 Theta(nH)를 가진다.
 */

class Solution5 {
    static final int max_n = 1000;
    static final int max_H = 10000;

    static int n, H;
    static final int[] h = new int[max_n];
    static final int[] d = new int[max_n - 1];
    static int Answer;
    //    static int[][][] counts;
    static int[][] counts0 = new int[max_H + 1][max_n];
    static int[][] counts1 = new int[max_H + 1][max_n];

    public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input5.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output5.txt 로 정답을 출력합니다.
		 */
        BufferedReader br = new BufferedReader(new FileReader("input5.txt"));
        StringTokenizer stk;
        PrintWriter pw = new PrintWriter("output5.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
//        long time = System.nanoTime();
        for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 블록의 개수와 최대 높이를 각각 n, H에 읽어들입니다.
			   그리고 각 블록의 높이를 h[0], h[1], ... , h[n-1]에 읽어들입니다.
			   다음 각 블록에 파인 구멍의 깊이를 d[0], d[1], ... , d[n-2]에 읽어들입니다.
			 */
            stk = new StringTokenizer(br.readLine());
            n = Integer.parseInt(stk.nextToken());
            H = Integer.parseInt(stk.nextToken());
            stk = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                h[i] = Integer.parseInt(stk.nextToken());
            }
            stk = new StringTokenizer(br.readLine());
            for (int i = 0; i < n - 1; i++) {
                d[i] = Integer.parseInt(stk.nextToken());
            }


            /////////////////////////////////////////////////////////////////////////////////////////////
			/*
			   이 부분에서 여러분의 알고리즘이 수행됩니다.
			   문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
			 */
            /////////////////////////////////////////////////////////////////////////////////////////////

            // 높이제한 0..H 최상위 블록 0..n-1 사용여부 0..1
//            long time3 = System.nanoTime();
//            counts0 = new int[H + 1][n];
//            counts1 = new int[H + 1][n];

//            for (int i = 0; i <= H; i++) {
//                Arrays.fill(counts0[i], -1);
//                Arrays.fill(counts1[i], -1);
//            }
//            op = 0;
            final int nm1 = n - 1;
            final int nm2 = n - 2;
            for (int hh = 0; hh <= H; hh++) {
//                op++;
                counts0[hh][nm1] = h[nm1] <= hh ? 2 : 1;
                counts1[hh][nm1] = (h[nm1] - d[nm2]) <= hh ? 2 : 1;
            }
            for (int k = nm1; k > 1; k--) {
                final int km1 = k - 1;
                for (int hh = 0; hh <= H; hh++) {
                    final int counts0hhk = counts0[hh][k];
                    final int hhmhkm1 = hh - h[km1];
                    final int leftVal;
                    if (hhmhkm1 < 0) {
                        leftVal = 0;
                    } else {
                        leftVal = counts1[hhmhkm1][k];
                    }
                    // k-1 블록을 안 썼을 때, k이상 n 이하의 블록을 사용할 수 있여 hh 이하의 탑 쌓는 경우의 수
                    counts0[hh][km1] = (leftVal // k 블록을 사용하고 k+1이상 n 이하의 블록으로 hh-h(k) 이하의탑쌓는경우의수
                            + counts0hhk) % 1000000; // k블록을 사용하지 않고 k+1이상 n 이하의 블록으로 hh 이하의 탑쌓는 경우의수
                    final int internalVal;
                    if (km1 == 0) {
                        internalVal = 0;
                    } else {
                        internalVal = d[km1 - 1];
                    }
                    final int leftVal2;
                    final int hhmhkm1pinterVal = hhmhkm1 + internalVal;
                    if (hhmhkm1pinterVal < 0) {
                        leftVal2 = 0;
                    } else {
                        leftVal2 = counts1[hhmhkm1pinterVal][k];
                    }
                    // k-1 블록을 썼을 때 k 이상 n 이하의 블록을 사용할 수 있여 hh 이하의 탑 쌓는 경우의 수
                    counts1[hh][km1] = (leftVal2  // k블록을 사용
                            + counts0hhk) % 1000000; // k 블록을 사용하지 않음
                }
            }
            // k=1
            for (int hh = 0; hh <= H; hh++) {
                final int counts0hhkp1 = counts0[hh][1];
                final int hhmhkm1 = hh - h[0];
                int val;
                if (hhmhkm1 < 0)
                    val = counts0hhkp1;
                else
                    val = counts1[hhmhkm1][1] + counts0hhkp1;
                counts0[hh][0] = val % 1000000;
                counts1[hh][0] = counts0[hh][1]; //(counts(hhmhkm1, 2) + counts0hhkp1) % 1000000;
            }

            Answer = (counts0[H][0] % 1000000) - 1; // + counts[H][1][1]) % 1000000;
//            System.out.println(op);
            // output5.txt로 답안을 출력합니다.
            pw.print("#");
            pw.print(test_case);
            pw.print(" ");
            pw.println(Answer);
//            pw.println("#" + test_case + " " + Answer);
			/*
			   아래 코드를 수행하지 않으면 여러분의 프로그램이 제한 시간 초과로 강제 종료 되었을 때,
			   출력한 내용이 실제로 파일에 기록되지 않을 수 있습니다.
			   따라서 안전을 위해 반드시 flush() 를 수행하시기 바랍니다.
			 */
            pw.flush();
//            long time2 = System.nanoTime();
//            System.out.println(time2 - time);
//            System.out.println(time2 - time3);
//            time = time2;

        }

        br.close();
        pw.close();
    }

    // 접근되는 데이터 양을 줄이기 위해 memoized 함수 모양으로 짰었으나 시간이 더 걸린다.

//    static int op = 0;

    static int getCount0(int hh, int km1) {
//        System.out.println("getCount0(" + hh + "," + km1 + ")");
        if (counts0[hh][km1] == 0) {
//            op++;
            int k = km1 + 1;
            if (k == 1) {
                final int counts0hhkp1 = getCount0(hh, 1);
                final int hhmhkm1 = hh - h[0];
                int val;
                if (hhmhkm1 < 0)
                    val = counts0hhkp1;
                else
                    val = getCount1(hhmhkm1, 1) + counts0hhkp1;
                counts0[hh][0] = val % 1000000;
                counts1[hh][0] = counts0[hh][0]; //(counts(hhmhkm1, 2) + counts0hhkp1) % 1000000;
            } else {
                final int counts0hhk = getCount0(hh, k);
                final int hhmhkm1 = hh - h[km1];
                final int leftVal;
                if (hhmhkm1 < 0) {
                    leftVal = 0;
                } else {
                    leftVal = getCount1(hhmhkm1, k);
                }
                // k-1 블록을 안 썼을 때, k이상 n 이하의 블록을 사용할 수 있여 hh 이하의 탑 쌓는 경우의 수
                counts0[hh][km1] = (leftVal // k 블록을 사용하고 k+1이상 n 이하의 블록으로 hh-h(k) 이하의탑쌓는경우의수
                        + counts0hhk) % 1000000; // k블록을 사용하지 않고 k+1이상 n 이하의 블록으로 hh 이하의 탑쌓는 경우의수
            }
        }
        return counts0[hh][km1];
    }

    static int getCount1(int hh, int km1) {
//        System.out.println("getCount1(" + hh + "," + km1 + ")");
        if (counts1[hh][km1] == 0) {
//            op++;
            int k = km1 + 1;
            if (k == 1) {
                final int counts0hhkp1 = getCount0(hh, 1);
                final int hhmhkm1 = hh - h[0];
                int val;
                if (hhmhkm1 < 0)
                    val = counts0hhkp1;
                else
                    val = getCount1(hhmhkm1, 1) + counts0hhkp1;
                counts0[hh][0] = val % 1000000;
                counts1[hh][0] = counts0[hh][1]; //(counts(hhmhkm1, 2) + counts0hhkp1) % 1000000;
            } else {
                final int counts0hhk = getCount0(hh, k);
                final int hhmhkm1 = hh - h[km1];
                final int internalVal;
                if (km1 == 0) {
                    internalVal = 0;
                } else {
                    internalVal = d[km1 - 1];
                }
                final int leftVal2;
                final int hhmhkm1pinterVal = hhmhkm1 + internalVal;
                if (hhmhkm1pinterVal < 0) {
                    leftVal2 = 0;
                } else {
                    leftVal2 = getCount1(hhmhkm1pinterVal, k);
                }
                // k-1 블록을 썼을 때 k 이상 n 이하의 블록을 사용할 수 있여 hh 이하의 탑 쌓는 경우의 수
                counts1[hh][km1] = (leftVal2  // k블록을 사용
                        + counts0hhk) % 1000000; // k 블록을 사용하지 않음
            }
        }
        return counts1[hh][km1];
    }

//    static int counts(int hh) {
//        if (hh < 0)
//            return 0;
//        return counts1[hh][2];
//    }

//    static int h(int index) {
//        return h[index - 1];
//    }

//    static int d(int index) {
//        return index == 0 ? 0 : d[index - 1];
//    }

    // 들어오는것: 블록번호
//    static int diff(int topi, int whati) {
//        if (topi == 0)
//            return h[whati - 1];
//        if (whati - topi == 1) {
//            return h[whati - 1] - d[topi - 1];
//        }
//        return h[whati - 1];
//    }

    // 블록의 개수가 n
    // 블록은 0...n-1
    //
    // 최상층에 i가  쌓여 있고leftH  높이있을 때
//    static long count(int i, int leftH) {
//        if (i == n - 1) {
//            return 0;
//        }
//        if (i == n - 2) { // 블록이 하나만 남았다.
//            if (h[n - 1] <= leftH) {
//                return 1;
//            } else {
//                return 0;
//            }
//        }
//
//        long result = 0;
//        int j1 = i + 1; // 새로쌓으려 하는 블록의 인덱스. 바로 다음블록
//        int diffH = -d[i] + h[j1];
//        result = count(j1, leftH - diffH); // 바로 다음 블록을 테스트
//        // 바로 다음 블록이 아닌 것들을 테스트
//        for (int j2 = i + 2; j2 < n; j2++) {
//            result += count(j2, leftH - h[j2]);
//        }
//        return result;
//    }
}

