import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution4 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution4.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output4.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution4

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution4
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution4   // 0.5초 수행
       timeout 1 java Solution4     // 1초 수행
 */

/*
    시간 복잡도 설명
    for문을 기준으로 생각해보면,
    초기화 Theta(k) (k는 상수)
    실제 구하는 부분 Theta(kn) (k는 상수)
    패턴의 가지수(상수)에 비례하고 열의 개수 n에 비례한다.
    따라서 이 알고리즘의 시간 복잡도는 Theta(n)이다.

 */

class Solution4 {
    static final int max_n = 100000;

    static int n;
    static int[][] A = new int[3][max_n];
    static int Answer;

    public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input4.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output4.txt 로 정답을 출력합니다.
		 */
        BufferedReader br = new BufferedReader(new FileReader("input4.txt"));
        StringTokenizer stk;
        PrintWriter pw = new PrintWriter("output4.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
        for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 놀이판의 열의 개수를 n에 읽어들입니다.
			   그리고 첫 번째 행에 쓰여진 n개의 숫자를 차례로 A[0][0], A[0][1], ... , A[0][n-1]에 읽어들입니다.
			   마찬가지로 두 번째 행에 쓰여진 n개의 숫자를 차례로 A[1][0], A[1][1], ... , A[1][n-1]에 읽어들이고,
			   세 번째 행에 쓰여진 n개의 숫자를 차례로 A[2][0], A[2][1], ... , A[2][n-1]에 읽어들입니다.
			 */
            stk = new StringTokenizer(br.readLine());
            n = Integer.parseInt(stk.nextToken());
            for (int i = 0; i < 3; i++) {
                stk = new StringTokenizer(br.readLine());
                for (int j = 0; j < n; j++) {
                    A[i][j] = Integer.parseInt(stk.nextToken());
                }
            }


            /////////////////////////////////////////////////////////////////////////////////////////////
			/*
			   이 부분에서 여러분의 알고리즘이 수행됩니다.
			   문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
			 */
            /////////////////////////////////////////////////////////////////////////////////////////////
            // 한 열에 놓이는 패턴의 가지수는 6개
            int[][] pebble = new int[n][patterns.length];
            // pebble[i][j]는 i번째 열에 패턴 j로 놓일 때의 최대 점수합
            // 0열 초기화
            for (int pattern = 0; pattern < patterns.length; pattern++) {
                pebble[0][pattern] = score(pattern, 0, A);
            }
            // 한 열씩 계산해 나간다.
            for (int i = 1; i < n; i++) {
                for (int pattern = 0; pattern < patterns.length; pattern++) {
                    int maxpeb = pebble[i - 1][compatibles[pattern][0]];
                    if (maxpeb < pebble[i - 1][compatibles[pattern][1]]) {
                        maxpeb = pebble[i - 1][compatibles[pattern][1]];
                    }
                    pebble[i][pattern] = maxpeb + score(pattern, i, A);
                }
            }

            Answer = pebble[n - 1][0];
            for (int pattern = 0; pattern < patterns.length; pattern++) {
                if (Answer < pebble[n - 1][pattern]) {
                    Answer = pebble[n - 1][pattern];
                }
            }

            // output4.txt로 답안을 출력합니다.
            pw.println("#" + test_case + " " + Answer);
			/*
			   아래 코드를 수행하지 않으면 여러분의 프로그램이 제한 시간 초과로 강제 종료 되었을 때,
			   출력한 내용이 실제로 파일에 기록되지 않을 수 있습니다.
			   따라서 안전을 위해 반드시 flush() 를 수행하시기 바랍니다.
			 */
            pw.flush();
        }

        br.close();
        pw.close();
    }

    // 양립할 수 있는 패턴 모음
    static final int[][] compatibles = new int[][]{
            {3, 4},
            {2, 5},
            {1, 5},
            {0, 4},
            {0, 3},
            {1, 2}
    };

    // 종류에 따라 계산하는 부분
    static final int[][] patterns = new int[][]{
            // index 0 : index of O index 1: index of X
            {0, 1}, // O X ㅅ---0
            {0, 2}, // O ㅅX ---1
            {1, 0}, // X O ㅅ---2
            {2, 0}, // X ㅅO ---3
            {1, 2}, // ㅅO X ---4
            {2, 1}, // ㅅX O ---5
    };

    // 패턴에 따라서 점수를 계산한다.
    // 시간 복잡도는 상수
    static int score(int method, int column, int[][] values) {
        int[] rule = patterns[method];
        return values[rule[0]][column] - values[rule[1]][column];
    }
}

