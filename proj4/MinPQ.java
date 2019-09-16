//Celine Cui
//3.26.2019
public class MinPQ{
    private Edge[] pq;
    private int n;

    public MinPQ() {
        n = 0;
        pq = new Edge[5];
    }

    private Edge[] resize() {
        Edge[] temp = new Edge[n*2];
        for (int i = 1; i <= n; i++) {
            temp[i] = pq[i];
        }
        return temp;
    }

    public void insert(Edge edge) {
        if(edge == null) return;
        if (n == pq.length - 1) pq = resize();
        pq[++n] = edge;
        swim(n);
    }

    public Edge delMin() {
        if (n == 0) return null;
        Edge min = pq[1];
        exch(1, n--);
        sink(1);
        pq[n+1] = null;
        if ((n > 0) && (n == (pq.length - 1) / 4)) pq = resize();
        return min;
    }

    /***************************************************************************
    * Helper functions to restore the heap invariant.
    ***************************************************************************/

    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }
   /***************************************************************************
    * Helper functions for compares and swaps.
    ***************************************************************************/
    private boolean greater(int i, int j) {
        return pq[i].getTime() > pq[j].getTime();
    }


    private void exch(int i, int j) {
        Edge swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }
}
