//Celine Cui
//3.2.2019
public class CarPQ{
    private int n;
    private Car[] pq;
    private char mode;       //m for mileage, p for price
    private boolean specialHeap;


    public CarPQ(char mode, boolean specialHeap) {
        n = 0;
        this.mode = mode;
        this.specialHeap = specialHeap;
        pq = new Car[15]; //initialize 15 cars to save memory since cars.txt has 14 cars.
    }


    public void insert(Car car){
        if(car == null) return;
        n++;
        if(n == pq.length){
            pq =resize();
        }
        pq[n] = car;
        swim(n);
    }

    public Car[] resize(){
        Car[] newHeap = new Car[n*2];
        for(int i = 0; i < n; i++){
            newHeap[i] = pq[i];
        }
        return newHeap;
    }

    public Car getMin(){
        if (n == 0) return null;
        return pq[1]; //heap[0] is null
    }

    public void update(int i){
        if (i < 0) throw new IndexOutOfBoundsException();
        swim(i);
        sink(i);
    }

    public void remove(int i){
        if (i < 0) throw new IndexOutOfBoundsException();
        exch(i, n--);
        swim(i);
        sink(i);
        pq[n+1] = null;
    }

   /***************************************************************************
    * General helper functions.
    ***************************************************************************/
    private boolean greater(int i, int j){
        if(mode == 'm') return pq[i].getMileage() > pq[j].getMileage();
        else if(mode == 'p') return pq[i].getPrice() > pq[j].getPrice();
        else return false;
    }

    private void exch(int i, int j){
        Car swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;

        setSpecialHeapIndex(i);
        setSpecialHeapIndex(j);
    }

    private void setSpecialHeapIndex(int i){
        if(mode == 'm'){
            if(specialHeap) pq[i].setSpecialMileage(i);
            else pq[i].setMileageIndex(i);
        } else if(mode == 'p'){
            if(specialHeap) pq[i].setSpecialPrice(i);
            else pq[i].setPriceIndex(i);
        }
    }

    /***************************************************************************
     * Helper functions to restore the heap invariant.
     ***************************************************************************/
    private void swim(int k){
        while (k > 1 && greater(k/2, k)){
            exch(k, k/2);
            k = k/2;
        }
        setSpecialHeapIndex(k);
    }

    private void sink(int k){
        while (2*k <= n){
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
        setSpecialHeapIndex(k);
    }
}
