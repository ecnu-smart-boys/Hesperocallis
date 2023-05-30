package org.ecnusmartboys.application.dto.request.command;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ecnusmartboys.api.annotation.IdNumber;
import org.ecnusmartboys.api.annotation.Phone;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("添加督导请求")
public class AddSupervisorRequest {

    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z_]+$", message = "用户名只允许输入英文和下划线")
    @Size(max = 32, message = "用户名长度不能超过32个字符")
    private String username;

    @NotNull(message = "密码不能为空")
    private String password;

    @NotNull(message = "名字不能为空")
    @Pattern(regexp = "^[\\p{L}a-zA-Z]{2,32}$", message = "姓名格式不正确")
    private String name;

    @NotNull(message = "性别不能为空")
    @Range(min = 1, max = 2, message = "性别只能取值为 1 或 2")
    private Integer gender;

    @NotNull(message = "年龄不能为空")
    @Range(min = 10, max = 100, message = "年龄必须在10-100之间")
    private Integer age;

    @NotNull(message = "电话号码不能为空")
    @Phone
    private String phone;

    @NotNull(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotNull(message = "身份证号码不能为空")
    @IdNumber
    private String idNumber;

    @NotNull(message = "工作单位不能为空")
    @Size(max = 32, message = "工作单位不能超过32个字符")
    private String department;

    @NotNull(message = "职称不能为空")
    @Size(max = 32, message = "职称不能超过32个字符")
    private String title;

    @NotNull(message = "资质不能为空")
    @Pattern(regexp = "^(1级|2级|3级)$", message = "资质不正确")
    private String qualification;

    @NotNull(message = "资质编号不能为空")
    @Size(max = 255, message = "资质编号不能超过255个字符")
    private String qualificationCode;
}
