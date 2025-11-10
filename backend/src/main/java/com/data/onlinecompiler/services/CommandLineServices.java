package com.data.onlinecompiler.services;

import org.springframework.stereotype.Service;
import java.io.*;

@Service
public class CommandLineServices {

    public void setNewContainer(){
//        String currentDir = System.getProperty("user.dir");
//
//        String customPath = "C:\\Users\\Anirudh\\Desktop\\onlinecompiler\\compilerpy";
//        String pathToBind;
//        if (customPath != null && !customPath.isEmpty()) {
//            pathToBind = customPath;
//        } else {
//            pathToBind = System.getProperty("user.dir");
//        }
//
//        String volumeMapping;
//        String os = System.getProperty("os.name").toLowerCase();
//        if (os.contains("win")) {
//            volumeMapping = pathToBind.replace("\\", "/") + ":/app";
//        } else {
//            volumeMapping = pathToBind + ":/app";
//        }
//
//        String[] command = {
//                "docker", "run", "-dit",
//                "--name", "compilerpy",
//                "-v", volumeMapping,
//                "-w", "/app",
//                "python:3.11-slim"
//        };
//
//        ProcessBuilder processBuilder = new ProcessBuilder(command);
//        processBuilder.redirectErrorStream(true);
//
//        try {
//            Process process = processBuilder.start();
//
//            // Output from the Docker command
//            BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(process.getInputStream())
//            );
//            String line;
//            System.out.println("Docker Output:");
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            int exitCode = process.waitFor();
//            System.out.println("Exited with code: " + exitCode);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void runPythonWithInputs() {
//        String[] command = {
//                "docker", "exec", "-i", "py1", "python", "/app/py1.py"
//        };
//
//        ProcessBuilder processBuilder = new ProcessBuilder(command);
//        Process process = null;
//
//        try {
//            process = processBuilder.start();
//
//            BufferedWriter writer = new BufferedWriter(
//                    new OutputStreamWriter(process.getOutputStream())
//            );
//
//            writer.write("4\n");
//            writer.write("a\n");
//            writer.write("b\n");
//            writer.write("c\n");
//            writer.write("d\n");
//            writer.flush();
//            writer.close();
//
//
//            BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(process.getInputStream())
//            );
//            String line;
//            System.out.println("OUTPUT:");
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            BufferedReader errorReader = new BufferedReader(
//                    new InputStreamReader(process.getErrorStream())
//            );
//            System.out.println("ERROR (if any):");
//            while ((line = errorReader.readLine()) != null) {
//                System.err.println(line);
//            }
//
//            int exitCode = process.waitFor();
//            System.out.println("Exited with code: " + exitCode);
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
    }

}
