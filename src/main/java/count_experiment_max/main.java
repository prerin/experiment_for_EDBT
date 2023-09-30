package count_experiment;

import java.util.*;

import static count_experiment.FlatFit.FlatFit;
import static count_experiment.LBiX.LBiX;
import static count_experiment.MCQA.MCQA;
import static count_experiment.proposed.Proposed;


public class main {
    static void main_test(int arg) {
        ArrayList<Integer> W = new ArrayList<>();
        int s = 10;

//        experiment with  varying window size
        W.add(35*arg);
        W.add(65*arg);
        W.add(75*arg);
        W.add(100*arg);
        W.add(110*arg);
        W.add(160*arg);
        W.add(205*arg);
        W.add(220*arg);
        W.add(235*arg);
        W.add(280*arg);

//        experiment with  varying number of query
//        int ad = 10;
//        List<Integer> target = new ArrayList<>(Arrays.asList(10,15,20,25,30,35,40,45,50));
//        for(int i=0;i<arg;i++) {
//            Collections.shuffle(target);
//            ad += target.get(0);
//            W.add(ad);
//        }

        System.out.println("n = " + arg);
        for(int i=0;i<W.size();i++) {
            System.out.print(W.get(i) + " ");
        }
        System.out.println();

        String filename = "DEBS2012-ChallengeData 2.txt";
        FlatFit(W, s, filename);
        LBiX(W, s, filename);
        MCQA(W, s, filename);
        Proposed(W, s, filename);
    }
    public static void main(String[] args) {

//        experiment with  varying window size
//        for(int i=0;i<5;i++) main_test(10);
//        for(int i=0;i<5;i++) main_test(20);
//        for(int i=0;i<5;i++) main_test(50);
//        for(int i=0;i<5;i++) main_test(100);
//        for(int i=0;i<5;i++) main_test(500);
//        for(int i=0;i<5;i++) main_test(1000);

//        experiment with  varying numbers of query
//        for(int i=0;i<2;i++) main_test(1);
//        for(int i=0;i<5;i++) main_test(5);
//        for(int i=0;i<5;i++) main_test(10);
//        for(int i=0;i<5;i++) main_test(20);
//        for(int i=0;i<5;i++) main_test(50);
//        for(int i=0;i<5;i++) main_test(100);
        main_test(10);
    }
}
