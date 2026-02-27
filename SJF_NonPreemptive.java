import java.util.*;

public class SJF_NonPreemptive {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        int[] AT = new int[n];
        int[] BT = new int[n];
        int[] CT = new int[n];
        int[] TAT = new int[n];
        int[] WT = new int[n];
        boolean[] completed = new boolean[n];

        for (int i = 0; i < n; i++) {
            System.out.print("Enter Arrival Time for P" + (i + 1) + ": ");
            AT[i] = sc.nextInt();

            System.out.print("Enter Burst Time for P" + (i + 1) + ": ");
            BT[i] = sc.nextInt();
        }

        int current_time = 0;
        int completed_count = 0;

        ArrayList<Integer> order = new ArrayList<>();
        ArrayList<Integer> times = new ArrayList<>();
        times.add(0);

        while (completed_count < n) {

            int min_bt = Integer.MAX_VALUE;
            int index = -1;

            for (int i = 0; i < n; i++) {
                if (AT[i] <= current_time && !completed[i] && BT[i] < min_bt) {
                    min_bt = BT[i];
                    index = i;
                }
            }

            if (index == -1) {
                current_time++;
            } else {
                order.add(index);

                current_time += BT[index];

                CT[index] = current_time;
                TAT[index] = CT[index] - AT[index];
                WT[index] = TAT[index] - BT[index];

                completed[index] = true;
                completed_count++;

                times.add(current_time);
            }
        }

        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT");

        double totalTAT = 0, totalWT = 0;

        for (int i = 0; i < n; i++) {
            System.out.println("P" + (i + 1) + "\t" + AT[i] + "\t" + BT[i] +
                    "\t" + CT[i] + "\t" + TAT[i] + "\t" + WT[i]);

            totalTAT += TAT[i];
            totalWT += WT[i];
        }

        System.out.println("\nGantt Chart:");

        for (int i = 0; i < order.size(); i++) {
            System.out.print("|  P" + (order.get(i) + 1) + "  ");
        }
        System.out.println("|");

        for (int t : times) {
            System.out.print(t + "\t");
        }

        System.out.printf("\n\nAverage Turnaround Time = %.2f", totalTAT / n);
        System.out.printf("\nAverage Waiting Time = %.2f\n", totalWT / n);

        sc.close();
    }
}