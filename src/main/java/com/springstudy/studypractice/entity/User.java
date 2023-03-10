package com.springstudy.studypractice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springstudy.studypractice.controller.dto.SignUpRequestDto;
import com.springstudy.studypractice.controller.dto.UserInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
// lombok을 사용하면 반복되는 코드를 줄일 수 있고, 필드를 추가할 때 getter, setter, 생성자를 수정하지 않아도 되어 편리한 기능이다.
// 하지만 주의해서 사용해야 한다.
@Getter // getter (lombok)
@Builder // builder 패턴 (lombok)
@NoArgsConstructor // 기본 생성자 (lombok)
@AllArgsConstructor // 모든 필드를 매개변수로 갖는 생성자 (lombok)
public class User implements UserDetails {

    @Id // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 값 생성 전략 -> MySQL에서 AUTO_INCREMENT인 경우 보통 IDENTITY
    @Column(name = "user_id") // 데이터베이스 테이블의 'user_id' column과 매핑
    private Long id;

    @NotBlank // Spring Validation - null, "", " " 모두 불가능
    @Column(unique = true) // UNIQUE 설정
    private String username;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Email // Spring Validation - Email 형식 검증
    private String email;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$") // Java 정규식을 사용한 전화번호 형식 검증
    private String phone;

    @Min(value = 14) // Spring Validation - 최소 14 이상이어야 한다.
    @Max(value = 120) // Spring Validation - 최대 120 이하여야 한다.
    private Integer age;

    @Enumerated(value = EnumType.STRING) // 열거형(Enum)의 이름을 String 타입으로 저장
    @Builder.Default // builder 패턴을 사용할 때 해당 필드의 값을 지정하지 않으면 null이 아닌 아래의 값이 default
    private Membership membership = Membership.BASIC;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    // CollectionUserRepository 사용 시 필요
    public void setId(Long id) {
        this.id = id;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public static User of(SignUpRequestDto signUpRequestDto, PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(signUpRequestDto.getUsername())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .email(signUpRequestDto.getEmail())
                .phone(signUpRequestDto.getPhone())
                .age(signUpRequestDto.getAge())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }

    public UserInfoResponseDto toDto() {
        return UserInfoResponseDto.builder()
                .username(this.username)
                .email(this.email)
                .phone(this.phone)
                .age(this.age)
                .membership(membership)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public boolean isEnabled() {
        return true;
    }
}
