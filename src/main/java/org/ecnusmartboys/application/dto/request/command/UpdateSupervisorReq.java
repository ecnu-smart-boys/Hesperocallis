package org.ecnusmartboys.application.dto.request.command;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ApiModel("更新督导请求")
public class UpdateSupervisorReq {

    @NotNull
    private Long supervisorId;

    @NotNull(message = "名字不能为空")
    @Pattern(regexp = "^[\\p{L}a-zA-Z]{2,32}$", message = "姓名格式不正确")
    private String name;

    @NotNull(message = "性别不能为空")
    @Range(min = 1, max = 2, message = "性别只能取值为 1 或 2")
    private Integer gender;

    @NotNull(message = "年龄不能为空")
    @Range(min = 10, max = 100, message = "年龄必须在10-100之间")
    private Integer age;

    @NotNull(message = "邮箱不能为空")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    @NotNull(message = "身份证号码不能为空")
    @Pattern(regexp = "^\\d{17}(\\d|X|x)$", message = "身份证号码格式不正确")
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