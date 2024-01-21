package dozun.mini.controllers;

import dozun.mini.dtos.SubjectDTO;
import dozun.mini.model.ResponseObject;
import dozun.mini.services.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/subject")
public class SubjectController {

    private SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/view")
    public ResponseEntity<ResponseObject> viewStudent() {
        return subjectService.viewSubject();
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> create(@RequestBody SubjectDTO newSubject){
        return subjectService.createSubject(newSubject);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseObject> update(@RequestBody SubjectDTO updatedSubject){
        return subjectService.updateSubject(updatedSubject);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> delete(@RequestBody SubjectDTO deletedSubject){
        return subjectService.deleteSubject(deletedSubject);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> search(@RequestBody SubjectDTO keyword){
        return subjectService.searchSubject(keyword);
    }

}
