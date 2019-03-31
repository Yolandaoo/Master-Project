/*
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {//注意while处理多个case
            int a = in.nextInt();
            int b = in.nextInt();
            System.out.println(a + b);
        }
    }
}
*/

import java.util.Scanner;
public class Main {

    public int lcm(int[] n){
        int result = 0;
        for(int i = 0; i < n.length; i++){
            int a = n[i], b = n[i+1];
            int temp = a % b;
            while(temp != 0){
                a = b;
                b = temp;
                temp = a % b;
            }
            result = n[i] * n[i+1] / n[i+1];
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in .nextInt();
        /*int ans = 0, x;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                x = sc.nextInt();
                ans += x;
            }
        }
        System.out.println(ans);*/
        if(n >= 1 && n <= 1000000){
            int m = 1;
            for(int i = n; i < 1000000; i++){
                int[] a = {n, };
            }
        }
    }
}

