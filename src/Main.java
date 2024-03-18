import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Band of the Hour");
        System.out.println("-------------------------------");

        // Create a class of BandAssignmentManager
        BandAssignmentManager manager = new BandAssignmentManager(scanner);
        char userChoice;
        do {
            System.out.print("\n (A)dd, (R)emove, (P)rint, e(X)it : ");
            userChoice = scanner.nextLine().charAt(0);

            switch (userChoice) {
                case 'A':
                case 'a':
                    manager.addMusician(scanner);
                    break;
                case 'R':
                case 'r':
                    manager.removeMusician(scanner);
                    break;
                case 'P':
                case 'p':
                    manager.printCurrentAssignment();
                    break;
                case 'X':
                case 'x':
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("ERROR: Invalid option, try again.");
            }
        } while (userChoice != 'X' && userChoice != 'x');
    }
}

class BandAssignmentManager {
    private double[][] weightAssignments; // 2D array to store musician weights
    private int numRows; // Number of rows in the band
    private int[] numPositionsInRow; // Array to store the number of positions in each row

    // Constructor to initialize the BandAssignmentManager
    public BandAssignmentManager(Scanner scanner) {
        System.out.print("Please enter number of rows: ");
        numRows = scanner.nextInt();
        scanner.nextLine();
        while (numRows < 1 || numRows > 10) {
            System.out.print("ERROR: Out of range, try again: ");
            numRows = scanner.nextInt();
            scanner.nextLine();
        }

        numPositionsInRow = new int[numRows];
        weightAssignments = new double[numRows][];
        for (int i = 0; i < numRows; i++) {
            System.out.print("Enter number of positions in row " + (char) ('A' + i) + ": ");
            numPositionsInRow[i] = scanner.nextInt();
            scanner.nextLine();
            while (numPositionsInRow[i] < 1 || numPositionsInRow[i] > 8) {
                System.out.print("ERROR: Out of range, try again: ");
                numPositionsInRow[i] = scanner.nextInt();
                scanner.nextLine();
            }
            weightAssignments[i] = new double[numPositionsInRow[i]];
        }
    }

    // Method to add a musician to a position
    public void addMusician(Scanner scanner) {
        char rowLetter;
        int positionNumber;
        double weight;

        // Prompt the user to enter the row letter
        do {
            System.out.print("Please enter row letter: ");
            rowLetter = Character.toUpperCase(scanner.nextLine().charAt(0));
        } while (rowLetter < 'A' || rowLetter >= ('A' + numRows));

        int totalPositions = numPositionsInRow[rowLetter - 'A'];
        double totalWeight = 0.0;
        int occupiedPositions = 0;

        // Calculate the total weight and occupied positions in the specified row
        for (int i = 0; i < totalPositions; i++) {
            totalWeight += weightAssignments[rowLetter - 'A'][i];
            if (weightAssignments[rowLetter - 'A'][i] > 0) {
                occupiedPositions++;
            }
        }

        // Prompt the user to enter the position number
        do {
            System.out.print("Please enter position number (1 to " + totalPositions + "): ");
            positionNumber = Integer.parseInt(scanner.nextLine());
        } while (positionNumber < 1 || positionNumber > totalPositions);

        // Check if the position is already occupied
        if (weightAssignments[rowLetter - 'A'][positionNumber - 1] > 0) {
            System.out.println("ERROR: There is already a musician there.");
            return; // Exit the method
        }

        // Prompt the user to enter the weight of the musician
        do {
            System.out.print("Please enter weight (45.0 to 200.0): ");
            weight = Double.parseDouble(scanner.nextLine());

            // Check if adding the musician would exceed the weight limit for the row
            if (totalWeight + weight > 100.0 * totalPositions) {
                System.out.println("ERROR: Adding this musician would exceed the weight limit for the row.");
                System.out.println("Please enter weight within the allowed range.");
            }
        } while (weight < 45.0 || weight > 200.0 || totalWeight + weight > 100.0 * totalPositions);

        // Add the weight of the musician to the specified position
        weightAssignments[rowLetter - 'A'][positionNumber - 1] = weight;
    //Success message
        System.out.println("****** Musician added.\n");
    }

    // Method to remove a musician from a position
    public void removeMusician(Scanner scanner) {
        System.out.print("Please enter row letter: ");
        char rowLetter = scanner.nextLine().toUpperCase().charAt(0);
        int row = rowLetter - 'A';
        while (row < 0 || row >= numRows) {
            System.out.print("ERROR: Invalid input, try again: ");
            rowLetter = scanner.nextLine().toUpperCase().charAt(0);
            row = rowLetter - 'A';
        }

        // Prompt the user to enter the position number
        System.out.print("Please enter position number (1 to " + numPositionsInRow[row] + "): ");
        int position = Integer.parseInt(scanner.nextLine()) - 1;
        while (position < 0 || position >= numPositionsInRow[row]) {
            System.out.print("ERROR: Invalid input, try again: ");
            position = Integer.parseInt(scanner.nextLine()) - 1;
        }

        // Check if the position is vacant
        if (weightAssignments[row][position] == 0) {
            System.out.println("ERROR: That position is vacant.");
            return;
        }

        // Remove the weight of the musician from the specified position
        weightAssignments[row][position] = 0;
        System.out.println("****** Musician removed.");
    }

    // Method to print the current assignment of musicians
    public void printCurrentAssignment() {

        for (char rowLetter = 'A'; rowLetter < 'A' + numRows; rowLetter++) {
            System.out.print(rowLetter + ": ");
            double totalWeight = 0.0;
            int totalPositions = numPositionsInRow[rowLetter - 'A'];
            int occupiedPositions = 0;

            for (int i = 0; i < totalPositions; i++) {
                if (weightAssignments[rowLetter - 'A'][i] > 0) {
                    System.out.printf("%5.1f   ", weightAssignments[rowLetter - 'A'][i]);
                    totalWeight += weightAssignments[rowLetter - 'A'][i];
                    occupiedPositions++;
                } else {
                    System.out.print("  0.0   ");
                }
            }
            //Calculate average weight
            double averageWeight;
            if (totalPositions > 0) {
                averageWeight = totalWeight / totalPositions;
            } else {
                averageWeight = Double.NaN;
            }

            System.out.printf("[ %5.1f, %5.1f ]%n", totalWeight, averageWeight);
        }
    }

}

