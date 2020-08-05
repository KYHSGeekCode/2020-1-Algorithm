import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution4 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution3.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output4.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution3

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution3
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution3   // 0.5초 수행
       timeout 1 java Solution3     // 1초 수행
 */

/*
    시간 복잡도 분석
    초기화 Theta(n)
    상수 복잡도를 가지는 if문을n번의 2중 for문으로 둘러싸므로
    시간 복잡도는 Theta(n^2)이다.
    답: Theta(n^2)

 */

class Solution3 {
    static final int max_n = 100000;

    static int n;
    static String s;
    static int Answer;

    public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input3.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output3.txt 로 정답을 출력합니다.
		 */
        BufferedReader br = new BufferedReader(new FileReader("input3.txt"));
        StringTokenizer stk;
        PrintWriter pw = new PrintWriter("output3.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
        for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 파일에서 읽어옵니다.
               첫 번째 행에 쓰여진 문자열의 길이를 n에 읽어들입니다.
               그 다음 행에 쓰여진 문자열을 s에 한번에 읽어들입니다.
			 */
            stk = new StringTokenizer(br.readLine());
            n = Integer.parseInt(stk.nextToken());
            s = br.readLine();

            /////////////////////////////////////////////////////////////////////////////////////////////
			/*
			   이 부분에서 여러분의 알고리즘이 수행됩니다.
			   문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
			 */
            /////////////////////////////////////////////////////////////////////////////////////////////]
            // 최장 회문 부분순서의 길이를 구하라
            // 다를 수 있는 문자열의 최장 공통 부분 순서를 구할 때
            // 문자열 x와 문자열 y를 가지고 하는 대신 y를 x를  뒤집은 문자열로 적용한다.
            int[][] C = new int[n + 1][n + 1]; // C[i][j]는 문자열 a라 하면 a[0...i]와 a[n...n-j]의 최장공통부분길이
            for (int i = 0; i <= n; i++) {
                C[i][0] = 0;
                C[0][i] = 0;
            }
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    char xi = s.charAt(i - 1);
                    char yj = s.charAt(n - j); // 문자열 2를 다른 문자열이 아닌 그 문자열의 역방향으로 한다.
                    // 최장공통부분길이 알고리즘과 동일
                    if (xi == yj) { // 회문 성공
                        C[i][j] = C[i - 1][j - 1] + 1;
                    } else { // (a[i-1],a[j]) / (a[i], a[j-1]) 두 LCS의 길이 중 큰 것과 같다.
                        C[i][j] = Math.max(C[i - 1][j], C[i][j - 1]);
                    }
                }
            }
            Answer = C[n][n];


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
}

