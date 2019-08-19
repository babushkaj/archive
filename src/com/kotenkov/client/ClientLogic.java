package com.kotenkov.client;

import com.kotenkov.client.console_input.InputHandler;

import java.io.IOException;
import java.net.Socket;

public class ClientLogic {

    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputHandler ih = new InputHandler();
        Socket client = new Socket();
        ArchiveClient archiveClient = new ArchiveClient(client);
        String menu = splitInputText(archiveClient.getMessage());

        while(true){
            System.out.println(menu);
            int menuNum = ih.enterInt(1,4);
            switch (menuNum){
                case 1: {
                    archiveClient.sendMessage("1\n");
                    System.out.println(splitInputText(archiveClient.getMessage()));
                    break;
                }
                case 2 : {
                    processOneStudent(ih, archiveClient);
                    break;
                }
                case 3 : {
                    archiveClient.sendMessage("3\n");
                    addNewStudent(ih, archiveClient);
                    break;
                }
            }
            if(menuNum == 4){
                archiveClient.sendMessage("4\n");
                break;
            }
        }

    }

    private static String splitInputText(String inputText){
        StringBuilder sb = new StringBuilder();
        sb.append("==============================\n");
        String [] s = inputText.split("\\$\\$\\$");
        for (String str : s) {
            sb.append(str);
            sb.append("\n");
        }
        sb.append("==============================");
        sb.append("\n");
        return sb.toString();
    }

    private static void processOneStudent(InputHandler ih, ArchiveClient archiveClient){
        while(true) {
            System.out.println("==============================");
            System.out.println("Select action:");
            System.out.println("1. Select user by \'id\'.");
            System.out.println("2. Back.");
            System.out.println("==============================");
            int menuNum = ih.enterInt(1, 2);
            if(menuNum == 1) {
                while (true) {
                    System.out.println("Enter valid student id or \'0\' to back");
                    int inputId = ih.enterInt(0, Integer.MAX_VALUE);
                    if(inputId != 0){
                        archiveClient.sendMessage("2\n");
                        archiveClient.sendMessage(inputId + "\n");
                        String response = archiveClient.getMessage();
                        if (!response.isEmpty()) {
                            archiveClient.sendMessage("ok\n");
                            System.out.println("Chosen personal file\n");
                            System.out.println(splitInputText(response));
                            editStudent(ih, archiveClient, inputId);
                            menuNum = 2;
                            break;
                        } else {
                            archiveClient.sendMessage("fail\n");
                            System.out.println("There is no user with id = " + inputId);
                            menuNum = 2;
                            break;
                        }
                    } else {
                        menuNum = 2;
                        break;
                    }
                }
            }
            if(menuNum == 2){
                break;
            }
        }

    }

    private static void editStudent(InputHandler ih, ArchiveClient archiveClient, int id){
        while(true) {
            System.out.println("==============================");
            System.out.println("Select action:");
            System.out.println("1. Edit user.");
            System.out.println("2. Back.");
            System.out.println("==============================");
            int menuNum = ih.enterInt(1, 2);
            if(menuNum == 1) {
                System.out.println("Enter firstname (or \'skip\'):");
                String firstname = ih.enterWords();
                System.out.println("Enter surname (or \'skip\'):");
                String surname = ih.enterWords();
                System.out.println("Enter AVGmark (or \'0\' to skip):");
                double avgMark = ih.enterDouble(0, 10);
                StringBuilder sb = new StringBuilder();
                sb.append(id);
                sb.append("$$$");
                sb.append(firstname);
                sb.append("$$$");
                sb.append(surname);
                sb.append("$$$");
                sb.append(avgMark);
                sb.append("\n");
                archiveClient.sendMessage(sb.toString());
                menuNum = 2;
                }
            if(menuNum == 2){
                break;
            }
        }
    }

    private static void addNewStudent(InputHandler ih, ArchiveClient archiveClient){
        System.out.println("Enter firstname (or \'back\' to back):");
        java.lang.String firstname = ih.enterWords();
        if(firstname.equalsIgnoreCase("back")){
            archiveClient.sendMessage("back\n");
            return;
        }
        System.out.println("Enter surname (or \'back\' to back):");
        String surname = ih.enterWords();
        if(surname.equalsIgnoreCase("back")){
            archiveClient.sendMessage("back\n");
            return;
        }
        System.out.println("Enter AVGmark (or \'0\' to back):");
        double avgMark = ih.enterDouble(0.1, 10);
        if(avgMark == 0){
            archiveClient.sendMessage("back\n");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(firstname);
        sb.append("$$$");
        sb.append(surname);
        sb.append("$$$");
        sb.append(avgMark);
        sb.append("\n");
        archiveClient.sendMessage(sb.toString());
    }

}
