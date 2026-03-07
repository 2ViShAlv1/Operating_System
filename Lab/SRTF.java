import java.util.Scanner;

public class SRTF {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] at = new int[n];
        int[] bt = new int[n];
        int[] rt = new int[n];
        int[] ct = new int[n];
        int[] tat = new int[n];
        int[] wt = new int[n];

        for (int i = 0; i < n; i++) {
            System.out.println("Process " + (i + 1) + ":");
            System.out.print("Arrival Time: ");
            at[i] = sc.nextInt();

            System.out.print("Burst Time: ");
            bt[i] = sc.nextInt();

            rt[i] = bt[i];
        }

        int completed = 0, time = 0;
        int minIndex = -1;
        int minRemaining;
        boolean found;

        while (completed < n) {

            minRemaining = Integer.MAX_VALUE;
            found = false;

            for (int i = 0; i < n; i++) {
                if (at[i] <= time && rt[i] > 0 && rt[i] < minRemaining) {
                    minRemaining = rt[i];
                    minIndex = i;
                    found = true;
                }
            }

            if (!found) {
                time++;
                continue;
            }

            rt[minIndex]--;
            time++;

            if (rt[minIndex] == 0) {
                completed++;
                ct[minIndex] = time;
                tat[minIndex] = ct[minIndex] - at[minIndex];
                wt[minIndex] = tat[minIndex] - bt[minIndex];
            }
        }

        double totalWT = 0, totalTAT = 0;

        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT");

        for (int i = 0; i < n; i++) {
            totalWT += wt[i];
            totalTAT += tat[i];

            System.out.println("P" + (i + 1) + "\t" +
                    at[i] + "\t" +
                    bt[i] + "\t" +
                    ct[i] + "\t" +
                    tat[i] + "\t" +
                    wt[i]);
        }

        System.out.println("\nAverage Waiting Time = " + (totalWT / n));
        System.out.println("Average Turnaround Time = " + (totalTAT / n));

        sc.close();
    }
}