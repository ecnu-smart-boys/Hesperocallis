package org.ecnusmartboys.api.interceptor;

import lombok.RequiredArgsConstructor;
import org.ecnusmartboys.api.Extractor;
import org.ecnusmartboys.api.annotation.AnonymousAccess;
import org.ecnusmartboys.api.annotation.AuthRoles;
import org.ecnusmartboys.domain.repository.UserRepository;
import org.ecnusmartboys.infrastructure.mapper.UserMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * 接口访问权限拦截器，用于权限校验。
 */
@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserMapper userRepository;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            if (method.hasMethodAnnotation(AnonymousAccess.class)) {
                return true;
            }
            // 不加AnonymousAccess注解表示登录才可以访问
            var common = Extractor.extract(request);
            String userId = common.getUserId();
            if(userId == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            // 如果没有AnonymousAccess注解，必须登录
            var user = userRepository.selectById(userId);
            if (user == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            var authRoles = method.getMethodAnnotation(AuthRoles.class);
            if (authRoles != null && authRoles.value().length > 0) {
                String[] permissions = authRoles.value();
                if (!Arrays.asList(permissions).contains(user.getRole())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

    }
}
