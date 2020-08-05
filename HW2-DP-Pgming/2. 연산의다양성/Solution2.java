import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution4 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution2.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output4.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution2

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution2
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution2   // 0.5초 수행
       timeout 1 java Solution2     // 1초 수행
 */

/*
    Solution 2의 시간 복잡도 분석
    사중 루프이다. 그러나 루프 하나는 상수회수 반복이다.
    입력 문자열의 길이 l = n이라 하면
    가장 안쪽 m 변수의  루프가 Theta(n)
    그 밖의  alpha 변수의 루프는 상수복잡도
    그 밖의 from 변수의 루프가 Theta(n) 곱함
    그 밖의 to 변수의 루프가 Theta(n) 곱함
    따라서 최종 복잡도는 Theta(n^3)이다.
 */

class Solution2 {

    /*
        ** 주의사항
        정답의 숫자가 매우 크기 때문에 답안은 반드시 int 대신 long 타입을 사용합니다.
        그렇지 않으면 overflow가 발생해서 0점 처리가 됩니다.
        Answer[0]을 a의 개수, Answer[1]을 b의 개수, Answer[2]를 c의 개수라고 가정했습니다.
    */
    static int n;                           // 문자열 길이
    static String s;                        // 문자열
    static long[] Answer = new long[3];     // 정답

    public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input2.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output4.txt 로 정답을 출력합니다.
		 */
        BufferedReader br = new BufferedReader(new FileReader("input2.txt"));
        StringTokenizer stk;
        PrintWriter pw = new PrintWriter("output2.txt");

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
//            System.out.println(s);
            /////////////////////////////////////////////////////////////////////////////////////////////
			/*
			   이 부분에서 여러분의 알고리즘이 수행됩니다.
			   정답을 구하기 위해 주어진 문자열 s를 여러분이 원하시는 대로 가공하셔도 좋습니다.
			   문제의 답을 계산하여 그 값을 Answer(long 타입!!)에 저장하는 것을 가정하였습니다.
			 */
            /////////////////////////////////////////////////////////////////////////////////////////////
            int l = s.length();
            converted = new int[l];
            // 인덱스로 쓰려고 숫자로 변환
            for (int i = 0; i < l; i++) {
                converted[i] = s.charAt(i) - 'a';
            }

            result = new long[3][l][l];
//            for (int alpha = 0; alpha < 3; alpha++) {
//                for (int i = 0; i < l; i++) {
//                    for (int j = 0; j < l; j++) {
//                        result[alpha][i][j] = -1;
//                    }
//                }
//            }

            for (int alpha = 0; alpha < 3; alpha++) {
                for (int k = 0; k < l; k++) {
                    result[alpha][k][k] = converted[k] == alpha ? 1 : 0;
                }
            }
//            for (int alpha = 0; alpha < 3; alpha++) {
//                for (int k = 0; k < l - 1; k++) {
//                    result[alpha][k][k + 1] = table[converted[k]][converted[k + 1]] == alpha ? 1 : 0;
//                }
//            }

            // 루프 자체는 l 복잡도
            for (int to = 1; to < l; to++) {
                // 루프 자체는 l 복잡도
                for (int from = 0; from < l - to; from++) {
                    // 루프 자체는 상수 복잡도
                    for (int alpha = 0; alpha < 3; alpha++) {
                        result[alpha][from][from + to] = 0;
                        // 루프 자체는 l 복잡도
                        for (int m = from; m <= from + to - 1; m++) {
                            // 상수 복잡도 루프 언롤링
                            result[alpha][from][from + to] += result[how[alpha][0][0]][from][m] * result[how[alpha][0][1]][m + 1][from + to];
                            result[alpha][from][from + to] += result[how[alpha][1][0]][from][m] * result[how[alpha][1][1]][m + 1][from + to];
                            result[alpha][from][from + to] += result[how[alpha][2][0]][from][m] * result[how[alpha][2][1]][m + 1][from + to];
                        }
                    }
                }
            }

            // 0부터 l-1까지의 문자열이 각 알파벳이 되는 경우의 수
            for (int i = 0; i < 3; i++) {
                Answer[i] = result[i][0][l - 1];
            }
            
            // output4.txt로 답안을 출력합니다.
            pw.println("#" + test_case + " " + Answer[0] + " " + Answer[1] + " " + Answer[2]);
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

    private static final int[][] table = new int[][]{
            {1, 1, 0},
            {2, 1, 0},
            {0, 2, 2}
    };

    private static final int[][][] countTable = new int[][][]{
            // a
            {
                    {0, 0, 1},
                    {0, 0, 1},
                    {1, 0, 0},
            },
            // b
            {
                    {1, 1, 0},
                    {0, 1, 0},
                    {0, 0, 0},
            },
            // c
            {
                    {0, 0, 0},
                    {1, 0, 0},
                    {0, 1, 1},
            }
    };

    private static final int[][][] how = new int[][][]{
            // how to make a
            {
                    {0, 2},
                    {1, 2},
                    {2, 0},
            },
            // how to make b
            {
                    {0, 0},
                    {0, 1},
                    {1, 1},
            },
            // how to make c
            {
                    {1, 0},
                    {2, 1},
                    {2, 2},
            },

    };

    static long[][][] result;
    static int[] converted;

}

