package com.test;

public class Main {

    public static void main(String[] args) {
        SetNode n1 = new SetNode(1);
        SetNode n2 = new SetNode(2);
        SetNode n3 = new SetNode(1);
        SetNode n4 = new SetNode(3);

        System.out.println(n1.findSet() != n2.findSet());
        SetNode.union(n2, n1);
        System.out.println(n1.findSet() == n2.findSet());
        SetNode.union(n3, n1);
        System.out.println(n2.findSet() == n3.findSet());
    }

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
}
