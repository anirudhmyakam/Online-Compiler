package com.data.onlinecompiler.services;

import com.data.onlinecompiler.Dtos.CodeFileDto;
import com.data.onlinecompiler.Dtos.OutputDto;
import com.data.onlinecompiler.configurations.CodeExecWebSocketHandler;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HelperFunctionsService {


    public void saveFile(CodeFileDto codeFile,String folderPath){

        File newFolder = new File(folderPath);

        // Check if the folder already exists
        if (!newFolder.exists()) {
            // If the folder does not exist, try to create it
            boolean success = newFolder.mkdirs(); // Use mkdirs() to create parent directories if needed

            if (success) {
                System.out.println("Folder created successfully: " + folderPath);
            } else {
                System.err.println("Failed to create folder: " + folderPath);
                // Handle the error appropriately, e.g., log it, throw an exception
            }
        } else {
            System.out.println("Folder already exists: " + folderPath);
        }


        String filePath = folderPath + File.separator + codeFile.getFileName();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(codeFile.getCode());
            System.out.println("Python file saved successfully at " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving Python file: " + e.getMessage());
        }

    }

    public void createContainer(String containerName, String folderPath,CodeFileDto codeFile){

        String pathToBind;
        if (folderPath != null && !folderPath.isEmpty()) {
            pathToBind = folderPath;
        } else {
            pathToBind = System.getProperty("user.dir");
        }

        String volumeMapping;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            volumeMapping = pathToBind.replace("\\", "/") + ":/app";
        } else {
            volumeMapping = pathToBind + ":/app";
        }

        Map<String, String> images = new HashMap<>();
        images.put("python","python:3.11-slim");
        images.put("java","openjdk:17-jdk-slim");

        String[] command = {
                "docker", "run", "-dit",
                "--name", containerName,
                "-v", volumeMapping,
                "-w", "/app",
                images.get(codeFile.getLanguage())
        };


        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();

            // Output from the Docker command
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String line;
//            System.out.println("Docker Output:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void compileCode(CodeFileDto codeFile, String folder, String containerName){
        String[] command = {
                "docker","exec","-i",
                containerName,"javac", "/app/"+codeFile.getFileName()
        };

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();

            // Output from the Docker command
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
//            System.out.println("Compilation Output:");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isContainerPresent(String containerName) {
        try {
            // Build the Docker command
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "docker", "ps", "-a",
                    "--filter", "name=^/" + containerName + "$",
                    "--format", "{{.Names}}"
            );

            // Start the process
            Process process = processBuilder.start();

            // Read output from the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals(containerName)) {
                    return true; // Container found
                }
            }

            // Wait for the process to finish
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // Container not found
    }

    public OutputDto exicuteCodeWithTestCases(CodeFileDto codeFile, String folder, String containerName){

//        compile the code first
        if(codeFile.getLanguage().equals("java")){
            this.compileCode(codeFile,folder,containerName);
        }

        Map<String,String[]> commands = new HashMap<>();
        commands.put("python", new String[]{"docker", "exec", "-i", containerName, "python", "/app/" + codeFile.getFileName(),});
        commands.put("java", new String[]{"docker", "exec", "-i", containerName, "java", "-cp", "/app", codeFile.getFileName().split("\\.")[0],});
        ProcessBuilder processBuilder = new ProcessBuilder(commands.get(codeFile.getLanguage()));
        Process process = null;

            try {
                process = processBuilder.start();

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(process.getOutputStream())
                );

                for (String input : codeFile.getInputs()) {
                    writer.write(input + "\n");
                }
                writer.flush();
                writer.close();


                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream())
                );
                String line;
                String ternimalOutput = "";
                while ((line = reader.readLine()) != null) {
//                    System.out.println(line+"\n");
                    ternimalOutput = ternimalOutput.concat("\n" + line);
                }

                BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(process.getErrorStream())
                );

                String terminalError = "";
//                System.out.println("ERROR (if any):");
                while ((line = errorReader.readLine()) != null) {
//                    System.err.println(line);
                    terminalError = terminalError.concat("\n" + line);
                }

                int exitCode = process.waitFor();
                System.out.println("Exited with code: " + exitCode);

                return OutputDto.builder()
                        .output(ternimalOutput)
                        .exitCode(exitCode)
                        .errorMessage(terminalError)
                        .build();


            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return OutputDto.builder()
                        .output("")
                        .exitCode(-1)
                        .errorMessage(e.getMessage())
                        .build();
            }

    }

    private String containername;
    private String fileName;
    private String language;

    public String getContainername() {
        return containername;
    }

    public String getFileName() {
        return fileName;
    }

    public String getLanguage(){
        return language;
    }

    public void exicuteCodeWithTerminal(CodeFileDto codeFile, String folder, String containerName){


        if(codeFile.getLanguage().equals("java")){
            this.compileCode(codeFile,folder,containerName);
        }

        containername = containerName;
        fileName=codeFile.getFileName();
        language=codeFile.getLanguage();

    }

    public void removeContainer(String containerName){

        String[] command = {
                "docker", "rm", "-f", containerName,
        };


        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();

            // Output from the Docker command
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String line;
//            System.out.println("Docker Output:");
            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
            }
//            System.out.println("Docker container closed");
            int exitCode = process.waitFor();
//            System.out.println("Exited with code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
