package org.ecnusmartboys.infrastructure.convertor;

import org.ecnusmartboys.domain.model.user.*;
import org.ecnusmartboys.infrastructure.data.mysql.StaffInfoDO;
import org.ecnusmartboys.infrastructure.data.mysql.UserDO;
import org.ecnusmartboys.infrastructure.data.mysql.VisitorInfoDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = BaseConvertor.COMPONENT_MODEL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConvertor {
    User toUser(UserDO userDO);
    Admin toAdmin(UserDO userDO);
    Visitor toVisitor(UserDO userDO, VisitorInfoDO visitorInfoDO);
    Consultant toConsultant(UserDO userDO, StaffInfoDO staffInfoDO);
    Supervisor toSupervisor(UserDO userDO, StaffInfoDO staffInfoDO);
    UserDO toUserDO(User user);
    VisitorInfoDO toVisitorInfoDO(Visitor visitor);
    StaffInfoDO toStaffInfoDO(Consultant consultant);
    StaffInfoDO toStaffInfoDO(Supervisor supervisor);
}
