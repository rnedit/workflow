package kz.spring.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class WorkflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowApplication.class, args);
    }

}
