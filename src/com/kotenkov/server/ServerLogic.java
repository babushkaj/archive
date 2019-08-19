package com.kotenkov.server;

import com.kotenkov.xml_processing.XMLProcessing;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.ServerSocket;

public class ServerLogic {

    public static final String MENU_TEXT = "Select action:$$$1. Show all students.$$$" +
                                           "2. Edit student.$$$3. Add student.$$$4. Exit.\n";

    public static void main(String[] args) {
        try{
            ServerSocket ss = new ServerSocket(8118);
            ArchiveServer archiveServer = new ArchiveServer(ss);
            StringBuilder sb = new StringBuilder();
            System.out.println("server works");
            archiveServer.sendMessage(MENU_TEXT);
            XMLProcessing xmlProcessing = null;
            try {
                xmlProcessing = new XMLProcessing();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            xmlProcessing.readDataFromFile();
            while(true){
                int menuNum = Integer.parseInt(archiveServer.getMessage());
                switch (menuNum){
                    case 1: {
                        archiveServer.sendMessage(xmlProcessing.getAllNames());
                        break;
                    }
                    case 2 : {
                        int id = Integer.parseInt(archiveServer.getMessage());
                        System.out.println("Try to find one personal file with id = " + id);
                        archiveServer.sendMessage(xmlProcessing.getStudentById(id));
                        String dataConfirmed = archiveServer.getMessage();
                        if(dataConfirmed.equalsIgnoreCase("ok")){
                            System.out.println("File is found.");
                            String stData = archiveServer.getMessage();
                            System.out.println(stData);
                            xmlProcessing.editStudent(stData);
                        }else{
                            System.out.println("File not found.");
                        }
                        break;
                    }
                    case 3 : {
                        System.out.println("add new file");
                        String dataFromClient = archiveServer.getMessage();
                        if(!(dataFromClient.equalsIgnoreCase("back"))){
                            xmlProcessing.addNewStudent(dataFromClient);
                        }
                        break;
                    }
                }
                if(menuNum == 4){
                    break;
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
