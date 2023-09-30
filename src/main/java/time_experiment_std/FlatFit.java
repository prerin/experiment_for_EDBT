package time_experiment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import static time_experiment.Aggregation.Aggregation;

public class FlatFit {
//    Function to split a query into parts of window using the technique of PAIR
    static ArrayList<Integer> BuildSharePlan(int w, int s) {
        int f1 = w%s;
        int f2 = s-f1;
        ArrayList<Integer> sharedPlan = new ArrayList<>();
        if(f1 == 0) {
            sharedPlan.add(f2);
            sharedPlan.add(0);
        } else {
            sharedPlan.add(f1);
            sharedPlan.add(f2);
        }
        return sharedPlan;
    }

//    Functions for storing initial values
    static void preparation(int[] Partial, int[] Pointer, String fun, int len) {
        if(fun == "sum") {
            for(int i=0;i<len;i++) {
                Partial[i] = 0;
                Pointer[i] = i+1;

            }
        } else if(fun == "max") {
            for (int i = 0; i < len; i++) {
                Partial[i] = -100000000;
                Pointer[i] = i+1;
            }
        } else if(fun == "min") {
            for(int i=0;i<len;i++) {
                Partial[i] = 100000000;
                Pointer[i] = i+1;
            }
        }
        Pointer[len-1] = 0;
    }

//    FUnctions to be performed in the first aggregate
    static int Wreset(int[] Partial, int[] Pointer, int len, int currInd, int prevInd, int value, int[] cnt) {
        for(int i=0;i<len;i++) {
            if(i == currInd) continue;
            Partial[i] = value;
            Pointer[i] = currInd;
        }
        int startInd = currInd;
        Deque<Integer> Position = new ArrayDeque<>();
        do {
            Position.push(startInd);
            startInd = Pointer[startInd];
        } while(startInd != currInd);
        int ans = Partial[Position.pop()];
        int tempInd;
        while(Position.size() > 1) {
            tempInd = Position.pop();

//            seek sum
            ans += Partial[tempInd];
//            seek max
//            ans = Math.max(ans, Partial[tempInd]);
            cnt[0]++;
            Partial[tempInd] = ans;
            Pointer[tempInd] = currInd;
        }
        tempInd = Position.pop();

//        seek sum
        ans += Partial[tempInd];
//        seek max
//        ans = Math.max(ans, Partial[tempInd]);

        cnt[0]++;
        return ans;
    }

//    Functions to perform aggregation
    static int execution(int[] Partial, int[] Pointer, int len, int currInd, int prevInd, int value, int[] cnt) {
        Partial[prevInd] = value;
        Pointer[prevInd] = currInd;
        int startInd = currInd;
        Deque<Integer> Position = new ArrayDeque<>();
        do {
            Position.push(startInd);
            startInd = Pointer[startInd];
        } while(startInd != currInd);
        int ans = Partial[Position.pop()];
        int tempInd;
        while(Position.size() > 1) {
            tempInd = Position.pop();

//            seek sum
            ans += Partial[tempInd];
//            seek max
//            ans = Math.max(ans, Partial[tempInd]);

            cnt[0]++;
            Partial[tempInd] = ans;
            Pointer[tempInd] = currInd;
        }
        tempInd = Position.pop();

//        seek sum
        ans += Partial[tempInd];
//       seek max
//        ans = Math.max(ans, Partial[tempInd]);

        cnt[0]++;
        return ans;
    }

//    FlatFit main function
    public static void FlatFit(ArrayList<Integer> W, int s, String filename) {
//         W is an array containing the window size, s is a variable containing the slide size and filename is the name of the file containing the stream data
        int len = W.size();
        int[][] Partials = new int[len][];
        int[][] Pointers = new int[len][];
        int[][] square_Partials = new int[len][];
        int[][] square_Pointers = new int[len][];
        ArrayList<ArrayList<Integer>> sharedPlans = new ArrayList<>();
        ArrayList<Integer> len_list = new ArrayList<>();
        int[] curr_Ind = new int[len];
        int[] prev_Ind = new int[len];

//        Array preparation
        for(int i=0;i<len;i++) {
            int w = W.get(i);
            ArrayList<Integer> sharedPlan = BuildSharePlan(w, s);
            sharedPlans.add(sharedPlan);
            int length;
            if(w%s == 0) {
                length = w/s;
            } else {
                length = (w/s)*2 + 1;
            }
            len_list.add(length);
            int[] Partial = new int[length];
            int[] Pointer = new int[length];
            int[] square_Partial = new int[length];
            int[] square_Pointer = new int[length];
            preparation(Partial, Pointer, "sum", length);
            preparation(square_Partial, square_Pointer, "sum", length);
            Partials[i] = Partial;
            Pointers[i] = Pointer;
            square_Partials[i] = square_Partial;
            square_Pointers[i] = square_Pointer;
            prev_Ind[i] = length-1;
            curr_Ind[i] = 0;
        }
        int sum, square_sum;
//        int[] ans = new int[len];
        float[] ans = new float[len];
        int[] cnt = new int[len];
        int[] judge = new int[len];
        int[] cnt_value = new int[len];
        int[] value = new int[len];
        int[] square_value = new int[len];
        boolean[] flag = new boolean[len];
        boolean[] output_flag = new boolean[len];
        boolean[] first_flag = new boolean[len];
        long time_sum = 0, kk = 0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), Charset.forName("UTF-8")));
            String text;
            String[] data;
            while((text = br.readLine()) != null) {
                int v;
                data = text.split("\\s+");
                v = Aggregation(data);
                for(int j=0;j<len;j++) {
                    if (judge[j] == 0) {
//                        seek sum
                        value[j] += v;
//                        seek max
//                        value[j] = Math.max(value[j], v);
//                        seek sum of squares
                        square_value[j] += v*v;

                        cnt_value[j]++;
                        if (cnt_value[j] == sharedPlans.get(j).get(0)) {
                            if(sharedPlans.get(j).get(1) == 0) judge[j] = 0;
                            else judge[j] = 1;
                            flag[j] = true;
                            output_flag[j] = true;
                        }
                    } else {
//                        seek sum
                        value[j] += v;
//                        seek max
//                        value[j] = Math.max(value[j], v);
//                        seek sum of squares
                        square_value[j] += v*v;
                        cnt_value[j]++;
                        if (cnt_value[j] == sharedPlans.get(j).get(1)) {
                            judge[j] = 0;
                            flag[j] = true;
                        }
                    }
                    if (!flag[j]) continue;
                    long start_time = System.nanoTime();
                    if (first_flag[j] == false) {
                        sum = Wreset(Partials[j], Pointers[j], len_list.get(j), curr_Ind[j], prev_Ind[j], value[j], cnt);
                        square_sum = Wreset(square_Partials[j], square_Pointers[j], len_list.get(j), curr_Ind[j], prev_Ind[j], square_value[j], cnt);
                        first_flag[j] = true;
                    } else {
                        sum = execution(Partials[j], Pointers[j], len_list.get(j), curr_Ind[j], prev_Ind[j], value[j], cnt);
                        square_sum = execution(square_Partials[j], square_Pointers[j], len_list.get(j), curr_Ind[j], prev_Ind[j], square_value[j], cnt);
                    }
//                    ans[j] = sum;
                    ans[j] = (float) square_sum/W.get(j) - (float) Math.pow(sum/W.get(j), 2);
//                    if(output_flag[j]) {
//                        System.out.print(ans[j] + " ");
//                    }
//                    if(output_flag[j] && j == len-1) {
//                        System.out.println();
//                    }
                    prev_Ind[j] = curr_Ind[j];
                    curr_Ind[j]++;
                    if (curr_Ind[j] == len_list.get(j)) curr_Ind[j] = 0;
                    value[j] = 0;
                    square_value[j] = 0;
                    flag[j] = false;
                    cnt_value[j] = 0;
                    output_flag[j] = false;
                    long end_time = System.nanoTime();
                    time_sum += end_time - start_time;
                    if(j == 0) kk+= 1;
                }
            }
        } catch (Exception ex) {}
        System.out.println();
        System.out.println("FlatFit_result : " + time_sum/kk + "μs");
        float quot = (float) time_sum/1000000000;
        int throughput = (int) (kk*len/quot);
        System.out.println("FlatFit_result : " + throughput + "tuple/s");
    }
}
