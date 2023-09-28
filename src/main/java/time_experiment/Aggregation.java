package time_experiment;

public class Aggregation {
    public static int Aggregation(String[] records) {
        int ans = 0;
        for (int i = 0; i < records.length; i++) {
//            合計を求める
            ans += Integer.parseInt(records[i]);
//            最大を求める
//            ans = Math.max(ans, Integer.parseInt(records[i]));
        }
        return ans;
    }
    public static int Aggregation(String[] records, int a) {
        int ans = 0;
        for (int i = 0; i < records.length; i++) {
            ans += Integer.parseInt(records[i])*Integer.parseInt(records[i]);
        }
        return ans;
    }
    public static int Aggregation(int A, int B) {
        return A+B;
//        return Math.max(A,B);
    }

    public static int Aggregation(int A, int B, boolean c) {
        return A+B;
//        return Math.max(A,B);
//        return A+B*B;
    }
}
