package experiment;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class LBiX {

    //    unction to split a query into parts of window using the technique of PAIR
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

    //    Functions for backward aggregation
    static void backward_aggregation(int[] A, int value, int p_value) {
        int endP = A.length-1;

        A[endP] = value;
//        seed sum
//        A[endP-1] = p_value + value;
//        for(int i=endP-2;i>=0;i--) {
//            A[i] += A[i+1];
//        }

//        seek max
        A[endP-1] = Math.max(p_value, value);
        for(int i=endP-2;i>=0;i--) {
            A[i] = Math.max(A[i], A[i+1]);
        }
    }

    //    Functions for forward aggregation
    static void forward_aggregation(int[] A, int index, int value, int p_value) {
        if(index == 0) A[index] = value;
        else {
//        seek sum
//            A[index] = value + A[index-1];
//            A[index-1] = p_value;
//         seek max
            A[index] = Math.max(value, A[index-1]);
            A[index-1] = p_value;
        }
    }

    //    Function to find the result of a query after backward aggregation.
    static int backward_result(int[] A) {
        return A[0];
    }

    //    Function to find the result of a query after forward aggregation.
    static int forward_result(int[] A, int index) {
//        seek sum
//        return A[index] + A[index+1];
//        seek max
        return Math.max(A[index], A[index+1]);
    }

    //    LBiX main function
    static void LBiX(ArrayList<Integer> W, int s, String filename) {
        int len = W.size();
        ArrayList<ArrayList<Integer>> sharedPlans = new ArrayList<>();
        int [][] A = new int[len][];
//        int [][] square_A = new int[len][];
        ArrayList<Integer> len_list = new ArrayList<>();
        int[] index = new int[len];
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
            int[] Partials = new int[length];
//            int[] square_Partials = new int[length];
            A[i] = Partials;
//            square_A[i] = square_Partials;
        }
        int sum, square_sum;
        int[] ans = new int[len];
//        float[] ans = new float[len];
        int[] cnt = new int[len];
        int[] judge = new int[len];
        int[] cnt_value = new int[len];
        int[] value = new int[len];
//        int[] square_value = new int[len];
        boolean[] flag = new boolean[len];
        boolean[] output_flag = new boolean[len];
        boolean[] first_flag = new boolean[len];
        int[] p_v = new int[len];
//        int[] square_p_v = new int[len];
        long time_sum = 0, kk = 0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), Charset.forName("UTF-8")));
            String text;
            String[] data;
            while((text = br.readLine()) != null) {
                int v;
                data = text.split("\\s+");
                v = Integer.parseInt(data[3]);
                for(int i=0;i<len;i++) {
                    if(judge[i] == 0) {
//                        seek sum
//                        value[i] += v;
//                        seek max
                        value[i] = Math.max(value[i], v);
//                        seek sum of squares
//                        square_value[i] += v*v;

                        cnt_value[i]++;
                        if(cnt_value[i] == sharedPlans.get(i).get(0)) {
                            if(sharedPlans.get(i).get(1) == 0) judge[i] = 0;
                            else judge[i] = 1;
                            flag[i] = true;
                            output_flag[i] = true;
                        }
                    } else {
//                        seek sum
//                        value[i] += v;
//                        seek max
                        value[i] = Math.max(value[i], v);
//                        seek sum of squares
//                        square_value[i] += v*v;

                        cnt_value[i]++;
                        if(cnt_value[i] == sharedPlans.get(i).get(1)) {
                            judge[i] = 0;
                            flag[i] = true;
                        }
                    }
                    if(!flag[i]) continue;
                    long start_time = System.nanoTime();
                    if(index[i] == len_list.get(i)-1) {
                        backward_aggregation(A[i], value[i], p_v[i]);
//                        backward_aggregation(square_A[i], square_value[i], square_p_v[i]);
                        sum = backward_result(A[i]);
//                        square_sum = backward_result(square_A[i]);
                    } else {
                        forward_aggregation(A[i], index[i], value[i], p_v[i]);
//                        forward_aggregation(square_A[i], index[i], square_value[i], square_p_v[i]);
                        sum = forward_result(A[i], index[i]);
//                        square_sum = forward_result(square_A[i], index[i]);
                    }
//                    ans[i] = (float) square_sum/W.get(i) - (float) Math.pow(sum/W.get(i), 2);
                    ans[i] = sum;
//                    if(output_flag[i]) {
//                        System.out.print(ans[i] + " ");
//                    }
//                    if(output_flag[i] && i == 1) {
//                        System.out.println();
//                    }
                    p_v[i] = value[i];
//                    square_p_v[i] = square_value[i];
                    index[i]++;
                    if(index[i] == len_list.get(i)) index[i] = 0;
                    value[i] = 0;
//                    square_value[i] = 0;
                    flag[i] = false;
                    cnt_value[i] = 0;
                    output_flag[i] = false;
                    cnt[i]++;
                    long end_time = System.nanoTime();
                    time_sum += end_time - start_time;
                    if(i == 0) kk += 1;
                }
            }
        } catch(Exception ex) {}
        System.out.println();
        System.out.println("LBiX_result : " + time_sum/kk + "μs");
//        float quot = (float) time_sum/1000000000;
//        int throughput = (int) (kk*len/quot);
//        System.out.println("LBiX_result : " + throughput + "tuple/s");
    }
}
