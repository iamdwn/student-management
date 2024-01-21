package dozun.mini.controllers;

import dozun.mini.dtos.StudentDTO;
import dozun.mini.model.ResponseObject;
import dozun.mini.services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/view")
    public ResponseEntity<ResponseObject> view(){
        return studentService.viewStudent();
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> create(@RequestBody StudentDTO newStudent){
        return studentService.createStudent(newStudent);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseObject> update(@RequestBody StudentDTO updatedStudent){
        return studentService.updateStudent(updatedStudent);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> delete(@RequestBody StudentDTO deletedStudent){
        return studentService.deleteStudent(deletedStudent);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> search(@RequestBody StudentDTO keyword) {
        return studentService.searchStudent(keyword);
    }
}
