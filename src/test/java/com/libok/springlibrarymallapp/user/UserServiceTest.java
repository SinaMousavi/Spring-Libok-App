package com.libok.springlibrarymallapp.user;

import com.libok.springlibrarymallapp.dto.UserDto;
import com.libok.springlibrarymallapp.dto.UserMapper;
import com.libok.springlibrarymallapp.model.order.Order;
import com.libok.springlibrarymallapp.model.user.User;
import com.libok.springlibrarymallapp.model.user.UserRole;
import com.libok.springlibrarymallapp.repository.OrderRepository;
import com.libok.springlibrarymallapp.repository.UserRepository;
import com.libok.springlibrarymallapp.repository.UserRoleRepository;
import com.libok.springlibrarymallapp.service.UserService;
import com.libok.springlibrarymallapp.verification.MailService;
import com.libok.springlibrarymallapp.verification.TokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.libok.springlibrarymallapp.service.UserService.DEFAULT_ROLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    TokenRepository tokenRepository;
    @Mock
    UserRoleRepository roleRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    MailService mailService;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    @Test
    void addWithDefaultRoleTest() {
        //given
        User user = new User();
        user.setEmail("testmail@mail.com");
        user.setPassword("password");
        UserRole userRole = new UserRole();
        userRole.setRole("ROLE_USER");
        given(userRepository.findByEmailOpt("testmail@mail.com")).willReturn(Optional.empty());
        given(roleRepository.findByRole(DEFAULT_ROLE)).willReturn(userRole);
        given(passwordEncoder.encode(user.getPassword())).willReturn("***HASHED_PASSWORD***");

        //when
        userService.addWithDefaultRole(user);

        //then;
        then(userRepository).should().findByEmailOpt("testmail@mail.com");
        then(roleRepository).should().findByRole(DEFAULT_ROLE);
        then(passwordEncoder).should().encode("password");
        then(userRepository).should().save(user);
    }

    @Test
    void findAllTest() {
        //given
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setEmail("testmail@mail.com");
        user.setPassword("password");
        users.add(user);
        given(userRepository.findAll()).willReturn(users);

        //when
        List<UserDto> userDtoList = userService.findAll();

        //then
        then(userRepository).should().findAll();
        assertThat(userDtoList
                    .stream()
                    .map(UserMapper::toEntity)
                    .collect(Collectors.toList()))
                .isEqualTo(users);
    }

    @Test
    void findByIdTest() {
        //given
        Long id = 1L;
        User userEntity = new User();
        userEntity.setFirstname("test");
        given(userRepository.findById(id)).willReturn(Optional.of(userEntity));

        //when
        UserDto user = userService.findById(id);

        //then
        then(userRepository).should().findById(id);
        assertThat(user).isNotNull();
        assertThat(user.getFirstname()).isEqualTo("test");
    }

    @Test
    void findAllByNameOrAuthorTest() {
        //given
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("testmail@mail.com");
        user.setPassword("password");
        users.add(user);
        given(userRepository.findAllByNameOrLastName("Doe")).willReturn(users);

        //when
        List<UserDto> userDtoList = userService.findAllByNameOrAuthor("Doe");

        //then
        then(userRepository).should().findAllByNameOrLastName("Doe");
        assertThat(userDtoList
                .stream()
                .map(UserMapper::toEntity)
                .collect(Collectors.toList()))
                .isEqualTo(users);
    }
    @Test
    void delete() {
        //given
        Long id = 1L;
        User userEntity = new User();
        List<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);
        orders.add(order1);
        orders.add(order2);
        userEntity.setOrders(orders);
        given(userRepository.findById(id)).willReturn(Optional.of(userEntity));

        //when
        userService.delete(id);

        //then
        then(orderRepository).should().deleteById(order1.getId());
        then(orderRepository).should().deleteById(order2.getId());
        then(userRepository).should().deleteById(id);

    }

    @Test
    void update() {
        //given
        Long id = 1L;
        User userEntity = new User();
        userEntity.setFirstname("James");
        userEntity.setLastname("Bond");
        User userToUpdate = new User();
        userToUpdate.setFirstname("Jimmy");
        userToUpdate.setLastname("Bond");
        userToUpdate.setEmail("test@test.com");
        userToUpdate.setPassword("password");
        given(userRepository.findById(id)).willReturn(Optional.of(userEntity));
        given(passwordEncoder.encode(userToUpdate.getPassword())).willReturn("Password encoded");

        //when
        userService.update(userToUpdate,id);

        //then
        userToUpdate.setPassword("Password encoded");
        then(userRepository).should().save(userToUpdate);
    }
}