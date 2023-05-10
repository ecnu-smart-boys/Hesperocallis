package org.ecnusmartboys.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecnusmartboys.mapstruct.UserInfoMapper;
import org.ecnusmartboys.model.dto.UserInfo;
import org.ecnusmartboys.model.entity.User;
import org.ecnusmartboys.model.entity.Visitor;
import org.ecnusmartboys.model.request.WxRegisterReq;
import org.ecnusmartboys.repository.StaffRepository;
import org.ecnusmartboys.repository.UserRepository;
import org.ecnusmartboys.repository.VisitorRepository;
import org.ecnusmartboys.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserRepository, User> implements UserService, InitializingBean {

    private final VisitorRepository visitorRepository;

    private final StaffRepository staffRepository;

    private final UserInfoMapper userInfoMapper;

    @Override
    @Transactional
    public User saveVisitor(WxRegisterReq req) {
        User user = new User();
        BeanUtils.copyProperties(req, user);
        user.setRoles(Collections.singletonList(ROLE_VISITOR));
        getBaseMapper().insert(user);

        Visitor visitor = new Visitor();
        visitor.setId(user.getId());
        visitor.setEmergencyContact(req.getEmergencyContact());
        visitor.setEmergencyPhone(req.getEmergencyPhone());
        visitorRepository.insert(visitor);

        return user;
    }

    @Override
    public UserInfo getUserInfo(Long id) {
        if (id == null) {
            return null;
        }
        var user = getById(id);
        if (user == null) {
            return null;
        }
        var userInfo = userInfoMapper.toDto(user);
        if (user.getRoles() != null) {
            // 假设访客与咨询师督导互斥
            if (user.getRoles().contains(ROLE_VISITOR)) {
                userInfo.setVisitor(visitorRepository.selectById(id));
            } else {
                userInfo.setStaff(staffRepository.selectById(id));
            }
        }

        return userInfo;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 创建超级管理员
        var wrapper = new QueryWrapper<User>().like("roles", ROLE_ADMIN);
        if (getBaseMapper().selectCount(wrapper) == 0) {
            var user = new User();
            user.setName("弗洛伊德");
            user.setAvatar("https://ts1.cn.mm.bing.net/th/id/R-C.45b3a4f888e913e1ada56e2950bbd193?rik=ScD5TlSiXveZ9A&riu=http%3a%2f%2fwww.cuimianxinli.com%2fupload%2f2016-12%2f16122009305407.jpg&ehk=%2fzLL0q3fqB7F%2b8YM4OpdDSd33tZowsGpwk0VLco4p7g%3d&risl=&pid=ImgRaw&r=0");
            user.setAge(167);
            user.setGender(0);
            user.setUsername("freud_admin");
            var rowpw = "freud_admin" + System.currentTimeMillis();
            user.setPassword(BCrypt.hashpw(rowpw));
            user.setRoles(Collections.singletonList(ROLE_ADMIN));
            getBaseMapper().insert(user);
            log.info("创建超级管理员成功，用户名：{}，密码：{}", user.getUsername(), rowpw);
        }
    }
}
