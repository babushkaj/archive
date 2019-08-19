package com.kotenkov.client.console_input;

import java.util.Scanner;

public class InputHandler {

    public static final String WORDS_PAT = "[A-Za-z]+";

    Scanner s;

    public InputHandler() {
        s = new Scanner(System.in);
    }

    public int enterInt(int from, int to){
        s.reset();
        int number = -1;

        while (!(number >= from && number <= to)) {
            System.out.println("\nEnter number from " + from + " to " + to + ":");
            while (!s.hasNextInt()) {
                s.next();
                System.out.println("\nEnter number from " + from + " to " + to + ":");
            }

            number = s.nextInt();

            if (number >= from && number <= to) {
                break;
            }
        }

        return number;
    }

    public double enterDouble(double from, double to){
        s.reset();
        double number = -1;

        while (!(number >= from && number <= to)) {
            System.out.println("\nEnter number from " + from + " to " + to + ":");
            while (!s.hasNextDouble()) {
                s.next();
                System.out.println("\nEnter number from " + from + " to " + to + ":");
            }

            number = s.nextDouble();

            if (number >= from && number <= to) {
                break;
            }
        }

        return number;
    }

    public String enterWords(){
        StringBuilder sb = new StringBuilder();

        System.out.println("\nEnter a correct word:");
        while (!(s.hasNext(WORDS_PAT))) {
            s.next();
            System.out.println("\nEnter a correct word:");
        }

        sb.append(s.next());

        return sb.toString();
    }

}
