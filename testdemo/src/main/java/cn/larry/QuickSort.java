package cn.larry;

public class QuickSort {

    public static void main(String[] args) {
        int[] a = new int[]{2,3,1,4,7};
      //  quickSort(a);
       // for(int i:a){
            System.out.println(findK(a,0,a.length-1,4));
      //  }
    }

    public static void quickSort(int[] a){
        sort(a,0,a.length-1);
    }

    private static void sort(int[] a,int lo,int hi){
        if(lo>=hi)return;
        int j = partition(a,lo,hi);
        sort(a,lo,j-1);
        sort(a,j+1,hi);
    }

    private static int partition(int[] a,int lo,int hi){
        int i = lo;int j = hi+1;
        int  v = a[lo];
        while (true){
            while (a[++i]<v)if(i==hi) break;
            while (a[--j]>v)if(j==lo)break;
            if(i>=j)break;
            exch(a,i,j);
        }
        exch(a,lo,j);
        return j;
    }

    private static void exch(int[] a,int i,int j){
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static int findK(int[] a,int lo,int hi,int k){
        if(lo>=hi )return a[lo];
        while (true){
            int j = partition(a,lo,hi);
            int p = j-lo+1;
            if(p==k)return a[j];
            if(p>k)return findK(a,lo,j-1,k);
            if(p<k)return findK(a,j+1,hi,k-p);
        }
    }
}
