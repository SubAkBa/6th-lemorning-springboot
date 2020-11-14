package com.example.demo.user;

import com.example.demo.creditcard.CreditCardInfo;
import com.example.demo.customOrder.CustomOrder;
import com.example.demo.order.Order;
import com.example.demo.product.Product;
import com.example.demo.point.Point;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "auth")
    private String auth;

    private LocalDate birthday;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String nickname;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CreditCardInfo> creditCardInfos = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CustomOrder> customOrders = new ArrayList<>();

    private int pointSum;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Point point;

    // 연관관계의 종속자
    @OneToMany(mappedBy = "user") // 반대쪽 변수 명을 적는다.
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Product> buyingProducts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Product> sellingProducts = new ArrayList<>();

    public User(String email){
        this.email = email;
        this.point = new Point(0);
    }

    @Builder
    public User(String email, String password, String auth, int point, LocalDate birthday, Gender gender, String nickname) {
        this.email = email;
        this.password = password;
        this.auth = auth;
        this.point = new Point(point);
        this.birthday = birthday;
        this.gender = gender;
        this.nickname = nickname;
    }

    // 연관관계 메소드
    public void addCreditCardInfo(CreditCardInfo creditCardInfo) {
        this.creditCardInfos.add(creditCardInfo);
        creditCardInfo.setUser(this);
    }

    public User(String email, Point point) {
            this.email = email;
            this.point = point;
            this.pointSum += point.getPointAmount();
        }

    // 사용자의 권한을 콜렉션 형태로 반환
    // 단, 클래스 자료형은 GrantedAuthority를 구현해야함
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : auth.split(",")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    // 사용자의 id를 반환 (unique한 값)
    @Override
    public String getUsername() {
        return email;
    }

    // 사용자의 password를 반환
    @Override
    public String getPassword() {
        return password;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되었는지 확인하는 로직
        return true; // true -> 잠금되지 않았음
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true; // true -> 만료되지 않았음
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return true; // true -> 사용 가능
    }
}