package com.server.smallgroup.Controller;

import com.server.smallgroup.DTO.*;
import com.server.smallgroup.ResponseEntityBuilder;
import com.server.smallgroup.Service.GroupService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GroupContorller {
    private final GroupService groupService;

    @GetMapping("/group/{group_id}")
    public ResponseEntity<Map<String, Object>> getGroupInfo(@PathVariable Long group_id){
        GroupInfoDTO info = this.groupService.getGroupInfo(group_id);
        return ResponseEntityBuilder.buildSuccessResponseWithInfo(HttpStatus.OK, "group", info);
    }

    @PostMapping(value="/group", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, Object>> insertGroup(HttpServletRequest request,
                                                         @Valid @RequestPart MakeGroupDTO info, BindingResult bindingResult,
                                                         @RequestPart MultipartFile image) throws IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            return ResponseEntityBuilder.buildErrorResponse("세션이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        if(bindingResult.hasErrors()){
            return ResponseEntityBuilder.buildBindingErrorResponse(bindingResult);
        }

        Long user_id = Long.valueOf(session.getAttribute("id").toString());
        this.groupService.insertGroup(user_id, info.getName(), info.getHobby(), info.getMinage(), info.getMaxage(), info.getAdmit(),
                info.getAddress(), info.getMessage(), info.getKeyword(), image);
        return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
    }

    @PatchMapping(value="/group", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Map<String, Object>> revisionGroup(HttpServletRequest request,
                                                           @Valid @RequestPart MakeGroupDTO info, BindingResult bindingResult,
                                                           @RequestPart MultipartFile image) throws IOException {
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            return ResponseEntityBuilder.buildErrorResponse("세션이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        if(bindingResult.hasErrors()){
            return ResponseEntityBuilder.buildBindingErrorResponse(bindingResult);
        }

        this.groupService.revisionGroup(info.getId(), info.getName(), info.getHobby(), info.getMinage(), info.getMaxage(),
                info.getAdmit(), info.getAddress(), info.getMessage(), info.getKeyword(), image);
        return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
    }

    @DeleteMapping(value="/group/{group_id}")
    public ResponseEntity<Map<String, Object>> deleteGroup(HttpServletRequest request, @PathVariable Long group_id) throws Exception {
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            return ResponseEntityBuilder.buildErrorResponse("세션이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        Long user_id = Long.valueOf(session.getAttribute("id").toString());
        this.groupService.deleteGroup(group_id, user_id);
        return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
    }

    @GetMapping(value = "/mygroup/{page}")
    public ResponseEntity<Map<String, Object>> getMyGruop(@PathVariable Integer page, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            return ResponseEntityBuilder.buildErrorResponse("세션이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        Long user_id = Long.valueOf(session.getAttribute("id").toString());
        Page<SimpleGroupInfoDTO> list = this.groupService.myGroups(user_id, page);
        return ResponseEntityBuilder.buildSuccessResponseWithPages(HttpStatus.OK, list);
    }

    @PostMapping(value = "/mygroup/{group_id}")
    public ResponseEntity<Map<String, Object>> registerGroup(HttpServletRequest request, @PathVariable Long group_id) throws Exception {
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            return ResponseEntityBuilder.buildErrorResponse("세션이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        Long user_id = Long.valueOf(session.getAttribute("id").toString());
        this.groupService.registerGroup(group_id, user_id);
        return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
    }

    @DeleteMapping(value = "/mygroup/{group_id}")
    public ResponseEntity<Map<String, Object>> leaveGroup(HttpServletRequest request, @PathVariable Long group_id) throws Exception {
        HttpSession session = request.getSession();
        if(session.getAttribute("id")==null){
            return ResponseEntityBuilder.buildErrorResponse("세션이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
        Long user_id = Long.valueOf(session.getAttribute("id").toString());
        this.groupService.leaveGroup(group_id, user_id);
        return ResponseEntityBuilder.buildSuccessResponse(HttpStatus.OK);
    }

    @GetMapping(value = "/groupimage/{group_id}")
    public UrlResource getGroupImage(@PathVariable Long group_id) throws MalformedURLException {
        String path = this.groupService.getImage(group_id);
        if(path == null)
            return null;
        else
            return new UrlResource("file:"+path);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<Map<String, Object>> findGroup(HttpServletRequest request, @PathVariable String keyword,
                                                         @RequestBody FilterInfoDTO info) throws Exception {
        HttpSession session = request.getSession();
        if(keyword.equals("none")){
            List<FindGroupDTO> list = this.groupService.randomGroups(4);
            return ResponseEntityBuilder.buildSuccessResponseWithInfos(HttpStatus.OK, "group", list);
        }else{
            Long user_id = null;
            if(info.getLocation() && session.getAttribute("id")==null){
                return ResponseEntityBuilder.buildErrorResponse("세션이 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
            }else if(info.getLocation()){
                user_id = Long.valueOf(session.getAttribute("id").toString());
            }
            Page<FindGroupDTO> list = this.groupService.findGroup(keyword, info, user_id);
            return ResponseEntityBuilder.buildSuccessResponseWithPages(HttpStatus.OK, list);
        }
    }
}
