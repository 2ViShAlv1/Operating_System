import java.util.*;

public class FCFS_Scheduling {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] pid = new int[n];
        int[] at = new int[n];
        int[] bt = new int[n];
        int[] ct = new int[n];
        int[] tat = new int[n];
        int[] wt = new int[n];

        for (int i = 0; i < n; i++) {
            pid[i] = i + 1;
            System.out.print("Enter Arrival Time of P" + pid[i] + ": ");
            at[i] = sc.nextInt();

            System.out.print("Enter Burst Time of P" + pid[i] + ": ");
            bt[i] = sc.nextInt();
        }

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (at[i] > at[j]) {

                    int temp;

                    temp = at[i];
                    at[i] = at[j];
                    at[j] = temp;

                    temp = bt[i];
                    bt[i] = bt[j];
                    bt[j] = temp;

                    temp = pid[i];
                    pid[i] = pid[j];
                    pid[j] = temp;
                }
            }
        }

        int current_time = 0;
        double total_wt = 0;
        double total_tat = 0;


        for (int i = 0; i < n; i++) {

            if (current_time < at[i]) {
                current_time = at[i];   
            }

            ct[i] = current_time + bt[i];
            current_time = ct[i];

            tat[i] = ct[i] - at[i];
            wt[i] = tat[i] - bt[i];

            total_wt += wt[i];
            total_tat += tat[i];
        }

        // 🔹 Output Table
        System.out.println("\nPID\tAT\tBT\tCT\tTAT\tWT");

        for (int i = 0; i < n; i++) {
            System.out.println(
                    "P" + pid[i] + "\t" +
                    at[i] + "\t" +
                    bt[i] + "\t" +
                    ct[i] + "\t" +
                    tat[i] + "\t" +
                    wt[i]
            );
        }

        System.out.printf("\nAverage Waiting Time = %.2f", total_wt / n);
        System.out.printf("\nAverage Turnaround Time = %.2f\n", total_tat / n);

        System.out.println("\nGantt Chart:");
        System.out.print("0 ");

        for (int i = 0; i < n; i++) {
            System.out.print("| P" + pid[i] + " | " + ct[i] + " ");
        }

        sc.close();
    }
}
