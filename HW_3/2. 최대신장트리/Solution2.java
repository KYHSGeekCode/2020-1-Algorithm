import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution2 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution2.java -encoding UTF8

   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output2.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution2

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution2
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution2   // 0.5초 수행
       timeout 1 java Solution2     // 1초 수행
 */

/*
    이 알고리즘의 시간 복잡도
    node 들을 초기화하는 시간 복잡도 Theta(N)
    radix sort 시간 복잡도 Theta(E)
    마지막 while 루프의  시간 복잡도 O(E log* N)
    총 복잡도 O(E log* N)이다.
    단, log * N은 log log log...N<= 1 되는 log의 횟수
 */

class Solution2 {
    static final int MAX_N = 20000;
    static final int MAX_E = 80000;

    static int N, E;
    static int[] U = new int[MAX_E], V = new int[MAX_E], W = new int[MAX_E];
    static int Answer;
    static SetNode[] nodes = new SetNode[MAX_N];

    public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input2.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output2.txt 로 정답을 출력합니다.
		 */
        BufferedReader br = new BufferedReader(new FileReader("input2.txt"));
        StringTokenizer stk;
        PrintWriter pw = new PrintWriter("output2.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
        for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 정점의 개수와 간선의 개수를 각각 N, E에 읽어들입니다.
			   그리고 각 i번째 간선의 양 끝점의 번호를 U[i], V[i]에 읽어들이고, i번째 간선의 가중치를 W[i]에 읽어들입니다. (0 ≤ i ≤ E-1, 1 ≤ U[i] ≤ N, 1 ≤ V[i] ≤ N)
			 */
            stk = new StringTokenizer(br.readLine());
            N = Integer.parseInt(stk.nextToken());
            E = Integer.parseInt(stk.nextToken());
            stk = new StringTokenizer(br.readLine());

            U = new int[E];
            V = new int[E];
            W = new int[E];
            for (int i = 0; i < E; i++) {
                U[i] = Integer.parseInt(stk.nextToken());
                V[i] = Integer.parseInt(stk.nextToken());
                W[i] = Integer.parseInt(stk.nextToken());
            }

//            System.out.println("Case" + test_case);
            /////////////////////////////////////////////////////////////////////////////////////////////
			/*
			   이 부분에서 여러분의 알고리즘이 수행됩니다.
			   문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
			 */
            /////////////////////////////////////////////////////////////////////////////////////////////
            //KRUSKAL ALGORITHM을 거꾸로
            for (int i = 0; i < N; i++) {
                nodes[i] = new SetNode(i);
            }

            int[] W2 = W.clone();
            int[] U2 = U.clone();
            int[] V2 = V.clone();
//            System.out.println(Arrays.toString(Arrays.copyOfRange(W, 0, E)));
//            System.out.println(Arrays.toString(Arrays.copyOfRange(U, 0, E)));
//            System.out.println(Arrays.toString(Arrays.copyOfRange(V, 0, E)));
            DoRadixSort(W, U, V, W2, U2, V2);
//            System.out.println(Arrays.toString(Arrays.copyOfRange(W2, 0, E)));
//            System.out.println(Arrays.toString(Arrays.copyOfRange(U2, 0, E)));
//            System.out.println(Arrays.toString(Arrays.copyOfRange(V2, 0, E)));
//            System.out.println(Arrays.toString(Arrays.copyOfRange(W, 0, E)));
//            System.out.println(Arrays.toString(Arrays.copyOfRange(U, 0, E)));
//            System.out.println(Arrays.toString(Arrays.copyOfRange(V, 0, E)));
            W = W2;
            U = U2;
            V = V2;

            int numOfEdges = 0;
            int i = E - 1;
            int sumW = 0;
            while (numOfEdges < N - 1 && i >= 0) {
                SetNode nodeU, nodeV;
//                System.out.println(" N=" + N + " E=" + E + "  NOE=" + numOfEdges + " i = " + i + " U = " + U[i] + " V = " + V[i] + " W=" + W[i]);
                nodeU = nodes[U[i] - 1];
                nodeV = nodes[V[i] - 1];

                if (nodeU.findSet() != nodeV.findSet()) {
                    SetNode.union(nodeU, nodeV);
                    numOfEdges++;
//                    System.out.println("Pass " + W[i]);
                    sumW += W[i];
                }
                i--;
            }
            Answer = sumW;

            // output2.txt로 답안을 출력합니다.
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

    // 집함의 표현을 위한 자료구조
    private static class SetNode {
        int data;
        int rank;
        SetNode parent;

        public SetNode(int data) {
            this.data = data;
            parent = this;
            rank = 0;
        }

        public SetNode findSet() {
            if (parent != this)
                parent = parent.findSet();
            return parent;
        }

        public static void union(SetNode x, SetNode y) {
            x = x.findSet();
            y = y.findSet();
            if (x.rank > y.rank) {
                y.parent = x;
            } else {
                x.parent = y;
                if (x.rank == y.rank)
                    y.rank++;
            }
        }
    }

    // modified 자료구조 HW 4
    private static int[] DoRadixSort(int[] value, int[] U, int[] V, int[] backBuffer, int[] backU, int[] backV) {
        // 256진법 radix sort with metadata
        int[] currentBuffer = value;
        int[] currentV = V;
        int[] currentU = U;
        for (int i = 0; i < 4; i++) {
            int[] counts = new int[256];
            // 끝에서 i째 바이트에 대한 count정렬을 위한 count
            for (int j = 0; j < currentBuffer.length; j++) {
                int theByte = (currentBuffer[j] >> (i << 3)) & 0x00FF; // 끝에서 i째 바이트를 가져온다.
                counts[theByte]++;
            }
            // count를 누적 count로 변환.
            for (int j = 1; j < 256; j++) {
                counts[j] = counts[j] + counts[j - 1];
            }
            // 누적 count를 이용하여 데이터를 정렬하여 넣는다.
            for (int j = currentBuffer.length - 1; j >= 0; j--) {
                int theByte = (currentBuffer[j] >> (i << 3)) & 0x00FF; // 끝에서 i째 바이트를 가져온다.
                final int theIndex = (counts[theByte] - 1);
                backBuffer[theIndex] = currentBuffer[j]; // 원본데이터를 개수에 맞게 적절한 위치에 넣는다
                backU[theIndex] = currentU[j];
                backV[theIndex] = currentV[j];
                counts[theByte]--;
            }
            // 잦은메모리 할당을 막기 위한 최적화.
            // write 할 곳과 read할곳을 바꾼다.
            int[] tmp = currentBuffer;
            currentBuffer = backBuffer;
            backBuffer = tmp;
            tmp = currentU;
            currentU = backU;
            backU = tmp;
            tmp = currentV;
            currentV = backV;
            backV = tmp;
        }
        return currentBuffer;
    }
}

