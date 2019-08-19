package com.kotenkov.xml_processing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;

public class XMLProcessing {

    private  final  String FILE_NAME = "archive.xml";

    static int id;
    DocumentBuilderFactory dbf;
    DocumentBuilder documentBuilder;
    Document document;
    File file;

    public XMLProcessing() throws ParserConfigurationException {
        dbf = DocumentBuilderFactory.newInstance();
        documentBuilder = dbf.newDocumentBuilder();
        file = new File((FILE_NAME));
    }

    public void readDataFromFile(){
        try {
            if (file.exists()) {
                try {
                    document = documentBuilder.parse(file);
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        document.getDocumentElement().normalize();
        id = initializeIdCounter();
    }

    private void writeDataToFile(){
        DOMSource src = src = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        Transformer trf = null;
        try {
            trf = TransformerFactory.newInstance()
                    .newTransformer();
            trf.setOutputProperty(OutputKeys.INDENT, "yes");
            trf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            trf.transform(src, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public String getAllNames(){
        StringBuilder sb = new StringBuilder();
        Element root = document.getDocumentElement();
        NodeList studentsList = root.getElementsByTagName("student");
        if(studentsList.getLength() != 0){
            for (int i = 0; i < studentsList.getLength(); i++) {
                if(studentsList.item(i).getNodeType() == Node.ELEMENT_NODE){
                    Element student = (Element)studentsList.item(i);
                    sb.append(student.getAttribute("id"));
                    sb.append(". ");
                    sb.append(student.getElementsByTagName("firstname").item(0).getTextContent());
                    sb.append(" ");
                    sb.append(student.getElementsByTagName("surname").item(0).getTextContent());
                    sb.append("$$$");
                }
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    public String getStudentById(int id){
        StringBuilder sb = new StringBuilder();
        Element root = document.getDocumentElement();
        NodeList studentsList = root.getElementsByTagName("student");
        if(studentsList.getLength() != 0) {
            for (int i = 0; i < studentsList.getLength(); i++) {
                if (studentsList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element student = (Element) studentsList.item(i);
                    int studentId = Integer.parseInt(student.getAttribute("id"));
                    if(studentId == id){
                        sb.append("Id: " + studentId);
                        sb.append("$$$Firstname: ");
                        sb.append(student.getElementsByTagName("firstname").item(0).getTextContent());
                        sb.append("$$$Surname: ");
                        sb.append(student.getElementsByTagName("surname").item(0).getTextContent());
                        sb.append("$$$AVGmark: ");
                        sb.append(student.getElementsByTagName("avgmark").item(0).getTextContent());
                    }
                }
            }
        }

        sb.append("\n");
        return sb.toString();
    }

    public void editStudent(String studentString){
        String [] studentData = studentString.split("\\$\\$\\$");
        int newStudentId = Integer.parseInt(studentData[0]);

        Element root = document.getDocumentElement();
        NodeList studentsList = root.getElementsByTagName("student");
        if(studentsList.getLength() != 0) {
            for (int i = 0; i < studentsList.getLength(); i++) {
                if (studentsList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element student = (Element) studentsList.item(i);
                    int xmlStudentId = Integer.parseInt(student.getAttribute("id"));
                    if(newStudentId == xmlStudentId){
                        if(!studentData[1].trim().equalsIgnoreCase("skip")) {
                            student.getElementsByTagName("firstname").item(0).setTextContent(studentData[1].trim());
                        }
                        if(!studentData[2].trim().equalsIgnoreCase("skip")){
                            student.getElementsByTagName("surname").item(0).setTextContent(studentData[2].trim());
                        }
                        double avgMark = Double.parseDouble(studentData[3].trim());
                        if(avgMark != 0){
                            student.getElementsByTagName("avgmark").item(0).setTextContent(studentData[3].trim());
                        }
                        System.out.println("Everything is OK!");
                        writeDataToFile();
                    }
                }
            }
        }
    }

    public void addNewStudent(String studentString){
        String [] studentData = studentString.split("\\$\\$\\$");

        Element root = document.getDocumentElement();
        Node students = root.getElementsByTagName("students").item(0);
        if (students.getNodeType() == Node.ELEMENT_NODE) {
            Element stud = (Element) students;
            Element newStudent = (Element)stud.appendChild(document.createElement("student"));
            newStudent.setAttribute("id", String.valueOf(++id));
            Element firstname = (Element)newStudent.appendChild(document.createElement("firstname"));
            firstname.setTextContent(studentData[0].trim());
            Element surname = (Element)newStudent.appendChild(document.createElement("surname"));
            surname.setTextContent(studentData[1].trim());
            Element avgMark = (Element)newStudent.appendChild(document.createElement("avgmark"));
            System.out.println("Student has been added.");
            avgMark.setTextContent(studentData[2].trim());
        }
        writeDataToFile();
    }

    private int initializeIdCounter(){
        int initId = id;
        Element root = document.getDocumentElement();
        NodeList studentsList = root.getElementsByTagName("student");
        if(studentsList.getLength() != 0) {
            for (int i = 0; i < studentsList.getLength(); i++) {
                if (studentsList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element student = (Element) studentsList.item(i);
                    int studentId = Integer.parseInt(student.getAttribute("id"));
                    if(studentId > initId){
                        initId = studentId;
                    }
                }
            }
        }
        return initId;
    }

}
