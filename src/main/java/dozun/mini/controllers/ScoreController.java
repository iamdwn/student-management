package dozun.mini.controllers;

import dozun.mini.dtos.ScoreDTO;
import dozun.mini.model.ResponseObject;
import dozun.mini.services.ScoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/score")
public class ScoreController {

    private ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/view")
    public ResponseEntity<ResponseObject> viewScore(){
        return scoreService.viewScore();
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> create(@RequestBody ScoreDTO newScore){
        return scoreService.createScore(newScore);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseObject> update(@RequestBody ScoreDTO newScore){
        return scoreService.updateScore(newScore);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> delete(@RequestBody ScoreDTO newScore){
        return scoreService.deleteScore(newScore);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseObject> search(@RequestBody ScoreDTO info){
        return scoreService.searchScore(info);
    }

    @GetMapping("/search/{subject}")
    public ResponseEntity<ResponseObject> searchBySubjectCode(@PathVariable("subject") String subject){
        return scoreService.searchBySubjectCode(subject);
    }

}
