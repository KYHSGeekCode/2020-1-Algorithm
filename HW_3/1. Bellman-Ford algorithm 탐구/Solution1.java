import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution1 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution1.java -encoding UTF8

   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output1.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution1

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution1
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution1   // 0.5초 수행
       timeout 1 java Solution1     // 1초 수행
 */

/*
    시간 복잡도 분석
    BellmanFord1 : 초기화 Theta(N),  항상 N의 루프와 E의 루프를 도므로 Theta(NE) (상수무시)
    BellmanFord2 : Answer, length 초기화 Theta(N) Adjacency array 만드는데 Theta(E)이고 메인 루프는  O(NE)
                    따라서 O(NE)이다. 최악의 경우는 ajacency가 포화일경우이다.

 */

class Solution1 {

    static final int MAX_N = 1000;
    static final int MAX_E = 100000;
    static final int Div = 100000000;  // 1억
    static int N, E;
    static final int[] U = new int[MAX_E];
    static final int[] V = new int[MAX_E];
    static final int[] W = new int[MAX_E];
    static int[] Answer1 = new int[MAX_N + 1];
    static int[] Answer2 = new int[MAX_N + 1];
    static double start1, start2;
    static double time1, time2;

    static final int[][] adjacencyArray = new int[MAX_N][MAX_N];
    static final int[] adjacencyArrayLength = new int[MAX_N];


    public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input1.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output1.txt 로 정답을 출력합니다.
		 */
        BufferedReader br = new BufferedReader(new FileReader("input1.txt"));
        StringTokenizer stk;
        PrintWriter pw = new PrintWriter("output1.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
        for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 정점의 개수와 간선의 개수를 각각 N, E에 읽어들입니다.
			   그리고 각 i번째 간선의 시작점의 번호를 U[i], 끝점의 번호를 V[i]에, 간선의 가중치를 W[i]에 읽어들입니다.
			   (0 ≤ i ≤ E-1, 1 ≤ U[i] ≤ N, 1 ≤ V[i] ≤ N)
			 */
            stk = new StringTokenizer(br.readLine());
            N = Integer.parseInt(stk.nextToken());
            E = Integer.parseInt(stk.nextToken());
            stk = new StringTokenizer(br.readLine());
            for (int i = 0; i < E; i++) {
                U[i] = Integer.parseInt(stk.nextToken());
                V[i] = Integer.parseInt(stk.nextToken());
                W[i] = Integer.parseInt(stk.nextToken());
            }

            /* Problem 1-1 */
            start1 = System.currentTimeMillis();
            Answer1 = BellmanFord1();
            time1 = (System.currentTimeMillis() - start1);

            /* Problem 1-2 */
            start2 = System.currentTimeMillis();
            Answer2 = BellmanFord2();
            time2 = (System.currentTimeMillis() - start2);

            // output1.txt로 답안을 출력합니다.
            pw.println("#" + test_case);
            for (int i = 1; i <= N; i++) {
                pw.print(Answer1[i]);
                if (i != N)
                    pw.print(" ");
                else
                    pw.print("\n");
            }
            pw.println(time1);

            for (int i = 1; i <= N; i++) {
                pw.print(Answer2[i]);
                if (i != N)
                    pw.print(" ");
                else
                    pw.print("\n");
            }

            pw.println(time2);
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

    private static int[] BellmanFord1() {
        for (int i = 1; i <= N; i++) {
            Answer1[i] = 2000;
        }
        Answer1[1] = 0;

        for (int i = 1; i <= N - 1; i++) {
            for (int j = 0; j < E; j++) {
                final int u = U[j];
                final int v = V[j];
                final int add = Answer1[u] + W[j];
                if (add < Answer1[v])
                    Answer1[v] = add % Div;
            }
        }

        return Answer1;
    }


    private static final ArrayIntegerQueue queue = new ArrayIntegerQueue();
    private static final boolean[] has = new boolean[MAX_N];

    // 큐를 하나 만들어서
    // 하나의 버텍스의 거리를 relaxation 할 때마다 그 버텍스를 큐에 넣는다.
    // 큐에서 꺼내면 그 버텍스에 연결된 모든 vertex 들에 대하여 relaxation을 시도한다.
    // 모든 인덱스는  0부터 시작한다.
    private static int[] BellmanFord2() {
        final int startVertexm1 = 0;
        // convert to adjacencyArray & initialize answer2
        for (int i = 0; i < N; i++) {
            adjacencyArrayLength[i] = 0; // node i로부터 출발하는 엣지들의 수
            Answer2[i] = 2000; // node 0부터 node i 까지 가는 거리
        }
        Answer2[1] = 0;
        Answer2[N] = 2000; // 무한으로 초기화

        // 주어진 정보를 adjacency array로 변환하여 본 파트에서 성능 향상
        for (int e = 0; e < E; e++) {
            final int u = U[e] - 1; // starting edge
            adjacencyArray[u][adjacencyArrayLength[u]] = e; // u에서 출발하는 edge 번호를 추가한다.
            adjacencyArrayLength[u]++; // 배열의 끝 표시용
        }

        // Queue를 이용한 relaxation. 직전에 변화가 일어나 업데이트가 필요한 버텍스들만 relaxation
        queue.clear();
        queue.enqueue(startVertexm1);
        has[startVertexm1] = true;
        while (!queue.isEmpty()) {
            int u = queue.remove();
            has[u] = false;
            final int numAdjacent = adjacencyArrayLength[u];
            final int up1 = u + 1;
            for (int i = 0; i < numAdjacent; i++) {
                // relaxation
                final int e = adjacencyArray[u][i];
                final int vp1 = V[e];
                final int v = vp1 - 1;
                final int answer2Up1PWe = Answer2[up1] + W[e];
                // relaxation
                if (Answer2[vp1] > answer2Up1PWe) {
                    Answer2[vp1] = answer2Up1PWe % Div;
                    if (!has[v]) {
                        queue.enqueue(v);
                        has[v] = true;
                    }
                }
            }
        }
        return Answer2;
    }

    // Array based simple queue
    private static class ArrayIntegerQueue {
        final int SIZE = MAX_N;
        final int[] queue = new int[SIZE];
        int reader;
        int writer;

        public ArrayIntegerQueue() {
            reader = 0;
            writer = 0;
        }

        public boolean isEmpty() {
            return (reader % SIZE) == (writer % SIZE);
        }

        public void enqueue(int data) {
            queue[writer % SIZE] = data;
            writer++;
        }

        public int remove() {
            final int data = queue[reader % SIZE];
            reader++;
            return data;
        }

        public void clear() {
            reader = 0;
            writer = 0;
        }
    }
}













