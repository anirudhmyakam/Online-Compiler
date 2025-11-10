package com.data.onlinecompiler.services;

import com.data.onlinecompiler.Dtos.CodeFileDto;
import com.data.onlinecompiler.Dtos.OutputDto;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class MainRunService {

    private HelperFunctionsService helperFunctions;

    public MainRunService(HelperFunctionsService helperFunctionsService) {
        this.helperFunctions = helperFunctionsService;
    }

    public OutputDto runTheCodeWithTestCases(CodeFileDto codeFile, String containerName) {

//        create the folder
//        String baseFolder="/home/ec2-user/onlinecompiler";
        String baseFolder="C:\\Users\\Anirudh\\Desktop\\onlinecompiler";
        String folderPath = baseFolder + File.separator + containerName;


//        fist step is to store the file in the desired folder
        this.helperFunctions.saveFile(codeFile, folderPath);

//        creating the containers in the file folder
        if(!this.helperFunctions.isContainerPresent(containerName)){

            this.helperFunctions.createContainer(containerName, folderPath,codeFile);
        }

//        now we will run the code and return the output
        OutputDto outputs = this.helperFunctions.exicuteCodeWithTestCases(codeFile, folderPath, containerName);

        return outputs;

    }

    public void runWithTerminal(CodeFileDto codeFile, String containerName){
        //        create the folder
        String baseFolder="C:\\Users\\Anirudh\\Desktop\\onlinecompiler";
//        String baseFolder="/home/ec2-user/onlinecompiler";
        String folderPath = baseFolder + File.separator + containerName;

//        create the folder
        this.helperFunctions.saveFile(codeFile, folderPath);

        if(!this.helperFunctions.isContainerPresent(containerName)){

            this.helperFunctions.createContainer(containerName, folderPath,codeFile);
        }

        this.helperFunctions.exicuteCodeWithTerminal(codeFile, folderPath, containerName);

    }

    public void removeContainer(String containerName){
        if(this.helperFunctions.isContainerPresent(containerName)){
            this.helperFunctions.removeContainer(containerName);
        }
    }

}
