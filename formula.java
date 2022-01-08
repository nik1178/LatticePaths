public class formula {
    public static void main(String[] args) {
        System.out.println(paths(20));
    }

    static long paths(int n){
        // apply the above stated rule for n iterations
        long j = 1;
        for (int i= 1; i <= n; i++)
        {
            j = j * (n + i) / i;
        }
        return j;
    }
}
