package com.data.onlinecompiler.controllers;

import com.data.onlinecompiler.Dtos.CodeFileDto;
import com.data.onlinecompiler.Dtos.ResponceDto;
import com.data.onlinecompiler.services.MainRunService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:4200/")
public class CompilerController {

    private MainRunService mainRunService;

    CompilerController(MainRunService mainRunService) {
        this.mainRunService = mainRunService;
    }

    @PostMapping("/run-testcases/{containerName}")
    public ResponseEntity<?> testCasesRun(@RequestBody CodeFileDto codeFile,@PathVariable String containerName) {

        try{
            return ResponseEntity.ok().body(mainRunService.runTheCodeWithTestCases(codeFile,containerName));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/run-terminal/{containerName}")
    public ResponseEntity<?> terminalRun(@RequestBody CodeFileDto codeFile,@PathVariable String containerName) {
        try{
            mainRunService.runWithTerminal(codeFile,containerName);
            return ResponseEntity.ok().body(new ResponceDto("completed execution"));
        }
        catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/remove-container/{containerName}")
    public ResponseEntity<?> removeContainer(@PathVariable String containerName) {
        try{
            mainRunService.removeContainer(containerName);
            return ResponseEntity.ok().body(new ResponceDto("completed execution"));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        System.out.println("in the testcalled");
        return ResponseEntity.ok().body(new ResponceDto("its working"));
    }
}
