package com.bty.neobilliardsclubmanager.neobilliardsclubmanagerinsys.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeProfileUpdateRequest {

    Long id;

    @Size(min = 1, max = 250, message = "Họ tên nhân viên có ít nhất 1 và nhiều nhất 250 ký tự")
    String fullName;

    Boolean isMale;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dateOfBirth;

    @Size(min = 1, max = 250, message = "Địa chỉ nhân viên có ít nhất 1 và nhiều nhất 250 ký tự")
    String address;

    @Size(max = 15, message = "Số điện thoại có tối đa 15 ký tự")
    @Pattern(
            regexp = "^[0-9]+$",
            message = "Chỉ được nhập ký tự số"
    )
    String phoneNumber;

    @Size(min = 12, max = 12, message = "Số CCCD có đúng 12 ký tự")
    @Pattern(
            regexp = "^[0-9]+$",
            message = "Chỉ được nhập ký tự số"
    )
    String citizenIDNumber;

    //    @Size(min = 3, max = 254, message = "Email nhân viên có ít nhất 3 và nhiều nhất 254 ký tự")
    @Email(message = "Email không đúng")
    String email;
}
