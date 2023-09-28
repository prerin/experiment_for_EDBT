package experiment;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class proposed {
    //    Function to create array S
    static int[] settingS(int[] a, int d, int s) {
        int[] S = new int[d];
        int sum = 0;
        for(int i = d-1;i>=0;i--) {
            if(i%s == 0) {
                sum += a[i];
                S[i] = sum;
                sum = 0;
            } else {
                sum += a[i];
                S[i] = sum;
            }
        }
        return S;
    }

    //    Function to create array D
    static int[] settingD(int[] S, int Dlen, int s) {
        int[] D = new int[Dlen+1];
        int sum = 0;
        for(int i=Dlen; i>=0;i--) {
            sum += S[i*s];
            D[i] = sum;
        }
        return D;
    }

    //    Functions to perform aggregation
    static void aggregation(int[] a, int[] S, int[] D, int d, int s,int pos) {
//        seek sum
        if(pos%s == s-1 || pos == d-1) {
            int sum = a[pos]+a[pos-1];
            S[pos] = a[pos];
            int i;
            for(i=pos-1; i%s!=0; i--) {
                S[i] = sum;
                sum += S[i-1];
            }
            S[i] = sum;
            int t = pos/s;
            D[t] = sum;
            int len;
            if(d%s == 0) {
                len = d/s;
            } else {
                len = d/s + 1;
            }
            for(int j=0;j<len-1;j++) {
                t = (t+len-1)%len;
                sum += S[t*s];
                D[t] = sum;
            }
        }
        else if(pos%s == 0) {
            S[pos] = a[pos];
            D[pos/s] = S[pos];
        }
        else {
            S[pos] = S[pos-1] + a[pos];
            S[pos-1] = a[pos-1];
            D[pos/s] = S[pos];
        }

//        seek max
//        if(pos%s == s-1 || pos == d-1) {
//            int ans = Math.max(a[pos], a[pos-1]);
//            S[pos] = a[pos];
//            int i;
//            for(i=pos-1; i%s!=0; i--) {
//                S[i] = ans;
//                ans = Math.max(ans, S[i-1]);
//            }
//            S[i] = ans;
//            int t = pos/s;
//            D[t] = ans;
//            int len;
//            if(d%s == 0) {
//                len = d/s;
//            } else {
//                len = d/s + 1;
//            }
//            for(int j=0;j<len-1;j++) {
//                t = (t+len-1)%len;
//                ans = Math.max(ans, S[t*s]);
//                D[t] = ans;
//            }
//        }
//        else if(pos%s == 0) {
//            S[pos] = a[pos];
//            D[pos/s] = S[pos];
//        }
//        else {
//            S[pos] = Math.max(S[pos-1], a[pos]);
//            S[pos-1] = a[pos-1];
//            D[pos/s] = S[pos];
//        }
    }

//     Functions for query results

    static void create_result(int[] result, ArrayList<Integer> W, int[] S, int[] D, int d, int s, int pos) {
//        seek sum
        int len = W.size();
        int X;
        if(d%s == 0) {
            X = d;
        } else {
            X = (d/s+1)*s;
        }

        for(int i=0;i<len;i++) {
            int Pe = (pos+d+1-W.get(i))%d;
            if((pos%s == s-1 || pos == d-1) && Pe%s == 0) {
                result[i] = D[Pe/s];
            } else if(pos%s == s-1 || pos == d-1) {
                int x = (Pe+s)%X/s;
                result[i] = D[x] + S[Pe];
            } else if(Pe%s == 0) {
                result[i] = S[pos] + D[Pe/s];
            } else {
                int x = (Pe+s)%X/s;
                int y = pos%X/s;
                if(x != y) {
                    result[i] = S[pos] + S[Pe] + D[x];
                } else {
                    result[i] = S[pos] + S[Pe];
                }
            }
        }

//        seek max
//        int len = W.size();
//        int X;
//        if(d%s == 0) {
//            X = d;
//        } else {
//            X = (d/s+1)*s;
//        }
//
//        for(int i=0;i<len;i++) {
//            int Pe = (pos+d+1-W.get(i))%d;
//            if((pos%s == s-1 || pos == d-1) && Pe%s == 0) {
//                result[i] = D[Pe/s];
//            } else if(pos%s == s-1 || pos == d-1) {
//                int x = (Pe+s)%X/s;
//                result[i] = Math.max(D[x], S[Pe]);
//            } else if(Pe%s == 0) {
//                result[i] = Math.max(S[pos], D[Pe/s]);
//            } else {
//                int x = (Pe+s)%X/s;
//                int y = pos%X/s;
//                if(x != y) {
//                    result[i] = Math.max(S[pos], Math.max(S[Pe], D[x]));
//                } else {
//                    result[i] = Math.max(S[pos], S[Pe]);
//                }
//            }
//        }
    }

    // LSiX main function
    static void Proposed(ArrayList<Integer> W, int p, String filename) {
        int len = W.size();
        int pos = 0;
        int d = W.get(len - 1);
        int s = W.get(0);
        int p_data[] = new int[d];
//        int square_p_data[] = new int[d];
        for (int i = 0; i < d; i++) {
            p_data[i] = 0;
//            square_p_data[i] = 0;
        }
        int[] result = new int[len];
//        int[] square_result = new int[len];
        float[] ans = new float[len];
        int count = 0;
        int[] S = settingS(p_data, d, s);
//        int[] square_S = settingS(square_p_data, d, s);
        int Dlen = 0;
        if (d % s == 0) {
            Dlen = d / s - 1;
        } else {
            Dlen = d / s;
        }
        int[] D = settingD(S, Dlen, s);
//        int[] square_D = settingD(square_S, Dlen, s);
        long sum_time = 0;
        int kk = 0;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), Charset.forName("UTF-8")));
            String text;
            String[] data;
            while ((text = br.readLine()) != null) {
                data = text.split("\\s+");
                p_data[count] = Integer.parseInt(data[3]);
//                square_p_data[count] = (int) Math.pow(Integer.parseInt(data[3]), 2);
                long start_time = System.nanoTime();
                aggregation(p_data, S, D, d, s, count);
//                aggregation(square_p_data, square_S, square_D, d, s, count);
                create_result(result, W, S, D, d, s, count);
//                create_result(square_result, W, square_S, square_D, d, s, count);
                for (int i = 0; i < len; i++) {
//                    ans[i] = (float) square_result[i]/W.get(i) - (float) Math.pow(result[i]/W.get(i),2);
                    ans[i] = result[i];
                }
                long end_time = System.nanoTime();
                count = (count + 1) % d;
                kk += 1;
                sum_time += end_time - start_time;
//                if(kk%10 != 0) continue;
//                for(int i=0;i<len;i++) {
//                    System.out.print(result[i] + " " );
//                }
//                System.out.println();
            }
        } catch (Exception ex) {
        }
        System.out.println();
        System.out.println("proposed_result：" + sum_time / kk + " μs");
//        double quot = (double) sum_time/1000000000;
//        long throughput = (long) (kk/quot);
//        System.out.println("proposed_result : " + throughput*len + " tuple/s");
    }
}
