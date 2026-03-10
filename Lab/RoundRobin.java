import java.util.Scanner;

public class RoundRobin {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int n, tq;
        int time = 0, remain;
        int done;

        System.out.print("Enter number of processes: ");
        n = sc.nextInt();

        int[] at = new int[n];
        int[] bt = new int[n];
        int[] rt = new int[n];
        int[] wt = new int[n];
        int[] tat = new int[n];
        int[] ct = new int[n];

        for (int i = 0; i < n; i++) {
            System.out.println("Process P" + (i + 1));

            System.out.print("Arrival Time: ");
            at[i] = sc.nextInt();

            System.out.print("Burst Time: ");
            bt[i] = sc.nextInt();

            rt[i] = bt[i];
        }

        System.out.print("Enter Time Quantum: ");
        tq = sc.nextInt();

        remain = n;

        System.out.println("\nGantt Chart:");
        System.out.print(time);

        while (remain != 0) {

            done = 0;

            for (int i = 0; i < n; i++) {

                if (rt[i] > 0 && at[i] <= time) {

                    done = 1;

                    if (rt[i] <= tq) {

                        time = time + rt[i];

                        System.out.print(" | P" + (i + 1) + " | " + time);

                        rt[i] = 0;
                        ct[i] = time;

                        wt[i] = ct[i] - bt[i] - at[i];

                        remain--;

                    } else {

                        time = time + tq;

                        System.out.print(" | P" + (i + 1) + " | " + time);

                        rt[i] = rt[i] - tq;

                    }
                }
            }

            if (done == 0) {
                time++;
            }
        }

        double avgWT = 0, avgTAT = 0;

        System.out.println("\n\nProcess\tAT\tBT\tCT\tWT\tTAT");

        for (int i = 0; i < n; i++) {

            tat[i] = bt[i] + wt[i];

            avgWT += wt[i];
            avgTAT += tat[i];

            System.out.println("P" + (i + 1) + "\t" + at[i] + "\t" + bt[i] + "\t" + ct[i] + "\t" + wt[i] + "\t" + tat[i]);
        }

        System.out.println("\nAverage Waiting Time = " + (avgWT / n));
        System.out.println("Average Turnaround Time = " + (avgTAT / n));

        sc.close();
    }
}