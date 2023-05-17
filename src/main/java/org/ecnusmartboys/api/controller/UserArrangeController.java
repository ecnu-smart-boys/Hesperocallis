package org.ecnusmartboys.api.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.ecnusmartboys.api.annotation.AuthRoles;
import org.ecnusmartboys.application.dto.*;
import org.ecnusmartboys.application.dto.request.command.*;
import org.ecnusmartboys.application.dto.response.Response;
import org.ecnusmartboys.domain.service.ConsulvisorService;
import org.ecnusmartboys.domain.service.StaffService;
import org.ecnusmartboys.domain.service.UserService;
import org.ecnusmartboys.domain.service.VisitorService;
import org.ecnusmartboys.infrastructure.exception.BadRequestException;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.ecnusmartboys.domain.service.UserService.*;

@Slf4j
@RestController
@RequestMapping("/user_arrange")
@Api(tags = "用户管理接口")
public class UserArrangeController {

    private final UserService userService;

    private final ConsulvisorService consulvisorService;

    private final StaffService staffService;

    private final VisitorService visitorService;

    public UserArrangeController(UserService userService, ConsulvisorService consulvisorService, StaffService staffService, VisitorService visitorService) {
        this.userService = userService;
        this.consulvisorService = consulvisorService;
        this.staffService = staffService;
        this.visitorService = visitorService;
    }


    @AuthRoles(ROLE_ADMIN)
    @ApiOperation("获取咨询师列表")
    @GetMapping("/consultants")
    public Response<ConsultantsDTO> getConsultants(@RequestBody @Validated UserListReq req) {
        List<ConsultantInfo> consultantInfoList = new ArrayList<>();
        var consultants = userService.getUsers(req, ROLE_CONSULTANT);
        consultants.forEach( v -> {
            ConsultantInfo consultantInfo = new ConsultantInfo();
            BeanUtils.copyProperties(v, consultantInfo);

            consultantInfo.setSupervisors(consulvisorService.getSupervisors(consultantInfo.getId()));
            consultantInfo.setStaff(staffService.getById(consultantInfo.getId()));
            // TODO 累计咨询次数，咨询时间，平均评价
            consultantInfo.setConsultNum(0);
            consultantInfo.setAccumulatedTime(0L);
            // TODO 排班

            consultantInfoList.add(consultantInfo);
        });
        return Response.ok(new ConsultantsDTO(consultantInfoList, userService.getUserCount(ROLE_CONSULTANT)));
    }

    @AuthRoles(ROLE_ADMIN)
    @ApiOperation("获取督导列表")
    @GetMapping("/supervisors")
    public Response<SupervisorsDTO> getSupervisors(@RequestBody @Validated UserListReq req) {
        List<SupervisorInfo> supervisorInfoList = new ArrayList<>();
        var supervisors = userService.getUsers(req, ROLE_SUPERVISOR);
        supervisors.forEach( v -> {
            SupervisorInfo supervisorInfo = new SupervisorInfo();
            BeanUtils.copyProperties(v, supervisorInfo);

            supervisorInfo.setConsultants(consulvisorService.getConsultants(supervisorInfo.getId()));
            supervisorInfo.setStaff(staffService.getById(supervisorInfo.getId()));
            // TODO 累计咨询次数，咨询时间
            supervisorInfo.setConsultNum(0);
            supervisorInfo.setAccumulatedTime(0L);
            // TODO 排班

            supervisorInfoList.add(supervisorInfo);
        });
        return Response.ok(new SupervisorsDTO(supervisorInfoList, userService.getUserCount(ROLE_SUPERVISOR)));
    }

    @AuthRoles(ROLE_ADMIN)
    @ApiOperation("获取访客列表")
    @GetMapping("/visitors")
    public Response<VisitorsDTO> getVisitors(@RequestBody @Validated UserListReq req) {
        List<VisitorInfo> visitorInfoList = new ArrayList<>();
        var visitors = userService.getUsers(req, ROLE_VISITOR);
        visitors.forEach( v -> {
            VisitorInfo visitorInfo = new VisitorInfo();
            BeanUtils.copyProperties(v, visitorInfo);

            visitorInfo.setVisitor(visitorService.getById(visitorInfo.getId()));
            visitorInfoList.add(visitorInfo);
        });
        return Response.ok(new VisitorsDTO(visitorInfoList, userService.getUserCount(ROLE_VISITOR)));
    }

    @AuthRoles(ROLE_ADMIN)
    @ApiOperation("禁用咨询师")
    @PutMapping("/disable/consultant/{id}")
    public Response<Object> disableConsultant(@PathVariable Long id) {
        userService.disable(id, ROLE_CONSULTANT);
        return Response.ok("成功禁用咨询师");
    }

    @AuthRoles(ROLE_ADMIN)
    @ApiOperation("禁用督导")
    @PutMapping("/disable/supervisor/{id}")
    public Response<Object> disableSupervisor(@PathVariable Long id) {
        userService.disable(id, ROLE_SUPERVISOR);
        return Response.ok("成功禁用督导");
    }

    @AuthRoles(ROLE_ADMIN)
    @ApiOperation("禁用访客")
    @PutMapping("/disable/visitor/{id}")
    public Response<Object> disableVisitor(@PathVariable Long id) {
        userService.disable(id, ROLE_VISITOR);
        return Response.ok("成功禁用访客");
    }

    @AuthRoles(ROLE_ADMIN)
    @ApiOperation("启用访客")
    @PutMapping("/enable/visitor/{id}")
    public Response<Object> enableVisitor(@PathVariable Long id) {
        userService.enable(id, ROLE_VISITOR);
        return Response.ok("成功启用访客");
    }

    @AuthRoles(ROLE_ADMIN)
    @ApiOperation("启用督导")
    @PutMapping("/enable/supervisor/{id}")
    public Response<Object> enableSupervisor(@PathVariable Long id) {
        userService.enable(id, ROLE_SUPERVISOR);
        return Response.ok("成功启用督导");
    }

    @AuthRoles(ROLE_ADMIN)
    @ApiOperation("添加督导")
    @PostMapping("/add/supervisor")
    public Response<Object> addSupervisor(@RequestBody @Validated AddSupervisorReq req) {
        if(userService.getByUsername(req.getUsername()) != null) {
            throw new BadRequestException("该用户名已存在");
        }

        if(userService.getByPhone(req.getPhone()) != null) {
            throw new BadRequestException("该手机号已被注册");
        }
        userService.saveSupervisor(req);
        return Response.ok("成功添加督导");
    }

    @AuthRoles(ROLE_ADMIN)
    @ApiOperation("更新督导")
    @PostMapping("/update/supervisor")
    public Response<Object> updateSupervisor(@RequestBody @Validated UpdateSupervisorReq req) {
        if(userService.getSingleUser(req.getSupervisorId(), ROLE_SUPERVISOR) == null) {
            throw new BadRequestException("该咨询师不存在");
        }
        userService.updateSupervisor(req);
        return Response.ok("成功更新督导");
    }

    @AuthRoles(ROLE_ADMIN)
    @ApiOperation("添加咨询师")
    @PostMapping("/add/consultant")
    public Response<Object> addConsultant(@RequestBody @Validated AddConsultantReq req) {
        if(userService.getByUsername(req.getUsername()) != null) {
            throw new BadRequestException("该用户名已存在");
        }

        if(userService.getByPhone(req.getPhone()) != null) {
            throw new BadRequestException("该手机号已被注册");
        }

        var ids = req.getSuperVisorIds();
        ids.forEach(id -> {
            if(userService.getSingleUser(id, ROLE_SUPERVISOR) == null) {
                throw new BadRequestException("所要绑定的督导不存在");
            }
        });

        userService.saveConsultant(req);
        return Response.ok("成功添加咨询师");
    }

    @AuthRoles(ROLE_ADMIN)
    @ApiOperation("更新咨询师")
    @PostMapping("/update/consultant")
    public Response<Object> updateConsultant(@RequestBody @Validated UpdateConsultantReq req) {
        if(userService.getSingleUser(req.getConsultantId(), ROLE_CONSULTANT) == null) {
            throw new BadRequestException("该咨询师不存在");
        }

        var ids = req.getSuperVisorIds();
        ids.forEach(id -> {
            if(userService.getSingleUser(id, ROLE_SUPERVISOR) == null) {
                throw new BadRequestException("所要绑定的督导不存在");
            }
        });
//        userService.updateConsultant(req); TODO
        return Response.ok("成功更新督导");
    }
}