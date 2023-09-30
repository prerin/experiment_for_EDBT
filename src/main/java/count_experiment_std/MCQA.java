package count_experiment;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class MCQA {
    //   Function to find the array R
    static Integer[] Rset(ArrayList<Integer> W, int p) {
        int len = W.size();
        List<Integer> R = new ArrayList <>();
        for(int i=0;i<len;i++) {
            int elementW = W.get(i);
            int k = elementW % p;
            R.add(k);
        }
        R.add(0);
        List<Integer> RR = new ArrayList<Integer>(new HashSet<>(R));
        Collections.sort(RR);
        RR.add(p);
        Integer[] k = RR.toArray(new Integer[0]);
        return k;
    }

    // Definition of initial declarations
    static ArrayList<ArrayList<Integer>> first_Declaration(Integer[] R, int p) {
        int len = R.length;
        ArrayList<ArrayList<Integer>> feeds = new ArrayList<ArrayList<Integer>>();
        for(int i=0;i<len-1;i++) {
            ArrayList<Integer> feed = new ArrayList<Integer>();
            feed.add((int) (p));
            feed.add((int) (R[i]));
            int k = R[i+1] - R[i];
            feed.add((int) (k));
            feeds.add(feed);
            if (i != 0) {
                ArrayList<Integer> feed2 = new ArrayList<Integer>();
                feed2.add((int) (p));
                feed2.add((int) (0));
                feed2.add((int) (R[i+1]));
                feeds.add(feed2);
            }
        }
        return feeds;
    }

    //    Determine the components of each window
    static ArrayList<Integer> window_component(int W, ArrayList<ArrayList<Integer>> feeds, int p) {
        int s = 0;
        int left = W;
        int len = feeds.size();
        int sum = 0;
        ArrayList<Integer> ans = new ArrayList<Integer>();
        while(left != 0) {
            int k = 0;
            int index = 0;
            for(int i=0;i<len;i++) {
                if(feeds.get(i).get(1) == s) {
                    int x = feeds.get(i).get(2);
                    if(x > k && left >= x) {
                        k = x;
                        index = i;
                    }
                }
            }
            left -= k;
            sum += k;
            ans.add(index);
            s = sum % p;
        }
        return ans;
    }

    //    adding feeds
    static ArrayList<ArrayList<Integer>> add_feeds(ArrayList<Integer> com, ArrayList<ArrayList<Integer>> feeds, int p) {
        ArrayList<Integer> feed2 = new ArrayList<Integer>();
        ArrayList<Integer> ep = new ArrayList<Integer>();
        int end = 0;
        int len = com.size();

        for(int i=0;i<len;i++) {
            end += feeds.get(com.get(i)).get(2);
            ep.add(end);
        }
        int Isi = 0;
        for(int i=1;i<len;i++) {
            end = ep.get(i);
            if(end%p==0) {
                for (int j=i-2; j >= Isi; j--) {
                    ArrayList<Integer> feed3 = new ArrayList<Integer>();
                    int stop = ep.get(j);
                    feed3.add((int) (p));
                    feed3.add((int) (stop%p));
                    feed3.add((int) (end-stop));
                    feeds.add(feed3);
                }
                ArrayList<Integer> feed4 = new ArrayList<Integer>();
                feed4.add((int) (p));
                feed4.add((int) (0));
                feed4.add((int) (end));
                feeds.add(feed4);
            }
        }
        feed2.add((int) (p));
        feed2.add((int) (0));
        feed2.add((int) (end));
        feeds.add(feed2);
        return feeds;
    }

    //    Creating an array of components
    static ArrayList<ArrayList<Integer>> add_component(ArrayList<ArrayList<Integer>> component_W,
                                                       ArrayList<Integer> com, ArrayList<ArrayList<Integer>> feeds, int p) {
        ArrayList<Integer> ep = new ArrayList<Integer>();
        int end = 0;
        int len = com.size();

        for(int i=0;i<len;i++) {
            end += feeds.get(com.get(i)).get(2);
            ep.add(end);
        }
        int Isi = 0;
        for(int i=1;i<len;i++) {
            ArrayList<Integer> feed2 = new ArrayList<Integer>();
            end = ep.get(i);
            if(end%p==0) {
                feed2.add(com.get(i));
                for (int j=i-1; j >= Isi; j--) {
                    feed2.add(0, com.get(j));
                    ArrayList<Integer> feed3 = new ArrayList<>(feed2);
                    component_W.add(feed3);
                }
            }
        }
        component_W.add(com);
        return component_W;
    }

    //    Function to find out how many components of each window are from the previous window
    static ArrayList<Integer> sub_component(ArrayList<Integer> com, ArrayList<ArrayList<Integer>> feeds, int p) {
        ArrayList<Integer> feed2 = new ArrayList<Integer>();
        ArrayList<Integer> ep = new ArrayList<Integer>();
        int end = 0;
        int len = com.size();
        for(int i=0;i<len;i++) {
            end += feeds.get(com.get(i)).get(2);
            ep.add(end);
        }
        int k;
        if(end%p != 0) {
            k = (end/p + 1) * p;
        } else {
            k = end;
        }
        for(int i=0;i<len;i++) {
            int ans = (k-ep.get(i)) / p;
            feed2.add(ans);
        }
        return feed2;
    }

    //    Function to find active feeds
    static ArrayList<ArrayList<Integer>> active_feed(ArrayList<Integer> com, ArrayList<ArrayList<Integer>> feeds) {
        int len = com.size();
        for(int i=0;i<len;i++) {
            int k = com.get(i);
            if(feeds.get(k).size() < 4)
                feeds.get(k).add(1);
        }
        return feeds;
    }

    //    Functions to aggregate
    static int[][] create_output(int[][] input, ArrayList<ArrayList<Integer>> feeds,
                                 ArrayList<ArrayList<Integer>> component_W,
                                 ArrayList<ArrayList<Integer>> W_sub, int[] A,int times, int max_len) {
//        seek sum
        int len = feeds.size();
        for(int i=0;i<len;i++) {
            if(feeds.get(i).size() != 3) {
                int sum = 0;
                if(component_W.get(i).get(0) == -1) {
                    for(int j=feeds.get(i).get(1);j<feeds.get(i).get(1)+feeds.get(i).get(2);j++) {
                        sum += A[j];
                    }
                } else {
                    int l = component_W.get(i).size();
                    for(int j=0;j<l;j++) {
                        int k = (max_len + times - W_sub.get(i).get(j))%max_len;
                        sum = sum + input[component_W.get(i).get(j)][k];
                    }
                }
                input[i][times] = sum;
            }
        }
        return input;

//        seek max
//        int len = feeds.size();
//        for(int i=0;i<len;i++) {
//            if(feeds.get(i).size() != 3) {
//                int sum = 0;
//                if(component_W.get(i).get(0) == -1) {
//                    for(int j=feeds.get(i).get(1);j<feeds.get(i).get(1)+feeds.get(i).get(2);j++) {
//                        sum = Math.max(sum, A[j]);
//                    }
//                } else {
//                    int l = component_W.get(i).size();
//                    for(int j=0;j<l;j++) {
//                        int k = (max_len + times - W_sub.get(i).get(j))%max_len;
//                        sum = Math.max(sum, input[component_W.get(i).get(j)][k]);
//                    }
//                }
//                input[i][times] = sum;
//            }
//        }
//        return input;
    }

//    MCQA main function
    static void MCQA(ArrayList<Integer> W, int p, String filename) {
        int len = W.size();
        Integer[] R = Rset(W, p);
        ArrayList<ArrayList<Integer>> feeds = first_Declaration(R,p);
        int Iw_len = feeds.size();

        ArrayList<ArrayList<Integer>> component_W = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> W_sub = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> Window_index = new ArrayList<>();
        int sum = 0;
        ArrayList<Integer> k = new ArrayList<>();
        k.add(-1);
        for(int i=0;i<Iw_len;i++) {
            component_W.add(k);
            W_sub.add(k);
        }
        for (int i = 0; i < len; i++) {
            ArrayList<Integer> com = window_component(W.get(i), feeds, p);
            feeds = add_feeds(com, feeds, p);
            sum += com.size() -1;
            component_W = add_component(component_W, com, feeds, p);
            if(W.get(i)%p == 0 && i != len-1) {
                Window_index.add(component_W.size()-2);
            } else {
                Window_index.add(component_W.size()-1);
            }
            feeds = active_feed(com, feeds);
        }
        feeds.get(feeds.size()-1).add(1);

        for(int i=Iw_len;i<component_W.size();i++) {
            ArrayList<Integer> sub = sub_component(component_W.get(i), feeds, p);
            W_sub.add(sub);
        }

        int l = 0;
        for(int i=Iw_len;i<W_sub.size();i++) {
            if(W_sub.get(i).get(0) > l) l= W_sub.get(i).get(0);
        }
        final int max_len = l+1;
        final int final_len = feeds.size();
        int[][] agg = new int[final_len][max_len];
//        int[][] square_agg = new int[final_len][max_len];
        float[] ans = new float[len];
        int[] r_data = new int[p];
//        int[] square_r_data = new int[p];
        int times = 0;
        long time_sum = 0;

        for(int i=0;i<p;i++) {
            r_data[i] = 0;
//            square_r_data[i] = 0;
        }
        int input_times = p;
        int kk = 0;
        long kkk = 0;
        try {
            //FileInputStreamを使用し、ファイルの内容を1行ずつ表示するJavaサンプルコード
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), Charset.forName("UTF-8")));
            String text;
            String[] data;
            while ((text = br.readLine()) != null) {
                int input_int;
                data = text.split("\\s+");
                input_int = Integer.parseInt(data[3]);
                if(kk%p == 0) {
                    long start_time = System.nanoTime();
                    agg = create_output(agg, feeds, component_W, W_sub, r_data, times, max_len);
//                    square_agg = create_output(square_agg, feeds, component_W, W_sub, square_r_data, times, max_len);
                    times = (times+1)%max_len;
                    kkk += 1;
                    long end_time = System.nanoTime();
                    time_sum += end_time - start_time;
                    for(int i=0;i<len;i++) {
                        int raw_sum = agg[Window_index.get(i)][times];
//                        int square_sum = square_agg[Window_index.get(i)][times];
//                        ans[i] = (float) square_sum/W.get(i) - (float) Math.pow(raw_sum/W.get(i), 2);
                        ans[i] = agg[Window_index.get(i)][times];;
                    }
                }
//                if(kkk%10 == 1) {
//                    for(int i=0;i<len;i++) {
//                        System.out.print(agg[Window_index.get(i)][times] + " ");
//                    }
//                    System.out.println();
//                }
                kk += 1;
                r_data[kk%p] = input_int;
//                square_r_data[kk%p] = input_int*input_int;
            }
        } catch (Exception ex) {}
        System.out.println();
        System.out.println("MCQA_result：" + time_sum/kkk + "μs");
//        float quot = (float) time_sum/1000000000;
//        int throughput = (int) (kkk*len/quot);
//        System.out.println("MCQA_result : " + throughput + "tuple/s");
    }
}
