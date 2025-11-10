package com.data.onlinecompiler.configurations;

import com.data.onlinecompiler.services.HelperFunctionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class CodeExecWebSocketHandler extends TextWebSocketHandler {


    private Process process;
    private BufferedWriter writer;
    private WebSocketSession session;
    private static int userCnt=1;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        System.out.println("Connected to " + session.getRemoteAddress());
        session.sendMessage(new TextMessage("HERE IS THE CONTAINER NAME$_$User"+userCnt));
    }


    public void executeCode(WebSocketSession session, String containerName, String fileName, String language) throws Exception {

        Map<String,String[]> commands = new HashMap<>();
        commands.put("python", new String[]{"docker", "exec", "-i", containerName, "python", "/app/" + fileName,});
        commands.put("java", new String[]{"docker", "exec", "-i", containerName, "java", "-cp", "/app", fileName.split("\\.")[0],});
        ProcessBuilder processBuilder = new ProcessBuilder(commands.get(language));

        try{
//            ProcessBuilder builder = new ProcessBuilder("docker", "exec", "-i", "something", "python", "/app/" + "main.py");
            process = processBuilder.start();
//            System.out.println("the python code is running");
        }
        catch(Exception e){
            System.out.println("failed to start python");
            e.printStackTrace();
        }


        writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

        // STDOUT
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
//                    System.out.println("  "+line);
                    try{
                        session.sendMessage(new TextMessage(line));
                    }
                    catch(Exception e){
                        System.out.println("failed to send message");
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // STDERR
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    try{
                        session.sendMessage(new TextMessage("  " + line));
                    }
                    catch(Exception e){
                        System.out.println("failed to send message");
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Wait for process to exit and send exit code
        new Thread(() -> {
            try {
                int exitCode = process.waitFor(); // This will block until the process exits
//                System.out.println("Python process exited with code: " + exitCode);
                try {
                    session.sendMessage(new TextMessage("[EXIT] Process exited with code: " + exitCode));
                } catch (Exception e) {
                    System.out.println("Failed to send exit code message");
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                System.out.println("Waiting for process was interrupted");
                e.printStackTrace();
            }
        }).start();

//        System.out.println("the code exited");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Receive input from frontend and send to process
        String messageValve = message.getPayload();
        String[] parameters = messageValve.split(" ");
        if("start-execution".equals(parameters[0])){
            for(String s:parameters){
                System.out.println(s);
            }
            this.executeCode(session,parameters[1],parameters[2],parameters[3]);
            return;
        }
//        System.out.println("input recived");
        try{
//            System.out.println("in the input "+message.getPayload());
            writer.write(message.getPayload() + "\n");
            writer.flush();
        }
        catch(Exception e){
            System.out.println("failed to reciev message");
            e.printStackTrace();
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (writer != null) writer.close();
        if (process != null) process.destroy();
        session.close();
    }
}
