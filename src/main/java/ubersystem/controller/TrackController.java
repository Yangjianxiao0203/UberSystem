package ubersystem.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ubersystem.Result.ResponseStatus;
import ubersystem.Result.Result;
import ubersystem.pojo.Track;
import ubersystem.service.TrackService;

@CrossOrigin
@RestController
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@RequestMapping("track")
public class TrackController {
    @Autowired
    TrackService trackService;

    @PostMapping("/create")
    public Result<Track> createTrack(@RequestBody Track track) {
        Track createdTrack = trackService.createTrack(track);
        if(createdTrack!=null) {
            return new Result<>(ResponseStatus.SUCCESS.getCode(), "track created", createdTrack);
        } else {
            return new Result<>(ResponseStatus.FAILURE.getCode(), "track creation failed", null);
        }
    }
}
