package com.data.onlinecompiler.controllers;

import com.data.onlinecompiler.services.CommandLineServices;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PythonControllers {

    private CommandLineServices commandLineServices;
    PythonControllers(CommandLineServices commandLineServices) {
        this.commandLineServices = commandLineServices;
    }

    @GetMapping("/setcontainer")
    public ResponseEntity<?> setContainer() {
        try{
            this.commandLineServices.setNewContainer();
            return ResponseEntity.ok(Map.of("message","Set container successfully"));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/runprog")
    public ResponseEntity<?> runProgram() {
        try {
            this.commandLineServices.runPythonWithInputs();
            return ResponseEntity.ok(Map.of("message", "Run program successfully"));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
